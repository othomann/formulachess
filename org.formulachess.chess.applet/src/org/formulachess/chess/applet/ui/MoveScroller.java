package org.formulachess.chess.applet.ui;

import java.awt.Font;
import java.awt.ScrollPane;

public class MoveScroller extends ScrollPane {

	private PGNBoard pgnBoard;
	private MoveCanvas moveList;
	private Font font;
	
	public MoveScroller(PGNBoard pgnBoard) {
		super(ScrollPane.SCROLLBARS_AS_NEEDED);
		this.font = new Font("SansSerif", Font.PLAIN, 16); //$NON-NLS-1$
		this.pgnBoard = pgnBoard;
		this.moveList = new MoveCanvas(pgnBoard, this);
		add(this.moveList);
		getVAdjustable().setUnitIncrement(17);
	}
}