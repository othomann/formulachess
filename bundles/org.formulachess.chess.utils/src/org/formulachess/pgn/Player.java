package org.formulachess.pgn;

public class Player {
	public String firstName;
	public String lastName;

	public Player(String tagName) {
		int indexOfComa = tagName.indexOf(',');
		if (indexOfComa == -1) {
			this.lastName = tagName.substring(1, tagName.length() - 1);
			this.firstName = ""; //$NON-NLS-1$
		} else {
			this.lastName = tagName.substring(1, indexOfComa).trim();
			this.firstName =
				tagName.substring(indexOfComa + 1, tagName.length() - 1).trim();
		}
	}
	public String toString() {
		return this.lastName + ", " + this.firstName; //$NON-NLS-1$
	}
}
