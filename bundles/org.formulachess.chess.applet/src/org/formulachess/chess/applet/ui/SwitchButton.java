package org.formulachess.chess.applet.ui;

import java.awt.Color;

import org.formulachess.chess.applet.util.ImageButton;

public class SwitchButton extends ImageButton {
	private BoardView view;
	
	public SwitchButton(BoardView v) {
		super(SwitchButton.class.getResourceAsStream("sets/common/switch.gif")); //$NON-NLS-1$
		setBackground(new Color(240, 240, 240));
		setView(v);
	}
	public void action() {
		getView().switchSide();
	}

	public void setView(BoardView view) {
		this.view = view;
	}

	public BoardView getView() {
		return this.view;
	}
}
