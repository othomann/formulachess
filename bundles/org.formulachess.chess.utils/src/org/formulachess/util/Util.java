package org.formulachess.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Util {

	private static final int DEFAULT_READING_SIZE = 4096;
	public  static final String LINE_SEPARATOR = System.getProperty("line.separator"); //$NON-NLS-1$

	/**
	 * Returns the contents of the given file as a char array.
	 * When encoding is null, then the platform default one is used
	 * @throws IOException if a problem occured reading the file.
	 */
	public static char[] getFileCharContent(File file, String encoding) throws IOException {
		InputStream stream = null;
		try {
			stream = new BufferedInputStream(new FileInputStream(file));
			return getInputStreamAsCharArray(stream, (int) file.length(), encoding);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Returns the contents of the given file as a char array.
	 * When encoding is null, then the platform default one is used
	 * @throws IOException if a problem occured reading the file.
	 */
	public static char[] getFileCharContent(InputStream stream, String encoding) throws IOException {
		try {
			return getInputStreamAsCharArray(stream, -1, encoding);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Returns the given input stream's contents as a character array.
	 * If a length is specified (ie. if length != -1), only length chars
	 * are returned. Otherwise all chars in the stream are returned.
	 * Note this doesn't close the stream.
	 * @throws IOException if a problem occurred reading the stream.
	 */
	public static char[] getInputStreamAsCharArray(InputStream stream, int length, String encoding) throws IOException {
		try (InputStreamReader reader = encoding == null ? new InputStreamReader(stream)
				: new InputStreamReader(stream, encoding)) {
			char[] contents;
			if (length == -1) {
				contents = new char[0];
				int contentsLength = 0;
				int charsRead = -1;
				do {
					int available = Math.max(stream.available(), DEFAULT_READING_SIZE);
					// resize contents if needed
					if (contentsLength + available > contents.length) {
						System.arraycopy(contents,
								0,
								contents = new char[contentsLength + available],
								0,
								contentsLength);
					}
					// read as many chars as possible
					charsRead = reader.read(contents, contentsLength, available);
					if (charsRead > 0) {
						// remember length of contents
						contentsLength += charsRead;
					}
				} while (charsRead >= 0);
				// resize contents if necessary
				if (contentsLength < contents.length) {
					System.arraycopy(contents, 0, contents = new char[contentsLength], 0, contentsLength);
				}
			} else {
				contents = new char[length];
				int len = 0;
				int readSize = 0;
				while ((readSize != -1) && (len != length)) {
					len += readSize;
					readSize = reader.read(contents, len, length - len);
				}
				if (len != length)
					System.arraycopy(contents, 0, (contents = new char[len]), 0, len);
			}
			return contents;
		}
	}

	/**
	 * Returns the contents of the given zip entry as a byte array.
	 * @throws IOException if a problem occured reading the zip entry.
	 */
	public static char[] getZipEntryCharContent(ZipEntry ze, ZipFile zip)
		throws IOException {

		InputStream stream = null;
		try {
			stream = new BufferedInputStream(zip.getInputStream(ze));
			return getInputStreamAsCharArray(stream, (int) ze.getSize(), "ISO-8859-1"); //$NON-NLS-1$
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String getFisherRandomFEN(int number) {
		StringBuffer FEN = new StringBuffer(43);
		FEN.append("00000000/pppppppp/8/8/8/8/PPPPPPPP/11111111 w KQkq - 0 1"); //$NON-NLS-1$
		final int blackStartIndex = 0;
		final int whiteStartIndex = FEN.indexOf("1"); //$NON-NLS-1$
		int lightSquareBishopColum = number % 4;
		switch(lightSquareBishopColum) {
			case 0 :
				lightSquareBishopColum = 1;
				break;
			case 1 :
				lightSquareBishopColum = 3;
				break;
			case 2 :
				lightSquareBishopColum = 5;
				break;
			case 3 :
				lightSquareBishopColum = 7;
		}
		FEN.replace(blackStartIndex + lightSquareBishopColum, blackStartIndex + lightSquareBishopColum + 1, "b"); //$NON-NLS-1$
		FEN.replace(whiteStartIndex + lightSquareBishopColum, whiteStartIndex + lightSquareBishopColum + 1, "B"); //$NON-NLS-1$

		int darkSquaredBishopColum = (number / 4) % 4;
		switch(darkSquaredBishopColum) {
			case 0 :
				darkSquaredBishopColum = 0;
				break;
			case 1 :
				darkSquaredBishopColum = 2;
				break;
			case 2 :
				darkSquaredBishopColum = 4;
				break;
			case 3 :
				darkSquaredBishopColum = 6;
		}
		FEN.replace(blackStartIndex + darkSquaredBishopColum, blackStartIndex + darkSquaredBishopColum + 1, "b"); //$NON-NLS-1$
		FEN.replace(whiteStartIndex + darkSquaredBishopColum, whiteStartIndex + darkSquaredBishopColum + 1, "B"); //$NON-NLS-1$

		final int temp = (number / 4) / 4;
		final int kernIndex = (temp) / 6;
		int queenColumn = temp % 6;
		int index = 0;
		for (int i = 0; index <= queenColumn; i++) {
			if (i != lightSquareBishopColum && i != darkSquaredBishopColum) {
				index++;
			}
		}

		FEN.replace(blackStartIndex + index, blackStartIndex + index + 1, "q"); //$NON-NLS-1$
		FEN.replace(whiteStartIndex + index, whiteStartIndex + index + 1, "Q"); //$NON-NLS-1$

		String remainingPieces = null;
		switch(kernIndex) {
			case 0 :
				remainingPieces = "NNRKR"; //$NON-NLS-1$
				break;
			case 1 :
				remainingPieces = "NRNKR"; //$NON-NLS-1$
				break;
			case 2 :
				remainingPieces = "NRKNR"; //$NON-NLS-1$
				break;
			case 3 :
				remainingPieces = "NRKRN"; //$NON-NLS-1$
				break;
			case 4 :
				remainingPieces = "RNNKR"; //$NON-NLS-1$
				break;
			case 5 :
				remainingPieces = "RNKNR"; //$NON-NLS-1$
				break;
			case 6 :
				remainingPieces = "RNKRN"; //$NON-NLS-1$
				break;
			case 7 :
				remainingPieces = "RKNNR"; //$NON-NLS-1$
				break;
			case 8 :
				remainingPieces = "RKNRN"; //$NON-NLS-1$
				break;
			case 9 :
				remainingPieces = "RKRNN"; //$NON-NLS-1$
				break;
		}
		if (remainingPieces == null) return null;
		for (int i = 0; i < 5; i++) {
			char piece = remainingPieces.charAt(i);
			for (int j = 0; j < 8; j++) {
				if (FEN.charAt(j) == '0') {
					FEN.replace(blackStartIndex + j, blackStartIndex + j + 1, Character.toString(piece).toLowerCase());
					FEN.replace(whiteStartIndex + j, whiteStartIndex + j + 1, Character.toString(piece));
					break;
				}
			}
		}
		return String.valueOf(FEN);
	}

	public static void main(String[] args) {
		System.out.println(Util.getFisherRandomFEN(518));
	}
}
