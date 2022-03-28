package org.formulachess.pgn.engine;

import java.util.Locale;

import org.formulachess.engine.ChessEngine;
import org.formulachess.pgn.ast.Move;
import org.formulachess.pgn.ast.PGNGame;
import org.formulachess.pgn.ast.Player;
import org.formulachess.pgn.ast.TagSection;

public class PGNModel {

	private String fenNotation;
	private ChessEngine model;
	private int currentMoveCounter;
	private Player white;
	private Player black;
	private boolean isFischerRandom;

	public PGNModel(PGNGame pgnGame, ChessEngine model) {
		if (pgnGame != null) {
			this.fenNotation = pgnGame.getTagSection().getTag(TagSection.TAG_FEN); // $NON-NLS-1$
			if (this.fenNotation != null) {
				this.fenNotation = this.fenNotation.substring(1, this.fenNotation.length() - 1);
			}
			this.white = new Player(pgnGame.getTagSection().getTag(TagSection.TAG_WHITE));
			this.black = new Player(pgnGame.getTagSection().getTag(TagSection.TAG_BLACK));
			this.isFischerRandom = pgnGame.isFischerRandom();
		}
		this.model = model;
		this.currentMoveCounter = 0;
	}

	public int getCurrentMoveCounter() {
		return currentMoveCounter;
	}

	public void setIsReady(boolean value) {
		this.model.setIsReady(value);
	}

	public boolean isReady() {
		return this.model.isReady();
	}
	
	public Player getWhite() {
		return this.white;
	}

	public Player getBlack() {
		return this.black;
	}

	public void playMovesTill(Move[] moves, int index) {
		if (index == -1) {
			if (this.fenNotation != null) {
				this.model.initialize(this.fenNotation, this.isFischerRandom);
			} else {
				this.model.initializeToStartingPosition();
			}
			this.currentMoveCounter = -1;
		} else if (this.currentMoveCounter != index || this.model.getMoveNumber() != index) {
			if (this.fenNotation != null) {
				this.model.initialize(this.fenNotation, this.isFischerRandom);
			} else {
				this.model.initializeToStartingPosition();
			}
			for (int i = 0; i <= index; i++) {
				PGNMoveContainer container = new PGNMoveContainer(this.model, this.model.allMoves(), Locale.US);
				String trimmedMoveNotation = trimmedMoveNotation(moves[i]);
				long move = container.get(trimmedMoveNotation);
				if (move != -1) {
					this.model.playMoveWithoutNotification(move);
				}
			}
			this.currentMoveCounter = index;
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
