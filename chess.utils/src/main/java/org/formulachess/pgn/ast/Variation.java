package org.formulachess.pgn.ast;

import org.formulachess.pgn.ASTVisitor;

public class Variation extends ASTNode {

	private Move[] moves;

	public Variation(Move[] moves) {
		this.moves = moves;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('(');
		int i= 0 ;
		for (Move move : this.getMoves()) {
			if (i == 0 && !move.isWhiteMove()) {
				builder.append(move.getMoveIndication()).append("...") //$NON-NLS-1$
						.append(move);
			} else {
				builder.append(move);
			}
			builder.append(' ');
			i++;
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
	 * 
	 * @return Move[]
	 */
	public Move[] getMoves() {
		return this.moves;
	}

	@Override
	public void accept(ASTVisitor visitor) {
		if (visitor.visit(this)) {
			for (Move move : this.getMoves()) {
				move.accept(visitor);
			}
		}
		visitor.endVisit(this);
	}
}
