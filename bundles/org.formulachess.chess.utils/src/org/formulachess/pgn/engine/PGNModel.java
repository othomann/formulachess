package org.formulachess.pgn.engine;

import java.util.Locale;

import org.formulachess.engine.ChessEngine;
import org.formulachess.pgn.ast.Move;
import org.formulachess.pgn.ast.PGNGame;

public class PGNModel {
	
	private String fenNotation;
	private ChessEngine model;
	public int currentMoveCounter;
	
	public PGNModel(PGNGame pgnGame, ChessEngine model) {
		if (pgnGame != null) {
			this.fenNotation = pgnGame.getTagSection().getTag("[FEN"); //$NON-NLS-1$
			if (this.fenNotation != null) {
				this.fenNotation = this.fenNotation.substring(1, this.fenNotation.length() - 1);
			}
		}
		this.model = model;
		this.currentMoveCounter = 0;
	}

	public void setIsReady(boolean value) {
		this.model.isReady = value;
	}
	
	public boolean isReady() {
		return this.model.isReady;
	}	
	
	public void playMovesTill(Move[] moves, int index) {
		if (index == -1) {
			if (this.fenNotation != null) {
				this.model.initialize(this.fenNotation);
			} else {
				this.model.initializeToStartingPosition();
			}
			this.model.update();
			this.currentMoveCounter = -1;
		} else if (this.currentMoveCounter != index || this.model.moveNumber != index) {
			if (this.fenNotation != null) {
				this.model.initialize(this.fenNotation);
			} else {
				this.model.initializeToStartingPosition();
			}
			for (int i = 0; i <= index; i++) {
				PGNMoveContainer container = new PGNMoveContainer(this.model, this.model.allMoves(), Locale.getDefault());
				String trimmedMoveNotation = trimmedMoveNotation(moves[i]);
				long move = container.get(trimmedMoveNotation);
				if (move != -1) {
					this.model.playMoveWithoutNotification(move);
				}
			}
			this.currentMoveCounter = index;
			this.model.update();
		}
	}
	
	private String trimmedMoveNotation(Move move) {
		String moveNotation = move.getMoveNotation();
		int index = moveNotation.indexOf('+');
		if (index != -1) {
			return moveNotation.substring(0, index);
		}
		index = moveNotation.indexOf('#');
		if (index != -1) {
			return moveNotation.substring(0, index);
		}
		return moveNotation;
	}
}
