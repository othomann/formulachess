package org.formulachess.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.formulachess.util.Util;
import static org.formulachess.engine.Turn.*;

public class MateNode implements Comparable<MateNode> {

	static Comparator<MateNode> mateNodeComparator = (o1, o2) -> o1.compareTo(o2);

	public static MateNode newRoot(Turn turn) {
		return new MateNode(null, null, turn, 0);
	}

	ArrayList<MateNode> children;
	Turn turn;
	int depth;
	MateMove move;
	MateNode parent;

	public MateNode(MateNode parent, MateMove move, Turn turn, int depth) {
		this.parent = parent;
		this.move = move;
		this.depth = depth;
		this.turn = turn;
	}

	public MateNode add(MateMove moveArg, Turn turn, int depthArg) {
		if (this.children == null) {
			this.children = new ArrayList<MateNode>();
		}
		MateNode newNode = new MateNode(this, moveArg, turn, depthArg);
		this.children.add(newNode);
		newNode.setDepth(depthArg);
		Collections.sort(this.children, mateNodeComparator);
		return newNode;
	}

	@Override
	public int compareTo(MateNode o) {
		return o.depth - this.depth;
	}

	public boolean contains(MateMove move, int depth) {
		if (this.children == null) {
			return false;
		}
		return this.children.contains(new MateNode(this, move, WHITE_TURN, depth));
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MateNode))
			return false;
		return this.move.equals(((MateNode) obj).move);
	}

	private void generarePGNForSingleNode(int number, Turn rootTurn, boolean detailFirst, StringBuilder buffer) {
		if (this.move != null) {
			if (rootTurn == WHITE_TURN) {
				final int moveNumber = (number / 2) + 1;
				if (!detailFirst) {
					buffer.append(" "); //$NON-NLS-1$
				}
				if (this.turn == WHITE_TURN) {
					buffer.append(moveNumber + ". "); //$NON-NLS-1$
				} else if (detailFirst) {
					buffer.append(moveNumber + "..."); //$NON-NLS-1$
				}
			} else {
				final int moveNumber = ((number + 1) / 2) + 1;
				if (!detailFirst) {
					buffer.append(" "); //$NON-NLS-1$
				}
				if (this.turn == WHITE_TURN) {
					buffer.append(moveNumber + ". "); //$NON-NLS-1$
				} else if (detailFirst) {
					buffer.append(moveNumber + "..."); //$NON-NLS-1$
				}
			}
			buffer.append(this.move.getNotation());
			if (MateMove.isCheck(this.move.getMove())) {
				if (this.children == null) {
					buffer.append('#');
				} else {
					buffer.append('+');
				}
			}
		}
	}

	public String generatePGN() {
		StringBuilder buffer = new StringBuilder();
		generatePGN(-1, this.turn, true, buffer);
		return String.valueOf(buffer);
	}

	public void generatePGN(int number, Turn rootTurn, boolean detailFirst, StringBuilder buffer) {
		ArrayList<MateNode> siblings = this.parent != null ? this.parent.children : this.children;
		boolean hasSibling = siblings.size() != 1;
		if (hasSibling) {
			generarePGNForSingleNode(number, rootTurn, detailFirst, buffer);
			generatePGNForSiblings(siblings, this, number, rootTurn, buffer);
			if (siblings != this.children) {
				generatePGNForChildren(this.children, number + 1, rootTurn, true, buffer);
			}
		} else if (this.isRoot()) {
			if (this.children != null && !this.children.isEmpty()) {
				this.children.get(0).generatePGN(number + 1, rootTurn, true, buffer);
			}
		} else {
			generarePGNForSingleNode(number, rootTurn, detailFirst, buffer);
			if (this.children != null && !this.children.isEmpty()) {
				this.children.get(0).generatePGN(number + 1, rootTurn, false, buffer);
			}
		}
	}

	public void generatePGNForChildren(List<MateNode> subNodes, int number, Turn rootTurn, boolean detailFirst,
			StringBuilder buffer) {
		if (subNodes != null) {
			for (int i = 0, max = subNodes.size(); i < max; i++) {
				MateNode nextNode = subNodes.get(i);
				nextNode.generatePGN(number, rootTurn, detailFirst, buffer);
			}
		}
	}

	public void generatePGNForSiblings(List<MateNode> subNodes, MateNode currentNode, int number, Turn rootTurn,
			StringBuilder buffer) {
		if (subNodes != null) {
			int i = 0;
			MateNode nextNode = subNodes.get(i);
			while (!nextNode.equals(currentNode)) {
				i++;
				nextNode = subNodes.get(i);
			}
			i++;
			buffer.append(Util.LINE_SEPARATOR);
			for (int max = subNodes.size(); i < max; i++) {
				nextNode = subNodes.get(i);
				buffer.append('(');
				nextNode.printSingleNode(number, rootTurn, true, buffer);
				if (!nextNode.children.isEmpty()) {
					for (int j = 0, max2 = nextNode.children.size(); j < max2; j++) {
						nextNode.children.get(j).generatePGN(number + 1, rootTurn, false, buffer);
					}
				}
				buffer.append(')').append(Util.LINE_SEPARATOR);
			}
		}
	}

	@Override
	public int hashCode() {
		return this.move.hashCode();
	}

	public boolean isRoot() {
		return this.parent == null;
	}

	private void printSingleNode(int number, Turn rootTurn, boolean detailFirst, StringBuilder buffer) {
		if (this.move != null) {
			if (rootTurn == WHITE_TURN) {
				final int moveNumber = (number / 2) + 1;
				if (!detailFirst) {
					buffer.append(" "); //$NON-NLS-1$
				}
				if (this.turn == WHITE_TURN) {
					buffer.append(moveNumber + ". "); //$NON-NLS-1$
				} else if (detailFirst) {
					buffer.append(moveNumber + "..."); //$NON-NLS-1$
				}
			} else {
				final int moveNumber = ((number + 1) / 2) + 1;
				if (!detailFirst) {
					buffer.append(" "); //$NON-NLS-1$
				}
				if (this.turn == WHITE_TURN) {
					buffer.append(moveNumber + ". "); //$NON-NLS-1$
				} else if (detailFirst) {
					buffer.append(moveNumber + "..."); //$NON-NLS-1$
				}
			}
			buffer.append(this.move.getNotation());
			if (MateMove.isCheck(this.move.getMove())) {
				if (this.children == null) {
					buffer.append('#');
				} else {
					buffer.append('+');
				}
			}
		}
	}

	public Object remove(MateMove move, int depth) {
		if (this.children == null) {
			return null;
		}
		int index = this.children.indexOf(new MateNode(this, move, WHITE_TURN, depth));
		if (index == -1) {
			return null;
		}
		return this.children.remove(index);
	}

	/**
	 * @param depth
	 */
	private void setDepth(int depth) {
		MateNode node = this.parent;
		while (node != null) {
			node.depth = depth;
			Collections.sort(node.children, mateNodeComparator);
			node = node.parent;
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		printSingleNode(1, this.turn, true, buffer);
		return String.valueOf(buffer);
	}

}
