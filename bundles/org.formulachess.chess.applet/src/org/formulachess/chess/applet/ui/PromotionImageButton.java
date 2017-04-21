package org.formulachess.chess.applet.ui;

import java.awt.event.MouseEvent;
import java.io.InputStream;

import org.formulachess.chess.applet.util.ImageButton;

public class PromotionImageButton extends ImageButton {
	private BoardView boardView;
	private int index;
	private PromotionDialog dialog;

	public PromotionImageButton(
		InputStream inputStream,
		int index,
		PromotionDialog diag) {
		super(inputStream);
		setBorders();
		addMouseListener(this);
		this.index = index;
		this.dialog = diag;
	}
	public void action() {
		this.boardView.setPromotionIndex(this.index);
		this.dialog.setVisible(false);
		this.dialog.dispose();
	}
	public void mousePressed(MouseEvent e) {
		this.dialog.setPromotionIndex(this.index);
		this.dialog.setVisible(false);
		this.dialog.dispose();
	}
}
