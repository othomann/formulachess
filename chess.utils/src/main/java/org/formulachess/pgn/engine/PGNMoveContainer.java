package org.formulachess.pgn.engine;

import static org.formulachess.engine.Piece.EMPTY;
import static org.formulachess.engine.Piece.UNDEFINED;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import org.formulachess.engine.ChessEngine;
import org.formulachess.engine.MoveConstants;
import org.formulachess.engine.Piece;
import org.formulachess.util.HashtableOfLong;

public class PGNMoveContainer {

	private HashtableOfLong pgnNotationContainer;
	private HashtableOfLong currentLocaleContainer;
	private HashMap<Locale, ResourceBundle> bundles;
	private long[] moves;

	public PGNMoveContainer(ChessEngine model, long[] moves, Locale currentLocale) {
		this.bundles = new HashMap<>();
		this.moves = moves;
		this.bundles.put(Locale.ENGLISH, ResourceBundle.getBundle("org.formulachess.engine.messages", Locale.ENGLISH)); //$NON-NLS-1$
		this.bundles.put(currentLocale, ResourceBundle.getBundle("org.formulachess.engine.messages", currentLocale)); //$NON-NLS-1$

		this.pgnNotationContainer = new HashtableOfLong();
		this.currentLocaleContainer = new HashtableOfLong();
		if (moves == null) {
			return;
		}
		for (int i = 0, max = moves.length; i < max; i++) {
			String notation = getShortNotation(model, moves[i], Locale.ENGLISH);
			String currentLocalNotation = getShortNotation(model, moves[i], currentLocale);
			if (this.pgnNotationContainer.containsKey(notation)) {
				// ambiguity
				long conflictMove = this.pgnNotationContainer.remove(notation);
				this.currentLocaleContainer.remove(currentLocalNotation);
				String oldNotation = getColumnNotation(model, conflictMove, Locale.ENGLISH);
				notation = getColumnNotation(model, moves[i], Locale.ENGLISH);
				currentLocalNotation = getColumnNotation(model, moves[i], currentLocale);
				if (oldNotation.equals(notation)) {
					this.pgnNotationContainer.put(getRowNotation(model, conflictMove, Locale.ENGLISH), conflictMove);
					this.pgnNotationContainer.put(getRowNotation(model, moves[i], Locale.ENGLISH), moves[i]);
					this.currentLocaleContainer.put(getRowNotation(model, conflictMove, currentLocale), conflictMove);
					this.currentLocaleContainer.put(getRowNotation(model, moves[i], currentLocale), moves[i]);
				} else {
					this.pgnNotationContainer.put(oldNotation, conflictMove);
					this.pgnNotationContainer.put(notation, moves[i]);
					this.currentLocaleContainer.put(getColumnNotation(model, conflictMove, currentLocale),
							conflictMove);
					this.currentLocaleContainer.put(currentLocalNotation, moves[i]);
				}
			} else {
				this.pgnNotationContainer.put(notation, moves[i]);
				this.currentLocaleContainer.put(currentLocalNotation, moves[i]);
			}
		}
	}

	public boolean contains(String moveNotation) {
		return this.pgnNotationContainer.containsKey(moveNotation);
	}

	public long get(String moveNotation) {
		return this.pgnNotationContainer.get(moveNotation);
	}

	public String getMoveNotation(long move) {
		for (String moveNotation : this.currentLocaleContainer) {
			if (this.currentLocaleContainer.get(moveNotation) == move) {
				return moveNotation;
			}
		}
		return null;
	}

	public String getPGNMoveNotation(long move) {
		for (String moveNotation : this.pgnNotationContainer) {
			if (this.pgnNotationContainer.get(moveNotation) == move) {
				return moveNotation;
			}
		}
		return null;
	}

	private String getShortNotation(ChessEngine model, long move, Locale locale) {
		return moveToString(model, move, locale, false, false);
	}

	private String getColumnNotation(ChessEngine model, long move, Locale locale) {
		return moveToString(model, move, locale, true, false);
	}

