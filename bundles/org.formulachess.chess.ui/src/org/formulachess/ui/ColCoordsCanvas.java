package org.formulachess.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import static org.formulachess.ui.Sets.*;

public class ColCoordsCanvas extends Canvas {

	class Controller implements PaintListener {

		@Override
		public void paintControl(PaintEvent e) {
			updateBuffer();
			e.gc.drawImage(ColCoordsCanvas.this.doubleBuffer, 0, 0);
		}
	}

	private int[] settings;
	private boolean whiteBottom;
	private boolean initialized = false;
	// double-buffering
	Image doubleBuffer;

	// layout management
	Point preferredSize;

	public ColCoordsCanvas(Composite composite, int[] settings) {
		super(composite, SWT.NONE);
		Display display = getDisplay();
		this.settings = settings;
		setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		this.preferredSize = new Point(settings[BOARD_WIDTH_INDEX], settings[SWITCH_BUTTON_HEIGHT_SIZE_INDEX]);
		setSize(this.preferredSize);
		// init double buffer
		this.doubleBuffer = new Image(display, settings[BOARD_WIDTH_INDEX], settings[SWITCH_BUTTON_HEIGHT_SIZE_INDEX]);

		addPaintListener(new Controller());
	}

	@Override
	public void dispose() {
		this.doubleBuffer.dispose();
	}

	public void init(boolean whiteB) {
		this.whiteBottom = whiteB;
		this.initialized = true;
	}

	public void switchSide() {
		this.whiteBottom = !this.whiteBottom;
	}

	public void updateBuffer() {
		GC gc = new GC(this.doubleBuffer);

		if (!this.initialized)
			return;

		Display display = getDisplay();

		gc.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		gc.fillRectangle(0, 0, this.preferredSize.x, this.preferredSize.y);

		gc.setForeground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
		Font font = new Font(display, "Courier", this.settings[FONT_SIZE_INDEX], SWT.BOLD); //$NON-NLS-1$
		gc.setFont(font);
		FontMetrics metrics = gc.getFontMetrics();
		for (int j = 0; j < 8; j++) {
			String coord;
			if (this.whiteBottom) {
				coord = String.valueOf((char) ('A' + j));
			} else {
				coord = String.valueOf((char) ('A' + 7 - j));
			}
			gc.drawString(coord, (j * this.settings[SQUARE_WIDTH_INDEX]) + (this.settings[SQUARE_WIDTH_INDEX] / 2)
					- (metrics.getAverageCharWidth() / 2), this.settings[FONT_HEIGHT_DELTA_INDEX], true);
		}
		gc.dispose();
		font.dispose();
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		return this.preferredSize;
	}
}
