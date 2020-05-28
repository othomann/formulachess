package org.formulachess.pgn.ast;

import org.formulachess.pgn.ASTVisitor;

public class PGNGame extends ASTNode {

	private TagSection tagSection;
	private MoveText moveText;

	public PGNGame(MoveText moveText, TagSection tagSection) {
		this.tagSection = tagSection;
		this.moveText = moveText;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getTagSection()).append(LINE_SEPARATOR).append(this.getMoveText());
		return String.valueOf(builder);
	}

	/**
	 * Returns the tagSection.
	 * 
	 * @return TagSection
	 */
	public TagSection getTagSection() {
		return this.tagSection;
	}

	/**
	 * Returns the moveText.
	 * 
	 * @return MoveText
	 */
	public MoveText getMoveText() {
		return this.moveText;
	}

	@Override
	public void accept(ASTVisitor visitor) {
		if (visitor.visit(this)) {
			this.getTagSection().accept(visitor);
			this.getMoveText().accept(visitor);
		}
		visitor.endVisit(this);
	}
}
