package org.formulachess.chess.applet.ui;

import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.InputStream;

import org.formulachess.engine.ChessEngine;

public class BoardCanvas extends java.awt.Canvas implements MouseMotionListener, MouseListener {
	private BoardView view;
	private int xOffset, yOffset, currentX, currentY, rowOrigin, colOrigin;
	private Image offscreen;
	private Graphics offGraphics;
	private int[] settings;
	private Image[][] pieces = new Image[8][8];
	private Image board;
	private boolean whiteBottom;
	private boolean dragging;
	private char[][] ASCIIposition;
	private Cursor currentCursor;
	private Image whiteKing;
	private Image whiteKingMate;
	private Image whiteQueen;
	private Image whiteKnight;
	private Image whiteBishop;
	private Image whiteRook;
	private Image whitePawn;
	private Image blackKing;
	private Image blackKingMate;
	private Image blackQueen;
	private Image blackKnight;
	private Image blackBishop;
	private Image blackRook;
	private Image blackPawn;
	private ChessEngine model;
	private boolean initialized;
	private boolean enabled = true;
	private boolean isWhiteMate = false;
	private boolean isBlackMate = false;	
	private String setimages;
	private int playedMove;
	private Frame frame;
	
public BoardCanvas(Frame frame, String setimages, int[] settings) {
	this.frame = frame;
	this.settings = settings;
	this.setimages = setimages;
	this.initialized = false;
	addMouseListener(this);
	addMouseMotionListener(this);
	initImages();
}
public void destroy() {
	removeMouseListener(this);
	removeMouseMotionListener(this);
}
public void init(BoardView v) {
	this.view = v;
	this.whiteBottom = v.getWhiteBottom();
	this.ASCIIposition = v.getASCIIPosition();
	this.model = v.getModel();
	this.initialized = true;
	reset();
	setEnabledDragging(false);
}
public void initImages() {
	MediaTracker mediaTracker = new MediaTracker(this);
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	this.board = retrieveImage(this.setimages + "/board.gif");  //$NON-NLS-1$
	this.whiteKing = retrieveImage(this.setimages + "/wk.gif");  //$NON-NLS-1$
	this.whiteKingMate = retrieveImage(this.setimages + "/wkmat.gif");  //$NON-NLS-1$
	this.whiteQueen = retrieveImage(this.setimages + "/wq.gif");  //$NON-NLS-1$
	this.whiteKnight = retrieveImage(this.setimages + "/wn.gif");  //$NON-NLS-1$
	this.whiteBishop = retrieveImage(this.setimages + "/wb.gif");  //$NON-NLS-1$
	this.whiteRook = retrieveImage(this.setimages + "/wr.gif");  //$NON-NLS-1$
	this.whitePawn = retrieveImage(this.setimages + "/wp.gif");  //$NON-NLS-1$
	this.blackKing = retrieveImage(this.setimages + "/bk.gif");  //$NON-NLS-1$
	this.blackKingMate = retrieveImage(this.setimages + "/bkmat.gif");  //$NON-NLS-1$
	this.blackQueen = retrieveImage(this.setimages + "/bq.gif");  //$NON-NLS-1$
	this.blackKnight = retrieveImage(this.setimages + "/bn.gif");  //$NON-NLS-1$
	this.blackBishop = retrieveImage(this.setimages + "/bb.gif");  //$NON-NLS-1$
	this.blackRook = retrieveImage(this.setimages + "/br.gif");  //$NON-NLS-1$
	this.blackPawn = retrieveImage(this.setimages + "/bp.gif");  //$NON-NLS-1$
	mediaTracker.addImage(this.board, 0);
	mediaTracker.addImage(this.whiteKing, 0);
	mediaTracker.addImage(this.whiteKingMate, 0);	
	mediaTracker.addImage(this.whiteQueen, 0);
	mediaTracker.addImage(this.whiteKnight, 0);
	mediaTracker.addImage(this.whiteBishop, 0);
	mediaTracker.addImage(this.whitePawn, 0);
	mediaTracker.addImage(this.whiteRook, 0);
	mediaTracker.addImage(this.blackKing, 0);
	mediaTracker.addImage(this.blackKingMate, 0);
	mediaTracker.addImage(this.blackQueen, 0);
	mediaTracker.addImage(this.blackKnight, 0);
	mediaTracker.addImage(this.blackRook, 0);
	mediaTracker.addImage(this.blackBishop, 0);
	mediaTracker.addImage(this.blackPawn, 0);
	try {
		mediaTracker.waitForID(0);
	} catch (InterruptedException e) {
		// ignore
	}
}

public Image retrieveImage(String imageLocation) {
    try {
        InputStream in = getClass().getResourceAsStream(imageLocation);
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
public void mouseClicked(MouseEvent e) {
	// click and released quickly
	// called after pressed/released
}
public void mouseDragged(MouseEvent e) {
	// move with pressing a button
	this.currentX = e.getX();
	this.currentY = e.getY();
	repaint();
}
public void mouseEntered(MouseEvent e) {
	// Enter in the canvas
	this.currentCursor = getCursor();
	setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
}
public void mouseExited(MouseEvent e) {
	// Exit in the canvas
	setCursor(this.currentCursor);
}
public void mouseMoved(MouseEvent e) {
	// move without pressing a button
}
public void mousePressed(MouseEvent e) {
	// click one button
/*	if ((model == null) || (whiteBottom && model.turn != Board.WHITE) || (!whiteBottom && model.turn != Board.BLACK)) {
		return;
	}*/
	if (this.model == null) {
		return;
	}
	int x = e.getX();
	int y = e.getY();
	this.colOrigin = (x - this.settings[4]) / this.settings[2];
	this.rowOrigin = (y - this.settings[5]) / this.settings[3];
	if (this.whiteBottom) {
		if (this.ASCIIposition[7 - this.rowOrigin][this.colOrigin] == ' ') {
			return;
		}
	} else {
		if (this.ASCIIposition[this.rowOrigin][7 - this.colOrigin] == ' ') {
			return;
		}
	}
	this.xOffset = x - (this.settings[2] * this.colOrigin + this.settings[4]);
	this.yOffset = y - (this.settings[3] * this.rowOrigin + this.settings[5]);
	this.currentX = x;
	this.currentY = y;
	this.dragging = true;
	repaint();
}
public void mouseReleased(MouseEvent e) {
	// released one button
	if (!this.enabled) {
		this.dragging = false;		
		repaint();
		return;
	}
	int x = e.getX();
	int y = e.getY();
	this.dragging = false;
	if (this.model == null) {
		repaint();
		return;
	}
	int rowEnd = (y - this.settings[5]) / this.settings[3];
	int colEnd = (x - this.settings[4]) / this.settings[2];
	this.playedMove = null;
	if ((rowEnd != this.rowOrigin) || (colEnd != this.colOrigin)) {
		AbstractMove[] moves;
		if (this.whiteBottom) {
			moves = this.model.validateMove(this.colOrigin + 2, 7 - this.rowOrigin + 1, colEnd + 2, 7 - rowEnd + 1);
		} else {
			moves = this.model.validateMove(7 - this.colOrigin + 2, this.rowOrigin + 1, 7 - colEnd + 2, rowEnd + 1);
		}
		if (moves != null) {
			boolean inCheck;
			if (moves.length != 1) {
				// promotion
				PromotionDialog promotionDialog = new PromotionDialog(this.frame, this.setimages, this.model.turn);
				promotionDialog.setVisible(true);
				this.playedMove = moves[promotionDialog.getPromotionIndex()];
				this.model.playMove(this.playedMove);
				inCheck = this.playedMove.getCheck();
			} else {
				this.playedMove = moves[0];
				this.model.playMove(this.playedMove);
				inCheck = this.playedMove.getCheck();
			}
			System.arraycopy(this.model.charPieces, 0, this.ASCIIposition, 0, 8);
			this.isWhiteMate = false;
			this.isBlackMate = false;			
			if (inCheck) {
				if (((this.model.turn == Board.WHITE) && (this.model.checkMateForBlacks())) || ((this.model.turn == Board.BLACK) && (this.model.checkMateForWhites()))) {
					setMate(this.model.turn);
				}
			}
		}
	}
	this.model.playMovesTill(this.model.getTotalMoveCounter());
}
public void paint(Graphics g) {
	update(g);
}
public void reset() {
	this.enabled = true;
	this.isWhiteMate = false;
	this.isBlackMate = false;
}
public void setMate(int color) {
	this.isWhiteMate = color == Board.WHITE;
	this.isBlackMate = !this.isWhiteMate;
}
public void switchSide() {
	this.whiteBottom = !this.whiteBottom;
}
public void update(Graphics g) {
	if (((this.model.turn == Board.WHITE) && (this.model.checkMateForBlacks())) || ((this.model.turn == Board.BLACK) && (this.model.checkMateForWhites()))) {
		setMate(this.model.turn);
	} else {
		this.isBlackMate = false;
		this.isWhiteMate = false;
	}
	if (this.offscreen == null) {
		this.offscreen = createImage(this.settings[0], this.settings[1]);
		this.offGraphics = this.offscreen.getGraphics();
	}
	// draw the board
	for (int i = 0; i < 4; i++) {
		for (int j = 0; j < 4; j++) {
			this.offGraphics.drawImage(this.board, this.settings[2] * 2 * i, this.settings[3] * 2 * j, this);
		}
	}
	this.offGraphics.drawImage(this.board, 0, 0, this);
	if (!this.initialized) {
		g.drawImage(this.offscreen, 0, 0, this);
		return;
	}
	for (int i = 0; i < 8; i++) {
		for (int j = 0; j < 8; j++) {
			switch (this.ASCIIposition[i][j]) {
				case 'r' :
					this.pieces[i][j] = this.blackRook;
					break;
				case 'n' :
					this.pieces[i][j] = this.blackKnight;
					break;
				case 'b' :
					this.pieces[i][j] = this.blackBishop;
					break;
				case 'q' :
					this.pieces[i][j] = this.blackQueen;
					break;
				case 'k' :
					if (this.isBlackMate) {
						this.pieces[i][j] = this.blackKingMate;
					} else {
						this.pieces[i][j] = this.blackKing;
					}
					break;
				case 'p' :
					this.pieces[i][j] = this.blackPawn;
					break;
				case 'R' :
					this.pieces[i][j] = this.whiteRook;
					break;
				case 'N' :
					this.pieces[i][j] = this.whiteKnight;
					break;
				case 'B' :
					this.pieces[i][j] = this.whiteBishop;
					break;
				case 'Q' :
					this.pieces[i][j] = this.whiteQueen;
					break;
				case 'K' :
					if (this.isWhiteMate) {
						this.pieces[i][j] = this.whiteKingMate;
					} else {
						this.pieces[i][j] = this.whiteKing;						
					}
					break;
				case 'P' :
					this.pieces[i][j] = this.whitePawn;
					break;
				default :
					this.pieces[i][j] = null;
					break;
			}
		}
	}	
	if (this.whiteBottom) {
		for (int i = 7; i >= 0; i--) {
			for (int j = 0; j < 8; j++) {
				if (this.pieces[i][j] != null) {
					if (!this.dragging || ((7 - this.rowOrigin) != i) || (j != this.colOrigin)) {
						this.offGraphics.drawImage(this.pieces[i][j], this.settings[4] + j * this.settings[2], this.settings[5] + (7 - i) * this.settings[3], this);
					}
				}
			}
		}
		if (this.dragging) {
			this.offGraphics.drawImage(this.pieces[7 - this.rowOrigin][this.colOrigin], this.currentX - this.xOffset, this.currentY - this.yOffset, this);
		}		
	} else {
		for (int i = 0; i < 8; i++) {
			for (int j = 7; j >= 0; j--) {
				int index = i * 8 +  7 - j;
				if (this.pieces[i][j] != null) {
					if (!this.dragging || (i != this.rowOrigin) || ((7 - this.colOrigin) != j)) {
						this.offGraphics.drawImage(this.pieces[i][j], this.settings[4] + (7 - j) * this.settings[2], this.settings[5] + i * this.settings[3], this);
					}
				}
			}
		}
		if (this.dragging) {
			this.offGraphics.drawImage(this.pieces[this.rowOrigin][7-this.colOrigin], this.currentX - this.xOffset, this.currentY - this.yOffset, this);
		}		
	}
	g.drawImage(this.offscreen, 0, 0, this);
}
public void setEnabledDragging(boolean value) {
	this.enabled = value;
}
}
