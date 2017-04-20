package org.formulachess.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class SwitchCanvas extends Canvas {

	class Controller implements PaintListener {
		
		public void paintControl(PaintEvent e) {
			updateBuffer();
			e.gc.drawImage(
				SwitchCanvas.this.doubleBuffer,
				0,
				0
			);
		}
	}

	public Button switchButton;
	
    // double-buffering
    Image doubleBuffer;

	// layout management
	Point preferredSize;

	public SwitchCanvas(Composite composite, ImageFactory imageFactory) {
		super(composite, SWT.NONE);
		Display display = getDisplay();
		this.preferredSize = new Point(30, 30);
		setSize(this.preferredSize);
		setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		this.switchButton = new Button(this, SWT.PUSH);
		
		Image switchImage = imageFactory.switchImage;
		this.switchButton.setImage(switchImage);
		this.switchButton.setSize(switchImage.getBounds().width, switchImage.getBounds().height);

		this.switchButton.setLocation(1, 7);
		
		// init double buffer
		this.doubleBuffer = new Image(display, 30, 30);
		
		addPaintListener(new Controller());
	}

	public void dispose() {
		this.doubleBuffer.dispose();
	}
	
	void updateBuffer() {			
		GC gc = new GC(this.doubleBuffer);
		gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		gc.fillRectangle(0,0,this.preferredSize.x, this.preferredSize.y);
		gc.dispose();
	}

	public Point computeSize (int wHint, int hHint, boolean changed) {
		return this.preferredSize;
	}
}
