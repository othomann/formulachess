package org.formulachess.pgn.ast;

import java.util.HashMap;

public class TagSection extends ASTNode {

	public static final String TAG_SITE = "[Site"; //$NON-NLS-1$
	public static final String TAG_ROUND = "[Round"; //$NON-NLS-1$
	public static final String TAG_DATE = "[Date"; //$NON-NLS-1$
	public static final String TAG_PLYCOUNT = "[PlyCount"; //$NON-NLS-1$
	public static final String TAG_ECO = "[ECO"; //$NON-NLS-1$
	public static final String TAG_UNFINISHED = "*"; //$NON-NLS-1$
	public static final String TAG_RESULT = "[Result"; //$NON-NLS-1$
	public static final String TAG_BLACK = "[Black"; //$NON-NLS-1$
	public static final String TAG_BLACK_ELO = "[BlackElo"; //$NON-NLS-1$
	public static final String TAG_WHITE = "[White"; //$NON-NLS-1$
	public static final String TAG_WHITE_ELO = "[WhiteElo"; //$NON-NLS-1$
	public static final String TAG_EVENT = "[Event"; //$NON-NLS-1$

	private static final int INITIAL_SIZE = 7;
	private TagPair[] tagPairs = new TagPair[INITIAL_SIZE];
	private int tagPairCounter = 0;
	private HashMap<String, String> tags;

	public TagSection() {
		this.tags = new HashMap<>(INITIAL_SIZE);
	}

	public void addTagPair(TagPair tagPair) {
		if (this.tagPairCounter == this.tagPairs.length) {
			System.arraycopy(this.tagPairs, 0, this.tagPairs = new TagPair[this.tagPairCounter * 2], 0,
					this.tagPairCounter);
		}
		this.tagPairs[this.tagPairCounter++] = tagPair;
		this.tags.put(new String(tagPair.getName()).trim(), new String(tagPair.getValue()).trim());
	}

	public TagPair[] getTagPairs() {
		if (this.tagPairCounter != this.tagPairs.length) {
			System.arraycopy(this.tagPairs, 0, this.tagPairs = new TagPair[this.tagPairCounter], 0,
					this.tagPairCounter);
		}
		return this.tagPairs;
	}

	/**
	 * This method was created by a SmartGuide.
	 * 
	 * @param key
	 *            java.lang.String
	 * @return java.lang.String
	 */
	public String getTag(String key) {
		return this.tags.get(key);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0, max = this.tagPairCounter; i < max; i++) {
			builder.append(this.tagPairs[i]).append(LINE_SEPARATOR);
		}
		return String.valueOf(builder);
	}

}
