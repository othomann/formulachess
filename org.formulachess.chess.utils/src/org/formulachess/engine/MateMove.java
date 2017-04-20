package org.formulachess.engine;

public class MateMove implements Comparable<MateMove> {

	private static final int DEFAULT = 0x01;
	private static final int CAPTURE = 0x100;
	private static final int CHECK = 0x10;
	
	public long move;
	public int mobility;
	public String notation;

	public MateMove(long move, int mobility, String notation) {
		this.move = move;
		this.mobility = mobility;
		this.notation = notation;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(MateMove o) {
		MateMove otherMove = o;
		
		if (this.mobility == 0) {
			return -1;
		}
		if (otherMove.mobility == 0) {
			return 1;
		}
		int category = category(this);
		int otherCategory = category(otherMove);
		
		if (category == otherCategory) {
			return this.mobility - otherMove.mobility;
		}
		return category - otherCategory;
	}

	/**
	 * @param moveArg
	 * @return
	 */
	private int category(MateMove moveArg) {
		long moveValue = moveArg.move;
		if (((moveValue & MoveConstants.CAPTURE_PIECE_MASK) >> MoveConstants.CAPTURE_PIECE_SHIFT) != 0) {
			return CAPTURE;
		}
		if (((moveValue & MoveConstants.CHECK_MASK) >> MoveConstants.CHECK_SHIFT) != 0) {
			return CHECK;
		}
		return DEFAULT;
	}

	public String toString() {
		return this.notation; // + " mobility = " + this.mobility;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof MateMove)) {
			return false;
		}
		MateMove otherMove = (MateMove) obj;
		return this.mobility == otherMove.mobility && this.move == otherMove.move;
	}

	public int hashCode() {
		return (int) this.move + this.mobility;
	}
}

