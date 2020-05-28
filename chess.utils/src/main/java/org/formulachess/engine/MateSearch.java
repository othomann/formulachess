package org.formulachess.engine;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.formulachess.pgn.engine.PGNMoveContainer;

public class MateSearch {
	private static final Logger MY_LOGGER = Logger.getLogger(MateSearch.class.getCanonicalName());
	public static final boolean DEBUG = false;

	private MateSearch() {
		// default constructor
	}

	public static MateMove[] createSearchableMoves(ChessEngine model, long[] moves, boolean sort) {
		int movesLength = moves.length;
		PGNMoveContainer container = new PGNMoveContainer(model, moves, Locale.ENGLISH);
		MateMove[] next = new MateMove[movesLength];
		for (int i = 0; i < movesLength; i++) {
			final String moveNotation = container.getMoveNotation(moves[i]);
			model.playMoveWithoutNotification(moves[i]);
			int mob = model.allMoves().length;
			model.undoMoveWithoutNotification(moves[i]);
			next[i] = new MateMove(moves[i], mob, moveNotation);
		}
		if (sort && next.length > 1) {
			Arrays.sort(next, (o1, o2) -> o1.compareTo(o2));
		}
		return next;
	}

	public static boolean searchMate(ChessEngine model, int depth, int maximum, MateNode result) {
		long[] nextMoves = model.allMoves();
		int nextMovesLength = nextMoves.length;
		MateMove[] next = createSearchableMoves(model, nextMoves, true);
		MateNode currentResult = result;
		loop: for (int i = 0; i < nextMovesLength; i++) {
			long move = next[i].getMove();
			MateNode current = currentResult.add(next[i], model.getTurn(), depth);
			if ((depth == maximum && MateMove.isCheck(move)) || depth != maximum) {
				int mobility = next[i].getMobility();
				if (mobility == 0) {
					debug(next[i], 2 * depth - 1);
					if (!MateMove.isCheck(move)) {
						// stalemate
						currentResult.remove(next[i], depth);
						continue loop;
					}
					// mate
					if (depth != 1) {
						return true;
					}
					currentResult.remove(next[i], depth);
				} else if (depth < maximum) {
					debug(next[i], 2 * depth - 1);
					model.playMoveWithoutNotification(move);
					int moveCounter = 0;
					currentResult = current;
					MateMove[] opponentNextMoves = createSearchableMoves(model, model.allMoves(), true);
					opponentLoop: for (int j = 0, max = opponentNextMoves.length; j < max; j++) {
						long opponentMove = opponentNextMoves[j].getMove();
						debug(opponentNextMoves[j], 2 * depth);
						MateNode opponentCurrent = currentResult.add(opponentNextMoves[j], model.getTurn(), depth);
						currentResult = opponentCurrent;
						model.playMoveWithoutNotification(opponentMove);
						if (!searchMate(model, depth + 1, maximum, currentResult)) {
							model.undoMoveWithoutNotification(opponentMove);
							currentResult = currentResult.parent;
							currentResult.remove(opponentNextMoves[j], depth);
							break opponentLoop;
						}
						currentResult = currentResult.parent;
						moveCounter++;
						model.undoMoveWithoutNotification(opponentMove);
					}
					currentResult = currentResult.parent;
					model.undoMoveWithoutNotification(move);
					if (mobility == moveCounter) {
						return true;
					}
					currentResult.remove(next[i], depth);
				} else {
					// mobility != 0 && depth == maximum
					// we can stop this depth
					currentResult.remove(next[i], depth);
					return false;
				}
			} else {
				currentResult.remove(next[i], depth);
			}
		}
		return false;
	}

	static void debug(MateMove move, int depth) {
		if (!DEBUG) {
			return;
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < depth - 1; i++) {
			builder.append('\t');
		}
		builder.append(move).append("\n");
		MY_LOGGER.log(Level.INFO, () -> String.valueOf(builder));
	}
}
