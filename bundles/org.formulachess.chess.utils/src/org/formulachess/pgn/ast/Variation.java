package org.formulachess.pgn.ast;

public class Variation extends ASTNode {

	private Move[] moves;
	
	public Variation(Move[] moves) {
		this.moves = moves;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('(');
		for (int i = 0, max = this.moves.length; i < max; i++) {
			Move move = this.moves[i];
			if (i == 0 && !move.isWhiteMove()) {
				builder
					.append(move.getMoveIndication())
					.append("...") //$NON-NLS-1$
					.append(move);
			} else {
				builder.append(move);
			}
			builder.append(' ');
		}
		builder.append(')');
		return String.valueOf(builder);
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
