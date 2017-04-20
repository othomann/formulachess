package org.formulachess.pgn.ast;

public class Comment extends ASTNode {
	private char[] contents;

	public Comment(char[] commentContents) {
		this.contents = commentContents;
	}
	
	public boolean isLineComment() {
		return this.contents != null && this.contents[0] == ';';
	}
	
	public String toString() {
		if (isLineComment()) {
			return new String(this.contents, 1, this.contents.length - 1);
		}
		return new String(this.contents, 1, this.contents.length - 2);
	}
}
