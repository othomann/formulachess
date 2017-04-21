package org.formulachess.chess.applet.util;

import java.awt.image.RGBImageFilter;

public class GrayFilter extends RGBImageFilter {
	private int darkness = 0xffafafaf;
	public GrayFilter() {
		this.canFilterIndexColorModel = true;
	}
	public GrayFilter(int darkness) {
		this();
		this.darkness = darkness;
	}
	public int filterRGB(int x, int y, int rgb) {
		return (rgb & this.darkness);
	}
}
