package org.formulachess.engine.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestPositions extends TestCase {
	private static final String WRONG_SQUARE = "wrong square ("; //$NON-NLS-1$

	public TestPositions(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(TestPositions.class);
	}

	public void test001() {
		assertTrue(WRONG_SQUARE + Converter.squareToInt("a8") + ")", Converter.squareToInt("a8") == 0); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertTrue(WRONG_SQUARE + Converter.squareToInt("a7") + ")", Converter.squareToInt("a7") == 8); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertTrue(WRONG_SQUARE + Converter.squareToInt("e1") + ")", Converter.squareToInt("e1") == 60); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertTrue(WRONG_SQUARE + Converter.squareToInt("b6") + ")", Converter.squareToInt("b6") == 17); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertTrue(WRONG_SQUARE + Converter.squareToInt("h1") + ")", Converter.squareToInt("h1") == 63); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}