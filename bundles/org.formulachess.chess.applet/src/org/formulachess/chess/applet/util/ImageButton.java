package org.formulachess.chess.applet.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.io.InputStream;

public abstract class ImageButton extends ImageLabel implements MouseListener {
	/** Default width of 3D border around image.
	 *  Currently 4.
	 * @see ImageLabel#setBorder
	 * @see ImageLabel#getBorder
	 */
	protected static final int defaultBorderWidth = 2;

	/** Default color of 3D border around image.
	 *  Currently a gray with R/G/B of 160/160/160.
	 *  Light grays look best.
	 * @see ImageLabel#setBorderColor
	 * @see ImageLabel#getBorderColor
	 */
	protected static final Color defaultBorderColor = new Color(160, 160, 160);
	private boolean mouseIsDown = false;
	// Changing darker is consistent with regular buttons

	private int darkness = 0xffafafaf;

	private Image grayImage = null;
/** Create an ImageButton using the image specified.
   *  You would only want to use this if you already
   *  have an image (e.g. created via createImage).
   * @param image The image.
   */
public ImageButton(Image image) {
	super(image);
	setBorders();
	addMouseListener(this);
}
/** Create an ImageButton using the image at URL
   *  specified by the string.
   * @param imageURLString A String specifying the URL
   *        of the image.
   */
public ImageButton(InputStream inputStream) {
	super(inputStream);
	setBorders();
	addMouseListener(this);
}
abstract public void action();
//----------------------------------------------------
// The first time the image is drawn, update() is
// called, and the result does not come out correctly.
// So this forces a brief draw on loadup, replaced
// by real, non-gray image.

private void createGrayImage(Graphics g) {
	ImageFilter filter = new GrayFilter(this.darkness);
	ImageProducer producer = 
		new FilteredImageSource(getImage().getSource(), filter); 
	this.grayImage = createImage(producer);
	int border = getBorder();
	if (hasExplicitSize())
		prepareImage(
			this.grayImage, 
			getWidth() - 2 * border, 
			getHeight() - 2 * border, 
			this); 
	else
		prepareImage(this.grayImage, this);
	super.paint(g);
}
public void destroy() {
	removeMouseListener(this);
}
//----------------------------------------------------

private void drawBorder(boolean isUp) {
	Graphics g = getGraphics();
	g.setColor(getBorderColor());
	int left = 0;
	int top = 0;
	int width = getWidth();
	int height = getHeight();
	int border = getBorder();
	for (int i = 0; i < border; i++) {
		g.draw3DRect(left, top, width, height, isUp);
		left++;
		top++;
		width = width - 2;
		height = height - 2;
	}
}
//----------------------------------------------------

/** The darkness value to use for grayed images.
 * @see #setDarkness
 */
public int getDarkness() {
	return this.darkness;
}
//----------------------------------------------------
/** The gray image used when button is down.
 * @see #setthis.grayImage
 */

public Image getGrayImage() {
	return this.grayImage;
}
public void mouseClicked(MouseEvent e) {
	// nothing to do
}
public void mouseDragged(MouseEvent e) {
	// nothing to do
}
public void mouseEntered(MouseEvent e) {
	// nothing to do
}
public void mouseExited(MouseEvent e) {
	if (this.mouseIsDown)
		paint(getGraphics());
}
public void mouseMoved(MouseEvent e) {
	// move without pressing a button
}
public void mousePressed(MouseEvent e) {
	if (!isEnabled()) return;
	this.mouseIsDown = true;
	Graphics g = getGraphics();
	int border = getBorder();
	if (hasExplicitSize())
		g.drawImage(
			this.grayImage, 
			border, 
			border, 
			getWidth() - 2 * border, 
			getHeight() - 2 * border, 
			this); 
	else
		g.drawImage(this.grayImage, border, border, this);
	drawBorder(false);
	action();	
}
public void mouseReleased(MouseEvent e) {
	this.mouseIsDown = false;
	repaint();
}
//----------------------------------------------------
/** Draws the image with the border around it. If you
 *  override this in a subclass, call super.paint().
 */
public void paint(Graphics g) {
	super.paint(g);
	if (this.grayImage == null)
		createGrayImage(g);
	drawBorder(true);
}
public void setBorders() {
	setBorder(defaultBorderWidth);
	setBorderColor(defaultBorderColor);
}
/** An int whose bits are combined via "and" ("&")
   *  with the alpha, red, green, and blue bits of the
   *  pixels of the image to produce the grayed-out
   *  image to use when button is depressed.
   *  Default is 0xffafafaf: af combines with r/g/b
   *  to darken image.
   */

public void setDarkness(int darkness) {
	this.darkness = darkness;
}
/** Sets gray image created automatically from regular
   *  image via an image filter to use when button is
   *  depressed. You won't normally use this directly. 
   */
public void setGrayImage(Image grayImage) {
	this.grayImage = grayImage;
}
}
