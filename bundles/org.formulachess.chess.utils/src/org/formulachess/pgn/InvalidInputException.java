package org.formulachess.pgn;

/**
 * Exception thrown by a scanner when encountering lexical errors.
 */
public class InvalidInputException extends Exception {

	private static final long serialVersionUID = 1113984964082788778L;

	/**
	 * InvalidInputException constructor comment.
	 */
	public InvalidInputException() {
		super();
	}

	/**
	 * InvalidInputException constructor comment.
	 * 
	 * @param s
	 *            java.lang.String
	 */
	public InvalidInputException(String s) {
		super(s);
	}
}
