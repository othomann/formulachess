package org.formulachess.pgn.ast;

import java.util.ResourceBundle;

public class Castle extends Move {

	private boolean isKingSize;
	
	public Castle(boolean isKingSize) {
		this.isKingSize = isKingSize;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		if (this.isWhiteMove) {
			buffer.append(this.moveIndication).append('.').append(' ');
		}
		if (this.isKingSize) {
			buffer.append("O-O"); //$NON-NLS-1$
		} else {
			buffer.append("O-O-O"); //$NON-NLS-1$
		}
		if (this.nag != null) {
			buffer.append(this.nag);
		}
		if (this.isCheck) {
			buffer.append('+');
		}
		if (this.isCheckMate) {
			buffer.append('#');
		}
		return buffer.toString();
	}

	/**
	 * @see formulachess.pgn.ast.MateMove#getMoveNotation()
	 */
	public String getMoveNotation() {
		StringBuffer buffer = new StringBuffer();
		if (this.isKingSize) {
			buffer.append("O-O"); //$NON-NLS-1$
		} else {
			buffer.append("O-O-O"); //$NON-NLS-1$
		}
		if (this.isCheck) {
			buffer.append('+');
		}
		return buffer.toString();
	}

	public String getMoveNotation(ResourceBundle bundle) {
		return getMoveNotation();
	}
	
	public void appendDetailed(StringBuffer buffer) {
		// nothing to do
	}
	
	public void appendSpecificEnd(StringBuffer buffer) {
		// nothing to do
	}

	public void appendDetailed(StringBuffer buffer, ResourceBundle bundle) {
		// nothing to do
	}

	public void appendSpecificEnd(StringBuffer buffer, ResourceBundle bundle) {
		// nothing to do
	}

}
