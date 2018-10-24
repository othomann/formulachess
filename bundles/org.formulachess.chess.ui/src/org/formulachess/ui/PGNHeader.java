package org.formulachess.ui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import org.formulachess.engine.ChessEngine;
import org.formulachess.pgn.ast.Move;
import org.formulachess.pgn.ast.PGNGame;
import org.formulachess.pgn.ast.TagSection;
import static org.formulachess.engine.Turn.*;

public class PGNHeader extends Composite implements Observer {

	class Controller implements PaintListener {
		@Override
		public void paintControl(PaintEvent e) {
			updatebuilder();
			e.gc.drawImage(PGNHeader.this.doublebuilder, 0, 0);
		}
	}

	private static final String EMPTY = ""; //$NON-NLS-1$
	private static final String DEFAULT_DATE = "????.??.??"; //$NON-NLS-1$
	private static final Point PREFERRED_SIZE = new Point(316, 85);
	private Font boldFont;
	private Font normalFont;
	private boolean initialized;
	String eventName;
	String whiteRating;
	String whiteName;
	String blackRating;
	String blackName;
	String result;
	String eco;
	String plyMoves;
	String date;
	String round;
	String site;
	private int prefixOffset;
	private int resultOffset;
	private String whitePrefix;
	private String blackPrefix;
	private Point preferredSize;
	private ChessEngine model;
	private Locale locale;

	// double-buildering
	Image doublebuilder;

	private Messages currentMessages;

	public PGNHeader(Composite parent, int style, PGNGame pgnGame, Locale locale, ChessEngine model) {
		super(parent, style);
		this.locale = locale;
		this.currentMessages = new Messages(locale);
		this.model = model;
		this.model.addObserver(this);
		Display display = parent.getShell().getDisplay();
		setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		this.boldFont = new Font(display, "Dialog", 8, SWT.BOLD); //$NON-NLS-1$
		this.normalFont = new Font(display, "SansSerif", 8, SWT.NORMAL); //$NON-NLS-1$

		setGame(pgnGame);

		this.preferredSize = PREFERRED_SIZE;
		setSize(this.preferredSize);
		// init double builder
		this.doublebuilder = new Image(display, this.preferredSize.x, this.preferredSize.y);
		addPaintListener(new Controller());
	}

	/**
	 * @param pgnGame
	 * @param locale
	 */
	public void setGame(PGNGame pgnGame) {
		if (pgnGame == null) {
			this.initialized = false;
			this.eventName = EMPTY;
			this.whiteRating = EMPTY;
			this.whiteName = EMPTY;
			this.blackRating = EMPTY;
			this.blackName = EMPTY;
			this.result = EMPTY;
			this.eco = EMPTY;
			this.plyMoves = EMPTY;
			this.date = EMPTY;
			this.round = EMPTY;
			this.site = EMPTY;
		} else {
			this.initialized = true;
			TagSection tagSection = pgnGame.getTagSection();
			this.eventName = getTag(tagSection, TagSection.TAG_EVENT);
			this.whiteRating = getTag(tagSection, TagSection.TAG_WHITE_ELO);
			this.whiteName = getTag(tagSection, TagSection.TAG_WHITE);
			this.blackRating = getTag(tagSection, TagSection.TAG_BLACK_ELO);
			this.blackName = getTag(tagSection, TagSection.TAG_BLACK);
			this.result = getTag(tagSection, TagSection.TAG_RESULT);
			if (this.result.equals(TagSection.TAG_UNFINISHED)) {
				this.result = this.currentMessages.getString("pgnheader.game.result"); //$NON-NLS-1$
			}
			this.eco = getTag(tagSection, TagSection.TAG_ECO);
			this.plyMoves = getTag(tagSection, TagSection.TAG_PLYCOUNT);
			if (this.plyMoves == null || this.plyMoves.length() == 0) {
				Move[] moves = pgnGame.getMoveText().getMoves();
				if (moves != null) {
					this.plyMoves = Integer.toString((moves.length + 1) / 2);
				} else {
					this.plyMoves = this.currentMessages.getString("pgnheader.game.plymoves.zero"); //$NON-NLS-1$
				}
			}
			this.date = getTag(tagSection, TagSection.TAG_DATE);
			Date actualDate = null;
			try {
				if (this.date.length() != 0 && this.date.indexOf('?') == -1) {
					actualDate = new SimpleDateFormat("yyyy.MM.dd", Locale.US).parse(this.date); //$NON-NLS-1$
					this.date = DateFormat.getDateInstance(DateFormat.LONG, this.locale).format(actualDate);
				}
			} catch (ParseException e) {
				this.date = DEFAULT_DATE;
			}
			this.round = getTag(tagSection, TagSection.TAG_ROUND);
			this.site = getTag(tagSection, TagSection.TAG_SITE);
		}
	}

