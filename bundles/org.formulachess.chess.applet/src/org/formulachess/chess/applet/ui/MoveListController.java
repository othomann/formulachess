package org.formulachess.chess.applet.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.TextArea;
import java.util.Observable;
import java.util.Observer;

import chess.component.pgn.PGNBoard;

public class MoveListController extends Panel implements Observer {

	private MoveScroller scroller;
	private TextArea comments;
	private PGNBoard pgnBoard;

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
	
	public MoveListController(PGNBoard pgnBoard) {
		this.pgnBoard = pgnBoard;
		pgnBoard.addObserver(this);
		this.scroller = new MoveScroller(pgnBoard);
		this.comments = new TextArea(2,30);
		setLayout(new GridBagLayout());
		Insets globalInsets = new Insets(2, 2, 2, 2);
		addComponent(this, // container
			this.scroller, // component
			0, // gridx
			0, // gridy
			GridBagConstraints.REMAINDER, // gridwidth
			1, // gridheight
			GridBagConstraints.BOTH, // fill
			GridBagConstraints.NORTH, // anchor
			1.0, // weightx
			10.0, // weighty
			0, // ipadx
			0, // ipady
			globalInsets // insets
		);					
		addComponent(this, // container
			this.comments, // component
			0, // gridx
			1, // gridy
			GridBagConstraints.REMAINDER, // gridwidth
			GridBagConstraints.REMAINDER, // gridheight
			GridBagConstraints.HORIZONTAL, // fill
			GridBagConstraints.NORTH, // anchor
			1.0, // weightx
			1.0, // weighty
			0, // ipadx
			0, // ipady
			globalInsets // insets
		);					
	}
	/**
	 * @see java.util.Observer#update(Observable, Object)
	 */
	public void update(Observable o, Object arg) {
		if (o == this.pgnBoard) {
			String comment = this.pgnBoard.comment;
			if (comment != null) {
				this.comments.setText(comment);
			} else {
				this.comments.setText(""); //$NON-NLS-1$
			}
			repaint();
		}
	}

}
