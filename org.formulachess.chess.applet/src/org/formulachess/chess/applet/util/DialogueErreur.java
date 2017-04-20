package org.formulachess.chess.applet.util;

import java.awt.Button;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.StringTokenizer;

public class DialogueErreur extends Dialog {
	private static final long serialVersionUID = -2012008367359159448L;
	private static final int GAME_OVER = 0x2;
	private static final int CORRUPTED_GAME = 0x1;
	
	public DialogueErreur(int errorCode) {
		super(new Frame(), "Error", true); //$NON-NLS-1$
		setLayout(new GridBagLayout());
		StringTokenizer tokenizer =
			new StringTokenizer(getErrorMessage(errorCode), "|"); //$NON-NLS-1$
		int i = 0;
		Insets globalInsets = new Insets(0, 0, 0, 0);
		while (tokenizer.hasMoreElements()) {
			String nextLine = (String) tokenizer.nextElement();
			addComponent(this, // container
			new Label(nextLine, Label.CENTER), // component
			0, // gridx
			i, // gridy
			1, // gridwidth
			1, // gridheight
			GridBagConstraints.NONE, // fill
			GridBagConstraints.CENTER, // anchor
			1.0, // weightx
			1.0, // weighty
			0, // ipadx
			0, // ipady
			globalInsets // insets
			);
			i++;
		}
		Button ok;
		addComponent(this, // container
			ok = new Button("OK"), // component //$NON-NLS-1$
			0, // gridx
			i, // gridy
			GridBagConstraints.REMAINDER, // gridwidth
			GridBagConstraints.RELATIVE, // gridheight
			GridBagConstraints.BOTH, // fill
			GridBagConstraints.CENTER, // anchor
			1.0, // weightx
			1.0, // weighty
			0, // ipadx
			0, // ipady
			globalInsets // insets
		);
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		Dimension screenSize = getToolkit().getScreenSize();
		Dimension size = getSize();
		setLocation(
			(screenSize.width - size.width) / 2,
			(screenSize.height - size.height) / 2);
		pack();
		setEnabled(true);
		setVisible(true);
	}
	public DialogueErreur(String s) {
		super(new Frame(), "", true); //$NON-NLS-1$
		setLayout(new GridBagLayout());
		StringTokenizer tokenizer = new StringTokenizer(s, "|"); //$NON-NLS-1$
		int i = 0;
		Insets globalInsets = new Insets(0, 0, 0, 0);
		while (tokenizer.hasMoreElements()) {
			String nextLine = (String) tokenizer.nextElement();
			addComponent(this, // container
			new Label(nextLine, Label.CENTER), // component
			0, // gridx
			i, // gridy
			1, // gridwidth
			1, // gridheight
			GridBagConstraints.NONE, // fill
			GridBagConstraints.CENTER, // anchor
			1.0, // weightx
			1.0, // weighty
			0, // ipadx
			0, // ipady
			globalInsets // insets
			);
			i++;
		}
		Button ok;
		addComponent(this, // container
			ok = new Button("OK"), // component //$NON-NLS-1$
			0, // gridx
			i, // gridy
			GridBagConstraints.REMAINDER, // gridwidth
			GridBagConstraints.RELATIVE, // gridheight
			GridBagConstraints.BOTH, // fill
			GridBagConstraints.CENTER, // anchor
			1.0, // weightx
			1.0, // weighty
			0, // ipadx
			0, // ipady
			globalInsets // insets
		);
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		Dimension screenSize = getToolkit().getScreenSize();
		Dimension size = getSize();
		setLocation(
			(screenSize.width - size.width) / 2,
			(screenSize.height - size.height) / 2);
		pack();
		setEnabled(true);
		setVisible(true);
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
	private static void addComponent(
		Container container,
		Component component,
		int gridx,
		int gridy,
		int gridwidth,
		int gridheight,
		int fill,
		int anchor,
		double weightx,
		double weighty,
		int ipadx,
		int ipady,
		Insets insets) {
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
	
	public String getErrorMessage(int errorCode) {
		switch (errorCode) {
			case CORRUPTED_GAME :
				return "Corrupted game.|Report problem to|thomann@home.com"; //$NON-NLS-1$
			case GAME_OVER :
				return "This game is over"; //$NON-NLS-1$
		}
		return "Missing error code"; //$NON-NLS-1$
	}
}
