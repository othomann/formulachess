package org.formulachess.chess.applet.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;

public class ColCoordsCanvas extends Canvas {
	private Image colCoordinatesOffscreen;
	private Graphics colCoordinatesOffGraphics;
	private int[] settings;
	private boolean whiteBottom;
	private boolean initialized = false;
	
public ColCoordsCanvas(int[] settings) {
	this.settings = settings;
	setBackground(new Color(240, 240, 240));
	setSize(30, settings[1]);
}
public void init(boolean whiteB) {
	this.whiteBottom = whiteB;
	this.initialized = true;
}
public void paint(Graphics g) {
	update(g);
}
public void switchSide() {
	this.whiteBottom = !this.whiteBottom;
}
public void update(Graphics g) {
	if (!this.initialized) return;
	if (this.colCoordinatesOffscreen == null) {
		this.colCoordinatesOffscreen = createImage(this.settings[0], this.settings[7]);
		this.colCoordinatesOffGraphics = this.colCoordinatesOffscreen.getGraphics();
	}
	this.colCoordinatesOffGraphics.setColor(new Color(240, 240, 240));
	this.colCoordinatesOffGraphics.fillRect(0,0,this.settings[0],this.settings[7]);
	this.colCoordinatesOffGraphics.setColor(Color.black);
	Font coordFont = new Font("Courier", Font.BOLD, this.settings[12]); //$NON-NLS-1$
	this.colCoordinatesOffGraphics.setFont(coordFont);
	FontMetrics metrics = this.colCoordinatesOffGraphics.getFontMetrics();
	if (this.whiteBottom) {
		for (int j = 0; j < 8; j++) {
			this.colCoordinatesOffGraphics.drawString("" + (char) ('A' + j), (j * this.settings[2]) + (this.settings[2] / 2) - (metrics.stringWidth("H") / 2), 25); //$NON-NLS-1$ //$NON-NLS-2$
		}
	} else {
		for (int j = 7; j >= 0; j--) {
			this.colCoordinatesOffGraphics.drawString("" + (char) ('A' + 7 - j), (j * this.settings[2]) + (this.settings[2] / 2) - (metrics.stringWidth("H") / 2), 25); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	g.drawImage(this.colCoordinatesOffscreen, 0, 0, this);
}
}
