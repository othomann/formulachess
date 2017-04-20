package org.formulachess.chess.applet;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.formulachess.chess.applet.ui.Sets;

import org.formulachess.chess.applet.databaselist.DatabaseList;
import org.formulachess.chess.applet.util.Util;
import org.formulachess.pgn.Parser;
import org.formulachess.pgn.ast.PGNDatabase;

public class PGNViewer extends Applet {

	PGNDatabase pgnDatabase;
	GameFrame frame;
	String set;
	int[] sets;
	
	public void init() {
		String database = getParameter("database"); //$NON-NLS-1$
		this.set = "sets/set2"; //$NON-NLS-1$
		this.sets = Sets.SET2;
		try {
			URL url = new URL(database);
			URLConnection connection = url.openConnection();
			int length = connection.getContentLength();
			final InputStream stream = connection.getInputStream();//getClass().getResourceAsStream(database);
			if (stream == null) {
				reportError("check the database parameter : file not found"); //$NON-NLS-1$
				return;
			}
			char[] contents = Util.getFileCharContent(stream, length, null);
			Parser parser = new Parser();
			this.pgnDatabase = parser.parse(contents);
		} catch (IOException e) {
			reportError("check the database parameter : file not found"); //$NON-NLS-1$
			return;
		}
		Insets globalInsets = new Insets(2,2,2,2);
		setLayout(new GridBagLayout());
		addComponent(this, // container
			new DatabaseList(this.pgnDatabase) {
				public void doAction(String s) {
					if (PGNViewer.this.frame != null) {
						PGNViewer.this.frame.setVisible(false);
						PGNViewer.this.frame.dispose();
					}
					PGNViewer.this.frame = new GameFrame(this, PGNViewer.this.set, PGNViewer.this.sets, s);
				}
			}, // component
			0, // gridx
			0, // gridy
			GridBagConstraints.REMAINDER, // gridwidth
			1, // gridheight
			GridBagConstraints.BOTH, // fill
			GridBagConstraints.NORTH, // anchor
			1.0, // weightx
			1.0, // weighty
			0, // ipadx
			0, // ipady
			globalInsets // insets
		);
	}

	private void reportError(String s) {
		// TODO implements
	}
	
	/**
	 * add a component using a gridBagLayout
	 *
	 * @param container conteneur du layout
	 * @param component composant à ajouter
	 * @param gridx position X dans la grille
	 * @param gridy position Y dans la grille
	 * @param gridwidth largeur dans la grille
	 * @param gridheight hauteur dans la grille
	 * @param fill indique si le composant doit remplir l'espace
	 * @param anchor indique l'ancrage du composant
	 * @param weightx poids relatif du composant
	 * @param weighty poids relatif du composant
	 * @param ipadx 
	 * @param ipady
	 * @param insets insets pour ce component
	 */
	private static void addComponent(Container container, Component component, int gridx, int gridy, int gridwidth, int gridheight, int fill, int anchor, double weightx, double weighty, int ipadx, int ipady, Insets insets) {
		GridBagLayout gbl = (GridBagLayout) container.getLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;
		gbc.fill = fill;
		gbc.anchor = anchor;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbc.insets = insets;
		gbc.ipadx = ipadx;
		gbc.ipady = ipady;
		gbl.setConstraints(component, gbc);
		container.add(component);
	}	
}
