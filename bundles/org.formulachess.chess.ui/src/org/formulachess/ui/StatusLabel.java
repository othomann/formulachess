package org.formulachess.ui;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.formulachess.engine.ChessEngine;
import org.formulachess.engine.MateMove;
import org.formulachess.engine.MoveConstants;
import org.formulachess.engine.Piece;
import org.formulachess.pgn.engine.PGNMoveContainer;
import static org.formulachess.engine.Turn.*;

public class StatusLabel extends Composite implements Observer {

	private ChessEngine model;
	private Label status;
	private Messages currentMessages;
	private Font labelFont;

	public StatusLabel(Composite parent, int style, Locale locale, ChessEngine model) {
		super(parent, style);
		this.currentMessages = new Messages(locale);
		this.model = model;
		this.model.addObserver(this);
		Display display = getDisplay();
		final Color backgroundColor = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
		final Color foregroundColor = display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
		this.setBackground(backgroundColor);
		this.setForeground(foregroundColor);

		// layout the component
		FormLayout formLayout = new FormLayout();
		this.setLayout(formLayout);

		this.status = new Label(this, SWT.NONE);
		this.status.setText(""); //$NON-NLS-1$
		this.labelFont = new Font(display, "SansSerif", 8, SWT.BOLD); //$NON-NLS-1$
		this.status.setFont(this.labelFont);

		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 2);
		formData.left = new FormAttachment(0, 2);
		formData.right = new FormAttachment(100, -2);
		formData.bottom = new FormAttachment(100, -2);
		this.status.setLayoutData(formData);
	}

	@Override
	public void dispose() {
		this.labelFont.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o == this.model) {
			update();
		}
	}

	@Override
	public void update() {
		// check if it is mate or stalemate
		long[] nextMoves = this.model.allMoves();
		if (nextMoves.length == 0 && ((this.model.getTurn() == WHITE_TURN && !this.model.isWhiteInCheck())
				|| (this.model.getTurn() == BLACK_TURN && !this.model.isBlackInCheck()))) {
			this.status.setText(this.currentMessages.getString("statuslabel.status.stalemate")); //$NON-NLS-1$
			return;
		}
		long lastMove = this.model.getLastMove();
		if (lastMove != -1) {
			this.model.undoMoveWithoutNotification(lastMove);

			int startingSquare = MoveConstants.getStartingSquare(lastMove);
			final Piece piece = this.model.getBoard(startingSquare);

			long[] moves = this.model.allMoves(piece);

			PGNMoveContainer container = new PGNMoveContainer(this.model, moves, Locale.getDefault());
			this.model.playMoveWithoutNotification(lastMove);

			StringBuilder moveNotation = new StringBuilder();
			if (this.model.getStartingColor() == WHITE_TURN) {
				moveNotation.append(this.model.getMoveNumber() / 2 + 1);
			} else {
				moveNotation.append((this.model.getMoveNumber() + 1) / 2 + 1);
			}
			if (piece.isBlack()) {
				moveNotation.append("..."); //$NON-NLS-1$
			} else {
				moveNotation.append(". "); //$NON-NLS-1$
			}
			moveNotation.append(container.getMoveNotation(lastMove));
			if (this.model.isMate()) {
				moveNotation.append(this.currentMessages.getString("statuslabel.movenotation.mate")); //$NON-NLS-1$
			} else if (MateMove.isCheck(lastMove)) {
				moveNotation.append(this.currentMessages.getString("statuslabel.movenotation.check")); //$NON-NLS-1$
			}
			this.status.setText(moveNotation.toString());
		} else {
			if (this.model.getTurn() == WHITE_TURN) {
				this.status.setText(this.currentMessages.getString("statuslabel.status.whitetoplay")); //$NON-NLS-1$
			} else {
				this.status.setText(this.currentMessages.getString("statuslabel.status.blacktoplay")); //$NON-NLS-1$
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Control#computeSize(int, int, boolean)
	 */
	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		return new Point(90, 25);
	}

}
