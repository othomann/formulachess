package org.formulachess.ui;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import org.formulachess.engine.ChessEngine;
import org.formulachess.pgn.ast.Comment;
import org.formulachess.pgn.ast.Move;
import org.formulachess.pgn.ast.PGNGame;
import org.formulachess.pgn.engine.PGNModel;

public class MoveController extends Composite {

	private static final int COLUMN_COUNT = 3;
	private static final int[] DIMENSIONS = { 22, 18, 1}; // width of one square, height, gap between squares
	Display display;
	boolean initialized;
	private ChessEngine model;
	Move[] moves;
	private Text moveText;
	PGNModel pgnModel;
	Table table;
	int moveIndex;
	private ResourceBundle bundle;
	private Messages currentMessages;

	public MoveController(Composite parent, int style, Locale locale, ImageFactory imageFactory, ChessEngine model) {
		super(parent, style);
		this.initialized = false;
		this.currentMessages = new Messages(locale);
		this.bundle = ResourceBundle.getBundle("org.formulachess.engine.messages", locale); //$NON-NLS-1$
		this.model = model;
		this.display = parent.getShell().getDisplay();
		final Color backgroundColor = this.display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
		final Color foregroundColor = this.display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
		this.setBackground(backgroundColor);

		// layout the component
		FormLayout formLayout = new FormLayout();
		this.setLayout(formLayout);

		Label control = new Label(this, SWT.BORDER);
		control.setImage(imageFactory.control);
		control.addListener (SWT.MouseDown, new Listener () {
			public void handleEvent (Event event) {
				int x = event.x;
				int y = event.y;
				if (insideSquareN(0, x, y)) {
					restartGame();
					return;
				}
				if (insideSquareN(1, x, y)) {
					play5MovesBackward();
					return;
				}
				if (insideSquareN(2, x, y)) {
					playMoveBackward();
					return;
				}
				if (insideSquareN(3, x, y)) {
					playMoveForward();
					return;
				}
				if (insideSquareN(4, x, y)) {
					play5MovesForward();
					return;
				}
				if (insideSquareN(5, x, y)) {
					goToEndGame();
				}
			}
		});


		Group movesGroup = new Group(this, SWT.NONE);
		movesGroup.setText(this.currentMessages.getString("movecontroller.moves.title")); //$NON-NLS-1$
		movesGroup.setBackground(backgroundColor);
		movesGroup.setForeground(foregroundColor);
		movesGroup.setLayout(new FormLayout());

		this.table = new Table(movesGroup, SWT.BORDER);
		this.table.setLinesVisible(true);
		this.table.setHeaderVisible(true);
		String[] titles =
			{
				this.currentMessages.getString("movecontroller.table.header.number"), //$NON-NLS-1$
				this.currentMessages.getString("movecontroller.table.header.white"), //$NON-NLS-1$
				this.currentMessages.getString("movecontroller.table.header.black") //$NON-NLS-1$
			};
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(this.table, SWT.NONE);
			column.setText(titles[i]);
			column.pack();
		}

		this.table.addListener (SWT.MouseDown, new Listener () {
			public void handleEvent (Event event) {
				if (!MoveController.this.initialized) return;
				Rectangle clientArea = MoveController.this.table.getClientArea ();
				Point pt = new Point (event.x, event.y);
				int index = MoveController.this.table.getTopIndex ();
				while (index < MoveController.this.table.getItemCount ()) {
					boolean visible = false;
					TableItem item = MoveController.this.table.getItem (index);
					for (int i=0; i < COLUMN_COUNT; i++) {
						Rectangle rect = item.getBounds (i);
						if (rect.contains (pt)) {
							MoveController.this.pgnModel.playMovesTill(MoveController.this.moves, getMoveIndex(index,i));
							update();
						}
						if (!visible && rect.intersects (clientArea)) {
							visible = true;
						}
					}
					if (!visible) return;
					index++;
				}
			}
		});

