package org.formulachess.engine;

public class MoveKey implements Comparable<MoveKey> {

	MateMove move;
	int depth;

	public MoveKey(MateMove move, int depth) {
		this.move = move;
		this.depth = depth;
	}

	public int compareTo(MoveKey o) {
		return this.depth - o.depth;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MoveKey) {
			MoveKey other = (MoveKey) obj;
			return this.move.equals(other.move) && this.depth == other.depth;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.move.hashCode();
	}

	@Override
	public String toString() {
		return this.move.toString() + "(" + this.depth + ")"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
