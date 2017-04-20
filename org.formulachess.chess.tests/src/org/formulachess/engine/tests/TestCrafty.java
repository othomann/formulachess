/*
 * Created on Mar 27, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package org.formulachess.engine.tests;

import java.io.*;

class StreamGobbler extends Thread {
	InputStream is;
	String type;

	StreamGobbler(InputStream is, String type) {
		this.is = is;
		this.type = type;
	}

	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(this.is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
				System.out.println(this.type + ">" + line); //$NON-NLS-1$
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}

class StreamWriter extends Thread {
	BufferedWriter output;
	String type;

	StreamWriter(OutputStream os, String type) {
		this.output = new BufferedWriter(new OutputStreamWriter(os));
		this.type = type;
	}

	public void run() {
		try {
			this.output.write("perft 1"); //$NON-NLS-1$
			this.output.flush();
			this.output.write("quit"); //$NON-NLS-1$
			this.output.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}

public class TestCrafty {
	public static void main(String args[]) {
		if (args.length < 1) {
			System.out.println("USAGE: java TestCrafty <cmd>"); //$NON-NLS-1$
			System.exit(1);
		}

	try {
/*
 			String osName = System.getProperty("os.name"); //$NON-NLS-1$
			String[] cmd = new String[3];
			if (osName.equals("Windows 2000")) {
				cmd[0] = "cmd.exe";
				cmd[1] = "/C";
				cmd[2] = args[0];
			} else if (osName.equals("Windows 95")) {
				cmd[0] = "command.com";
				cmd[1] = "/C";
				cmd[2] = args[0];
			}
*/
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(new String[] { "d:/temp/crafty.exe", "import", "d:/temp/testAllMoves.txt" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			// any error message?
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR"); //$NON-NLS-1$

			// any output?
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT"); //$NON-NLS-1$

			errorGobbler.start();
			outputGobbler.start();
		
			int exitVal = proc.waitFor();
			System.out.println("ExitValue: " + exitVal); //$NON-NLS-1$

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}