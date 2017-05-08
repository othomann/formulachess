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
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
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
	@Override
	public String getMoveNotation() {
		StringBuilder buffer = new StringBuilder();
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

	@Override
	public String getMoveNotation(ResourceBundle bundle) {
		return getMoveNotation();
	}

	@Override
	public void appendDetailed(StringBuilder buffer, ResourceBundle bundle) {
		// nothing to do
	}

	@Override
	public void appendSpecificEnd(StringBuilder buffer, ResourceBundle bundle) {
		// nothing to do
	}

}
