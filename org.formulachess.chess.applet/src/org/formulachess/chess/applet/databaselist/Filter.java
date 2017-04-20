package org.formulachess.chess.applet.databaselist;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Filter extends Dialog {

	String filter;
	int filterId;
	int index;
	
	/**
	 * Constructor for Filter.
	 * @param owner
	 * @param title
	 * @param modal
	 */
	public Filter(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		CheckboxGroup checkboxGroup = new CheckboxGroup();
		Checkbox[] checkboxes = new Checkbox[6];
		checkboxes[0] = new Checkbox("Use ECO filter", false, checkboxGroup); //$NON-NLS-1$
		checkboxes[0].addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Filter.this.filterId = ListItem.ECO;
			}
		});
		checkboxes[1] = new Checkbox("Use White filter", false, checkboxGroup); //$NON-NLS-1$
		checkboxes[1].addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Filter.this.filterId = ListItem.WHITE_NAME;
			}
		});
		checkboxes[2] = new Checkbox("Use Black filter", false, checkboxGroup); //$NON-NLS-1$
		checkboxes[2].addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Filter.this.filterId = ListItem.BLACK_NAME;
			}
		});
		checkboxes[3]  = new Checkbox("Use Event filter", false, checkboxGroup); //$NON-NLS-1$
		checkboxes[3].addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Filter.this.filterId = ListItem.EVENT;
			}
		});
		checkboxes[4]  = new Checkbox("Use Round filter", false, checkboxGroup); //$NON-NLS-1$
		checkboxes[4].addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Filter.this.filterId = ListItem.ROUND;
			}
		});
		checkboxes[5]  = new Checkbox("Use Result filter", false, checkboxGroup); //$NON-NLS-1$
		checkboxes[5].addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Filter.this.filterId = ListItem.RESULT;
			}
		});
		
		// eco filter
		final TextField ecoTextField = new TextField();
		Label ecoFilterLabel = new Label("Enter ECO : "); //$NON-NLS-1$
		ecoTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					Filter.this.filter = ecoTextField.getText();
					setVisible(false);
					dispose();
				}
			}

		});
		
		// white filter
		final TextField whiteTextField = new TextField();
		Label whiteFilterLabel = new Label("Enter White name : "); //$NON-NLS-1$
		setLayout(new GridLayout(1,3));
		whiteTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					Filter.this.filter = whiteTextField.getText();
					setVisible(false);
					dispose();
				}
			}

		});
		
		// black filter
		final TextField blackTextField = new TextField();
		Label blackFilterLabel = new Label("Enter Black name : "); //$NON-NLS-1$
		setLayout(new GridLayout(1,3));
		blackTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					Filter.this.filter = blackTextField.getText();
					setVisible(false);
					dispose();
				}
			}

		});
	
		// event filter
		final TextField eventTextField = new TextField();
		Label eventFilterLabel = new Label("Enter Event name : "); //$NON-NLS-1$
		setLayout(new GridLayout(1,3));
		eventTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					Filter.this.filter = eventTextField.getText();
					setVisible(false);
					dispose();
				}
			}

		});	

		// round filter
		final TextField roundTextField = new TextField();
		Label roundFilterLabel = new Label("Enter Round number: "); //$NON-NLS-1$
		setLayout(new GridLayout(1,3));
		roundTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					Filter.this.filter = roundTextField.getText();
					setVisible(false);
					dispose();
				}
			}

		});	
		
		// result filter
		final TextField resultTextField = new TextField();
		Label resultFilterLabel = new Label("Enter Result : "); //$NON-NLS-1$
		setLayout(new GridLayout(1,3));
		resultTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					Filter.this.filter = resultTextField.getText();
					setVisible(false);
					dispose();
				}
			}

		});	
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Filter.this.filterId = ListItem.NONE;				
				setVisible(false);
				dispose();
			}
		});
		
		setLayout(new GridLayout(6,3));
		add(checkboxes[0]);
		add(ecoFilterLabel);
		add(ecoTextField);		

		add(checkboxes[1]);
		add(whiteFilterLabel);
		add(whiteTextField);		

		add(checkboxes[2]);
		add(blackFilterLabel);
		add(blackTextField);	

		add(checkboxes[3]);
		add(eventFilterLabel);
		add(eventTextField);
						
		add(checkboxes[4]);
		add(roundFilterLabel);
		add(roundTextField);

		add(checkboxes[5]);
		add(resultFilterLabel);
		add(resultTextField);

		pack();
	}

	
	/**
	 * Returns the filter.
	 * @return String
	 */
	public String getFilter() {
		return this.filter;
	}

	/**
	 * Returns the filterId.
	 * @return int
	 */
	public int getFilterId() {
		return this.filterId;
	}

}
