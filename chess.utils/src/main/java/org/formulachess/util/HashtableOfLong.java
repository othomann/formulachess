/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.formulachess.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Hashtable of {String --> long}
 */
public final class HashtableOfLong implements Iterable<String> {

	class Enumerator implements Iterator<String> {
		String entry;
		int index;
		String[] keys;

		public Enumerator(String[] keys) {
			this.keys = keys;
			this.index = keys.length;
		}

		@Override
		public boolean hasNext() {
			String currentEntry = this.entry;
			int i = this.index;
			String[] table = this.keys;
			while (currentEntry == null && i > 0) {
				currentEntry = table[--i];
			}
			this.entry = currentEntry;
			this.index = i;
			return currentEntry != null;
		}

		@Override
		public String next() {
			String currentEntry = this.entry;
			int i = this.index;
			String[] table = this.keys;
			while (currentEntry == null && i > 0) {
				currentEntry = table[--i];
			}
			this.entry = currentEntry;
			this.index = i;
			if (currentEntry != null) {
				this.entry = null;
				return currentEntry;
			}
			throw new NoSuchElementException();
		}
	}

	public static final long NO_VALUE = Long.MIN_VALUE;

	private int elementSize; // number of elements in the table

	// to avoid using Enumerations, walk the individual tables skipping nulls
	private String[] keyTable;
	private int threshold;
	private long[] valueTable;

	public HashtableOfLong() {
		this(13);
	}

	public HashtableOfLong(int size) {

		this.elementSize = 0;
		this.threshold = size; // size represents the expected number of elements
		int extraRoom = (int) (size * 1.75f);
		if (this.threshold == extraRoom) {
			extraRoom++;
		}
		this.keyTable = new String[extraRoom];
		this.valueTable = new long[extraRoom];
	}

	public HashtableOfLong(HashtableOfLong table) {
		this.elementSize = table.elementSize;
		this.threshold = table.threshold;

		int length = table.keyTable.length;
		this.keyTable = new String[length];
		System.arraycopy(table.keyTable, 0, this.keyTable, 0, length);

		length = table.valueTable.length;
		this.valueTable = new long[length];
		System.arraycopy(table.valueTable, 0, this.valueTable, 0, length);
	}

	public boolean containsKey(String key) {
		int index = getIndex(key);
		String currentKey;
		while ((currentKey = this.keyTable[index]) != null) {
			if (key.equals(currentKey))
				return true;
			index = (index + 1) % this.keyTable.length;
		}
		return false;
	}

	public long get(String key) {
		int index = getIndex(key);
		String currentKey;
		while ((currentKey = this.keyTable[index]) != null) {
			if (key.equals(currentKey))
				return this.valueTable[index];
			index = (index + 1) % this.keyTable.length;
		}
		return NO_VALUE;
	}

	@Override
	public Iterator<String> iterator() {
		return new Enumerator(this.keyTable);
	}

	public void put(String key, long value) {
		int index = getIndex(key);
		String currentKey;
		while ((currentKey = this.keyTable[index]) != null) {
			if (key.equals(currentKey)) {
				this.valueTable[index] = value;
				return;
			}
			index = (index + 1) % this.keyTable.length;
		}
		this.keyTable[index] = key;
		this.valueTable[index] = value;

		// assumes the threshold is never equal to the size of the table
		if (++this.elementSize > this.threshold)
			rehash();
	}

	private void rehash() {

		HashtableOfLong newHashtable = new HashtableOfLong(this.elementSize * 2); // double the number of expected
																					// elements
		String currentKey;
		for (int i = this.keyTable.length; --i >= 0;)
			if ((currentKey = this.keyTable[i]) != null)
				newHashtable.put(currentKey, this.valueTable[i]);

		this.keyTable = newHashtable.keyTable;
		this.valueTable = newHashtable.valueTable;
		this.threshold = newHashtable.threshold;
	}

	public long remove(String key) {
		int index = getIndex(key);
		String currentKey;
		while ((currentKey = this.keyTable[index]) != null) {
			if (key.equals(currentKey)) {
				long value = this.valueTable[index];
				this.elementSize--;
				this.keyTable[index] = null;
				this.valueTable[index] = NO_VALUE;
				rehash();
				return value;
			}
			index = (index + 1) % this.keyTable.length;
		}
		return NO_VALUE;
	}

	private int getIndex(String key) {
		int index = key.hashCode();
		if (index == Integer.MIN_VALUE) {
			return 0;
		}
		if (index < 0) {
			return -index % this.valueTable.length;
		}
		return index % this.valueTable.length;
	}

	public int size() {
		return this.elementSize;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		String key;
		for (int i = 0, length = this.valueTable.length; i < length; i++)
			if ((key = this.keyTable[i]) != null)
				builder.append(key).append(" -> ").append(this.valueTable[i]).append("\n"); //$NON-NLS-2$ //$NON-NLS-1$
		return String.valueOf(builder);
	}
}
