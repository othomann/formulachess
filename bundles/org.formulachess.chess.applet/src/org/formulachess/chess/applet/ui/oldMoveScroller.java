package org.formulachess.chess.applet.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import chess.component.pgn.PGNBoard;

public class oldMoveScroller
	extends Panel
	implements ScrollerInterface, AdjustmentListener, KeyListener {

	private Scrollbar vscroller;
	private Scrollbar hscroller;
	private Dimension dim;
	private PGNBoard pgnBoard;
	private MoveCanvas moveList;
	private Font font;

	public oldMoveScroller(PGNBoard pgnBoard) {
		this.dim = new Dimension(100, 100);
		this.font = new Font("SansSerif", Font.PLAIN, 16); //$NON-NLS-1$
		this.pgnBoard = pgnBoard;
//		this.moveList = new MoveCanvas(pgnBoard, this);
		setLayout(new BorderLayout());
		add("West", this.moveList); //$NON-NLS-1$
		this.vscroller = new Scrollbar(1);
		this.vscroller.setBlockIncrement(250);
		add("East", this.vscroller); //$NON-NLS-1$
		this.hscroller = new Scrollbar(0);
		add("South", this.hscroller); //$NON-NLS-1$
		this.hscroller.setBackground(Color.white);
		this.vscroller.setBackground(Color.white);
		this.hscroller.addAdjustmentListener(this);
		this.vscroller.addAdjustmentListener(this);
	}

	public void adjustmentValueChanged(AdjustmentEvent adjustmentevent) {
		if (adjustmentevent.getAdjustable() == this.vscroller)
			this.vscroller.setValue(adjustmentevent.getValue());
		else if (adjustmentevent.getAdjustable() == this.hscroller)
			this.hscroller.setValue(adjustmentevent.getValue());
		this.moveList.repaint();
	}

	public int getHorizontalMax() {
		return this.hscroller.getMaximum();
	}

	public int getHorizontalMin() {
		return this.hscroller.getMinimum();
	}

	public int getHorizontalVal() {
		return this.hscroller.getValue();
	}

	public int getHorizontalVis() {
		return this.hscroller.getVisibleAmount();
	}

	public Dimension getPreferredSize() {
		return this.dim;
	}

	public int getVerticalMax() {
		return this.vscroller.getMaximum();
	}

	public int getVerticalMin() {
		return this.vscroller.getMinimum();
	}

	public int getVerticalVal() {
		return this.vscroller.getValue();
	}

	public int getVerticalVis() {
		return this.vscroller.getVisibleAmount();
	}

	public void keyPressed(KeyEvent keyevent) {
		// nothing to do
	}

	public void keyReleased(KeyEvent keyevent) {
		// nothing to do
	}

	public void keyTyped(KeyEvent keyevent) {
		// nothing to do
	}

	public void setHorizontalMax(int i) {
		this.hscroller.setMaximum(i);
	}

	public void setHorizontalMin(int i) {
		this.hscroller.setMinimum(i);
	}

	public void setHorizontalValue(int i) {
		this.hscroller.setValue(i);
	}

	public void setHorizontalVis(int i) {
		this.hscroller.setVisibleAmount(i);
	}

	public void setHorizontalVisible(boolean flag) {
		this.hscroller.setVisible(flag);
	}

	public void setVerticalMax(int i) {
		this.vscroller.setMaximum(i);
	}

	public void setVerticalMin(int i) {
		this.vscroller.setMinimum(i);
	}

	public void setVerticalValue(int i) {
		this.vscroller.setValue(i);
	}

	public void setVerticalVis(int i) {
		this.vscroller.setVisibleAmount(i);
	}

	public void setVerticalVisible(boolean flag) {
		this.vscroller.setVisible(flag);
	}

	public void updateTree() {
		int i = getVerticalVal();
		int j = getVerticalVis();
		int k = 0;
		int l = getHorizontalVal();
		int i1 = getHorizontalVis();
		int j1 = 0;
		Dimension dimension = this.moveList.getSize();
		Dimension dimension1 = this.moveList.getSize();
		if (k <= i || k >= i + j) {
			setVerticalMax(Math.max(dimension1.height, dimension.height));
			setVerticalValue(k - j / 2);
		}
		if (j1 <= l || j1 >= l + i1) {
			setHorizontalMax(Math.max(dimension1.width, dimension.width));
			setHorizontalValue(Math.max(j1 - 16, 0));
		}
		this.moveList.repaint();
	}

}