	@Override
	public void dispose() {
		this.normalFont.dispose();
		this.boldFont.dispose();
		this.doublebuilder.dispose();
	}

	private String getTag(TagSection tagSection, String key) {
		String value = tagSection.getTag(key);
		return value == null ? EMPTY : value.substring(1, value.length() - 1);
	}

	public void updatebuilder() {
		GC gc = new GC(this.doublebuilder);
		Display display = getDisplay();
		if (!this.initialized) {
			gc.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
			gc.fillRectangle(0, 0, this.preferredSize.x, this.preferredSize.y);
		} else {
			gc.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
			gc.fillRectangle(0, 0, this.preferredSize.x, this.preferredSize.y);
			gc.setFont(this.normalFont);
			FontMetrics plainFontMetrics = gc.getFontMetrics();
			gc.setFont(this.boldFont);
			FontMetrics boldFontMetrics = gc.getFontMetrics();
			StringBuilder builder = new StringBuilder();
			appendTobuilder(builder, this.whiteRating, "    "); //$NON-NLS-1$
			appendTobuilder(builder, "  "); //$NON-NLS-1$
			this.whitePrefix = builder.toString();
			builder = new StringBuilder();
			appendTobuilder(builder, this.blackRating, "    "); //$NON-NLS-1$
			appendTobuilder(builder, "  "); //$NON-NLS-1$
			this.blackPrefix = builder.toString();
			this.prefixOffset = Math.max(((int)plainFontMetrics.getAverageCharacterWidth()) * this.whitePrefix.length(),
					((int) plainFontMetrics.getAverageCharacterWidth()) * this.blackPrefix.length());
			this.resultOffset = ((int) boldFontMetrics.getAverageCharacterWidth()) * this.result.length();

			builder = new StringBuilder();
			appendTobuilder(builder, this.eventName);
			appendTobuilder(builder, ", "); //$NON-NLS-1$
			appendTobuilder(builder, this.site);
			appendTobuilder(builder, " ("); //$NON-NLS-1$
			appendTobuilder(builder, this.round);
			appendTobuilder(builder, ")"); //$NON-NLS-1$
			gc.setFont(this.boldFont);
			gc.drawString(builder.toString(), 5, 5, true);
			gc.drawRectangle(15, 25, 15, 15);
			gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
			gc.fillRectangle(16, 26, 13, 13);
			gc.setFont(this.normalFont);
			gc.drawString(this.whitePrefix, 39, 24, true);
			builder = new StringBuilder();
			appendTobuilder(builder, this.whiteName);
			gc.setFont(this.boldFont);
			if (this.model.getTurn() == WHITE_TURN) {
				gc.drawString(">", 8, 25, true); //$NON-NLS-1$
			}
			gc.drawString(builder.toString(), 39 + this.prefixOffset, 24, true);
			gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
			gc.fillRectangle(15, 45, 15, 15);
			gc.setFont(this.normalFont);
			gc.drawString(this.blackPrefix, 39, 43, true);
			gc.setFont(this.boldFont);
			if (this.model.getTurn() == BLACK_TURN) {
				gc.drawString(">", 8, 45, true); //$NON-NLS-1$
			}
			builder = new StringBuilder();
			appendTobuilder(builder, this.blackName);
			gc.drawString(builder.toString(), 39 + this.prefixOffset, 43, true);
			gc.setFont(this.boldFont);
			gc.drawString(this.result, 5, 60, true);
			gc.setFont(this.normalFont);
			builder = new StringBuilder();
			appendTobuilder(builder, this.plyMoves, "   "); //$NON-NLS-1$
			gc.drawString(builder.toString(), 25 + this.resultOffset, 60, true);
			builder = new StringBuilder();
			appendTobuilder(builder, this.eco, "   "); //$NON-NLS-1$
			gc.drawString(builder.toString(), 65 + this.resultOffset, 60, true);
			gc.drawString(this.date, 115 + this.resultOffset, 60, true);
		}
		gc.dispose();
	}

	private void appendTobuilder(StringBuilder builder, String value, String subsitute) {
		if (value != null && value.length() != 0) {
			builder.append(value);
		} else {
			builder.append(subsitute);
		}
	}

	private void appendTobuilder(StringBuilder builder, String value) {
		if (value != null) {
			builder.append(value);
		}
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		return this.preferredSize;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o == this.model) {
			this.redraw();
		}
	}

}
