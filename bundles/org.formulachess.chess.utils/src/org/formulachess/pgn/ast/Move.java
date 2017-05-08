package org.formulachess.pgn.ast;

import java.util.ArrayList;
import java.util.ResourceBundle;

public abstract class Move extends ASTNode {
	private static final Variation[] EMPTY_VARIATIONS = new Variation[0];
	
	protected char startingFile;
	protected char endingFile;
	protected char startingRank;
	protected char endingRank;
	protected Variation[] variations;
	protected int variationsCounter;
	protected char[] nag;
	protected boolean isWhiteMove;
	protected Comment comment;
	protected int moveIndication;
	protected boolean isCheck;
	protected boolean isCheckMate;
	protected boolean isCapture;
	
	public void setIsWhiteMove(boolean isWhiteMove) {
		this.isWhiteMove = isWhiteMove;
	}
	
	public void setComment(Comment comment) {
		this.comment = comment;
	}
	
	public void setMoveIndication(int moveIndication) {
		this.moveIndication = moveIndication;
	}
	/**
	 * Sets the isCheck.
	 * @param isCheck The isCheck to set
	 */
	public void setIsCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	/**
	 * Sets the isCheckMate.
	 * @param isCheckMate The isCheckMate to set
	 */
	public void setIsCheckMate(boolean isCheckMate) {
		this.isCheckMate = isCheckMate;
	}

	/**
	 * Sets the endingFile.
	 * @param endingFile The endingFile to set
	 */
	public void setEndingFile(char endingFile) {
		this.endingFile = endingFile;
	}

	/**
	 * Sets the endingRank.
	 * @param endingRank The endingRank to set
	 */
	public void setEndingRank(char endingRank) {
		this.endingRank = endingRank;
	}

	/**
	 * Sets the startingFile.
	 * @param startingFile The startingFile to set
	 */
	public void setStartingFile(char startingFile) {
		this.startingFile = startingFile;
	}

	/**
	 * Sets the startingRank.
	 * @param startingRank The startingRank to set
	 */
	public void setStartingRank(char startingRank) {
		this.startingRank = startingRank;
	}

	/**
	 * Sets the isCapture.
	 * @param isCapture The isCapture to set
	 */
	public void setIsCapture(boolean isCapture) {
		this.isCapture = isCapture;
	}

	/**
	 * Returns the isWhiteMove.
	 * @return boolean
	 */
	public boolean isWhiteMove() {
		return this.isWhiteMove;
	}

	/**
	 * @see formulachess.pgn.ast.MateMove#getMoveNotation()
	 */
	public String getMoveNotation() {
		StringBuilder buffer = new StringBuilder();
		appendDetailed(buffer);
		if (this.startingFile != 0) {
			buffer.append(this.startingFile);	
		}
		if (this.startingRank != 0) {
			buffer.append(this.startingRank);
		}
		if (this.isCapture) {
			buffer.append('x');
		}
		if (this.endingFile != 0) {
			buffer.append(this.endingFile);
		}
		if (this.endingRank != 0) {
			buffer.append(this.endingRank);
		}
		appendSpecificEnd(buffer);
		if (this.isCheck) {
			buffer.append('+');
		}
		return buffer.toString();
	}
	
	/**
	 * @see formulachess.pgn.ast.MateMove#getMoveNotation()
	 */
	public String getMoveNotation(ResourceBundle bundle) {
		StringBuilder buffer = new StringBuilder();
		appendDetailed(buffer, bundle);
		if (this.startingFile != 0) {
			buffer.append(this.startingFile);	
		}
		if (this.startingRank != 0) {
			buffer.append(this.startingRank);
		}
		if (this.isCapture) {
			buffer.append('x');
		}
		if (this.endingFile != 0) {
			buffer.append(this.endingFile);
		}
		if (this.endingRank != 0) {
			buffer.append(this.endingRank);
		}
		appendSpecificEnd(buffer, bundle);
		if (this.isCheck) {
			buffer.append('+');
		}
		return buffer.toString();
	}

	abstract void appendSpecificEnd(StringBuilder buffer);

