package org.formulachess.chess.applet.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Panel;
import java.util.Observable;
import java.util.Observer;

import org.formulachess.engine.ChessEngine;

public class BoardView extends Panel implements Observer {
	private BoardCanvas boardCanvas;
	private RowCoordsCanvas rowCoordinates;
	private ColCoordsCanvas colCoordinates;
	private boolean whiteBottom;
	private ChessEngine model;
	private int promotionIndex;
public BoardView(Frame frame, String setimages, int[] settings) {
	frame.setBackground(new Color(240, 240, 240));
	setSize(settings[0]+settings[6], settings[1]+settings[7]);
	setLayout(null);
	this.boardCanvas = new BoardCanvas(frame, setimages, settings);
	this.boardCanvas.setSize(settings[0], settings[1]);
	this.rowCoordinates = new RowCoordsCanvas(settings);
	this.rowCoordinates.setSize(settings[6], settings[1]);
	this.colCoordinates = new ColCoordsCanvas(settings);
	this.colCoordinates.setSize(settings[0], settings[7]);
	this.boardCanvas.setLocation(settings[6], 0);
	this.rowCoordinates.setLocation(0,0);
	this.colCoordinates.setLocation(settings[6], settings[1]);
	SwitchButton switchSide = new SwitchButton(this);
	switchSide.setLocation(settings[10], settings[1] + settings[11]);
	switchSide.setSize(settings[8],settings[9]);
	add(this.boardCanvas);
	add(this.colCoordinates);
	add(this.rowCoordinates);
	add(switchSide);
	setWhiteBottom(true);
	this.rowCoordinates.init(getWhiteBottom());
	this.colCoordinates.init(getWhiteBottom());
	validate();
}
public void paint(Graphics g) {
	this.update(g);
}

public void setLabelSize(Label label) {
	Font font = label.getFont();
	FontMetrics metrics = label.getFontMetrics(font);
	label.setSize(metrics.stringWidth(label.getText()), metrics.getMaxAscent() + metrics.getMaxDescent());
}
public void setBoard(ChessEngine b) {
	setModel(b);
	this.boardCanvas.init(this);
}
public void switchSide() {
	setWhiteBottom(!getWhiteBottom());
	this.boardCanvas.switchSide();
	this.rowCoordinates.switchSide();
	this.colCoordinates.switchSide();
	repaint();
}

public void setWhiteBottom(boolean whiteBottom) {
	this.whiteBottom = whiteBottom;
}

public boolean getWhiteBottom() {
	return this.whiteBottom;
}

public void setModel(ChessEngine model) {
	this.model = model;
	model.addObserver(this);
}

public ChessEngine getModel() {
	return this.model;
}

public void setPromotionIndex(int promotionIndex) {
	this.promotionIndex = promotionIndex;
}

public int getPromotionIndex() {
	return this.promotionIndex;
}
	/**
	 * @see java.util.Observer#update(Observable, Object)
	 */
	public void update(Observable o, Object arg) {
		if (o == this.model) {
			repaint();
		}
	}

	/**
	 * @see java.awt.Component#update(Graphics)
	 */
	public void update(Graphics g) {
		this.boardCanvas.repaint();
		this.rowCoordinates.repaint();
		this.colCoordinates.repaint();
	}

}
