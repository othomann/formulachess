package org.formulachess.pgn.ast;

import org.formulachess.engine.Messages;

public class PawnMove extends Move {

	private boolean isPromotion;
	private char promotedPiece;

	public PawnMove() {
		this.isCapture = false;
		this.isCheck = false;
		this.isCheckMate = false;
		this.isPromotion = false;
		this.isWhiteMove = false;
	}

	/**
	 * Sets the isPromotion.
	 * 
	 * @param isPromotion
	 *            The isPromotion to set
	 */
	public void setIsPromotion(boolean isPromotion) {
		this.isPromotion = isPromotion;
	}

	/**
	 * Sets the promotedPiece.
	 * 
	 * @param promotedPiece
	 *            The promotedPiece to set
	 */
	public void setPromotedPiece(char promotedPiece) {
		this.promotedPiece = promotedPiece;
	}

	@Override
	public void appendSpecificEnd(StringBuilder buffer, Messages messages) {
		if (this.isPromotion) {
			buffer.append('=');
			switch (this.promotedPiece) {
				case 'B':
					buffer.append(messages.getString("piece.bishop")); //$NON-NLS-1$
					break;
				case 'N':
					buffer.append(messages.getString("piece.knight")); //$NON-NLS-1$
					break;
				case 'R':
					buffer.append(messages.getString("piece.rook")); //$NON-NLS-1$
					break;
				case 'Q':
					buffer.append(messages.getString("piece.queen")); //$NON-NLS-1$
					break;
				default:
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (isPromotion ? 1231 : 1237);
		result = prime * result + promotedPiece;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof PawnMove))
			return false;
		PawnMove other = (PawnMove) obj;
		if (isPromotion != other.isPromotion)
			return false;
		if (promotedPiece != other.promotedPiece)
			return false;
		return super.equals(obj);
	}

}
