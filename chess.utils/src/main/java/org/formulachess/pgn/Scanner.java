package org.formulachess.pgn;

import java.util.ArrayList;
import java.util.List;

import org.formulachess.pgn.ast.Comment;

public class Scanner {
	
	// TerminalSymbols
	public static final int TOKEN_NAME_INTEGER = 19;
	public static final int TOKEN_NAME_STRING = 30;
	public static final int TOKEN_NAME_COLUMN = 1;
	public static final int TOKEN_NAME_RANK = 4;
	public static final int TOKEN_NAME_PIECE_ID = 5;
	public static final int TOKEN_NAME_START_TAG_SECTION = 31;
	public static final int TOKEN_NAME_START_NAG = 6;
	public static final int TOKEN_NAME_START_VARIATION = 20;
	public static final int TOKEN_NAME_END_VARIATION = 32;
	public static final int TOKEN_NAME_DOT = 15;
	public static final int TOKEN_NAME_END_TAG_SECTION = 33;
	public static final int TOKEN_NAME_WHITE_VICTORY = 34;
	public static final int TOKEN_NAME_BLACK_VICTORY = 35;
	public static final int TOKEN_NAME_DRAW = 36;
	public static final int TOKEN_NAME_UNKNOWN = 37;
	public static final int TOKEN_NAME_CAPTURE = 14;
	public static final int TOKEN_NAME_CHECK = 21;
	public static final int TOKEN_NAME_CHECKMATE = 22;
	public static final int TOKEN_NAME_CASTLE_KING_SIDE = 16;
	public static final int TOKEN_NAME_CASTLE_QUEEN_SIDE = 17;
	public static final int TOKEN_NAME_PROMOTE = 23;
	public static final int TOKEN_NAME_EXCELLENT_MOVE = 7;
	public static final int TOKEN_NAME_VERY_BAD_MOVE = 8;
	public static final int TOKEN_NAME_BAD_MOVE = 9;
	public static final int TOKEN_NAME_GOOD_MOVE = 10;
	public static final int TOKEN_NAME_INTERESTING_MOVE = 11;
	public static final int TOKEN_NAME_SUSPICIOUS_MOVE = 12;
	public static final int TOKEN_NAME_GREATER_THAN = 38;
	public static final int TOKEN_NAME_EN_PASSANT = 39;
	public static final int TOKEN_NAME_WHITESPACE = 2;
	public static final int TOKEN_NAME_EOF = 24;
	public static final int TOKEN_NAME_ERROR = 51;

	public static final String UNTERMINATED_STRING = "Unterminated_String"; //$NON-NLS-1$
	public static final String UNTERMINATED_COMMENT = "Unterminated_Comment"; //$NON-NLS-1$
	public static final String INVALID_CHAR_IN_STRING = "Invalid_Char_In_String"; //$NON-NLS-1$
	public static final String UNTERMINATED_TAG_NAME = "Unterminated_Tag_Name"; //$NON-NLS-1$
	public static final String UNTERMINATED_NAG = "Unterminated_NAG"; //$NON-NLS-1$

	private char currentCharacter;
	private int startPosition;
	private int currentPosition;
	private int eofPosition;

	private char[] source;
	private char[] pieceIdentifications = new char[] { 'N', 'R', 'K', 'B', 'Q' };
	private List<Comment> comments;

	public Scanner() {
		this.comments = new ArrayList<>();
	}

