package org.formulachess.engine.tests;

import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.formulachess.chess.pgn.PGNLexer;
import org.formulachess.chess.pgn.PGNParser;
import org.formulachess.chess.pgn.SimplePGNVisitor;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestNewPgnParser extends TestCase {
	public TestNewPgnParser(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(TestNewPgnParser.class);
	}

	public void test003() {
		try (InputStream stream = this.getClass().getResourceAsStream("database.pgn")) {//$NON-NLS-1$
			ANTLRInputStream inputStream = new ANTLRInputStream(stream);
			PGNLexer lexer = new PGNLexer(inputStream);
			CommonTokenStream commonTokenStream = new CommonTokenStream(lexer);
			PGNParser parser = new PGNParser(commonTokenStream);

			SimplePGNVisitor visitor = new SimplePGNVisitor();
			visitor.visit(parser.pgn_database());
		} catch (IOException e) {
			assertTrue("Could not read the pgn file", false); //$NON-NLS-1$
		}
	}
}