	private String getRowNotation(ChessEngine model, long move, Locale locale) {
		return moveToString(model, move, locale, false, true);
	}

	private String moveToString(
			ChessEngine model,
			final long move,
			Locale locale,
			boolean columnAmbiguity,
			boolean rowAmbiguity) {
		ResourceBundle bundle = this.bundles.get(locale);
		StringBuilder builder = new StringBuilder();
		int startingPosition = MoveConstants.getStartingSquare(move);
		int endingPosition = MoveConstants.getEndingSquare(move);
		Piece capturePiece = Piece.getPiece(MoveConstants.getCaptureValue(move));
		Piece promotion = Piece.getPiece(MoveConstants.getPromotionValue(move));
		switch (model.getBoard(startingPosition)) {
			case WHITE_BISHOP:
			case BLACK_BISHOP:
				builder.append(bundle.getString("piece.bishop")); //$NON-NLS-1$
				break;
			case WHITE_ROOK:
			case BLACK_ROOK:
				builder.append(bundle.getString("piece.rook")); //$NON-NLS-1$
				break;
			case WHITE_QUEEN:
			case BLACK_QUEEN:
				builder.append(bundle.getString("piece.queen")); //$NON-NLS-1$
				break;
			case WHITE_KING:
			case BLACK_KING:
				if (Math.abs(startingPosition - endingPosition) == 2) {
					if (endingPosition > startingPosition) {
						return "O-O"; //$NON-NLS-1$
					}
					return "O-O-O"; //$NON-NLS-1$
				}
				builder.append(bundle.getString("piece.king")); //$NON-NLS-1$
				break;
			case WHITE_KNIGHT:
			case BLACK_KNIGHT:
				builder.append(bundle.getString("piece.knight")); //$NON-NLS-1$
				break;
			case WHITE_PAWN:
			case BLACK_PAWN:
				if (capturePiece != EMPTY) {
					builder.append((char) ((startingPosition % 8) + 'a'));
				}
				break;
			default:
		}
		if (columnAmbiguity) {
			builder.append(MoveConstants.getColumn(startingPosition));
		}
		if (rowAmbiguity) {
			builder.append(MoveConstants.getRow(startingPosition));
		}
		if (capturePiece != EMPTY) {
			builder.append("x"); //$NON-NLS-1$
		}
		builder.append(MoveConstants.getColumn(endingPosition));
		builder.append(MoveConstants.getRow(endingPosition));
		if (promotion != UNDEFINED) {
			builder.append("="); //$NON-NLS-1$
			switch (promotion) {
				case WHITE_BISHOP:
				case BLACK_BISHOP:
					builder.append(bundle.getString("piece.bishop")); //$NON-NLS-1$
					break;
				case WHITE_ROOK:
				case BLACK_ROOK:
					builder.append(bundle.getString("piece.rook")); //$NON-NLS-1$
					break;
				case WHITE_QUEEN:
				case BLACK_QUEEN:
					builder.append(bundle.getString("piece.queen")); //$NON-NLS-1$
					break;
				case WHITE_KNIGHT:
				case BLACK_KNIGHT:
					builder.append(bundle.getString("piece.knight")); //$NON-NLS-1$
					break;
				default:
			}
		}
		return String.valueOf(builder);
	}

	@Override
	public String toString() {
		return toString(true);
	}

	public String toString(boolean sort) {
		StringBuilder builder = new StringBuilder();
		if (sort) {
			String[] movesNotations = new String[this.currentLocaleContainer.size()];
			int i = 0;
			for (String moveNotation : this.currentLocaleContainer) {
				movesNotations[i] = moveNotation;
				i++;
			}
			java.util.Arrays.sort(movesNotations);
			i = 0;
			for (int max = movesNotations.length; i < max; i++) {
				builder.append(movesNotations[i]).append(" "); //$NON-NLS-1$
			}
		} else if (this.moves != null) {
			for (int i = 0, max = this.moves.length; i < max; i++) {
				builder.append(getMoveNotation(this.moves[i])).append(" "); //$NON-NLS-1$
			}
		}
		return String.valueOf(builder);
	}

}
