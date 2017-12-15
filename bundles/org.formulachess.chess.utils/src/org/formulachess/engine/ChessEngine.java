package org.formulachess.engine;

import static org.formulachess.engine.Piece.BLACK_BISHOP;
import static org.formulachess.engine.Piece.BLACK_KING;
import static org.formulachess.engine.Piece.BLACK_KNIGHT;
import static org.formulachess.engine.Piece.BLACK_PAWN;
import static org.formulachess.engine.Piece.BLACK_QUEEN;
import static org.formulachess.engine.Piece.BLACK_ROOK;
import static org.formulachess.engine.Piece.EMPTY;
import static org.formulachess.engine.Piece.UNDEFINED;
import static org.formulachess.engine.Piece.WHITE_BISHOP;
import static org.formulachess.engine.Piece.WHITE_KING;
import static org.formulachess.engine.Piece.WHITE_KNIGHT;
import static org.formulachess.engine.Piece.WHITE_PAWN;
import static org.formulachess.engine.Piece.WHITE_QUEEN;
import static org.formulachess.engine.Piece.WHITE_ROOK;
import static org.formulachess.engine.Turn.BLACK_TURN;
import static org.formulachess.engine.Turn.WHITE_TURN;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.formulachess.util.Util;

public class ChessEngine extends AbstractChessEngine {

