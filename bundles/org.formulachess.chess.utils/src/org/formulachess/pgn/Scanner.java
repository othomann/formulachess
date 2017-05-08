package org.formulachess.pgn;

import java.util.ArrayList;

import org.formulachess.pgn.ast.Comment;

/**
 * @author oliviert
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Scanner implements TerminalSymbols {

	public static final String UNTERMINATED_STRING = "Unterminated_String"; //$NON-NLS-1$
	public static final String UNTERMINATED_COMMENT = "Unterminated_Comment"; //$NON-NLS-1$
	public static final String INVALID_CHAR_IN_STRING = "Invalid_Char_In_String"; //$NON-NLS-1$
	public static final String UNTERMINATED_TAG_NAME = "Unterminated_Tag_Name"; //$NON-NLS-1$
	public static final String UNTERMINATED_NAG = "Unterminated_NAG"; //$NON-NLS-1$

	public char currentCharacter;
	public int startPosition;
	public int currentPosition;
	public int initialPosition, eofPosition;

	public char source[];
	public char[] pieceIdentifications = new char[] { 'N', 'R', 'K', 'B', 'Q' };
	public ArrayList<Comment> comments;

	public Scanner() {
		this.comments = new ArrayList<Comment>();
	}

	public int getNextToken() throws InvalidInputException {

		try {
			while (true) {
				boolean isWhiteSpace;
				boolean hasWhiteSpace = false;
				do {
					this.startPosition = this.currentPosition;
					this.currentCharacter = this.source[this.currentPosition++];
					isWhiteSpace =
						(this.currentCharacter == ' ')
							|| Character.isWhitespace(this.currentCharacter);
					hasWhiteSpace = hasWhiteSpace || isWhiteSpace;
				} while (isWhiteSpace);
				if (this.currentCharacter == '$') {
					try {
						this.currentCharacter = this.source[this.currentPosition++];
						if (!Character.isDigit(this.currentCharacter)) {
							return TokenNameERROR;
						}
						while (Character.isDigit(this.currentCharacter)) {
							this.currentCharacter = this.source[this.currentPosition++];
						}
						this.currentPosition--;
						return TokenNameStart_nag;
					} catch (IndexOutOfBoundsException e) {
						throw new InvalidInputException(UNTERMINATED_NAG);
					}
				}
				if (hasWhiteSpace) {
					this.currentPosition--;
					return TokenNameWHITESPACE;	
				}
				//little trick to get out in the middle of a source computation
				if (this.currentPosition > this.eofPosition)
					return TokenNameEOF;

				// ---------Identify the next token-------------

				switch (this.currentCharacter) {
					case '(' :
						return TokenNameSTART_VARIATION;
					case ')' :
						return TokenNameEND_VARIATION;
					case '[' :
						try {
							this.currentCharacter = this.source[this.currentPosition++];
							while (!Character.isWhitespace(this.currentCharacter)) {
								// consume next character
								this.currentCharacter = this.source[this.currentPosition++];
							}
							return TokenNameStart_Tag_Section;
						} catch (IndexOutOfBoundsException e) {
							throw new InvalidInputException(UNTERMINATED_TAG_NAME);
						}
					case ']' :
						return TokenNameEND_TAG_SECTION;
					case '.' :
						return TokenNameDOT;
					case '+' :
						return TokenNameCHECK;
					case '#' :
						return TokenNameCHECKMATE;
					case 'x' :
					case 'X' :
						return TokenNameCAPTURE;
					case '*' :
						return TokenNameUNKNOWN;
					case '=' :
						return TokenNamePROMOTE;
					case '?' :
						if (checkQuestionMark()) {
							this.currentCharacter = this.source[this.currentPosition++];
							return TokenNameVERY_BAD_MOVE;
						}
						if (checkExclamationPoint()) {
							this.currentCharacter = this.source[this.currentPosition++];
							return TokenNameSUSPICIOUS_MOVE;
						}
						return TokenNameBAD_MOVE;
					case '!' :
						if (checkQuestionMark()) {
							this.currentCharacter = this.source[this.currentPosition++];
							return TokenNameINTERESTING_MOVE;
						}
						if (checkExclamationPoint()) {
							this.currentCharacter = this.source[this.currentPosition++];
							return TokenNameEXCELLENT_MOVE;
						}
						return TokenNameGOOD_MOVE;
					case '"' :
						try {
							// consume next character
							this.currentCharacter = this.source[this.currentPosition++];

							while (this.currentCharacter != '"') {
								/**** \r and \n are not valid in string literals ****/
								if ((this.currentCharacter == '\n')
									|| (this.currentCharacter == '\r')) {
									// relocate if finding another quote fairly close: thus unicode '/u000D' will be fully consumed
									for (int lookAhead = 0;
										lookAhead < 50;
										lookAhead++) {
										if (this.currentPosition + lookAhead
											== this.source.length)
											break;
										if (this.source[this.currentPosition + lookAhead]
											== '\n')
											break;
										if (this.source[this.currentPosition + lookAhead]
											== '\"') {
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
							throw new InvalidInputException(UNTERMINATED_STRING);
						}
						return TokenNameStringLiteral;
					case '{' :
						try {
							// consume next character
							this.currentCharacter = this.source[this.currentPosition++];

							while (this.currentCharacter != '}') {
								// consume next character
								this.currentCharacter = this.source[this.currentPosition++];
							}
						} catch (IndexOutOfBoundsException e) {
							throw new InvalidInputException(UNTERMINATED_COMMENT);
						}
						recordComment();
						while (Character.isWhitespace(this.source[this.currentPosition])) {
							this.currentPosition++;
						}						
						break;
					case 'a' :
					case 'b' :
					case 'c' :
					case 'd' :
					case 'e' :
						if (getNextChar('p')) {
							return TokenNameEN_PASSANT;
						}
						return TokenNameFileName;
					case 'f' :
					case 'g' :
					case 'h' :
						return TokenNameFileName;
					case '$' :
						try {
							this.currentCharacter = this.source[this.currentPosition++];
							if (!Character.isDigit(this.currentCharacter)) {
								return TokenNameERROR;
							}
							while (Character.isDigit(this.currentCharacter)) {
								this.currentCharacter = this.source[this.currentPosition++];
							}
							this.currentPosition--;
							return TokenNameStart_nag;
						} catch (IndexOutOfBoundsException e) {
							throw new InvalidInputException(UNTERMINATED_NAG);
						}
					case 'O' :
						this.currentCharacter = this.source[this.currentPosition++];
						if (checkDash()) {
							this.currentCharacter = this.source[this.currentPosition++];
							if (this.currentCharacter == 'O') {
								this.currentCharacter = this.source[this.currentPosition++];
								if (checkDash()) {
									this.currentCharacter =
										this.source[this.currentPosition++];
									if (this.currentCharacter == 'O') {
										return TokenNameCASTLE_QUEEN_SIDE;
									}
								} else {
									this.currentPosition--;
									return TokenNameCASTLE_KING_SIDE;
								}
							}
						}
						return TokenNameERROR;
					case ';' :
						try {
							// consume next character
							this.currentCharacter = this.source[this.currentPosition++];

							while (this.currentCharacter != '\n' && this.currentCharacter != '\r') {
								// consume next character
								this.currentCharacter = this.source[this.currentPosition++];
							}
							this.currentPosition--;
						} catch (IndexOutOfBoundsException e) {
							throw new InvalidInputException(UNTERMINATED_COMMENT);
						}
						recordComment();
						while (Character.isWhitespace(this.source[this.currentPosition])) {
							this.currentPosition++;
						}
						break;
					default :
						if (this.isPieceIndentification()) {
							return TokenNamePieceIdentification;
						}
						if (Character.isDigit(this.currentCharacter)) {
							try {
								// consume next character
								this.currentCharacter = this.source[this.currentPosition++];

								while (Character.isDigit(this.currentCharacter)) {
									// consume next character
									this.currentCharacter =
										this.source[this.currentPosition++];
								}
							} catch (IndexOutOfBoundsException e) {
								return TokenNameEOF;
							}
							if (checkDot()) {
								this.currentPosition--;
								return TokenNameIntegerLiteral;
							} else if (checkDash()) {
								this.currentCharacter = this.source[this.currentPosition];
								switch (this.currentCharacter) {
									case '0' :
										if (this.source[this.currentPosition - 2]
											!= '1') {
											return TokenNameERROR;
										}
										this.currentPosition++;
										return TokenNameWHITE_VICTORY;
									case '1' :
										if (this.source[this.currentPosition - 2]
											!= '0') {
											return TokenNameERROR;
										}
										this.currentPosition++;
										return TokenNameBLACK_VICTORY;
									default :
										return TokenNameERROR;
								}
							} else if (checkSlash()) {
								if (this.source[this.currentPosition - 2] != '1') {
									return TokenNameERROR;
								}
								if ((this.source[this.currentPosition] == '2')
										&& (this.source[this.currentPosition+1] == '-')
										&& (this.source[this.currentPosition+2] == '1')
										&& (this.source[this.currentPosition+3] == '/')
										&& (this.source[this.currentPosition+4] == '2')) {
									this.currentPosition += 5;
									return TokenNameDRAW;
								}
							} else {
								this.currentPosition--;
								return TokenNameRankName;
							}
						}
						return TokenNameERROR;
				}
			}
		} //-----------------end switch while try--------------------
		catch (IndexOutOfBoundsException e) {
			// ignore
		}
		return TokenNameEOF;
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
		try {
			return this.source[this.currentPosition - 1] == '.';
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	private boolean checkDash() {
		try {
			return this.source[this.currentPosition - 1] == '-';
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	private boolean checkQuestionMark() {
		try {
			return this.source[this.currentPosition] == '?';
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	private boolean checkExclamationPoint() {
		try {
			return this.source[this.currentPosition] == '!';
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	private boolean checkSlash() {
		try {
			return this.source[this.currentPosition - 1] == '/';
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	public void setSource(char[] source) {
		this.source = source;
	}

	private void recordComment() {
		this.comments.add(new Comment(this.getCurrentTokenSource()));
	}

	public void resetTo(int begin, int end) {
		//reset the scanner to a given position where it may rescan again
		this.initialPosition = this.startPosition = this.currentPosition = begin;
		this.eofPosition = end < Integer.MAX_VALUE ? end + 1 : end;
	}

	public char[] getCurrentTokenSource() {
		char[] result;
		int length;
		System.arraycopy(
			this.source,
			this.startPosition,
			result = new char[length = this.currentPosition - this.startPosition],
			0,
			length);
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
			System.arraycopy(
				this.source, 
				this.startPosition, 
				middle, 
				0, 
				middleLength);
		} else {
			middle = new char[0];
		}
		
		char[] end = new char[this.source.length - (this.currentPosition - 1)];
		System.arraycopy(
			this.source, 
			(this.currentPosition - 1) + 1, 
			end, 
			0, 
			this.source.length - (this.currentPosition - 1) - 1);
		
		return new String(front)
			+ "\n===============================\nStarts here -->" //$NON-NLS-1$
			+ new String(middle)
			+ "<-- Ends here\n===============================\n" //$NON-NLS-1$
			+ new String(end); 
	}
}
