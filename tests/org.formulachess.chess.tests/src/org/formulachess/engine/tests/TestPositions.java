package org.formulachess.engine.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestPositions extends TestCase {
	public TestPositions(String name) {
		super(name);
	}
	public static Test suite() {
		if (true) {
			return new TestSuite(TestPositions.class);
		}
		TestSuite suite= new TestSuite();
		suite.addTest(new TestPositions("test009")); //$NON-NLS-1$
		return suite;
	}
	public void test001() {
		assertTrue("wrong square (" + Converter.squareToInt("a8") + ")", Converter.squareToInt("a8") == 0); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertTrue("wrong square (" + Converter.squareToInt("a7") + ")", Converter.squareToInt("a7") == 8); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertTrue("wrong square (" + Converter.squareToInt("e1") + ")", Converter.squareToInt("e1") == 60); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertTrue("wrong square (" + Converter.squareToInt("b6") + ")", Converter.squareToInt("b6") == 17); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertTrue("wrong square (" + Converter.squareToInt("h1") + ")", Converter.squareToInt("h1") == 63); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}