	public int getNextToken() throws InvalidInputException {

		try {
			while (true) {
				boolean isWhiteSpace;
				boolean hasWhiteSpace = false;
				do {
					this.startPosition = this.currentPosition;
					this.currentCharacter = this.source[this.currentPosition++];
					isWhiteSpace = (this.currentCharacter == ' ') || Character.isWhitespace(this.currentCharacter);
					hasWhiteSpace = hasWhiteSpace || isWhiteSpace;
				} while (isWhiteSpace);
				if (this.currentCharacter == '$') {
					return consumeNag();
				}
				if (hasWhiteSpace) {
					this.currentPosition--;
					return TOKEN_NAME_WHITESPACE;
				}
				// little trick to get out in the middle of a source computation
				if (this.currentPosition > this.eofPosition)
					return TOKEN_NAME_EOF;

				// ---------Identify the next token-------------

				switch (this.currentCharacter) {
					case '(':
						return TOKEN_NAME_START_VARIATION;
					case ')':
						return TOKEN_NAME_END_VARIATION;
					case '[':
						return consumeTagSection();
					case ']':
						return TOKEN_NAME_END_TAG_SECTION;
					case '.':
						return TOKEN_NAME_DOT;
					case '+':
						return TOKEN_NAME_CHECK;
					case '#':
						return TOKEN_NAME_CHECKMATE;
					case 'x':
					case 'X':
						return TOKEN_NAME_CAPTURE;
					case '*':
						return TOKEN_NAME_UNKNOWN;
					case '=':
						return TOKEN_NAME_PROMOTE;
					case '?':
						if (checkQuestionMark()) {
							this.currentCharacter = this.source[this.currentPosition++];
							return TOKEN_NAME_VERY_BAD_MOVE;
						}
						if (checkExclamationPoint()) {
							this.currentCharacter = this.source[this.currentPosition++];
							return TOKEN_NAME_SUSPICIOUS_MOVE;
						}
						return TOKEN_NAME_BAD_MOVE;
					case '!':
						if (checkQuestionMark()) {
							this.currentCharacter = this.source[this.currentPosition++];
							return TOKEN_NAME_INTERESTING_MOVE;
						}
						if (checkExclamationPoint()) {
							this.currentCharacter = this.source[this.currentPosition++];
							return TOKEN_NAME_EXCELLENT_MOVE;
						}
						return TOKEN_NAME_GOOD_MOVE;
					case '"':
						return consumeStringLiteral();
					case '{':
						consumeComment();
						break;
					case 'a':
					case 'b':
					case 'c':
					case 'd':
					case 'e':
						if (getNextChar('p')) {
							return TOKEN_NAME_EN_PASSANT;
						}
						return TOKEN_NAME_COLUMN;
					case 'f':
					case 'g':
					case 'h':
						return TOKEN_NAME_COLUMN;
					case '$':
						return consumeNag();
					case 'O':
						this.currentCharacter = this.source[this.currentPosition++];
						if (checkDash()) {
							this.currentCharacter = this.source[this.currentPosition++];
							if (this.currentCharacter == 'O') {
								this.currentCharacter = this.source[this.currentPosition++];
								if (checkDash()) {
									this.currentCharacter = this.source[this.currentPosition++];
									if (this.currentCharacter == 'O') {
										return TOKEN_NAME_CASTLE_QUEEN_SIDE;
									}
								} else {
									this.currentPosition--;
									return TOKEN_NAME_CASTLE_KING_SIDE;
								}
							}
						}
						return TOKEN_NAME_ERROR;
					case ';':
						consumeLineComment();
						break;
					default:
						if (this.isPieceIndentification()) {
							return TOKEN_NAME_PIECE_ID;
						}
						if (Character.isDigit(this.currentCharacter)) {
							try {
								// consume next character
								this.currentCharacter = this.source[this.currentPosition++];

								while (Character.isDigit(this.currentCharacter)) {
									// consume next character
									this.currentCharacter = this.source[this.currentPosition++];
								}
							} catch (IndexOutOfBoundsException e) {
								return TOKEN_NAME_EOF;
							}
							if (checkDot()) {
								this.currentPosition--;
								return TOKEN_NAME_INTEGER;
							} else if (checkDash()) {
								this.currentCharacter = this.source[this.currentPosition];
								switch (this.currentCharacter) {
									case '0':
										if (this.source[this.currentPosition - 2] != '1') {
											return TOKEN_NAME_ERROR;
										}
										this.currentPosition++;
										return TOKEN_NAME_WHITE_VICTORY;
									case '1':
										if (this.source[this.currentPosition - 2] != '0') {
											return TOKEN_NAME_ERROR;
										}
										this.currentPosition++;
										return TOKEN_NAME_BLACK_VICTORY;
									default:
										return TOKEN_NAME_ERROR;
								}
							} else if (checkSlash()) {
								if (this.source[this.currentPosition - 2] != '1') {
									return TOKEN_NAME_ERROR;
								}
								if ((this.source[this.currentPosition] == '2')
										&& (this.source[this.currentPosition + 1] == '-')
										&& (this.source[this.currentPosition + 2] == '1')
										&& (this.source[this.currentPosition + 3] == '/')
										&& (this.source[this.currentPosition + 4] == '2')) {
									this.currentPosition += 5;
									return TOKEN_NAME_DRAW;
								}
							} else {
								this.currentPosition--;
								return TOKEN_NAME_RANK;
							}
						}
						return TOKEN_NAME_ERROR;
				}
			}
		} // -----------------end switch while try--------------------
		catch (IndexOutOfBoundsException e) {
			// ignore
		}
		return TOKEN_NAME_EOF;
	}

	private void consumeLineComment() throws InvalidInputException {
		try {
			// consume next character
			this.currentCharacter = this.source[this.currentPosition++];

			while (this.currentCharacter != '\n' && this.currentCharacter != '\r') {
				// consume next character
				this.currentCharacter = this.source[this.currentPosition++];
			}
			this.currentPosition--;
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidInputException(UNTERMINATED_COMMENT, e);
		}
		recordComment();
		while (Character.isWhitespace(this.source[this.currentPosition])) {
			this.currentPosition++;
		}
	}

	private void consumeComment() throws InvalidInputException {
		try {
			// consume next character
			this.currentCharacter = this.source[this.currentPosition++];

			while (this.currentCharacter != '}') {
				// consume next character
				this.currentCharacter = this.source[this.currentPosition++];
			}
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidInputException(UNTERMINATED_COMMENT, e);
		}
		recordComment();
		while (Character.isWhitespace(this.source[this.currentPosition])) {
			this.currentPosition++;
		}
	}

