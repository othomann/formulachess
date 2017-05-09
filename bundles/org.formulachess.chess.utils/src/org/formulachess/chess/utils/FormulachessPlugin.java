package org.formulachess.chess.utils;

import org.eclipse.core.runtime.Plugin;

/**
 * The main plugin class to be used in the desktop.
 */
public class FormulachessPlugin extends Plugin {
	// The shared instance.
	private static FormulachessPlugin plugin;

	/**
	 * The constructor.
	 */
	public FormulachessPlugin() {
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static FormulachessPlugin getDefault() {
		return plugin;
	}
}
