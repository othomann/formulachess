package org.formulachess.ui;

import org.eclipse.core.runtime.Plugin;

/**
 * The main plugin class to be used in the desktop.
 */
public class ChessUIPlugin extends Plugin {

	/**
	 * The plug-in identifier of the puzzles (value
	 * <code>"org.formulachess.chess.puzzles"</code>).
	 */
	public static final String PLUGIN_ID = "org.formulachess.chess.ui"; //$NON-NLS-1$

	// The shared instance.
	private static ChessUIPlugin plugin;

	/**
	 * The constructor.
	 */
	public ChessUIPlugin() {
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static ChessUIPlugin getDefault() {
		return plugin;
	}
}
