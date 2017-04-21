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

/**
 * @author Olivier
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
public class FileConverter3 {

	public static void main(String[] args) {
		try {
			LineNumberReader reader = new LineNumberReader(new InputStreamReader(FileConverter3.class.getResourceAsStream("mate3.txt"))); //$NON-NLS-1$
			String fenNotation;
			int counter = 403;
			int mateMaximum = 3;
			while((fenNotation = reader.readLine()) != null) {
				String pattern =
					"\tpublic void test" + display(counter) + "() {\r\n" + //$NON-NLS-1$ //$NON-NLS-2$
					"\t	checkMate(\"Position" + display(counter) + "\", \"" + fenNotation + "\", " + mateMaximum + ");\r\n" + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					"\t}\r\n"; //$NON-NLS-1$
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