		this.table.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
//				System.out.println(e);
			}
		});

		FormData formData2 = new FormData();
		formData2.top = new FormAttachment(0, 2);
		formData2.left = new FormAttachment(0, 2);
		formData2.right = new FormAttachment(100, -2);
		formData2.bottom = new FormAttachment(100, -2);
		this.table.setLayoutData(formData2);

		Group comment = new Group(this, SWT.NONE);
		comment.setText(this.currentMessages.getString("movecontroller.comments.title")); //$NON-NLS-1$
		comment.setBackground(backgroundColor);
		comment.setForeground(foregroundColor);
		comment.setLayout(new FormLayout());

		this.moveText = new Text(comment, SWT.BORDER);

		FormData formData3 = new FormData();
		formData3.left = new FormAttachment(0, 0);
		formData3.right = new FormAttachment(100, 0);
		formData3.top = new FormAttachment(0, 0);
		formData3.bottom = new FormAttachment(100, 0);
		this.moveText.setLayoutData(formData3);

		FormData formData = new FormData();
		formData.left = new FormAttachment(10, 2);
		formData.top = new FormAttachment(0, 2);
		control.setLayoutData(formData);

		FormData formData4 = new FormData();
		formData4.top = new FormAttachment(control, 2);
		formData4.left = new FormAttachment(0, 2);
		formData4.right = new FormAttachment(100, -2);
		formData4.height = 200;
		movesGroup.setLayoutData(formData4);

		FormData formData5 = new FormData();
		formData5.top = new FormAttachment(movesGroup, 2);
		formData5.left = new FormAttachment(0, 2);
		formData5.right = new FormAttachment(100, -2);
		formData5.bottom = new FormAttachment(100, -2);
		comment.setLayoutData(formData5);
	}
	int getMoveIndex(int i, int j) {
		switch(j) {
			case 0 :
			case 1 :
				// white move
				if (this.moves[0].isWhiteMove()) {
					return i * 2;
				}
				int index = i * 2 - 1;
				if (index >= 0) {
					return index;
				}
				break;
			case 2 :
				// black move
				if (this.moves[0].isWhiteMove()) {
					return i * 2 + 1;
				}
				return i * 2;
		}
		return 0;
	}

	private int getRowIndex(int moveIdx) {
		if (this.moves == null) {
			return -1;
		}
		if (this.moves[0].isWhiteMove()) {
			return moveIdx / 2;
		}
		return (moveIdx + 1 ) / 2;
	}

	public void initializeMoveList(PGNGame pgnGame) {
		this.table.removeAll();
		this.pgnModel = new PGNModel(pgnGame, this.model);
		if (pgnGame == null) {
			this.initialized = false;
			return;
		}
		Move[] movesList = pgnGame.getMoveText().getMoves();
		this.moves = movesList;
		if (movesList == null || movesList.length == 0) {
			this.initialized = true;
			return;
		}
		if (movesList[0].isWhiteMove()) {
			for (int i = 0, max = movesList.length; i < max; i += 2) {
				TableItem item = new TableItem (this.table, SWT.READ_ONLY);
				item.setText(0, Integer.toString((i / 2) + 1));
				item.setText(1, movesList[i].getMoveNotation(this.bundle));
				if (i + 1 < max) {
					item.setText(2, movesList[i + 1].getMoveNotation(this.bundle));
				}
			}
			for (int i=0; i< COLUMN_COUNT; i++) {
				TableColumn column = this.table.getColumn(i);
				column.setResizable(false);
				column.pack();
			}
		} else {
			TableItem item = new TableItem (this.table, SWT.READ_ONLY);
			item.setText(0, "1");//$NON-NLS-1$
			item.setText(1, "...");//$NON-NLS-1$
			item.setText(2, movesList[0].getMoveNotation(this.bundle));

			for (int i = 1, max = movesList.length; i < max; i += 2) {
				item = new TableItem (this.table, SWT.READ_ONLY);
				item.setText(0, Integer.toString(((i + 1) / 2) + 1));
				item.setText(1, movesList[i].getMoveNotation(this.bundle));
				if (i + 1 < max) {
					item.setText(2, movesList[i + 1].getMoveNotation(this.bundle));
				}
			}
			for (int i=0; i< COLUMN_COUNT; i++) {
				TableColumn column = this.table.getColumn(i);
				column.setResizable(false);
				column.pack();
			}
		}
		this.initialized = true;
	}

	boolean insideSquareN(int i, int x, int y) {
		if (y >= 0 && y <= DIMENSIONS[1]) {
			if (x >= (i * 22 + 1) && (x <= ((i + 1) * 22 + 1))) {
				return true;
			}
		}
		return false;
	}

	public boolean isReady() {
		if (!this.initialized) return false;
		return this.pgnModel.isReady();
	}

	public void setIsReady(boolean value) {
		if (!this.initialized) return;
		this.pgnModel.setIsReady(value);
	}

	public void restartGame() {
		if (!this.initialized) return;
		this.pgnModel.playMovesTill(MoveController.this.moves, -1);
		update();
	}

	public void goToEndGame() {
		if (!this.initialized) return;
		if (this.moves != null) {
			this.pgnModel.playMovesTill(this.moves, this.moves.length - 1);
		} else {
			this.pgnModel.playMovesTill(this.moves, - 1);
		}
		update();
	}

	public void play5MovesForward() {
		if (!this.initialized) return;
		if (this.moves != null) {
			int length = this.moves.length - 1;
			this.pgnModel.playMovesTill(this.moves, Math.min(this.moveIndex + 10, length));
		}
		update();
	}

	public void play5MovesBackward() {
		if (!this.initialized) return;
		if (this.moves != null) {
			this.pgnModel.playMovesTill(this.moves, Math.max(this.moveIndex - 10, -1));
		}
		update();
	}

	public void playMoveForward() {
		if (!this.initialized) return;
		if (this.moves != null) {
			int length = this.moves.length - 1;
			this.pgnModel.playMovesTill(this.moves, Math.min(this.moveIndex + 1, length));
		}
		update();
	}

	public void playMoveBackward() {
		if (!this.initialized) return;
		if (this.moves != null) {
			this.pgnModel.playMovesTill(this.moves, Math.max(this.moveIndex - 1, -1));
		}
		update();
	}

	public void update() {
		this.moveIndex = MoveController.this.pgnModel.currentMoveCounter;
		// update the table
		if (this.moveIndex >= 0) {
			this.table.setSelection(getRowIndex(this.moveIndex));
		} else {
			this.table.deselectAll();
			this.table.setTopIndex(0);
		}
		if (this.moves == null || this.moveIndex == this.moves.length - 1) {
			this.pgnModel.setIsReady(true);
		} else {
			this.pgnModel.setIsReady(false);
		}
		if (this.moveIndex >= 0 && this.moves != null) {
			final Comment commentText = this.moves[this.moveIndex].getComment();
			if (commentText != null) {
				this.moveText.setText(commentText.toString());
			} else {
				this.moveText.setText(""); //$NON-NLS-1$
			}
		}
	}
}
