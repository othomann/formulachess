package org.formulachess.engine.tests;

import org.formulachess.engine.Converter;
import org.formulachess.util.Util;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

public class TestPositions {
	private static final String WRONG_SQUARE = "wrong square ("; //$NON-NLS-1$

	@Test
	@DisplayName("test001")
	public void test001() {
		assertTrue(Converter.squareToInt("a8") == 0, WRONG_SQUARE + Converter.squareToInt("a8") + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertTrue(Converter.squareToInt("a7") == 8, WRONG_SQUARE + Converter.squareToInt("a7") + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertTrue(Converter.squareToInt("e1") == 60, WRONG_SQUARE + Converter.squareToInt("e1") + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertTrue(Converter.squareToInt("b6") == 17, WRONG_SQUARE + Converter.squareToInt("b6") + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertTrue(Converter.squareToInt("h1") == 63, WRONG_SQUARE + Converter.squareToInt("h1") + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	@Test
	@DisplayName("testFisherRandom")
	public void test002() {
		Set<String> positions = new HashSet<>();
		for (int i = 0; i < 960; i++) {
			positions.add(Util.getFisherRandomFEN(i));
		}
		assertEquals(960, positions.size(), "Wrong number of fisher random positions");
	}
}