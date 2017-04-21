package org.formulachess.chess.applet.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

import org.formulachess.engine.ChessEngine;

public class BoardController extends Panel implements MouseListener, Observer {
	
	public static final int[] DIMENSIONS = { 22, 18, 1}; // width of one square, height, gap between squares

	private Image control;
	private ChessEngine board;
	
	public BoardController() {
		initImages();
		setSize(this.control.getWidth(this), this.control.getHeight(this));
		addMouseListener(this);
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

	public void initImages() {
		MediaTracker tracker = new MediaTracker(this);
		this.control = retrieveImage("sets/common/this.control.gif"); //$NON-NLS-1$
		tracker.addImage(this.control, 0);
		try {
			tracker.waitForID(0);
		} catch (InterruptedException e) {
			// ignore
		}
	}

	/**
	 * @see java.awt.Component#getPreferredSize()
	 */
	public Dimension getPreferredSize() {
		return new Dimension(this.control.getWidth(null), this.control.getHeight(null));
	}

	/**
	 * @see java.awt.Component#paint(Graphics)
	 */
	public void paint(Graphics g) {
		update(g);
	}

	/**
	 * @see java.awt.Component#update(Graphics)
	 */
	public void update(Graphics g) {
		g.drawImage(this.control, 0, 0, this);
	}

	public void setBoard(ChessEngine board) {
		this.board = board;
		board.addObserver(this);
	}
	
	public void play5MovesForward() {
		int moveCounter = this.board.getMoveCounter();
		int totalMoveNumber = this.board.getTotalMoveCounter();
		moveCounter+=10;
		if (moveCounter > totalMoveNumber) {
			moveCounter = totalMoveNumber;
		}
		this.board.playMovesTill(moveCounter);
	}

	public void play5MovesBackward() {
		int moveCounter = this.board.getMoveCounter();
		moveCounter-=10;
		if (moveCounter < 0) {
			moveCounter = 0;
		}
		this.board.playMovesTill(moveCounter);
	}

	public void playMoveForward() {
		int moveCounter = this.board.getMoveCounter();
		int totalMoveNumber = this.board.getTotalMoveCounter();
		moveCounter++;
		if (moveCounter > totalMoveNumber) {
			moveCounter = totalMoveNumber;
		}
		this.board.playMovesTill(moveCounter);
	}
	
	public void playMoveBackward() {
		int moveCounter = this.board.getMoveCounter();
		moveCounter--;
		if (moveCounter < 0) {
			moveCounter = 0;
		}
		this.board.playMovesTill(moveCounter);
	}
	
	public void restartGame() {
		int moveCounter = 0;
		this.board.playMovesTill(moveCounter);
	}

	public void goToEndGame() {
		this.board.playMovesTill(this.board.getTotalMoveCounter());
	}
	
	/**
	 * @see java.awt.event.MouseListener#mouseClicked(MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		// nothing to do
	}

	private boolean insideSquareN(int i, int x, int y) {
		if (y >= 0 && y <= DIMENSIONS[1]) {
			if (x >= (i * 22 + 1) && (x <= ((i + 1) * 22 + 1))) {
				return true;
			}
		}
		return false;
	}
	/**
	 * @see java.awt.event.MouseListener#mouseEntered(MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
		// nothing to do
	}

	/**
	 * @see java.awt.event.MouseListener#mouseExited(MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		// nothing to do
	}

	/**
	 * @see java.awt.event.MouseListener#mousePressed(MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		// nothing to do
	}

	/**
	 * @see java.awt.event.MouseListener#mouseReleased(MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (insideSquareN(0, x, y)) {
			restartGame();
			return;
		}
		if (insideSquareN(1, x, y)) {
			play5MovesBackward();
			return;
		}
		if (insideSquareN(2, x, y)) {
			playMoveBackward();
			return;
		}
		if (insideSquareN(3, x, y)) {
			playMoveForward();
			return;
		}
		if (insideSquareN(4, x, y)) {
			play5MovesForward();
			return;
		}
		if (insideSquareN(5, x, y)) {
			goToEndGame();
		}
	}
	
	public void update(Observable o, Object arg) {
		// nothing to do
	}	
}
