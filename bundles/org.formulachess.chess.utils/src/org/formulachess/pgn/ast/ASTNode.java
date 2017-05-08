package org.formulachess.pgn.ast;

import java.util.ArrayList;
import java.util.List;

public abstract class ASTNode {

	public static final String LINE_SEPARATOR = System.getProperty("line.separator"); //$NON-NLS-1$
	
	private ASTNode parent;
	private List<Comment> comments;

	/**
	 * Returns the parent.
	 * @return ASTNode
	 */
	public ASTNode getParent() {
		return this.parent;
	}

	/**
	 * Sets the parent.
	 * @param parent The parent to set
	 */
	public void setParent(ASTNode parent) {
		this.parent = parent;
	}

	/**
	 * @param comment
	 */
	public void setComments(List<Comment> comments) {
		if (comments != null && !comments.isEmpty()) {
			if (this.comments == null) {
				this.comments = new ArrayList<Comment>();
			}
			this.comments.addAll(comments);
		}
	}

}
