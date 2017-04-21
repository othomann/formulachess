package org.formulachess.chess.applet.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;

public class SimpleBorder extends Panel {

	private static final long serialVersionUID = -3020425448071394472L;
	public SimpleBorder(Component component) {
		this(component, _defaultThickness, _defaultGap);
	}

	public SimpleBorder(Component component, int i) {
		this(component, i, _defaultGap);
	}

	public SimpleBorder(Component component, int i, int j) {
		this.thickness = i;
		this.gap = j;
		setLayout(new BorderLayout());
		add("Center", component); //$NON-NLS-1$
	}

	protected DrawnRectangle border() {
		if (this.border == null)
			this.border = new DrawnRectangle(this, this.thickness);
		return this.border;
	}

	public Rectangle getInnerBounds() {
		return border().getInnerBounds();
	}

	public Color getLineColor() {
		return border().getLineColor();
	}

	public Insets getInsets() {
		return new Insets(
			this.thickness + this.gap,
			this.thickness + this.gap,
			this.thickness + this.gap,
			this.thickness + this.gap);
	}

	public void paint(Graphics g) {
		border().paint();
	}

	protected String paramString() {
		return super.paramString()
			+ ",border=" //$NON-NLS-1$
			+ border().toString()
			+ ",this.thickness=" //$NON-NLS-1$
			+ this.thickness
			+ ",this.gap=" //$NON-NLS-1$
			+ this.gap;
	}

	public void setBounds(int i, int j, int k, int l) {
		super.setBounds(i, j, k, l);
		border().setSize(k, l);
	}

	public void setLineColor(Color color) {
		border().setLineColor(color);
	}

	public void setSize(int i, int j) {
		Point point = getLocation();
		setBounds(point.x, point.y, i, j);
	}

	protected int thickness;
	protected int gap;
	protected DrawnRectangle border;
	protected static int _defaultThickness = 2;
	protected static int _defaultGap = 0;

}
