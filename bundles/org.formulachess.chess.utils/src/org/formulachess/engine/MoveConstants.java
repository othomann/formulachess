package org.formulachess.engine;

/**
 *  1: starting square
 *  2: starting square
 *  3: starting square
 *  4: starting square
 *  5: starting square
 *  6: starting square
 *  7: ending square
 *  8: ending square
 *  9: ending square
 * 10: ending square
 * 11: ending square
 * 12: ending square
 * 13: capture piece
 * 14: capture piece
 * 15: capture piece
 * 16: capture piece
 * 17: promotion piece
 * 18: promotion piece
 * 19: promotion piece
 * 20: promotion piece
 * 21: en passant square
 * 22: en passant square
 * 23: en passant square
 * 24: en passant square
 * 25: en passant square
 * 26: castling
 * 27: castling
 * 28: castling
 * 29: castling
 * 30: check
 * 31: castle
 * 32:
 */
public class MoveConstants {
	private static final int STARTING_SQUARE_MASK = 0x3F;
	private static final int ENDING_SQUARE_SHIFT = 0x06;
	private static final int ENDING_SQUARE_MASK = 0x3F << ENDING_SQUARE_SHIFT;
	private static final int CAPTURE_PIECE_SHIFT = 0x0C;
	private static final int CAPTURE_PIECE_MASK = 0xF << CAPTURE_PIECE_SHIFT;
	private static final int PROMOTION_PIECE_SHIFT = 0x10;
	private static final int PROMOTION_PIECE_MASK = 0xF << PROMOTION_PIECE_SHIFT;
	private static final int EN_PASSANT_SQUARE_SHIFT = 0x14;
	private static final int EN_PASSANT_SQUARE_MASK = 0x1F << EN_PASSANT_SQUARE_SHIFT;
	private static final int CASTLING_SHIFT = 0x19;
	private static final int CASTLING_MASK = 0xF << CASTLING_SHIFT;
	private static final int CHECK_SHIFT = 0x1D;
	private static final int CHECK_MASK = 0x1 << CHECK_SHIFT;
	private static final int CASTLE_SHIFT = 0x1E;
	private static final int CASTLE_MASK = 0x1 << CASTLE_SHIFT;

	private MoveConstants() {
		// default constructor
	}

	public static int getStartingSquare(long move) {
		return (int) (move & STARTING_SQUARE_MASK);
	}

	public static int getEndingSquare(long move) {
		return (int) ((move & ENDING_SQUARE_MASK) >> ENDING_SQUARE_SHIFT);
	}

	public static int getCaptureValue(long move) {
		return (int) ((move & CAPTURE_PIECE_MASK) >> CAPTURE_PIECE_SHIFT);
	}

	public static int getPromotionValue(long move) {
		return (int) ((move & PROMOTION_PIECE_MASK) >> PROMOTION_PIECE_SHIFT);
	}

	public static boolean isWhiteCanCastleKingSide(long info) {
		return (((info & CASTLING_MASK) >> CASTLING_SHIFT) & 0x1) == 0x1;
	}

	public static boolean isWhiteCanCastleQueenSide(long info) {
		return (((info & CASTLING_MASK) >> CASTLING_SHIFT) & 0x2) == 0x2;
	}

	public static boolean isBlackCanCastleKingSide(long info) {
		return (((info & CASTLING_MASK) >> CASTLING_SHIFT) & 0x4) == 0x4;
	}

	public static boolean isBlackCanCastleQueenSide(long info) {
		return (((info & CASTLING_MASK) >> CASTLING_SHIFT) & 0x8) == 0x8;
	}

	public static int getEnPassantSquare(long info) {
		return (int) ((info & EN_PASSANT_SQUARE_MASK) >> EN_PASSANT_SQUARE_SHIFT);
	}

	public static boolean isPromotion(long move) {
		return (move & PROMOTION_PIECE_MASK) != 0;
	}

	public static long getMoveValue(
			int startingPosition,
			int endingPosition,
			Piece capturePieceType,
			Piece promotedPiece,
			int enPassantSquare,
			boolean whiteCanCastleKingSide,
			boolean whiteCanCastleQueenSide,
			boolean blackCanCastleKingSide,
			boolean blackCanCastleQueenSide) {
		long info = 0;
		info |= startingPosition;
		info |= endingPosition << ENDING_SQUARE_SHIFT;
		info |= capturePieceType.getValue() << CAPTURE_PIECE_SHIFT;
		info |= promotedPiece.getValue() << PROMOTION_PIECE_SHIFT;
		switch (enPassantSquare) {
			case 16:
			case 17:
			case 18:
			case 19:
			case 20:
			case 21:
			case 22:
			case 23:
				info |= (enPassantSquare - 15) << EN_PASSANT_SQUARE_SHIFT;
				break;
			case 40:
			case 41:
			case 42:
			case 43:
			case 44:
			case 45:
			case 46:
			case 47:
				info |= (enPassantSquare - 31) << EN_PASSANT_SQUARE_SHIFT;
				break;
			default:
		}
		info |= (whiteCanCastleKingSide ? 1 : 0) << CASTLING_SHIFT;
		info |= (whiteCanCastleQueenSide ? 1 : 0) << (CASTLING_SHIFT + 1);
		info |= (blackCanCastleKingSide ? 1 : 0) << (CASTLING_SHIFT + 2);
		info |= (blackCanCastleQueenSide ? 1 : 0) << (CASTLING_SHIFT + 3);
		return info;
	}

	public static long tagAsCheck(long info) {
		return info | 1 << CHECK_SHIFT;
	}

	public static long tagAsCastle(long info) {
		return info | 1 << CASTLE_SHIFT;
	}

	public static Object getColumn(int squareNumber) {
		return (char) ((squareNumber % 8) + 'a');
	}

	public static Object getRow(int squareNumber) {
		return 8 - (squareNumber / 8);
	}

	public static boolean isCheck(long move) {
		return (move & MoveConstants.CHECK_MASK) >> MoveConstants.CHECK_SHIFT != 0;
	}

	public static boolean isCastle(long move) {
		return (move & MoveConstants.CASTLE_MASK) >> MoveConstants.CASTLE_SHIFT != 0;
	}
}
