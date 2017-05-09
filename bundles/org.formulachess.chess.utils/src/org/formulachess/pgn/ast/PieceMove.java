package org.formulachess.pgn.ast;

import java.util.ResourceBundle;

public class PieceMove extends Move {

	private char pieceIdentification;

	public PieceMove() {
		this.isCapture = false;
		this.isCheck = false;
		this.isCheckMate = false;
		this.isWhiteMove = false;
	}

	/**
	 * Sets the pieceIdentification.
	 * 
	 * @param pieceIdentification
	 *            The pieceIdentification to set
	 */
	public void setPieceIdentification(char pieceIdentification) {
		this.pieceIdentification = pieceIdentification;
	}

	@Override
	public void appendDetailed(StringBuilder buffer, ResourceBundle bundle) {
		if (bundle == null) {
			buffer.append(this.pieceIdentification);
		} else {
			switch (this.pieceIdentification) {
				case 'B':
					buffer.append(bundle.getString("piece.bishop")); //$NON-NLS-1$
					break;
				case 'N':
					buffer.append(bundle.getString("piece.knight")); //$NON-NLS-1$
					break;
				case 'R':
					buffer.append(bundle.getString("piece.rook")); //$NON-NLS-1$
					break;
				case 'Q':
					buffer.append(bundle.getString("piece.queen")); //$NON-NLS-1$
					break;
				case 'K':
					buffer.append(bundle.getString("piece.king")); //$NON-NLS-1$
					break;
				default:
			}
		}
	}

	@Override
	public void appendSpecificEnd(StringBuilder buffer, ResourceBundle bundle) {
		// nothing to do
	}

}
