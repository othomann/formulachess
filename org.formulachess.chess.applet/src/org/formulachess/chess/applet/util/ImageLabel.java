package org.formulachess.chess.applet.util;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;

/**
 * A class for displaying images. It places the Image
 * into a canvas so that it can moved around by layout
 * managers, will get repainted automatically, etc.
 * No mouseXXX or action events are defined, so it is
 * most similar to the Label Component.
 * <P>
 * By default, with FlowLayout the ImageLabel takes
 * its minimum size (just enclosing the image). The
 * default with BorderLayout is to expand to fill
 * the region in width (North/South), height
 * (East/West) or both (Center). This is the same
 * behavior as with the builtin Label class. If you
 * give an explicit resize or
 * reshape call <B>before</B> adding the
 * ImageLabel to the Container, this size will
 * override the defaults.
 * <P>
 * Here is an example of its use:
 * <P>
 * <PRE>
 * public class ShowImages extends Applet {
 *   private ImageLabel image1, image2;
 *
 *   public void init() {
 *     image1 = new ImageLabel(getCodeBase(),
 *                             "some-image.gif");
 *     image2 = new ImageLabel(getCodeBase(),
 *                             "other-image.jpg");
 *     add(image1);
 *     add(image2);
 *   }
 * }
 * </PRE>
 *
 * @author Marty Hall (hall@apl.jhu.edu)
 * @see Icon
 * @see ImageButton
 * @version 1.0 (1997)
 */

public class ImageLabel extends Canvas {
	//----------------------------------------------------
	// Instance variables.

	private static final long serialVersionUID = 5800141985834725078L;

	// The actual Image drawn on the canvas. 
	private Image image;

	// The URL of the image. But sometimes we will use
	// an existing image object (e.g. made by
	// createImage) for which this info will not be
	// available, so a default string is used here.
	private String imageString;

	/** Amount of extra space around the image. */
	private int border = 0;

	/** If there is a non-zero border, what color should
	 *  it be? Default is to use the background color
	 *  of the Container.
	 */
	private Color borderColor = null;

	// Width and height of the Canvas. This is the
	//  width/height of the image plus twice the border.
	private int width, height;

	/** Determines if it will be sized automatically.
	 *  If the user issues a resize() or reshape()
	 *  call before adding the label to the Container,
	 *  or if the LayoutManager resizes before
	 *  drawing (as with BorderLayout), then those sizes
	 *  override the default, which is to make the label
	 *  the same size as the image it holds (after
	 *  reserving space for the border, if any).
	 *  This flag notes this, so subclasses that
	 *  override ImageLabel need to check this flag, and
	 *  if it is true, and they draw modified image,
	 *  then they need to draw them based on the width
	 *  height variables, not just blindly drawing them
	 *  full size.
	 */
	private boolean explicitSize = false;
	private int explicitWidth = 0, explicitHeight = 0;

	// The MediaTracker that can tell if image has been
	// loaded before trying to paint it or resize
	// based on its size.
	private MediaTracker tracker;

	// Used by MediaTracker to be sure image is loaded
	// before paint & resize, since you can't find out
	// the size until it is done loading.
	private static int lastTrackerID = 0;
	private int currentTrackerID;
	private boolean doneLoading = false;

