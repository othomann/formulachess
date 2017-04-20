package org.formulachess.ui;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.formulachess.engine.BoardConstants;
import org.formulachess.engine.ChessEngine;
import org.formulachess.engine.MoveConstants;
import org.formulachess.engine.PieceConstants;

public class BoardCanvas extends Canvas implements Settings, PieceConstants, Observer, BoardConstants {
		
	class Controller implements PaintListener, MouseMoveListener, MouseTrackListener, MouseListener {
		
		ImageFactory imageFactory;
		int[] boardSettings;
		
		Controller(ImageFactory imageFactory, int[] settings) {
			this.imageFactory = imageFactory;
			this.boardSettings = settings;
		}

		/**
		 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseDoubleClick(MouseEvent e) {
			// nothing to do
		}

		/**
		 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseDown(MouseEvent e) {
			if (e.button != 1) {
				return;
			}
			BoardCanvas.this.drag = true;
			BoardCanvas.this.setCursor(BoardCanvas.this.handCursor);
			BoardCanvas.this.xImage = e.x;
			BoardCanvas.this.yImage = e.y;
			int colOrigin = (BoardCanvas.this.xImage - this.boardSettings[BOARD_WIDTH_DELTA_INDEX]) / this.boardSettings[SQUARE_WIDTH_INDEX];
			int rowOrigin = (BoardCanvas.this.yImage - this.boardSettings[BOARD_HEIGHT_DELTA_INDEX]) / this.boardSettings[SQUARE_HEIGHT_INDEX];

			BoardCanvas.this.xOrigin = colOrigin * this.boardSettings[SQUARE_WIDTH_INDEX];
			BoardCanvas.this.yOrigin = rowOrigin * this.boardSettings[SQUARE_HEIGHT_INDEX];

			BoardCanvas.this.xOffset = BoardCanvas.this.xOrigin - BoardCanvas.this.xImage;
			BoardCanvas.this.yOffset = BoardCanvas.this.yOrigin - BoardCanvas.this.yImage;		
			
			if (BoardCanvas.this.orientation == WHITE_BOTTOM) {
				BoardCanvas.this.selectedPieceIndex = colOrigin + rowOrigin * 8;
			} else {
				BoardCanvas.this.selectedPieceIndex = 7 - colOrigin + (7 - rowOrigin) * 8;
			}
			BoardCanvas.this.dragImage = getSelectedPieceImage();
			
			BoardCanvas.this.possibleNextMoves = BoardCanvas.this.model.allMoves(BoardCanvas.this.model.board[BoardCanvas.this.selectedPieceIndex], BoardCanvas.this.selectedPieceIndex);
			if (BoardCanvas.this.possibleNextMoves != null) {
				int row, col;
				for (int i = 0, max = BoardCanvas.this.possibleNextMoves.length; i < max; i++) {
						int endingSquare = (int) ((BoardCanvas.this.possibleNextMoves[i] & MoveConstants.ENDING_SQUARE_MASK) >> MoveConstants.ENDING_SQUARE_SHIFT);
						if (BoardCanvas.this.orientation == WHITE_BOTTOM) {
							row = endingSquare % 8;
							col = endingSquare / 8;
						} else {
							row = 7 - endingSquare % 8;
							col = 7 - endingSquare / 8;
						}
						BoardCanvas.this.redraw(
							this.boardSettings[BOARD_WIDTH_DELTA_INDEX] + row * this.boardSettings[SQUARE_WIDTH_INDEX],
							this.boardSettings[BOARD_HEIGHT_DELTA_INDEX] + col * this.boardSettings[SQUARE_HEIGHT_INDEX],
							this.boardSettings[SQUARE_WIDTH_INDEX],
							this.boardSettings[SQUARE_HEIGHT_INDEX],
							false);
				}
			}
			updateBuffer();	
		}
		/**
		 * @see org.eclipse.swt.events.MouseTrackListener#mouseEnter(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseEnter(MouseEvent e) {
			if (BoardCanvas.this.drag) {
				BoardCanvas.this.xImage = e.x;
				BoardCanvas.this.yImage = e.y;
				BoardCanvas.this.redraw(
						BoardCanvas.this.xImage,
						BoardCanvas.this.yImage,
						this.boardSettings[SQUARE_WIDTH_INDEX],
						this.boardSettings[SQUARE_HEIGHT_INDEX],
						true);
			}
			updateBuffer();				
		}

		/**
		 * @see org.eclipse.swt.events.MouseTrackListener#mouseExit(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseExit(MouseEvent e) {
			if (BoardCanvas.this.drag) {
				int oldX = BoardCanvas.this.xImage;
				int oldY = BoardCanvas.this.yImage;
				BoardCanvas.this.xImage = -1;
				BoardCanvas.this.yImage = -1;
				BoardCanvas.this.redraw(
					oldX,
					oldY,
					this.boardSettings[SQUARE_WIDTH_INDEX],
					this.boardSettings[SQUARE_HEIGHT_INDEX],
					true);
			}
			updateBuffer();				
		}

		/**
		 * @see org.eclipse.swt.events.MouseTrackListener#mouseHover(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseHover(MouseEvent e) {
			// nothing to do
		}
		
		public void mouseMove(MouseEvent e) {
			if (BoardCanvas.this.drag) {
				BoardCanvas.this.redraw(
						BoardCanvas.this.xImage + BoardCanvas.this.xOffset,
						BoardCanvas.this.yImage + BoardCanvas.this.yOffset,
						this.boardSettings[SQUARE_WIDTH_INDEX],
						this.boardSettings[SQUARE_HEIGHT_INDEX],
						false);
				BoardCanvas.this.xImage = e.x;
				BoardCanvas.this.yImage = e.y;
				updateBuffer();
				BoardCanvas.this.redraw(
						BoardCanvas.this.xImage +  BoardCanvas.this.xOffset,
						BoardCanvas.this.yImage + BoardCanvas.this.yOffset,
						this.boardSettings[SQUARE_WIDTH_INDEX],
						this.boardSettings[SQUARE_HEIGHT_INDEX],
						false);
			}
		}

		/**
		 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseUp(MouseEvent e) {
			BoardCanvas.this.possibleNextMoves = null;
			if (!BoardCanvas.this.model.isReady) {
				BoardCanvas.this.drag = false;
				BoardCanvas.this.setCursor(null);
				BoardCanvas.this.selectedPieceIndex = -1;
				BoardCanvas.this.dragImage = null;
	
				BoardCanvas.this.redraw();
				BoardCanvas.this.xImage = -1;
				BoardCanvas.this.yImage = -1;
				BoardCanvas.this.xOrigin = -1;
				BoardCanvas.this.yOrigin = -1;
				updateBuffer();	
				return;
			}
			if (BoardCanvas.this.selectedPieceIndex < 0) {
				return;
			}
			int colOrigin = (BoardCanvas.this.xImage - this.boardSettings[BOARD_WIDTH_DELTA_INDEX]) / this.boardSettings[SQUARE_WIDTH_INDEX];
			int rowOrigin = (BoardCanvas.this.yImage - this.boardSettings[BOARD_HEIGHT_DELTA_INDEX]) / this.boardSettings[SQUARE_HEIGHT_INDEX];

			if (BoardCanvas.this.orientation == WHITE_BOTTOM) {
				BoardCanvas.this.endingSquareIndex = colOrigin + rowOrigin * 8;
			} else {
				BoardCanvas.this.endingSquareIndex = 7 - colOrigin + (7 - rowOrigin) * 8;
			}
			long[] moves = BoardCanvas.this.model.allMoves(BoardCanvas.this.model.board[BoardCanvas.this.selectedPieceIndex]);
			long[] selectedMoves = new long[4];
			int selectedMoveIndex = 0;
			loop: for (int i = 0, max = moves.length; i < max; i++) {
				final long info = moves[i];
				if (((info & MoveConstants.STARTING_SQUARE_MASK) == BoardCanvas.this.selectedPieceIndex)
					&& (((info & MoveConstants.ENDING_SQUARE_MASK) >> MoveConstants.ENDING_SQUARE_SHIFT) == BoardCanvas.this.endingSquareIndex)) {
						if ((info & MoveConstants.PROMOTION_PIECE_MASK) >> MoveConstants.PROMOTION_PIECE_SHIFT != 0) {
							selectedMoves[selectedMoveIndex++]  = moves[i];
						} else {
							selectedMoves[selectedMoveIndex++]  = moves[i];
							break loop;
						}
						if (selectedMoveIndex >= 4) {
							break loop;
						}
					}
			}
			if (selectedMoveIndex > 0) {
				if (selectedMoveIndex > 1) {
					PromotionDialog dialog = new PromotionDialog(BoardCanvas.this.getShell(), BoardCanvas.this.model.turn, this.imageFactory, BoardCanvas.this.model.locale);
					dialog.open();
					int code = dialog.getPromotionCode();
					for (int i = 0; i < selectedMoveIndex; i++) {
						if (((selectedMoves[i] & MoveConstants.PROMOTION_PIECE_MASK) >> MoveConstants.PROMOTION_PIECE_SHIFT) == code) {
							BoardCanvas.this.model.playMove(selectedMoves[i]);
						}
					}
				} else {
					BoardCanvas.this.model.playMove(selectedMoves[0]);
				}
			}
			BoardCanvas.this.drag = false;
			BoardCanvas.this.setCursor(null);
			BoardCanvas.this.selectedPieceIndex = -1;
			BoardCanvas.this.dragImage = null;

			BoardCanvas.this.redraw();
			BoardCanvas.this.xImage = -1;
			BoardCanvas.this.yImage = -1;
			BoardCanvas.this.xOrigin = -1;
			BoardCanvas.this.yOrigin = -1;
			updateBuffer();				
		}

		public void paintControl(PaintEvent e) {
			e.gc.drawImage(
					BoardCanvas.this.doubleBuffer,
					0,
					0
			);
		}
	}

	public static final int BLACK_BOTTOM = 1;
	public static final int WHITE_BOTTOM = 0;

    private Image blackBishop;
    private Image blackKing;
    private Image blackKingMate;
    private Image blackKnight;
    private Image blackPawn;
    private Image blackQueen;
    private Image blackRook;
    private Image whiteBishop;
    private Image whiteKing;
	private Image whiteKingMate;
    private Image whiteKnight;
    private Image whitePawn;
    private Image whiteQueen;
    private Image whiteRook;
    private Image board;
    private Image possibleMove;

	private Rectangle boardSize;
	
    // double-buffering
    Image doubleBuffer;
	boolean drag;
	Image dragImage;
	int endingSquareIndex;
	long[] possibleNextMoves;
	
	// cursor
	Cursor handCursor;
	
	ChessEngine model;
	int orientation;
	
	// layout management
	private Point preferredSize;
    
	// move management
	int selectedPieceIndex;

	// drag and drop management.
	int xImage = -1;
	int xOffset;
	int xOrigin;
	int yImage = -1;
	int yOffset;
	int yOrigin;
	
	int[] settings;
		
	public BoardCanvas(ChessEngine model, Composite composite, int orientation, ImageFactory imageFactory, int[] settings) {
		super(composite, SWT.NONE);
		this.settings = settings;
		this.preferredSize = new Point(this.settings[BOARD_WIDTH_INDEX], this.settings[BOARD_HEIGHT_INDEX]);
		setSize(this.preferredSize);

		this.selectedPieceIndex = -1;
		
		final Display display = getDisplay();
		this.handCursor = new Cursor(display, SWT.CURSOR_HAND);

		this.orientation = orientation;
		// set model
		this.model = model;
		this.model.addObserver(this);

		// init images
		this.board = imageFactory.board;
		this.blackBishop =	imageFactory.blackBishop;
		this.blackKing = imageFactory.blackKing;
		this.blackKingMate = imageFactory.blackKingMate;
		this.blackKnight = imageFactory.blackKnight;
		this.blackPawn = imageFactory.blackPawn;
		this.blackQueen = imageFactory.blackQueen;
		this.blackRook = imageFactory.blackRook;

		this.whiteBishop =	imageFactory.whiteBishop;
		this.whiteKing = imageFactory.whiteKing;
		this.whiteKingMate = imageFactory.whiteKingMate;
		this.whiteKnight = imageFactory.whiteKnight;
		this.whitePawn = imageFactory.whitePawn;
		this.whiteQueen = imageFactory.whiteQueen;
		this.whiteRook = imageFactory.whiteRook;
		this.possibleMove = imageFactory.possibleMove;

		this.boardSize = this.board.getBounds();

		// init double buffer
		this.doubleBuffer = new Image(display, this.settings[BOARD_WIDTH_INDEX], this.settings[BOARD_HEIGHT_INDEX]);
		
		// create controller
		BoardCanvas.Controller controller = new Controller(imageFactory, settings);

		// add listeners
		this.addPaintListener(controller);
		this.addMouseMoveListener(controller);
		this.addMouseListener(controller);
		this.addMouseTrackListener(controller);
		
		this.updateBuffer();
	}
	
	public Point computeSize (int wHint, int hHint, boolean changed) {
		return this.preferredSize;
	}
	
	public void dispose() {
		this.doubleBuffer.dispose();
		this.handCursor.dispose();
	}
	
	Image getSelectedPieceImage() {
		switch(this.model.board[this.selectedPieceIndex]) {
				case WHITE_BISHOP :
					return this.whiteBishop;
				case WHITE_KING :
					return this.whiteKing;
				case WHITE_KNIGHT :
					return this.whiteKnight;
				case WHITE_PAWN :
					return this.whitePawn;
				case WHITE_QUEEN :
					return this.whiteQueen;
				case WHITE_ROOK :
					return this.whiteRook;
				case BLACK_BISHOP :
					return this.blackBishop;
				case BLACK_KING :
					return this.blackKing;
				case BLACK_KNIGHT :
					return this.blackKnight;
				case BLACK_PAWN :
					return this.blackPawn;
				case BLACK_QUEEN :
					return this.blackQueen;
				case BLACK_ROOK :
					return this.blackRook;
				default:
					return null;
			}
	}
	
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public void switchSide() {
		this.orientation = 1 - this.orientation;
	}
	/**
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {
		if (o == this.model) {
			this.redraw(
				this.settings[BOARD_WIDTH_ORIGIN_INDEX],
				this.settings[BOARD_HEIGHT_ORIGIN_INDEX],
				this.settings[BOARD_WIDTH_INDEX],
				this.settings[BOARD_HEIGHT_INDEX],
				true);
			updateBuffer();
		}
	}

	public void updateBuffer() {
		GC gc = new GC(this.doubleBuffer);
		boolean isMate = this.model.isMate();
		//paint chessboard
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				gc.drawImage(
					this.board,
					0,
					0,
					this.boardSize.width,
					this.boardSize.height,
					this.settings[SQUARE_WIDTH_INDEX] * 2 * i,
					this.settings[SQUARE_HEIGHT_INDEX] * 2 * j,
					this.boardSize.width,
					this.boardSize.height);
			}
		}		
		
		int row = 8;
		int col = 0;

		if (this.possibleNextMoves != null) {
			for (int i = 0, max = this.possibleNextMoves.length; i < max; i++) {
					int endingSquare = (int) ((this.possibleNextMoves[i] & MoveConstants.ENDING_SQUARE_MASK) >> MoveConstants.ENDING_SQUARE_SHIFT);
					if (this.orientation == WHITE_BOTTOM) {
						row = endingSquare % 8;
						col = endingSquare / 8;
					} else {
						row = 7 - endingSquare % 8;
						col = 7 - endingSquare / 8;
					}
					gc.drawImage(
						this.possibleMove,
						0,
						0,
						this.settings[SQUARE_WIDTH_INDEX],
						this.settings[SQUARE_HEIGHT_INDEX],
						this.settings[BOARD_WIDTH_DELTA_INDEX] + row * this.settings[SQUARE_WIDTH_INDEX],
						this.settings[BOARD_HEIGHT_DELTA_INDEX] + col * this.settings[SQUARE_HEIGHT_INDEX],
						this.settings[SQUARE_WIDTH_INDEX],
						this.settings[SQUARE_HEIGHT_INDEX]);
			}
		}
		//paint pieces
		loop: for (int i = 0; i < 64; i++) {
			if (i == this.selectedPieceIndex) {
				continue loop;
			}
			if (this.orientation == WHITE_BOTTOM) {
				row = i % 8;
				col = i / 8;
			} else {
				row = 7 - i % 8;
				col = 7 - i / 8;
			}
			Image currentImage = null;
			switch(this.model.board[i]) {
				case WHITE_BISHOP :
					currentImage = this.whiteBishop;
					break;
				case WHITE_KING :
					if (isMate && this.model.turn == BoardConstants.WHITE_TURN) {
						currentImage = this.whiteKingMate;
					} else {
						currentImage = this.whiteKing;
					}
					break;
				case WHITE_KNIGHT :
					currentImage = this.whiteKnight;
					break;
				case WHITE_PAWN :
					currentImage = this.whitePawn;
					break;
				case WHITE_QUEEN :
					currentImage = this.whiteQueen;
					break;
				case WHITE_ROOK :
					currentImage = this.whiteRook;
					break;
				case BLACK_BISHOP :
					currentImage = this.blackBishop;
					break;
				case BLACK_KING :
					if (isMate && this.model.turn == BoardConstants.BLACK_TURN) {
						currentImage = this.blackKingMate;
					} else {
						currentImage = this.blackKing;
					}
					break;
				case BLACK_KNIGHT :
					currentImage = this.blackKnight;
					break;
				case BLACK_PAWN :
					currentImage = this.blackPawn;
					break;
				case BLACK_QUEEN :
					currentImage = this.blackQueen;
					break;
				case BLACK_ROOK :
					currentImage = this.blackRook;
					break;
			}
			if (currentImage != null) {
				gc.drawImage(
					currentImage,
					0,
					0,
					this.settings[SQUARE_WIDTH_INDEX],
					this.settings[SQUARE_HEIGHT_INDEX],
					this.settings[BOARD_WIDTH_DELTA_INDEX] + row * this.settings[SQUARE_WIDTH_INDEX],
					this.settings[BOARD_HEIGHT_DELTA_INDEX] + col * this.settings[SQUARE_HEIGHT_INDEX],
					this.settings[SQUARE_WIDTH_INDEX],
					this.settings[SQUARE_HEIGHT_INDEX]);
			}

		}
		if (this.drag && this.xImage >= 0 && this.yImage >= 0 && this.dragImage != null) {
			gc.drawImage(
				this.dragImage,
				0,
				0,
				this.settings[SQUARE_WIDTH_INDEX],
				this.settings[SQUARE_HEIGHT_INDEX],
				this.xImage + this.xOffset,
				this.yImage + this.yOffset,
				this.settings[SQUARE_WIDTH_INDEX],
				this.settings[SQUARE_HEIGHT_INDEX]);
		}
		gc.dispose();
	}
}
