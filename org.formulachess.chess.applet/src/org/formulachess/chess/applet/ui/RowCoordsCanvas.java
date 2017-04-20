package org.formulachess.chess.applet.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;

public class RowCoordsCanvas extends Canvas {
	private Image rowCoordinatesOffscreen;
	private Graphics rowCoordinatesOffGraphics;
	private int[] settings;
	private boolean whiteBottom;
	private boolean initialized = false;
	
	public RowCoordsCanvas(int[] settings) {
		this.settings = settings;
		setBackground(new Color(240, 240, 240));
		setSize(settings[0], 30);
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
		if (!this.initialized)
			return;
		if (this.rowCoordinatesOffscreen == null) {
			this.rowCoordinatesOffscreen = createImage(this.settings[6], this.settings[1]);
			this.rowCoordinatesOffGraphics = this.rowCoordinatesOffscreen.getGraphics();
		}
		this.rowCoordinatesOffGraphics.setColor(new Color(240, 240, 240));
		this.rowCoordinatesOffGraphics.fillRect(0, 0, this.settings[6], this.settings[1]);
		this.rowCoordinatesOffGraphics.setColor(Color.black);
		Font coordFont = new Font("Courier", Font.BOLD, this.settings[12]); //$NON-NLS-1$
		this.rowCoordinatesOffGraphics.setFont(coordFont);
		FontMetrics metrics = this.rowCoordinatesOffGraphics.getFontMetrics();
		if (this.whiteBottom) {
			for (int i = 7; i >= 0; i--) {
				this.rowCoordinatesOffGraphics.drawString(
					Integer.toString(8 - i),
					10,
					(this.settings[3] / 2)
						+ (metrics.getDescent() * 2)
						+ (i * this.settings[3]));
			}
		} else {
			for (int i = 0; i < 8; i++) {
				this.rowCoordinatesOffGraphics.drawString(
					Integer.toString(i + 1),
					10,
					(this.settings[3] / 2)
						+ (metrics.getDescent() * 2)
						+ (i * this.settings[3]));
			}
		}
		g.drawImage(this.rowCoordinatesOffscreen, 0, 0, this);
	}
}
