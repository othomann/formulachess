package org.formulachess.views;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.formulachess.chess.puzzles.PuzzlesPlugin;
import org.formulachess.engine.ChessEngine;
import org.formulachess.pgn.Parser;
import org.formulachess.pgn.ast.PGNDatabase;
import org.formulachess.pgn.ast.PGNGame;
import org.formulachess.pgn.ast.TagSection;
import org.formulachess.ui.*;
import org.formulachess.util.Util;

public class ChessPuzzles {

	private static final Logger MyLogger = Logger.getLogger(ChessPuzzles.class.getCanonicalName());
	private static final int[] BOARD_SETTINGS = Sets.getSet(Sets.SET3_PATH);
	public static final String ID_VIEW = "org.formulachess.chess.puzzles.views.chessPuzzleView"; //$NON-NLS-1$
	private static final String HTTP_HEADER = "http://"; //$NON-NLS-1$
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$
	Button backButton;
	private Board board;
	TabItem boardTab;
	ChessEngine chessEngine;
	Locale currentLocale;

	Messages currentMessages;
	PGNGame currentPGNGame;
	TabItem databaseTab;

	DatabaseElementData[] datas;
	public Display display;
	Table fileNamesTable;
	PGNGame[] games;
	PGNHeader header;
	private ImageFactory imageFactory;

	Composite internalProblemComposite;
	Composite internalPGNViewerComposite;
	Composite internalSolutionComposite;

	TabItem pgnViewerTab;
	MoveController pgnViewerTabMoveController;

	TabItem problemsTab;
	Table problemsTable;
	MoveController problemTabMoveController;
	Button resetButton;
	public Composite shell;

	TabItem solutionTab;
	StatusLabel statusLabel;
	TabFolder subTabFolders;
	Combo combo;
	HashMap<String, PGNGame> hashMap;

