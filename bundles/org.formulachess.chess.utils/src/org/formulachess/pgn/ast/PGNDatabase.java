package org.formulachess.pgn.ast;

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
	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0, max = this.gamesCounter; i < max; i++) {
			builder.append(this.games[i]).append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		}
		return String.valueOf(builder);
	}


}
