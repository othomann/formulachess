package org.formulachess.engine.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTests extends TestCase {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(TestAllMoves.class);
		suite.addTestSuite(TestPgnParser.class);
		suite.addTestSuite(TestPositions.class);
//		suite.addTestSuite(TestMateSearch.class);
		return suite;
	}
}
