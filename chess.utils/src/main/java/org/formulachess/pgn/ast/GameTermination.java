package org.formulachess.pgn.ast;

import org.formulachess.pgn.ASTVisitor;

public class GameTermination extends ASTNode {

	public static final GameTermination BLACK_VICTORY = new GameTermination("0-1"); //$NON-NLS-1$
	public static final GameTermination WHITE_VICTORY = new GameTermination("1-0"); //$NON-NLS-1$
	public static final GameTermination DRAW = new GameTermination("1/2-1/2"); //$NON-NLS-1$
	public static final GameTermination UNKNOWN = new GameTermination("*"); //$NON-NLS-1$

	private String result;

	private GameTermination(String result) {
		this.result = result;
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
		visitor.endVisit(this);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.result;
	}
}
