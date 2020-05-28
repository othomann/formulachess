package org.formulachess.engine;

public class MateMove implements Comparable<MateMove> {

	private static final int DEFAULT = 0x1;
	private static final int CAPTURE = 0x100;
	private static final int CHECK = 0x1000;
	private static final int CASTLE = 0x10;

	private long move;
	private int mobility;
	private String notation;

	public MateMove(long move, int mobility, String notation) {
		this.move = move;
		this.mobility = mobility;
		this.notation = notation;
	}

	public static boolean isCheck(long move) {
		return MoveConstants.isCheck(move);
	}

	public static boolean isCastle(long move) {
		return MoveConstants.isCastle(move);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(MateMove o) {
		MateMove otherMove = o;
		int category = category(this);
		int otherCategory = category(otherMove);

		if (this.mobility == 0) {
			if (otherMove.mobility == 0) {
				return category - otherCategory;
			}
			return -1;
		} else if (otherMove.mobility == 0) {
			return 1;
		}

		if (this.mobility != otherMove.mobility) {
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
		int value = DEFAULT;
		if (MateMove.isCapture(moveValue)) {
			value |= CAPTURE;
		}
		if (MateMove.isCheck(moveValue)) {
			value |= CHECK;
		}
		if (MateMove.isCastle(moveValue)) {
			value |= CASTLE;
		}
		return value;
	}

	private static boolean isCapture(long moveValue) {
		return MoveConstants.getCaptureValue(moveValue) != Piece.EMPTY.getValue();
	}

	@Override
	public String toString() {
		return this.notation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + mobility;
		result = prime * result + (int) (move ^ (move >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MateMove other = (MateMove) obj;
		if (mobility != other.mobility)
			return false;
		return move == other.move;
	}

	public int getMobility() {
		return this.mobility;
	}

	public String getNotation() {
		return this.notation;
	}

	public long getMove() {
		return this.move;
	}
}
