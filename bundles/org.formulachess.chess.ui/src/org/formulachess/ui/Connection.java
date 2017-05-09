package org.formulachess.ui;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.formulachess.engine.ChessEngine;
import org.formulachess.engine.MateMove;
import org.formulachess.engine.MoveConstants;
import org.formulachess.pgn.engine.PGNMoveContainer;

public class Connection extends Composite implements Observer {

	private static final String EMPTY = ""; //$NON-NLS-1$
	private Font normalFont;
	private Text moveText;
	private ChessEngine model;
	private Messages currentMessages;
	private Locale currentLocale;

	public Connection(Composite parent, int style, ChessEngine model, Locale locale) {
		super(parent, style);
		this.model = model;
		this.currentMessages = new Messages(locale);
		this.currentLocale = locale;
		this.model.addObserver(this);
		Display display = getDisplay();
		this.normalFont = new Font(display, "SansSherif", 10, SWT.BOLD); //$NON-NLS-1$
		final Color backgroundColor = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
		this.setBackground(backgroundColor);
		Label move = new Label(this, SWT.NONE);
		move.setFont(this.normalFont);
		final Color foregroundColor = display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
		move.setForeground(foregroundColor);
		move.setBackground(backgroundColor);
		move.setText(this.currentMessages.getString("connection.label.yourmove")); //$NON-NLS-1$
		this.moveText = new Text(this, SWT.BORDER | SWT.READ_ONLY);
		final Color whiteColor = display.getSystemColor(SWT.COLOR_WHITE);
		this.moveText.setBackground(whiteColor);

		Group proposal = new Group(this, SWT.NONE);
		proposal.setText(this.currentMessages.getString("connection.label.yourproposal")); //$NON-NLS-1$
		proposal.setBackground(backgroundColor);
		proposal.setForeground(foregroundColor);
		FormLayout proposalFormLayout = new FormLayout();
		proposal.setLayout(proposalFormLayout);

		final Button continueButton = new Button(proposal, SWT.RADIO);
		continueButton.setText("Continue"); //$NON-NLS-1$
		continueButton.setBackground(backgroundColor);

		FormData continueFormData = new FormData();
		continueFormData.left = new FormAttachment(10, 15);
		continueFormData.top = new FormAttachment(0, 5);
		continueFormData.right = new FormAttachment(50, -5);
		continueButton.setLayoutData(continueFormData);

		final Button drawButton = new Button(proposal, SWT.RADIO);
		drawButton.setText("Draw"); //$NON-NLS-1$
		drawButton.setBackground(backgroundColor);

		FormData drawFormData = new FormData();
		drawFormData.left = new FormAttachment(60, 5);
		drawFormData.top = new FormAttachment(0, 5);
		drawFormData.right = new FormAttachment(100, -5);
		drawButton.setLayoutData(drawFormData);

		final Button acceptDrawButton = new Button(proposal, SWT.RADIO);
		acceptDrawButton.setText("Accept draw"); //$NON-NLS-1$
		acceptDrawButton.setBackground(backgroundColor);
		acceptDrawButton.setEnabled(false);

		FormData acceptDrawFormData = new FormData();
		acceptDrawFormData.left = new FormAttachment(10, 15);
		acceptDrawFormData.top = new FormAttachment(continueButton, 5);
		acceptDrawFormData.right = new FormAttachment(50, -5);
		acceptDrawButton.setLayoutData(acceptDrawFormData);

		final Button resignButton = new Button(proposal, SWT.RADIO);
		resignButton.setText("Resign"); //$NON-NLS-1$
		resignButton.setBackground(backgroundColor);

		FormData resignFormData = new FormData();
		resignFormData.left = new FormAttachment(60, 5);
		resignFormData.top = new FormAttachment(drawButton, 5);
		resignFormData.right = new FormAttachment(100, -5);
		resignButton.setLayoutData(resignFormData);

		Group commentGroup = new Group(this, SWT.NONE);
		commentGroup.setText(this.currentMessages.getString("connection.label.yourcomment")); //$NON-NLS-1$
		commentGroup.setBackground(backgroundColor);
		commentGroup.setForeground(foregroundColor);
		FormLayout commentGroupFormLayout = new FormLayout();
		commentGroup.setLayout(commentGroupFormLayout);

		Text comment = new Text(commentGroup, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		comment.setBackground(whiteColor);

		FormData commentFormData = new FormData();
		commentFormData.left = new FormAttachment(0, 2);
		commentFormData.top = new FormAttachment(0, 2);
		commentFormData.right = new FormAttachment(100, -2);
		commentFormData.bottom = new FormAttachment(100, -2);
		comment.setLayoutData(commentFormData);

		Button submit = new Button(this, SWT.PUSH);
		submit.setText(this.currentMessages.getString("connection.button.submit")); //$NON-NLS-1$
		submit.setBackground(backgroundColor);

		Button cancel = new Button(this, SWT.PUSH);
		cancel.setText(this.currentMessages.getString("connection.button.cancel")); //$NON-NLS-1$
		cancel.setBackground(backgroundColor);

		// layout the component
		FormLayout formLayout = new FormLayout();
		this.setLayout(formLayout);

		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 15);
		formData.top = new FormAttachment(0, 5);
		move.setLayoutData(formData);

		FormData formData2 = new FormData();
		formData2.left = new FormAttachment(move, 5);
		formData2.top = new FormAttachment(0, 5);
		formData2.right = new FormAttachment(100, -5);
		this.moveText.setLayoutData(formData2);

		FormData formData3 = new FormData();
		formData3.left = new FormAttachment(0, 5);
		formData3.top = new FormAttachment(this.moveText, 5);
		formData3.right = new FormAttachment(100, -5);
		proposal.setLayoutData(formData3);

		FormData formData5 = new FormData();
		formData5.top = new FormAttachment(proposal, 15);
		formData5.right = new FormAttachment(100, -5);
		submit.setLayoutData(formData5);

		FormData formData6 = new FormData();
		formData6.top = new FormAttachment(submit, 5);
		formData6.right = new FormAttachment(100, -5);
		cancel.setLayoutData(formData6);

		FormData formData4 = new FormData();
		formData4.left = new FormAttachment(0, 5);
		formData4.top = new FormAttachment(proposal, 5);
		formData4.bottom = new FormAttachment(100, -5);
		formData4.right = new FormAttachment(submit, -5);
		commentGroup.setLayoutData(formData4);
	}

	@Override
	public void dispose() {
		this.normalFont.dispose();
	}

	/**
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
		long lastMove = this.model.getLastMove();
		if (lastMove != -1) {
			this.model.undoMoveWithoutNotification(lastMove);

			int startingSquare = (int) (lastMove & MoveConstants.STARTING_SQUARE_MASK);

			long[] moves = this.model.allMoves(this.model.getBoard(startingSquare));

			PGNMoveContainer container = new PGNMoveContainer(this.model, moves, this.currentLocale);
			String moveNotation = container.getMoveNotation(lastMove);
			if (this.model.isMate()) {
				moveNotation += this.currentMessages.getString("connection.move.mate"); //$NON-NLS-1$
			} else if (MateMove.isCheck(lastMove)) {
				moveNotation += this.currentMessages.getString("connection.move.check"); //$NON-NLS-1$
			}
			this.moveText.setText(moveNotation);
			this.model.playMoveWithoutNotification(lastMove);
		} else {
			this.moveText.setText(EMPTY);
		}
	}
}
