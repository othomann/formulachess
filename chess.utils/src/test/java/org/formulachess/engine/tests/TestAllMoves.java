package org.formulachess.engine.tests;

import static org.formulachess.engine.Turn.BLACK_TURN;
import static org.formulachess.engine.Turn.WHITE_TURN;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.formulachess.engine.AbstractChessEngine;
import org.formulachess.engine.ChessEngine;
import org.formulachess.engine.Converter;
import org.formulachess.engine.Piece;
import org.formulachess.pgn.engine.PGNMoveContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestAllMoves {

	private static final String INITIAL_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"; //$NON-NLS-1$
	private static final String WRONG_MOVES = "Wrong moves"; //$NON-NLS-1$
	private static final String WRONG_VALUE = "Wrong value"; //$NON-NLS-1$
	private static Logger myLogger = Logger.getLogger(TestAllMoves.class.getCanonicalName());
	private static final String WRONG_SIZE = "wrong size"; //$NON-NLS-1$
	private static final boolean TOTAL = true;
	private static final boolean DEBUG = false;

	public long[] repeatAllMoves(ChessEngine model, int repetitions) {
		long[] moves = null;
		long time = System.currentTimeMillis();
		for (int i = 0; i < repetitions; i++) {
			moves = model.allMoves();
		}
		if (DEBUG) {
			myLogger.log(Level.SEVERE, () -> "spent " + (System.currentTimeMillis() - time) + "ms");//$NON-NLS-1$//$NON-NLS-2$
		}
		return moves;
	}

	public void display(String testName, ChessEngine model, long[] moves, String expectedMoves) {
		PGNMoveContainer pgnMoveContainer = new PGNMoveContainer(model, moves, Locale.FRANCE);
		if (DEBUG) {
			myLogger.log(Level.INFO,
					() -> "================ START " + testName + " ===================================="); //$NON-NLS-1$ //$NON-NLS-2$
			myLogger.log(Level.INFO, () -> model.toString());
			myLogger.log(Level.INFO, () -> pgnMoveContainer.toString());
		}
		assertEquals(expectedMoves, pgnMoveContainer.toString(), WRONG_MOVES);
		if (DEBUG) {
			myLogger.log(Level.INFO,
					() -> "================ END " + testName + " ===================================="); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	public void display(ChessEngine model, long[] moves) {
		if (DEBUG) {
			PGNMoveContainer pgnMoveContainer = new PGNMoveContainer(model, moves, Locale.FRANCE);
			myLogger.log(Level.INFO, () -> model.toString());
			myLogger.log(Level.INFO, () -> pgnMoveContainer.toString(false));
		}
	}

	private void displayTime(long time) {
		if (DEBUG) {
			myLogger.log(Level.INFO, () -> "spent " + (System.currentTimeMillis() - time) + "ms"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	private void playAllMoves(String fenNotation, ChessEngine model, long[] moves) {
		for (int i = 0, max = moves.length; i < max; i++) {
			final long move = moves[i];
			model.playMove(move);
			model.allMoves();
			model.undoMove(move);
			if (!fenNotation.equals(model.toFENNotation())) {
				PGNMoveContainer pgnMoveContainer = new PGNMoveContainer(model, moves, Locale.FRANCE);
				assertTrue(false,
						"different fen notation for move[" + i + "] = " + pgnMoveContainer.getMoveNotation(move));
			}
		}
	}

	@Test
	@DisplayName("test001")
	public void test001() {
		ChessEngine model = new ChessEngine("8/8/8/4K3/8/8/8/8 w - - 0 1");
		long[] moves = model.allMoves();
		assertEquals(8, moves.length, WRONG_SIZE);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < moves.length; i++) {
			builder.append(Converter.moveToString(model.getBoard(), moves[i], Locale.FRANCE) + " "); //$NON-NLS-1$
		}
		assertEquals("Rd6 Re6 Rf6 Rd5 Rf5 Rd4 Re4 Rf4 ", String.valueOf(builder), WRONG_MOVES); //$NON-NLS-1$

		builder = new StringBuilder();
		for (int i = 0; i < moves.length; i++) {
			builder.append(Converter.moveToString(model.getBoard(), moves[i], Locale.US) + " "); //$NON-NLS-1$
		}
		assertEquals("Kd6 Ke6 Kf6 Kd5 Kf5 Kd4 Ke4 Kf4 ", String.valueOf(builder), WRONG_MOVES); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test002")
	public void test002() {
		ChessEngine model = new ChessEngine("k7/8/8/4n3/8/8/8/8 b - - 0 1");
		long[] moves = model.allMoves();
		assertEquals(11, moves.length, WRONG_SIZE);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < moves.length; i++) {
			builder.append(Converter.moveToString(model.getBoard(), moves[i], Locale.FRANCE) + " "); //$NON-NLS-1$
		}
		assertEquals("Rb8 Ra7 Rb7 Cf3 Cd3 Cg4 Cc4 Cg6 Cc6 Cf7 Cd7 ", //$NON-NLS-1$
				String.valueOf(builder), WRONG_MOVES);
	}

	@Test
	@DisplayName("test003")
	public void test003() {
		ChessEngine model = new ChessEngine("k7/8/8/R3n3/8/8/8/8 b - - 0 1");
		long[] moves = model.allMoves();
		assertEquals(2, moves.length, WRONG_SIZE);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < moves.length; i++) {
			builder.append(Converter.moveToString(model.getBoard(), moves[i], Locale.FRANCE) + " "); //$NON-NLS-1$
		}
		assertEquals("Rb8 Rb7 ", String.valueOf(builder), WRONG_MOVES); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test004")
	public void test004() {
		ChessEngine model = new ChessEngine("k7/8/8/RR2n3/8/8/8/8 b - - 0 1");
		long[] moves = model.allMoves();
		assertEquals(0, moves.length, WRONG_SIZE);
	}

	@Test
	@DisplayName("test005")
	public void test005() {
		ChessEngine model = new ChessEngine("8/8/8/8/8/8/8/R3K2R w KQ - 0 1");
		long[] moves = model.allMoves();
		assertEquals(26, moves.length, WRONG_SIZE);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < moves.length; i++) {
			builder.append(Converter.moveToString(model.getBoard(), moves[i], Locale.FRANCE) + " "); //$NON-NLS-1$
		}
		assertEquals(
				"Ta2 Ta3 Ta4 Ta5 Ta6 Ta7 Ta8 Tb1 Tc1 Td1 Rd2 Re2 Rf2 Rd1 Rf1 O-O O-O-O Th2 Th3 Th4 Th5 Th6 Th7 Th8 Tg1 Tf1 ", //$NON-NLS-1$
				String.valueOf(builder), WRONG_MOVES);
	}

	@Test
	@DisplayName("test064")
	public void test006() {
		ChessEngine model = new ChessEngine("8/8/8/8/8/8/4n3/R3K2R w KQ - 0 1");
		long[] moves = model.allMoves();
		assertEquals(24, moves.length, WRONG_SIZE);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < moves.length; i++) {
			builder.append(Converter.moveToString(model.getBoard(), moves[i], Locale.FRANCE) + " "); //$NON-NLS-1$
		}
		assertEquals("Ta2 Ta3 Ta4 Ta5 Ta6 Ta7 Ta8 Tb1 Tc1 Td1 Rd2 Re2 Rf2 Rd1 Rf1 Th2 Th3 Th4 Th5 Th6 Th7 Th8 Tg1 Tf1 ", //$NON-NLS-1$
				String.valueOf(builder), WRONG_MOVES);
	}

	@Test
	@DisplayName("test007")
	public void test007() {
		ChessEngine model = new ChessEngine("8/8/8/8/8/1P6/8/4K3 w - - 0 1");
		long[] moves = model.allMoves();
		assertEquals(6, moves.length, WRONG_SIZE);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < moves.length; i++) {
			builder.append(Converter.moveToString(model.getBoard(), moves[i], Locale.FRANCE) + " "); //$NON-NLS-1$
		}
		assertEquals("b4 Rd2 Re2 Rf2 Rd1 Rf1 ", String.valueOf(builder), WRONG_MOVES); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test008")
	public void test008() {
		ChessEngine model = new ChessEngine("8/8/8/8/8/8/1P6/4K3 w - - 0 1");
		long[] moves = model.allMoves();
		assertEquals(7, moves.length, WRONG_SIZE);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < moves.length; i++) {
			builder.append(Converter.moveToString(model.getBoard(), moves[i], Locale.FRANCE) + " "); //$NON-NLS-1$
		}

		assertEquals("b3 b4 Rd2 Re2 Rf2 Rd1 Rf1 ", String.valueOf(builder), WRONG_MOVES); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test009")
	public void test009() {
		ChessEngine model = new ChessEngine("8/8/8/8/8/n7/1P6/4K3 w - - 0 1");
		model.setTurn(WHITE_TURN);
		long[] moves = model.allMoves();
		assertEquals(8, moves.length, WRONG_SIZE);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < moves.length; i++) {
			builder.append(Converter.moveToString(model.getBoard(), moves[i], Locale.FRANCE) + " "); //$NON-NLS-1$
		}
		assertEquals("b3 b4 a3 Rd2 Re2 Rf2 Rd1 Rf1 ", String.valueOf(builder), WRONG_MOVES); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test010")
	public void test010() {
		ChessEngine model = new ChessEngine("7k/8/8/8/8/n7/1P6/4K3 w - - 0 1");
		long[] moves = model.allMoves();
		assertEquals(8, moves.length, WRONG_SIZE);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < moves.length; i++) {
			builder.append(Converter.moveToString(model.getBoard(), moves[i], Locale.FRANCE) + " "); //$NON-NLS-1$
		}
		assertEquals("b3 b4 a3 Rd2 Re2 Rf2 Rd1 Rf1 ", String.valueOf(builder), WRONG_MOVES); //$NON-NLS-1$

		for (int i = 0, max = moves.length; i < max; i++) {
			model.playMove(moves[i]);
			model.undoMove(moves[i]);
			assertEquals(Piece.WHITE_KING, model.getBoard(60), "wrong white king position after move " + i); //$NON-NLS-1$
			assertEquals(Piece.BLACK_KING, model.getBoard(7), "wrong black king position after move " + i); //$NON-NLS-1$
			assertEquals(Piece.WHITE_PAWN, model.getBoard(49), "wrong white pawn position after move " + i); //$NON-NLS-1$
			assertEquals(Piece.BLACK_KNIGHT, model.getBoard(40), "wrong black knight position after move " + i); //$NON-NLS-1$
		}
	}

	@Test
	@DisplayName("test011")
	public void test011() {
		ChessEngine model = new ChessEngine(Locale.getDefault(), INITIAL_POSITION);
		long[] moves = model.allMoves();
		assertEquals(20, moves.length, WRONG_SIZE);
		playAllMoves(INITIAL_POSITION, model, moves);
		moves = model.allMoves();
		assertEquals(20, moves.length, WRONG_SIZE);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < moves.length; i++) {
			builder.append(Converter.moveToString(model.getBoard(), moves[i], Locale.FRANCE) + " "); //$NON-NLS-1$
		}
		assertEquals("a3 a4 b3 b4 c3 c4 d3 d4 e3 e4 f3 f4 g3 g4 h3 h4 Cc3 Ca3 Ch3 Cf3 ", //$NON-NLS-1$
				String.valueOf(builder), WRONG_MOVES);
	}

	@Test
	@DisplayName("test012")
	public void test012() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(30, moves.length, WRONG_SIZE);
		playAllMoves("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2", model, moves); //$NON-NLS-1$
		moves = repeatAllMoves(model, 1000);
		assertEquals(30, moves.length, WRONG_SIZE);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < moves.length; i++) {
			builder.append(Converter.moveToString(model.getBoard(), moves[i], Locale.FRANCE) + " "); //$NON-NLS-1$
		}
		assertEquals(
				"e5 a3 a4 b3 b4 c3 c4 d3 d4 f3 f4 g3 g4 h3 h4 Cc3 Ca3 De2 Df3 Dg4 Dh5 Re2 Fe2 Fd3 Fc4 Fb5 Fa6 Ce2 Ch3 Cf3 ", //$NON-NLS-1$
				String.valueOf(builder),
				WRONG_MOVES);
	}

	@Test
	@DisplayName("test013")
	public void test013() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r1bqkb1r/pppp1ppp/2n2n2/4p3/4P3/3P1N2/PPP2PPP/RNBQKB1R w KQkq - 0 1"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(31, moves.length, WRONG_SIZE);
		playAllMoves("r1bqkb1r/pppp1ppp/2n2n2/4p3/4P3/3P1N2/PPP2PPP/RNBQKB1R w KQkq - 0 1", model, moves); //$NON-NLS-1$
		display("test013", model, moves, //$NON-NLS-1$
				"Ca3 Cbd2 Cc3 Cd4 Cfd2 Cg1 Cg5 Ch4 Cxe5 Dd2 De2 Fd2 Fe2 Fe3 Ff4 Fg5 Fh6 Rd2 Re2 Tg1 a3 a4 b3 b4 c3 c4 d4 g3 g4 h3 h4 "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test014")
	public void test014() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r1bqkb1r/pppp1ppp/2n5/4p3/2n1P3/2NP1N2/PPPBBPPP/R2Q1RK1 b kq - 0 1"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(36, moves.length, WRONG_SIZE);
		playAllMoves("r1bqkb1r/pppp1ppp/2n5/4p3/2n1P3/2NP1N2/PPPBBPPP/R2Q1RK1 b kq - 0 1", model, moves); //$NON-NLS-1$
		display("test014", model, moves, //$NON-NLS-1$
				"C4a5 C6a5 Ca3 Cb4 Cb6 Cb8 Cd4 Cd6 Ce3 Ce7 Cxb2 Cxd2 De7 Df6 Dg5 Dh4 Fa3 Fb4 Fc5 Fd6 Fe7 Re7 Tb8 Tg8 a5 a6 b5 b6 d5 d6 f5 f6 g5 g6 h5 h6 "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test015")
	public void test015() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r1bqkb1r/ppp2ppp/2np1n2/1B2p3/4P3/3P1N2/PPP2PPP/RNBQK2R w KQkq - 0 1"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(37, moves.length, WRONG_SIZE);
		playAllMoves("r1bqkb1r/ppp2ppp/2np1n2/1B2p3/4P3/3P1N2/PPP2PPP/RNBQK2R w KQkq - 0 1", model, moves); //$NON-NLS-1$
		display("test015", model, moves, //$NON-NLS-1$
				"Ca3 Cbd2 Cc3 Cd4 Cfd2 Cg1 Cg5 Ch4 Cxe5 Dd2 De2 Fa4 Fa6 Fc4 Fd2 Fe3 Ff4 Fg5 Fh6 Fxc6 O-O Rd2 Re2 Rf1 Tf1 Tg1 a3 a4 b3 b4 c3 c4 d4 g3 g4 h3 h4 "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test016")
	public void test016() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r3k2r/pppq1ppp/2npbn2/1B2p1B1/1b2P3/2NP1N2/PPPQ1PPP/R3K2R w KQkq - 0 1"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(41, moves.length, WRONG_SIZE);
		playAllMoves("r3k2r/pppq1ppp/2npbn2/1B2p1B1/1b2P3/2NP1N2/PPPQ1PPP/R3K2R w KQkq - 0 1", model, moves); //$NON-NLS-1$
		display("test016", model, moves, //$NON-NLS-1$
				"Ca4 Cb1 Cd1 Cd4 Cd5 Ce2 Cg1 Ch4 Cxe5 Dc1 Dd1 De2 De3 Df4 Fa4 Fa6 Fc4 Fe3 Ff4 Fh4 Fh6 Fxc6 Fxf6 O-O O-O-O Rd1 Re2 Rf1 Tb1 Tc1 Td1 Tf1 Tg1 a3 a4 b3 d4 g3 g4 h3 h4 "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test017")
	public void test017() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r2q1rk1/p1pb2p1/3p4/1p3p1Q/7R/4n3/PP2BPPP/6K1 w - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(36, moves.length, WRONG_SIZE);
		playAllMoves("r2q1rk1/p1pb2p1/3p4/1p3p1Q/7R/4n3/PP2BPPP/6K1 w - - 0 0", model, moves); //$NON-NLS-1$
		display("test017", model, moves, //$NON-NLS-1$
				"De8 Df3 Df7 Dg4 Dg5 Dg6 Dh6 Dh7 Dh8 Dxf5 Fc4 Fd1 Fd3 Ff1 Ff3 Fg4 Fxb5 Rh1 Ta4 Tb4 Tc4 Td4 Te4 Tf4 Tg4 Th3 a3 a4 b3 b4 f3 f4 fxe3 g3 g4 h3 "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test018")
	public void test018() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r2q1rk1/p1pb2p1/3p4/1p3p1Q/4p2R/4n3/PP2BPPP/6K1 w - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(32, moves.length, WRONG_SIZE);
		playAllMoves("r2q1rk1/p1pb2p1/3p4/1p3p1Q/4p2R/4n3/PP2BPPP/6K1 w - - 0 0", model, moves); //$NON-NLS-1$
		display("test018", model, moves, //$NON-NLS-1$
				"De8 Df3 Df7 Dg4 Dg5 Dg6 Dh6 Dh7 Dh8 Dxf5 Fc4 Fd1 Fd3 Ff1 Ff3 Fg4 Fxb5 Rh1 Tf4 Tg4 Th3 Txe4 a3 a4 b3 b4 f3 f4 fxe3 g3 g4 h3 "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test019")
	public void test019() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r2q1rk1/p1pb2p1/3p4/1p3p1Q/4p2R/4n3/PPn1BPPP/6K1 w - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(32, moves.length, WRONG_SIZE);
		playAllMoves("r2q1rk1/p1pb2p1/3p4/1p3p1Q/4p2R/4n3/PPn1BPPP/6K1 w - - 0 0", model, moves); //$NON-NLS-1$
		display("test019", model, moves, //$NON-NLS-1$
				"De8 Df3 Df7 Dg4 Dg5 Dg6 Dh6 Dh7 Dh8 Dxf5 Fc4 Fd1 Fd3 Ff1 Ff3 Fg4 Fxb5 Rh1 Tf4 Tg4 Th3 Txe4 a3 a4 b3 b4 f3 f4 fxe3 g3 g4 h3 "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test020")
	public void test020() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r2q1rk1/p1pb2p1/3p4/1p3p1Q/4pP1R/4n3/PPn1B1PP/6K1 b - f3 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(36, moves.length, WRONG_SIZE);
		playAllMoves("r2q1rk1/p1pb2p1/3p4/1p3p1Q/4pP1R/4n3/PPn1B1PP/6K1 b - f3 0 0", model, moves); //$NON-NLS-1$
		display("test020", model, moves, //$NON-NLS-1$
				"Ca1 Ca3 Cb4 Cc4 Cd1 Cd4 Cd5 Ce1 Cf1 Cg4 Cxg2 Db8 Dc8 De7 De8 Df6 Dg5 Dxh4 Fc6 Fc8 Fe6 Fe8 Tb8 Tc8 Te8 Tf6 Tf7 a5 a6 b4 c5 c6 d5 exf3 g5 g6 "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test021")
	public void test021() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r2q1r2/p1pb1kpQ/3pR3/1p1n1p2/4R3/8/PP2BPPP/6K1 b - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(30, moves.length, WRONG_SIZE);
		playAllMoves("r2q1r2/p1pb1kpQ/3pR3/1p1n1p2/4R3/8/PP2BPPP/6K1 b - - 0 0", model, moves); //$NON-NLS-1$
		display("test021", model, moves, //$NON-NLS-1$
				"Cb4 Cb6 Cc3 Ce3 Ce7 Cf4 Cf6 Db8 Dc8 De7 De8 Df6 Dg5 Dh4 Fc6 Fc8 Fe8 Fxe6 Tb8 Tc8 Te8 Tg8 Th8 a5 a6 b4 c5 c6 f4 fxe4 "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test022")
	public void test022() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r2q1r2/p1pb1kpQ/3pR3/1p1n1p2/4R3/8/PP2BPPP/6K1 w - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(45, moves.length, WRONG_SIZE);
		playAllMoves("r2q1r2/p1pb1kpQ/3pR3/1p1n1p2/4R3/8/PP2BPPP/6K1 w - - 0 0", model, moves); //$NON-NLS-1$
		display("test022", model, moves, //$NON-NLS-1$
				"Dg6 Dg8 Dh3 Dh4 Dh5 Dh6 Dh8 Dxf5 Dxg7 Fc4 Fd1 Fd3 Ff1 Ff3 Fg4 Fh5 Fxb5 Rf1 Rh1 T4e5 T6e5 Ta4 Tb4 Tc4 Td4 Te3 Te7 Te8 Tf4 Tf6 Tg4 Tg6 Th4 Th6 Txd6 a3 a4 b3 b4 f3 f4 g3 g4 h3 h4 "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test023")
	public void test023() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r2q1r2/p1pb1kpQ/3pR3/1p1n1p2/4pP2/8/PP2B1PP/6K1 b - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(30, moves.length, WRONG_SIZE);
		playAllMoves("r2q1r2/p1pb1kpQ/3pR3/1p1n1p2/4pP2/8/PP2B1PP/6K1 b - - 0 0", model, moves); //$NON-NLS-1$
		display("test023", model, moves, //$NON-NLS-1$
				"Cb4 Cb6 Cc3 Ce3 Ce7 Cf6 Cxf4 Db8 Dc8 De7 De8 Df6 Dg5 Dh4 Fc6 Fc8 Fe8 Fxe6 Rxe6 Tb8 Tc8 Te8 Tg8 Th8 a5 a6 b4 c5 c6 e3 "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test024")
	public void test024() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r2q1r2/p1pb1kpQ/3pR3/1p1n1p2/8/5p2/PP2B1PP/6K1 b - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(33, moves.length, WRONG_SIZE);
		display(model, moves);
		playAllMoves("r2q1r2/p1pb1kpQ/3pR3/1p1n1p2/8/5p2/PP2B1PP/6K1 b - - 0 0", model, moves); //$NON-NLS-1$
		display("test024", model, moves, //$NON-NLS-1$
				"Cb4 Cb6 Cc3 Ce3 Ce7 Cf4 Cf6 Db8 Dc8 De7 De8 Df6 Dg5 Dh4 Fc6 Fc8 Fe8 Fxe6 Rxe6 Tb8 Tc8 Te8 Tg8 Th8 a5 a6 b4 c5 c6 f2 f4 fxe2 fxg2 "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test025")
	public void test025() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r2q1r2/p1pb1kpQ/3pR3/1p1n4/4p3/8/PP2BPPP/6K1 w - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(38, moves.length, WRONG_SIZE);
		playAllMoves("r2q1r2/p1pb1kpQ/3pR3/1p1n4/4p3/8/PP2BPPP/6K1 w - - 0 0", model, moves); //$NON-NLS-1$
		display("test025", model, moves, //$NON-NLS-1$
				"Df5 Dg6 Dg8 Dh3 Dh4 Dh5 Dh6 Dh8 Dxe4 Dxg7 Fc4 Fd1 Fd3 Ff1 Ff3 Fg4 Fh5 Fxb5 Rf1 Rh1 Te5 Te7 Te8 Tf6 Tg6 Th6 Txd6 Txe4 a3 a4 b3 b4 f3 f4 g3 g4 h3 h4 "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test026")
	public void test026() {
		String position = "r2q1r2/p1pb1kpQ/3pR3/1p1n4/4pP2/8/PP2B1PP/6K1 b - f3 0 0"; //$NON-NLS-1$
		ChessEngine model = new ChessEngine(Locale.getDefault(), position);
		long[] moves = model.allMoves();
		assertEquals(31, moves.length, WRONG_SIZE);
		playAllMoves(position, model, moves);
		display("test026", model, moves, //$NON-NLS-1$
				"Cb4 Cb6 Cc3 Ce3 Ce7 Cf6 Cxf4 Db8 Dc8 De7 De8 Df6 Dg5 Dh4 Fc6 Fc8 Fe8 Fxe6 Rxe6 Tb8 Tc8 Te8 Tg8 Th8 a5 a6 b4 c5 c6 e3 exf3 "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test027")
	public void test027() {
		String position = "r2q1r2/p1pb1kpQ/3pR3/1p1n4/4pP2/8/PP2B1PP/6K1 b - f3 0 0"; //$NON-NLS-1$
		ChessEngine model = new ChessEngine(Locale.getDefault(), position);
		assertEquals(position, // $NON-NLS-1$
				model.toFENNotation(), "different fen notation");
	}

	@Test
	@DisplayName("test028")
	public void test028() {
		if (TOTAL) {
			long time = System.currentTimeMillis();
			String position = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0"; //$NON-NLS-1$
			assertEquals(48, ChessEngine.perft(position, 1), WRONG_VALUE);
			assertEquals(2039, ChessEngine.perft(position, 2), WRONG_VALUE);
			assertEquals(97862, ChessEngine.perft(position, 3), WRONG_VALUE);
			assertEquals(4085603, ChessEngine.perft(position, 4), WRONG_VALUE);
			assertEquals(193690690, ChessEngine.perft(position, 5), WRONG_VALUE);
			displayTime(time);
		}
	}

	@Test
	@DisplayName("test029")
	public void test029() {
		if (TOTAL) {
			assertEquals(20, ChessEngine.perft(INITIAL_POSITION, 1), WRONG_VALUE);
			assertEquals(400, ChessEngine.perft(INITIAL_POSITION, 2), WRONG_VALUE);
			assertEquals(8902, ChessEngine.perft(INITIAL_POSITION, 3), WRONG_VALUE);
			assertEquals(197281, ChessEngine.perft(INITIAL_POSITION, 4), WRONG_VALUE);
			long time = System.currentTimeMillis();
			assertEquals(4865609, ChessEngine.perft(INITIAL_POSITION, 5), WRONG_VALUE);
			displayTime(time);
		}
	}

	@Test
	@DisplayName("test030")
	public void test030() {
		if (TOTAL) {
			String fenNotation = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0"; //$NON-NLS-1$
			assertEquals(48, ChessEngine.perft(fenNotation, 1), WRONG_VALUE);
			assertEquals(2039, ChessEngine.perft(fenNotation, 2), WRONG_VALUE);
			assertEquals(97862, ChessEngine.perft(fenNotation, 3), WRONG_VALUE);
			assertEquals(4085603, ChessEngine.perft(fenNotation, 4), WRONG_VALUE);
			long time = System.currentTimeMillis();
			assertEquals(193690690, ChessEngine.perft(fenNotation, 5), WRONG_VALUE);
			displayTime(time);
		}
	}

	@Test
	@DisplayName("test031")
	public void test031() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/P1N2Q2/1PPBBPpP/R3K2R w KQkq - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(48, moves.length, WRONG_SIZE);
		playAllMoves("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/P1N2Q2/1PPBBPpP/R3K2R w KQkq - 0 0", model, moves); //$NON-NLS-1$
		display("test031", model, moves, //$NON-NLS-1$
				"Ca2 Ca4 Cb1 Cb5 Cc4 Cc6 Cd1 Cd3 Cg4 Cxd7 Cxf7 Cxg6 Dd3 De3 Df4 Df5 Dg3 Dg4 Dh3 Dh5 Dxf6 Dxg2 Fb5 Fc1 Fc4 Fd1 Fd3 Fe3 Ff1 Ff4 Fg5 Fh6 Fxa6 O-O-O Rd1 Ta2 Tb1 Tc1 Td1 Tf1 Tg1 a4 axb4 b3 d6 dxe6 h3 h4 "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test020")
	public void test032() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r3k2r/p1ppqpb1/bnN1pnp1/3P4/1p2P3/2N2Q2/PPPBBPpP/R3K2R b KQkq - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(50, moves.length);
		playAllMoves("r3k2r/p1ppqpb1/bnN1pnp1/3P4/1p2P3/2N2Q2/PPPBBPpP/R3K2R b KQkq - 0 0", model, moves); //$NON-NLS-1$
		display("test032", model, moves, //$NON-NLS-1$
				"Ca4 Cbxd5 Cc4 Cc8 Cfxd5 Cg4 Cg8 Ch5 Ch7 Cxe4 Dc5 Dd6 Dd8 Df8 Fb5 Fb7 Fc4 Fc8 Fd3 Ff8 Fh6 Fxe2 O-O Rf8 Tb8 Tc8 Td8 Tf8 Tg8 Th3 Th4 Th5 Th6 Th7 Txh2 b3 bxc3 d6 dxc6 e5 exd5 g1=C g1=D g1=F g1=T g5 gxh1=C gxh1=D gxh1=F gxh1=T "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test033")
	public void test033() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPB1PPP/R2BK2R w KQkq - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(39, moves.length, WRONG_SIZE);
		playAllMoves("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPB1PPP/R2BK2R w KQkq - 0 0", model, moves); //$NON-NLS-1$
		display("test033", model, moves, //$NON-NLS-1$
				"Ca4 Cb1 Cb5 Cc4 Cc6 Cd3 Ce2 Cg4 Cxd7 Cxf7 Cxg6 Dd3 De2 De3 Df4 Df5 Dg3 Dg4 Dh5 Dxf6 Dxh3 Fc1 Fe2 Fe3 Ff4 Fg5 Fh6 Tb1 Tc1 Tf1 Tg1 a3 a4 b3 d6 dxe6 g3 g4 gxh3 "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test034")
	public void test034() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r3k2r/p2pqpb1/bn2pnp1/2pPN3/1p2P3/P1N2Q1p/1PPBBPPP/R3K2R w KQkq c6 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(51, moves.length, WRONG_SIZE);
		playAllMoves("r3k2r/p2pqpb1/bn2pnp1/2pPN3/1p2P3/P1N2Q1p/1PPBBPPP/R3K2R w KQkq c6 0 0", model, moves); //$NON-NLS-1$
		display("test034", model, moves, //$NON-NLS-1$
				"Ca2 Ca4 Cb1 Cb5 Cc4 Cc6 Cd1 Cd3 Cg4 Cxd7 Cxf7 Cxg6 Dd3 De3 Df4 Df5 Dg3 Dg4 Dh5 Dxf6 Dxh3 Fb5 Fc1 Fc4 Fd1 Fd3 Fe3 Ff1 Ff4 Fg5 Fh6 Fxa6 O-O O-O-O Rd1 Rf1 Ta2 Tb1 Tc1 Td1 Tf1 Tg1 a4 axb4 b3 d6 dxc6 dxe6 g3 g4 gxh3 "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test035")
	public void test035() {
		String fenPosition = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0"; //$NON-NLS-1$
		ChessEngine model = new ChessEngine(Locale.getDefault(), fenPosition);
		long[] moves = model.allMoves();
		assertEquals(48, moves.length, WRONG_SIZE);
		playAllMoves(fenPosition, model, moves);
		display("test035", model, moves, //$NON-NLS-1$
				"Ca4 Cb1 Cb5 Cc4 Cc6 Cd1 Cd3 Cg4 Cxd7 Cxf7 Cxg6 Dd3 De3 Df4 Df5 Dg3 Dg4 Dh5 Dxf6 Dxh3 Fb5 Fc1 Fc4 Fd1 Fd3 Fe3 Ff1 Ff4 Fg5 Fh6 Fxa6 O-O O-O-O Rd1 Rf1 Tb1 Tc1 Td1 Tf1 Tg1 a3 a4 b3 d6 dxe6 g3 g4 gxh3 "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test036")
	public void test036() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"1r2k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/5Q1p/PPPBBPPP/RN2K2R w KQk - 0 2"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(49, moves.length, WRONG_SIZE);
		playAllMoves("1r2k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/5Q1p/PPPBBPPP/RN2K2R w KQk - 0 2", model, moves); //$NON-NLS-1$
		display("test036", model, moves, //$NON-NLS-1$
				"Ca3 Cc3 Cc4 Cc6 Cd3 Cg4 Cxd7 Cxf7 Cxg6 Da3 Db3 Dc3 Dd3 De3 Df4 Df5 Dg3 Dg4 Dh5 Dxf6 Dxh3 Fb5 Fc1 Fc3 Fc4 Fd1 Fd3 Fe3 Ff1 Ff4 Fg5 Fh6 Fxa6 Fxb4 O-O Rd1 Rf1 Tf1 Tg1 a3 a4 b3 c3 c4 d6 dxe6 g3 g4 gxh3 "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test037")
	public void test037() {
		if (TOTAL) {
			String fenNotation = "1rb2rk1/p4ppp/1p1qp1n1/3n2N1/2pP4/2P3P1/PPQ2PBP/R1B1R1K1 w - - 0 1"; //$NON-NLS-1$
			assertEquals(1709, ChessEngine.perft(fenNotation, 2), WRONG_VALUE);
			assertEquals(73743, ChessEngine.perft(fenNotation, 3), WRONG_VALUE);
			long time = System.currentTimeMillis();
			assertEquals(2824658, ChessEngine.perft(fenNotation, 4), WRONG_VALUE);
			displayTime(time);
		}
	}

	@Test
	@DisplayName("test038")
	public void test038() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"4rk1r/pPp3p1/3q1n2/2b3p1/4p3/1B3bP1/PPPPNP1P/R1B1QRK1 b - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(52, moves.length, WRONG_SIZE);
		playAllMoves("4rk1r/pPp3p1/3q1n2/2b3p1/4p3/1B3bP1/PPPPNP1P/R1B1QRK1 b - - 0 0", model, moves); //$NON-NLS-1$
		display("test038", model, moves, //$NON-NLS-1$
				"Cd5 Cd7 Cg4 Cg8 Ch5 Ch7 Da6 Db6 Dc6 Dd3 Dd4 Dd5 Dd7 Dd8 De5 De6 De7 Df4 Dxd2 Dxg3 Fa3 Fb4 Fb6 Fd4 Fe3 Fg2 Fg4 Fh1 Fh5 Fxe2 Fxf2 Re7 Ta8 Tb8 Tc8 Td8 Te5 Te6 Te7 Tg8 Th3 Th4 Th5 Th6 Th7 Txh2 a5 a6 c6 e3 g4 g6 "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test039")
	public void test039() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r4k1r/pPp3p1/3q1n2/2b3p1/4p3/1B3bP1/PPPPNP1P/R1B1QRK1 b - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(51, moves.length, WRONG_SIZE);
		playAllMoves("r4k1r/pPp3p1/3q1n2/2b3p1/4p3/1B3bP1/PPPPNP1P/R1B1QRK1 b - - 0 0", model, moves); //$NON-NLS-1$
		display("test039", model, moves, //$NON-NLS-1$
				"Cd5 Cd7 Ce8 Cg4 Cg8 Ch5 Ch7 Da6 Db6 Dc6 Dd3 Dd4 Dd5 Dd7 Dd8 De5 De6 De7 Df4 Dxd2 Dxg3 Fa3 Fb4 Fb6 Fd4 Fe3 Fg2 Fg4 Fh1 Fh5 Fxe2 Fxf2 Re7 Re8 Tb8 Tc8 Td8 Te8 Tg8 Th3 Th4 Th5 Th6 Th7 Txh2 a5 a6 c6 e3 g4 g6 "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test040")
	public void test040() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r4k2/pPp3p1/3q4/2b3p1/4p1n1/1B3bP1/PPPPNP1K/R1B1QR2 w - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(2, moves.length, WRONG_SIZE);
		playAllMoves("r4k2/pPp3p1/3q4/2b3p1/4p1n1/1B3bP1/PPPPNP1K/R1B1QR2 w - - 0 0", model, moves); //$NON-NLS-1$
		display("test040", model, moves, "Rg1 Rh3 "); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test041")
	public void test041() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r4k2/pPp3p1/3q4/2b3p1/4p1n1/1B3bPK/PPPPNP2/R1B1QR2 b - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(43, moves.length, WRONG_SIZE);
		playAllMoves("r4k2/pPp3p1/3q4/2b3p1/4p1n1/1B3bPK/PPPPNP2/R1B1QR2 b - - 0 0", model, moves); //$NON-NLS-1$
		display("test041", model, moves, //$NON-NLS-1$
				"Ce3 Ce5 Cf6 Ch2 Ch6 Cxf2 Da6 Db6 Dc6 Dd3 Dd4 Dd5 Dd7 Dd8 De5 De6 De7 Df4 Df6 Dg6 Dh6 Dxd2 Dxg3 Fa3 Fb4 Fb6 Fd4 Fe3 Fg2 Fh1 Fxe2 Fxf2 Re7 Re8 Tb8 Tc8 Td8 Te8 a5 a6 c6 e3 g6 "); //$NON-NLS-1$
	}

	@Test
	@DisplayName("test042")
	public void test042() {
		ChessEngine model = new ChessEngine(Locale.getDefault(), "8/8/2NppRBb/1K1k3r/4n3/3np3/4N3/8 b - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals(27, moves.length, WRONG_SIZE);
		playAllMoves("8/8/2NppRBb/1K1k3r/4n3/3np3/4N3/8 b - - 0 0", model, moves); //$NON-NLS-1$
		display("test042", model, moves, //$NON-NLS-1$
				"Cb2 Cb4 Cc1 Cc3 Cd2 Cdc5 Cdf2 Ce1 Ce5 Cec5 Cef2 Cf4 Cg3 Cg5 Cxf6 Ff4 Ff8 Fg5 Fg7 Te5 Tf5 Tg5 Th1 Th2 Th3 Th4 e5 "); //$NON-NLS-1$
	}
}