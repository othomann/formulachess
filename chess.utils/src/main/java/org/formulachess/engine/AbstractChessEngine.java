package org.formulachess.engine;

import java.util.Arrays;

public interface AbstractChessEngine {

	public long[] allMoves();

	public long[] allMoves(Piece pieceType);

	public long[] allMoves(Piece pieceType, int startingSquare);

	public long getLastMove();

	public void initialize(String fenNotation, boolean isFischerRandom);

	public default void initialize(String fenNotation) {
		initialize(fenNotation, false);
	}

	public void initializeToStartingPosition();

	public boolean isBlackInCheck();

	public boolean isMate();

	public boolean isWhiteInCheck();

	public default long perft(int depth) {
		long[] moves = this.allMoves();
		if (depth == 1) {
			return moves.length;
		}
		long sum = 0;
		for (int i = 0, max = moves.length; i < max; i++) {
			this.playMoveWithoutNotification(moves[i]);
			sum += this.perft(depth - 1);
			this.undoMoveWithoutNotification(moves[i]);
		}
		return sum;
	}

	public void playMove(long move);

	public Piece getBoard(int position);
	
	public void playMoveWithoutNotification(long move);

	public String toFENNotation();

	public void undoMoveWithoutNotification();

	public void undoMove();

	public void undoMove(long move);

	public void undoMoveWithoutNotification(long move);

	public default Piece[] getEmptyBoard() {
		Piece[] array = new Piece[64];
		Arrays.fill(array, Piece.EMPTY);
		return array;
	}
}
