package org.formulachess.chess.applet.pgn.iterator;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.formulachess.pgn.ast.Move;
import org.formulachess.pgn.ast.Variation;

public class MoveSelector extends Dialog {
	public static Dimension screenSize =
		Toolkit.getDefaultToolkit().getScreenSize();

	private List list;
	private int moveSelectionIndex;
	private Move[] moves;
	private WindowAdapter windowAdapter;
	private ItemListener itemListener;
	
	public MoveSelector(Move move) {
		super(new Frame(), "Select your next move", true); //$NON-NLS-1$
		setLayout(new GridBagLayout());
		Variation[] variations = null;
		if (move.isVariationFirstMove()) {
			variations = ((Move)move.getParent()).getVariations();
			move = (Move)move.getParent();
		} else {
			variations = move.getVariations();
		}
		int length = variations.length;
		this.moves = new Move[length + 1];
		this.moves[0] = move;
		for (int i = 0; i < length; i++) {
			Variation variation = variations[i];
			Move firstMove = variation.getMove(0);
			if (firstMove != null) {
				this.moves[i + 1] = firstMove;
			}
		}		
		add(this.list = new List(this.moves.length, false));
		for (int i = 0, max = this.moves.length; i < max; i++) {
			this.list.add(this.moves[i].getMoveNotation());
		}
		addWindowListener(this.windowAdapter = new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				windowClosure();
			}
		});
		this.list.addItemListener(this.itemListener = new ItemListener() {
			/**
			 * @see java.awt.event.ItemListener#itemStateChanged(ItemEvent)
			 */
			public void itemStateChanged(ItemEvent e) {
				itemChanged(e);
			}
		});
		pack();
		setLocation(
			(screenSize.width - getSize().width) / 2,
			(screenSize.height - getSize().height) / 2);
	}
	
	public int getMoveIndex() {
		return this.moveSelectionIndex;
	}
	
	void itemChanged(ItemEvent e) {
		this.moveSelectionIndex = this.list.getSelectedIndex();
		this.list.removeItemListener(this.itemListener);
		windowClosure();
	}

	public Move getMove() {
		return this.moves[this.moveSelectionIndex];
	}
	
	void windowClosure() {
		setVisible(false);
		removeWindowListener(this.windowAdapter);
		dispose();
	}
}
