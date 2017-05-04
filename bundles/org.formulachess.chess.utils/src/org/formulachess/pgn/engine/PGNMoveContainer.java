package org.formulachess.pgn.engine;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import org.formulachess.engine.ChessEngine;
import org.formulachess.engine.ModelConstants;
import org.formulachess.engine.MoveConstants;
import org.formulachess.engine.Piece;
import org.formulachess.util.HashtableOfLong;

import static org.formulachess.engine.Piece.*;

public class PGNMoveContainer implements MoveConstants, ModelConstants {

	private HashtableOfLong pgnNotationContainer;
	private HashtableOfLong currentLocaleContainer;
	private Hashtable<Locale, ResourceBundle> bundles;
	private long[] moves;
	
	public PGNMoveContainer(ChessEngine model, long[] moves, Locale currentLocale) {
		this.bundles = new Hashtable<Locale, ResourceBundle>();
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
					this.currentLocaleContainer.put(getColumnNotation(model, conflictMove, currentLocale), conflictMove);
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
		for (Enumeration<String> enumeration = this.currentLocaleContainer.keys(); enumeration.hasMoreElements(); ) {
			String moveNotation = enumeration.nextElement();
			if (this.currentLocaleContainer.get(moveNotation) == move) {
				return moveNotation;
			}
		}
		return null;
	}

	public String getPGNMoveNotation(long move) {
		for (Enumeration<String> enumeration = this.pgnNotationContainer.keys(); enumeration.hasMoreElements(); ) {
			String moveNotation = enumeration.nextElement();
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
	
	private String moveToString(ChessEngine model, long move, Locale locale, boolean columnAmbiguity, boolean rowAmbiguity) {
		/*
		 * 		buffer.append((char) ((this.endingPosition % 8) + 'a'));
		 * 		buffer.append(8 - (this.endingPosition / 8));
		 */
		ResourceBundle bundle = this.bundles.get(locale);
		StringBuffer buffer = new StringBuffer();
		long info = move;
		int startingPosition = (int) (info & STARTING_SQUARE_MASK);
		int endingPosition = (int) ((info & ENDING_SQUARE_MASK) >> ENDING_SQUARE_SHIFT);
		Piece capturePiece = Piece.getPiece((int) ((info & CAPTURE_PIECE_MASK) >> CAPTURE_PIECE_SHIFT));
		Piece promotion = Piece.getPiece((int) ((info & PROMOTION_PIECE_MASK) >> PROMOTION_PIECE_SHIFT));
		switch(model.getBoard(startingPosition)) {
			case WHITE_BISHOP :
			case BLACK_BISHOP :
				buffer.append(bundle.getString("piece.bishop")); //$NON-NLS-1$
				break;
			case WHITE_ROOK :
			case BLACK_ROOK :
				buffer.append(bundle.getString("piece.rook")); //$NON-NLS-1$
				break;
			case WHITE_QUEEN :
			case BLACK_QUEEN :
				buffer.append(bundle.getString("piece.queen")); //$NON-NLS-1$
				break;
			case WHITE_KING :
			case BLACK_KING :
				if (Math.abs(startingPosition - endingPosition) == 2) {
					if (endingPosition > startingPosition) {
						return "O-O"; //$NON-NLS-1$
					}
					return "O-O-O"; //$NON-NLS-1$
				}
				buffer.append(bundle.getString("piece.king")); //$NON-NLS-1$
				break;
			case WHITE_KNIGHT :
			case BLACK_KNIGHT :
				buffer.append(bundle.getString("piece.knight")); //$NON-NLS-1$
				break;
			case WHITE_PAWN :
			case BLACK_PAWN :
				if (capturePiece != EMPTY) {
					buffer.append((char) ((startingPosition % 8) + 'a'));
				}
			default:
		}
		if (columnAmbiguity) {
			buffer.append((char) ((startingPosition % 8) + 'a'));
		}
		if (rowAmbiguity) {
			buffer.append(8 - (startingPosition / 8));
		}
		if (capturePiece != EMPTY) {
			buffer.append("x"); //$NON-NLS-1$
		}
		buffer.append((char) ((endingPosition % 8) + 'a'));
		buffer.append(8 - (endingPosition / 8));
		if (promotion != UNDEFINED) {
			buffer.append("="); //$NON-NLS-1$
			switch(promotion) {
				case WHITE_BISHOP :
				case BLACK_BISHOP :
					buffer.append(bundle.getString("piece.bishop")); //$NON-NLS-1$
					break;
				case WHITE_ROOK :
				case BLACK_ROOK :
					buffer.append(bundle.getString("piece.rook")); //$NON-NLS-1$
					break;
				case WHITE_QUEEN :
				case BLACK_QUEEN :
					buffer.append(bundle.getString("piece.queen")); //$NON-NLS-1$
					break;
				case WHITE_KNIGHT :
				case BLACK_KNIGHT :
					buffer.append(bundle.getString("piece.knight")); //$NON-NLS-1$
					break;
				default:
			}
		}
		return buffer.toString();
	}
	
	public String toString() {
		return toString(true);
	}
	
	public String toString(boolean sort) {
		StringBuffer buffer = new StringBuffer();
		if (sort) {
			Enumeration<String> enumeration = this.currentLocaleContainer.keys();
			String[] movesNotations = new String[this.currentLocaleContainer.size()];
			int i = 0;
			for (; enumeration.hasMoreElements(); ) {
				movesNotations[i] = enumeration.nextElement();
				i++;
			}
			java.util.Arrays.sort(movesNotations);
			i = 0;
			for (int max = movesNotations.length; i < max; i++) {
				buffer
					.append(movesNotations[i])
					.append(" "); //$NON-NLS-1$
			}
		} else if (this.moves != null) {
			for (int i = 0, max = this.moves.length; i < max; i++) {
				buffer
					.append(getMoveNotation(this.moves[i]))
					.append(" "); //$NON-NLS-1$
			}
		}
		return buffer.toString();
	}
	
}
