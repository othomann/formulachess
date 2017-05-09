package org.formulachess.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import static org.formulachess.ui.Sets.*;

public class RowCoordsCanvas extends Canvas {

	class Controller implements PaintListener {

		public void paintControl(PaintEvent e) {
			updateBuffer();
			e.gc.drawImage(RowCoordsCanvas.this.doubleBuffer, 0, 0);
		}
	}

	private int[] settings;
	private boolean whiteBottom;
	private boolean initialized = false;
	// double-buffering
	Image doubleBuffer;

	// layout management
	Point preferredSize;

	public RowCoordsCanvas(Composite composite, int[] settings) {
		super(composite, SWT.NONE);
		Display display = getDisplay();
		this.settings = settings;
		setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		this.preferredSize = new Point(settings[SWITCH_BUTTON_WIDTH_SIZE_INDEX], settings[BOARD_HEIGHT_INDEX]);
		setSize(this.preferredSize);
		// init double buffer
		this.doubleBuffer = new Image(display, settings[SWITCH_BUTTON_WIDTH_SIZE_INDEX], settings[BOARD_HEIGHT_INDEX]);

		addPaintListener(new Controller());
	}

	public void init(boolean whiteB) {
		this.whiteBottom = whiteB;
		this.initialized = true;
	}

	public void switchSide() {
		this.whiteBottom = !this.whiteBottom;
	}

	void updateBuffer() {
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
		if (this.whiteBottom) {
			for (int i = 7; i >= 0; i--) {
				gc.drawString(Integer.toString(8 - i), 10, (this.settings[SQUARE_HEIGHT_INDEX] / 2)
						- (metrics.getDescent() * 2) + (i * this.settings[SQUARE_HEIGHT_INDEX]), true);
			}
		} else {
			for (int i = 0; i < 8; i++) {
				gc.drawString(Integer.toString(i + 1), this.settings[FONT_WIDTH_DELTA_INDEX],
						(this.settings[SQUARE_HEIGHT_INDEX] / 2) - (metrics.getDescent() * 2)
								+ (i * this.settings[SQUARE_HEIGHT_INDEX]),
						true);
			}
		}
		gc.dispose();
		font.dispose();
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		return this.preferredSize;
	}

	@Override
	public void dispose() {
		this.doubleBuffer.dispose();
	}
}
