package org.formulachess.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Util {

	private static final char[] NO_CHAR = new char[0];
	private static final String UTF_8 = "UTF-8"; //$NON-NLS-1$
	private static final int DEFAULT_READING_SIZE = 4096;
	public static final String LINE_SEPARATOR = System.getProperty("line.separator"); //$NON-NLS-1$

	private Util() {
		// disable constructor
	}

	/**
	 * Returns the contents of the given file as a char array. When encoding is
	 * null, then the platform default one is used
	 * 
	 * @throws IOException
	 *             if a problem occurred reading the file.
	 */
	public static char[] getFileCharContent(File file, String encoding) throws IOException {
		try (InputStream stream = new BufferedInputStream(new FileInputStream(file))) {
			return getInputStreamAsCharArray(stream, (int) file.length(), encoding);
		}
	}

	/**
	 * Returns the contents of the given file as a char array. When encoding is
	 * null, then the platform default one is used
	 * 
	 * @throws IOException
	 *             if a problem occurred reading the file.
	 */
	public static char[] getFileCharContent(InputStream stream, String encoding) throws IOException {
		return getInputStreamAsCharArray(stream, -1, encoding);
	}

	/**
	 * Returns the given input stream's contents as a character array. If a length
	 * is specified (i.e. if length != -1), only length chars are returned.
	 * Otherwise all chars in the stream are returned. Note this doesn't close the
	 * stream.
	 * 
	 * @throws IOException
	 *             if a problem occurred reading the stream.
	 */
	public static char[] getInputStreamAsCharArray(InputStream stream, int length, String encoding) throws IOException {
		BufferedReader reader = null;
		try {
			reader = encoding == null ? new BufferedReader(new InputStreamReader(stream))
					: new BufferedReader(new InputStreamReader(stream, encoding));
		} catch (UnsupportedEncodingException e) {
			// encoding is not supported
			reader = new BufferedReader(new InputStreamReader(stream));
		}
		char[] contents;
		int totalRead = 0;
		if (length == -1) {
			contents = NO_CHAR;
		} else {
			// length is a good guess when the encoding produces less or the same amount of
			// characters than the file length
			contents = new char[length]; // best guess
		}

		while (true) {
			int amountRequested;
			if (totalRead < length) {
				// until known length is met, reuse same array sized eagerly
				amountRequested = length - totalRead;
			} else {
				// reading beyond known length
				int current = reader.read();
				if (current < 0) {
					break;
				}

				amountRequested = Math.max(stream.available(), DEFAULT_READING_SIZE); // read at least 8K

				// resize contents if needed
				if (totalRead + 1 + amountRequested > contents.length)
					System.arraycopy(contents, 0, contents = new char[totalRead + 1 + amountRequested], 0, totalRead);

				// add current character
				contents[totalRead++] = (char) current; // coming from totalRead==length
			}
			// read as many chars as possible
			int amountRead = reader.read(contents, totalRead, amountRequested);
			if (amountRead < 0) {
				break;
			}
			totalRead += amountRead;
		}

		// Do not keep first character for UTF-8 BOM encoding
		int start = 0;
		if (totalRead > 0 && UTF_8.equals(encoding) && contents[0] == 0xFEFF) { // if BOM char then skip
			totalRead--;
			start = 1;
		}

		// resize contents if necessary
		if (totalRead < contents.length) {
			System.arraycopy(contents, start, contents = new char[totalRead], 0, totalRead);
		}
		return contents;
	}

	/**
	 * Returns the contents of the given zip entry as a byte array.
	 * 
	 * @throws IOException
	 *             if a problem occurred reading the zip entry.
	 */
	public static char[] getZipEntryCharContent(ZipEntry ze, ZipFile zip) throws IOException {
		try (InputStream stream = new BufferedInputStream(zip.getInputStream(ze))) {
			return getInputStreamAsCharArray(stream, (int) ze.getSize(), "ISO-8859-1"); //$NON-NLS-1$
		}
	}

	public static String getFisherRandomFEN(int number) {
		StringBuilder fenNotation = new StringBuilder(43);
		fenNotation.append("00000000/pppppppp/8/8/8/8/PPPPPPPP/11111111 w KQkq - 0 1"); //$NON-NLS-1$
		final int blackStartIndex = 0;
		final int whiteStartIndex = fenNotation.indexOf("1"); //$NON-NLS-1$
		int lightSquareBishopColum = number % 4;
		switch (lightSquareBishopColum) {
		case 0:
			lightSquareBishopColum = 1;
			break;
		case 1:
			lightSquareBishopColum = 3;
			break;
		case 2:
			lightSquareBishopColum = 5;
			break;
		case 3:
			lightSquareBishopColum = 7;
			break;
		default:
		}
		fenNotation.replace(blackStartIndex + lightSquareBishopColum, blackStartIndex + lightSquareBishopColum + 1,
				"b"); //$NON-NLS-1$
		fenNotation.replace(whiteStartIndex + lightSquareBishopColum, whiteStartIndex + lightSquareBishopColum + 1,
				"B"); //$NON-NLS-1$

		int darkSquaredBishopColum = (number / 4) % 4;
		switch (darkSquaredBishopColum) {
		case 0:
			darkSquaredBishopColum = 0;
			break;
		case 1:
			darkSquaredBishopColum = 2;
			break;
		case 2:
			darkSquaredBishopColum = 4;
			break;
		case 3:
			darkSquaredBishopColum = 6;
			break;
		default:
		}
		fenNotation.replace(blackStartIndex + darkSquaredBishopColum, blackStartIndex + darkSquaredBishopColum + 1,
				"b"); //$NON-NLS-1$
		fenNotation.replace(whiteStartIndex + darkSquaredBishopColum, whiteStartIndex + darkSquaredBishopColum + 1,
				"B"); //$NON-NLS-1$

		final int temp = (number / 4) / 4;
		final int kernIndex = (temp) / 6;
		int queenColumn = temp % 6;
		int index = 0;
		for (int i = 0; index <= queenColumn; i++) {
			if (i != lightSquareBishopColum && i != darkSquaredBishopColum) {
				index++;
			}
		}

		fenNotation.replace(blackStartIndex + index, blackStartIndex + index + 1, "q"); //$NON-NLS-1$
		fenNotation.replace(whiteStartIndex + index, whiteStartIndex + index + 1, "Q"); //$NON-NLS-1$

		String remainingPieces = null;
		switch (kernIndex) {
		case 0:
			remainingPieces = "NNRKR"; //$NON-NLS-1$
			break;
		case 1:
			remainingPieces = "NRNKR"; //$NON-NLS-1$
			break;
		case 2:
			remainingPieces = "NRKNR"; //$NON-NLS-1$
			break;
		case 3:
			remainingPieces = "NRKRN"; //$NON-NLS-1$
			break;
		case 4:
			remainingPieces = "RNNKR"; //$NON-NLS-1$
			break;
		case 5:
			remainingPieces = "RNKNR"; //$NON-NLS-1$
			break;
		case 6:
			remainingPieces = "RNKRN"; //$NON-NLS-1$
			break;
		case 7:
			remainingPieces = "RKNNR"; //$NON-NLS-1$
			break;
		case 8:
			remainingPieces = "RKNRN"; //$NON-NLS-1$
			break;
		case 9:
			remainingPieces = "RKRNN"; //$NON-NLS-1$
			break;
		default:
		}
		if (remainingPieces == null) {
			return null;
		}
		for (int i = 0; i < 5; i++) {
			char piece = remainingPieces.charAt(i);
			for (int j = 0; j < 8; j++) {
				if (fenNotation.charAt(j) == '0') {
					fenNotation.replace(blackStartIndex + j, blackStartIndex + j + 1,
							Character.toString(piece).toLowerCase());
					fenNotation.replace(whiteStartIndex + j, whiteStartIndex + j + 1, Character.toString(piece));
					break;
				}
			}
		}
		return String.valueOf(fenNotation);
	}
}