	private static final int[] TAB_120 = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, 0, 1, 2, 3, 4, 5, 6, 7, -1, -1, 8, 9, 10, 11, 12, 13, 14, 15, -1, -1, 16, 17, 18, 19, 20, 21, 22,
			23, -1, -1, 24, 25, 26, 27, 28, 29, 30, 31, -1, -1, 32, 33, 34, 35, 36, 37, 38, 39, -1, -1, 40, 41, 42, 43,
			44, 45, 46, 47, -1, -1, 48, 49, 50, 51, 52, 53, 54, 55, -1, -1, 56, 57, 58, 59, 60, 61, 62, 63, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };

	private static final int[] TAB_64 = { 21, 22, 23, 24, 25, 26, 27, 28, 31, 32, 33, 34, 35, 36, 37, 38, 41, 42, 43,
			44, 45, 46, 47, 48, 51, 52, 53, 54, 55, 56, 57, 58, 61, 62, 63, 64, 65, 66, 67, 68, 71, 72, 73, 74, 75, 76,
			77, 78, 81, 82, 83, 84, 85, 86, 87, 88, 91, 92, 93, 94, 95, 96, 97, 98 };

	private static final int[] KNIGHT_POSITIONS = { 21, 19, 12, 8, -8, -12, -19, -21 };
	private static final int[] KING_POSITIONS = { -11, -10, -9, -1, 1, 9, 10, 11 };
	private static final long[] NO_MOVES = new long[0];

	static final boolean DEBUG = true;
	private static final long[] EMPTY_MOVES = new long[0];

	private static final int HISTORY_DEFAULT_SIZE = 10;
	private static final Logger logger = Logger.getLogger(ChessEngine.class.getName());

	private boolean blackCanCastleKingSide;
	private boolean blackCanCastleQueenSide;

	private int blackKingSquare;
	private Piece[] board;
	private Messages currentMessages;
	private int enPassantSquare;
	private int fiftyMovesRuleNumber;
	private long[] history;
	private boolean isReady;

	private Locale locale;
	private String[] moveHistory;
	private int moveNumber;
	private int movesCounter;

	private long[] nextMoves;

	private Turn startingColor;

	private int startingMoveNumber;
	private Turn turn;
	private boolean whiteCanCastleKingSide;
	private boolean whiteCanCastleQueenSide;

	private int whiteKingSquare;

	public static long perft(String fenNotation, int depth) {
		ChessEngine model = new ChessEngine(Locale.getDefault(), fenNotation);
		return model.perft(depth);
	}

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

	void addMove(int startingPosition, int endingPosition, Piece capturePieceType) {
		addMove(startingPosition, endingPosition, capturePieceType, UNDEFINED);
	}

	void addMove(int startingPosition, int endingPosition, Piece capturePieceType, Piece promotedPiece) {
		long info = MoveConstants.getMoveValue(
				startingPosition,
				endingPosition,
				capturePieceType,
				promotedPiece,
				this.enPassantSquare,
				this.whiteCanCastleKingSide,
				this.whiteCanCastleQueenSide,
				this.blackCanCastleKingSide,
				this.blackCanCastleQueenSide);

		try {
			movePiece(info);
			if (this.turn == WHITE_TURN) {
				if (startingPosition == this.whiteKingSquare) {
					if (isWhiteInCheck(endingPosition)) {
						return;
					}
				} else if (isWhiteInCheck(this.whiteKingSquare)) {
					return;
				}
				if (isBlackInCheck(this.blackKingSquare)) {
					info = MoveConstants.tagAsCheck(info);
				}
			}
			if (this.getTurn() == BLACK_TURN) {
				if (startingPosition == this.blackKingSquare) {
					if (isBlackInCheck(endingPosition)) {
						return;
					}
				} else if (isBlackInCheck(this.blackKingSquare)) {
					return;
				}
				if (isWhiteInCheck(this.whiteKingSquare)) {
					info = MoveConstants.tagAsCheck(info);
				}
			}
		} finally {
			movePieceBack(info);
		}
		if (this.nextMoves == null) {
			this.nextMoves = new long[3];
		}
		if (this.movesCounter == this.nextMoves.length) {
			System.arraycopy(this.nextMoves, 0, this.nextMoves = new long[this.movesCounter * 2], 0, this.movesCounter);
		}
		this.nextMoves[this.movesCounter++] = info;
	}

	private final long[] allBlackMoves() {
		this.movesCounter = 0;
		for (int i = 0; i < 64; i++) {
			Piece currentPiece = this.board[i];
			if (currentPiece.isBlack()) {
				switch (currentPiece) {
					case BLACK_BISHOP:
						moveBishop(i, false);
						break;
					case BLACK_KNIGHT:
						moveKnight(i, false);
						break;
					case BLACK_ROOK:
						moveRook(i, false);
						break;
					case BLACK_QUEEN:
						moveQueen(i, false);
						break;
					case BLACK_KING:
						moveBlackKing(i);
						break;
					case BLACK_PAWN:
						moveBlackPawn(i);
						break;
					default:
				}
			}
		}
		return getNextMoves();
	}

	private final long[] allBlackMoves(Piece searchPieceType) {
		if (searchPieceType == null || searchPieceType.isWhiteOrEmpty()) {
			return EMPTY_MOVES;
		}
		this.movesCounter = 0;
		for (int i = 0; i < 64; i++) {
			Piece currentPiece = this.board[i];
			if (currentPiece == searchPieceType) {
				switch (currentPiece) {
					case BLACK_BISHOP:
						moveBishop(i, false);
						break;
					case BLACK_KNIGHT:
						moveKnight(i, false);
						break;
					case BLACK_ROOK:
						moveRook(i, false);
						break;
					case BLACK_QUEEN:
						moveQueen(i, false);
						break;
					case BLACK_KING:
						moveBlackKing(i);
						break;
					case BLACK_PAWN:
						moveBlackPawn(i);
						break;
					default:
				}
			}
		}
		return getNextMoves();
	}

	private final long[] allBlackMoves(Piece searchPieceType, int startingSquare) {
		this.movesCounter = 0;
		switch (searchPieceType) {
			case BLACK_BISHOP:
				moveBishop(startingSquare, false);
				break;
			case BLACK_KNIGHT:
				moveKnight(startingSquare, false);
				break;
			case BLACK_ROOK:
				moveRook(startingSquare, false);
				break;
			case BLACK_QUEEN:
				moveQueen(startingSquare, false);
				break;
			case BLACK_KING:
				moveBlackKing(startingSquare);
				break;
			case BLACK_PAWN:
				moveBlackPawn(startingSquare);
				break;
			default:
		}
		return getNextMoves();
	}

	@Override
	public long[] allMoves() {
		return this.turn == WHITE_TURN ? allWhiteMoves() : allBlackMoves();
	}

	@Override
	public long[] allMoves(Piece pieceType) {
		return this.turn == WHITE_TURN ? allWhiteMoves(pieceType) : allBlackMoves(pieceType);
	}

	@Override
	public long[] allMoves(Piece pieceType, int startingSquare) {
		return this.turn == WHITE_TURN ? allWhiteMoves(pieceType, startingSquare)
				: allBlackMoves(pieceType, startingSquare);
	}

	private final long[] allWhiteMoves() {
		this.movesCounter = 0;
		for (int i = 0; i < 64; i++) {
			Piece currentPiece = this.board[i];
			if (currentPiece != EMPTY) {
				switch (currentPiece) {
					case WHITE_BISHOP:
						moveBishop(i, true);
						break;
					case WHITE_KNIGHT:
						moveKnight(i, true);
						break;
					case WHITE_ROOK:
						moveRook(i, true);
						break;
					case WHITE_QUEEN:
						moveQueen(i, true);
						break;
					case WHITE_KING:
						moveWhiteKing(i);
						break;
					case WHITE_PAWN:
						moveWhitePawn(i);
						break;
					default:
				}
			}
		}
		return getNextMoves();
	}

	private final long[] allWhiteMoves(Piece searchPieceType) {
		this.movesCounter = 0;
		for (int i = 0; i < 64; i++) {
			Piece currentPiece = this.board[i];
			if (currentPiece == searchPieceType) {
				switch (currentPiece) {
					case WHITE_BISHOP:
						moveBishop(i, true);
						break;
					case WHITE_KNIGHT:
						moveKnight(i, true);
						break;
					case WHITE_ROOK:
						moveRook(i, true);
						break;
					case WHITE_QUEEN:
						moveQueen(i, true);
						break;
					case WHITE_KING:
						moveWhiteKing(i);
						break;
					case WHITE_PAWN:
						moveWhitePawn(i);
						break;
					default:
				}
			}
		}
		return getNextMoves();
	}

	private final long[] allWhiteMoves(Piece searchPieceType, int startingSquare) {
		this.movesCounter = 0;
		switch (searchPieceType) {
			case WHITE_BISHOP:
				moveBishop(startingSquare, true);
				break;
			case WHITE_KNIGHT:
				moveKnight(startingSquare, true);
				break;
			case WHITE_ROOK:
				moveRook(startingSquare, true);
				break;
			case WHITE_QUEEN:
				moveQueen(startingSquare, true);
				break;
			case WHITE_KING:
				moveWhiteKing(startingSquare);
				break;
			case WHITE_PAWN:
				moveWhitePawn(startingSquare);
				break;
			default:
		}
		return getNextMoves();
	}

	private void decodeFENNotation(String fenPosition) {
		this.moveNumber = -1;
		this.history = new long[HISTORY_DEFAULT_SIZE];
		this.moveHistory = new String[HISTORY_DEFAULT_SIZE];
		this.board = new Piece[64];
		try {
			String fen = fenPosition.trim();
			StringTokenizer fenDecoder = new StringTokenizer(fen);
			StringTokenizer tokenizer = new StringTokenizer(fenDecoder.nextToken(), "/"); //$NON-NLS-1$
			int row = 0;
			while (tokenizer.hasMoreTokens()) {
				String temp = tokenizer.nextToken();
				int col = 0;
				for (int index = 0; index < temp.length(); index++) {
					char token = temp.charAt(index);
					switch (token) {
						case 'r':
							this.board[row * 8 + col] = BLACK_ROOK;
							col++;
							break;
						case 'n':
							this.board[row * 8 + col] = BLACK_KNIGHT;
							col++;
							break;
						case 'b':
							this.board[row * 8 + col] = BLACK_BISHOP;
							col++;
							break;
						case 'q':
							this.board[row * 8 + col] = BLACK_QUEEN;
							col++;
							break;
						case 'k':
							this.board[row * 8 + col] = BLACK_KING;
							this.blackKingSquare = row * 8 + col;
							col++;
							break;
						case 'p':
							this.board[row * 8 + col] = BLACK_PAWN;
							col++;
							break;
						case 'R':
							this.board[row * 8 + col] = WHITE_ROOK;
							col++;
							break;
						case 'N':
							this.board[row * 8 + col] = WHITE_KNIGHT;
							col++;
							break;
						case 'B':
							this.board[row * 8 + col] = WHITE_BISHOP;
							col++;
							break;
						case 'Q':
							this.board[row * 8 + col] = WHITE_QUEEN;
							col++;
							break;
						case 'K':
							this.board[row * 8 + col] = WHITE_KING;
							this.whiteKingSquare = row * 8 + col;
							col++;
							break;
						case 'P':
							this.board[row * 8 + col] = WHITE_PAWN;
							col++;
							break;
						default:
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
			// is the black player.
			if ("w".equals(fenDecoder.nextToken())) { //$NON-NLS-1$
				this.turn = WHITE_TURN;
				this.startingColor = WHITE_TURN;
			} else {
				this.turn = BLACK_TURN;
				this.startingColor = BLACK_TURN;
			}
			// now we looking on each side each color can castle
			String castling = fenDecoder.nextToken();
			this.whiteCanCastleQueenSide = castling.indexOf('Q') != -1; // $NON-NLS-1$
			this.whiteCanCastleKingSide = castling.indexOf('K') != -1; // $NON-NLS-1$
			this.blackCanCastleQueenSide = castling.indexOf('q') != -1; // $NON-NLS-1$
			this.blackCanCastleKingSide = castling.indexOf('k') != -1; // $NON-NLS-1$

			String enPassantCoords = fenDecoder.nextToken();
			if ("-".equals(enPassantCoords)) { //$NON-NLS-1$
				this.enPassantSquare = -1;
			} else {
				this.enPassantSquare = Converter.squareToInt(enPassantCoords);
			}
			// now we retrieve the half move clock
			this.fiftyMovesRuleNumber = Integer.parseInt(fenDecoder.nextToken());
			this.startingMoveNumber = Integer.parseInt(fenDecoder.nextToken());
		} catch (NoSuchElementException e) {
			logger.log(Level.SEVERE, this.currentMessages.getString("fen.decoding.error"), e);//$NON-NLS-1$
		}
	}

	public Piece[] getBoard() {
		return board;
	}

	public Piece getBoard(int index) {
		return board[index];
	}

	public Locale getLocale() {
		return this.locale;
	}

	@Override
	public long getLastMove() {
		if (this.moveNumber < 0) {
			return -1;
		}
		return this.history[this.moveNumber];
	}

	public int getMoveNumber() {
		return moveNumber;
	}

	private long[] getNextMoves() {
		if (this.movesCounter == 0) {
			return NO_MOVES;
		}
		long[] moves = new long[this.movesCounter];
		System.arraycopy(this.nextMoves, 0, moves, 0, this.movesCounter);
		return moves;
	}

	public Turn getStartingColor() {
		return startingColor;
	}

	public Turn getTurn() {
		return this.turn;
	}

	@Override
	public void initialize(String fenNotation) {
		decodeFENNotation(fenNotation);
	}

	@Override
	public void initializeToStartingPosition() {
		decodeFENNotation("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"); //$NON-NLS-1$
	}

	public boolean isBlackCanCastleKingSide() {
		return blackCanCastleKingSide;
	}

	public boolean isBlackCanCastleQueenSide() {
		return blackCanCastleQueenSide;
	}

	@Override
	public boolean isBlackInCheck() {
		return isBlackInCheck(this.blackKingSquare);
	}

	private boolean isBlackInCheck(int square) {
		int currentPosition = TAB_64[square];
		// north_west
		boolean firstSquare = true;
		north_west_next: for (int j = currentPosition - 11; j > 0; j -= 11) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (nextSquare.isWhiteOrEmpty()) {
					switch (nextSquare) {
						case WHITE_QUEEN:
						case WHITE_BISHOP:
							return true;
						case WHITE_KING:
							if (firstSquare) {
								return true;
							}
							break;
						case EMPTY:
							firstSquare = false;
							continue north_west_next;
						default:
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
				final Piece nextSquare = this.board[nextPosition];
				if (nextSquare.isWhiteOrEmpty()) {
					switch (nextSquare) {
						case WHITE_QUEEN:
						case WHITE_BISHOP:
							return true;
						case WHITE_KING:
							if (firstSquare) {
								return true;
							}
							break;
						case WHITE_PAWN:
							if (firstSquare) {
								return true;
							}
							break;
						case EMPTY:
							firstSquare = false;
							continue south_west_next;
						default:
					}
				}
			}
			break south_west_next;
		}
		// north_east
		firstSquare = true;
		north_east_next: for (int j = currentPosition - 9; j > 0; j -= 9) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (nextSquare.isWhiteOrEmpty()) {
					switch (nextSquare) {
						case WHITE_QUEEN:
						case WHITE_BISHOP:
							return true;
						case WHITE_KING:
							if (firstSquare) {
								return true;
							}
							break;
						case EMPTY:
							firstSquare = false;
							continue north_east_next;
						default:
					}
				}
			}
			break north_east_next;
		}
		// south_east
		firstSquare = true;
		south_east_next: for (int j = currentPosition + 11; j < 120; j += 11) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (nextSquare.isWhiteOrEmpty()) {
					switch (nextSquare) {
						case WHITE_QUEEN:
						case WHITE_BISHOP:
							return true;
						case WHITE_KING:
							if (firstSquare) {
								return true;
							}
							break;
						case WHITE_PAWN:
							if (firstSquare) {
								return true;
							}
							break;
						case EMPTY:
							firstSquare = false;
							continue south_east_next;
						default:
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
				final Piece nextSquare = this.board[nextPosition];
				if (nextSquare.isWhiteOrEmpty()) {
					switch (nextSquare) {
						case WHITE_QUEEN:
						case WHITE_ROOK:
							return true;
						case WHITE_KING:
							if (firstSquare) {
								return true;
							}
							break;
						case EMPTY:
							firstSquare = false;
							continue north_next;
						default:
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
				final Piece nextSquare = this.board[nextPosition];
				if (nextSquare.isWhiteOrEmpty()) {
					switch (nextSquare) {
						case WHITE_QUEEN:
						case WHITE_ROOK:
							return true;
						case WHITE_KING:
							if (firstSquare) {
								return true;
							}
							break;
						case EMPTY:
							firstSquare = false;
							continue south_next;
						default:
					}
				}
			}
			break south_next;
		}
		// west
		firstSquare = true;
		west_next: for (int j = currentPosition - 1; j > 0; j--) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (nextSquare.isWhiteOrEmpty()) {
					switch (nextSquare) {
						case WHITE_QUEEN:
						case WHITE_ROOK:
							return true;
						case WHITE_KING:
							if (firstSquare) {
								return true;
							}
							break;
						case EMPTY:
							firstSquare = false;
							continue west_next;
						default:
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
				final Piece nextSquare = this.board[nextPosition];
				if (nextSquare.isWhiteOrEmpty()) {
					switch (nextSquare) {
						case WHITE_QUEEN:
						case WHITE_ROOK:
							return true;
						case WHITE_KING:
							if (firstSquare) {
								return true;
							}
							break;
						case EMPTY:
							firstSquare = false;
							continue east_next;
						default:
					}
				}
			}
			break east_next;
		}
		knight_check: for (int j = 0; j < 8; j++) {
			int nextPosition = TAB_120[currentPosition + KNIGHT_POSITIONS[j]];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (nextSquare.isWhiteOrEmpty()) {
					switch (nextSquare) {
						case WHITE_KNIGHT:
							return true;
						case EMPTY:
							continue knight_check;
						default:
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean isMate() {
		return allMoves().length == 0
				&& ((this.turn == WHITE_TURN && isWhiteInCheck()) || (this.turn == BLACK_TURN && isBlackInCheck()));
	}

	public boolean isReady() {
		return this.isReady;
	}

	public boolean isWhiteCanCastleKingSide() {
		return whiteCanCastleKingSide;
	}

	public boolean isWhiteCanCastleQueenSide() {
		return whiteCanCastleQueenSide;
	}

	@Override
	public boolean isWhiteInCheck() {
		return isWhiteInCheck(this.whiteKingSquare);
	}

	private boolean isWhiteInCheck(int square) {
		int currentPosition = TAB_64[square];
		// north_west
		boolean firstSquare = true;
		north_west_next: for (int j = currentPosition - 11; j > 0; j -= 11) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (nextSquare.isBlackOrEmpty()) {
					switch (nextSquare) {
						case BLACK_QUEEN:
						case BLACK_BISHOP:
							return true;
						case BLACK_KING:
							if (firstSquare) {
								return true;
							}
							break;
						case BLACK_PAWN:
							if (firstSquare) {
								return true;
							}
							break;
						case EMPTY:
							firstSquare = false;
							continue north_west_next;
						default:
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
				final Piece nextSquare = this.board[nextPosition];
				if (nextSquare.isBlackOrEmpty()) {
					switch (nextSquare) {
						case BLACK_QUEEN:
						case BLACK_BISHOP:
							return true;
						case BLACK_KING:
							if (firstSquare) {
								return true;
							}
							break;
						case EMPTY:
							firstSquare = false;
							continue south_west_next;
						default:
					}
				}
			}
			break south_west_next;
		}
		// north_east
		firstSquare = true;
		north_east_next: for (int j = currentPosition - 9; j > 0; j -= 9) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (nextSquare.isBlackOrEmpty()) {
					switch (nextSquare) {
						case BLACK_QUEEN:
						case BLACK_BISHOP:
							return true;
						case BLACK_KING:
							if (firstSquare) {
								return true;
							}
							break;
						case BLACK_PAWN:
							if (firstSquare) {
								return true;
							}
							break;
						case EMPTY:
							firstSquare = false;
							continue north_east_next;
						default:
					}
				}
			}
			break north_east_next;
		}
		// south_east
		firstSquare = true;
		south_east_next: for (int j = currentPosition + 11; j < 120; j += 11) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (nextSquare.isBlackOrEmpty()) {
					switch (nextSquare) {
						case BLACK_QUEEN:
						case BLACK_BISHOP:
							return true;
						case BLACK_KING:
							if (firstSquare) {
								return true;
							}
							break;
						case EMPTY:
							firstSquare = false;
							continue south_east_next;
						default:
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
				final Piece nextSquare = this.board[nextPosition];
				if (nextSquare.isBlackOrEmpty()) {
					switch (nextSquare) {
						case BLACK_QUEEN:
						case BLACK_ROOK:
							return true;
						case BLACK_KING:
							if (firstSquare) {
								return true;
							}
							break;
						case EMPTY:
							firstSquare = false;
							continue north_next;
						default:
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
				final Piece nextSquare = this.board[nextPosition];
				if (nextSquare.isBlackOrEmpty()) {
					switch (nextSquare) {
						case BLACK_QUEEN:
						case BLACK_ROOK:
							return true;
						case BLACK_KING:
							if (firstSquare) {
								return true;
							}
							break;
						case EMPTY:
							firstSquare = false;
							continue south_next;
						default:
					}
				}
			}
			break south_next;
		}
		// west
		firstSquare = true;
		west_next: for (int j = currentPosition - 1; j > 0; j--) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (nextSquare.isBlackOrEmpty()) {
					switch (nextSquare) {
						case BLACK_QUEEN:
						case BLACK_ROOK:
							return true;
						case BLACK_KING:
							if (firstSquare) {
								return true;
							}
							break;
						case EMPTY:
							firstSquare = false;
							continue west_next;
						default:
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
				final Piece nextSquare = this.board[nextPosition];
				if (nextSquare.isBlackOrEmpty()) {
					switch (nextSquare) {
						case BLACK_QUEEN:
						case BLACK_ROOK:
							return true;
						case BLACK_KING:
							if (firstSquare) {
								return true;
							}
							break;
						case EMPTY:
							firstSquare = false;
							continue east_next;
						default:
					}
				}
			}
			break east_next;
		}
		knight_check: for (int j = 0; j < 8; j++) {
			int nextPosition = TAB_120[currentPosition + KNIGHT_POSITIONS[j]];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (nextSquare.isBlackOrEmpty()) {
					switch (nextSquare) {
						case BLACK_KNIGHT:
							return true;
						case EMPTY:
							continue knight_check;
						default:
					}
				}
			}
		}
		return false;
	}

	private final void moveBishop(int i, boolean isWhite) {
		int currentPosition = TAB_64[i];
		// north_west
		north_west_next: for (int j = currentPosition - 11; j > 0; j -= 11) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare.getValue() >= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue north_west_next;
						}
					}
				} else {
					if (nextSquare.getValue() <= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue north_west_next;
						}
					}
				}
			}
			break north_west_next;
		}
		// south_west
		south_west_next: for (int j = currentPosition + 9; j < 120; j += 9) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare.getValue() >= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue south_west_next;
						}
					}
				} else {
					if (nextSquare.getValue() <= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue south_west_next;
						}
					}
				}
			}
			break south_west_next;
		}
		// north_east
		north_east_next: for (int j = currentPosition - 9; j > 0; j -= 9) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare.getValue() >= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue north_east_next;
						}
					}
				} else {
					if (nextSquare.getValue() <= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue north_east_next;
						}
					}
				}
			}
			break north_east_next;
		}
		// south_east
		south_east_next: for (int j = currentPosition + 11; j < 120; j += 11) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare.getValue() >= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue south_east_next;
						}
					}
				} else {
					if (nextSquare.getValue() <= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue south_east_next;
						}
					}
				}
			}
			break south_east_next;
		}
	}

	private final void moveBlackKing(int i) {

		// all squares around the king + castling
		int currentPosition = TAB_64[i];
		for (int j = 0; j < 8; j++) {
			int nextPosition = TAB_120[currentPosition + KING_POSITIONS[j]];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (nextSquare.getValue() <= EMPTY.getValue()) {
					addMove(i, nextPosition, nextSquare);
				}
			}
		}
		if (this.blackCanCastleKingSide && !isBlackInCheck() && this.board[i + 2] == EMPTY && this.board[i + 1] == EMPTY
				&& !isBlackInCheck(i + 2) && !isBlackInCheck(i + 1) && this.board[i + 3] == BLACK_ROOK) {
			addMove(i, i + 2, EMPTY);
		}
		if (this.blackCanCastleQueenSide && !isBlackInCheck() && this.board[i - 3] == EMPTY
				&& this.board[i - 2] == EMPTY && this.board[i - 1] == EMPTY && !isBlackInCheck(i - 2)
				&& !isBlackInCheck(i - 1) && this.board[i - 4] == BLACK_ROOK) {
			addMove(i, i - 2, EMPTY);
		}
	}

	private final void moveBlackPawn(int i) {
		int currentPosition = TAB_64[i];
		// one square or two squares if it has not moved yet.
		if (i >= 8 && i <= 15) {
			int nextPosition = TAB_120[currentPosition + 10];
			if (this.board[nextPosition] == EMPTY) {
				addMove(i, nextPosition, EMPTY);
				nextPosition = TAB_120[currentPosition + 20];
				if (this.board[nextPosition] == EMPTY) {
					addMove(i, nextPosition, EMPTY);
				}
			}
		} else {
			int nextPosition = TAB_120[currentPosition + 10];
			if (nextPosition > 0 && this.board[nextPosition] == EMPTY) {
				if (nextPosition >= 56) {
					// this is a promotion
					for (Piece promotionPiece : Piece.getBlackPromotionPieces()) {
						addMove(i, nextPosition, EMPTY, promotionPiece);
					}
				} else {
					addMove(i, nextPosition, EMPTY);
				}
			}
		}
		// capture on the side
		int nextPosition = TAB_120[currentPosition + 11];
		if (nextPosition > 0) {
			Piece nextSquare = this.board[nextPosition];
			if (nextSquare.getValue() < EMPTY.getValue()) {
				if (nextPosition >= 56) {
					// this is a promotion
					for (Piece promotionPiece : Piece.getBlackPromotionPieces()) {
						addMove(i, nextPosition, nextSquare, promotionPiece);
					}
				} else {
					addMove(i, nextPosition, nextSquare);
				}
			} else if (i >= 32 && i <= 39 && nextPosition == this.enPassantSquare) {
				// en passant
				addMove(i, nextPosition, this.board[TAB_120[currentPosition + 1]]);
			}
		}
		nextPosition = TAB_120[currentPosition + 9];
		if (nextPosition > 0) {
			Piece nextSquare = this.board[nextPosition];
			if (nextSquare.getValue() < EMPTY.getValue()) {
				if (nextPosition >= 56) {
					// this is a promotion
					for (Piece promotionPiece : Piece.getBlackPromotionPieces()) {
						addMove(i, nextPosition, nextSquare, promotionPiece);
					}
				} else {
					addMove(i, nextPosition, nextSquare);
				}
			} else if (i >= 32 && i <= 39 && nextPosition == this.enPassantSquare) {
				// en passant
				addMove(i, nextPosition, this.board[TAB_120[currentPosition - 1]]);
			}
		}
	}

	private final void moveKnight(int i, boolean isWhite) {
		int currentPosition = TAB_64[i];
		for (int j = 0; j < 8; j++) {
			int nextPosition = TAB_120[currentPosition + KNIGHT_POSITIONS[j]];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare.getValue() >= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
					}
				} else {
					if (nextSquare.getValue() <= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
					}
				}
			}
		}
	}

	private final void movePiece(long move) {
		int startingSquare = MoveConstants.getStartingSquare(move);
		int endingSquare = MoveConstants.getEndingSquare(move);
		Piece pieceType = this.board[startingSquare];

		// common behavior
		this.board[startingSquare] = EMPTY;
		this.board[endingSquare] = pieceType;
		switch (pieceType) {
			case WHITE_KING:
				if ((((startingSquare - endingSquare) == 2) || ((startingSquare - endingSquare) == -2))
						&& startingSquare == 60) {
					switch (endingSquare) {
						case 62:
							this.board[61] = WHITE_ROOK;
							this.board[63] = EMPTY;
							break;
						case 58:
							this.board[59] = WHITE_ROOK;
							this.board[56] = EMPTY;
							break;
						default:
					}
				}
				this.whiteKingSquare = endingSquare;
				break;
			case BLACK_KING:
				if ((((startingSquare - endingSquare) == 2) || ((startingSquare - endingSquare) == -2))
						&& startingSquare == 4) {
					switch (endingSquare) {
						case 6:
							this.board[5] = BLACK_ROOK;
							this.board[7] = EMPTY;
							break;
						case 2:
							this.board[3] = BLACK_ROOK;
							this.board[0] = EMPTY;
							break;
						default:
					}
				}
				this.blackKingSquare = endingSquare;
				break;
			case WHITE_PAWN:
				if (MoveConstants.isPromotion(move)) {
					// promotion
					this.board[endingSquare] = Piece.getPiece(MoveConstants.getPromotionValue(move));
				}
				if (endingSquare == this.enPassantSquare) {
					switch (endingSquare - startingSquare) {
						case -7:
							this.board[startingSquare + 1] = EMPTY;
							break;
						case -9:
							this.board[startingSquare - 1] = EMPTY;
							break;
						default:
					}
				}
				break;
			case BLACK_PAWN:
				if (MoveConstants.isPromotion(move)) {
					// promotion
					this.board[endingSquare] = Piece.getPiece(MoveConstants.getPromotionValue(move));
				}
				if (endingSquare == this.enPassantSquare) {
					switch (endingSquare - startingSquare) {
						case 9:
							this.board[startingSquare + 1] = EMPTY;
							break;
						case 7:
							this.board[startingSquare - 1] = EMPTY;
							break;
						default:
					}
				}
				break;
			default:
		}
	}

	private final void movePieceBack(long move) {
		int startingSquare = MoveConstants.getStartingSquare(move);
		int endingSquare = MoveConstants.getEndingSquare(move);
		Piece pieceType = this.board[endingSquare];
		Piece capturedPiece = Piece.getPiece(MoveConstants.getCaptureValue(move));

		// common behavior
		this.board[startingSquare] = pieceType;
		this.board[endingSquare] = capturedPiece;

		// handle promotion
		Piece promotedPiece = Piece.getPiece(MoveConstants.getPromotionValue(move));
		if (promotedPiece != UNDEFINED) {
			if (promotedPiece.getValue() < EMPTY.getValue()) {
				this.board[startingSquare] = WHITE_PAWN;
			} else {
				this.board[startingSquare] = BLACK_PAWN;
			}
		}

		// detect castling and update the this.board
		switch (pieceType) {
			case WHITE_KING:
				if ((((startingSquare - endingSquare) == 2) || ((startingSquare - endingSquare) == -2))
						&& startingSquare == 60) {
					switch (endingSquare) {
						case 62:
							this.board[63] = WHITE_ROOK;
							this.board[61] = EMPTY;
							break;
						case 58:
							this.board[56] = WHITE_ROOK;
							this.board[59] = EMPTY;
							break;
						default:
					}
				}
				this.whiteKingSquare = startingSquare;
				break;
			case BLACK_KING:
				if ((((startingSquare - endingSquare) == 2) || ((startingSquare - endingSquare) == -2))
						&& startingSquare == 4) {
					switch (endingSquare) {
						case 6:
							this.board[7] = BLACK_ROOK;
							this.board[5] = EMPTY;
							break;
						case 2:
							this.board[0] = BLACK_ROOK;
							this.board[3] = EMPTY;
							break;
						default:
					}
				}
				this.blackKingSquare = startingSquare;
				break;
			case WHITE_PAWN:
				if (endingSquare == this.enPassantSquare) {
					switch (endingSquare - startingSquare) {
						case -7:
							this.board[startingSquare + 1] = capturedPiece;
							break;
						case -9:
							this.board[startingSquare - 1] = capturedPiece;
							break;
						default:
					}
					this.board[endingSquare] = EMPTY;
				}
				break;
			case BLACK_PAWN:
				if (endingSquare == this.enPassantSquare) {
					switch (endingSquare - startingSquare) {
						case 9:
							this.board[startingSquare + 1] = capturedPiece;
							break;
						case 7:
							this.board[startingSquare - 1] = capturedPiece;
							break;
						default:
					}
					this.board[endingSquare] = EMPTY;
				}
				break;
			default:
		}
	}

	private final void moveQueen(int i, boolean isWhite) {
		int currentPosition = TAB_64[i];
		// north_west
		north_west_next: for (int j = currentPosition - 11; j > 0; j -= 11) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare.getValue() >= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue north_west_next;
						}
					}
				} else {
					if (nextSquare.getValue() <= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue north_west_next;
						}
					}
				}
			}
			break north_west_next;
		}
		// south_west
		south_west_next: for (int j = currentPosition + 9; j < 120; j += 9) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare.getValue() >= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue south_west_next;
						}
					}
				} else {
					if (nextSquare.getValue() <= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue south_west_next;
						}
					}
				}
			}
			break south_west_next;
		}
		// north_east
		north_east_next: for (int j = currentPosition - 9; j > 0; j -= 9) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare.getValue() >= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue north_east_next;
						}
					}
				} else {
					if (nextSquare.getValue() <= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue north_east_next;
						}
					}
				}
			}
			break north_east_next;
		}
		// south_east
		south_east_next: for (int j = currentPosition + 11; j < 120; j += 11) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare.getValue() >= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue south_east_next;
						}
					}
				} else {
					if (nextSquare.getValue() <= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue south_east_next;
						}
					}
				}
			}
			break south_east_next;
		}

		// north
		north_next: for (int j = currentPosition - 10; j > 0; j -= 10) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare.getValue() >= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue north_next;
						}
					}
				} else {
					if (nextSquare.getValue() <= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue north_next;
						}
					}
				}
			}
			break north_next;
		}
		// south
		south_next: for (int j = currentPosition + 10; j < 120; j += 10) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare.getValue() >= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue south_next;
						}
					}
				} else {
					if (nextSquare.getValue() <= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue south_next;
						}
					}
				}
			}
			break south_next;
		}
		// west
		west_next: for (int j = currentPosition - 1; j > 0; j--) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare.getValue() >= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue west_next;
						}
					}
				} else {
					if (nextSquare.getValue() <= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue west_next;
						}
					}
				}
			}
			break west_next;
		}
		// east
		east_next: for (int j = currentPosition + 1; j < 120; j++) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare.getValue() >= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue east_next;
						}
					}
				} else {
					if (nextSquare.getValue() <= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue east_next;
						}
					}
				}
			}
			break east_next;
		}
	}

	private final void moveRook(int i, boolean isWhite) {

		int currentPosition = TAB_64[i];
		// north
		north_next: for (int j = currentPosition - 10; j > 0; j -= 10) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare.getValue() >= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue north_next;
						}
					}
				} else {
					if (nextSquare.getValue() <= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue north_next;
						}
					}
				}
			}
			break north_next;
		}
		// south
		south_next: for (int j = currentPosition + 10; j < 120; j += 10) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare.getValue() >= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue south_next;
						}
					}
				} else {
					if (nextSquare.getValue() <= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue south_next;
						}
					}
				}
			}
			break south_next;
		}
		// west
		west_next: for (int j = currentPosition - 1; j > 0; j--) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare.getValue() >= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue west_next;
						}
					}
				} else {
					if (nextSquare.getValue() <= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue west_next;
						}
					}
				}
			}
			break west_next;
		}
		// east
		east_next: for (int j = currentPosition + 1; j < 120; j++) {
			int nextPosition = TAB_120[j];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (isWhite) {
					if (nextSquare.getValue() >= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue east_next;
						}
					}
				} else {
					if (nextSquare.getValue() <= EMPTY.getValue()) {
						addMove(i, nextPosition, nextSquare);
						if (nextSquare == EMPTY) {
							continue east_next;
						}
					}
				}
			}
			break east_next;
		}

	}

	private final void moveWhiteKing(int i) {
		// all squares around the king + castling
		int currentPosition = TAB_64[i];
		for (int j = 0; j < 8; j++) {
			int nextPosition = TAB_120[currentPosition + KING_POSITIONS[j]];
			if (nextPosition >= 0) {
				final Piece nextSquare = this.board[nextPosition];
				if (nextSquare.getValue() >= EMPTY.getValue()) {
					addMove(i, nextPosition, nextSquare);
				}
			}
		}
		if (this.whiteCanCastleKingSide && !isWhiteInCheck() && this.board[i + 2] == EMPTY && this.board[i + 1] == EMPTY
				&& !isWhiteInCheck(i + 2) && !isWhiteInCheck(i + 1) && this.board[i + 3] == WHITE_ROOK) {
			addMove(i, i + 2, EMPTY);
		}
		if (this.whiteCanCastleQueenSide && !isWhiteInCheck() && this.board[i - 3] == EMPTY
				&& this.board[i - 2] == EMPTY && this.board[i - 1] == EMPTY && !isWhiteInCheck(i - 2)
				&& !isWhiteInCheck(i - 1) && this.board[i - 4] == WHITE_ROOK) {
			addMove(i, i - 2, EMPTY);
		}
	}

	private final void moveWhitePawn(int i) {

		int currentPosition = TAB_64[i];
		// one square or two squares if it has not moved yet.
		if (i >= 48 && i <= 55) {
			int nextPosition = TAB_120[currentPosition - 10];
			if (this.board[nextPosition] == EMPTY) {
				addMove(i, nextPosition, EMPTY);
				nextPosition = TAB_120[currentPosition - 20];
				if (this.board[nextPosition] == EMPTY) {
					addMove(i, nextPosition, EMPTY);
				}
			}
		} else {
			int nextPosition = TAB_120[currentPosition - 10];
			if (nextPosition >= 0 && this.board[nextPosition] == EMPTY) {
				if (nextPosition <= 7) {
					// this is a promotion
					for (Piece promotionPiece : Piece.getWhitePromotionPieces()) {
						addMove(i, nextPosition, EMPTY, promotionPiece);
					}
				} else {
					addMove(i, nextPosition, EMPTY);
				}
			}
		}
		// capture on the side
		int nextPosition = TAB_120[currentPosition - 11];
		if (nextPosition >= 0) {
			Piece nextSquare = this.board[nextPosition];
			if (nextSquare.getValue() > EMPTY.getValue()) {
				if (nextPosition <= 7) {
					// this is a promotion
					for (Piece promotionPiece : Piece.getWhitePromotionPieces()) {
						addMove(i, nextPosition, nextSquare, promotionPiece);
					}
				} else {
					addMove(i, nextPosition, nextSquare);
				}
			} else if (i >= 24 && i <= 31 && nextPosition == this.enPassantSquare) {
				// en passant
				addMove(i, nextPosition, this.board[TAB_120[currentPosition - 1]]);
			}
		}
		nextPosition = TAB_120[currentPosition - 9];
		if (nextPosition >= 0) {
			Piece nextSquare = this.board[nextPosition];
			if (nextSquare.getValue() > EMPTY.getValue()) {
				if (nextPosition <= 7) {
					// this is a promotion
					for (Piece promotionPiece : Piece.getWhitePromotionPieces()) {
						addMove(i, nextPosition, nextSquare, promotionPiece);
					}
				} else {
					addMove(i, nextPosition, nextSquare);
				}
			} else if (i >= 24 && i <= 31 && nextPosition == this.enPassantSquare) {
				// en passant
				addMove(i, nextPosition, this.board[TAB_120[currentPosition + 1]]);
			}
		}
	}

	@Override
	public void playMove(long move) {
		this.playMoveWithoutNotification(move);
		this.update();
	}

	@Override
	public void playMoveWithoutNotification(long move) {
		if (this.history.length == this.moveNumber + 1) {
			// resize
			System.arraycopy(this.history, 0, this.history = new long[this.history.length + HISTORY_DEFAULT_SIZE], 0,
					this.moveNumber + 1);
			System.arraycopy(this.moveHistory, 0,
					this.moveHistory = new String[this.history.length + HISTORY_DEFAULT_SIZE], 0, this.moveNumber + 1);
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
		int startingSquare = MoveConstants.getStartingSquare(info);
		int endingSquare = MoveConstants.getEndingSquare(info);
		Piece pieceType = this.board[startingSquare];

		// common behavior
		this.board[startingSquare] = EMPTY;
		this.board[endingSquare] = pieceType;

		// detect castling and update the this.board
		switch (pieceType) {
			case WHITE_KING:
				if ((((startingSquare - endingSquare) == 2) || ((startingSquare - endingSquare) == -2))
						&& startingSquare == 60) {
					switch (endingSquare) {
						case 62:
							this.board[61] = WHITE_ROOK;
							this.board[63] = EMPTY;
							break;
						case 58:
							this.board[59] = WHITE_ROOK;
							this.board[56] = EMPTY;
						default:
					}
				}
				this.whiteKingSquare = endingSquare;
				this.whiteCanCastleKingSide = false;
				this.whiteCanCastleQueenSide = false;
				this.enPassantSquare = -1;
				break;
			case BLACK_KING:
				if ((((startingSquare - endingSquare) == 2) || ((startingSquare - endingSquare) == -2))
						&& startingSquare == 4) {
					switch (endingSquare) {
						case 6:
							this.board[5] = BLACK_ROOK;
							this.board[7] = EMPTY;
							break;
						case 2:
							this.board[3] = BLACK_ROOK;
							this.board[0] = EMPTY;
							break;
						default:
					}
				}
				this.blackCanCastleKingSide = false;
				this.blackCanCastleQueenSide = false;
				this.enPassantSquare = -1;
				this.blackKingSquare = endingSquare;
				break;
			case WHITE_PAWN:
				if (MoveConstants.isPromotion(info)) {
					// promotion
					this.board[endingSquare] = Piece.getPiece(MoveConstants.getPromotionValue(info));
				}
				if (endingSquare == this.enPassantSquare) {
					switch (endingSquare - startingSquare) {
						case -7:
							this.board[startingSquare + 1] = EMPTY;
							break;
						case -9:
							this.board[startingSquare - 1] = EMPTY;
							break;
						default:
					}
				}
				if (startingSquare - endingSquare == 16) {
					this.enPassantSquare = startingSquare - 8;
				} else {
					this.enPassantSquare = -1;
				}
				break;
			case BLACK_PAWN:
				if (MoveConstants.isPromotion(info)) {
					// promotion
					this.board[endingSquare] = Piece.getPiece(MoveConstants.getPromotionValue(info));
				}
				if (endingSquare == this.enPassantSquare) {
					switch (endingSquare - startingSquare) {
						case 9:
							this.board[startingSquare + 1] = EMPTY;
							break;
						case 7:
							this.board[startingSquare - 1] = EMPTY;
							break;
						default:
					}
				}
				if (endingSquare - startingSquare == 16) {
					this.enPassantSquare = endingSquare - 8;
				} else {
					this.enPassantSquare = -1;
				}
				break;
			case WHITE_ROOK:
				if (startingSquare == 63) {
					this.whiteCanCastleKingSide = false;
				} else if (startingSquare == 56) {
					this.whiteCanCastleQueenSide = false;
				}
				this.enPassantSquare = -1;
				break;
			case BLACK_ROOK:
				if (startingSquare == 7) {
					this.blackCanCastleKingSide = false;
				} else if (startingSquare == 0) {
					this.blackCanCastleQueenSide = false;
				}
				this.enPassantSquare = -1;
				break;
			default:
				this.enPassantSquare = -1;
		}
	}

	public void setBlackCanCastleKingSide(boolean blackCanCastleKingSide) {
		this.blackCanCastleKingSide = blackCanCastleKingSide;
	}

	public void setBlackCanCastleQueenSide(boolean blackCanCastleQueenSide) {
		this.blackCanCastleQueenSide = blackCanCastleQueenSide;
	}

	public void setBoard(int i, Piece piece) {
		if (this.board == null) {
			this.board = getEmptyBoard();
		}
		this.board[i] = piece;
		switch (piece) {
			case WHITE_KING:
				this.whiteKingSquare = i;
				break;
			case BLACK_KING:
				this.blackKingSquare = i;
				break;
			default:
		}
	}

	public void setBoard(Piece[] board) {
		this.board = board;
	}

	public void setIsReady(boolean value) {
		this.isReady = value;
	}

	public void setMoveNumber(int moveNumber) {
		this.moveNumber = moveNumber;
	}

	public void setStartingColor(Turn startingColor) {
		this.startingColor = startingColor;
	}

	public void setTurn(Turn turn) {
		this.turn = turn;
	}

	public void setWhiteCanCastleKingSide(boolean whiteCanCastleKingSide) {
		this.whiteCanCastleKingSide = whiteCanCastleKingSide;
	}

	public void setWhiteCanCastleQueenSide(boolean whiteCanCastleQueenSide) {
		this.whiteCanCastleQueenSide = whiteCanCastleQueenSide;
	}

	@Override
	public String toFENNotation() {
		StringBuilder buffer = new StringBuilder();
		int empty = 0;
		for (int i = 0; i < 64; i++) {
			if (i % 8 == 0 && i > 0) {
				if (empty != 0) {
					buffer.append(empty);
					empty = 0;
				}
				buffer.append("/"); //$NON-NLS-1$
			}
			switch (this.board[i]) {
				case WHITE_BISHOP:
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					}
					buffer.append('B');
					break;
				case WHITE_KING:
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					}
					buffer.append('K');
					break;
				case WHITE_KNIGHT:
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					}
					buffer.append('N');
					break;
				case WHITE_PAWN:
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					}
					buffer.append('P');
					break;
				case WHITE_QUEEN:
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					}
					buffer.append('Q');
					break;
				case WHITE_ROOK:
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					}
					buffer.append('R');
					break;
				case BLACK_BISHOP:
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					}
					buffer.append('b');
					break;
				case BLACK_KING:
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					}
					buffer.append('k');
					break;
				case BLACK_KNIGHT:
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					}
					buffer.append('n');
					break;
				case BLACK_PAWN:
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					}
					buffer.append('p');
					break;
				case BLACK_QUEEN:
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					}
					buffer.append('q');
					break;
				case BLACK_ROOK:
					if (empty != 0) {
						buffer.append(empty);
						empty = 0;
					}
					buffer.append('r');
					break;
				case EMPTY:
					empty++;
					break;
				case UNDEFINED:
				default:
			}
		}
		if (empty != 0) {
			buffer.append(empty);
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
		buffer.append(" " + Converter.intToSquare(this.enPassantSquare)); //$NON-NLS-1$
		buffer.append(" " + this.fiftyMovesRuleNumber + " " + this.startingMoveNumber); //$NON-NLS-1$ //$NON-NLS-2$
		return String.valueOf(buffer);
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < 64; i++) {
			if (i % 8 == 0) {
				if (i > 0) {
					buffer.append("|") //$NON-NLS-1$
							.append(Util.LINE_SEPARATOR);
				}
				buffer.append(" +-+-+-+-+-+-+-+-+") //$NON-NLS-1$
						.append(Util.LINE_SEPARATOR).append(8 - (i / 8));
			}
			buffer.append("|"); //$NON-NLS-1$
			char pieceType;
			switch (this.board[i]) {
				case WHITE_BISHOP:
					pieceType = 'F';
					break;
				case WHITE_KING:
					pieceType = 'R';
					break;
				case WHITE_KNIGHT:
					pieceType = 'C';
					break;
				case WHITE_PAWN:
					pieceType = 'P';
					break;
				case WHITE_QUEEN:
					pieceType = 'D';
					break;
				case WHITE_ROOK:
					pieceType = 'T';
					break;
				case BLACK_BISHOP:
					pieceType = 'f';
					break;
				case BLACK_KING:
					pieceType = 'r';
					break;
				case BLACK_KNIGHT:
					pieceType = 'c';
					break;
				case BLACK_PAWN:
					pieceType = 'p';
					break;
				case BLACK_QUEEN:
					pieceType = 'd';
					break;
				case BLACK_ROOK:
					pieceType = 't';
					break;
				case EMPTY:
					pieceType = ' ';
					break;
				default:
					pieceType = 'X';
			}
			buffer.append(pieceType);
		}
		buffer.append("|") //$NON-NLS-1$
				.append(Util.LINE_SEPARATOR).append(" +-+-+-+-+-+-+-+-+") //$NON-NLS-1$
				.append(Util.LINE_SEPARATOR).append("  a b c d e f g h ") //$NON-NLS-1$
				.append(Util.LINE_SEPARATOR).append(Util.LINE_SEPARATOR).append("move number = " + this.moveNumber) //$NON-NLS-1$
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
		return String.valueOf(buffer);
	}

	@Override
	public void undoMove() {
		undoMoveWithoutNotification();
		update();
	}

	@Override
	public void undoMove(long move) {
		undoMoveWithoutNotification(move);
		update();
	}

	@Override
	public void undoMoveWithoutNotification() {
		if (this.moveNumber >= 0) {
			undoMoveWithoutNotification(getLastMove());
		}
	}

	@Override
	public void undoMoveWithoutNotification(long move) {
		if (this.turn == WHITE_TURN) {
			this.turn = BLACK_TURN;
		} else {
			this.turn = WHITE_TURN;
		}
		final long info = move;
		int startingSquare = MoveConstants.getStartingSquare(info);
		int endingSquare = MoveConstants.getEndingSquare(info);
		Piece pieceType = this.board[endingSquare];
		Piece capturedPiece = Piece.getPiece(MoveConstants.getCaptureValue(info));

		// common behavior
		this.board[startingSquare] = pieceType;
		this.board[endingSquare] = capturedPiece;

		// handle promotion
		int promotedPiece = MoveConstants.getPromotionValue(info);
		if (promotedPiece != 0) {
			if (promotedPiece < 7) {
				this.board[startingSquare] = WHITE_PAWN;
			} else {
				this.board[startingSquare] = BLACK_PAWN;
			}
		}

		this.whiteCanCastleKingSide = MoveConstants.isWhiteCanCastleKingSide(info);
		this.whiteCanCastleQueenSide = MoveConstants.isWhiteCanCastleQueenSide(info);
		this.blackCanCastleKingSide = MoveConstants.isBlackCanCastleKingSide(info);
		this.blackCanCastleQueenSide = MoveConstants.isBlackCanCastleQueenSide(info);

		int fakeEnPassantSquare = MoveConstants.getEnPassantSquare(info);
		switch (fakeEnPassantSquare) {
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
				this.enPassantSquare = fakeEnPassantSquare + 15;
				break;
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
				this.enPassantSquare = fakeEnPassantSquare + 31;
				break;
			default:
				this.enPassantSquare = -1;
		}

		// detect castling and update the this.board
		switch (pieceType) {
			case WHITE_KING:
				if ((((startingSquare - endingSquare) == 2) || ((startingSquare - endingSquare) == -2))
						&& startingSquare == 60) {
					switch (endingSquare) {
						case 62:
							this.board[63] = WHITE_ROOK;
							this.board[61] = EMPTY;
							break;
						case 58:
							this.board[56] = WHITE_ROOK;
							this.board[59] = EMPTY;
						default:
					}
				}
				this.whiteKingSquare = startingSquare;
				break;
			case BLACK_KING:
				if ((((startingSquare - endingSquare) == 2) || ((startingSquare - endingSquare) == -2))
						&& startingSquare == 4) {
					switch (endingSquare) {
						case 6:
							this.board[7] = BLACK_ROOK;
							this.board[5] = EMPTY;
							break;
						case 2:
							this.board[0] = BLACK_ROOK;
							this.board[3] = EMPTY;
						default:
					}
				}
				this.blackKingSquare = startingSquare;
				break;
			case WHITE_PAWN:
				if (endingSquare == this.enPassantSquare) {
					switch (endingSquare - startingSquare) {
						case -7:
							this.board[startingSquare + 1] = capturedPiece;
							break;
						case -9:
							this.board[startingSquare - 1] = capturedPiece;
							break;
						default:
					}
					this.board[endingSquare] = EMPTY;
				}
				break;
			case BLACK_PAWN:
				if (endingSquare == this.enPassantSquare) {
					switch (startingSquare - endingSquare) {
						case -9:
							this.board[startingSquare + 1] = capturedPiece;
							break;
						case -7:
							this.board[startingSquare - 1] = capturedPiece;
							break;
						default:
					}
					this.board[endingSquare] = EMPTY;
				}
				break;
			default:
		}
		this.moveNumber--;
		this.startingMoveNumber--;
	}
}
