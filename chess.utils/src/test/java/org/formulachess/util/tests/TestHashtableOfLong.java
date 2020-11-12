package org.formulachess.util.tests;


import static org.junit.jupiter.api.Assertions.*;

import org.formulachess.util.HashtableOfLong;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestHashtableOfLong {

	@Test
	@DisplayName("test001")
	public void test001() {
		HashtableOfLong table = new HashtableOfLong();
		assertNotNull(table);
		table.put("test", 3);
		assertNotNull(table.get("test"), "key not found");
		assertEquals(HashtableOfLong.NO_VALUE, table.get("test2"), "key found");
		assertTrue(table.containsKey("test"), "key not found");
	}

	@Test
	@DisplayName("test002")
	public void test002() {
		HashtableOfLong table = new HashtableOfLong();
		assertNotNull(table);
		table.put("test", 3);
		table.put("max", Long.MAX_VALUE);
		table.put("min", Long.MIN_VALUE);
		assertEquals(3, table.size(), "wrong size");
		
		for (String key : table) {
			assertNotNull(key);
		}
	}
	
	@Test
	@DisplayName("test003")
	public void test003() {
		HashtableOfLong table = new HashtableOfLong();
		assertNotNull(table);
		table.put("test", 3);
		table.put("max", Long.MAX_VALUE);
		table.put("min", Long.MIN_VALUE);
		assertEquals(3, table.size(), "wrong size");
		table.remove("test");
		assertEquals(2, table.size(), "wrong size");
	}

	@Test
	@DisplayName("test004")
	public void test004() {
		HashtableOfLong table = new HashtableOfLong(5);
		assertNotNull(table);
		table.put("test", 3);
		table.put("max", Long.MAX_VALUE);
		table.put("min", Long.MIN_VALUE);
		table.put("test2", 5677555);
		assertEquals(4, table.size(), "wrong size");
	}
}
