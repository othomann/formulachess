package org.formulachess.engine;

import java.io.IOException;
import java.io.StringWriter;

public class Converter implements MoveConstants, ModelConstants {

	public static String moveToString(int[] board, long move) {
		/*
		 * 		buffer.append((char) ((this.endingPosition % 8) + 'a'));
		 * 		buffer.append(8 - (this.endingPosition / 8));
		 */
		StringBuffer buffer = new StringBuffer();
		long info = move;
		int startingPosition = (int) (info & STARTING_SQUARE_MASK);
		int endingPosition = (int) ((info & ENDING_SQUARE_MASK) >> ENDING_SQUARE_SHIFT);
		switch(board[startingPosition]) {
			case WHITE_BISHOP :
			case BLACK_BISHOP :
				buffer.append('F');
				break;
			case WHITE_ROOK :
			case BLACK_ROOK :
				buffer.append('T');
				break;
			case WHITE_QUEEN :
			case BLACK_QUEEN :
				buffer.append('D');
				break;
			case WHITE_KING :
			case BLACK_KING :
				buffer.append('R');
				break;
			case WHITE_KNIGHT :
			case BLACK_KNIGHT :
				buffer.append('C');
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
