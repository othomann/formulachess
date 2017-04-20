package org.formulachess.engine;

import java.util.*;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.formulachess.util.Util;

public class ChessEngine extends AbstractChessEngine {

	static final boolean DEBUG = true;
	private static final int HISTORY_DEFAULT_SIZE = 10;
	
	public boolean blackCanCastleKingSide;
	public boolean blackCanCastleQueenSide;
	public int blackKing;

	public int[] board;
	public int enPassantSquare;
	public int fiftyMovesRuleNumber;
	long[] history;
	String[] moveHistory;
	public boolean isReady;
	public int moveNumber;
	
	private int movesCounter;
	public long[] nextMoves;
	public int startingColor;
	public int startingMoveNumber;
	
	public int turn;
	public boolean whiteCanCastleKingSide;
	public boolean whiteCanCastleQueenSide;
	public int whiteKing;
	
	private Messages currentMessages;
	public Locale locale;
	
	public ChessEngine(Locale currentLocale) {
		this.locale = currentLocale;
		this.currentMessages = new Messages(currentLocale);
		decodeFENNotation("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"); //$NON-NLS-1$
		this.isReady = false;
	}

	public ChessEngine(Locale currentLocale, String fenPosition) {
		this.locale = currentLocale;
		this.currentMessages = new Messages(currentLocale);
		decodeFENNotation(fenPosition);
		this.isReady = false;
	}
	
	void addMove(int startingPosition, int endingPosition, int capturePieceType, int promotedPiece) {
		long info = 0;
		info |= startingPosition;
		info |= endingPosition << ENDING_SQUARE_SHIFT;
		info |= capturePieceType << CAPTURE_PIECE_SHIFT;
		info |= promotedPiece << PROMOTION_PIECE_SHIFT;
		switch(this.enPassantSquare) {
			case 16 :
			case 17 :
			case 18 :
			case 19 :
			case 20 :
			case 21 :
			case 22 :
			case 23 :
				info |= (this.enPassantSquare - 15) << EN_PASSANT_SQUARE_SHIFT;
				break;
			case 40 :
			case 41 :
			case 42 :
			case 43 :
			case 44 :
			case 45 :
			case 46 :
			case 47 :
				info |= (this.enPassantSquare - 31) << EN_PASSANT_SQUARE_SHIFT;
		}
		info |= (this.whiteCanCastleKingSide ? 1 : 0) << CASTLING_SHIFT;
		info |= (this.whiteCanCastleQueenSide ? 1 : 0) << (CASTLING_SHIFT + 1);
		info |= (this.blackCanCastleKingSide ? 1 : 0) << (CASTLING_SHIFT + 2);
		info |= (this.blackCanCastleQueenSide ? 1 : 0) << (CASTLING_SHIFT + 3);
	
		try {
			movePiece(info);
			if (this.turn == WHITE_TURN) {
				if (startingPosition == this.whiteKing) {
					if (isWhiteInCheck(endingPosition)) {
						return;
					}
				} else if (isWhiteInCheck(this.whiteKing)) {
					return;
				} 
				if (isBlackInCheck(this.blackKing)) {
					info |= 1 << CHECK_SHIFT;
				}
			}
			if (this.turn == BLACK_TURN) {
				if (startingPosition == this.blackKing) {
					if (isBlackInCheck(endingPosition)) {
						return;
					}
				} else if (isBlackInCheck(this.blackKing)) {
					return;
				}
				if (isWhiteInCheck(this.whiteKing)) {
					info |= 1 << CHECK_SHIFT;
				}
			}
		} finally {
			movePieceBack(info);
		}
		if (this.nextMoves == null) {
			this.nextMoves = new long[3];
		}
		if (this.movesCounter == this.nextMoves.length) {
			System.arraycopy(this.nextMoves, 0, (this.nextMoves = new long[this.movesCounter * 2]), 0, this.movesCounter);
		}
		this.nextMoves[this.movesCounter++] = info;
	}

	private final long[] allBlackMoves() {
		this.movesCounter = 0;
		for (int i = 0; i < 64; i++) {
			int currentPiece = this.board[i];
			if (currentPiece > EMPTY) {
				switch(currentPiece) {
					case BLACK_BISHOP :
						moveBishop(i, false);
						break;
					case BLACK_KNIGHT :
						moveKnight(i, false);
						break;
					case BLACK_ROOK :
						moveRook(i, false);
						break;
					case BLACK_QUEEN :
						moveQueen(i, false);
						break;
					case BLACK_KING :
						moveBlackKing(i);
						break;
					case BLACK_PAWN :
						moveBlackPawn(i);
				}
			}
		}
		return getNextMoves();
	}

	private long[] getNextMoves() {
		if (this.movesCounter == 0) {
			return NO_MOVES;
		}
		long[] moves = new long[this.movesCounter];
		System.arraycopy(this.nextMoves, 0, moves, 0, this.movesCounter);
		return moves;
	}

	private final long[] allBlackMoves(int searchPieceType) {
		this.movesCounter = 0;
		for (int i = 0; i < 64; i++) {
			int currentPiece = this.board[i];
			if (currentPiece == searchPieceType) {
				switch(currentPiece) {
					case BLACK_BISHOP :
						moveBishop(i, false);
						break;
					case BLACK_KNIGHT :
						moveKnight(i, false);
						break;
					case BLACK_ROOK :
						moveRook(i, false);
						break;
					case BLACK_QUEEN :
						moveQueen(i, false);
						break;
					case BLACK_KING :
						moveBlackKing(i);
						break;
					case BLACK_PAWN :
						moveBlackPawn(i);
				}
			}
		}
		return getNextMoves();
	}

	private final long[] allBlackMoves(int searchPieceType, int startingSquare) {
		this.movesCounter = 0;
		switch(searchPieceType) {
			case BLACK_BISHOP :
				moveBishop(startingSquare, false);
				break;
			case BLACK_KNIGHT :
				moveKnight(startingSquare, false);
				break;
			case BLACK_ROOK :
				moveRook(startingSquare, false);
				break;
			case BLACK_QUEEN :
				moveQueen(startingSquare, false);
				break;
			case BLACK_KING :
				moveBlackKing(startingSquare);
				break;
			case BLACK_PAWN :
				moveBlackPawn(startingSquare);
		}
		return getNextMoves();
	}
	
	public long[] allMoves() {
		return this.turn == WHITE_TURN ? allWhiteMoves() : allBlackMoves();
	}

	public long[] allMoves(int pieceType) {
		return this.turn == WHITE_TURN ? allWhiteMoves(pieceType) : allBlackMoves(pieceType);
	}
	
	public long[] allMoves(int pieceType, int startingSquare) {
		return this.turn == WHITE_TURN ? allWhiteMoves(pieceType, startingSquare) : allBlackMoves(pieceType, startingSquare);
	}
	