	private int consumeNag() throws InvalidInputException {
		try {
			this.currentCharacter = this.source[this.currentPosition++];
			if (!Character.isDigit(this.currentCharacter)) {
				return TOKEN_NAME_ERROR;
			}
			while (Character.isDigit(this.currentCharacter)) {
				this.currentCharacter = this.source[this.currentPosition++];
			}
			this.currentPosition--;
			return TOKEN_NAME_START_NAG;
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidInputException(UNTERMINATED_NAG, e);
		}
	}

	private int consumeTagSection() throws InvalidInputException {
		try {
			this.currentCharacter = this.source[this.currentPosition++];
			while (!Character.isWhitespace(this.currentCharacter)) {
				// consume next character
				this.currentCharacter = this.source[this.currentPosition++];
			}
			return TOKEN_NAME_START_TAG_SECTION;
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidInputException(UNTERMINATED_TAG_NAME, e);
		}
	}

	private int consumeStringLiteral() throws InvalidInputException {
		try {
			// consume next character
			this.currentCharacter = this.source[this.currentPosition++];

			while (this.currentCharacter != '"') {
				/**** \r and \n are not valid in string literals ****/
				if ((this.currentCharacter == '\n') || (this.currentCharacter == '\r')) {
					// relocate if finding another quote fairly close: thus unicode '/u000D' will be
					// fully consumed
					for (int lookAhead = 0; lookAhead < 50; lookAhead++) {
						if (this.currentPosition + lookAhead == this.source.length)
							break;
						if (this.source[this.currentPosition + lookAhead] == '\n')
							break;
						if (this.source[this.currentPosition + lookAhead] == '\"') {
							this.currentPosition += lookAhead + 1;
							break;
						}
					}
					throw new InvalidInputException(INVALID_CHAR_IN_STRING);
				}
				// consume next character
				this.currentCharacter = this.source[this.currentPosition++];
			}
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidInputException(UNTERMINATED_STRING, e);
		}
		return TOKEN_NAME_STRING;
	}

	private boolean isPieceIndentification() {
		for (int i = 0, max = this.pieceIdentifications.length; i < max; i++) {
			if (this.currentCharacter == this.pieceIdentifications[i]) {
				return true;
			}
		}
		return false;
	}

	private boolean checkDot() {
		return this.source[this.currentPosition - 1] == '.';
	}

	private boolean checkDash() {
		return this.source[this.currentPosition - 1] == '-';
	}

	private boolean checkQuestionMark() {
		return this.source[this.currentPosition] == '?';
	}

	private boolean checkExclamationPoint() {
		return this.source[this.currentPosition] == '!';
	}

	private boolean checkSlash() {
		return this.source[this.currentPosition - 1] == '/';
	}

	public void setSource(char[] source) {
		this.source = source;
	}

	private void recordComment() {
		this.comments.add(new Comment(this.getCurrentTokenSource()));
	}

	public void resetTo(int begin, int end) {
		// reset the scanner to a given position where it may rescan again
		this.startPosition = this.currentPosition = begin;
		this.eofPosition = end < Integer.MAX_VALUE ? end + 1 : end;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void clearComments() {
		if (this.comments != null) {
			this.comments.clear();
		}
	}

	public char[] getCurrentTokenSource() {
		char[] result;
		int length;
		System.arraycopy(this.source, this.startPosition,
				result = new char[length = this.currentPosition - this.startPosition], 0, length);
		return result;
	}

	public final boolean getNextChar(char testedChar) {
		if (this.currentPosition >= this.source.length) { // handle the obvious case upfront
			return false;
		}

		int temp = this.currentPosition;
		try {
			this.currentCharacter = this.source[this.currentPosition++];
			if (this.currentCharacter != testedChar) {
				this.currentPosition = temp;
				return false;
			}
			return true;
		} catch (IndexOutOfBoundsException e) {
			this.currentPosition = temp;
			return false;
		}
	}

	@Override
	public String toString() {
		if (this.startPosition == this.source.length)
			return "EOF\n\n" + new String(this.source); //$NON-NLS-1$
		if (this.currentPosition > this.source.length)
			return "behind the EOF :-( ....\n\n" + new String(this.source); //$NON-NLS-1$

		char[] front = new char[this.startPosition];
		System.arraycopy(this.source, 0, front, 0, this.startPosition);

		int middleLength = (this.currentPosition - 1) - this.startPosition + 1;
		char[] middle;
		if (middleLength > -1) {
			middle = new char[middleLength];
			System.arraycopy(this.source, this.startPosition, middle, 0, middleLength);
		} else {
			middle = new char[0];
		}

		char[] end = new char[this.source.length - (this.currentPosition - 1)];
		System.arraycopy(this.source, (this.currentPosition - 1) + 1, end, 0,
				this.source.length - (this.currentPosition - 1) - 1);

		return new String(front) + "\n===============================\nStarts here -->" //$NON-NLS-1$
				+ new String(middle) + "<-- Ends here\n===============================\n" //$NON-NLS-1$
				+ new String(end);
	}
}
