package org.formulachess.ui;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import org.formulachess.engine.Piece;
import org.formulachess.engine.Turn;

import static org.formulachess.engine.Turn.*;

public class PromotionDialog {
	private static final Logger MyLogger = Logger.getLogger(PromotionDialog.class.getCanonicalName());
	public Rectangle screenSize;
	private Piece promotionCode;
	private ImageFactory imageFactory;
	private Shell dialog;
	private Display display;
	private Messages currentMessages;

	public PromotionDialog(Shell shell, Turn color, ImageFactory imageFactory, Locale locale) {
		this.dialog = new Shell(shell, SWT.CLOSE | SWT.APPLICATION_MODAL);
		this.currentMessages = new Messages(locale);
		this.dialog.setText(this.currentMessages.getString("dialog.promotion.title")); //$NON-NLS-1$
		this.dialog.setLayout(new FormLayout());
		this.imageFactory = imageFactory;
		this.display = shell.getDisplay();
		initImageButtons(this.dialog, color);
		this.dialog.pack();
	}

	public void open() {
		this.dialog.setVisible(true);
		runEventLoop();
	}

	public void close() {
		this.dialog.close();
		this.dialog.dispose();
	}
	
	public void initImageButtons(Shell shell, final Turn color) {
		Button queen = new Button(shell, SWT.PUSH);
		Button rook = new Button(shell, SWT.PUSH);
		Button knight = new Button(shell, SWT.PUSH);
		Button bishop = new Button(shell, SWT.PUSH);
		queen.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				if (color == WHITE_TURN) {
					setPromotionCode(Piece.WHITE_QUEEN);
				} else {
					setPromotionCode(Piece.BLACK_QUEEN);
				}
				close();
			}
		});
		rook.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				if (color == WHITE_TURN) {
					setPromotionCode(Piece.WHITE_ROOK);
				} else {
					setPromotionCode(Piece.BLACK_ROOK);
				}
				close();
			}
		});
		knight.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				if (color == WHITE_TURN) {
					setPromotionCode(Piece.WHITE_KNIGHT);
				} else {
					setPromotionCode(Piece.BLACK_KNIGHT);
				}
				close();
			}
		});
		bishop.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				if (color == WHITE_TURN) {
					setPromotionCode(Piece.WHITE_BISHOP);
				} else {
					setPromotionCode(Piece.BLACK_BISHOP);
				}
				close();
			}
		});

		
		if (color == WHITE_TURN) {
			queen.setImage(this.imageFactory.whiteQueen);
			rook.setImage(this.imageFactory.whiteRook);
			knight.setImage(this.imageFactory.whiteKnight);
			bishop.setImage(this.imageFactory.whiteBishop);
		} else {
			queen.setImage(this.imageFactory.blackQueen);
			rook.setImage(this.imageFactory.blackRook);
			knight.setImage(this.imageFactory.blackKnight);
			bishop.setImage(this.imageFactory.blackBishop);
		}
		
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 2);
		formData.top = new FormAttachment(0, 5);
		queen.setLayoutData(formData);

		FormData formData2 = new FormData();
		formData2.left = new FormAttachment(queen, 2);
		formData2.top = new FormAttachment(0, 5);
		rook.setLayoutData(formData2);

		FormData formData3 = new FormData();
		formData3.left = new FormAttachment(rook, 2);
		formData3.top = new FormAttachment(0, 5);
		knight.setLayoutData(formData3);

		FormData formData4 = new FormData();
		formData4.left = new FormAttachment(knight, 2);
		formData4.top = new FormAttachment(0, 5);
		formData4.right = new FormAttachment(100, -2);
		bishop.setLayoutData(formData4);
	}

	public void setPromotionCode(Piece promotionPiece) {
		this.promotionCode = promotionPiece;
	}

	public Piece getPromotionCode() {
		return this.promotionCode;
	}
	/**
	 * Runs the event loop for the given shell.
	 *
	 * @param shell the shell
	 */
	private void runEventLoop() {
		
		while (this.dialog != null && ! this.dialog.isDisposed()) {
			try {
				if (!this.display.readAndDispatch())
					this.display.sleep();
			} catch (Exception e) {
				MyLogger.log(Level.SEVERE, "Failed in dispatch", e);
			}
		}
		this.display.update();
	}	
}
