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

public interface ASTVisitor {

	public default void endVisit(Castle castle) {
		// do nothing by default
	}

	public default void endVisit(Comment comment) {
		// do nothing by default
	}

	public default void endVisit(GameTermination termination) {
		// do nothing by default
	}

	public default void endVisit(Move move) {
		// do nothing by default
	}

	public default void endVisit(MoveText moveText) {
		// do nothing by default
	}

	public default void endVisit(PGNDatabase database) {
		// do nothing by default
	}

	public default void endVisit(PGNGame game) {
		// do nothing by default
	}

	public default void endVisit(TagPair pair) {
		// do nothing by default
	}

	public default void endVisit(TagSection section) {
		// do nothing by default
	}

	public default void endVisit(Variation variation) {
		// do nothing by default
	}

	public default boolean visit(Castle castle) {
		return true; // do nothing by default, keep traversing
	}

	public default boolean visit(Comment comment) {
		return true; // do nothing by default, keep traversing
	}

	public default boolean visit(GameTermination termination) {
		return true; // do nothing by default, keep traversing
	}
	
	public default boolean visit(Move move) {
		return true; // do nothing by default, keep traversing
	}
	
	public default boolean visit(MoveText moveText) {
		return true; // do nothing by default, keep traversing
	}

	public default boolean visit(PGNDatabase database) {
		return true; // do nothing by default, keep traversing
	}

	public default boolean visit(PGNGame game) {
		return true; // do nothing by default, keep traversing
	}

	public default boolean visit(TagPair pair) {
		return true; // do nothing by default, keep traversing
	}
	
	public default boolean visit(TagSection section) {
		return true; // do nothing by default, keep traversing
	}

	public default boolean visit(Variation variation) {
		return true; // do nothing by default, keep traversing
	}	
}
