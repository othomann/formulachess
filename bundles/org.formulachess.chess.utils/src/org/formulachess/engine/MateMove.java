package org.formulachess.engine;

public class MateMove implements Comparable<MateMove> {

	private static final int DEFAULT = 0x1;
	private static final int CAPTURE = 0x1000;
	private static final int CHECK = 0x100;
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

		if (category == otherCategory) {
			return otherMove.mobility - this.mobility ;
		}
		return category - otherCategory;
	}

	/**
	 * @param moveArg
	 * @return
	 */
	private int category(MateMove moveArg) {
		long moveValue = moveArg.move;
		if (MateMove.isCapture(moveValue)) {
			return CAPTURE;
		}
		if (MateMove.isCheck(moveValue)) {
			return CHECK;
		}
		if (MateMove.isCastle(moveValue)) {
			return CASTLE;
		}
		return DEFAULT;
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
		if (move != other.move)
			return false;
		return true;
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