	public ChessPuzzles(Composite parent) {
		this.hashMap = new HashMap<String, PGNGame>();
		this.currentLocale = Locale.getDefault();
		this.currentMessages = new Messages(this.currentLocale);
		this.display = parent.getDisplay();
		this.imageFactory = new ImageFactory(this.display);
		this.shell = new Composite(parent, SWT.NONE);
		this.shell.setBackground(this.display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

		// initialize datas
		IExtensionPoint extension = Platform.getExtensionRegistry().getExtensionPoint(PuzzlesPlugin.PLUGIN_ID,
				PuzzlesPlugin.CHESS_PROBLEM_ID);
		if (extension != null) {
			IExtension[] extensions = extension.getExtensions();
			this.datas = new DatabaseElementData[extensions.length];
			for (int i = 0; i < extensions.length; i++) {
				IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
				for (int j = 0; j < configElements.length; j++) {
					this.datas[i] = new DatabaseElementData(configElements[j].getAttribute("name"), //$NON-NLS-1$
							configElements[j].getAttribute("type")); //$NON-NLS-1$
				}
			}
		}

		TabFolder folders = new TabFolder(this.shell, SWT.BORDER);

		this.databaseTab = createDatabaseTabItem(folders);
		this.databaseTab.setControl(this.internalDatabaseComposite(folders));
		createBoardTab(folders);

		this.shell.setLayout(new FormLayout());

		FormData formData2 = new FormData();
		formData2.bottom = new FormAttachment(100, 0);
		formData2.top = new FormAttachment(0, 0);
		formData2.left = new FormAttachment(0, 0);
		formData2.right = new FormAttachment(100, 0);
		folders.setLayoutData(formData2);

		this.shell.pack();
	}

	public void close() {
		this.chessEngine.deleteObservers();
		this.statusLabel.dispose();
		this.imageFactory.dispose();
		this.board.dispose();
		this.shell.dispose();
		if (this.header != null) {
			this.header.dispose();
		}
	}

	private void createBoardTab(TabFolder folders) {
		this.boardTab = createBoardTabItem(folders);
		final Color foregroundColor = this.display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
		final Color backgroundColor = this.display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);

		Composite innerPanel = new Composite(folders, SWT.BORDER);
		this.boardTab.setControl(innerPanel);
		innerPanel.setForeground(foregroundColor);
		innerPanel.setBackground(backgroundColor);

		FormLayout formLayout = new FormLayout();
		innerPanel.setLayout(formLayout);

		this.chessEngine = new ChessEngine(this.currentLocale);
		this.board = new Board(innerPanel, this.chessEngine, BOARD_SETTINGS, BoardCanvas.WHITE_BOTTOM,
				this.imageFactory, SWT.NONE);

		this.resetButton = new Button(innerPanel, SWT.PUSH);
		this.resetButton.setText(this.currentMessages.getString("chesspuzzles.button.reset")); //$NON-NLS-1$
		this.resetButton.setBackground(backgroundColor);
		this.resetButton.setForeground(foregroundColor);
		this.resetButton.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				resetControllers();
			}
		});

		this.statusLabel = new StatusLabel(innerPanel, SWT.NONE, this.currentLocale, this.chessEngine);

		this.backButton = new Button(innerPanel, SWT.PUSH);
		this.backButton.setText(this.currentMessages.getString("chesspuzzles.button.back")); //$NON-NLS-1$
		this.backButton.setBackground(backgroundColor);
		this.backButton.setForeground(foregroundColor);
		this.backButton.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				ChessPuzzles.this.chessEngine.undoMove();
			}
		});

		this.subTabFolders = new TabFolder(innerPanel, SWT.NONE);
		this.internalProblemComposite = this.internalProblemComposite(this.subTabFolders);
		this.internalProblemComposite.setBackground(backgroundColor);
		this.internalProblemComposite.setForeground(foregroundColor);

		this.internalPGNViewerComposite = this.internalPGNViewerComposite(this.subTabFolders);
		this.internalPGNViewerComposite.setBackground(backgroundColor);
		this.internalPGNViewerComposite.setForeground(foregroundColor);

		this.internalSolutionComposite = this.internalSolutionComposite(this.subTabFolders);
		this.internalProblemComposite.setVisible(false);
		this.internalPGNViewerComposite.setVisible(false);
		this.internalSolutionComposite.setVisible(false);

		this.subTabFolders.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (e.item == ChessPuzzles.this.solutionTab) {
					ChessPuzzles.this.problemTabMoveController.restartGame();
					ChessPuzzles.this.problemTabMoveController.setIsReady(false);
					ChessPuzzles.this.backButton.setVisible(false);
					ChessPuzzles.this.resetButton.setVisible(false);
				} else {
					ChessPuzzles.this.problemTabMoveController.restartGame();
					ChessPuzzles.this.problemTabMoveController.setIsReady(true);
					ChessPuzzles.this.pgnViewerTabMoveController.restartGame();
					ChessPuzzles.this.pgnViewerTabMoveController.setIsReady(true);
					ChessPuzzles.this.backButton.setVisible(true);
					ChessPuzzles.this.resetButton.setVisible(true);
				}
			}
		});

		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 2);
		formData.top = new FormAttachment(0, 2);
		this.board.setLayoutData(formData);

		FormData formData5 = new FormData();
		formData5.left = new FormAttachment(this.board, 2);
		formData5.right = new FormAttachment(100, -2);
		formData5.top = new FormAttachment(0, 2);
		formData5.bottom = new FormAttachment(100, -2);
		this.subTabFolders.setLayoutData(formData5);

		FormData formData6 = new FormData();
		formData6.top = new FormAttachment(this.board, 0);
		formData6.left = new FormAttachment(2, 2);
		this.resetButton.setLayoutData(formData6);

		FormData formData8 = new FormData();
		formData8.top = new FormAttachment(this.board, 0);
		formData8.left = new FormAttachment(this.resetButton, 2);
		this.backButton.setLayoutData(formData8);

		FormData formData7 = new FormData();
		formData7.top = new FormAttachment(this.board, 0);
		formData7.left = new FormAttachment(this.backButton, 2);
		formData7.right = new FormAttachment(this.subTabFolders, -2);
		this.statusLabel.setLayoutData(formData7);
	}

	/**
	 * @param folders
	 * @return
	 */
	private TabItem createBoardTabItem(TabFolder folders) {
		TabItem tabItem = new TabItem(folders, SWT.NULL);
		tabItem.setText(this.currentMessages.getString("chesspuzzles.tabfolder.board")); //$NON-NLS-1$
		return tabItem;
	}

	private TabItem createDatabaseTabItem(TabFolder folders) {
		TabItem tabItem = new TabItem(folders, SWT.NULL);
		tabItem.setText(this.currentMessages.getString("chesspuzzles.tabfolder.database")); //$NON-NLS-1$
		return tabItem;
	}

	TabItem createPGNViewerTabItem(TabFolder folders) {
		TabItem tabItem = new TabItem(folders, SWT.NULL);
		tabItem.setText(this.currentMessages.getString("chesspuzzles.tabfolder.pgn_viewer")); //$NON-NLS-1$
		return tabItem;
	}

	void createPGNViewerTab(TabFolder folders) {
		this.pgnViewerTab = createPGNViewerTabItem(folders);
		this.pgnViewerTab.setControl(this.internalPGNViewerComposite);
	}

	/**
	 * @param subFolders
	 */
	void createProblemsTab(TabFolder folders) {
		this.problemsTab = createProblemTabItem(folders);
		this.problemsTab.setControl(this.internalProblemComposite);
	}

	private TabItem createProblemTabItem(TabFolder folders) {
		TabItem tabItem = new TabItem(folders, SWT.NULL);
		tabItem.setText(this.currentMessages.getString("chesspuzzles.tabfolder.problems")); //$NON-NLS-1$
		return tabItem;
	}

	/**
	 * @param subFolders
	 */
	void createSolutionTab(TabFolder folders) {
		this.solutionTab = createSolutionTabItem(folders);
		this.solutionTab.setControl(this.internalSolutionComposite);
	}

	/**
	 * @param folders
	 * @return
	 */
	private TabItem createSolutionTabItem(TabFolder folders) {
		TabItem tabItem = new TabItem(folders, SWT.NULL);
		tabItem.setText(this.currentMessages.getString("chesspuzzles.tabfolder.solution")); //$NON-NLS-1$
		return tabItem;
	}

	void addNewEntry(String filePath) {
		TableItem item = new TableItem(this.fileNamesTable, SWT.READ_ONLY);
		DatabaseElementData data = new DatabaseElementData(filePath, DatabaseElementData.PGN_GAME);
		item.setData(data);
		item.setText(data.fileName);
	}

	void initializeFileNamesTable() {
		if (this.datas != null) {
			for (int i = 0, max = this.datas.length; i < max; i++) {
				TableItem item = new TableItem(this.fileNamesTable, SWT.READ_ONLY);
				item.setData(this.datas[i]);
				item.setText(this.datas[i].fileName);
			}
		}
	}

	void initializeGames(String fileName) {
		InputStream inputStream = null;
		PGNDatabase pgnDatabase = null;
		try {
			URL url = null;
			if (fileName.startsWith(HTTP_HEADER)) {
				url = new URL(fileName);
			} else {
				url = FileLocator.find(PuzzlesPlugin.getDefault().getBundle(), new Path(fileName), null);
				if (url == null) {
					url = new URL("file:///" + fileName); //$NON-NLS-1$
				}
			}
			URLConnection connection = url.openConnection();
			inputStream = connection.getInputStream();
			char[] contents = null;
			if (fileName.toLowerCase().endsWith(".zip")) { //$NON-NLS-1$
				ZipInputStream zipInputStream = new ZipInputStream(inputStream);
				ZipEntry nextEntry = zipInputStream.getNextEntry();
				StringBuffer buffer = new StringBuffer();
				while (nextEntry != null) {
					if (nextEntry.getName().toLowerCase().endsWith(".pgn")) { //$NON-NLS-1$
						buffer.append(Util.getInputStreamAsCharArray(zipInputStream, (int) nextEntry.getSize(),
								"ISO-8859-1")); //$NON-NLS-1$
					}
					nextEntry = zipInputStream.getNextEntry();
				}
				contents = String.valueOf(buffer).toCharArray();
			} else {
				contents = Util.getFileCharContent(inputStream, null);
			}
			Parser parser = new Parser();
			pgnDatabase = parser.parse(contents);
		} catch (IOException e) {
			MyLogger.log(Level.SEVERE, "Could not read puzzles", e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					MyLogger.log(Level.SEVERE, "Could not close stream", e);
				}
			}
		}
		if (pgnDatabase == null) {
			this.games = null;
			return;
		}
		this.games = pgnDatabase.getPGNGames();
		if (this.games != null && this.games.length >= 1) {
			this.problemsTable.select(0);
			this.currentPGNGame = this.games[0];
			this.initializeCombo(null);
			refresh();
		}
	}

	/**
	 *
	 */
	void initializeCombo(FilterDialog.FilterResult result) {
		int max = this.games.length;
		if (max > 0) {
			if (result != null && result.filterId != FilterDialog.NONE) {
				this.combo.removeAll();
			}
			String[] headers = new String[max];
			int counter = 0;
			for (int i = 0; i < max; i++) {
				final PGNGame currentGame = this.games[i];
				if (!isFilter(result, currentGame)) {
					headers[counter] = getHeader(currentGame);
					this.hashMap.put(headers[counter], currentGame);
					counter++;
				}
			}
			if (counter != max) {
				// resize
				System.arraycopy(headers, 0, (headers = new String[counter]), 0, counter);
			}
			if (counter > 0) {
				this.combo.setItems(headers);
				this.combo.select(0);
				this.currentPGNGame = this.hashMap.get(headers[0]);
			} else {
				this.currentPGNGame = null;
			}
			this.header.setGame(this.currentPGNGame);
			this.refresh();
		}
	}

	void initializeProblemsTable() {
		if (this.games != null) {
			MessageFormat form = new MessageFormat(this.currentMessages.getString("chesspuzzles.tableitem.entryname")); //$NON-NLS-1$
			Object[] arguments = new Object[] { null };
			for (int i = 0, max = this.games.length; i < max; i++) {
				TableItem item = new TableItem(this.problemsTable, SWT.NONE);
				arguments[0] = new Integer(i + 1);
				item.setText(form.format(arguments));
			}
		}
	}

	/**
	 * @param folders
	 * @param backgroundColor
	 */
	private Composite internalDatabaseComposite(final TabFolder folders) {
		ScrolledComposite composite = new ScrolledComposite(folders, SWT.V_SCROLL);
		Composite innerPanel = new Composite(composite, SWT.BORDER);
		composite.setContent(innerPanel);
		composite.setExpandHorizontal(true);
		composite.setExpandVertical(true);
		innerPanel.setBackground(this.display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

		FormLayout formLayout = new FormLayout();
		innerPanel.setLayout(formLayout);

		Button button = new Button(innerPanel, SWT.BORDER);
		button.setText(this.currentMessages.getString("chesspuzzles.tabfolder.database.add_file")); //$NON-NLS-1$
		button.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events
			 * .SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				final String filePath = new FileDialog(new Shell()).open();
				if (filePath != null) {
					addNewEntry(filePath);
				}
			}
		});

		Text urlText = new Text(innerPanel, SWT.BORDER);
		urlText.setText(HTTP_HEADER);
		urlText.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				addNewEntry(((Text) e.widget).getText());
			}
		});
		this.fileNamesTable = new Table(innerPanel, SWT.BORDER);
		this.fileNamesTable.setLinesVisible(false);

		this.fileNamesTable.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				ChessPuzzles.this.problemsTable.removeAll();
				Rectangle clientArea = ChessPuzzles.this.fileNamesTable.getClientArea();
				Point pt = new Point(event.x, event.y);
				int index = ChessPuzzles.this.fileNamesTable.getTopIndex();
				while (index < ChessPuzzles.this.fileNamesTable.getItemCount()) {
					boolean visible = false;
					TableItem item = ChessPuzzles.this.fileNamesTable.getItem(index);
					for (int i = 0; i < 1; i++) {
						Rectangle rect = item.getBounds(i);
						if (rect.contains(pt)) {
							DatabaseElementData data = (DatabaseElementData) item.getData();
							if (ChessPuzzles.this.pgnViewerTab != null) {
								ChessPuzzles.this.pgnViewerTab.dispose();
								ChessPuzzles.this.internalPGNViewerComposite.setVisible(false);
							}
							if (ChessPuzzles.this.problemsTab != null) {
								ChessPuzzles.this.problemsTab.dispose();
								ChessPuzzles.this.internalProblemComposite.setVisible(false);
							}
							if (ChessPuzzles.this.solutionTab != null) {
								ChessPuzzles.this.solutionTab.dispose();
								ChessPuzzles.this.internalSolutionComposite.setVisible(false);
							}
							initializeGames(data.fileName);
							if (ChessPuzzles.this.games == null) {
								ChessPuzzles.this.fileNamesTable.remove(index);
								return;
							}
							if (DatabaseElementData.PROBLEM.equals(data.type)) {
								initializeProblemsTable();
								createProblemsTab(ChessPuzzles.this.subTabFolders);
								createSolutionTab(ChessPuzzles.this.subTabFolders);
								ChessPuzzles.this.internalProblemComposite.setVisible(true);
								ChessPuzzles.this.internalSolutionComposite.setVisible(true);
								folders.setSelection(1);
							} else if (DatabaseElementData.PGN_GAME.equals(data.type)) {
								createPGNViewerTab(ChessPuzzles.this.subTabFolders);
								ChessPuzzles.this.internalPGNViewerComposite.setVisible(true);
								ChessPuzzles.this.backButton.setVisible(false);
								ChessPuzzles.this.resetButton.setVisible(false);
								folders.setSelection(1);
							}
						}
						if (!visible && rect.intersects(clientArea)) {
							visible = true;
						}
					}
					if (!visible)
						return;
					index++;
				}
			}
		});
		initializeFileNamesTable();

		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 5);
		formData.left = new FormAttachment(25, 5);
		button.setLayoutData(formData);

		formData = new FormData();
		formData.top = new FormAttachment(0, 5);
		formData.left = new FormAttachment(button, 15);
		formData.right = new FormAttachment(100, -5);
		urlText.setLayoutData(formData);

		FormData formData2 = new FormData();
		formData2.top = new FormAttachment(button, 5);
		formData2.left = new FormAttachment(0, 5);
		formData2.right = new FormAttachment(100, -5);
		formData2.bottom = new FormAttachment(100, -5);
		formData2.width = 350;
		this.fileNamesTable.setLayoutData(formData2);

		composite.setMinSize(innerPanel.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		return composite;
	}

	/**
	 * @param folders
	 */
	private Composite internalPGNViewerComposite(TabFolder folders) {
		ScrolledComposite composite = new ScrolledComposite(folders, SWT.V_SCROLL | SWT.H_SCROLL);
		Composite innerPanel = new Composite(composite, SWT.BORDER);
		composite.setContent(innerPanel);
		composite.setExpandHorizontal(true);
		composite.setExpandVertical(true);

		FormLayout formLayout = new FormLayout();
		innerPanel.setLayout(formLayout);

		this.combo = new Combo(innerPanel, SWT.READ_ONLY);
		this.combo.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events
			 * .SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				Combo c = (Combo) e.widget;
				PGNGame game = ChessPuzzles.this.hashMap.get(c.getText());
				if (game != null) {
					ChessPuzzles.this.currentPGNGame = game;
					ChessPuzzles.this.header.setGame(game);
					refresh();
				}
			}
		});

		Button setFilterButton = new Button(innerPanel, SWT.BORDER);
		setFilterButton.setText(this.currentMessages.getString("chesspuzzles.tabfolder.pgn_viewer.button.filter")); //$NON-NLS-1$
		setFilterButton.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events
			 * .SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				FilterDialog filterDialog = new FilterDialog(new Shell(), ChessPuzzles.this.currentMessages);
				filterDialog.open();
				initializeCombo(filterDialog.filterResult);
			}
		});
		Group headerGroup = new Group(innerPanel, SWT.NONE);
		headerGroup.setBackground(this.display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		headerGroup.setForeground(this.display.getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
		FormLayout headerLayout = new FormLayout();
		headerGroup.setLayout(headerLayout);

		this.header = new PGNHeader(headerGroup, SWT.NONE, this.currentPGNGame, Locale.getDefault(), this.chessEngine);
		FormData headerFormData = new FormData();
		headerFormData.left = new FormAttachment(0, 0);
		headerFormData.top = new FormAttachment(0, 0);
		headerFormData.right = new FormAttachment(100, 0);
		headerFormData.bottom = new FormAttachment(100, 0);
		this.header.setLayoutData(headerFormData);

		this.pgnViewerTabMoveController = new MoveController(innerPanel, SWT.NONE, Locale.getDefault(),
				this.imageFactory, this.chessEngine);
		if (this.currentPGNGame != null) {
			this.pgnViewerTabMoveController.initializeMoveList(this.currentPGNGame);
			this.pgnViewerTabMoveController.restartGame();
		} else {
			this.pgnViewerTabMoveController.initializeMoveList(null);
		}

		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 2);
		formData.top = new FormAttachment(0, 2);
		this.combo.setLayoutData(formData);

		formData = new FormData();
		formData.left = new FormAttachment(this.combo, 2);
		formData.top = new FormAttachment(0, 2);
		setFilterButton.setLayoutData(formData);

		FormData formData2 = new FormData();
		formData2.left = new FormAttachment(0, 2);
		formData2.top = new FormAttachment(this.combo, 2);
		formData2.right = new FormAttachment(100, -2);
		headerGroup.setLayoutData(formData2);

		FormData formData3 = new FormData();
		formData3.left = new FormAttachment(0, 2);
		formData3.top = new FormAttachment(headerGroup, 2);
		formData3.right = new FormAttachment(100, -2);
		formData3.bottom = new FormAttachment(100, -2);
		this.pgnViewerTabMoveController.setLayoutData(formData3);

		return composite;
	}

	private boolean isFilter(FilterDialog.FilterResult result, PGNGame game) {
		if (result == null || result.filter == null || result.filterId == FilterDialog.NONE) {
			return false;
		}
		TagSection tagSection = game.getTagSection();
		String tag = null;
		switch (result.filterId) {
		case FilterDialog.BLACK_NAME:
			tag = getTag(tagSection, TagSection.TAG_BLACK);
			break;
		case FilterDialog.ECO:
			tag = getTag(tagSection, TagSection.TAG_ECO);
			break;
		case FilterDialog.EVENT:
			tag = getTag(tagSection, TagSection.TAG_EVENT);
			break;
		case FilterDialog.RESULT:
			tag = getTag(tagSection, TagSection.TAG_RESULT);
			break;
		case FilterDialog.ROUND:
			tag = getTag(tagSection, TagSection.TAG_ROUND);
			break;
		case FilterDialog.WHITE_NAME:
			tag = getTag(tagSection, TagSection.TAG_WHITE);
			break;
		}
		if (tag == null) {
			return false;
		}
		tag = tag.toLowerCase();
		if (result.useRegexp) {
			Pattern p = Pattern.compile(result.filter, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(tag);
			return !m.find();
		}
		return tag.indexOf(result.filter) == -1;
	}

	/**
	 * @param game
	 * @return
	 */
	private String getTag(TagSection tagSection, String tag) {
		String tagValue = tagSection.getTag(tag);
		return tagValue == null ? EMPTY_STRING : tagValue.substring(1, tagValue.length() - 1);
	}

	private String getHeader(PGNGame game) {
		StringBuffer buffer = new StringBuffer();
		TagSection tagSection = game.getTagSection();
		String round = this.getTag(tagSection, TagSection.TAG_ROUND);
		if (round != null) {
			buffer.append('(').append(round).append(") "); //$NON-NLS-1$
		} else {
			buffer.append('(').append('*').append(") "); //$NON-NLS-1$
		}
		String white = this.getTag(tagSection, TagSection.TAG_WHITE);
		if (white != null) {
			buffer.append(white);
		} else {
			buffer.append('*');
		}
		buffer.append(" - "); //$NON-NLS-1$
		String black = this.getTag(tagSection, TagSection.TAG_BLACK);
		if (black != null) {
			buffer.append(black);
		} else {
			buffer.append('*');
		}
		buffer.append(' ');
		buffer.append(this.getTag(tagSection, TagSection.TAG_RESULT));
		return String.valueOf(buffer);
	}

	/**
	 * @param folders
	 */
	private Composite internalProblemComposite(TabFolder folders) {
		ScrolledComposite composite = new ScrolledComposite(folders, SWT.V_SCROLL);
		Composite innerPanel = new Composite(composite, SWT.BORDER);
		composite.setContent(innerPanel);
		composite.setExpandHorizontal(true);
		composite.setExpandVertical(true);

		this.problemsTable = new Table(innerPanel, SWT.BORDER);
		this.problemsTable.setLinesVisible(false);

		this.problemsTable.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				Rectangle clientArea = ChessPuzzles.this.problemsTable.getClientArea();
				Point pt = new Point(event.x, event.y);
				int index = ChessPuzzles.this.problemsTable.getTopIndex();
				while (index < ChessPuzzles.this.problemsTable.getItemCount()) {
					boolean visible = false;
					TableItem item = ChessPuzzles.this.problemsTable.getItem(index);
					for (int i = 0; i < 1; i++) {
						Rectangle rect = item.getBounds(i);
						if (rect.contains(pt)) {
							ChessPuzzles.this.currentPGNGame = ChessPuzzles.this.games[index];
							refresh();
						}
						if (!visible && rect.intersects(clientArea)) {
							visible = true;
						}
					}
					if (!visible)
						return;
					index++;
				}
			}
		});

		FormLayout formLayout = new FormLayout();
		innerPanel.setLayout(formLayout);

		FormData formData2 = new FormData();
		formData2.left = new FormAttachment(0, 2);
		formData2.top = new FormAttachment(0, 2);
		formData2.right = new FormAttachment(100, -2);
		formData2.bottom = new FormAttachment(100, -2);
		this.problemsTable.setLayoutData(formData2);

		composite.setMinSize(innerPanel.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		return composite;
	}

	/**
	 * @param folders
	 */
	private Composite internalSolutionComposite(TabFolder folders) {
		ScrolledComposite composite = new ScrolledComposite(folders, SWT.V_SCROLL | SWT.H_SCROLL);
		Composite innerPanel = new Composite(composite, SWT.BORDER);
		composite.setContent(innerPanel);
		composite.setExpandHorizontal(true);
		composite.setExpandVertical(true);

		FormLayout formLayout = new FormLayout();
		innerPanel.setLayout(formLayout);

		this.problemTabMoveController = new MoveController(innerPanel, SWT.NONE, this.currentLocale, this.imageFactory,
				this.chessEngine);
		if (this.currentPGNGame != null) {
			this.problemTabMoveController.initializeMoveList(this.currentPGNGame);
		} else {
			this.problemTabMoveController.initializeMoveList(null);
		}
		this.problemTabMoveController.restartGame();

		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 2);
		formData.top = new FormAttachment(0, 2);
		formData.right = new FormAttachment(100, -2);
		formData.bottom = new FormAttachment(100, -2);
		this.problemTabMoveController.setLayoutData(formData);

		this.problemTabMoveController.setVisible(true);
		this.problemTabMoveController.setIsReady(true);

		composite.setMinSize(innerPanel.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		return composite;
	}

	public void refresh() {
		this.refreshMoveController(this.problemTabMoveController);
		this.refreshMoveController(this.pgnViewerTabMoveController);
	}

	private void refreshMoveController(MoveController moveController) {
		moveController.initializeMoveList(this.currentPGNGame);
		moveController.restartGame();
		moveController.setIsReady(true);
	}

	void resetControllers() {
		this.problemTabMoveController.restartGame();
		this.problemTabMoveController.setIsReady(!this.problemTabMoveController.isVisible());
		this.pgnViewerTabMoveController.restartGame();
		this.pgnViewerTabMoveController.setIsReady(!this.pgnViewerTabMoveController.isVisible());
	}

	public void setFocus() {
		this.shell.setFocus();
	}

	public void setLocale(Locale newLocale) {
		this.currentLocale = newLocale;
		this.currentMessages = new Messages(this.currentLocale);
		// TODO: Refresh all widgets
	}
}
