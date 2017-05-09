package org.formulachess.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class FilterDialog extends Dialog {
	public static class FilterResult {
		private String filter;
		private int filterId;
		private boolean useRegexp;

		public FilterResult() {
			this.filterId = FilterDialog.NONE;
		}

		public int getFilterId() {
			return filterId;
		}

		public String getFilter() {
			return filter;
		}

		public boolean isUseRegexp() {
			return useRegexp;
		}
	}

	public static final int NONE = 0;
	public static final int ROUND = 1;
	public static final int WHITE_NAME = 2;
	public static final int RESULT = 4;
	public static final int ECO = 8;
	public static final int BLACK_NAME = 16;
	public static final int EVENT = 32;

	private FilterResult filterResult;
	private Messages messages;

	/**
	 * Constructor for Filter.
	 * 
	 * @param owner
	 * @param title
	 * @param modal
	 */
	public FilterDialog(Shell shell, Messages messages) {
		super(shell, SWT.NONE);
		this.messages = messages;
	}

	public FilterResult getFilterResult() {
		return filterResult;
	}

	public String open() {
		final FilterResult result = new FilterResult();
		Shell parent = getParent();
		final Shell shell = new Shell(parent, SWT.TITLE | SWT.APPLICATION_MODAL | SWT.BORDER);
		shell.setText(this.messages.getString("chesspuzzles.filter.title")); //$NON-NLS-1$
		Display display = shell.getDisplay();
		Group group = new Group(shell, SWT.NONE);
		group.setText(this.messages.getString("chesspuzzles.filter.label.key")); //$NON-NLS-1$
		group.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		group.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
		GridLayout layout = new GridLayout(2, true);
		group.setLayout(layout);

		Button[] checkboxes = new Button[6];
		Button button = new Button(group, SWT.RADIO);
		button.setText(this.messages.getString("chesspuzzles.filter.key.eco")); //$NON-NLS-1$
		checkboxes[0] = button;
		button.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events
			 * .SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				result.filterId = FilterDialog.ECO;
			}
		});

		button = new Button(group, SWT.RADIO);
		button.setText(this.messages.getString("chesspuzzles.filter.key.white")); //$NON-NLS-1$
		checkboxes[1] = button;
		button.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events
			 * .SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				result.filterId = FilterDialog.WHITE_NAME;
			}
		});

		button = new Button(group, SWT.RADIO);
		button.setText(this.messages.getString("chesspuzzles.filter.key.black")); //$NON-NLS-1$
		checkboxes[2] = button;
		button.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events
			 * .SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				result.filterId = FilterDialog.BLACK_NAME;
			}
		});

		button = new Button(group, SWT.RADIO);
		button.setText(this.messages.getString("chesspuzzles.filter.key.event")); //$NON-NLS-1$
		checkboxes[3] = button;
		button.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events
			 * .SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				result.filterId = FilterDialog.EVENT;
			}
		});

		button = new Button(group, SWT.RADIO);
		button.setText(this.messages.getString("chesspuzzles.filter.key.round")); //$NON-NLS-1$
		checkboxes[4] = button;
		button.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events
			 * .SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				result.filterId = FilterDialog.ROUND;
			}
		});

		button = new Button(group, SWT.RADIO);
		button.setText(this.messages.getString("chesspuzzles.filter.key.result")); //$NON-NLS-1$
		checkboxes[5] = button;
		button.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events
			 * .SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				result.filterId = FilterDialog.RESULT;
			}
		});

		checkboxes[0].setLayoutData(
				new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, GridData.VERTICAL_ALIGN_CENTER, false, false));
		checkboxes[1].setLayoutData(
				new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, GridData.VERTICAL_ALIGN_CENTER, false, false));
		checkboxes[2].setLayoutData(
				new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, GridData.VERTICAL_ALIGN_CENTER, false, false));
		checkboxes[3].setLayoutData(
				new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, GridData.VERTICAL_ALIGN_CENTER, false, false));
		checkboxes[4].setLayoutData(
				new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, GridData.VERTICAL_ALIGN_CENTER, false, false));
		checkboxes[5].setLayoutData(
				new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, GridData.VERTICAL_ALIGN_CENTER, false, false));

		Group valueGroup = new Group(shell, SWT.NONE);
		valueGroup.setText(this.messages.getString("chesspuzzles.filter.label.value")); //$NON-NLS-1$
		valueGroup.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		valueGroup.setForeground(display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
		valueGroup.setLayout(new FormLayout());

		final Text text = new Text(valueGroup, SWT.BORDER);
		text.addKeyListener(new KeyAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.
			 * KeyEvent)
			 */
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					result.filter = ((Text) e.widget).getText();
				}
			}
		});

		Button useRegexp = new Button(valueGroup, SWT.CHECK);
		useRegexp.setText(this.messages.getString("chesspuzzles.filter.button.useregexp")); //$NON-NLS-1$
		useRegexp.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events
			 * .SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button) e.widget;
				result.useRegexp = b.getSelection();
			}
		});
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 5);
		formData.right = new FormAttachment(75, -5);
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(100, -5);
		text.setLayoutData(formData);

		formData = new FormData();
		formData.left = new FormAttachment(text, 5);
		formData.right = new FormAttachment(100, -5);
		formData.top = new FormAttachment(0, 5);
		formData.bottom = new FormAttachment(100, -5);
		useRegexp.setLayoutData(formData);

		shell.setLayout(new FormLayout());

		formData = new FormData();
		formData.left = new FormAttachment(0, 2);
		formData.top = new FormAttachment(0, 2);
		formData.right = new FormAttachment(100, -2);
		group.setLayoutData(formData);

		formData = new FormData();
		formData.left = new FormAttachment(0, 2);
		formData.top = new FormAttachment(group, 2);
		formData.right = new FormAttachment(100, -2);
		valueGroup.setLayoutData(formData);

		/* OK button sets data into table */
		Button ok = new Button(shell, SWT.PUSH);
		ok.setText(this.messages.getString("chesspuzzles.filter.button.ok")); //$NON-NLS-1$

		formData = new FormData();
		formData.left = new FormAttachment(30, 2);
		formData.top = new FormAttachment(valueGroup, 2);
		formData.right = new FormAttachment(50, -2);
		formData.bottom = new FormAttachment(100, -2);
		ok.setLayoutData(formData);

		ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result.filter = text.getText();
				shell.close();
			}
		});
		Button cancel = new Button(shell, SWT.PUSH);
		cancel.setText(this.messages.getString("chesspuzzles.filter.button.cancel")); //$NON-NLS-1$
		formData = new FormData();
		formData.left = new FormAttachment(50, 2);
		formData.top = new FormAttachment(valueGroup, 2);
		formData.right = new FormAttachment(70, -2);
		formData.bottom = new FormAttachment(100, -2);
		cancel.setLayoutData(formData);

		cancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result.filterId = FilterDialog.NONE;
				result.filter = null;
				shell.close();
			}
		});

		shell.setDefaultButton(ok);
		shell.pack();
		/* Center the dialog */
		Point center = parent.getLocation();
		center.x = center.x + (parent.getBounds().width / 2) - (shell.getBounds().width / 2);
		center.y = center.y + (parent.getBounds().height / 2) - (shell.getBounds().height / 2);
		shell.setLocation(center);
		shell.open();
		while (!shell.isDisposed()) {
			if (display.readAndDispatch()) {
				display.sleep();
			}
		}
		this.filterResult = result;
		return null;
	}
}
