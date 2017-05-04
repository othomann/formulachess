package org.formulachess.engine.tests;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.Locale;

import org.formulachess.engine.AbstractChessEngine;
import org.formulachess.engine.ChessEngine;
import org.formulachess.engine.Piece;
import org.formulachess.pgn.engine.PGNMoveContainer;
import static org.formulachess.engine.Turn.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestAllMoves extends TestCase {

	static final private boolean TOTAL = false;
	static final private boolean DEBUG = false;
	static final private boolean DUMP_SYSOUT = false;

	public TestAllMoves(String name) {
		super(name);
	}

	PrintStream stream;

	protected void setUp() {
		if (DUMP_SYSOUT) {
			try {
				this.stream = new PrintStream(new FileOutputStream("d:/temp/testAllMoves.txt", true)); //$NON-NLS-1$
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return;
			}
			System.setOut(this.stream);
		}
	}

	protected void teardDown() {
		if (DUMP_SYSOUT) {
			if (this.stream != null) {
				this.stream.flush();
				this.stream.close();
			}
		}
	}

	public static Test suite() {
		return new TestSuite(TestAllMoves.class);
//		TestSuite suite = new TestSuite();
//		suite.addTest(new TestAllMoves("test013")); //$NON-NLS-1$
//		return suite;
	}

	public long[] repeatAllMoves(ChessEngine model, int repetitions) {
		long[] moves = null;
		long time = System.currentTimeMillis();
		for (int i = 0; i < repetitions; i++) {
			moves = model.allMoves();
		}
		if (DEBUG) {
			System.out.println("spent " + (System.currentTimeMillis() - time) + "ms"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return moves;
	}

	public void display(String testName, ChessEngine model, long[] moves, String expectedMoves) {
		PGNMoveContainer pgnMoveContainer = new PGNMoveContainer(model, moves, Locale.FRANCE);
		if (DEBUG) {
			System.out.println("================ START " + testName + " ===================================="); //$NON-NLS-1$ //$NON-NLS-2$
			System.out.println(model);
			System.out.println(pgnMoveContainer.toString());
		}
		assertEquals("Wrong moves", expectedMoves, pgnMoveContainer.toString()); //$NON-NLS-1$
		if (DEBUG) {
			System.out.println("================ END " + testName + " ===================================="); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	public void display(ChessEngine model, long[] moves) {
		if (DEBUG) {
			PGNMoveContainer pgnMoveContainer = new PGNMoveContainer(model, moves, Locale.FRANCE);
			System.out.println(model);
			System.out.println(pgnMoveContainer.toString(false));
		}
	}

	private void displayTime(long time) {
		if (DEBUG) {
			System.out.println("spent " + (System.currentTimeMillis() - time) + "ms"); //$NON-NLS-1$ //$NON-NLS-2$
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
				assertTrue("different fen notation for move[" + i + "] = " + pgnMoveContainer.getMoveNotation(move), //$NON-NLS-1$ //$NON-NLS-2$
						false);
			}
		}
	}

	public void test001() {
		ChessEngine model = new ChessEngine(Locale.getDefault());
		model.setBoard(AbstractChessEngine.getEmptyBoard());
		model.setBoard(28, Piece.WHITE_KING);
		model.setTurn(WHITE_TURN);
		long[] moves = model.allMoves();
		assertEquals("wrong size", 8, moves.length); //$NON-NLS-1$
		StringWriter stringWriter = null;
		try {
			stringWriter = new StringWriter();
			for (int i = 0; i < moves.length; i++) {
				stringWriter.write(Converter.moveToString(model.getBoard(), moves[i], Locale.FRANCE) + " "); //$NON-NLS-1$
			}
			stringWriter.flush();
			stringWriter.close();
		} catch (IOException e) {
			assertTrue("should not happen", false); //$NON-NLS-1$
		}
		assertNotNull(stringWriter);
		assertEquals("Wrong moves", "Rd6 Re6 Rf6 Rd5 Rf5 Rd4 Re4 Rf4 ", stringWriter.getBuffer().toString()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test002() {
		ChessEngine model = new ChessEngine(Locale.getDefault());
		model.setBoard(AbstractChessEngine.getEmptyBoard());
		model.setBoard(28, Piece.BLACK_KNIGHT);
		model.setBoard(0, Piece.BLACK_KING);
		model.setTurn(BLACK_TURN);
		model.setBlackCanCastleKingSide(false);
		model.setBlackCanCastleQueenSide(false);
		long[] moves = model.allMoves();
		assertEquals("wrong size", 11, moves.length); //$NON-NLS-1$
		StringWriter stringWriter = null;
		try {
			stringWriter = new StringWriter();
			for (int i = 0; i < moves.length; i++) {
				stringWriter.write(Converter.moveToString(model.getBoard(), moves[i], Locale.FRANCE) + " "); //$NON-NLS-1$
			}
			stringWriter.flush();
			stringWriter.close();
		} catch (IOException e) {
			assertTrue("should not happen", false); //$NON-NLS-1$
		}
		assertNotNull(stringWriter);
		assertEquals("Wrong moves", "Rb8 Ra7 Rb7 Cf3 Cd3 Cg4 Cc4 Cg6 Cc6 Cf7 Cd7 ", //$NON-NLS-1$ //$NON-NLS-2$
				stringWriter.getBuffer().toString());
	}

	public void test003() {
		ChessEngine model = new ChessEngine(Locale.getDefault());
		model.setBoard(AbstractChessEngine.getEmptyBoard());
		model.setBoard(28, Piece.BLACK_KNIGHT);
		model.setBoard(0, Piece.BLACK_KING);
		model.setBoard(24, Piece.WHITE_ROOK);
		model.setTurn(BLACK_TURN);
		model.setBlackCanCastleKingSide(false);
		model.setBlackCanCastleQueenSide(false);
		long[] moves = model.allMoves();
		assertEquals("wrong size", 2, moves.length); //$NON-NLS-1$
		StringWriter stringWriter = null;
		try {
			stringWriter = new StringWriter();
			for (int i = 0; i < moves.length; i++) {
				stringWriter.write(Converter.moveToString(model.getBoard(), moves[i], Locale.FRANCE) + " "); //$NON-NLS-1$
			}
			stringWriter.flush();
			stringWriter.close();
		} catch (IOException e) {
			assertTrue("should not happen", false); //$NON-NLS-1$
		}
		assertNotNull(stringWriter);
		assertEquals("Wrong moves", "Rb8 Rb7 ", stringWriter.getBuffer().toString()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test004() {
		ChessEngine model = new ChessEngine(Locale.getDefault());
		model.setBoard(AbstractChessEngine.getEmptyBoard());
		model.setBoard(28, Piece.BLACK_KNIGHT);
		model.setBoard(0, Piece.BLACK_KING);
		model.setBoard(24, Piece.WHITE_ROOK);
		model.setBoard(25, Piece.WHITE_ROOK);
		model.setTurn(BLACK_TURN);
		model.setBlackCanCastleKingSide(false);
		model.setBlackCanCastleQueenSide(false);
		long[] moves = model.allMoves();
		assertEquals("wrong size", 0, moves.length); //$NON-NLS-1$
	}

	public void test005() {
		ChessEngine model = new ChessEngine(Locale.getDefault());
		model.setBoard(AbstractChessEngine.getEmptyBoard());
		model.setBoard(60, Piece.WHITE_KING);
		model.setBoard(63, Piece.WHITE_ROOK);
		model.setBoard(56, Piece.WHITE_ROOK);
		model.setWhiteCanCastleKingSide(true);
		model.setWhiteCanCastleQueenSide(true);
		model.setTurn(WHITE_TURN);
		long[] moves = model.allMoves();
		assertEquals("wrong size", 26, moves.length); //$NON-NLS-1$
		StringWriter stringWriter = null;
		try {
			stringWriter = new StringWriter();
			for (int i = 0; i < moves.length; i++) {
				stringWriter.write(Converter.moveToString(model.getBoard(), moves[i], Locale.FRANCE) + " "); //$NON-NLS-1$
			}
			stringWriter.flush();
			stringWriter.close();
		} catch (IOException e) {
			assertTrue("should not happen", false); //$NON-NLS-1$
		}
		assertNotNull(stringWriter);
		assertEquals("Wrong moves", //$NON-NLS-1$
				"Ta2 Ta3 Ta4 Ta5 Ta6 Ta7 Ta8 Tb1 Tc1 Td1 Rd2 Re2 Rf2 Rd1 Rf1 Rg1 Rc1 Th2 Th3 Th4 Th5 Th6 Th7 Th8 Tg1 Tf1 ", //$NON-NLS-1$
				stringWriter.getBuffer().toString());
	}

	public void test006() {
		ChessEngine model = new ChessEngine(Locale.getDefault());
		model.setBoard(AbstractChessEngine.getEmptyBoard());
		model.setBoard(60, Piece.WHITE_KING);
		model.setBoard(63, Piece.WHITE_ROOK);
		model.setBoard(56, Piece.WHITE_ROOK);
		model.setBoard(52, Piece.BLACK_KNIGHT);
		model.setWhiteCanCastleKingSide(true);
		model.setWhiteCanCastleQueenSide(true);
		model.setTurn(WHITE_TURN);
		long[] moves = model.allMoves();
		assertEquals("wrong size", 24, moves.length); //$NON-NLS-1$
		StringWriter stringWriter = null;
		try {
			stringWriter = new StringWriter();
			for (int i = 0; i < moves.length; i++) {
				stringWriter.write(Converter.moveToString(model.getBoard(), moves[i], Locale.FRANCE) + " "); //$NON-NLS-1$
			}
			stringWriter.flush();
			stringWriter.close();
		} catch (IOException e) {
			assertTrue("should not happen", false); //$NON-NLS-1$
		}
		assertNotNull(stringWriter);
		assertEquals("Wrong moves", //$NON-NLS-1$
				"Ta2 Ta3 Ta4 Ta5 Ta6 Ta7 Ta8 Tb1 Tc1 Td1 Rd2 Re2 Rf2 Rd1 Rf1 Th2 Th3 Th4 Th5 Th6 Th7 Th8 Tg1 Tf1 ", //$NON-NLS-1$
				stringWriter.getBuffer().toString());
	}

	public void test007() {
		ChessEngine model = new ChessEngine(Locale.getDefault());
		model.setBoard(AbstractChessEngine.getEmptyBoard());
		model.setBoard(60, Piece.WHITE_KING);
		model.setBoard(41, Piece.WHITE_PAWN);
		model.setTurn(WHITE_TURN);
		long[] moves = model.allMoves();
		assertEquals("wrong size", 6, moves.length); //$NON-NLS-1$
		StringWriter stringWriter = null;
		try {
			stringWriter = new StringWriter();
			for (int i = 0; i < moves.length; i++) {
				stringWriter.write(Converter.moveToString(model.getBoard(), moves[i], Locale.FRANCE) + " "); //$NON-NLS-1$
			}
			stringWriter.flush();
			stringWriter.close();
		} catch (IOException e) {
			assertTrue("should not happen", false); //$NON-NLS-1$
		}
		assertNotNull(stringWriter);
		assertEquals("Wrong moves", "b4 Rd2 Re2 Rf2 Rd1 Rf1 ", stringWriter.getBuffer().toString()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test008() {
		ChessEngine model = new ChessEngine(Locale.getDefault());
		model.setBoard(AbstractChessEngine.getEmptyBoard());
		model.setBoard(60, Piece.WHITE_KING);
		model.setBoard(49, Piece.WHITE_PAWN);
		model.setTurn(WHITE_TURN);
		long[] moves = model.allMoves();
		assertEquals("wrong size", 7, moves.length); //$NON-NLS-1$
		StringWriter stringWriter = null;
		try {
			stringWriter = new StringWriter();
			for (int i = 0; i < moves.length; i++) {
				stringWriter.write(Converter.moveToString(model.getBoard(), moves[i], Locale.FRANCE) + " "); //$NON-NLS-1$
			}
			stringWriter.flush();
			stringWriter.close();
		} catch (IOException e) {
			assertTrue("should not happen", false); //$NON-NLS-1$
		}
		assertNotNull(stringWriter);
		assertEquals("Wrong moves", "b3 b4 Rd2 Re2 Rf2 Rd1 Rf1 ", stringWriter.getBuffer().toString()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test009() {
		ChessEngine model = new ChessEngine(Locale.getDefault());
		model.setBoard(AbstractChessEngine.getEmptyBoard());
		model.setBoard(60, Piece.WHITE_KING);
		model.setBoard(49, Piece.WHITE_PAWN);
		model.setBoard(40, Piece.BLACK_KNIGHT);
		model.setTurn(WHITE_TURN);
		long[] moves = model.allMoves();
		assertEquals("wrong size", 8, moves.length); //$NON-NLS-1$
		StringWriter stringWriter = null;
		try {
			stringWriter = new StringWriter();
			for (int i = 0; i < moves.length; i++) {
				stringWriter.write(Converter.moveToString(model.getBoard(), moves[i], Locale.FRANCE) + " "); //$NON-NLS-1$
			}
			stringWriter.flush();
			stringWriter.close();
		} catch (IOException e) {
			assertTrue("should not happen", false); //$NON-NLS-1$
		}
		assertNotNull(stringWriter);
		assertEquals("Wrong moves", "b3 b4 a3 Rd2 Re2 Rf2 Rd1 Rf1 ", stringWriter.getBuffer().toString()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test010() {
		ChessEngine model = new ChessEngine(Locale.getDefault());
		model.setBoard(AbstractChessEngine.getEmptyBoard());
		model.setBoard(60, Piece.WHITE_KING);
		model.setBoard(49, Piece.WHITE_PAWN);
		model.setBoard(40, Piece.BLACK_KNIGHT);
		model.setBoard(7, Piece.BLACK_KING);
		model.setTurn(WHITE_TURN);
		long[] moves = model.allMoves();
		assertEquals("wrong size", 8, moves.length); //$NON-NLS-1$
		StringWriter stringWriter = null;
		try {
			stringWriter = new StringWriter();
			for (int i = 0; i < moves.length; i++) {
				stringWriter.write(Converter.moveToString(model.getBoard(), moves[i], Locale.FRANCE) + " "); //$NON-NLS-1$
			}
			stringWriter.flush();
			stringWriter.close();
		} catch (IOException e) {
			assertTrue("should not happen", false); //$NON-NLS-1$
		}
		assertNotNull(stringWriter);
		assertEquals("Wrong moves", "b3 b4 a3 Rd2 Re2 Rf2 Rd1 Rf1 ", stringWriter.getBuffer().toString()); //$NON-NLS-1$ //$NON-NLS-2$

		for (int i = 0, max = moves.length; i < max; i++) {
			model.playMove(moves[i]);
			model.undoMove(moves[i]);
			assertEquals("wrong white king position after move " + i, Piece.WHITE_KING, model.getBoard(60)); //$NON-NLS-1$
			assertEquals("wrong black king position after move " + i, Piece.BLACK_KING, model.getBoard(7)); //$NON-NLS-1$
			assertEquals("wrong white pawn position after move " + i, Piece.WHITE_PAWN, model.getBoard(49)); //$NON-NLS-1$
			assertEquals("wrong black knight position after move " + i, Piece.BLACK_KNIGHT, model.getBoard(40)); //$NON-NLS-1$
		}
	}

	public void test011() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 20, moves.length); //$NON-NLS-1$
		playAllMoves("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", model, moves); //$NON-NLS-1$
		moves = model.allMoves();
		assertEquals("wrong size", 20, moves.length); //$NON-NLS-1$
		StringWriter stringWriter = null;
		try {
			stringWriter = new StringWriter();
			for (int i = 0; i < moves.length; i++) {
				stringWriter.write(Converter.moveToString(model.getBoard(), moves[i], Locale.FRANCE) + " "); //$NON-NLS-1$
			}
			stringWriter.flush();
			stringWriter.close();
		} catch (IOException e) {
			assertTrue("should not happen", false); //$NON-NLS-1$
		}
		assertNotNull(stringWriter);
		assertEquals("Wrong moves", "a3 a4 b3 b4 c3 c4 d3 d4 e3 e4 f3 f4 g3 g4 h3 h4 Cc3 Ca3 Ch3 Cf3 ", //$NON-NLS-1$ //$NON-NLS-2$
				stringWriter.getBuffer().toString());
	}

	public void test012() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 30, moves.length); //$NON-NLS-1$
		playAllMoves("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2", model, moves); //$NON-NLS-1$
		moves = repeatAllMoves(model, 1000);
		assertEquals("wrong size", 30, moves.length); //$NON-NLS-1$
		StringWriter stringWriter = null;
		try {
			stringWriter = new StringWriter();
			for (int i = 0; i < moves.length; i++) {
				stringWriter.write(Converter.moveToString(model.getBoard(), moves[i], Locale.FRANCE) + " "); //$NON-NLS-1$
			}
			stringWriter.flush();
			stringWriter.close();
		} catch (IOException e) {
			assertTrue("should not happen", false); //$NON-NLS-1$
		}
		assertNotNull(stringWriter);
		assertEquals("Wrong moves", //$NON-NLS-1$
				"e5 a3 a4 b3 b4 c3 c4 d3 d4 f3 f4 g3 g4 h3 h4 Cc3 Ca3 De2 Df3 Dg4 Dh5 Re2 Fe2 Fd3 Fc4 Fb5 Fa6 Ce2 Ch3 Cf3 ", //$NON-NLS-1$
				stringWriter.getBuffer().toString());
	}

	public void test013() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r1bqkb1r/pppp1ppp/2n2n2/4p3/4P3/3P1N2/PPP2PPP/RNBQKB1R w KQkq - 0 1"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 31, moves.length); //$NON-NLS-1$
		playAllMoves("r1bqkb1r/pppp1ppp/2n2n2/4p3/4P3/3P1N2/PPP2PPP/RNBQKB1R w KQkq - 0 1", model, moves); //$NON-NLS-1$
		display("test013", model, moves, //$NON-NLS-1$
				"Ca3 Cbd2 Cc3 Cd4 Cfd2 Cg1 Cg5 Ch4 Cxe5 Dd2 De2 Fd2 Fe2 Fe3 Ff4 Fg5 Fh6 Rd2 Re2 Tg1 a3 a4 b3 b4 c3 c4 d4 g3 g4 h3 h4 "); //$NON-NLS-1$
	}

	public void test014() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r1bqkb1r/pppp1ppp/2n5/4p3/2n1P3/2NP1N2/PPPBBPPP/R2Q1RK1 b kq - 0 1"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 36, moves.length); //$NON-NLS-1$
		playAllMoves("r1bqkb1r/pppp1ppp/2n5/4p3/2n1P3/2NP1N2/PPPBBPPP/R2Q1RK1 b kq - 0 1", model, moves); //$NON-NLS-1$
		display("test014", model, moves, //$NON-NLS-1$
				"C4a5 C6a5 Ca3 Cb4 Cb6 Cb8 Cd4 Cd6 Ce3 Ce7 Cxb2 Cxd2 De7 Df6 Dg5 Dh4 Fa3 Fb4 Fc5 Fd6 Fe7 Re7 Tb8 Tg8 a5 a6 b5 b6 d5 d6 f5 f6 g5 g6 h5 h6 "); //$NON-NLS-1$
	}

	public void test015() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r1bqkb1r/ppp2ppp/2np1n2/1B2p3/4P3/3P1N2/PPP2PPP/RNBQK2R w KQkq - 0 1"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 37, moves.length); //$NON-NLS-1$
		playAllMoves("r1bqkb1r/ppp2ppp/2np1n2/1B2p3/4P3/3P1N2/PPP2PPP/RNBQK2R w KQkq - 0 1", model, moves); //$NON-NLS-1$
		display("test015", model, moves, //$NON-NLS-1$
				"Ca3 Cbd2 Cc3 Cd4 Cfd2 Cg1 Cg5 Ch4 Cxe5 Dd2 De2 Fa4 Fa6 Fc4 Fd2 Fe3 Ff4 Fg5 Fh6 Fxc6 O-O Rd2 Re2 Rf1 Tf1 Tg1 a3 a4 b3 b4 c3 c4 d4 g3 g4 h3 h4 "); //$NON-NLS-1$
	}

	public void test016() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r3k2r/pppq1ppp/2npbn2/1B2p1B1/1b2P3/2NP1N2/PPPQ1PPP/R3K2R w KQkq - 0 1"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 41, moves.length); //$NON-NLS-1$
		playAllMoves("r3k2r/pppq1ppp/2npbn2/1B2p1B1/1b2P3/2NP1N2/PPPQ1PPP/R3K2R w KQkq - 0 1", model, moves); //$NON-NLS-1$
		display("test016", model, moves, //$NON-NLS-1$
				"Ca4 Cb1 Cd1 Cd4 Cd5 Ce2 Cg1 Ch4 Cxe5 Dc1 Dd1 De2 De3 Df4 Fa4 Fa6 Fc4 Fe3 Ff4 Fh4 Fh6 Fxc6 Fxf6 O-O O-O-O Rd1 Re2 Rf1 Tb1 Tc1 Td1 Tf1 Tg1 a3 a4 b3 d4 g3 g4 h3 h4 "); //$NON-NLS-1$
	}

	public void test017() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r2q1rk1/p1pb2p1/3p4/1p3p1Q/7R/4n3/PP2BPPP/6K1 w - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 36, moves.length); //$NON-NLS-1$
		playAllMoves("r2q1rk1/p1pb2p1/3p4/1p3p1Q/7R/4n3/PP2BPPP/6K1 w - - 0 0", model, moves); //$NON-NLS-1$
		display("test017", model, moves, //$NON-NLS-1$
				"De8 Df3 Df7 Dg4 Dg5 Dg6 Dh6 Dh7 Dh8 Dxf5 Fc4 Fd1 Fd3 Ff1 Ff3 Fg4 Fxb5 Rh1 Ta4 Tb4 Tc4 Td4 Te4 Tf4 Tg4 Th3 a3 a4 b3 b4 f3 f4 fxe3 g3 g4 h3 "); //$NON-NLS-1$
	}

	public void test018() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r2q1rk1/p1pb2p1/3p4/1p3p1Q/4p2R/4n3/PP2BPPP/6K1 w - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 32, moves.length); //$NON-NLS-1$
		playAllMoves("r2q1rk1/p1pb2p1/3p4/1p3p1Q/4p2R/4n3/PP2BPPP/6K1 w - - 0 0", model, moves); //$NON-NLS-1$
		display("test018", model, moves, //$NON-NLS-1$
				"De8 Df3 Df7 Dg4 Dg5 Dg6 Dh6 Dh7 Dh8 Dxf5 Fc4 Fd1 Fd3 Ff1 Ff3 Fg4 Fxb5 Rh1 Tf4 Tg4 Th3 Txe4 a3 a4 b3 b4 f3 f4 fxe3 g3 g4 h3 "); //$NON-NLS-1$
	}

	public void test019() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r2q1rk1/p1pb2p1/3p4/1p3p1Q/4p2R/4n3/PPn1BPPP/6K1 w - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 32, moves.length); //$NON-NLS-1$
		playAllMoves("r2q1rk1/p1pb2p1/3p4/1p3p1Q/4p2R/4n3/PPn1BPPP/6K1 w - - 0 0", model, moves); //$NON-NLS-1$
		display("test019", model, moves, //$NON-NLS-1$
				"De8 Df3 Df7 Dg4 Dg5 Dg6 Dh6 Dh7 Dh8 Dxf5 Fc4 Fd1 Fd3 Ff1 Ff3 Fg4 Fxb5 Rh1 Tf4 Tg4 Th3 Txe4 a3 a4 b3 b4 f3 f4 fxe3 g3 g4 h3 "); //$NON-NLS-1$
	}

	public void test020() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r2q1rk1/p1pb2p1/3p4/1p3p1Q/4pP1R/4n3/PPn1B1PP/6K1 b - f3 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 36, moves.length); //$NON-NLS-1$
		playAllMoves("r2q1rk1/p1pb2p1/3p4/1p3p1Q/4pP1R/4n3/PPn1B1PP/6K1 b - f3 0 0", model, moves); //$NON-NLS-1$
		display("test020", model, moves, //$NON-NLS-1$
				"Ca1 Ca3 Cb4 Cc4 Cd1 Cd4 Cd5 Ce1 Cf1 Cg4 Cxg2 Db8 Dc8 De7 De8 Df6 Dg5 Dxh4 Fc6 Fc8 Fe6 Fe8 Tb8 Tc8 Te8 Tf6 Tf7 a5 a6 b4 c5 c6 d5 exf3 g5 g6 "); //$NON-NLS-1$
	}

	public void test021() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r2q1r2/p1pb1kpQ/3pR3/1p1n1p2/4R3/8/PP2BPPP/6K1 b - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 30, moves.length); //$NON-NLS-1$
		playAllMoves("r2q1r2/p1pb1kpQ/3pR3/1p1n1p2/4R3/8/PP2BPPP/6K1 b - - 0 0", model, moves); //$NON-NLS-1$
		display("test021", model, moves, //$NON-NLS-1$
				"Cb4 Cb6 Cc3 Ce3 Ce7 Cf4 Cf6 Db8 Dc8 De7 De8 Df6 Dg5 Dh4 Fc6 Fc8 Fe8 Fxe6 Tb8 Tc8 Te8 Tg8 Th8 a5 a6 b4 c5 c6 f4 fxe4 "); //$NON-NLS-1$
	}

	public void test022() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r2q1r2/p1pb1kpQ/3pR3/1p1n1p2/4R3/8/PP2BPPP/6K1 w - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 45, moves.length); //$NON-NLS-1$
		playAllMoves("r2q1r2/p1pb1kpQ/3pR3/1p1n1p2/4R3/8/PP2BPPP/6K1 w - - 0 0", model, moves); //$NON-NLS-1$
		display("test022", model, moves, //$NON-NLS-1$
				"Dg6 Dg8 Dh3 Dh4 Dh5 Dh6 Dh8 Dxf5 Dxg7 Fc4 Fd1 Fd3 Ff1 Ff3 Fg4 Fh5 Fxb5 Rf1 Rh1 T4e5 T6e5 Ta4 Tb4 Tc4 Td4 Te3 Te7 Te8 Tf4 Tf6 Tg4 Tg6 Th4 Th6 Txd6 a3 a4 b3 b4 f3 f4 g3 g4 h3 h4 "); //$NON-NLS-1$
	}

	public void test023() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r2q1r2/p1pb1kpQ/3pR3/1p1n1p2/4pP2/8/PP2B1PP/6K1 b - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 30, moves.length); //$NON-NLS-1$
		playAllMoves("r2q1r2/p1pb1kpQ/3pR3/1p1n1p2/4pP2/8/PP2B1PP/6K1 b - - 0 0", model, moves); //$NON-NLS-1$
		display("test023", model, moves, //$NON-NLS-1$
				"Cb4 Cb6 Cc3 Ce3 Ce7 Cf6 Cxf4 Db8 Dc8 De7 De8 Df6 Dg5 Dh4 Fc6 Fc8 Fe8 Fxe6 Rxe6 Tb8 Tc8 Te8 Tg8 Th8 a5 a6 b4 c5 c6 e3 "); //$NON-NLS-1$
	}

	public void test024() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r2q1r2/p1pb1kpQ/3pR3/1p1n1p2/8/5p2/PP2B1PP/6K1 b - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 33, moves.length); //$NON-NLS-1$
		display(model, moves);
		playAllMoves("r2q1r2/p1pb1kpQ/3pR3/1p1n1p2/8/5p2/PP2B1PP/6K1 b - - 0 0", model, moves); //$NON-NLS-1$
		display("test024", model, moves, //$NON-NLS-1$
				"Cb4 Cb6 Cc3 Ce3 Ce7 Cf4 Cf6 Db8 Dc8 De7 De8 Df6 Dg5 Dh4 Fc6 Fc8 Fe8 Fxe6 Rxe6 Tb8 Tc8 Te8 Tg8 Th8 a5 a6 b4 c5 c6 f2 f4 fxe2 fxg2 "); //$NON-NLS-1$
	}

	public void test025() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r2q1r2/p1pb1kpQ/3pR3/1p1n4/4p3/8/PP2BPPP/6K1 w - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 38, moves.length); //$NON-NLS-1$
		playAllMoves("r2q1r2/p1pb1kpQ/3pR3/1p1n4/4p3/8/PP2BPPP/6K1 w - - 0 0", model, moves); //$NON-NLS-1$
		display("test025", model, moves, //$NON-NLS-1$
				"Df5 Dg6 Dg8 Dh3 Dh4 Dh5 Dh6 Dh8 Dxe4 Dxg7 Fc4 Fd1 Fd3 Ff1 Ff3 Fg4 Fh5 Fxb5 Rf1 Rh1 Te5 Te7 Te8 Tf6 Tg6 Th6 Txd6 Txe4 a3 a4 b3 b4 f3 f4 g3 g4 h3 h4 "); //$NON-NLS-1$
	}

	public void test026() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r2q1r2/p1pb1kpQ/3pR3/1p1n4/4pP2/8/PP2B1PP/6K1 b - f3 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 31, moves.length); //$NON-NLS-1$
		playAllMoves("r2q1r2/p1pb1kpQ/3pR3/1p1n4/4pP2/8/PP2B1PP/6K1 b - f3 0 0", model, moves); //$NON-NLS-1$
		display("test026", model, moves, //$NON-NLS-1$
				"Cb4 Cb6 Cc3 Ce3 Ce7 Cf6 Cxf4 Db8 Dc8 De7 De8 Df6 Dg5 Dh4 Fc6 Fc8 Fe8 Fxe6 Rxe6 Tb8 Tc8 Te8 Tg8 Th8 a5 a6 b4 c5 c6 e3 exf3 "); //$NON-NLS-1$
	}

	public void test027() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r2q1r2/p1pb1kpQ/3pR3/1p1n4/4pP2/8/PP2B1PP/6K1 b - f3 0 0"); //$NON-NLS-1$
		assertEquals("different fen notation", "r2q1r2/p1pb1kpQ/3pR3/1p1n4/4pP2/8/PP2B1PP/6K1 b - f3 0 0", //$NON-NLS-1$ //$NON-NLS-2$
				model.toFENNotation());
	}

	public void test028() {
		if (TOTAL) {
			long time = System.currentTimeMillis();
			assertEquals("Wrong value", 18, ChessEngine.perft("8/PPP4k/8/8/8/8/4Kppp/8 w - - 0 0", 1)); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("Wrong value", 290, ChessEngine.perft("8/PPP4k/8/8/8/8/4Kppp/8 w - - 0 0", 2)); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("Wrong value", 5044, ChessEngine.perft("8/PPP4k/8/8/8/8/4Kppp/8 w - - 0 0", 3)); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("Wrong value", 89363, ChessEngine.perft("8/PPP4k/8/8/8/8/4Kppp/8 w - - 0 0", 4)); //$NON-NLS-1$ //$NON-NLS-2$
			assertEquals("Wrong value", 1745545, ChessEngine.perft("8/PPP4k/8/8/8/8/4Kppp/8 w - - 0 0", 5)); //$NON-NLS-1$ //$NON-NLS-2$
			displayTime(time);
		}
	}

	public void test029() {
		if (TOTAL) {
			assertEquals("Wrong value", 20, //$NON-NLS-1$
					ChessEngine.perft("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 1)); //$NON-NLS-1$
			assertEquals("Wrong value", 400, //$NON-NLS-1$
					ChessEngine.perft("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 2)); //$NON-NLS-1$
			assertEquals("Wrong value", 8902, //$NON-NLS-1$
					ChessEngine.perft("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 3)); //$NON-NLS-1$
			assertEquals("Wrong value", 197281, //$NON-NLS-1$
					ChessEngine.perft("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 4)); //$NON-NLS-1$
			long time = System.currentTimeMillis();
			assertEquals("Wrong value", 4865609, //$NON-NLS-1$
					ChessEngine.perft("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 5)); //$NON-NLS-1$
			displayTime(time);
		}
	}

	public void test030() {
		if (TOTAL) {
			assertEquals("Wrong value", 48, //$NON-NLS-1$
					ChessEngine.perft("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0", 1)); //$NON-NLS-1$
			assertEquals("Wrong value", 2039, //$NON-NLS-1$
					ChessEngine.perft("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0", 2)); //$NON-NLS-1$
			assertEquals("Wrong value", 97862, //$NON-NLS-1$
					ChessEngine.perft("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0", 3)); //$NON-NLS-1$
			assertEquals("Wrong value", 4085603, //$NON-NLS-1$
					ChessEngine.perft("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0", 4)); //$NON-NLS-1$
			long time = System.currentTimeMillis();
			assertEquals("Wrong value", 193690690, //$NON-NLS-1$
					ChessEngine.perft("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0", 5)); //$NON-NLS-1$
			displayTime(time);
		}
	}

	public void test031() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/P1N2Q2/1PPBBPpP/R3K2R w KQkq - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 48, moves.length); //$NON-NLS-1$
		playAllMoves("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/P1N2Q2/1PPBBPpP/R3K2R w KQkq - 0 0", model, moves); //$NON-NLS-1$
		display("test031", model, moves, //$NON-NLS-1$
				"Ca2 Ca4 Cb1 Cb5 Cc4 Cc6 Cd1 Cd3 Cg4 Cxd7 Cxf7 Cxg6 Dd3 De3 Df4 Df5 Dg3 Dg4 Dh3 Dh5 Dxf6 Dxg2 Fb5 Fc1 Fc4 Fd1 Fd3 Fe3 Ff1 Ff4 Fg5 Fh6 Fxa6 O-O-O Rd1 Ta2 Tb1 Tc1 Td1 Tf1 Tg1 a4 axb4 b3 d6 dxe6 h3 h4 "); //$NON-NLS-1$
	}

	public void test032() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r3k2r/p1ppqpb1/bnN1pnp1/3P4/1p2P3/2N2Q2/PPPBBPpP/R3K2R b KQkq - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 50, moves.length); //$NON-NLS-1$
		playAllMoves("r3k2r/p1ppqpb1/bnN1pnp1/3P4/1p2P3/2N2Q2/PPPBBPpP/R3K2R b KQkq - 0 0", model, moves); //$NON-NLS-1$
		display("test032", model, moves, //$NON-NLS-1$
				"Ca4 Cbxd5 Cc4 Cc8 Cfxd5 Cg4 Cg8 Ch5 Ch7 Cxe4 Dc5 Dd6 Dd8 Df8 Fb5 Fb7 Fc4 Fc8 Fd3 Ff8 Fh6 Fxe2 O-O Rf8 Tb8 Tc8 Td8 Tf8 Tg8 Th3 Th4 Th5 Th6 Th7 Txh2 b3 bxc3 d6 dxc6 e5 exd5 g1=C g1=D g1=F g1=T g5 gxh1=C gxh1=D gxh1=F gxh1=T "); //$NON-NLS-1$
	}

	public void test033() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPB1PPP/R2BK2R w KQkq - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 39, moves.length); //$NON-NLS-1$
		playAllMoves("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPB1PPP/R2BK2R w KQkq - 0 0", model, moves); //$NON-NLS-1$
		display("test033", model, moves, //$NON-NLS-1$
				"Ca4 Cb1 Cb5 Cc4 Cc6 Cd3 Ce2 Cg4 Cxd7 Cxf7 Cxg6 Dd3 De2 De3 Df4 Df5 Dg3 Dg4 Dh5 Dxf6 Dxh3 Fc1 Fe2 Fe3 Ff4 Fg5 Fh6 Tb1 Tc1 Tf1 Tg1 a3 a4 b3 d6 dxe6 g3 g4 gxh3 "); //$NON-NLS-1$
	}

	public void test034() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r3k2r/p2pqpb1/bn2pnp1/2pPN3/1p2P3/P1N2Q1p/1PPBBPPP/R3K2R w KQkq c6 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 51, moves.length); //$NON-NLS-1$
		playAllMoves("r3k2r/p2pqpb1/bn2pnp1/2pPN3/1p2P3/P1N2Q1p/1PPBBPPP/R3K2R w KQkq c6 0 0", model, moves); //$NON-NLS-1$
		display("test034", model, moves, //$NON-NLS-1$
				"Ca2 Ca4 Cb1 Cb5 Cc4 Cc6 Cd1 Cd3 Cg4 Cxd7 Cxf7 Cxg6 Dd3 De3 Df4 Df5 Dg3 Dg4 Dh5 Dxf6 Dxh3 Fb5 Fc1 Fc4 Fd1 Fd3 Fe3 Ff1 Ff4 Fg5 Fh6 Fxa6 O-O O-O-O Rd1 Rf1 Ta2 Tb1 Tc1 Td1 Tf1 Tg1 a4 axb4 b3 d6 dxc6 dxe6 g3 g4 gxh3 "); //$NON-NLS-1$
	}

	public void test035() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 48, moves.length); //$NON-NLS-1$
		playAllMoves("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0", model, moves); //$NON-NLS-1$
		display("test035", model, moves, //$NON-NLS-1$
				"Ca4 Cb1 Cb5 Cc4 Cc6 Cd1 Cd3 Cg4 Cxd7 Cxf7 Cxg6 Dd3 De3 Df4 Df5 Dg3 Dg4 Dh5 Dxf6 Dxh3 Fb5 Fc1 Fc4 Fd1 Fd3 Fe3 Ff1 Ff4 Fg5 Fh6 Fxa6 O-O O-O-O Rd1 Rf1 Tb1 Tc1 Td1 Tf1 Tg1 a3 a4 b3 d6 dxe6 g3 g4 gxh3 "); //$NON-NLS-1$
	}

	public void test036() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"1r2k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/5Q1p/PPPBBPPP/RN2K2R w KQk - 0 2"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 49, moves.length); //$NON-NLS-1$
		playAllMoves("1r2k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/5Q1p/PPPBBPPP/RN2K2R w KQk - 0 2", model, moves); //$NON-NLS-1$
		display("test036", model, moves, //$NON-NLS-1$
				"Ca3 Cc3 Cc4 Cc6 Cd3 Cg4 Cxd7 Cxf7 Cxg6 Da3 Db3 Dc3 Dd3 De3 Df4 Df5 Dg3 Dg4 Dh5 Dxf6 Dxh3 Fb5 Fc1 Fc3 Fc4 Fd1 Fd3 Fe3 Ff1 Ff4 Fg5 Fh6 Fxa6 Fxb4 O-O Rd1 Rf1 Tf1 Tg1 a3 a4 b3 c3 c4 d6 dxe6 g3 g4 gxh3 "); //$NON-NLS-1$
	}

	public void test037() {
		if (TOTAL) {
			assertEquals("Wrong value", 1709, //$NON-NLS-1$
					ChessEngine.perft("1rb2rk1/p4ppp/1p1qp1n1/3n2N1/2pP4/2P3P1/PPQ2PBP/R1B1R1K1 w - - 0 1", 2)); //$NON-NLS-1$
			assertEquals("Wrong value", 73743, //$NON-NLS-1$
					ChessEngine.perft("1rb2rk1/p4ppp/1p1qp1n1/3n2N1/2pP4/2P3P1/PPQ2PBP/R1B1R1K1 w - - 0 1", 3)); //$NON-NLS-1$
			long time = System.currentTimeMillis();
			assertEquals("Wrong value", 2824658, //$NON-NLS-1$
					ChessEngine.perft("1rb2rk1/p4ppp/1p1qp1n1/3n2N1/2pP4/2P3P1/PPQ2PBP/R1B1R1K1 w - - 0 1", 4)); //$NON-NLS-1$
			displayTime(time);
		}
	}

	public void test038() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"4rk1r/pPp3p1/3q1n2/2b3p1/4p3/1B3bP1/PPPPNP1P/R1B1QRK1 b - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 52, moves.length); //$NON-NLS-1$
		playAllMoves("4rk1r/pPp3p1/3q1n2/2b3p1/4p3/1B3bP1/PPPPNP1P/R1B1QRK1 b - - 0 0", model, moves); //$NON-NLS-1$
		display("test038", model, moves, //$NON-NLS-1$
				"Cd5 Cd7 Cg4 Cg8 Ch5 Ch7 Da6 Db6 Dc6 Dd3 Dd4 Dd5 Dd7 Dd8 De5 De6 De7 Df4 Dxd2 Dxg3 Fa3 Fb4 Fb6 Fd4 Fe3 Fg2 Fg4 Fh1 Fh5 Fxe2 Fxf2 Re7 Ta8 Tb8 Tc8 Td8 Te5 Te6 Te7 Tg8 Th3 Th4 Th5 Th6 Th7 Txh2 a5 a6 c6 e3 g4 g6 "); //$NON-NLS-1$
	}

	public void test039() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r4k1r/pPp3p1/3q1n2/2b3p1/4p3/1B3bP1/PPPPNP1P/R1B1QRK1 b - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 51, moves.length); //$NON-NLS-1$
		playAllMoves("r4k1r/pPp3p1/3q1n2/2b3p1/4p3/1B3bP1/PPPPNP1P/R1B1QRK1 b - - 0 0", model, moves); //$NON-NLS-1$
		display("test039", model, moves, //$NON-NLS-1$
				"Cd5 Cd7 Ce8 Cg4 Cg8 Ch5 Ch7 Da6 Db6 Dc6 Dd3 Dd4 Dd5 Dd7 Dd8 De5 De6 De7 Df4 Dxd2 Dxg3 Fa3 Fb4 Fb6 Fd4 Fe3 Fg2 Fg4 Fh1 Fh5 Fxe2 Fxf2 Re7 Re8 Tb8 Tc8 Td8 Te8 Tg8 Th3 Th4 Th5 Th6 Th7 Txh2 a5 a6 c6 e3 g4 g6 "); //$NON-NLS-1$
	}

	public void test040() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r4k2/pPp3p1/3q4/2b3p1/4p1n1/1B3bP1/PPPPNP1K/R1B1QR2 w - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 2, moves.length); //$NON-NLS-1$
		playAllMoves("r4k2/pPp3p1/3q4/2b3p1/4p1n1/1B3bP1/PPPPNP1K/R1B1QR2 w - - 0 0", model, moves); //$NON-NLS-1$
		display("test040", model, moves, "Rg1 Rh3 "); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void test041() {
		ChessEngine model = new ChessEngine(Locale.getDefault(),
				"r4k2/pPp3p1/3q4/2b3p1/4p1n1/1B3bPK/PPPPNP2/R1B1QR2 b - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 43, moves.length); //$NON-NLS-1$
		playAllMoves("r4k2/pPp3p1/3q4/2b3p1/4p1n1/1B3bPK/PPPPNP2/R1B1QR2 b - - 0 0", model, moves); //$NON-NLS-1$
		display("test041", model, moves, //$NON-NLS-1$
				"Ce3 Ce5 Cf6 Ch2 Ch6 Cxf2 Da6 Db6 Dc6 Dd3 Dd4 Dd5 Dd7 Dd8 De5 De6 De7 Df4 Df6 Dg6 Dh6 Dxd2 Dxg3 Fa3 Fb4 Fb6 Fd4 Fe3 Fg2 Fh1 Fxe2 Fxf2 Re7 Re8 Tb8 Tc8 Td8 Te8 a5 a6 c6 e3 g6 "); //$NON-NLS-1$
	}

	public void test042() {
		ChessEngine model = new ChessEngine(Locale.getDefault(), "8/8/2NppRBb/1K1k3r/4n3/3np3/4N3/8 b - - 0 0"); //$NON-NLS-1$
		long[] moves = model.allMoves();
		assertEquals("wrong size", 27, moves.length); //$NON-NLS-1$
		playAllMoves("8/8/2NppRBb/1K1k3r/4n3/3np3/4N3/8 b - - 0 0", model, moves); //$NON-NLS-1$
		display("test042", model, moves, //$NON-NLS-1$
				"Cb2 Cb4 Cc1 Cc3 Cd2 Cdc5 Cdf2 Ce1 Ce5 Cec5 Cef2 Cf4 Cg3 Cg5 Cxf6 Ff4 Ff8 Fg5 Fg7 Te5 Tf5 Tg5 Th1 Th2 Th3 Th4 e5 "); //$NON-NLS-1$
	}
}