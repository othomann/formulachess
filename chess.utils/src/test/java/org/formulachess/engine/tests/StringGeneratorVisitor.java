package org.formulachess.engine.tests;

import org.formulachess.pgn.ASTVisitor;
import org.formulachess.pgn.ast.Castle;
import org.formulachess.pgn.ast.Comment;
import org.formulachess.pgn.ast.GameTermination;
import org.formulachess.pgn.ast.Move;
import org.formulachess.pgn.ast.MoveText;
import org.formulachess.pgn.ast.PGNDatabase;
import org.formulachess.pgn.ast.PGNGame;
import org.formulachess.pgn.ast.TagPair;
import org.formulachess.pgn.ast.TagSection;
import org.formulachess.pgn.ast.Variation;

public class StringGeneratorVisitor extends ASTVisitor {
	
	StringBuilder builder = new StringBuilder();

	@Override
	public boolean visit(Castle castle) {
		this.builder.append(castle);
		return true;
	}

	@Override
	public boolean visit(Comment comment) {
		this.builder.append(comment);
		return true;
	}

	@Override
	public boolean visit(GameTermination termination) {
		this.builder.append(termination);
		return true;
	}

	@Override
	public boolean visit(Move move) {
		this.builder.append(move);
		return true;
	}

	@Override
	public boolean visit(MoveText moveText) {
		this.builder.append(moveText);
		return true;
	}

	@Override
	public boolean visit(PGNDatabase database) {
		this.builder.append(database);
		return true;
	}

	@Override
	public boolean visit(PGNGame game) {
		this.builder.append(game);
		return true;
	}

	@Override
	public boolean visit(TagPair pair) {
		this.builder.append(pair);
		return true;
	}

	@Override
	public boolean visit(TagSection section) {
		this.builder.append(section);
		return true;
	}

	@Override
	public boolean visit(Variation variation) {
		this.builder.append(variation);
		return true;
	}

	public String getOutput() {
		return String.valueOf(this.builder);
	}
}
