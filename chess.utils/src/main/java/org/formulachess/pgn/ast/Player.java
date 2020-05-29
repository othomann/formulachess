package org.formulachess.pgn.ast;

public class Player {
	private static final String NO_NAME = ""; //$NON-NLS-1$
	private String firstName;
	private String lastName;

	public Player(String tagName) {
		if (tagName == null) {
			this.firstName = NO_NAME;
			this.lastName = NO_NAME;
		} else {
			int indexOfComa = tagName.indexOf(',');
			if (indexOfComa == -1) {
				this.lastName = tagName.substring(1, tagName.length() - 1);
				this.firstName = ""; //$NON-NLS-1$
			} else {
				this.lastName = tagName.substring(1, indexOfComa).trim();
				this.firstName = tagName.substring(indexOfComa + 1, tagName.length() - 1).trim();
			}
		}
	}

	@Override
	public String toString() {
		return this.lastName + ", " + this.firstName; //$NON-NLS-1$
	}
}
