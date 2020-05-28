package org.formulachess.pgn.ast;

import org.formulachess.pgn.ASTVisitor;

public class MoveText extends ASTNode {

	private Move[] moves;
	private GameTermination gameTermination;
	private Comment comment;

	public MoveText() {
		// nothing to do
	}

	/**
	 * Sets the gameTermination.
	 * 
	 * @param gameTermination
	 *            The gameTermination to set
	 */
	public void setGameTermination(GameTermination gameTermination) {
		this.gameTermination = gameTermination;
	}

	/**
	 * Returns the gameTermination.
	 * 
	 * return the gameTermination
	 */
	public GameTermination getGameTermination() {
		return this.gameTermination;
	}

	/**
	 * Sets the moves.
	 * 
	 * @param moves
	 *            The moves to set
	 */
	public void setMoves(Move[] moves) {
		this.moves = moves;
	}

	/**
	 * Returns the comment
	 * 
	 * @return the comment
	 */
	public Comment getComment() {
		return this.comment;
	}

	/**
	 * Set the comment
	 *
	 * @param the given comment
	 */
	public void setComment(Comment comment) {
		this.comment = comment;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		Comment currentComment = this.getComment();
		if (currentComment != null) {
			builder.append(currentComment);
			builder.append(LINE_SEPARATOR);
		}
		for (Move move: this.getMoves()) {
			builder.append(move).append(' ');
		}
		builder.append(LINE_SEPARATOR);
		builder.append(this.getGameTermination());
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
	 * 
	 * @return Move[]
	 */
	public Move[] getMoves() {
		return this.moves;
	}

	@Override
	public void accept(ASTVisitor visitor) {
		if (visitor.visit(this)) {
			Comment comment = this.getComment();
			if (comment != null) {
				comment.accept(visitor);
			}
			Move[] moves = this.getMoves();
			if (moves != null) {
				for (Move move : moves) {
					move.accept(visitor);
				}
			}
			GameTermination termination = this.getGameTermination();
			if (termination != null) {
				termination.accept(visitor);
			}
		}
		visitor.endVisit(this);
	}
}
