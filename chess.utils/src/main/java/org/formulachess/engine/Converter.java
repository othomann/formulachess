package org.formulachess.engine;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

public class Converter {

	private Converter() {
		// default constructor
	}

	public static String moveToString(Piece[] board, long move) {
		return moveToString(board, move, Locale.US);
	}

	public static String moveToString(Piece[] board, long move, Locale locale) {
		Messages messages = new Messages(locale);
		StringBuilder buffer = new StringBuilder();
		int startingPosition = MoveConstants.getStartingSquare(move);
		int endingPosition = MoveConstants.getEndingSquare(move);
		switch (board[startingPosition]) {
			case WHITE_BISHOP:
			case BLACK_BISHOP:
				buffer.append(messages.getString("piece.bishop")); //$NON-NLS-1$
				break;
			case WHITE_ROOK:
			case BLACK_ROOK:
				buffer.append(messages.getString("piece.rook")); //$NON-NLS-1$
				break;
			case WHITE_QUEEN:
			case BLACK_QUEEN:
				buffer.append(messages.getString("piece.queen")); //$NON-NLS-1$
				break;
			case WHITE_KING:
				if (MoveConstants.isCastle(move)) {
					if (endingPosition == ChessEngine.WHITE_KING_CASTLE_QUEEN_SIDE) {
						buffer.append("O-O-O");
					} else {
						buffer.append("O-O");
					}
					return String.valueOf(buffer);
				}
				buffer.append(messages.getString("piece.king")); //$NON-NLS-1$
				break;
			case BLACK_KING:
				if (MoveConstants.isCastle(move)) {
					if (endingPosition == ChessEngine.BLACK_KING_CASTLE_QUEEN_SIDE) {
						buffer.append("O-O-O");
					} else {
						buffer.append("O-O");
					}
					return String.valueOf(buffer);
				}
				buffer.append(messages.getString("piece.king")); //$NON-NLS-1$
				break;
			case WHITE_KNIGHT:
			case BLACK_KNIGHT:
				buffer.append(messages.getString("piece.knight")); //$NON-NLS-1$
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
		if (squareNumber == -1) {
			return "-"; //$NON-NLS-1$
		}
		StringBuilder buffer = new StringBuilder();
		buffer.append(MoveConstants.getColumn(squareNumber));
		buffer.append(MoveConstants.getRow(squareNumber));
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
