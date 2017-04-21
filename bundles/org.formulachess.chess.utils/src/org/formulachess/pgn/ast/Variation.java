package org.formulachess.pgn.ast;

public class Variation extends ASTNode {

	private Move[] moves;
	
	public Variation(Move[] moves) {
		this.moves = moves;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append('(');
		for (int i = 0, max = this.moves.length; i < max; i++) {
			Move move = this.moves[i];
			if (i == 0 && !move.isWhiteMove()) {
				buffer
					.append(move.getMoveIndication())
					.append("...") //$NON-NLS-1$
					.append(move);
			} else {
				buffer.append(move);
			}
			buffer.append(' ');
		}
		buffer.append(')');
		return buffer.toString();
	}
	
	public Move getMove(int i) {
		if (i < 0 || i > this.moves.length) {
			return null;
		}
		return this.moves[i];
	}
	/**
	 * Method getMoves.
	 * @return Move[]
	 */
	public Move[] getMoves() {
		return this.moves;
	}

}