	public abstract void appendDetailed(StringBuilder buffer);

	abstract void appendSpecificEnd(StringBuilder buffer, ResourceBundle bundle);

	public abstract void appendDetailed(StringBuilder buffer, ResourceBundle bundle);
	
	public void addVariation(Variation variation) {
		if (this.variations == null) {
			this.variations = new Variation[2];
		}
		if (this.variations.length == this.variationsCounter) {
			System.arraycopy(this.variations, 0, (this.variations = new Variation[this.variationsCounter * 2]), 0, this.variationsCounter);
		}
		this.variations[this.variationsCounter++] = variation;
		Move[] moves = variation.getMoves();
		if (moves.length >= 1) {
			moves[0].setParent(this);
		}
	}
	
	public Variation[] getVariations() {
		if (this.variations == null) {
			return EMPTY_VARIATIONS;
		}
		if (this.variations.length != this.variationsCounter) {
			System.arraycopy(this.variations, 0, (this.variations = new Variation[this.variationsCounter]), 0, this.variationsCounter);
		}
		return this.variations;
	}
	

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		if (this.isWhiteMove) {
			buffer.append(this.moveIndication).append('.').append(' ');
		}
		appendDetailed(buffer);
		if (this.startingFile != 0) {
			buffer.append(this.startingFile);	
		}
		if (this.startingRank != 0) {
			buffer.append(this.startingRank);
		}
		if (this.isCapture) {
			buffer.append('x');
		}
		if (this.endingFile != 0) {
			buffer.append(this.endingFile);
		}
		if (this.endingRank != 0) {
			buffer.append(this.endingRank);
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
		return String.valueOf(buffer);
	}	
	/**
	 * Returns the moveIndication.
	 * @return int
	 */
	public int getMoveIndication() {
		return this.moveIndication;
	}

	/**
	 * Returns the nag.
	 * @return char[]
	 */
	public char[] getNag() {
		return this.nag;
	}

	/**
	 * Sets the nag.
	 * @param nag The nag to set
	 */
	public void setNag(char[] nag) {
		this.nag = nag;
	}

	public boolean hasVariations() {
		return this.variationsCounter != 0;
	}
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Move)) {
			return false;
		}
		Move move = (Move) obj;
		return this.getMoveNotation().equals(move.getMoveNotation()) && this.getMoveIndication() == move.getMoveIndication();
	}

	@Override
	public int hashCode() {
		return this.getMoveNotation().hashCode();
	}
	
	public Move[] pathFromRoot() {
		ArrayList<ASTNode> collection = new ArrayList<ASTNode>(this.getMoveIndication() * 2);
		collection.add(this);
		ASTNode parentNode = this.getParent();
		while  (parentNode != null) {
			if (((Move) parentNode).hasVariations() && variationsContainsLastMove(((Move) parentNode).getVariations(), (Move) collection.get(0))) {
				parentNode = parentNode.getParent();
				if (parentNode == null) {
					break;
				}
			}
			collection.add(0, parentNode);
			parentNode = parentNode.getParent();
		}
		Move[] result = new Move[collection.size()];
		collection.toArray(result);
		return result; 
	}
	
	private boolean variationsContainsLastMove(Variation[] variationsArg, Move firstMove) {
		for (int i = 0, max = variationsArg.length; i < max; i++) {
			Variation variation = variationsArg[i];
			Move variationFirstMove = variation.getMove(0);
			if (variationFirstMove != null && variationFirstMove.equals(firstMove)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isVariationFirstMove() {
		ASTNode parentNode = this.getParent();
		if (parentNode == null) {
			return false;
		}
		Variation[] currentVariations = ((Move)parentNode).getVariations();
		if (currentVariations.length == 0) {
			return false;
		}
		for (int i = 0, max = currentVariations.length; i < max; i++) {
			Move firstMove = currentVariations[i].getMove(0);
			if (firstMove != null && firstMove.equals(this)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Returns the comment.
	 * @return Comment
	 */
	public Comment getComment() {
		return this.comment;
	}

}
