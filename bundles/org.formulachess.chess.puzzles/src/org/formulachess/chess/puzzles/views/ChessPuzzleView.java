package org.formulachess.chess.puzzles.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import org.formulachess.views.ChessPuzzles;

public class ChessPuzzleView extends ViewPart {
	private ChessPuzzles formulachess;

	public ChessPuzzleView() {
		// nothing to do
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		this.formulachess = new ChessPuzzles(parent);
	}

	public void setFocus() {
		this.formulachess.setFocus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose() {
		this.formulachess.close();
		super.dispose();
	}
}