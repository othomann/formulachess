package org.formulachess.pgn.ast;

import java.util.ResourceBundle;

import org.formulachess.pgn.ASTVisitor;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (isKingSize ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Castle))
			return false;
		Castle other = (Castle) obj;
		if (isKingSize != other.isKingSize)
			return false;
		return super.equals(obj);
	}

	@Override
	public void accept(ASTVisitor visitor) {
		visitor.visit(this);
		visitor.endVisit(this);
	}
}
