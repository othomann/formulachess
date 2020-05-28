package org.formulachess.engine.tests;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.formulachess.pgn.Parser;
import org.formulachess.pgn.ast.PGNDatabase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestPgnParser {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");//$NON-NLS-1$
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
		try (InputStream stream = TestPgnParser.class.getResourceAsStream("database.pgn"); //$NON-NLS-1$
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line).append(LINE_SEPARATOR);
			}
		} catch (Exception e) {
			myLogger.log(Level.INFO, "Exception occurred while running test003", e); //$NON-NLS-1$
		}
		String source = String.valueOf(buffer);
		parseSource(source);
	}

	@Test
	@DisplayName("test003")
	public void test003() {
		byte[] buffer = new byte[2048];

		try (ZipInputStream zipStream = new ZipInputStream(
				new BufferedInputStream(TestPgnParser.class.getResourceAsStream("database.zip"))) //$NON-NLS-1$
		) {
			ZipEntry nextEntry;
			while ((nextEntry = zipStream.getNextEntry()) != null) {
				if (nextEntry.getName().endsWith(".pgn")) { //$NON-NLS-1$
					StringWriter writer = new StringWriter();
					int len;
					while ((len = zipStream.read(buffer)) > 0) {
						String contents = new String(buffer, 0, len);
						writer.write(contents);
					}
					myLogger.log(Level.INFO, "parse : " + nextEntry.getName()); //$NON-NLS-1$
					parseSource(writer.toString());
				}
			}
		} catch (Exception e) {
			myLogger.log(Level.INFO, "Exception occurred while running test004", e); //$NON-NLS-1$
			assertTrue(false, "Exception while parsing the database"); //$NON-NLS-1$
		}
	}

	private void parseSource(String source) {
		Parser parser = new Parser();
		PGNDatabase pgnDatabase = parser.parse(source.toCharArray());
		assertNotNull(pgnDatabase, "Should not be null"); //$NON-NLS-1$
	}
}