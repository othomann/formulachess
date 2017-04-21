package org.formulachess.chess.applet.databaselist;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import org.formulachess.chess.applet.util.Arrays;
import org.formulachess.pgn.ast.Move;
import org.formulachess.pgn.ast.PGNDatabase;
import org.formulachess.pgn.ast.PGNGame;
import org.formulachess.pgn.ast.TagSection;

public class DatabaseList extends Panel {

	private PGNDatabase pgnDatabase;
	ListItem[] listItems;
	int gameSelected;
	HeaderList list;
	String filterString;
	int filterId;
	Label numberOfGames;
	
	public DatabaseList(PGNDatabase database) {
		this.pgnDatabase = database;
		setLayout(new GridBagLayout());
		this.numberOfGames = new Label();
		final Choice choice = new Choice();
		choice.add("No sorting"); //$NON-NLS-1$
		choice.add("ECO"); //$NON-NLS-1$
		choice.add("White"); //$NON-NLS-1$
		choice.add("Black"); //$NON-NLS-1$
		choice.add("Round"); //$NON-NLS-1$
		choice.add("Event"); //$NON-NLS-1$
		choice.add("Result"); //$NON-NLS-1$
		choice.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				//setCriteria(ListItem.CODES[choice.getSelectedIndex()]);
				Arrays.sort(DatabaseList.this.listItems);
				DatabaseList.this.list.removeAll();
				for (int i = 0, max = DatabaseList.this.listItems.length; i < max; i++) {
					DatabaseList.this.list.add(DatabaseList.this.listItems[i].toString());
				}
			}
		});
		Button filter = new Button("Filter"); //$NON-NLS-1$
		filter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Filter _filter = new Filter(new Frame(), "Filter", true); //$NON-NLS-1$
				_filter.setVisible(true);
				DatabaseList.this.filterString = _filter.getFilter();
				DatabaseList.this.filterId = _filter.getFilterId();
				filterGames();
			}
		});
		if (database != null) {
			PGNGame[] pgnGames = database.getPGNGames();
			if (pgnGames != null) {
				this.list = new HeaderList(10);
				this.list.setFont(new Font("Dialog", Font.PLAIN, 12)); //$NON-NLS-1$
				this.list.setDimension(300, 150);
				filterGames();
				Arrays.sort(this.listItems);
				this.list.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						final ListItem selectedItem = DatabaseList.this.listItems[DatabaseList.this.list.getSelectedIndex()];
						DatabaseList.this.gameSelected = selectedItem.getGameIndex();
						doAction(selectedItem.toString());
					}
				});
				Insets globalInsets = new Insets(2,2,2,2);
				addComponent(this, // container
					choice, // component
					0, // gridx
					0, // gridy
					1, // gridwidth
					GridBagConstraints.RELATIVE, // gridheight
					GridBagConstraints.NONE, // fill
					GridBagConstraints.NORTHWEST, // anchor
					1.0, // weightx
					1.0, // weighty
					2, // ipadx
					2, // ipady
					globalInsets // insets
				);
				addComponent(this, // container
					this.numberOfGames, // component
					1, // gridx
					0, // gridy
					GridBagConstraints.RELATIVE, // gridwidth
					GridBagConstraints.RELATIVE, // gridheight
					GridBagConstraints.NONE, // fill
					GridBagConstraints.CENTER, // anchor
					1.0, // weightx
					1.0, // weighty
					2, // ipadx
					2, // ipady
					globalInsets // insets
				);
				addComponent(this, // container
					filter, // component
					2, // gridx
					0, // gridy
					GridBagConstraints.REMAINDER, // gridwidth
					GridBagConstraints.RELATIVE, // gridheight
					GridBagConstraints.NONE, // fill
					GridBagConstraints.NORTHEAST, // anchor
					1.0, // weightx
					1.0, // weighty
					2, // ipadx
					2, // ipady
					globalInsets // insets
				);
				addComponent(this, // container
					this.list, // component
					0, // gridx
					2, // gridy
					GridBagConstraints.REMAINDER, // gridwidth
					GridBagConstraints.REMAINDER, // gridheight
					GridBagConstraints.HORIZONTAL, // fill
					GridBagConstraints.SOUTH, // anchor
					1.0, // weightx
					1.0, // weighty
					2, // ipadx
					2, // ipady
					globalInsets // insets
				);
			}
		}
	}

	public ListItem addGameToList(int i) {
		TagSection tagSection = this.pgnDatabase.getPGNGame(i).getTagSection();
		String round = getTag(tagSection, "[Round"); //$NON-NLS-1$
		String whiteName = getTag(tagSection, "[White"); //$NON-NLS-1$
		String blackName = getTag(tagSection, "[Black"); //$NON-NLS-1$
		String eco  = getTag(tagSection, "[ECO"); //$NON-NLS-1$
		String result = getTag(tagSection, "[Result"); //$NON-NLS-1$
		String plyMovesString = getTag(tagSection, "[PlyCount"); //$NON-NLS-1$
		String event = getTag(tagSection, "[Event"); //$NON-NLS-1$
		int plyMoves = plyMovesString == null ? 0 : Integer.parseInt(plyMovesString) / 2;
		Move[] moves = this.pgnDatabase.getPGNGame(i).getMoveText().getMoves();
		if (moves != null) {
			plyMoves = (moves.length + 1) / 2;
		}
		return new ListItem(i, round, whiteName, blackName, eco, result, plyMoves, event);
		
	}
	
	private String getTag(TagSection tagSection, String key) {
		String value = tagSection.getTag(key);
		return value == null || value.length() == 0 ? null : value.substring(1, value.length() - 1);
	}
	/**
	 * Returns the gameSelected.
	 * @return int
	 */
	public int getGameSelected() {
		return this.gameSelected;
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
	
	public void filterGames() {
		if (this.pgnDatabase == null) {
			return;
		}
		PGNGame[] pgnGames = this.pgnDatabase.getPGNGames();
		if (pgnGames == null) {
			return;
		}
		int index = 0;
		this.list.removeAll();
		if (this.filterId == ListItem.NONE || this.filterString == null || this.filterString.length() == 0) {
			int length = pgnGames.length;
			this.listItems = new ListItem[length];			
			for (int i = 0, max = pgnGames.length; i < max; i++) {
				this.listItems[i] = addGameToList(i);
				this.listItems[i].setCriteria(ListItem.NONE);
			}			
			this.numberOfGames.setText(length + " games"); //$NON-NLS-1$
		} else if (this.filterString != null && this.filterString.length() != 0) {
			switch(this.filterId) {
				case ListItem.WHITE_NAME :
				case ListItem.BLACK_NAME :
				case ListItem.EVENT :
					this.filterString = this.filterString.toLowerCase();
			}
			int length = pgnGames.length;
			this.listItems = new ListItem[length];			
			for (int i = 0; i < length; i++) {
				TagSection tagSection = pgnGames[i].getTagSection();
				switch(this.filterId) {
					case ListItem.ECO :
						if (this.filterString.equals(getTag(tagSection, "[ECO"))) { //$NON-NLS-1$
							this.listItems[index++] = addGameToList(i);
						}
						break;
					case ListItem.WHITE_NAME :
						String whiteName = getTag(tagSection, "[White"); //$NON-NLS-1$
						if (whiteName != null) {
							whiteName = whiteName.toLowerCase();
							if (whiteName.indexOf(this.filterString) != -1) {
								this.listItems[index++] = addGameToList(i);
							}
						}
						break;
					case ListItem.BLACK_NAME :
						String blackName = getTag(tagSection, "[Black"); //$NON-NLS-1$
						if (blackName != null) {
							blackName = blackName.toLowerCase();
							if (blackName.indexOf(this.filterString) != -1) {
								this.listItems[index++] = addGameToList(i);
							}
						}
						break;
					case ListItem.RESULT :
						if (this.filterString.equals(getTag(tagSection, "[Result"))) { //$NON-NLS-1$
							this.listItems[index++] = addGameToList(i);
						}
						break;
					case ListItem.EVENT :
						String eventName = getTag(tagSection, "[Event"); //$NON-NLS-1$
						if (eventName != null) {
							eventName = eventName.toLowerCase();
							if (eventName.indexOf(this.filterString) != -1) {
								this.listItems[index++] = addGameToList(i);
							}
						}
						break;
					case ListItem.ROUND:
						if (this.filterString.equals(getTag(tagSection, "[Round"))) { //$NON-NLS-1$
							this.listItems[index++] = addGameToList(i);
						}
				}
			}
			if (this.listItems.length != index) {
				System.arraycopy(this.listItems, 0, (this.listItems = new ListItem[index]), 0, index);
			}
		}
		int length = this.listItems.length;
		for (int i = 0; i < length; i++) {
			String newItem = this.listItems[i].toString();
			this.list.add(newItem);
		}
		this.numberOfGames.setText(length + " games"); //$NON-NLS-1$

	}
	
	public void doAction(String s) {
		// nothing to do
	}
	/**
	 * Returns the this.pgnDatabase.
	 * @return this.pgnDatabase
	 */
	public PGNDatabase getPgnDatabase() {
		return this.pgnDatabase;
	}

}