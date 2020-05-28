package org.formulachess.pgn;

/**
 * Exception thrown by a scanner when encountering lexical errors.
 */
public class InvalidInputException extends Exception {
	private static final long serialVersionUID = 7676395983128570528L;

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
	 *            the given message
	 */
	public InvalidInputException(String s) {
		super(s);
	}

	/**
	 * InvalidInputException constructor comment.
	 * 
	 * @param s
	 *            the given message
	 * @param e
	 *            the causing exception
	 */
	public InvalidInputException(String s, Exception e) {
		super(s, e);
	}
}
