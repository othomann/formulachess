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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Olivier
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
public class FileConverter2 {
	static Logger CurrentLogger = Logger.getLogger(FileConverter2.class.getCanonicalName());

	public static void main(String[] args) {
		try {
			LineNumberReader reader = new LineNumberReader(new InputStreamReader(FileConverter2.class.getResourceAsStream("mate2.txt"))); //$NON-NLS-1$
			String fenNotation;
			int counter = 253;
			int mateMaximum = 2;
			while((fenNotation = reader.readLine()) != null) {
				String pattern =
					"\tpublic void test" + display(counter) + "() {\r\n" + //$NON-NLS-1$ //$NON-NLS-2$
					"\t	checkMate(\"Position" + display(counter) + "\", \"" + fenNotation + "\", " + mateMaximum + ");\r\n" + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					"\t}\r\n"; //$NON-NLS-1$
				System.out.println(pattern);
				counter++;
			}
		} catch (FileNotFoundException e) {
			CurrentLogger.log(Level.SEVERE, "Should not happen", e); //$NON-NLS-1$
		} catch (IOException e) {
			CurrentLogger.log(Level.SEVERE, "Should not happen", e); //$NON-NLS-1$
		}
	}
	
	static String display(int i) {
		if (i < 10) {
			return "00" + i; //$NON-NLS-1$
		}
		if (i < 100) {
			return "0" + i; //$NON-NLS-1$
		}
		return Integer.toString(i);
	}
}