	private Container parentContainer;
	private Dimension dimension;
/** Create an ImageLabel using the image specified.
   *  The other constructors eventually call this one,
   *  but you may want to call it directly if you
   *  already have an image (e.g. created via
   *  createImage).
   *
   * @param image The image
   */
public ImageLabel(Image image) {
	this.image = image;
	this.tracker = new MediaTracker(this);
	this.currentTrackerID = lastTrackerID++;
	this.tracker.addImage(image, this.currentTrackerID);
}
/** Create an ImageLabel using the image at URL
   *  specified by the string.
   *
   * @param imageURLString A String specifying the
   *   URL of the image.
   */
public ImageLabel(InputStream inputStream) {
	this(retrieveImage(inputStream));
}
/** Moves the image so that it is <I>centered</I> at
 *  the specified location, as opposed to the move
 *  method of Component which places the top left
 *  corner at the specified location.
 *  <P>
 *  <B>Note:</B> The effects of this could be undone
 *  by the LayoutManager of the parent Container, if
 *  it is using one. So this is normally only used
 *  in conjunction with a null LayoutManager.
 *
 * @param x The X coord of center of the image
 *          (in parent's coordinate system)
 * @param y The Y coord of center of the image
 *          (in parent's coordinate system)
 * @see java.awt.Component#move
 */
public void centerAt(int x, int y) {
	setLocation(x - this.width / 2, y - this.height / 2);
}
//----------------------------------------------------
/** Determines if the x and y <B>(in the ImageLabel's
 *  own coordinate system)</B> is inside the
 *  ImageLabel. Put here because Netscape 2.02 has
 *  a bug in which it doesn't process inside() and
 *  locate() tests correctly. 
 */
public synchronized boolean contains(int x, int y) {
	return ((x >= 0) && (x <= this.width) && (y >= 0) && (y <= this.height));
}
//----------------------------------------------------
// You can't just set the background color to
// the borderColor and skip drawing the border,
// since it messes up transparent gifs. You
// need the background color to be the same as
// the container.

/** Draws a rectangle with the specified OUTSIDE
 *  left, top, width, and height.
 *  Used to draw the border.
 */
protected void drawRect(
	Graphics g, 
	int left, 
	int top, 
	int _width, 
	int _height, 
	int lineThickness, 
	Color rectangleColor) {
	g.setColor(rectangleColor);
	for (int i = 0; i < lineThickness; i++) {
		g.drawRect(left, top, _width, _height);
		if (i < lineThickness - 1) { // Skip last iteration
			left = left + 1;
			top = top + 1;
			_width = _width - 2;
			_height = _height - 2;
		}
	}
}
//----------------------------------------------------
/** Gets the border this.width. */

public int getBorder() {
	return this.border;
}
//----------------------------------------------------
/** Gets the border color. */

public Color getBorderColor() {
	return this.borderColor;
}
/** Gets the height (image height plus 2x border). */

public int getHeight() {
	return (this.height);
}
//----------------------------------------------------
/** The Image associated with the ImageLabel. */

public Image getImage() {
	return this.image;
}
//----------------------------------------------------
/** Returns the string representing the URL
 *  of image.
 */
protected String getImageString() {
	return this.imageString;
}
//----------------------------------------------------
/** Used by layout managers to calculate the smallest
 *  size allocated for the Component. Since some
 *  layout managers (e.g. BorderLayout) may
 *  call this before paint is called, you need to
 *  make sure that the image is done loading, which
 *  will force a resize, which determines the values
 *  returned.
 */
public Dimension getMinimumSize() {
	if (!this.doneLoading)
		waitForImage(false);
	return (super.getMinimumSize());
}
//----------------------------------------------------
/** Used by layout managers to calculate the usual
 *  size allocated for the Component. Since some
 *  layout managers (e.g. BorderLayout) may
 *  call this before paint is called, you need to
 *  make sure that the image is done loading, which
 *  will force a resize, which determines the values
 *  returned.
 */
public Dimension getPreferredSize() {
	if (!this.doneLoading)
		waitForImage(false);
	if (this.dimension == null) 
		this.dimension = new Dimension(this.width, this.height);
	return this.dimension;
}
//----------------------------------------------------
// You could just call size().this.width and size().height,
// but since we've overridden resize to record
// this, we might as well use it.

/** Gets the this.width (image this.width plus twice border). */

public int getWidth() {
	return this.width;
}
//----------------------------------------------------
/** Has the ImageLabel been given an explicit size?
 *  This is used to decide if the image should be
 *  stretched or not. This will be true if you
 *  call resize or reshape on the ImageLabel before
 *  adding it to a Container. It will be false
 *  otherwise.
 */
protected boolean hasExplicitSize() {
	return this.explicitSize;
}
/** Draws the image. If you override this in a
 *  subclass, be sure to call super.paint.
 */
public void paint(Graphics g) {
	if (!this.doneLoading)
		waitForImage(true);
	else {
		if (this.explicitSize)
			g.drawImage(
				this.image, 
				this.border, 
				this.border, 
				this.width - 2 * this.border, 
				this.height - 2 * this.border, 
				this); 
		else
			g.drawImage(this.image, this.border, this.border, this);
		drawRect(g, 0, 0, this.width - 1, this.height - 1, this.border, this.borderColor);
	}
}
/** Sets the border thickness. */

public void setBorder(int border) {
	this.border = border;
}
/** Sets the border color. */

public void setBorderColor(Color borderColor) {
	this.borderColor = borderColor;
}
/** Resizes the ImageLabel. If you don't resize the
   *  label explicitly, then what happens depends on
   *  the layout manager. With FlowLayout, as with
   *  FlowLayout for Labels, the ImageLabel takes its
   *  minimum size, just enclosing the image. With
   *  BorderLayout, as with BorderLayout for Labels,
   *  the ImageLabel is expanded to fill the
   *  section. Stretching GIF/JPG files does not always
   *  result in clear looking images. <B>So just as
   *  with builtin Labels and Buttons, don't
   *  use FlowLayout if you don't want the Buttons to
   *  get resized.</B> If you don't use any
   *  LayoutManager, then the ImageLabel will also
   *  just fit the image.
   *  <P>
   *  Note that if you resize explicitly, you must do
   *  it <B>before</B> the ImageLabel is added to the
   *  Container. In such a case, the explicit size
   *  overrides the image dimensions.
   *
   * @see #setSize
   */
public void setBounds(int x, int y, int width, int height) {
	if (!this.doneLoading) {
		this.explicitSize = true;
		if (this.width > 0)
			this.explicitWidth = this.width;
		if (height > 0)
			this.explicitHeight = height;
	}
	super.setBounds(x, y, this.width, height);
}
//----------------------------------------------------
// LayoutManagers (such as BorderLayout) might call
// resize or reshape with only 1 dimension of
// this.width/height non-zero. In such a case, you still
// want the other dimension to come from the image
// itself.

/** Resizes the ImageLabel. If you don't resize the
 *  label explicitly, then what happens depends on
 *  the layout manager. With FlowLayout, as with
 *  FlowLayout for Labels, the ImageLabel takes its
 *  minimum size, just enclosing the image. With
 *  BorderLayout, as with BorderLayout for Labels,
 *  the ImageLabel is expanded to fill the
 *  section. Stretching GIF/JPG files does not always
 *  result in clear looking images. <B>So just as
 *  with builtin Labels and Buttons, don't
 *  use FlowLayout if you don't want the Buttons to
 *  get resized.</B> If you don't use any
 *  LayoutManager, then the ImageLabel will also
 *  just fit the image.
 *  <P>
 *  Note that if you resize explicitly, you must do
 *  it <B>before</B> the ImageLabel is added to the
 *  Container. In such a case, the explicit size
 *  overrides the image dimensions.
 *
 * @see #reshape
 */
public void setSize(int width, int height) {
	if (!this.doneLoading) {
		this.explicitSize = true;
		if (this.width > 0)
			this.explicitWidth = this.width;
		if (height > 0)
			this.explicitHeight = height;
	}
	super.setSize(this.width, height);
}
//----------------------------------------------------
/** Makes sure that the Image associated with the
 *  Canvas is done loading before returning, since
 *  loadImage spins off a separate thread to do the
 *  loading. Once you get around to drawing the
 *  image, this will make sure it is loaded,
 *  waiting if not. The user does not need to call
 *  this at all, but if several ImageLabels are used
 *  in the same Container, this can cause
 *  several repeated layouts, so users might want to
 *  explicitly call this themselves before adding
 *  the ImageLabel to the Container. Another
 *  alternative is to start asynchronous loading by
 *  calling prepareImage on the ImageLabel's
 *  image (see getImage). 
 *
 * @param doLayout Determines if the Container
 *   should be re-laid out after you are finished
 *   waiting. <B>This should be true when called
 *   from user functions</B>, but is set to false
 *   when called from preferredSize to avoid an
 *   infinite loop. This is needed when
 *   using BorderLayout, which calls preferredSize
 *   <B>before</B> calling paint.
 */
public void waitForImage(boolean doLayout) {
	if (!this.doneLoading) {
		try {
			this.tracker.waitForID(this.currentTrackerID);
		} catch (InterruptedException ie) {
			// ignore
		} catch (Exception e) {
			System.out.println("Error loading " + this.imageString + ": " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
			e.printStackTrace();
		}
		if (this.tracker.isErrorID(0))
			new Throwable("Error loading image " + this.imageString).printStackTrace(); //$NON-NLS-1$
		this.doneLoading = true;
		if (this.explicitWidth != 0)
			this.width = this.explicitWidth;
		else
			this.width = this.image.getWidth(this) + 2 * this.border;
		if (this.explicitHeight != 0)
			this.height = this.explicitHeight;
		else
			this.height = this.image.getHeight(this) + 2 * this.border;
		setSize(this.width, this.height);
		// If no parent, you are OK, since it will have
		// been resized before being added. But if
		// parent exists, you have already been added,
		// and the change in size requires re-layout. 
		if (((this.parentContainer = getParent()) != null) && doLayout) {
			setBackground(this.parentContainer.getBackground());
			this.parentContainer.getLayout();
		}
	}
}

public static Image retrieveImage(InputStream in) {
    try {
         if (in == null) {
            System.err.println("Image not found."); //$NON-NLS-1$
            return null;
        }
        byte[] buffer = new byte[in.available()];
        in.read(buffer);
        return Toolkit.getDefaultToolkit().createImage(buffer);
    } catch (IOException e) {
        System.err.println("Unable to read image."); //$NON-NLS-1$
        e.printStackTrace();
    } 
    return null;
}
}
