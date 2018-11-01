package org.formulachess.engine.tests;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.formulachess.pgn.Parser;
import org.formulachess.pgn.ast.PGNDatabase;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestPgnParser extends TestCase {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");//$NON-NLS-1$

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
		parseSource(source);
	}

	public void test002() {
		final String source = "[Event \"Sicilian Polugaevsky Tournament\"]\n" + //$NON-NLS-1$
				"\n" + //$NON-NLS-1$
				"1. e4 c5 { comment } 2. Nf3 {(1:39/2:01)} 1-0"; //$NON-NLS-1$
		parseSource(source);
	}

	public void test003() {
		StringBuffer buffer = new StringBuffer();
		try (InputStream stream = TestPgnParser.class.getResourceAsStream("database.pgn"); //$NON-NLS-1$
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line).append(LINE_SEPARATOR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String source = String.valueOf(buffer);
		parseSource(source);
	}

	public void test004() {
		byte[] buffer = new byte[2048];

		try (ZipInputStream zipStream = new ZipInputStream(
				new BufferedInputStream(TestPgnParser.class.getResourceAsStream("database.zip"))) //$NON-NLS-1$
		) {
			ZipEntry nextEntry;
			while ((nextEntry = zipStream.getNextEntry()) != null) {
				StringWriter writer = new StringWriter();
				int len;
				while ((len = zipStream.read(buffer)) > 0) {
					String contents = new String(buffer, 0, len);
					writer.write(contents);
				}
				System.out.println("parse : " + nextEntry.getName()); //$NON-NLS-1$
				parseSource(writer.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue("Exception while parsing the database", false); //$NON-NLS-1$
		}
	}

	private void parseSource(String source) {
		Parser parser = new Parser();
		PGNDatabase pgnDatabase = parser.parse(source.toCharArray());
		assertNotNull("Should not be null", pgnDatabase); //$NON-NLS-1$
	}
}