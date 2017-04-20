package org.formulachess.chess.applet.pgn.iterator;

import java.util.Vector;

import org.formulachess.pgn.ast.Move;
import org.formulachess.pgn.ast.PGNGame;
import org.formulachess.pgn.ast.Variation;

public class PGNGameWalker {

	private Move forkingMove;
	private PGNGame pgnGame;

	public PGNGameWalker(PGNGame pgnGame, Move forkingMove) {
		this.pgnGame = pgnGame;
		this.forkingMove = forkingMove;
	}
	
	public Move[] getMoves() {
		Move[] pathFromRoot = this.forkingMove.pathFromRoot();
		int length = pathFromRoot.length;
		int index = 0;
		Move[] currentMoves = this.pgnGame.getMoveText().getMoves();
		Vector resultingMoves = new Vector(length);
		Move moveWithVariations = null;
		for (int i = 0, max = pathFromRoot.length - 1; i < max; i++) {
			resultingMoves.addElement(pathFromRoot[i]);
			if (pathFromRoot[i].isVariationFirstMove()) {
				Variation[] variations = ((Move)pathFromRoot[i].getParent()).getVariations();
				for (int j = 0, max2 = variations.length; j < max2; j++) {
					Move[] variationMoves = variations[j].getMoves();
					if (variationMoves.length >= 1) {
						if (variationMoves[0].equals(pathFromRoot[i])) {
							currentMoves = variationMoves;
							index = 1;
						}
					}
				}
			} else {
				index++;
			}
		}
		resultingMoves.addElement(this.forkingMove);

		if (this.forkingMove.isVariationFirstMove()) {
			moveWithVariations = (Move) this.forkingMove.getParent();
			Variation[] variations = moveWithVariations.getVariations();
			for (int i = 0, max = variations.length; i < max; i++) {
				Move[] variationMoves = variations[i].getMoves();
				if (variationMoves.length >= 1) {
					if (variationMoves[0].equals(this.forkingMove)) {
						for (int j = 1, max2 = variationMoves.length; j < max2; j++) {
							resultingMoves.addElement(variationMoves[j]);
						}
					}
				}
			}
		} else {
			moveWithVariations = this.forkingMove;
			for (int i = index + 1, max = currentMoves.length; i < max; i++) {
				resultingMoves.addElement(currentMoves[i]);
			}
		}
		Move[] result = new Move[resultingMoves.size()];
		resultingMoves.copyInto(result);
		return result;
	}
}
