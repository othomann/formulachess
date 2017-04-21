package org.formulachess.pgn.ast;

import java.util.ResourceBundle;

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
	 * @param isPromotion The isPromotion to set
	 */
	public void setIsPromotion(boolean isPromotion) {
		this.isPromotion = isPromotion;
	}

	/**
	 * Sets the promotedPiece.
	 * @param promotedPiece The promotedPiece to set
	 */
	public void setPromotedPiece(char promotedPiece) {
		this.promotedPiece = promotedPiece;
	}
	
	public void appendDetailed(StringBuffer buffer) {
		// nothing to do
	}

	public void appendSpecificEnd(StringBuffer buffer) {
		if (this.isPromotion) {
			buffer
				.append('=')
				.append(this.promotedPiece);
		}
	}

	public void appendDetailed(StringBuffer buffer, ResourceBundle bundle) {
		// nothing to do
	}

	public void appendSpecificEnd(StringBuffer buffer, ResourceBundle bundle) {
		if (this.isPromotion) {
			buffer.append('=');
			switch(this.promotedPiece) {
				case 'B' :
					buffer.append(bundle.getString("piece.bishop")); //$NON-NLS-1$
					break;
				case 'N' :
					buffer.append(bundle.getString("piece.knight")); //$NON-NLS-1$
					break;
				case 'R' :
					buffer.append(bundle.getString("piece.rook")); //$NON-NLS-1$
					break;
				case 'Q' :
					buffer.append(bundle.getString("piece.queen")); //$NON-NLS-1$
					break;
			}
		}
	}

}
