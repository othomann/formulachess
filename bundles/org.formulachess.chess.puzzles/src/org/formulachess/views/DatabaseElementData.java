package org.formulachess.views;


public class DatabaseElementData {
	public static final String PGN_GAME = "PGN_GAME"; //$NON-NLS-1$ 
	public static final String PROBLEM = "PROBLEM"; //$NON-NLS-1$
	
	public String fileName;
	public String type;
	
	public DatabaseElementData(String fileName, String type) {
		this.fileName = fileName;
		this.type = type;
	}
}
