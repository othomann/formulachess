package org.formulachess.pgn.ast;

import org.formulachess.pgn.ASTVisitor;

public class PGNDatabase extends ASTNode {

	private static final int INITIAL_SIZE = 7;
	private PGNGame[] games = new PGNGame[INITIAL_SIZE];
	private int gamesCounter;

	public void addPGNGame(PGNGame pgnGame) {
		if (this.gamesCounter == this.games.length) {
			System.arraycopy(this.games, 0, this.games = new PGNGame[this.gamesCounter * 2], 0, this.gamesCounter);
		}
		this.games[this.gamesCounter++] = pgnGame;
	}

	public PGNGame[] getPGNGames() {
		if (this.gamesCounter != this.games.length) {
			System.arraycopy(this.games, 0, this.games = new PGNGame[this.gamesCounter], 0, this.gamesCounter);
		}
		return this.games;
	}

	public PGNGame getPGNGame(int i) {
		if (i >= this.gamesCounter) {
			return null;
		}
		return this.games[i];
	}

	public void concatPGNDatabase(PGNDatabase database) {
		PGNGame[] databaseGames = database.getPGNGames();
		PGNGame[] result = new PGNGame[this.gamesCounter + databaseGames.length];
		System.arraycopy(this.games, 0, result, 0, this.gamesCounter);
		System.arraycopy(databaseGames, 0, result, this.gamesCounter, databaseGames.length);
		this.games = result;
		this.gamesCounter += databaseGames.length;
	}

	@Override
	public void accept(ASTVisitor visitor) {
		if (visitor.visit(this)) {
			for (PGNGame game : this.getPGNGames()) {
				game.accept(visitor);
			}
		}
		visitor.endVisit(this);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (PGNGame game : this.getPGNGames()) {
			builder.append(game).append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		}
		return String.valueOf(builder);
	}

}
