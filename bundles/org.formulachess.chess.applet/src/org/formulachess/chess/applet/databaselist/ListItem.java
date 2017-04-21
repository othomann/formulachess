package org.formulachess.chess.applet.databaselist;

import org.formulachess.chess.applet.util.Comparable;

class ListItem implements Comparable {

	public static final int NONE = 0;
	public static final int ROUND = 1;
	public static final int WHITE_NAME = 2;
	public static final int RESULT = 4;
	public static final int ECO = 8;
	public static final int BLACK_NAME = 16;	
	public static final int PLY_MOVE = 32;
	public static final int EVENT = 64;
	public static final int[] CODES = new int[] {NONE, ECO, WHITE_NAME, BLACK_NAME, ROUND, EVENT, RESULT};
	private int criteria;
	private String round;
	private String whiteName;
	private String blackName;
	private String eco;
	private String result;
	private int gameIndex;
	private int plyMove;
	private String event;
	
	ListItem(int gameIndex, String round, String whiteName, String blackName, String eco, String result, int plyMove, String event) {
		this.result = result;
		this.round = round;
		this.eco = eco;
		this.whiteName = whiteName;
		this.blackName = blackName;
		this.criteria = ROUND;
		this.gameIndex = gameIndex;
		this.plyMove = plyMove;
		this.event = event;
	}
	/**
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(Object o) {
		if (!(o instanceof ListItem)) {
			return -1;
		}
		ListItem listItem = (ListItem) o;
		switch(this.criteria) {
			case ROUND :
				return this.round.compareTo(listItem.getRound());
			case EVENT :
			 	return this.event.compareTo(listItem.getEvent());
			case ECO :
				return this.eco.compareTo(listItem.getEco());
			case WHITE_NAME :
				return this.whiteName.compareTo(listItem.getWhiteName());
			case BLACK_NAME :
				return this.blackName.compareTo(listItem.getBlackName());
			case RESULT :
				return this.result.compareTo(listItem.getResult());
			case PLY_MOVE :
				if (this.plyMove < listItem.getPlyMove()) {
					return -1;
				} else if (this.plyMove > listItem.getPlyMove()) {
					return 1;
				}
				return 0;
			case NONE :
				if (this.gameIndex < listItem.getGameIndex()) {
					return -1;
				} else if (this.gameIndex > listItem.getGameIndex()) {
					return 1;
				}
				return 0;
		}
		return 0;
	}

	/**
	 * Returns the round.
	 * @return String
	 */
	public String getRound() {
		return this.round;
	}

	/**
	 * Returns the eco.
	 * @return String
	 */
	public String getEco() {
		return this.eco;
	}

	/**
	 * Returns the result.
	 * @return String
	 */
	public String getResult() {
		return this.result;
	}

	/**
	 * Sets the criteria.
	 * @param criteria The criteria to set
	 */
	public void setCriteria(int criteria) {
		this.criteria = criteria;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer
			.append("(") //$NON-NLS-1$
			.append(this.round)
			.append(") ") //$NON-NLS-1$
			.append(this.whiteName)
			.append(" - ") //$NON-NLS-1$
			.append(this.blackName)
			.append(", ") //$NON-NLS-1$
			.append(this.result);
		if (this.eco != null) {
			buffer.append(", "); //$NON-NLS-1$
			buffer.append(this.eco);
		}
		buffer
			.append(" (") //$NON-NLS-1$
			.append(this.plyMove)
			.append(")"); //$NON-NLS-1$
		return buffer.toString();
	}

	/**
	 * Returns the gameIndex.
	 * @return int
	 */
	public int getGameIndex() {
		return this.gameIndex;
	}

	/**
	 * Returns the blackName.
	 * @return String
	 */
	public String getBlackName() {
		return this.blackName;
	}

	/**
	 * Returns the whiteName.
	 * @return String
	 */
	public String getWhiteName() {
		return this.whiteName;
	}

	/**
	 * Returns the plyMove.
	 * @return int
	 */
	public int getPlyMove() {
		return this.plyMove;
	}

	/**
	 * Returns the event.
	 * @return String
	 */
	public String getEvent() {
		return this.event;
	}

}
