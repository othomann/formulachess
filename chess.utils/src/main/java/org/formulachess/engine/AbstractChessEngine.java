package org.formulachess.engine;

import static org.formulachess.engine.Piece.BLACK_BISHOP;
import static org.formulachess.engine.Piece.BLACK_KING;
import static org.formulachess.engine.Piece.BLACK_KNIGHT;
import static org.formulachess.engine.Piece.BLACK_PAWN;
import static org.formulachess.engine.Piece.BLACK_QUEEN;
import static org.formulachess.engine.Piece.BLACK_ROOK;
import static org.formulachess.engine.Piece.EMPTY;
import static org.formulachess.engine.Piece.WHITE_BISHOP;
import static org.formulachess.engine.Piece.WHITE_KING;
import static org.formulachess.engine.Piece.WHITE_KNIGHT;
import static org.formulachess.engine.Piece.WHITE_PAWN;
import static org.formulachess.engine.Piece.WHITE_QUEEN;
import static org.formulachess.engine.Piece.WHITE_ROOK;

import java.util.Arrays;
import java.util.Locale;
import java.util.Observable;

import org.formulachess.pgn.engine.PGNMoveContainer;

public abstract class AbstractChessEngine extends Observable {

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
		PGNMoveContainer pgnMoveContainer = new PGNMoveContainer(this, moves, Locale.FRANCE);
		System.out.println(pgnMoveContainer.toString(true));
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
		this.setChanged();
		this.notifyObservers();
	}

	public static Piece[] getEmptyBoard() {
		Piece[] array = new Piece[64];
		Arrays.fill(array, Piece.EMPTY);
		return array;
	}

	public static Piece[] getInitialPosition() {
		return new Piece[] { BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING, BLACK_BISHOP,
				BLACK_KNIGHT, BLACK_ROOK, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN,
				BLACK_PAWN, BLACK_PAWN, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
				EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
				EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN,
				WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING,
				WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK };
	}
}
