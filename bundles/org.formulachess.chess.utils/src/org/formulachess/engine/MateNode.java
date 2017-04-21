package org.formulachess.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.formulachess.util.Util;

public class MateNode implements Comparable<MateNode> {
	
	static Comparator<MateNode> MateNodeComparator = new Comparator<MateNode>() {
		public int compare(MateNode o1, MateNode o2) {
			return o1.compareTo(o2);
		}
	};
	
	public static MateNode newRoot(int color) {
		return new MateNode(null, null, color, 0);
	}
	
	ArrayList<MateNode> children;
	int color;
	int depth;
	MateMove move;
	MateNode parent;
	
	public MateNode(MateNode parent, MateMove move, int color, int depth) {
		this.parent = parent;
		this.move = move;
		this.depth = depth;
		this.color = color;
	}
	
	public MateNode add(MateMove moveArg, int colorArg, int depthArg) {
		if (this.children == null) {
			this.children = new ArrayList<MateNode>();
		}
		MateNode newNode = new MateNode(this, moveArg, colorArg, depthArg);
		this.children.add(newNode);
		newNode.setDepth(depthArg);
		Collections.sort(this.children, MateNodeComparator);
		return newNode;
	}

	public int compareTo(MateNode o) {
		return o.depth - this.depth;
	}
	
	public boolean contains(MateMove _move, int _depth) {
		if (this.children == null) {
			return false;
		}
		return this.children.contains(new MateNode(this, _move, 0, _depth));
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof MateNode)) return false;
		return this.move.equals(((MateNode) obj).move);
	}
	
	private void generarePGNForSingleNode(int number, int rootColor, boolean detailFirst, StringBuffer buffer) {
		if (this.move != null) {
			if (rootColor == BoardConstants.WHITE_TURN) {
				final int moveNumber = (number / 2) + 1;
				if (!detailFirst) {
					buffer.append(" "); //$NON-NLS-1$
				}
				if (this.color == BoardConstants.WHITE_TURN) {
					buffer.append(moveNumber + ". "); //$NON-NLS-1$ 
				} else if (detailFirst) {
					buffer.append(moveNumber + "..."); //$NON-NLS-1$ 
				}
			} else {
				final int moveNumber = ((number + 1) / 2) + 1;
				if (!detailFirst) {
					buffer.append(" "); //$NON-NLS-1$
				}
				if (this.color == BoardConstants.WHITE_TURN) {
					buffer.append(moveNumber + ". "); //$NON-NLS-1$ 
				} else if (detailFirst) {
					buffer.append(moveNumber + "..."); //$NON-NLS-1$ 
				}
			}
			buffer.append(this.move.notation);
			if (((this.move.move & MoveConstants.CHECK_MASK) >> MoveConstants.CHECK_SHIFT) != 0)  {
				if (this.children == null) {
					buffer.append('#');
				} else {
					buffer.append('+');
				}
			}
		}
	}	
	
	public String generatePGN() {
		StringBuffer buffer = new StringBuffer();
		generatePGN(-1, this.color, true, buffer);
		return buffer.toString();
	}
	
	public void generatePGN(int number, int rootColor, boolean detailFirst, StringBuffer buffer) {
		ArrayList<MateNode> siblings = this.parent != null ? this.parent.children : this.children;
		boolean hasSibling = siblings.size() != 1;
		if (hasSibling) {
			generarePGNForSingleNode(number, rootColor, detailFirst, buffer);
			generatePGNForSiblings(siblings, this, number, rootColor, buffer);
			if (siblings != this.children) {
				generatePGNForChildren(this.children, number + 1, rootColor, true, buffer);
			}
		} else if (this.isRoot()) {
			if (this.children != null && this.children.size() != 0) {
				this.children.get(0).generatePGN(number + 1, rootColor, true, buffer);
			}
		} else {
			generarePGNForSingleNode(number, rootColor, detailFirst, buffer);
			if (this.children != null && this.children.size() != 0) {
				this.children.get(0).generatePGN(number + 1, rootColor, false, buffer);
			}
		}
	}

	public void generatePGNForChildren(ArrayList<MateNode> subNodes, int number, int rootColor, boolean detailFirst, StringBuffer buffer) {
		if (subNodes != null) {
			for (int i = 0, max = subNodes.size(); i < max; i++) {
				MateNode nextNode = subNodes.get(i);
				nextNode.generatePGN(number, rootColor, detailFirst, buffer);
			}
		}
	}

	public void generatePGNForSiblings(ArrayList<MateNode> subNodes, MateNode currentNode, int number, int rootColor, StringBuffer buffer) {
		if (subNodes != null) {
			int i = 0;
			MateNode nextNode = subNodes.get(i);
			while(!nextNode.equals(currentNode)) {
				i++;
				nextNode = subNodes.get(i);
			}
			i++;
			buffer
				.append(Util.LINE_SEPARATOR);
			for (int max = subNodes.size(); i < max; i++) {
				nextNode =subNodes.get(i);
				buffer.append('(');
				nextNode.printSingleNode(number, rootColor, true, buffer);
				if (nextNode.children.size() != 0) {
					for (int j = 0, max2 = nextNode.children.size(); j < max2; j++) {
						nextNode.children.get(j).generatePGN(number + 1, rootColor, false, buffer);
					}
				}
				buffer
					.append(')')
					.append(Util.LINE_SEPARATOR);
			}
		}
	}

	public int hashCode() {
		return this.move.hashCode();
	}
	
	public boolean isRoot() {
		return this.parent == null;
	}

	private void printSingleNode(int number, int rootColor, boolean detailFirst, StringBuffer buffer) {
		if (this.move != null) {
			if (rootColor == BoardConstants.WHITE_TURN) {
				final int moveNumber = (number / 2) + 1;
				if (!detailFirst) {
					buffer.append(" "); //$NON-NLS-1$
				}
				if (this.color == BoardConstants.WHITE_TURN) {
					buffer.append(moveNumber + ". "); //$NON-NLS-1$
				} else if (detailFirst) {
					buffer.append(moveNumber + "..."); //$NON-NLS-1$ 
				}
			} else {
				final int moveNumber = ((number + 1) / 2) + 1;
				if (!detailFirst) {
					buffer.append(" "); //$NON-NLS-1$
				}
				if (this.color == BoardConstants.WHITE_TURN) {
					buffer.append(moveNumber + ". "); //$NON-NLS-1$ 
				} else if (detailFirst) {
					buffer.append(moveNumber + "..."); //$NON-NLS-1$
				}
			}
			buffer.append(this.move.notation);
			if (((this.move.move & MoveConstants.CHECK_MASK) >> MoveConstants.CHECK_SHIFT) != 0)  {
				if (this.children == null) {
					buffer.append('#');
				} else {
					buffer.append('+');
				}
			}
		}
	}

	public Object remove(MateMove _move, int _depth) {
		if (this.children == null) {
			return null;
		}
		int index = this.children.indexOf(new MateNode(this, _move, 0, _depth));
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
		while(node != null) {
			node.depth = depth;
			Collections.sort(node.children, MateNodeComparator);
			node = node.parent;
		}
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		printSingleNode(1, this.color, true, buffer);
		return buffer.toString();
	}

}
