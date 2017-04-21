package org.formulachess.chess.applet;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

import org.formulachess.chess.applet.ui.BoardController;
import org.formulachess.chess.applet.ui.BoardView;
import org.formulachess.chess.applet.ui.MoveListController;
import org.formulachess.chess.applet.ui.PGNHeader;

import org.formulachess.chess.applet.databaselist.DatabaseList;
import org.formulachess.chess.applet.util.SimpleBorder;
import org.formulachess.pgn.ast.PGNGame;

public class GameFrame extends Frame {

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
	
	public GameFrame(DatabaseList databaseList, String SET, int[] SETS, String title) {
		setTitle(title);
		BoardView boardView = new BoardView(this, SET, SETS);
		PGNGame pgnGame = databaseList.getPgnDatabase().getPGNGame(databaseList.getGameSelected());
		if (pgnGame == null) {
			return;
		}
		boardView.setBoard(board);
		
		BoardController boardControler = new BoardController();
		boardControler.setBoard(board);
		MoveListController moveListController = new MoveListController(board);
		
		Insets globalInsets = new Insets(2, 2, 2, 2);
		
		setLayout(new GridBagLayout());
		addComponent(this, // container
			new SimpleBorder(boardView), // component
			0, // gridx
			0, // gridy
			GridBagConstraints.RELATIVE, // gridwidth
			3, // gridheight
			GridBagConstraints.BOTH, // fill
			GridBagConstraints.CENTER, // anchor
			1.0, // weightx
			4.0, // weighty
			0, // ipadx
			0, // ipady
			globalInsets // insets
		);					

		addComponent(this, // container
			new SimpleBorder(new PGNHeader(pgnGame, Locale.FRANCE)), // component
			1, // gridx
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
		addComponent(this, // container
			boardControler, // component
			1, // gridx
			1, // gridy
			GridBagConstraints.REMAINDER, // gridwidth
			1, // gridheight
			GridBagConstraints.NONE, // fill
			GridBagConstraints.NORTH, // anchor
			1.0, // weightx
			1.0, // weighty
			0, // ipadx
			0, // ipady
			globalInsets // insets
		);
		addComponent(this, // container
			moveListController, // component
			1, // gridx
			2, // gridy
			GridBagConstraints.REMAINDER, // gridwidth
			GridBagConstraints.REMAINDER, // gridheight
			GridBagConstraints.BOTH, // fill
			GridBagConstraints.CENTER, // anchor
			1.0, // weightx
			100.0, // weighty
			0, // ipadx
			0, // ipady
			globalInsets // insets
		);					
		pack();
		setVisible(true);
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			    public void windowClosing(WindowEvent e) {
			    	setVisible(false);
			    	removeWindowListener(this);
			    	dispose();
			    }
		});
	}
}
