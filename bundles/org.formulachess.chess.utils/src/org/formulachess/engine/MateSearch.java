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
		if (sort) {
			Arrays.sort(next, new Comparator<MateMove>() {
				public int compare(MateMove o1, MateMove o2) {
					return o1.compareTo(o2);
				}
			});
		}
		return next;
	}

	public static boolean searchMate(ChessEngine model, int depth, int maximum, MateNode result) {
		long[] nextMoves = model.allMoves();
		int nextMovesLength = nextMoves.length;
		MateMove[] next = createSearchableMoves(model, nextMoves, true);
		loop: for (int i = 0; i < nextMovesLength; i++) {
			long move = next[i].getMove();
			MateNode current = result.add(next[i], model.getTurn(), depth);
			if ((depth == maximum && MateMove.isCheck(move)) || depth != maximum) {
				int mobility = next[i].getMobility();
				if (mobility == 0) {
					if (DEBUG) {
						debug(next[i], 2 * depth - 1);
					}
					if (!MateMove.isCheck(move)) {
						// stalemate
						result.remove(next[i], depth);
						continue loop;
					}
					// mate
					if (depth != 1) {
						return true;
					}
					result.remove(next[i], depth);
				} else if (depth < maximum) {
					if (DEBUG) {
						debug(next[i], 2 * depth - 1);
					}
					model.playMoveWithoutNotification(move);
					int move_counter = 0;
					result = current;
					MateMove[] opponentNextMoves = createSearchableMoves(model, model.allMoves(), true);
					opponentLoop: for (int j = 0, max = opponentNextMoves.length; j < max; j++) {
						long opponentMove = opponentNextMoves[j].getMove();
						if (DEBUG) {
							debug(opponentNextMoves[j], 2 * depth);
						}
						MateNode opponentCurrent = result.add(opponentNextMoves[j], model.getTurn(), depth);
						result = opponentCurrent;
						model.playMoveWithoutNotification(opponentMove);
						if (!searchMate(model, depth + 1, maximum, result)) {
							model.undoMoveWithoutNotification(opponentMove);
							result = result.parent;
							result.remove(opponentNextMoves[j], depth);
							break opponentLoop;
						}
						result = result.parent;
						move_counter++;
						model.undoMoveWithoutNotification(opponentMove);
					}
					result = result.parent;
					model.undoMoveWithoutNotification(move);
					if (mobility == move_counter) {
						return true;
					}
					result.remove(next[i], depth);
				} else {
					// mobility != 0 && depth == maximum
					// we can stop this depth
					result.remove(next[i], depth);
					return false;
				}
			} else {
				result.remove(next[i], depth);
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
		MY_LOGGER.log(Level.INFO, String.valueOf(builder));
	}
}