	private final long[] allWhiteMoves() {
		this.movesCounter = 0;
		for (int i = 0; i < 64; i++) {
			int currentPiece = this.board[i];
			if (currentPiece < EMPTY) {
				switch(currentPiece) {
					case WHITE_BISHOP :
						moveBishop(i, true);
						break;
					case WHITE_KNIGHT :
						moveKnight(i, true);
						break;
					case WHITE_ROOK : 
						moveRook(i, true);
						break;
					case WHITE_QUEEN :
						moveQueen(i, true);
						break;
					case WHITE_KING :
						moveWhiteKing(i);
						break;
					case WHITE_PAWN :
						moveWhitePawn(i);
				}
			}
		}
		return getNextMoves();
	}

	private final long[] allWhiteMoves(int searchPieceType) {
		this.movesCounter = 0;
		for (int i = 0; i < 64; i++) {
			int currentPiece = this.board[i];
			if (currentPiece == searchPieceType) {
				switch(currentPiece) {
					case WHITE_BISHOP :
						moveBishop(i, true);
						break;
					case WHITE_KNIGHT :
						moveKnight(i, true);
						break;
					case WHITE_ROOK : 
						moveRook(i, true);
						break;
					case WHITE_QUEEN :
						moveQueen(i, true);
						break;
					case WHITE_KING :
						moveWhiteKing(i);
						break;
					case WHITE_PAWN :
						moveWhitePawn(i);
				}
			}
		}
		return getNextMoves();
	}

	private final long[] allWhiteMoves(int searchPieceType, int startingSquare) {
		this.movesCounter = 0;
		switch(searchPieceType) {
			case WHITE_BISHOP :
				moveBishop(startingSquare, true);
				break;
			case WHITE_KNIGHT :
				moveKnight(startingSquare, true);
				break;
			case WHITE_ROOK : 
				moveRook(startingSquare, true);
				break;
			case WHITE_QUEEN :
				moveQueen(startingSquare, true);
				break;
			case WHITE_KING :
				moveWhiteKing(startingSquare);
				break;
			case WHITE_PAWN :
				moveWhitePawn(startingSquare);
		}
		return getNextMoves();
	}
	
	private void decodeFENNotation(String fenPosition) {
		this.moveNumber = -1;
		this.history = new long[HISTORY_DEFAULT_SIZE];
		this.moveHistory = new String[HISTORY_DEFAULT_SIZE];
		this.board = new int[64];
		try {
			fenPosition= fenPosition.trim();
			StringTokenizer FENdecoder = new StringTokenizer(fenPosition);
			StringTokenizer tokenizer = new StringTokenizer(FENdecoder.nextToken(), "/"); //$NON-NLS-1$
			int row = 0;
			while (tokenizer.hasMoreTokens()) {
				String temp = tokenizer.nextToken();
				int col = 0;
				for (int index = 0; index < temp.length(); index++) {
					char token = temp.charAt(index);
					switch (token) {
						case 'r' :
							this.board[row * 8 + col] = BLACK_ROOK;
							col++;
							break;
						case 'n' :
							this.board[row * 8 + col] = BLACK_KNIGHT;
							col++;
							break;
						case 'b' :
							this.board[row * 8 + col] = BLACK_BISHOP;
							col++;
							break;
						case 'q' :
							this.board[row * 8 + col] = BLACK_QUEEN;
							col++;
							break;
						case 'k' :
							this.board[row * 8 + col] = BLACK_KING;
							this.blackKing = row * 8 + col;
							col++;
							break;
						case 'p' :
							this.board[row * 8 + col] = BLACK_PAWN;
							col++;
							break;
						case 'R' :
							this.board[row * 8 + col] = WHITE_ROOK;
							col++;
							break;
						case 'N' :
							this.board[row * 8 + col] = WHITE_KNIGHT;
							col++;
							break;
						case 'B' :
							this.board[row * 8 + col] = WHITE_BISHOP;
							col++;
							break;
						case 'Q' :
							this.board[row * 8 + col] = WHITE_QUEEN;
							col++;
							break;
						case 'K' :
							this.board[row * 8 + col] = WHITE_KING;
							this.whiteKing = row * 8 + col;
							col++;
							break;
						case 'P' :
							this.board[row * 8 + col] = WHITE_PAWN;
							col++;
							break;
						default :
							int blank = Character.getNumericValue(token);
							for (int i = 0; i < blank; i++) {
								this.board[row * 8 + i + col] = EMPTY;
							}
							col += blank;
							break;
					}
				}
				row++;
			}
			// now we retrieve who is the next player
			// if the next token is w this is the white player, b 
			// is the black	player.
			if (FENdecoder.nextToken().equals("w")) { //$NON-NLS-1$
				this.turn = WHITE_TURN;
				this.startingColor = WHITE_TURN;
			} else {
				this.turn = BLACK_TURN;
				this.startingColor = BLACK_TURN;
			}
			// now we looking on each side each color can castle
			String castling = FENdecoder.nextToken();
			this.whiteCanCastleQueenSide = castling.indexOf("Q") != -1; //$NON-NLS-1$
			this.whiteCanCastleKingSide= castling.indexOf("K") != -1; //$NON-NLS-1$
			this.blackCanCastleQueenSide= castling.indexOf("q") != -1; //$NON-NLS-1$
			this.blackCanCastleKingSide= castling.indexOf("k") != -1; //$NON-NLS-1$

			String enPassantCoords = FENdecoder.nextToken();
			if (enPassantCoords.equals("-")) { //$NON-NLS-1$
				this.enPassantSquare = 0;
			} else {
				this.enPassantSquare = Converter.squareToInt(enPassantCoords);
			}
			// now we retrieve the half move clock
			this.fiftyMovesRuleNumber = Integer.parseInt(FENdecoder.nextToken());
			this.startingMoveNumber = Integer.parseInt(FENdecoder.nextToken());
		} catch (NoSuchElementException e) {
			System.out.println(this.currentMessages.getString("fen.decoding.error")); //$NON-NLS-1$
		}
	}
	
	public long getLastMove() {
		if (this.moveNumber < 0) {
			return -1;
		}
		return this.history[this.moveNumber];
	}
	
	public void initialize(String fenNotation) {
		decodeFENNotation(fenNotation);
	}
	
	public void initializeToStartingPosition() {
		decodeFENNotation("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"); //$NON-NLS-1$
	}

	public boolean isBlackInCheck() {
		return isBlackInCheck(this.blackKing);
	}
	
