package org.formulachess.chess.applet.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Panel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.formulachess.pgn.ast.PGNGame;
import org.formulachess.pgn.ast.TagSection;

public class PGNHeader extends Panel {

	private static final String EMPTY = ""; //$NON-NLS-1$
	private static final Font BOLD_FONT = new Font("Dialog", Font.BOLD, 12); //$NON-NLS-1$
	private static final Font PLAIN_FONT = new Font("SansSerif", Font.PLAIN, 12); //$NON-NLS-1$
	String eventName;
	String whiteRating;
	String whiteName;
	String blackRating;
	String blackName;
	String result;
	String ECO;
	String plyMoves;
	String date;
	String round;
	String site;
	private int prefixOffset;
	private int resultOffset;
	private String whitePrefix;
	private String blackPrefix;
	private Dimension dimension;
		
	public PGNHeader(PGNGame pgnGame, Locale locale) {
		TagSection tagSection = pgnGame.getTagSection();
		this.eventName = getTag(tagSection, "[Event"); //$NON-NLS-1$
		this.whiteRating = getTag(tagSection, "[WhiteElo"); //$NON-NLS-1$
		this.whiteName = getTag(tagSection, "[White"); //$NON-NLS-1$
		this.blackRating = getTag(tagSection, "[BlackElo"); //$NON-NLS-1$
		this.blackName = getTag(tagSection, "[Black"); //$NON-NLS-1$
		this.result = getTag(tagSection, "[Result"); //$NON-NLS-1$
		this.ECO = getTag(tagSection, "[ECO"); //$NON-NLS-1$
		this.plyMoves = getTag(tagSection, "[PlyCount"); //$NON-NLS-1$
		if (this.plyMoves == null || this.plyMoves.length() == 0) {
			this.plyMoves = Integer.toString((pgnGame.getMoveText().getMoves().length + 1) / 2);
		}
		this.date = getTag(tagSection, "[Date"); //$NON-NLS-1$
		Date actualDate = null;
		try {
			if (this.date.indexOf('?') == -1) {
				actualDate = new SimpleDateFormat("yyyy.MM.dd", Locale.US).parse(this.date); //$NON-NLS-1$
				this.date = DateFormat.getDateInstance(DateFormat.LONG, locale).format(actualDate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.round = getTag(tagSection, "[Round"); //$NON-NLS-1$
		this.site = getTag(tagSection, "[Site"); //$NON-NLS-1$
		this.dimension = new Dimension(245, 75);
	}
	
	private String getTag(TagSection tagSection, String key) {
		String value = tagSection.getTag(key);
		return value == null ? EMPTY : value.substring(1, value.length() - 1);
	}
	
	public void paint(Graphics g) {
		update(g);
	}
	
	public void update(Graphics g) {
		g.setFont(PLAIN_FONT);
		FontMetrics plainFontMetrics = g.getFontMetrics();
		g.setFont(BOLD_FONT);
		FontMetrics boldFontMetrics = g.getFontMetrics();
		StringBuffer buffer = new StringBuffer();
		appendToBuffer(buffer, this.whiteRating, "    "); //$NON-NLS-1$
		appendToBuffer(buffer, "  "); //$NON-NLS-1$
		this.whitePrefix = buffer.toString();
		buffer = new StringBuffer();
		appendToBuffer(buffer, this.blackRating, "    "); //$NON-NLS-1$
		appendToBuffer(buffer, "  "); //$NON-NLS-1$
		this.blackPrefix = buffer.toString();
		this.prefixOffset = Math.max(plainFontMetrics.stringWidth(this.whitePrefix), plainFontMetrics.stringWidth(this.blackPrefix));
		this.resultOffset = boldFontMetrics.stringWidth(this.result);
		this.dimension = new Dimension(130 + this.resultOffset + plainFontMetrics.stringWidth(this.date), 75);
		setBackground(new Color(0xF1F1F1));

		buffer = new StringBuffer();		
		appendToBuffer(buffer, this.eventName);
		appendToBuffer(buffer, ", "); //$NON-NLS-1$
		appendToBuffer(buffer, this.site);
		appendToBuffer(buffer, " ("); //$NON-NLS-1$
		appendToBuffer(buffer, this.round);		
		appendToBuffer(buffer, ")"); //$NON-NLS-1$
		g.setFont(BOLD_FONT);
		g.drawString(buffer.toString(), 15, 15);
		g.drawRect(25, 20, 15, 15);
		g.setColor(Color.white);
		g.fillRect(26, 21, 13, 13);
		g.setColor(Color.black);
		g.setFont(PLAIN_FONT);
		g.drawString(this.whitePrefix, 49, 34);
		buffer = new StringBuffer();
		appendToBuffer(buffer, this.whiteName);
		g.setFont(BOLD_FONT);
		g.drawString(buffer.toString(), 49 + this.prefixOffset, 34);
		g.fillRect(25, 40, 15, 15);
		g.setFont(PLAIN_FONT);
		g.drawString(this.blackPrefix, 49, 53);
		g.setFont(BOLD_FONT);
		buffer = new StringBuffer();
		appendToBuffer(buffer, this.blackName);
		g.drawString(buffer.toString(), 49 + this.prefixOffset, 53);
		g.setFont(BOLD_FONT);
		g.drawString(this.result, 15, 70);
		g.setFont(PLAIN_FONT);
		buffer = new StringBuffer();
		appendToBuffer(buffer, this.plyMoves, "   "); //$NON-NLS-1$
		g.drawString(buffer.toString(), 35 + this.resultOffset, 70);
		buffer = new StringBuffer();
		appendToBuffer(buffer, this.ECO, "   "); //$NON-NLS-1$
		g.drawString(buffer.toString(), 75 + this.resultOffset, 70);
		g.drawString(this.date, 125 + this.resultOffset, 70);
	}

	private void appendToBuffer(StringBuffer buffer, String value, String subsitute) {
		if (value != null && value.length() != 0) {
			buffer.append(value);
		} else {
			buffer.append(subsitute);
		}
	}

	private void appendToBuffer(StringBuffer buffer, String value) {
		if (value != null) {
			buffer.append(value);
		}
	}
		
	/**
	 * @see java.awt.Component#getPreferredSize()
	 */
	public Dimension getPreferredSize() {
		return this.dimension;
	}

	/**
	 * @see java.awt.Component#getMinimumSize()
	 */
	public Dimension getMinimumSize() {
		return this.dimension;
	}

}
