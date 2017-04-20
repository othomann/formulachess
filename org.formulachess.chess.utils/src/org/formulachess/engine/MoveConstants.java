package org.formulachess.engine;

/**
 * 31 30 29 28 27 26 25 24 23 22 21 20 19 18 17 16 15 14 13 12 11 10 9 8 7 6 5 4 3 2 1 0
 *                                                                           1 0 0 0 0 0
 *                                                              1  0 0 0 0 0
 *                                                  1  0  0  0
 *                                      0  0  0  0
 *                       1  1  1  1  1
 *           1  1  1  1
 *        1
 *     1
 */
public interface MoveConstants {
	int STARTING_SQUARE_MASK = 0x3F;
	int ENDING_SQUARE_SHIFT = 0x06;
	int ENDING_SQUARE_MASK = 0x3F << ENDING_SQUARE_SHIFT;
	int CAPTURE_PIECE_SHIFT = 0x0C;
	int CAPTURE_PIECE_MASK = 0xF << CAPTURE_PIECE_SHIFT;
	int PROMOTION_PIECE_SHIFT = 0x10;
	int PROMOTION_PIECE_MASK = 0xF << PROMOTION_PIECE_SHIFT;
	int EN_PASSANT_SQUARE_SHIFT = 0x14;
	int EN_PASSANT_SQUARE_MASK = 0x1F << EN_PASSANT_SQUARE_SHIFT;
	int CASTLING_SHIFT = 0x19;
	int CASTLING_MASK = 0xF << CASTLING_SHIFT;
	int CHECK_SHIFT = 0x1D;
	int CHECK_MASK = 0x1 << CHECK_SHIFT;
	int MATE_SHIFT = 0x1E;
	int MATE_MASK = 0x1 << MATE_SHIFT;
}
