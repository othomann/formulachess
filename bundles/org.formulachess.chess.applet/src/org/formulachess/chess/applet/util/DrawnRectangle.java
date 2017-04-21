package org.formulachess.chess.applet.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

public class DrawnRectangle extends Rectangle {

	private static final long serialVersionUID = -6515791184479218050L;
	public DrawnRectangle(Component component1) {
		this(component1, _defaultThickness, 0, 0, 0, 0);
	}

	public DrawnRectangle(Component component1, int i) {
		this(component1, i, 0, 0, 0, 0);
	}

	public DrawnRectangle(Component component1, int i, int j, int k, int l) {
		this(component1, _defaultThickness, i, j, k, l);
	}

	public DrawnRectangle(
		Component component1,
		int i,
		int j,
		int k,
		int l,
		int i1) {
		this.drawInto = component1;
		this.thick = i;
		setBounds(j, k, l, i1);
	}

	protected Color brighter() {
		return getLineColor().brighter().brighter().brighter().brighter();
	}

	public void clear() {
		clearExterior();
		clearInterior();
	}

	public void clearExterior() {
		paintFlat(this.drawInto.getGraphics(), this.drawInto.getBackground());
	}

	public void clearInterior() {
		fill(this.drawInto.getBackground());
	}

	public Component component() {
		return this.drawInto;
	}

	public void fill() {
		fill(getFillColor());
	}

	public void fill(Color color) {
		Graphics g = this.drawInto.getGraphics();
		if (g != null) {
			Rectangle rectangle = getInnerBounds();
			g.setColor(color);
			g.fillRect(
				rectangle.x,
				rectangle.y,
				rectangle.width,
				rectangle.height);
			setFillColor(color);
		}
	}

	public Color getFillColor() {
		if (this.fillColor == null)
			this.fillColor = this.drawInto.getBackground();
		return this.fillColor;
	}

	public Rectangle getInnerBounds() {
		return new Rectangle(
			this.x + this.thick,
			this.y + this.thick,
			this.width - this.thick * 2,
			this.height - this.thick * 2);
	}

	public Color getLineColor() {
		if (this.lineColor == null)
			this.lineColor = this.drawInto.getBackground().darker().darker().darker();
		return this.lineColor;
	}

	public int getThickness() {
		return this.thick;
	}

	public void paint() {
		Graphics g = this.drawInto.getGraphics();
		paintFlat(g, getLineColor());
	}

	private void paintFlat(Graphics g, Color color) {
		if (g != null) {
			g.setColor(color);
			for (int i = 0; i < this.thick; i++)
				g.drawRect(this.x + i, this.y + i, this.width - i * 2 - 1, this.height - i * 2 - 1);

		}
	}

	public String paramString() {
		return "color=" //$NON-NLS-1$
			+ getLineColor()
			+ ",thickness="//$NON-NLS-1$
			+ this.thick
			+ ",this.fillColor="//$NON-NLS-1$
			+ getFillColor();
	}

	public void setFillColor(Color color) {
		this.fillColor = color;
	}

	public void setLineColor(Color color) {
		this.lineColor = color;
	}

	public void setThickness(int i) {
		this.thick = i;
	}

	public String toString() {
		return super.toString() + "[" + paramString() + "]"; //$NON-NLS-1$//$NON-NLS-2$
	}

	protected static int _defaultThickness = 2;
	protected Component drawInto;
	private int thick;
	private Color lineColor;
	private Color fillColor;

}
