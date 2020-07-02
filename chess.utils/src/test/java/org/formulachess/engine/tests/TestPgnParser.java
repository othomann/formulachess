package org.formulachess.engine.tests;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipInputStream;

import org.formulachess.engine.ChessEngine;
import org.formulachess.pgn.Parser;
import org.formulachess.pgn.ast.Move;
import org.formulachess.pgn.ast.PGNDatabase;
import org.formulachess.pgn.ast.PGNGame;
import org.formulachess.pgn.ast.TagSection;
import org.formulachess.pgn.engine.PGNModel;
import org.formulachess.util.Util;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestPgnParser {
	private static Logger myLogger = Logger.getLogger(TestPgnParser.class.getCanonicalName());

	@Test
	@DisplayName("test001")
	public void test001() {
		final String source = "[Event \"Sicilian Polugaevsky Tournament\"]\n" + //$NON-NLS-1$
				"\n" + //$NON-NLS-1$
				"1. e4 c5 { comment } 2. Nf3 {(1:39/2:01)} 1-0"; //$NON-NLS-1$
		parseSource(source);
	}

	@Test
	@DisplayName("test002")
	public void test002() {
		StringBuilder buffer = new StringBuilder();
		try (InputStream stream = TestPgnParser.class.getResourceAsStream("database.pgn")) {
			buffer.append(Util.getFileCharContent(stream, Util.UTF_8));
		} catch (Exception e) {
			myLogger.log(Level.INFO, "Exception occurred while running test002", e); //$NON-NLS-1$
		}
		String source = String.valueOf(buffer);
		parseSource(source);
	}

	@Test
	@DisplayName("test003")
	public void test003() {
		try (ZipInputStream zipStream = new ZipInputStream(
				new BufferedInputStream(TestPgnParser.class.getResourceAsStream("database.zip")))) {
			new Parser().parseArchive(zipStream);
		} catch(IOException e) {
			myLogger.log(Level.INFO, "Exception occurred while running test003", e); //$NON-NLS-1$
		}
	}

	private void parseSource(String source) {
		Parser parser = new Parser();
		PGNDatabase pgnDatabase = parser.parse(source.toCharArray());
		assertNotNull(pgnDatabase, "Should not be null"); //$NON-NLS-1$
		PGNGame[] pgnGames = pgnDatabase.getPGNGames();
		for (PGNGame game : pgnGames) {
			if (game.isFischerRandom()) {
				// Fischer Random is not handled
				continue;
			}
			PGNModel model = new PGNModel(game, new ChessEngine());
			Move[] moves = game.getMoveText().getMoves();
			model.playMovesTill(moves, moves.length - 1);
		}
	}
}