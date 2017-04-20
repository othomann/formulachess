package org.formulachess.chess.applet.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import org.formulachess.chess.applet.pgn.iterator.MoveSelector;
import org.formulachess.chess.applet.pgn.iterator.PGNGameWalker;
import org.formulachess.pgn.ast.Move;

public class MoveCanvas extends Canvas implements MouseListener, Observer {

	private static final int WIDTH = 150;
	private static final int X_ORIGIN = 15;
	private static final int Y_ORIGIN = 15;
	private static final String DOTS = "..."; //$NON-NLS-1$
	private static final String DOT = "."; //$NON-NLS-1$
	private static final String HAS_VARIATIONS = "*"; //$NON-NLS-1$
	
	private int movesNumber;
	private int maxMovesNumber;
	private Font font;
	private Image offscreen;
	private Graphics offGraphics;
	private FontMetrics fontMetrics;
	private Move[] moves;
	private PGNBoard pgnBoard;
	private Rectangle[] rectangles;

	private int whiteMoveStart;
	private int blackMoveStart;
	
	private boolean isFirstMoveWhite;
	
	private int moveIndex;

	private MoveScroller moveListController;
	
	public MoveCanvas(PGNBoard pgnBoard, MoveScroller moveListController) {
		this.pgnBoard = pgnBoard;
		this.moveIndex = pgnBoard.getMoveCounter() - 1;
		this.moveListController = moveListController;
		this.font = new Font("SansSerif", Font.PLAIN, 16); //$NON-NLS-1$
		this.moves = pgnBoard.getPgnGame().getMoveText().getMoves();
		this.rectangles = new Rectangle[this.moves.length];
		this.movesNumber = this.moves.length / 2;
		this.maxMovesNumber = Math.max(this.maxMovesNumber, this.movesNumber);
		addMouseListener(this);
		pgnBoard.addObserver(this);
	}

	public void paint(Graphics g) {
		update(g);
	}

	public void update(Graphics g) {
		this.fontMetrics = g.getFontMetrics(this.font);
		int height = this.fontMetrics.getAscent();
		if (this.offscreen == null) {
			this.offscreen = createImage(getSize().width, getSize().height);
			this.offGraphics = this.offscreen.getGraphics();
		}
		if (this.moves.length != 0) {
			this.offGraphics.setColor(Color.white);
			this.offGraphics.fillRect(0,0,getSize().width, getSize().height);
			this.offGraphics.setColor(Color.black);
			this.isFirstMoveWhite = this.moves[0].isWhiteMove();
			this.whiteMoveStart = X_ORIGIN + this.fontMetrics.stringWidth(Integer.toString(this.movesNumber) + ".  "); //$NON-NLS-1$
			this.blackMoveStart = this.whiteMoveStart + getMaxWidthWhiteMoves();
			int x = X_ORIGIN;
			int y = Y_ORIGIN;
			for (int i = 0, max = this.moves.length; i < max; i++) {
				Move move = this.moves[i];
				String moveNotation = null;
				if (this.isFirstMoveWhite) {
					if (move.isWhiteMove()) {
						// this is a white move to display
						this.offGraphics.drawString(Integer.toString(this.moves[i].getMoveIndication()) + DOT, X_ORIGIN, y);
						if (i == this.moveIndex) {
							this.offGraphics.setColor(Color.red);
						}
						moveNotation = getDisplayString(i);
						this.offGraphics.drawString(moveNotation, this.whiteMoveStart, y);
						this.rectangles[i] = new Rectangle(this.whiteMoveStart, y - height, this.fontMetrics.stringWidth(moveNotation), height);
					} else {
						// this is a black move to display
						moveNotation = getDisplayString(i);
						if (i == this.moveIndex) {
							this.offGraphics.setColor(Color.red);
						}
						this.offGraphics.drawString(moveNotation, this.blackMoveStart, y);
						this.rectangles[i] = new Rectangle(this.blackMoveStart, y - height, this.fontMetrics.stringWidth(moveNotation), height);
						y += height;
					}
				} else{
					if (!move.isWhiteMove()) {
						if (i == 0) {
							// this is a black move to display
							this.offGraphics.drawString(Integer.toString(this.moves[i].getMoveIndication()) + DOT, X_ORIGIN, y);
							if (i == this.moveIndex) {
								this.offGraphics.setColor(Color.red);
							}
							this.offGraphics.drawString(DOTS, this.whiteMoveStart, y);
							moveNotation = getDisplayString(i);
							this.offGraphics.drawString(moveNotation, this.blackMoveStart, y);
							this.rectangles[i] = new Rectangle(this.blackMoveStart, y - height, this.fontMetrics.stringWidth(moveNotation), height);
						} else {
							moveNotation = getDisplayString(i);
							if (i == this.moveIndex) {
								this.offGraphics.setColor(Color.red);
							}
							this.offGraphics.drawString(moveNotation, this.blackMoveStart, y);
							this.rectangles[i] = new Rectangle(this.blackMoveStart, y - height, this.fontMetrics.stringWidth(moveNotation), height);
						}
						y += height;
					} else {
						// this is a white move to display
						this.offGraphics.drawString(Integer.toString(this.moves[i].getMoveIndication()) + DOT, X_ORIGIN, y);
						if (i == this.moveIndex) {
							this.offGraphics.setColor(Color.red);
						}
						moveNotation = getDisplayString(i);
						this.offGraphics.drawString(moveNotation, this.whiteMoveStart, y);
						this.rectangles[i] = new Rectangle(this.whiteMoveStart, y - height, this.fontMetrics.stringWidth(moveNotation), height);
					}
				}
				if (i == this.moveIndex) {
					this.offGraphics.setColor(Color.black);
				}
			}
		}
		this.moveListController.doLayout();
		g.drawImage(this.offscreen, 0, 0, this);		
	}

