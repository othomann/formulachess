/*
 * Created on 2003-03-16
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package org.formulachess.engine.tests;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.StringTokenizer;

/**
 * @author Olivier
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
public class FileConverter {

	public static void main(String[] args) {
		try {
			LineNumberReader reader = new LineNumberReader(new InputStreamReader(FileConverter.class.getResourceAsStream("test_cases.txt"))); //$NON-NLS-1$
			String line;
			int counter = 253;
			while((line = reader.readLine()) != null) {
				StringTokenizer tokenizer = new StringTokenizer(line, "|"); //$NON-NLS-1$
				tokenizer.nextToken();
				String fenNotation = tokenizer.nextToken();
				tokenizer.nextToken();
				String mateMaximum = tokenizer.nextToken();
				
				String pattern =
					"public void test" + display(counter) + "() {\r\n" + //$NON-NLS-1$ //$NON-NLS-2$
					"	checkMate(\"Position" + display(counter) + "\", \"" + fenNotation + "\", " + mateMaximum + ");\r\n" + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					"}\r\n"; //$NON-NLS-1$
				System.out.println(pattern);
				counter++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static String display(int i) {
		if (i < 10) {
			return "00" + i; //$NON-NLS-1$
		}
		if (i < 100) {
			return "0" + i; //$NON-NLS-1$
		}
		return "" + i; //$NON-NLS-1$
	}
}
