package org.formulachess.engine.tests;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.ResourceBundle;

import org.formulachess.engine.ChessEngine;
import org.formulachess.engine.MoveConstants;
import org.formulachess.engine.Piece;

public class Converter implements MoveConstants {

	private Converter() {
		// disable constructor
	}

	public static String moveToString(Piece[] board, long move) {
		return moveToString(board, move, Locale.getDefault());
	}

	public static String moveToString(Piece[] board, long move, Locale locale) {
		ResourceBundle bundle = ResourceBundle.getBundle("org.formulachess.engine.messages", locale); //$NON-NLS-1$
		StringBuilder buffer = new StringBuilder();
		int startingPosition = (int) (move & STARTING_SQUARE_MASK);
		int endingPosition = (int) ((move & ENDING_SQUARE_MASK) >> ENDING_SQUARE_SHIFT);
		switch (board[startingPosition]) {
		case WHITE_BISHOP:
		case BLACK_BISHOP:
			buffer.append(bundle.getString("piece.bishop")); //$NON-NLS-1$
			break;
		case WHITE_ROOK:
		case BLACK_ROOK:
			buffer.append(bundle.getString("piece.rook")); //$NON-NLS-1$
			break;
		case WHITE_QUEEN:
		case BLACK_QUEEN:
			buffer.append(bundle.getString("piece.queen")); //$NON-NLS-1$
			break;
		case WHITE_KING:
		case BLACK_KING:
			buffer.append(bundle.getString("piece.king")); //$NON-NLS-1$
			break;
		case WHITE_KNIGHT:
		case BLACK_KNIGHT:
			buffer.append(bundle.getString("piece.knight")); //$NON-NLS-1$
			break;
		default:
		}
		buffer.append(intToSquare(endingPosition));
		return String.valueOf(buffer);
	}

	public static int squareToInt(String s) {
		if (s.length() != 2) {
			return -1;
		}
		return s.charAt(0) - 'a' + (8 - (s.charAt(1) - '0')) * 8;
	}

	public static String intToSquare(int squareNumber) {
		StringBuilder buffer = new StringBuilder();
		buffer.append((char) ((squareNumber % 8) + 'a'));
		buffer.append(8 - (squareNumber / 8));
		return String.valueOf(buffer);
	}

	public static String allNextMoves(ChessEngine model, long[] moves) throws IOException {
		try (StringWriter stringWriter = new StringWriter()) {
			for (int i = 0; i < moves.length; i++) {
				stringWriter.write(Converter.moveToString(model.getBoard(), moves[i]) + " "); //$NON-NLS-1$
			}
			return String.valueOf(stringWriter);
		}
	}
}
