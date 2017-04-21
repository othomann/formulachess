package org.formulachess.chess.applet.ui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.formulachess.engine.BoardConstants;

public class PromotionDialog extends Dialog {
	public static Dimension screenSize =
		Toolkit.getDefaultToolkit().getScreenSize();
	private int promotionIndex;

	public PromotionDialog(Frame frame, String sets, int color) {
		super(frame, "Pawn promotion", true); //$NON-NLS-1$
		initImageButtons(sets, color);
		pack();
		setLocation(
			(screenSize.width - getSize().width) / 2,
			(screenSize.height - getSize().height) / 2);
		addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				setVisible(false);
				removeWindowListener(this);
				dispose();
			}
		});
	}

	public void initImageButtons(String sets, int color) {
		Panel panel = new Panel();
		if (color == BoardConstants.WHITE_TURN) {
			panel.add(
				new PromotionImageButton(
					getClass().getResourceAsStream("./" + sets + "/wq.gif"), //$NON-NLS-1$ //$NON-NLS-2$
					0,
					this));
			// dame
			panel.add(
				new PromotionImageButton(
					getClass().getResourceAsStream("./" + sets + "/wr.gif"), //$NON-NLS-1$ //$NON-NLS-2$
					1,
					this));
			// tour
			panel.add(
				new PromotionImageButton(
					getClass().getResourceAsStream("./" + sets + "/wn.gif"), //$NON-NLS-1$ //$NON-NLS-2$
					2,
					this));
			// cavalier
			panel.add(
				new PromotionImageButton(
					getClass().getResourceAsStream("./" + sets + "/wb.gif"), //$NON-NLS-1$ //$NON-NLS-2$
					3,
					this));
			// fou
		} else {
			panel.add(
				new PromotionImageButton(
					getClass().getResourceAsStream("./" + sets + "/bq.gif"), //$NON-NLS-1$ //$NON-NLS-2$
					0,
					this));
			// dame
			panel.add(
				new PromotionImageButton(
					getClass().getResourceAsStream("./" + sets + "/br.gif"), //$NON-NLS-1$ //$NON-NLS-2$
					1,
					this));
			// tour
			panel.add(
				new PromotionImageButton(
					getClass().getResourceAsStream("./" + sets + "/bn.gif"),  //$NON-NLS-1$//$NON-NLS-2$
					2,
					this));
			// cavalier
			panel.add(
				new PromotionImageButton(
					getClass().getResourceAsStream("./" + sets + "/bb.gif"), //$NON-NLS-1$ //$NON-NLS-2$
					3,
					this));
			// fou
		}
		add(panel);
	}

	public void setPromotionIndex(int promotionIndex) {
		this.promotionIndex = promotionIndex;
	}

	public int getPromotionIndex() {
		return this.promotionIndex;
	}
}
