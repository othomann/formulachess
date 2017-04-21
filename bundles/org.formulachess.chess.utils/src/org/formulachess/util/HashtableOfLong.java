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

import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * Hashtable of {String --> long}
 */
public final class HashtableOfLong implements Cloneable {
	
	class Enumerator implements Enumeration<String> {
		String entry;
		int index;
		String[] keys;
		
		public Enumerator(String[] keys) {
			this.keys = keys;
			this.index = keys.length;
		}
		public boolean hasMoreElements() {
			String _entry = this.entry;
			int i = this.index;
			String[] table = this.keys;
			while (_entry == null && i > 0) {
				_entry = table[--i];
			}
			this.entry = _entry;
			this.index = i;
			return _entry != null;
		}
		public String nextElement() {
			String _entry = this.entry;
			int i = this.index;
			String[] table = this.keys;
			while (_entry == null && i > 0) {
				_entry = table[--i];
			}
			this.entry = _entry;
			this.index = i;
			if (_entry != null) {
				this.entry = null;
				return _entry;
			}
			throw new NoSuchElementException();
		}
	}
	
	public static final long NO_VALUE = Long.MIN_VALUE;

	public int elementSize; // number of elements in the table
	
	// to avoid using Enumerations, walk the individual tables skipping nulls
	public String[] keyTable;
	int threshold;
	public long[] valueTable;

	public HashtableOfLong() {
		this(13);
	}

	public HashtableOfLong(int size) {

		this.elementSize = 0;
		this.threshold = size; // size represents the expected number of elements
		int extraRoom = (int) (size * 1.75f);
		if (this.threshold == extraRoom)
			extraRoom++;
		this.keyTable = new String[extraRoom];
		this.valueTable = new long[extraRoom];
	}

	public Object clone() throws CloneNotSupportedException {
		HashtableOfLong result = (HashtableOfLong) super.clone();
		result.elementSize = this.elementSize;
		result.threshold = this.threshold;

		int length = this.keyTable.length;
		result.keyTable = new String[length];
		System.arraycopy(this.keyTable, 0, result.keyTable, 0, length);

		length = this.valueTable.length;
		result.valueTable = new long[length];
		System.arraycopy(this.valueTable, 0, result.valueTable, 0, length);
		return result;
	}

	public boolean containsKey(String key) {

		int index = Math.abs(key.hashCode()) % this.valueTable.length;
		String currentKey;
		while ((currentKey = this.keyTable[index]) != null) {
			if (key.equals(currentKey))
				return true;
			index = (index + 1) % this.keyTable.length;
		}
		return false;
	}

	public long get(String key) {

		int index = Math.abs(key.hashCode()) % this.valueTable.length;
		String currentKey;
		while ((currentKey = this.keyTable[index]) != null) {
			if (key.equals(currentKey))
				return this.valueTable[index];
			index = (index + 1) % this.keyTable.length;
		}
		return NO_VALUE;
	}
	public Enumeration<String> keys() {
		return new Enumerator(this.keyTable);
	}

	public long put(String key, long value) {

		int index = Math.abs(key.hashCode()) % this.valueTable.length;
		String currentKey;
		while ((currentKey = this.keyTable[index]) != null) {
			if (key.equals(currentKey))
				return this.valueTable[index] = value;
			index = (index + 1) % this.keyTable.length;
		}
		this.keyTable[index] = key;
		this.valueTable[index] = value;

		// assumes the threshold is never equal to the size of the table
		if (++this.elementSize > this.threshold)
			rehash();
		return value;
	}

	private void rehash() {

		HashtableOfLong newHashtable = new HashtableOfLong(this.elementSize * 2);		// double the number of expected elements
		String currentKey;
		for (int i = this.keyTable.length; --i >= 0;)
			if ((currentKey = this.keyTable[i]) != null)
				newHashtable.put(currentKey, this.valueTable[i]);

		this.keyTable = newHashtable.keyTable;
		this.valueTable = newHashtable.valueTable;
		this.threshold = newHashtable.threshold;
	}

	public long remove(String key) {

		int index = key.hashCode() % this.valueTable.length;
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

	public int size() {
		return this.elementSize;
	}

	public String toString() {
		String s = ""; //$NON-NLS-1$
		String key;
		for (int i = 0, length = this.valueTable.length; i < length; i++)
			if ((key = this.keyTable[i]) != null)
				s += new String(key) + " -> " + this.valueTable[i] + "\n"; 	//$NON-NLS-2$ //$NON-NLS-1$
		return s;
	}
}