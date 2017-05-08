/*
 * Created on Feb 7, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package org.formulachess.ui;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ImageFactory {

	private static final int TRANSPARENCY = 150;

	public Image board;
	public Image whiteKing;
	public Image whiteKingMate;
	public Image whiteQueen;
	public Image whiteKnight;
	public Image whiteRook;
	public Image whiteBishop;
	public Image whitePawn;
	public Image blackKing;
	public Image blackKingMate;
	public Image blackQueen;
	public Image blackKnight;
	public Image blackRook;
	public Image blackBishop;
	public Image blackPawn;
	public Image switchImage;
	public Image control;
	public Image possibleMove;

	public ImageFactory(Display display) {
		// init images
		String imagesDirectory = "images/set3/"; //$NON-NLS-1$
		this.board = new Image(display, ImageFactory.class.getResourceAsStream(imagesDirectory + "board.gif")); //$NON-NLS-1$
		this.blackBishop = new Image(display, ImageFactory.class.getResourceAsStream(imagesDirectory + "bb.gif")); //$NON-NLS-1$
		this.blackKing = new Image(display, ImageFactory.class.getResourceAsStream(imagesDirectory + "bk.gif")); //$NON-NLS-1$
		this.blackKingMate = new Image(display, ImageFactory.class.getResourceAsStream(imagesDirectory + "bkmat.gif")); //$NON-NLS-1$
		this.blackKnight = new Image(display, ImageFactory.class.getResourceAsStream(imagesDirectory + "bn.gif")); //$NON-NLS-1$
		this.blackPawn = new Image(display, ImageFactory.class.getResourceAsStream(imagesDirectory + "bp.gif")); //$NON-NLS-1$
		this.blackQueen = new Image(display, ImageFactory.class.getResourceAsStream(imagesDirectory + "bq.gif")); //$NON-NLS-1$
		this.blackRook = new Image(display, ImageFactory.class.getResourceAsStream(imagesDirectory + "br.gif")); //$NON-NLS-1$

		this.whiteBishop = new Image(display, ImageFactory.class.getResourceAsStream(imagesDirectory + "wb.gif")); //$NON-NLS-1$
		this.whiteKing = new Image(display, ImageFactory.class.getResourceAsStream(imagesDirectory + "wk.gif")); //$NON-NLS-1$
		this.whiteKingMate = new Image(display, ImageFactory.class.getResourceAsStream(imagesDirectory + "wkmat.gif")); //$NON-NLS-1$
		this.whiteKnight = new Image(display, ImageFactory.class.getResourceAsStream(imagesDirectory + "wn.gif")); //$NON-NLS-1$
		this.whitePawn = new Image(display, ImageFactory.class.getResourceAsStream(imagesDirectory + "wp.gif")); //$NON-NLS-1$
		this.whiteQueen = new Image(display, ImageFactory.class.getResourceAsStream(imagesDirectory + "wq.gif")); //$NON-NLS-1$
		this.whiteRook = new Image(display, ImageFactory.class.getResourceAsStream(imagesDirectory + "wr.gif")); //$NON-NLS-1$
		this.switchImage = new Image(display, ImageFactory.class.getResourceAsStream(imagesDirectory + "switch.gif")); //$NON-NLS-1$
		this.control = new Image(display, ImageFactory.class.getResourceAsStream(imagesDirectory + "control.gif")); //$NON-NLS-1$

		PaletteData palette = new PaletteData(new RGB[] { new RGB(255, 0, 0) });
		final int block = Settings.SET3[Settings.BOARD_WIDTH_INDEX];
		ImageData imageData = new ImageData(block, block, 8, palette);
		for (int y = 0; y < block; y++) {
			for (int x = 0; x < block; x++) {
				imageData.setAlpha(x, y, TRANSPARENCY);
			}
		}
		this.possibleMove = new Image(display, imageData);
	}

	public void dispose() {
		this.board.dispose();
		this.blackBishop.dispose();
		this.blackKing.dispose();
		this.blackKingMate.dispose();
		this.blackKnight.dispose();
		this.blackPawn.dispose();
		this.blackQueen.dispose();
		this.blackRook.dispose();

		this.whiteBishop.dispose();
		this.whiteKing.dispose();
		this.whiteKingMate.dispose();
		this.whiteKnight.dispose();
		this.whitePawn.dispose();
		this.whiteQueen.dispose();
		this.whiteRook.dispose();
		this.switchImage.dispose();
		this.control.dispose();

		this.possibleMove.dispose();
	}
}
