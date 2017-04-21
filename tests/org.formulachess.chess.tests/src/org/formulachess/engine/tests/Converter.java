package org.formulachess.engine.tests;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.ResourceBundle;

import org.formulachess.engine.ChessEngine;
import org.formulachess.engine.ModelConstants;
import org.formulachess.engine.MoveConstants;

public class Converter implements MoveConstants, ModelConstants {

	public static String moveToString(int[] board, long move) {
		return moveToString(board, move, Locale.getDefault());
	}

	public static String moveToString(int[] board, long move, Locale locale) {
		/*
		 * 		buffer.append((char) ((this.endingPosition % 8) + 'a'));
		 * 		buffer.append(8 - (this.endingPosition / 8));
		 */
		ResourceBundle bundle = ResourceBundle.getBundle("org.formulachess.engine.messages", locale); //$NON-NLS-1$
		StringBuffer buffer = new StringBuffer();
		int startingPosition = (int) (move & STARTING_SQUARE_MASK);
		int endingPosition = (int) ((move & ENDING_SQUARE_MASK) >> ENDING_SQUARE_SHIFT);
		switch(board[startingPosition]) {
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
				buffer.append(bundle.getString("piece.king")); //$NON-NLS-1$
				break;
			case WHITE_KNIGHT :
			case BLACK_KNIGHT :
				buffer.append(bundle.getString("piece.knight")); //$NON-NLS-1$
				break;
		}
		buffer.append((char) ((endingPosition % 8) + 'a'));
		buffer.append(8 - (endingPosition / 8));
		return buffer.toString();
	}
	
	public static int squareToInt(String s) {
		if (s.length() != 2) {
			return -1;
		}
		return s.charAt(0) - 'a' + (8 - (s.charAt(1) - '0') ) * 8;
	}
	
	public static String intToSquare(int squareNumber) {
		StringBuffer buffer = new StringBuffer();
		buffer.append((char) ((squareNumber % 8) + 'a'));
		buffer.append(8 - (squareNumber / 8));
		return buffer.toString();
	}
	
	public static String allNextMoves(ChessEngine model, long[] moves) {
		StringWriter stringWriter = null;
		try {
			stringWriter = new StringWriter();
			for (int i = 0; i < moves.length; i++) {
				stringWriter.write(Converter.moveToString(model.board, moves[i]) + " "); //$NON-NLS-1$
			}
			stringWriter.flush();
			stringWriter.close();
		} catch (IOException e) {
			return "Could not retrieve all moves"; //$NON-NLS-1$
		}
		return stringWriter.toString();
	}
}