	private String getDisplayString(int i) {
		StringBuffer buffer = new StringBuffer(this.moves[i].getMoveNotation());
		if (isSpecialMove(this.moves[i])) {
			buffer.append(HAS_VARIATIONS);
		}
		return buffer.toString();
	}

	private boolean isSpecialMove(Move move) {
		return move.hasVariations() || move.isVariationFirstMove();
	}

	private void drawRectangle(Rectangle rectangle, Graphics graphics) {
		this.offGraphics.setColor(Color.blue);
		this.offGraphics.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
		this.offGraphics.setColor(Color.black);
	}
		
	private int getMaxWidthWhiteMoves() {
		int maxValue = 0;
		int initialIndex;
		if (this.isFirstMoveWhite) {
			initialIndex = 0;
		} else {
			initialIndex = 1;
		}
		for (int i = initialIndex, max = this.moves.length; i < max; i+=2) {
			int newValue = this.fontMetrics.stringWidth(getDisplayString(i));
			maxValue = Math.max(maxValue, newValue);
		}
		return maxValue;
	}

	private int getRectangle(int x, int y) {
		int initialIndex;
		if (x > this.blackMoveStart) {
			// we only need to look in black this.rectangles
			if (this.isFirstMoveWhite) {
				initialIndex = 1;
			} else {
				initialIndex = 0;
			}
			for (int i = initialIndex, max = this.rectangles.length; i < max; i+=2) {
				if (this.rectangles[i].contains(x, y)) {
					return i;
				}
			}
		} else if (x > this.whiteMoveStart) {
			// we only need to look in white this.rectangles
			if (this.isFirstMoveWhite) {
				initialIndex = 0;
			} else {
				initialIndex = 1;
			}
			for (int i = initialIndex, max = this.rectangles.length; i < max; i+=2) {
				if (this.rectangles[i].contains(x, y)) {
					return i;
				}
			}
		}
		return -1;
	}	
	/**
	 * @see java.awt.event.MouseListener#mouseClicked(MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		int index = getRectangle(e.getX(), e.getY());
		if (index != -1) {
			Move currentMove = this.moves[index];
			this.moveIndex = index + 1;
			if (isSpecialMove(currentMove)) {
				MoveSelector moveSelector = new MoveSelector(currentMove);
				moveSelector.setVisible(true);
				Move forkingMove = moveSelector.getMove();
				PGNGameWalker walker = new PGNGameWalker(this.pgnBoard.getPgnGame(), forkingMove);
				this.moves = walker.getMoves();
				this.pgnBoard.setMoves(this.moves, this.moveIndex);
				this.rectangles = new Rectangle[this.moves.length];
				this.movesNumber = this.moves.length / 2;
				if (this.movesNumber >= this.maxMovesNumber) {
					this.maxMovesNumber = this.movesNumber;
				}
				this.pgnBoard.playMovesTill(getMoveIndex() + 1);
				repaint();
			} else {
				this.pgnBoard.playMovesTill(getMoveIndex());	
				repaint();
			}
		}
	}

	/**
	 * @see java.awt.event.MouseListener#mouseEntered(MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * @see java.awt.event.MouseListener#mouseExited(MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * @see java.awt.event.MouseListener#mousePressed(MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
	}

	/**
	 * @see java.awt.event.MouseListener#mouseReleased(MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * @see java.awt.Component#getPreferredSize()
	 */
	public Dimension getPreferredSize() {
		Dimension dim = super.getPreferredSize();
		dim.height = 17 * (this.movesNumber + 1);
		return dim;
	}

	public int getMoveIndex() {
		return this.moveIndex;
	}
	/**
	 * Sets the moveIndex.
	 * @param moveIndex The moveIndex to set
	 */
	public void setMoveIndex(int moveIndex) {
		this.moveIndex = moveIndex;
	}

	/**
	 * @see java.util.Observer#update(Observable, Object)
	 */
	public void update(Observable o, Object arg) {
		if (o == pgnBoard) {
			setMoveIndex(pgnBoard.getMoveCounter() - 1);
			repaint();
		}
	}
}
