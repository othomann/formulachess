package org.formulachess.engine;

import java.util.Arrays;

public abstract class AbstractChessEngine {

	public abstract long[] allMoves();

	public abstract long[] allMoves(Piece pieceType);

	public abstract long[] allMoves(Piece pieceType, int startingSquare);

	public abstract long getLastMove();

	public abstract void initialize(String fenNotation, boolean isFischerRandom);

	public void initialize(String fenNotation) {
		initialize(fenNotation, false);
	}

	public abstract void initializeToStartingPosition();

	public abstract boolean isBlackInCheck();

	public abstract boolean isMate();

	public abstract boolean isWhiteInCheck();

	public long perft(int depth) {
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

	public abstract void playMove(long move);

	public abstract Piece getBoard(int position);
	
	public abstract void playMoveWithoutNotification(long move);

	public abstract String toFENNotation();

	public abstract void undoMoveWithoutNotification();

	public abstract void undoMove();

	public abstract void undoMove(long move);

	public abstract void undoMoveWithoutNotification(long move);

	public void update() {
		//this.setChanged();
		//this.notifyObservers();
	}

	public static Piece[] getEmptyBoard() {
		Piece[] array = new Piece[64];
		Arrays.fill(array, Piece.EMPTY);
		return array;
	}
}
