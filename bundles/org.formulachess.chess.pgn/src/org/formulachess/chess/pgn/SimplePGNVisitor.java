package org.formulachess.chess.pgn;

import org.formulachess.chess.pgn.PGNParser.ElementContext;
import org.formulachess.chess.pgn.PGNParser.Element_sequenceContext;
import org.formulachess.chess.pgn.PGNParser.Game_terminationContext;
import org.formulachess.chess.pgn.PGNParser.Tag_pairContext;

public class SimplePGNVisitor extends PGNBaseVisitor<String> {
	@Override
	public String visitTag_pair(Tag_pairContext ctx) {
		System.out.print("[" + ctx.tag_name().getText());
		System.out.println(" " + ctx.tag_value().getText() + ']');
		return super.visitTag_pair(ctx);
	}

	@Override
	public String visitElement_sequence(Element_sequenceContext ctx) {
		for (ElementContext context : ctx.element()) {
			System.out.println(context.getText());
		}
		return "";
	}

	@Override
	public String visitGame_termination(Game_terminationContext ctx) {
		System.out.println(ctx.getText());
		return super.visitGame_termination(ctx);
	}
}
