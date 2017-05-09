package org.formulachess.pgn.ast;

public class TagPair extends ASTNode {
	private char[] name;
	private char[] value;

	public TagPair(char[] name, char[] value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * Returns the name.
	 * 
	 * @return char[]
	 */
	public char[] getName() {
		return this.name;
	}

	/**
	 * Returns the value.
	 * 
	 * @return char[]
	 */
	public char[] getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.name).append(this.value).append(']');
		return String.valueOf(builder);
	}
}
