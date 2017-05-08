package org.formulachess.pgn.ast;

public class MoveText extends ASTNode {

	private Move[] moves;
	private GameTermination gameTermination;
	private Comment comment;
	
	public MoveText() {
		// nothing to do
	}

	/**
	 * Sets the gameTermination.
	 * @param gameTermination The gameTermination to set
	 */
	public void setGameTermination(GameTermination gameTermination) {
		this.gameTermination = gameTermination;
	}

	/**
	 * Sets the moves.
	 * @param moves The moves to set
	 */
	public void setMoves(Move[] moves) {
		this.moves = moves;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (this.comment != null) {
			builder.append(this.comment);
			builder.append(LINE_SEPARATOR);
		}
		for (int i = 0, max = this.moves == null? 0 : this.moves.length; i < max; i++) {
			builder.append(this.moves[i]).append(' ');
		}
		builder.append(LINE_SEPARATOR);
		builder.append(this.gameTermination);
		return String.valueOf(builder);
	}

	public Move getMove(int i) {
		if (i >= this.moves.length) {
			return null;
		}
		return this.moves[i];
	}
	/**
	 * Returns the moves.
	 * @return Move[]
	 */
	public Move[] getMoves() {
		return this.moves;
	}

}
