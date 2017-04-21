package org.formulachess.chess.puzzles;

import org.eclipse.ui.plugin.AbstractUIPlugin;
/**
 * The main plugin class to be used in the desktop.
 */
public class PuzzlesPlugin extends AbstractUIPlugin {

	/**
	 * The plug-in identifier of the puzzles
	 * (value <code>"org.formulachess.chess.puzzles"</code>).
	 */
	public static final String PLUGIN_ID = "org.formulachess.chess.puzzles" ; //$NON-NLS-1$
	
	public static final String CHESS_PROBLEM_ID = "chesspb"; //$NON-NLS-1$
	//The shared instance.
	private static PuzzlesPlugin plugin;
	
	/**
	 * The constructor.
	 */
	public PuzzlesPlugin() {
		plugin = this;
	}


	/**
	 * Returns the shared instance.
	 */
	public static PuzzlesPlugin getDefault() {
		return plugin;
	}
}
