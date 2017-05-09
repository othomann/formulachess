package org.formulachess.engine.tests;

import org.formulachess.pgn.InvalidInputException;
import org.formulachess.pgn.Parser;
import org.formulachess.pgn.Scanner;
import org.formulachess.pgn.TerminalSymbols;
import org.formulachess.pgn.ast.PGNDatabase;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestPgnParser extends TestCase {
	public TestPgnParser(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(TestPgnParser.class);
	}

	public void test001() {
		final String source = "[Event \"Sicilian Polugaevsky Tournament\"]\n" + //$NON-NLS-1$
				"\n" + //$NON-NLS-1$
				"1. e4 c5 { comment } 2. Nf3 {(1:39/2:01)} 1-0"; //$NON-NLS-1$
		Parser parser = new Parser();
		PGNDatabase pgnDatabase = parser.parse(source.toCharArray());
		assertNotNull("Should not be null", pgnDatabase); //$NON-NLS-1$
	}

	public void test002() throws InvalidInputException {
		final String source = "[Event \"Sicilian Polugaevsky Tournament\"]\n" + //$NON-NLS-1$
				"\n" + //$NON-NLS-1$
				"1. e4 c5 { comment } 2. Nf3 {(1:39/2:01)} 1-0"; //$NON-NLS-1$
		Scanner scanner = new Scanner();
		scanner.setSource(source.toCharArray());
		scanner.resetTo(0, source.length() - 1);
		while (scanner.getNextToken() != TerminalSymbols.TokenNameEOF) {
			scanner.getCurrentTokenSource();
		}
	}
}