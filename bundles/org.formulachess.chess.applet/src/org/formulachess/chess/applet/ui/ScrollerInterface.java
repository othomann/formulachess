package org.formulachess.chess.applet.ui;

public interface ScrollerInterface {
	public abstract int getHorizontalMax();

	public abstract int getHorizontalMin();

	public abstract int getHorizontalVal();

	public abstract int getHorizontalVis();

	public abstract int getVerticalMax();

	public abstract int getVerticalMin();

	public abstract int getVerticalVal();

	public abstract int getVerticalVis();

	public abstract void setHorizontalMax(int i);

	public abstract void setHorizontalMin(int i);

	public abstract void setHorizontalValue(int i);

	public abstract void setHorizontalVis(int i);

	public abstract void setHorizontalVisible(boolean flag);

	public abstract void setVerticalMax(int i);

	public abstract void setVerticalMin(int i);

	public abstract void setVerticalValue(int i);

	public abstract void setVerticalVis(int i);

	public abstract void setVerticalVisible(boolean flag);

}