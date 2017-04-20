package org.formulachess.chess.applet.databaselist;

import java.awt.Dimension;
import java.awt.List;

public class HeaderList extends List {

	private int actualWidth;
	private int actualHeight;
	
	/**
	 * Constructor for HeaderList.
	 * @throws HeadlessException
	 */
	public HeaderList() {
		super();
	}

	/**
	 * Constructor for HeaderList.
	 * @param rows
	 * @throws HeadlessException
	 */
	public HeaderList(int rows) {
		super(rows);
	}

	/**
	 * Constructor for HeaderList.
	 * @param rows
	 * @param multipleMode
	 * @throws HeadlessException
	 */
	public HeaderList(int rows, boolean multipleMode) {
		super(rows, multipleMode);
	}

	public void setDimension(int width, int height) {
		this.actualHeight = height;
		this.actualWidth = width;
	}
	/**
	 * @see java.awt.Component#getMinimumSize()
	 */
	public Dimension getMinimumSize() {
		return new Dimension(this.actualWidth, this.actualHeight);
	}

	/**
	 * @see java.awt.Component#getPreferredSize()
	 */
	public Dimension getPreferredSize() {
		return new Dimension(this.actualWidth, this.actualHeight);
	}

}