	private boolean isBlackInCheck(int square) {
		int currentPosition = TAB_64[square];
		// north_west
		boolean firstSquare = true;
		north_west_next: for (int j = currentPosition - 11; j > 0; j -= 11) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (nextSquare <= EMPTY) {
					if (nextSquare == EMPTY) {
						firstSquare = false;
						continue north_west_next;
					}
					switch(nextSquare) {
						case WHITE_QUEEN :
						case WHITE_BISHOP :
							return true;
						case WHITE_KING :
							if (firstSquare) {
								return true;
							}
							break;
					}
				}
			}
			break north_west_next;
		}

		// south_west
		firstSquare = true;
		south_west_next: for (int j = currentPosition + 9; j < 120; j += 9) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (nextSquare <= EMPTY) {
					if (nextSquare == EMPTY) {
						firstSquare = false;
						continue south_west_next;
					}
					switch(nextSquare) {
						case WHITE_QUEEN :
						case WHITE_BISHOP :
							return true;
						case WHITE_KING :
							if (firstSquare) {
								return true;
							}
							break;
						case WHITE_PAWN :
							if (firstSquare) {
								return true;
							}
					}
				}
			}
			break south_west_next;
		}
		// north_east
		firstSquare = true;
		north_east_next: for (int j = currentPosition - 9; j > 0; j -=9) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (nextSquare <= EMPTY) {
					if (nextSquare == EMPTY) {
						firstSquare = false;
						continue north_east_next;
					}
					switch(nextSquare) {
						case WHITE_QUEEN :
						case WHITE_BISHOP :
							return true;
						case WHITE_KING :
							if (firstSquare) {
								return true;
							}
							break;
					}
				}
			}
			break north_east_next;
		}
		// south_east
		firstSquare = true;
		south_east_next: for (int j = currentPosition + 11; j < 120; j+=11) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (nextSquare <= EMPTY) {
					if (nextSquare == EMPTY) {
						firstSquare = false;
						continue south_east_next;
					}
					switch(nextSquare) {
						case WHITE_QUEEN :
						case WHITE_BISHOP :
							return true;
						case WHITE_KING :
							if (firstSquare) {
								return true;
							}
							break;
						case WHITE_PAWN :
							if (firstSquare) {
								return true;
							}
					}
				}
			}
			break south_east_next;
		}

		// north
		firstSquare = true;
		north_next: for (int j = currentPosition - 10; j > 0; j -= 10) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (nextSquare <= EMPTY) {
					if (nextSquare == EMPTY) {
						firstSquare = false;
						continue north_next;
					}
					switch(nextSquare) {
						case WHITE_QUEEN :
						case WHITE_ROOK :
							return true;
						case WHITE_KING :
							if (firstSquare) {
								return true;
							}
					}
				}
			}
			break north_next;
		}
		// south
		firstSquare = true;
		south_next: for (int j = currentPosition + 10; j < 120; j += 10) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (nextSquare <= EMPTY) {
					if (nextSquare == EMPTY) {
						firstSquare = false;
						continue south_next;
					}
					switch(nextSquare) {
						case WHITE_QUEEN :
						case WHITE_ROOK :
							return true;
						case WHITE_KING :
							if (firstSquare) {
								return true;
							}
					}
				}
			}
			break south_next;
		}
		// west
		firstSquare = true;
		west_next: for (int j = currentPosition - 1; j > 0; j --) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (nextSquare <= EMPTY) {
					if (nextSquare == EMPTY) {
						firstSquare = false;
						continue west_next;
					}
					switch(nextSquare) {
						case WHITE_QUEEN :
						case WHITE_ROOK :
							return true;
						case WHITE_KING :
							if (firstSquare) {
								return true;
							}
					}
				}
			}
			break west_next;
		}
		// east
		firstSquare = true;
		east_next: for (int j = currentPosition + 1; j < 120; j++) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (nextSquare <= EMPTY) {
					if (nextSquare == EMPTY) {
						firstSquare = false;
						continue east_next;
					}
					switch(nextSquare) {
						case WHITE_QUEEN :
						case WHITE_ROOK :
							return true;
						case WHITE_KING :
							if (firstSquare) {
								return true;
							}
					}
				}
			}
			break east_next;
		}
		knight_check: for (int j = 0; j < 8; j++) {
			int nextPosition = TAB_120[currentPosition + KNIGHT_POSITIONS[j]];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (nextSquare <= EMPTY) {
					if (nextSquare == EMPTY) {
						continue knight_check;
					}
					switch(nextSquare) {
						case WHITE_KNIGHT :
							return true;
					}
				}
			}
		}		
		return false;
	}

	/**
	 * @return
	 */
	public boolean isMate() {
		return allMoves().length == 0 
		   && ((this.turn == WHITE_TURN && isWhiteInCheck())
		   || (this.turn == BLACK_TURN && isBlackInCheck()));
	}

	public boolean isWhiteInCheck() {
		return isWhiteInCheck(this.whiteKing);
	}

	private boolean isWhiteInCheck(int square) {
		int currentPosition = TAB_64[square];
		// north_west
		boolean firstSquare = true;
		north_west_next: for (int j = currentPosition - 11; j > 0; j -= 11) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (nextSquare >= EMPTY) {
					if (nextSquare == EMPTY) {
						firstSquare = false;
						continue north_west_next;
					}
					switch(nextSquare) {
						case BLACK_QUEEN :
						case BLACK_BISHOP :
							return true;
						case BLACK_KING :
							if (firstSquare) {
								return true;
							}
							break;
						case BLACK_PAWN :
							if (firstSquare) {
								return true;
							}
					}
				}
			}
			break north_west_next;
		}

		// south_west
		firstSquare = true;
		south_west_next: for (int j = currentPosition + 9; j < 120; j += 9) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (nextSquare >= EMPTY) {
					if (nextSquare == EMPTY) {
						firstSquare = false;
						continue south_west_next;
					}
					switch(nextSquare) {
						case BLACK_QUEEN :
						case BLACK_BISHOP :
							return true;
						case BLACK_KING :
							if (firstSquare) {
								return true;
							}
							break;
					}
				}
			}
			break south_west_next;
		}
		// north_east
		firstSquare = true;
		north_east_next: for (int j = currentPosition - 9; j > 0; j -=9) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (nextSquare >= EMPTY) {
					if (nextSquare == EMPTY) {
						firstSquare = false;
						continue north_east_next;
					}
					switch(nextSquare) {
						case BLACK_QUEEN :
						case BLACK_BISHOP :
							return true;
						case BLACK_KING :
							if (firstSquare) {
								return true;
							}
							break;
						case BLACK_PAWN :
							if (firstSquare) {
								return true;
							}
					}
				}
			}
			break north_east_next;
		}
		// south_east
		firstSquare = true;
		south_east_next: for (int j = currentPosition + 11; j < 120; j+=11) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (nextSquare >= EMPTY) {
					if (nextSquare == EMPTY) {
						firstSquare = false;
						continue south_east_next;
					}
					switch(nextSquare) {
						case BLACK_QUEEN :
						case BLACK_BISHOP :
							return true;
						case BLACK_KING :
							if (firstSquare) {
								return true;
							}
							break;
					}
				}
			}
			break south_east_next;
		}

		// north
		firstSquare = true;
		north_next: for (int j = currentPosition - 10; j > 0; j -= 10) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (nextSquare >= EMPTY) {
					if (nextSquare == EMPTY) {
						firstSquare = false;
						continue north_next;
					}
					switch(nextSquare) {
						case BLACK_QUEEN :
						case BLACK_ROOK :
							return true;
						case BLACK_KING :
							if (firstSquare) {
								return true;
							}
					}
				}
			}
			break north_next;
		}
		// south
		firstSquare = true;
		south_next: for (int j = currentPosition + 10; j < 120; j += 10) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (nextSquare >= EMPTY) {
					if (nextSquare == EMPTY) {
						firstSquare = false;
						continue south_next;
					}
					switch(nextSquare) {
						case BLACK_QUEEN :
						case BLACK_ROOK :
							return true;
						case BLACK_KING :
							if (firstSquare) {
								return true;
							}
					}
				}
			}
			break south_next;
		}
		// west
		firstSquare = true;
		west_next: for (int j = currentPosition - 1; j > 0; j --) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (nextSquare >= EMPTY) {
					if (nextSquare == EMPTY) {
						firstSquare = false;
						continue west_next;
					}
					switch(nextSquare) {
						case BLACK_QUEEN :
						case BLACK_ROOK :
							return true;
						case BLACK_KING :
							if (firstSquare) {
								return true;
							}
					}
				}
			}
			break west_next;
		}
		// east
		firstSquare = true;
		east_next: for (int j = currentPosition + 1; j < 120; j++) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (nextSquare >= EMPTY) {
					if (nextSquare == EMPTY) {
						firstSquare = false;
						continue east_next;
					}
					switch(nextSquare) {
						case BLACK_QUEEN :
						case BLACK_ROOK :
							return true;
						case BLACK_KING :
							if (firstSquare) {
								return true;
							}
					}
				}
			}
			break east_next;
		}
		knight_check: for (int j = 0; j < 8; j++) {
			int nextPosition = TAB_120[currentPosition + KNIGHT_POSITIONS[j]];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (nextSquare >= EMPTY) {
					if (nextSquare == EMPTY) {
						continue knight_check;
					}
					switch(nextSquare) {
						case BLACK_KNIGHT :
							return true;
					}
				}
			}
		}		
		return false;
	}
	
	final private void moveBishop(int i, boolean isWhite) {
		int currentPosition = TAB_64[i];
		// north_west
		north_west_next : for (int j = currentPosition - 11; j > 0; j -= 11) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare >= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue north_west_next;
						}
					}
				} else {
					if (nextSquare <= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue north_west_next;
						}
					}
				}
			}
			break north_west_next;
		}
		// south_west
		south_west_next : for (int j = currentPosition + 9; j < 120; j += 9) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare >= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue south_west_next;
						}
					}
				} else {
					if (nextSquare <= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue south_west_next;
						}
					}
				}
			}
			break south_west_next;
		}
		// north_east
		north_east_next : for (int j = currentPosition - 9; j > 0; j -= 9) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare >= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue north_east_next;
						}
					}
				} else {
					if (nextSquare <= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue north_east_next;
						}
					}
				}
			}
			break north_east_next;
		}
		// south_east
		south_east_next : for (int j = currentPosition + 11; j < 120; j += 11) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare >= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue south_east_next;
						}
					}
				} else {
					if (nextSquare <= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue south_east_next;
						}
					}
				}
			}
			break south_east_next;
		}
	}
	
	final private void moveBlackKing(int i) {

		// all squares around the king + castling
		int currentPosition = TAB_64[i];
		for (int j = 0; j < 8; j++) {
			int nextPosition = TAB_120[currentPosition + KING_POSITIONS[j]];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (nextSquare <= EMPTY) {
					addMove(i, nextPosition, nextSquare, 0);
				}
			}
		}
		if (this.blackCanCastleKingSide && !isBlackInCheck()) {
			if (this.board[i + 2] == EMPTY
				&& this.board[i + 1] == EMPTY
				&& !isBlackInCheck(i + 2)
				&& !isBlackInCheck(i + 1)
				&& this.board[i + 3] == BLACK_ROOK) {
				addMove(i, i + 2, EMPTY, 0);
			}
		}
		if (this.blackCanCastleQueenSide && !isBlackInCheck()) {
			if (this.board[i - 3] == EMPTY
				&& this.board[i - 2] == EMPTY
				&& this.board[i - 1] == EMPTY
				&& !isBlackInCheck(i - 2)
				&& !isBlackInCheck(i - 1)
				&& this.board[i - 4] == BLACK_ROOK) {
				addMove(i, i - 2, EMPTY, 0);
			}
		}
	}

	final private void moveBlackPawn(int i) {

		int currentPosition = TAB_64[i];
		// one square or two squares if it has not moved yet.
		if (i >= 8 && i <= 15) {
			int nextPosition = TAB_120[currentPosition + 10];
			if (this.board[nextPosition] == EMPTY) {
				addMove(i, nextPosition, EMPTY, 0);
				nextPosition = TAB_120[currentPosition + 20];
				if (this.board[nextPosition] == EMPTY) {
					addMove(i, nextPosition, EMPTY, 0);
				}
			}
		} else {
			int nextPosition = TAB_120[currentPosition + 10];
			if (nextPosition > 0 && this.board[nextPosition] == EMPTY) {
				if (nextPosition >= 56) {
					// this is a promotion
					for (int pieceType = PieceConstants.BLACK_QUEEN; pieceType <= PieceConstants.BLACK_KNIGHT; pieceType++) {
						addMove(i, nextPosition, EMPTY, pieceType);
					}
				} else {
					addMove(i, nextPosition, EMPTY, 0);
				}
			}
		}
		// capture on the side
		int nextPosition = TAB_120[currentPosition + 11];
		if (nextPosition > 0) {
			int nextSquare = this.board[nextPosition];
			if (nextSquare < EMPTY) {
				if (nextPosition >= 56) {
					// this is a promotion
					for (int pieceType = PieceConstants.BLACK_QUEEN; pieceType <= PieceConstants.BLACK_KNIGHT; pieceType++) {
						addMove(i, nextPosition, nextSquare, pieceType);
					}
				} else {
					addMove(i, nextPosition, nextSquare, 0);
				}
			} else if (i >= 32 && i <= 39 && nextPosition == this.enPassantSquare) {
				// en passant
				addMove(i, nextPosition, this.board[TAB_120[currentPosition + 1]], 0);
			}
		}
		nextPosition = TAB_120[currentPosition + 9];
		if (nextPosition > 0) {
			int nextSquare = this.board[nextPosition];
			if (nextSquare < EMPTY) {
				if (nextPosition >= 56) {
					// this is a promotion
					for (int pieceType = PieceConstants.BLACK_QUEEN; pieceType <= PieceConstants.BLACK_KNIGHT; pieceType++) {
						addMove(i, nextPosition, nextSquare, pieceType);
					}
				} else {
					addMove(i, nextPosition, nextSquare, 0);
				}
			} else if (i >= 32 && i <= 39 && nextPosition == this.enPassantSquare) {
				//en passant
				addMove(i, nextPosition, this.board[TAB_120[currentPosition - 1]], 0);
			}
		}
	}

	final private void moveKnight(int i, boolean isWhite) {
		int currentPosition = TAB_64[i];
		for (int j = 0; j < 8; j++) {
			int nextPosition = TAB_120[currentPosition + KNIGHT_POSITIONS[j]];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare >= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
					}
				} else {
					if (nextSquare <= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
					}
				}
			}
		}
	}
	
	final private void movePiece(long move) {
		int startingSquare = (int) (move & STARTING_SQUARE_MASK);
		int endingSquare = (int) ((move & ENDING_SQUARE_MASK) >> ENDING_SQUARE_SHIFT);
		int pieceType = this.board[startingSquare];

		// common behavior
		this.board[startingSquare] = EMPTY;
		this.board[endingSquare] = pieceType;
		switch(pieceType) {
			case WHITE_KING:
				if (((startingSquare - endingSquare) == 2) || ((startingSquare - endingSquare) == -2)) {
					if (startingSquare == 60) {
						switch(endingSquare) {
							case 62 :
								this.board[61] = WHITE_ROOK;
								this.board[63] = EMPTY;
								break;
							case 58 :
								this.board[59] = WHITE_ROOK;
								this.board[56] = EMPTY;
						}
					}
				}
				this.whiteKing = endingSquare;
				break;
			case BLACK_KING :
				if (((startingSquare - endingSquare) == 2) || ((startingSquare - endingSquare) == -2)) {
					if (startingSquare == 4) {
						switch(endingSquare) {
							case 6 :
								this.board[5] = BLACK_ROOK;
								this.board[7] = EMPTY;
								break;
							case 2 :
								this.board[3] = BLACK_ROOK;
								this.board[0] = EMPTY;
						}
					}
				}
				this.blackKing = endingSquare;
				break;
			case WHITE_PAWN :
				if ((move & PROMOTION_PIECE_MASK) != 0) {
						// promotion
					this.board[endingSquare] = (int) ((move & PROMOTION_PIECE_MASK) >> PROMOTION_PIECE_SHIFT);
				}
				if (this.enPassantSquare != 0 && endingSquare == this.enPassantSquare) {
					switch(endingSquare - startingSquare) {
						case -7 :
							this.board[startingSquare + 1] = EMPTY;
							break;
						case -9:
							this.board[startingSquare - 1] = EMPTY;
							break;
					}
				}
				break;
			case BLACK_PAWN :
				if ((move & PROMOTION_PIECE_MASK) != 0) {
						// promotion
					this.board[endingSquare] = (int) ((move & PROMOTION_PIECE_MASK) >> PROMOTION_PIECE_SHIFT);
				}
				if (endingSquare == this.enPassantSquare) {
					switch(endingSquare - startingSquare) {
						case 9 :
							this.board[startingSquare + 1] = EMPTY;
							break;
						case 7 :
							this.board[startingSquare - 1] = EMPTY;
							break;
					}
				}
		}
	}
	
	final private void movePieceBack(long move) {
		int startingSquare = (int) (move & STARTING_SQUARE_MASK);
		int endingSquare = (int) ((move & ENDING_SQUARE_MASK) >> ENDING_SQUARE_SHIFT);
		int pieceType = this.board[endingSquare];
		int capturedPiece = (int) ((move & CAPTURE_PIECE_MASK) >> CAPTURE_PIECE_SHIFT);

		// common behavior
		this.board[startingSquare] = pieceType;
		this.board[endingSquare] = capturedPiece;

		// handle promotion
		int promotedPiece = (int) ((move & PROMOTION_PIECE_MASK) >> PROMOTION_PIECE_SHIFT);
		if (promotedPiece != 0) {
			if (promotedPiece < EMPTY) {
				this.board[startingSquare] = WHITE_PAWN;
			} else {
				this.board[startingSquare] = BLACK_PAWN;
			}
		}
		
		// detect castling and update the this.board
		switch(pieceType) {
			case WHITE_KING:
				if (((startingSquare - endingSquare) == 2) || ((startingSquare - endingSquare) == -2)) {
					if (startingSquare == 60) {
						switch(endingSquare) {
							case 62 :
								this.board[63] = WHITE_ROOK;
								this.board[61] = EMPTY;
								break;
							case 58 :
								this.board[56] = WHITE_ROOK;
								this.board[59] = EMPTY;
						}
					}
				}
				this.whiteKing = startingSquare;
				break;
			case BLACK_KING :
				if (((startingSquare - endingSquare) == 2) || ((startingSquare - endingSquare) == -2)) {
					if (startingSquare == 4) {
						switch(endingSquare) {
							case 6 :
								this.board[7] = BLACK_ROOK;
								this.board[5] = EMPTY;
								break;
							case 2 :
								this.board[0] = BLACK_ROOK;
								this.board[3] = EMPTY;
						}
					}
				}
				this.blackKing = startingSquare;
				break;
			case WHITE_PAWN :
				if (this.enPassantSquare != 0 && endingSquare == this.enPassantSquare) {
					switch(endingSquare - startingSquare) {
						case -7 :
							this.board[startingSquare + 1] = capturedPiece;
							break;
						case -9:
							this.board[startingSquare - 1] = capturedPiece;
							break;
					}
					this.board[endingSquare] = EMPTY;
				}
				break;
			case BLACK_PAWN :
				if (endingSquare == this.enPassantSquare) {
					switch(endingSquare - startingSquare) {
						case 9 :
							this.board[startingSquare + 1] = capturedPiece;
							break;
						case 7 :
							this.board[startingSquare - 1] = capturedPiece;
							break;
					}
					this.board[endingSquare] = EMPTY;
				}
				break;
		}
	}
	
	final private void moveQueen(int i, boolean isWhite) {
		int currentPosition = TAB_64[i];
		// north_west
		north_west_next : for (int j = currentPosition - 11; j > 0; j -= 11) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare >= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue north_west_next;
						}
					}
				} else {
					if (nextSquare <= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue north_west_next;
						}
					}
				}
			}
			break north_west_next;
		}
		// south_west
		south_west_next : for (int j = currentPosition + 9; j < 120; j += 9) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare >= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue south_west_next;
						}
					}
				} else {
					if (nextSquare <= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue south_west_next;
						}
					}
				}
			}
			break south_west_next;
		}
		// north_east
		north_east_next : for (int j = currentPosition - 9; j > 0; j -= 9) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare >= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue north_east_next;
						}
					}
				} else {
					if (nextSquare <= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue north_east_next;
						}
					}
				}
			}
			break north_east_next;
		}
		// south_east
		south_east_next : for (int j = currentPosition + 11; j < 120; j += 11) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare >= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue south_east_next;
						}
					}
				} else {
					if (nextSquare <= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue south_east_next;
						}
					}
				}
			}
			break south_east_next;
		}

		// north
		north_next : for (int j = currentPosition - 10; j > 0; j -= 10) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare >= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue north_next;
						}
					}
				} else {
					if (nextSquare <= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue north_next;
						}
					}
				}
			}
			break north_next;
		}
		// south
		south_next : for (int j = currentPosition + 10; j < 120; j += 10) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare >= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue south_next;
						}
					}
				} else {
					if (nextSquare <= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue south_next;
						}
					}
				}
			}
			break south_next;
		}
		// west
		west_next : for (int j = currentPosition - 1; j > 0; j--) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare >= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue west_next;
						}
					}
				} else {
					if (nextSquare <= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue west_next;
						}
					}
				}
			}
			break west_next;
		}
		// east
		east_next : for (int j = currentPosition + 1; j < 120; j++) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare >= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue east_next;
						}
					}
				} else {
					if (nextSquare <= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue east_next;
						}
					}
				}
			}
			break east_next;
		}
	}

	final private void moveRook(int i, boolean isWhite) {

		int currentPosition = TAB_64[i];
		// north
		north_next : for (int j = currentPosition - 10; j > 0; j -= 10) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare >= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue north_next;
						}
					}
				} else {
					if (nextSquare <= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue north_next;
						}
					}
				}
			}
			break north_next;
		}
		// south
		south_next : for (int j = currentPosition + 10; j < 120; j += 10) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare >= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue south_next;
						}
					}
				} else {
					if (nextSquare <= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue south_next;
						}
					}
				}
			}
			break south_next;
		}
		// west
		west_next : for (int j = currentPosition - 1; j > 0; j--) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare >= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue west_next;
						}
					}
				} else {
					if (nextSquare <= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue west_next;
						}
					}
				}
			}
			break west_next;
		}
		// east
		east_next : for (int j = currentPosition + 1; j < 120; j++) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare >= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue east_next;
						}
					}
				} else {
					if (nextSquare <= EMPTY) {
						addMove(i, nextPosition, nextSquare, 0);
						if (nextSquare == EMPTY) {
							continue east_next;
						}
					}
				}
			}
			break east_next;
		}

	}
	
	final private void moveWhiteKing(int i) {
		// all squares around the king + castling
		int currentPosition = TAB_64[i];
		for (int j = 0; j < 8; j++) {
			int nextPosition = TAB_120[currentPosition + KING_POSITIONS[j]];
			if (nextPosition >= 0) {
				final int nextSquare = this.board[nextPosition];
				if (nextSquare >= EMPTY) {
					addMove(i, nextPosition, nextSquare, 0);
				}
			}
		}
		if (this.whiteCanCastleKingSide && !isWhiteInCheck()) {
			if (this.board[i + 2] == EMPTY
				&& this.board[i + 1] == EMPTY
				&& !isWhiteInCheck(i + 2)
				&& !isWhiteInCheck(i + 1)
				&& this.board[i + 3] == WHITE_ROOK) {
					addMove(i, i + 2, EMPTY, 0);
			}
		}
		if (this.whiteCanCastleQueenSide && !isWhiteInCheck()) {
			if (this.board[i - 3] == EMPTY
				&& this.board[i - 2] == EMPTY
				&& this.board[i - 1] == EMPTY
				&& !isWhiteInCheck(i - 2)
				&& !isWhiteInCheck(i - 1)
				&& this.board[i - 4] == WHITE_ROOK) {
					addMove(i, i - 2, EMPTY, 0);
			}
		}
	}
	
	final private void moveWhitePawn(int i) {

		int currentPosition = TAB_64[i];
		// one square or two squares if it has not moved yet.
		if (i >= 48 && i <= 55) {
			int nextPosition = TAB_120[currentPosition - 10];
			if (this.board[nextPosition] == EMPTY) {
				addMove(i, nextPosition, EMPTY, 0);
				nextPosition = TAB_120[currentPosition - 20];
				if (this.board[nextPosition] == EMPTY) {
					addMove(i, nextPosition, EMPTY, 0);
				}
			}
		} else {
			int nextPosition = TAB_120[currentPosition - 10];
			if (nextPosition >= 0 && this.board[nextPosition] == EMPTY) {
				if (nextPosition <= 7) {
					// this is a promotion
					for (int pieceType = PieceConstants.WHITE_QUEEN; pieceType <= PieceConstants.WHITE_KNIGHT; pieceType++) {
						addMove(i, nextPosition, EMPTY, pieceType);
					}
				} else {
					addMove(i, nextPosition, EMPTY, 0);
				}
			}
		}
		// capture on the side
		int nextPosition = TAB_120[currentPosition - 11];
		if (nextPosition >= 0) {
			int nextSquare = this.board[nextPosition];
			if (nextSquare > EMPTY) {
				if (nextPosition <= 7) {
					// this is a promotion
					for (int pieceType = PieceConstants.WHITE_QUEEN; pieceType <= PieceConstants.WHITE_KNIGHT; pieceType++) {
						addMove(i, nextPosition, nextSquare, pieceType);
					}
				} else {
					addMove(i, nextPosition, nextSquare, 0);
				}
			} else if (i >= 24 && i <= 31 && nextPosition == this.enPassantSquare) {
				// en passant
				addMove(i, nextPosition, this.board[TAB_120[currentPosition - 1]], 0);
			}
		}
		nextPosition = TAB_120[currentPosition - 9];
		if (nextPosition >= 0) {
			int nextSquare = this.board[nextPosition];
			if (nextSquare > EMPTY) {
				if (nextPosition <= 7) {
					// this is a promotion
					for (int pieceType = PieceConstants.WHITE_QUEEN; pieceType <= PieceConstants.WHITE_KNIGHT; pieceType++) {
						addMove(i, nextPosition, nextSquare, pieceType);
					}
				} else {
					addMove(i, nextPosition, nextSquare, 0);
				}
			} else if (i >= 24 && i <= 31 && nextPosition == this.enPassantSquare) {
				//en passant
				addMove(i, nextPosition, this.board[TAB_120[currentPosition + 1]], 0);
			}
		}
	}

	public static long perft(String FENNotation, int depth) {
		ChessEngine model = new ChessEngine(Locale.getDefault(), FENNotation);
		return model.perft(depth);
	}

	public void playMove(long move) {
		this.playMoveWithoutNotification(move);
		this.update();
	}

	public void playMoveWithoutNotification(long move) {
		if (this.history.length == this.moveNumber + 1) {
			// resize
			System.arraycopy(this.history, 0, (this.history = new long[this.history.length + HISTORY_DEFAULT_SIZE]), 0, this.moveNumber + 1);
			System.arraycopy(this.moveHistory, 0, (this.moveHistory = new String[this.history.length + HISTORY_DEFAULT_SIZE]), 0, this.moveNumber + 1);
		}
		++this.moveNumber;
		this.history[this.moveNumber] = move;
		this.startingMoveNumber++;
		this.moveHistory[this.moveNumber] = Converter.moveToString(this.board, move);
						
		if (this.turn == WHITE_TURN) {
			this.turn = BLACK_TURN;
		} else {
			this.turn = WHITE_TURN;
		}
		final long info = move;
		int startingSquare = (int) (info & STARTING_SQUARE_MASK);
		int endingSquare = (int) ((info & ENDING_SQUARE_MASK) >> ENDING_SQUARE_SHIFT);
		int pieceType = this.board[startingSquare];

		// common behavior
		this.board[startingSquare] = EMPTY;
		this.board[endingSquare] = pieceType;
		
		// detect castling and update the this.board
		switch(pieceType) {
			case WHITE_KING:
				if (((startingSquare - endingSquare) == 2) || ((startingSquare - endingSquare) == -2)) {
					if (startingSquare == 60) {
						switch(endingSquare) {
							case 62 :
								this.board[61] = WHITE_ROOK;
								this.board[63] = EMPTY;
								break;
							case 58 :
								this.board[59] = WHITE_ROOK;
								this.board[56] = EMPTY;
						}
					}
				}
				this.whiteKing = endingSquare;
				this.whiteCanCastleKingSide = false;
				this.whiteCanCastleQueenSide = false;
				this.enPassantSquare = 0;
				break;
			case BLACK_KING :
				if (((startingSquare - endingSquare) == 2) || ((startingSquare - endingSquare) == -2)) {
					if (startingSquare == 4) {
						switch(endingSquare) {
							case 6 :
								this.board[5] = BLACK_ROOK;
								this.board[7] = EMPTY;
								break;
							case 2 :
								this.board[3] = BLACK_ROOK;
								this.board[0] = EMPTY;
						}
					}
				}
				this.blackCanCastleKingSide = false;
				this.blackCanCastleQueenSide = false;
				this.enPassantSquare = -1;
				this.blackKing = endingSquare;
				break;
			case WHITE_PAWN :
				if ((info & PROMOTION_PIECE_MASK) != 0) {
						// promotion
					this.board[endingSquare] = (int) ((info & PROMOTION_PIECE_MASK) >> PROMOTION_PIECE_SHIFT);
				}
				if (endingSquare == this.enPassantSquare) {
					switch(endingSquare - startingSquare) {
						case -7 :
							this.board[startingSquare + 1] = EMPTY;
							break;
						case -9:
							this.board[startingSquare - 1] = EMPTY;
							break;
					}
				}
				if (startingSquare - endingSquare == 16) {
					this.enPassantSquare = startingSquare - 8;
				} else {
					this.enPassantSquare = 0;
				}
				break;
			case BLACK_PAWN :
				if ((info & PROMOTION_PIECE_MASK) != 0) {
						// promotion
					this.board[endingSquare] = (int) ((info & PROMOTION_PIECE_MASK) >> PROMOTION_PIECE_SHIFT);
				}
				if (endingSquare == this.enPassantSquare) {
					switch(endingSquare - startingSquare) {
						case 9 :
							this.board[startingSquare + 1] = EMPTY;
							break;
						case 7 :
							this.board[startingSquare - 1] = EMPTY;
							break;
					}
				}
				if (endingSquare - startingSquare == 16) {
					this.enPassantSquare = endingSquare - 8;
				} else {
					this.enPassantSquare = 0;
				}
				break;
			case WHITE_ROOK :
				if (startingSquare == 63) {
					this.whiteCanCastleKingSide = false;
				} else if (startingSquare == 56) {
					this.whiteCanCastleQueenSide = false;
				}
				this.enPassantSquare = 0;
				break;
			case BLACK_ROOK :
				if (startingSquare == 7) {
					this.blackCanCastleKingSide = false;
				} else if (startingSquare == 0) {
					this.blackCanCastleQueenSide = false;
				}
				this.enPassantSquare = 0;
				break;
			default:
				this.enPassantSquare = 0;
		}
	}
	
	public String toFENNotation() {
		StringBuffer buffer = new StringBuffer();
		int empty = 0;
		for (int i = 0; i < 64; i++) {
			if (i % 8 == 0 && i > 0) {
				if (empty != 0) {
					buffer.append(empty);
					empty = 0;
				} 
				buffer.append("/"); //$NON-NLS-1$
			}
			switch(this.board[i]) {
				case WHITE_BISHOP :
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					} 
					buffer.append('B');
					break;
				case WHITE_KING :
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					} 
					buffer.append('K');
					break;
				case WHITE_KNIGHT :
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					} 
					buffer.append('N');
					break;
				case WHITE_PAWN :
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					} 
					buffer.append('P');
					break;
				case WHITE_QUEEN :
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					} 
					buffer.append('Q');
					break;
				case WHITE_ROOK :
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					} 
					buffer.append('R');
					break;
				case BLACK_BISHOP :
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					} 
					buffer.append('b');
					break;
				case BLACK_KING :
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					} 
					buffer.append('k');
					break;
				case BLACK_KNIGHT :
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					} 
					buffer.append('n');
					break;
				case BLACK_PAWN :
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					} 
					buffer.append('p');
					break;
				case BLACK_QUEEN :
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					} 
					buffer.append('q');
					break;
				case BLACK_ROOK :
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					} 
					buffer.append('r');
					break;
				case EMPTY :
					empty++;
					break;
			}
		}
		if (empty != 0) {
			buffer.append(empty);
			empty = 0;
		}
		if (this.turn == WHITE_TURN) {
			buffer.append(" w "); //$NON-NLS-1$
		} else {
			buffer.append(" b "); //$NON-NLS-1$
		}
		boolean hasCastle = false;
		if (this.whiteCanCastleKingSide) {
			hasCastle = true;
			buffer.append('K');
		}
		if (this.whiteCanCastleQueenSide) {
			hasCastle = true;
			buffer.append('Q');
		}
		if (this.blackCanCastleKingSide) {
			hasCastle = true;
			buffer.append('k');
		}
		if (this.blackCanCastleQueenSide) {
			hasCastle = true;
			buffer.append('q');
		}
		if (!hasCastle) {
			buffer.append("-"); //$NON-NLS-1$
		}
		if (this.enPassantSquare != 0) {
			buffer.append(" " + Converter.intToSquare(this.enPassantSquare)); //$NON-NLS-1$
		} else {
			buffer.append(" -"); //$NON-NLS-1$
		}
		buffer.append(" " + this.fiftyMovesRuleNumber + " " + this.startingMoveNumber); //$NON-NLS-1$ //$NON-NLS-2$
		return buffer.toString();		
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < 64; i++) {
			if (i % 8 == 0) {
				if (i > 0) {
					buffer
						.append("|") //$NON-NLS-1$
						.append(Util.LINE_SEPARATOR);
				}
				buffer
					.append(" +-+-+-+-+-+-+-+-+") //$NON-NLS-1$
					.append(Util.LINE_SEPARATOR)
					.append(8 - (i / 8));
			}
			buffer.append("|"); //$NON-NLS-1$
			char pieceType = ' ';
			switch(this.board[i]) {
				case WHITE_BISHOP :
					pieceType = 'F';
					break;
				case WHITE_KING :
					pieceType = 'R';
					break;
				case WHITE_KNIGHT :
					pieceType = 'C';
					break;
				case WHITE_PAWN :
					pieceType = 'P';
					break;
				case WHITE_QUEEN :
					pieceType = 'D';
					break;
				case WHITE_ROOK :
					pieceType = 'T';
					break;
				case BLACK_BISHOP :
					pieceType = 'f';
					break;
				case BLACK_KING :
					pieceType = 'r';
					break;
				case BLACK_KNIGHT :
					pieceType = 'c';
					break;
				case BLACK_PAWN :
					pieceType = 'p';
					break;
				case BLACK_QUEEN :
					pieceType = 'd';
					break;
				case BLACK_ROOK :
					pieceType = 't';
					break;
				case EMPTY :
					pieceType = ' ';
					break;
				default :
					pieceType = 'X';
			}
			buffer.append(pieceType);
		}
		buffer
			.append("|") //$NON-NLS-1$
			.append(Util.LINE_SEPARATOR)
			.append(" +-+-+-+-+-+-+-+-+") //$NON-NLS-1$
			.append(Util.LINE_SEPARATOR)
			.append("  a b c d e f g h ") //$NON-NLS-1$
			.append(Util.LINE_SEPARATOR)
			.append(Util.LINE_SEPARATOR)
			.append("move number = " + this.moveNumber) //$NON-NLS-1$
			.append(" - fifty rule = " + this.fiftyMovesRuleNumber); //$NON-NLS-1$
		if (this.enPassantSquare != 0) {
			buffer.append(" - en passant = " + Converter.intToSquare(this.enPassantSquare)); //$NON-NLS-1$
		}
		buffer.append(" - TURN = "); //$NON-NLS-1$
		if (this.turn == WHITE_TURN) {
			buffer.append("WHITE TURN "); //$NON-NLS-1$
		} else {
			buffer.append("BLACK TURN "); //$NON-NLS-1$
		}
		boolean possibleCastling = false;
		if (this.whiteCanCastleKingSide) {
			buffer.append("K"); //$NON-NLS-1$
			possibleCastling = true;
		}
		if (this.whiteCanCastleQueenSide) {
			buffer.append("Q"); //$NON-NLS-1$
			possibleCastling = true;
		}
		if (this.blackCanCastleKingSide) {
			buffer.append("k"); //$NON-NLS-1$
			possibleCastling = true;
		}
		if (this.blackCanCastleQueenSide) {
			buffer.append("q"); //$NON-NLS-1$
			possibleCastling = true;
		}
		if (!possibleCastling) {
			buffer.append("- No castling"); //$NON-NLS-1$
		}
		buffer.append(Util.LINE_SEPARATOR);
		return buffer.toString();
	}

	public void undoMoveWithoutNotification() {
		if (this.moveNumber >= 0) {
			undoMoveWithoutNotification(getLastMove());
		}
	}
	
	public void undoMove() {
		undoMoveWithoutNotification();
		update();
	}

	public void undoMove(long move) {
		undoMoveWithoutNotification(move);
		update();
	}

	public void undoMoveWithoutNotification(long move) {
		if (this.turn == WHITE_TURN) {
			this.turn = BLACK_TURN;
		} else {
			this.turn = WHITE_TURN;
		}
		final long info = move;
		int startingSquare = (int) (info & STARTING_SQUARE_MASK);
		int endingSquare = (int) ((info & ENDING_SQUARE_MASK) >> ENDING_SQUARE_SHIFT);
		int pieceType = this.board[endingSquare];
		int capturedPiece = (int) ((info & CAPTURE_PIECE_MASK) >> CAPTURE_PIECE_SHIFT);
		
		// common behavior
		this.board[startingSquare] = pieceType;
		this.board[endingSquare] = capturedPiece;
		
		// handle promotion
		int promotedPiece = (int) ((info & PROMOTION_PIECE_MASK) >> PROMOTION_PIECE_SHIFT);
		if (promotedPiece != 0) {
			if (promotedPiece < 7) {
				this.board[startingSquare] = WHITE_PAWN;
			} else {
				this.board[startingSquare] = BLACK_PAWN;
			}
		}
		
		this.whiteCanCastleKingSide = (((info & CASTLING_MASK) >> CASTLING_SHIFT) & 0x1) == 1 ? true : false;
		this.whiteCanCastleQueenSide = (((info & CASTLING_MASK) >> CASTLING_SHIFT) & 0x2) == 2 ? true : false;
		this.blackCanCastleKingSide = (((info & CASTLING_MASK) >> CASTLING_SHIFT) & 0x4) == 4 ? true : false;
		this.blackCanCastleQueenSide = (((info & CASTLING_MASK) >> CASTLING_SHIFT) & 0x8)  == 8 ? true : false;
		
		int fakeEnPassantSquare = (int) ((info & EN_PASSANT_SQUARE_MASK) >> EN_PASSANT_SQUARE_SHIFT);
		switch(fakeEnPassantSquare) {
			case 1 :
			case 2 :
			case 3 :
			case 4 :
			case 5 :
			case 6 :
			case 7 :
			case 8 :
				this.enPassantSquare = fakeEnPassantSquare + 15;
				break;
			case 9 :
			case 10 :
			case 11 :
			case 12 :
			case 13 :
			case 14 :
			case 15 :
			case 16 :
				this.enPassantSquare = fakeEnPassantSquare + 31;
				break;
			default:
				this.enPassantSquare = -1;
		}
		
		// detect castling and update the this.board
		switch(pieceType) {
			case WHITE_KING:
				if (((startingSquare - endingSquare) == 2) || ((startingSquare - endingSquare) == -2)) {
					if (startingSquare == 60) {
						switch(endingSquare) {
							case 62 :
								this.board[63] = WHITE_ROOK;
								this.board[61] = EMPTY;
								break;
							case 58 :
								this.board[56] = WHITE_ROOK;
								this.board[59] = EMPTY;
						}
					}
				}
				this.whiteKing = startingSquare;
				break;
			case BLACK_KING :
				if (((startingSquare - endingSquare) == 2) || ((startingSquare - endingSquare) == -2)) {
					if (startingSquare == 4) {
						switch(endingSquare) {
							case 6 :
								this.board[7] = BLACK_ROOK;
								this.board[5] = EMPTY;
								break;
							case 2 :
								this.board[0] = BLACK_ROOK;
								this.board[3] = EMPTY;
						}
					}
				}
				this.blackKing = startingSquare;
				break;
			case WHITE_PAWN :
				if (endingSquare == this.enPassantSquare) {
					switch(endingSquare - startingSquare) {
						case -7 :
							this.board[startingSquare + 1] = capturedPiece;
							break;
						case -9 :
							this.board[startingSquare - 1] = capturedPiece;
							break;
					}
					this.board[endingSquare] = EMPTY;
				}
				break;
			case BLACK_PAWN :
				if (endingSquare == this.enPassantSquare) {
					switch(startingSquare - endingSquare) {
						case -9 :
							this.board[startingSquare + 1] = capturedPiece;
							break;
						case -7 :
							this.board[startingSquare - 1] = capturedPiece;
							break;
					}
					this.board[endingSquare] = EMPTY;
				}
				break;
		}
		this.moveNumber--;
		this.startingMoveNumber--;
	}
}
