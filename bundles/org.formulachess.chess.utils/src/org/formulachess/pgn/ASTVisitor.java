package org.formulachess.pgn;

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

public abstract class ASTVisitor {

	public void endVisit(Castle castle) {
		// do nothing by default
	}

	public void endVisit(Comment comment) {
		// do nothing by default
	}

	public void endVisit(GameTermination termination) {
		// do nothing by default
	}

	public void endVisit(Move move) {
		// do nothing by default
	}

	public void endVisit(MoveText moveText) {
		// do nothing by default
	}

	public void endVisit(PGNDatabase database) {
		// do nothing by default
	}

	public void endVisit(PGNGame game) {
		// do nothing by default
	}

	public void endVisit(TagPair pair) {
		// do nothing by default
	}

	public void endVisit(TagSection section) {
		// do nothing by default
	}

	public void endVisit(Variation variation) {
		// do nothing by default
	}

	public boolean visit(Castle castle) {
		return true; // do nothing by default, keep traversing
	}

	public boolean visit(Comment comment) {
		return true; // do nothing by default, keep traversing
	}

	public boolean visit(GameTermination termination) {
		return true; // do nothing by default, keep traversing
	}
	
	public boolean visit(Move move) {
		return true; // do nothing by default, keep traversing
	}
	
	public boolean visit(MoveText moveText) {
		return true; // do nothing by default, keep traversing
	}

	public boolean visit(PGNDatabase database) {
		return true; // do nothing by default, keep traversing
	}

	public boolean visit(PGNGame game) {
		return true; // do nothing by default, keep traversing
	}

	public boolean visit(TagPair pair) {
		return true; // do nothing by default, keep traversing
	}
	
	public boolean visit(TagSection section) {
		return true; // do nothing by default, keep traversing
	}

	public boolean visit(Variation variation) {
		return true; // do nothing by default, keep traversing
	}	
}
