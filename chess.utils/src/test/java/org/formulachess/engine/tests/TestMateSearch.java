package org.formulachess.engine.tests;

import static org.formulachess.engine.Turn.WHITE_TURN;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.formulachess.engine.ChessEngine;
import org.formulachess.engine.MateNode;
import org.formulachess.engine.MateSearch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestMateSearch {
	private static final Logger MyLogger = Logger.getLogger(TestMateSearch.class.getCanonicalName());
	static int counter = 1;

	private static final boolean DEBUG = false;
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");//$NON-NLS-1$
	public static final String NEW_PGN_HEADER = "[Site \"?\"]\r\n" + //$NON-NLS-1$
			"[Date \"????.??.??\"]\n" + //$NON-NLS-1$
			"[Round \"?\"]\n" + //$NON-NLS-1$
			"[White \"?\"]\n" + //$NON-NLS-1$
			"[Black \"?\"]"; //$NON-NLS-1$

	static boolean ignoreLongTest() {
		String longRunProperty = System.getProperty("IGNORE_LONG_RUN"); //$NON-NLS-1$
		if (longRunProperty != null) {
			return Boolean.getBoolean(longRunProperty);
		}
		// default is to ignore mate that needs more than 5 moves
		return true;
	}

	static String display(int i) {
		if (i < 10) {
			return "00" + i; //$NON-NLS-1$
		}
		if (i < 100) {
			return "0" + i; //$NON-NLS-1$
		}
		return Integer.toString(i);
	}

	public void checkMate(String testName, String fenNotation, int maxDepth) {
		if (ignoreLongTest() && maxDepth > 5) {
			MyLogger.log(Level.INFO, () -> "Skipping test: " + testName); //$NON-NLS-1$
			return;
		}
		ChessEngine model = new ChessEngine(fenNotation);
		long time = System.currentTimeMillis();
		MateNode root = MateNode.newRoot(model.getTurn());
		assertTrue(MateSearch.searchMate(model, 1, maxDepth, root), "No mate found"); //$NON-NLS-1$
		time = System.currentTimeMillis() - time;
		StringBuilder buffer = new StringBuilder();
		if (DEBUG) {
			buffer.append("spent ").append(time).append(LINE_SEPARATOR); //$NON-NLS-1$
			buffer.append(root).append(LINE_SEPARATOR);
		}
		buffer.append("[Event \"") //$NON-NLS-1$
				.append(testName).append("\"]") //$NON-NLS-1$
				.append(LINE_SEPARATOR).append(NEW_PGN_HEADER).append(LINE_SEPARATOR).append("[Result "); //$NON-NLS-1$
		if (model.getTurn() == WHITE_TURN) {
			buffer.append("\"1-0\"]").append(LINE_SEPARATOR); //$NON-NLS-1$
		} else {
			buffer.append("\"0-1\"]").append(LINE_SEPARATOR); //$NON-NLS-1$
		}
		buffer.append("[SetUp \"1\"]").append(LINE_SEPARATOR); //$NON-NLS-1$
		buffer.append("[FEN \"") //$NON-NLS-1$
				.append(fenNotation).append("\"]") //$NON-NLS-1$
				.append(LINE_SEPARATOR).append(LINE_SEPARATOR).append(root.generatePGN()).append(LINE_SEPARATOR);
		if (model.getTurn() == WHITE_TURN) {
			buffer.append("1-0").append(LINE_SEPARATOR); //$NON-NLS-1$
		} else {
			buffer.append("0-1").append(LINE_SEPARATOR); //$NON-NLS-1$
		}
		if (DEBUG) {
			MyLogger.log(Level.INFO, () -> String.valueOf(buffer));
		}
		try (PrintWriter writer = new PrintWriter(new FileWriter("solutions2.pgn", true))) { //$NON-NLS-1$
			writer.println(String.valueOf(buffer));
			writer.flush();
		} catch (IOException e) {
			MyLogger.log(Level.SEVERE, e, () -> "Could not save mate"); //$NON-NLS-1$
		}
	}
	@Test
	@DisplayName("test001")
	public void test001() {
		checkMate("Position001", "3nkr2/3Rb1pp/p1B1ppn1/1p4P1/7P/6Q1/PPPNq3/1K6 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test002")
	public void test002() {
		checkMate("Position002", "8/5RRp/4k3/1p1Np3/1P2p3/3nP2P/5P1K/1q6 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test003")
	public void test003() {
		checkMate("Position003", "8/8/6pk/3Q4/5K2/8/8/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test004")
	public void test004() {
		checkMate("Position004", "3k4/1P6/1KB3r1/2NN1p2/8/8/7b/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test005")
	public void test005() {
		checkMate("Position005", "r2qr2k/pbpp2pp/1p5N/3Q2b1/2P1P3/P7/1PP2PPP/R4RK1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test006")
	public void test006() {
		checkMate("Position006", "r1bq2k1/ppp2r1p/2np1pNQ/2bNpp2/2B1P3/3P4/PPP2PPP/R3K2R w KQ - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test007")
	public void test007() {
		checkMate("Position007", "r1bk3r/1pp2ppp/pb1p1n2/n2P4/B3P1q1/2Q2N2/PB3PPP/RN3RK1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test008")
	public void test008() {
		checkMate("Position008", "r2q1k1r/ppp1n1Np/1bnpB2B/8/1P1pb1P1/2P4P/P4P2/RN1Q1RK1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test009")
	public void test009() {
		checkMate("Position009", "r1b2rk1/ppp2p1p/1b1p1B2/5q1Q/2Bp4/2P5/PP3PPP/R3R1K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test010")
	public void test010() {
		checkMate("Position010", "1rqk3r/p1p2ppp/2Q1b3/3pN3/3P4/B7/P4PPP/b3R1K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test011")
	public void test011() {
		checkMate("Position011", "r2k1b1Q/pppn3p/3p4/1B5n/5pb1/5N2/PPPP1qPP/RNBKR3 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test012")
	public void test012() {
		checkMate("Position012", "r4r2/pQ3ppp/2np4/2bk4/5P2/6P1/PPP5/R1B1KB1q w Q - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test013")
	public void test013() {
		checkMate("Position013", "rnbqr2k/ppppn1p1/1b5p/6NQ/2BPPB2/8/PPP3PP/RN3K1R w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test014")
	public void test014() {
		checkMate("Position014", "r1bk3b/1pppq3/2n3n1/1p2P1BQ/3P4/8/PPP3P1/5RK1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test015")
	public void test015() {
		checkMate("Position015", "r1bk2nr/p2p1pNp/n2B4/1p1NP2P/6P1/3P1Q2/P1P1K3/q5b1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test016")
	public void test016() {
		checkMate("Position016", "rnb2k1r/ppp1qBpp/8/4N2Q/8/2n3b1/PPPP2K1/R1B2R2 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test017")
	public void test017() {
		checkMate("Position017", "r1b1kbnr/pp2qp2/1np4p/4P3/2B2BpN/2NQ1pP1/PPP4P/2KR3R w qk - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test018")
	public void test018() {
		checkMate("Position018", "rnbk3r/ppppb2p/3N1n2/7Q/4P3/2N5/PPPP3P/R1B1KB1q w Q - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test019")
	public void test019() {
		checkMate("Position019", "r1b2Qnr/p1pk3p/1pnp4/6q1/2BPP3/8/PPP3PP/RN3RK1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test020")
	public void test020() {
		checkMate("Position020", "r4b1r/ppp1qb2/2np3p/5R2/3PP2k/4B1NP/PPP3K1/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test021")
	public void test021() {
		checkMate("Position021", "r1b3nr/ppqk1Bb1/2pp4/4P1B1/3n4/3P4/PPP2QPP/R4RK1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test022")
	public void test022() {
		checkMate("Position022", "r2k2nr/pp1b1Q1p/2n4b/3N4/3q4/3P4/PPP3PP/4RR1K w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test023")
	public void test023() {
		checkMate("Position023", "rn5r/pp1b2b1/1q1p3p/3nk1p1/314/3Q2P1/PPP1NR2/4R1K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test024")
	public void test024() {
		checkMate("Position024", "r2q1k1r/ppp1bB1p/2np4/6N1/3PP1bP/8/PPP5/RNB2RK1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test025")
	public void test025() {
		checkMate("Position025", "r1b1qb1r/ppp3k1/2np3p/4P2n/2BP2pB/3Q4/PPP3P1/RN3RK1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test026")
	public void test026() {
		checkMate("Position026", "rnb2k1r/ppp1bppp/8/1B4B1/7P/2P1R3/Pq4P1/RN1QK3 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test027")
	public void test027() {
		checkMate("Position027", "r2qk2r/pb4pp/1n2Pb2/2B2Q2/p1p5/2P5/2B2PPP/RN2R1K1 w kq - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test028")
	public void test028() {
		checkMate("Position028", "rn3r1k/ppq3pp/2pb1p2/8/3PR2N/1Q6/PPP2PPP/R1B3K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test029")
	public void test029() {
		checkMate("Position029", "r2k1b1r/pp3ppp/8/1BBp1nq1/8/6P1/PP3P1P/2R1R1K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test030")
	public void test030() {
		checkMate("Position030", "r1bq3r/pp1nk2p/n1pNpp2/3pP1p1/3P1N1Q/3B4/PPP2PPP/R3K2R w KQ - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test031")
	public void test031() {
		checkMate("Position031", "r1bk1bnr/3npppp/pp6/B2N4/2pN4/8/PP2PPPP/R3KB1R w KQ - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test032")
	public void test032() {
		checkMate("Position032", "2n1r2k/pp5p/6pB/5bN1/2Qb4/1B5P/Pq3PP1/5RK1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test033")
	public void test033() {
		checkMate("Position033", "2kr1b1r/pp2pppp/5nn1/1N6/q1p2B2/5P2/PP1Q1PPP/1KR1R3 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test034")
	public void test034() {
		checkMate("Position034", "1n1qbr1k/r4pp1/p1p1pNPp/1p2P2Q/P1p5/4P3/1Pn1KP1P/R1B2BR1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test035")
	public void test035() {
		checkMate("Position035", "r1b1r1k1/p4Rpp/1ppq4/4N3/3np3/1QN5/PP4PP/3R2K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test036")
	public void test036() {
		checkMate("Position036", "r1bqrk2/pp3ppB/2pn3p/3pN2Q/3P1P2/2N5/PP4PP/R4RK1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test037")
	public void test037() {
		checkMate("Position037", "rnb1k2r/p1p2N2/4p2p/bp1qP2Q/2pP3B/2n5/PP3PPP/R3KB1R w KQkq - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test038")
	public void test038() {
		checkMate("Position038", "r1b1r3/ppp4R/3n1kp1/4qNNn/3pP3/P7/BP3PP1/R1Q1K3 w Q - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test039")
	public void test039() {
		checkMate("Position039", "2r1r2k/1q4pp/1p3p2/p7/P2R1N2/1Q2P1P1/5P1P/3n2K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test040")
	public void test040() {
		checkMate("Position040", "6rk/6pp/5p2/p7/P2Q1N2/4P1P1/2r2n1P/6K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test041")
	public void test041() {
		checkMate("Position041", "4k1r1/ppp4p/8/4p3/4P3/2N4n/PPP3rP/3R1R1K b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test042")
	public void test042() {
		checkMate("Position042", "r3r1k1/ppp2ppp/5n2/7q/8/2N3Qb/PPP2P1P/R1BR2K1 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test043")
	public void test043() {
		checkMate("Position043", "r2k1b1r/ppp2pp1/7p/3Pp3/2B1Nn1q/3P1b2/PPPQ1PPP/R1B2RK1 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test044")
	public void test044() {
		checkMate("Position044", "r1b1r1k1/pp3pp1/7p/2Pp4/4n3/1P3Q1P/P1PN1q2/R1BK1R2 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test045")
	public void test045() {
		checkMate("Position045", "r1b1k2r/p4pp1/2p4p/n7/3P2n1/2N3q1/PPPKB1PN/R1BQ3R b kq - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test046")
	public void test046() {
		checkMate("Position046", "r1b1k2r/pppp1ppp/2n2q2/2b1P3/4P3/2P3P1/PP1PN1Bn/RNBQKR2 b kqQ - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test047")
	public void test047() {
		checkMate("Position047", "r4r2/ppp1k1pp/8/4p2n/4P3/1B1P1R1P/PPPBK3/R2Q2q1 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test048")
	public void test048() {
		checkMate("Position048", "r2q3r/ppp2k1p/3p2p1/2b1P2n/3nP3/2NP3P/PPP3P1/R1BQ1RK1 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test049")
	public void test049() {
		checkMate("Position049", "rnb2rk1/ppp2ppp/1b6/8/2BPn3/2PK4/PP3qPP/R1B2R2 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test050")
	public void test050() {
		checkMate("Position050", "rn2k1nr/p4ppp/2p5/2b5/B3pPb1/2Pq4/PP1PN1PP/RNBQRK2 b qk - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test051")
	public void test051() {
		checkMate("Position051", "r1b5/pppp1B2/2n4k/8/4Pp1p/1P6/PBPP1KP1/7R w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test052")
	public void test052() {
		checkMate("Position052", "2k2b1r/ppp2p2/1n1p4/5Pp1/2PP1pQ1/1P4p1/P5P1/RNr2BK1 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test053")
	public void test053() {
		checkMate("Position053", "rn4k1/pppb1ppp/8/3P4/2BP2n1/2N1r3/PP1BNqP1/R2QR2K b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test054")
	public void test054() {
		checkMate("Position054", "2kr4/ppp2p1p/6r1/n2nPb2/3N4/P1P1P2P/2PQ1qP1/R1BK1B1R b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test055")
	public void test055() {
		checkMate("Position055", "7Q/b1qk1ppp/4p3/3pPb2/1n2P3/8/PP3PPP/3K1B1R b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test056")
	public void test056() {
		checkMate("Position056", "r6r/pppk1ppp/8/2b2b2/2P5/2N2N2/PPn1KnPP/1RB2B1R b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test057")
	public void test057() {
		checkMate("Position057", "2kr2nQ/pppb1p1p/8/4P3/1bPp4/3B3q/PPK1N2P/RNB4R b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test058")
	public void test058() {
		checkMate("Position058", "r1bq2k1/pp4pp/2p1p2r/3p1pN1/2PP1Pn1/2N1P1Q1/PP3PBP/R5RK b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test059")
	public void test059() {
		checkMate("Position059", "r3k2r/ppp2p2/5n1p/1b6/1b1PqP2/6B1/PP3KPP/3Q1BN1 b kq - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test060")
	public void test060() {
		checkMate("Position060", "r1bqk2r/bppp1ppp/8/PB2N3/3n4/B7/2PPQnPP/RN2K2R w kqKQ - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test061")
	public void test061() {
		checkMate("Position061", "r4rk1/ppp1qpp1/1bnp1P1p/6NQ/2BPP1b1/4n3/PP4PP/RN3RK1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test062")
	public void test062() {
		checkMate("Position062", "r3qk1r/ppp1n1pp/3pQb2/8/4P3/1BpP4/PPP3PP/2B1K2R w K - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test063")
	public void test063() {
		checkMate("Position063", "r1b1qr2/ppp2p1k/2np2pB/2b1pP1Q/2B1P3/2NP4/PPP3P1/R4K1R w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test064")
	public void test064() {
		checkMate("Position064", "r2qr3/pb1nb1pp/1p2Qnk1/2p2p2/2PP1B2/3B1N2/PP3PPP/R4RK1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test065")
	public void test065() {
		checkMate("Position065", "rn1qrn2/pppb1Rpp/2k1P3/4P3/2BP4/2N5/PP4PP/R2Q2K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test066")
	public void test066() {
		checkMate("Position066", "3qr1k1/pbr1bp1p/1pn1p1pB/8/2BP1QN1/2P3P1/P4P1P/2R1R1K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test067")
	public void test067() {
		checkMate("Position067", "r6r/pp1R1R2/1k5p/1Bp5/4P3/2N5/PP6/2Kb2Bq w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test068")
	public void test068() {
		checkMate("Position068", "r3kb1r/pQpbqppp/8/1B6/4n3/1N3n2/PPP2PPP/RNB2K1R b kq - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test069")
	public void test069() {
		checkMate("Position069", "2k4N/ppp3pp/8/3P4/6n1/3p3P/PP1P1qP1/RNBKR3 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test070")
	public void test070() {
		checkMate("Position070", "1k1r1b1r/p1p2ppp/B7/1P1bp3/Q2P1n1q/6N1/P2P1PPP/RNB2RK1 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test071")
	public void test071() {
		checkMate("Position071", "r2q2k1/ppp2ppp/8/2b2n2/N4Pn1/1P3Q1P/P1P3P1/R1B4K b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test072")
	public void test072() {
		checkMate("Position072", "5r1k/ppp3pb/5q2/7Q/3n1P1b/2P4b/PP1N3P/R5KR b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test073")
	public void test073() {
		checkMate("Position073", "r1b1k2r/ppp4p/2n5/2b1P1pq/1P3p2/2P2N2/P3N1PP/R1B1QK1R b kq - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test074")
	public void test074() {
		checkMate("Position074", "rnb1k2r/ppp2p1p/6n1/3B2pq/3PPb2/5NpP/PPP1N1K1/R1BQ3R b kq - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test075")
	public void test075() {
		checkMate("Position075", "5k1r/pp1r1p1p/1q4p1/1Q3N2/1b6/8/PP3PPP/R3R2K w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test076")
	public void test076() {
		checkMate("Position076", "r2q1r2/pb3p2/1p1bRNpk/2p1N3/2PP4/1P1Q4/P1B2PPP/5RK1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test077")
	public void test077() {
		checkMate("Position077", "3rk2r/pp1b3p/1q2p1p1/1B5Q/8/B3n3/P5PP/5R1K w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test078")
	public void test078() {
		checkMate("Position078", "6q1/p2Rr3/k2N4/b6b/1P4Q1/2n5/r5BK/R5B1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test079")
	public void test079() {
		checkMate("Position079", "8/8/8/8/2N1p2Q/5k2/8/5K2 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test080")
	public void test080() {
		checkMate("Position080", "8/8/8/3K4/8/4kp1R/7Q/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test081")
	public void test081() {
		checkMate("Position081", "4R3/2q2p1k/b2p3p/p1pP1p2/r1P5/2Q2N1P/5PP1/6K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test082")
	public void test082() {
		checkMate("Position082", "r2qk2r/1pp1n1pp/p1npQp2/3Np3/2B1P3/3PP3/PPP3PP/R4RK1 w kq - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test083")
	public void test083() {
		checkMate("Position083", "r1bq1rk1/ppp2pp1/2n5/2bnp1N1/2B1P3/2NP4/PPP2PP1/R2QK2R w KQ - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test084")
	public void test084() {
		checkMate("Position084", "r1bqk2r/ppppn1np/5pN1/b6Q/2BP4/2P5/P4PPP/RN2R1K1 w kq - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test085")
	public void test085() {
		checkMate("Position085", "r1bk2nr/ppp2ppp/3p4/bQ3q2/3p4/B1P5/P3BPPP/RN1KR3 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test086")
	public void test086() {
		checkMate("Position086", "r1b3nr/pppk2qp/1bnp4/4p1BQ/2BPP3/2P5/PP3PPP/RN3RK1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test087")
	public void test087() {
		checkMate("Position087", "r1bq2kr/pppp2pp/2n5/b5N1/3P4/B1P5/P4PPP/R2QR1K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test088")
	public void test088() {
		checkMate("Position088", "r1b3kr/pppp1p1p/5n1B/3PR3/4n3/2P2N2/P4PPP/R5K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test089")
	public void test089() {
		checkMate("Position089", "r2q1rk1/p1pb2p1/3pR3/1p1n1p1Q/7R/8/PP2BPPP/6K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test090")
	public void test090() {
		checkMate("Position090", "r1b1qrk1/pppp1p1p/5B2/8/2BQPp2/8/PPP3PP/RN4K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test091")
	public void test091() {
		checkMate("Position091", "r1bq1r2/pp1kn2p/2pN2p1/3BQ3/3p4/8/PPP2PPP/R3R1K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test092")
	public void test092() {
		checkMate("Position092", "r2q2kr/ppp2p1p/1b3p1B/4N3/2ppN3/8/PPP1bPPP/R3R1K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test093")
	public void test093() {
		checkMate("Position093", "r5kr/pppN1pp1/1bn1R3/1q1N2Bp/3p2Q1/8/PPP2PPP/R5K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test094")
	public void test094() {
		checkMate("Position094", "r1bq1k2/ppp1bp1r/2n2Npp/8/3p1Q2/8/PPP2PPP/R1B1R1K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test095")
	public void test095() {
		checkMate("Position095", "r3r3/ppp3pp/2n5/7k/3p3q/7Q/PPP2PPP/RNB3K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test096")
	public void test096() {
		checkMate("Position096", "rnbqkbnr/pppp4/6Pp/7Q/4Pp2/8/PPPP2PP/RNB1KBNR w KQkq - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test097")
	public void test097() {
		checkMate("Position097", "r1k1q1r1/1pp2RQp/p2b4/3Np1B1/2B1P3/3P3P/PP4P1/n2K4 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test098")
	public void test098() {
		checkMate("Position098", "r1bq3r/pppk1B1p/2np2pQ/2b3N1/4PB2/2NP4/PPP3PP/R3K2n w Q - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test099")
	public void test099() {
		checkMate("Position099", "r4r2/ppp2qpk/1b1p4/3NnP2/1PB1PR2/8/P6P/R6K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test100")
	public void test100() {
		checkMate("Position100", "r1k3nb/ppP2p2/2n1b3/3N4/2BPQB1q/8/PPP3P1/R4K2 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test101")
	public void test101() {
		checkMate("Position101", "rnb1qk1r/ppp1n1p1/3P1b1p/7Q/2BP4/8/PPP4p/RNB2R1K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test102")
	public void test102() {
		checkMate("Position102", "r7/pp2bR1p/2n1b2k/3p2r1/8/2P3Q1/PP4P1/5RK1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test103")
	public void test103() {
		checkMate("Position103", "r1b1k1nr/ppppbp1p/n7/3NN3/3PP2Q/8/PPP4P/R1B1KB1q w Qqk - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test104")
	public void test104() {
		checkMate("Position104", "r2q3r/ppp1n3/3pBk2/8/4P1Q1/2NP2P1/P1P5/R3K3 w Q - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test105")
	public void test105() {
		checkMate("Position105", "r2q4/ppp5/2npBk2/8/4P1Q1/2NP2P1/P1P2K2/r7 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test106")
	public void test106() {
		checkMate("Position106", "r2q1r2/p2n1p2/1pb2k2/3p3Q/1b6/6R1/2PP1PPP/R5K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test107")
	public void test107() {
		checkMate("Position107", "rnbq1b1r/pp4kp/5np1/4p2Q/2BN1R2/4B3/PPPN2PP/R5K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test108")
	public void test108() {
		checkMate("Position108", "r3r2k/pp1b1p1p/2p2Pp1/q3P1Q1/4pR2/B1P5/P1P3PP/R6K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test109")
	public void test109() {
		checkMate("Position109", "r1b2rk1/1p3p1p/pq2p1p1/4B1RQ/4B3/8/PPP2bPP/R6K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test110")
	public void test110() {
		checkMate("Position110", "r1bq1r2/pp2n1p1/4p1k1/3pPpN1/1b1n2QP/2N5/PP3PP1/R1B1K2R w QK - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test111")
	public void test111() {
		checkMate("Position111", "2rk2nr/pp3ppp/4p1q1/2Bp4/B5b1/2R5/PP3PPP/3QR1K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test112")
	public void test112() {
		checkMate("Position112", "r1b1k1r1/pp1nb1pp/1qn1p3/3pp3/3N1P2/2PB1N2/PPQ3PP/R1B1K2R w QKq - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test113")
	public void test113() {
		checkMate("Position113", "r1bqrk2/p2n1pp1/1pn1p3/2ppP1PQ/3P1N2/2P5/PP3PP1/R1B1K2R w QK - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test114")
	public void test114() {
		checkMate("Position114", "rnb2rk1/pppqnp1p/3bpB2/8/3P4/3B4/PPP2PPP/R2QK1NR w QK - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test115")
	public void test115() {
		checkMate("Position115", "r1b1r1k1/ppq2p1p/2pbpBp1/3n4/3PR2Q/2PB1N2/PP3PPP/R5K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test116")
	public void test116() {
		checkMate("Position116", "r2r1k2/p2nqp2/2b1p2P/4B3/2p5/3B4/PPP2P1P/2KR2R1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test117")
	public void test117() {
		checkMate("Position117", "r1bqkb1r/ppp1n3/3p1nNp/4pp1Q/2B1P3/3P4/PPP2PPP/R1B2RK1 w qk - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test118")
	public void test118() {
		checkMate("Position118", "r1b1knr1/pp2bp1p/1q6/5p2/4N3/8/PPPQBPPP/2KRR3 w q - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test119")
	public void test119() {
		checkMate("Position119", "2rr2k1/p1pnqp2/bp2pb2/6N1/3P2P1/2P2N2/PPQ2P2/1K1R3R w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test120")
	public void test120() {
		checkMate("Position120", "r1b5/pp3p1R/4p1p1/2np2N1/5Pk1/2N3P1/PqP5/R3KB2 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test121")
	public void test121() {
		checkMate("Position121", "r1bR2nr/ppp2kpp/2n1p3/qB3pB1/1b2p1Q1/2N2N2/PPP2PPP/2K4R w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test122")
	public void test122() {
		checkMate("Position122", "r2qr1k1/1p1bnpp1/p3p3/n2pP1NQ/P1pP4/2P5/2P2PPP/R1B1K2R w QK - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test123")
	public void test123() {
		checkMate("Position123", "rnbq1knr/ppb4p/3NpPp1/3p4/1P1p1Q2/8/P1PB1PPP/R3KBNR w QK - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test124")
	public void test124() {
		checkMate("Position124", "3r1b1r/pp1kpppp/5nn1/1N1B4/5q2/5P2/PP1Q1PPP/1KR1R3 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test125")
	public void test125() {
		checkMate("Position125", "rn1q1r2/pb2pp2/1p6/3pN1kn/3P4/2NBPQ2/PP4P1/R3K3 w Q - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test126")
	public void test126() {
		checkMate("Position126", "rn1q1b1r/pp2kppp/3np3/1B2NQ2/P2p4/2N5/1P3PPP/R1B1K2R w KQ - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test127")
	public void test127() {
		checkMate("Position127", "r1b3qr/pp1nb1pp/2p1Nn2/7k/3P4/2NQB3/PP3PPP/R3K2R w QK - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test128")
	public void test128() {
		checkMate("Position128", "r2n4/pp1R1Pp1/5kr1/6p1/PbB1R1P1/2p2P2/1P5P/2K5 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test129")
	public void test129() {
		checkMate("Position129", "r4rk1/pbq1bp1p/2p1pQp1/4N3/1nP5/4P3/PB3PPP/RB3RK1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test130")
	public void test130() {
		checkMate("Position130", "r4rk1/pp2qp2/2nR4/3p2Pp/1Pp5/P3P3/1BQ2PP1/2K2B2 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test131")
	public void test131() {
		checkMate("Position131", "r1bk1r2/pp2R1p1/3p3p/2p3Q1/3N4/8/PpP2PPP/6K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test132")
	public void test132() {
		checkMate("Position132", "r1b2rk1/pp1n1ppp/4p3/q2N2b1/1p1P3P/4PN2/P1Q2PP1/R3KB1R w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test133")
	public void test133() {
		checkMate("Position133", "r2qkbQ1/pb1n2p1/2p2r2/5N1p/Pp1P3N/4P3/1P3PPP/2R2RK1 w q - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test134")
	public void test134() {
		checkMate("Position134", "r1b1r3/1pqn2pk/p1p2nNp/4P3/2B5/P3P2P/1PQ2PP1/2R2RK1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test135")
	public void test135() {
		checkMate("Position135", "r2q1rk1/pp1nbpn1/2p5/3p2p1/3P2b1/2NBP1B1/PPQK1P2/6RR w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test136")
	public void test136() {
		checkMate("Position136", "r2q2k1/pbp1rpb1/1p6/n7/3P4/2N1P3/PPQ2P2/1BKR3R w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test137")
	public void test137() {
		checkMate("Position137", "r1br2k1/1p3pp1/1q2p2R/p3B3/3p4/8/PP1QN1PP/1B4K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test138")
	public void test138() {
		checkMate("Position138", "6r1/4r3/b2N3k/1q4pp/4Q3/8/7N/BB2K3 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test139")
	public void test139() {
		checkMate("Position139", "7Q/8/8/5p1p/7k/4b1RP/6PK/2qr4 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test140")
	public void test140() {
		checkMate("Position140", "6rk/3R1Qpp/1p3p2/p1r5/P4N2/2n1PqP1/5P1P/3R2K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test141")
	public void test141() {
		checkMate("Position141", "r3k2r/ppp2pp1/2np4/2B1p2n/2B1P1Nq/3P4/PPP2PP1/RN1Q1RK1 b qk - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test142")
	public void test142() {
		checkMate("Position142", "r3k3/ppp3Qp/3qp1p1/b4r2/3n4/4B3/P2N1PPP/R4RK1 b q - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test143")
	public void test143() {
		checkMate("Position143", "r1b2rk1/1pp3pp/1p1p4/3Ppq1n/2B3P1/2P4P/PP1N1P1K/R2Q1RN1 b q - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test144")
	public void test144() {
		checkMate("Position144", "r4rk1/ppp3pp/4b3/6K1/8/8/PB3bPP/RN1Q3R b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test145")
	public void test145() {
		checkMate("Position145", "r5k1/ppp3pp/2n5/3p2q1/8/2P2P1b/PP1P1P1P/RNB3QK b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test146")
	public void test146() {
		checkMate("Position146", "r2Bk3/ppp2pp1/2np4/4p3/2B1P1n1/3P4/PPPNKpP1/RN1r4 b q - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test147")
	public void test147() {
		checkMate("Position147", "r2k3r/ppp2Bp1/8/6p1/4n3/3q2P1/PP1Nb1KP/R1Q5 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test148")
	public void test148() {
		checkMate("Position148", "r1b1k2r/ppp5/6pp/2bpp1N1/3n2P1/3P4/PPP3PP/RNB2RK1 b kq - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test149")
	public void test149() {
		checkMate("Position149", "2k5/ppp3pp/8/2bPp3/1n1nP1P1/2K5/PPPN1qPP/R1BQ3R b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test150")
	public void test150() {
		checkMate("Position150", "r1b1k2N/ppp3pp/8/3pp3/3n1q2/N2K4/PPPP3P/R1B1Q2R b q - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test151")
	public void test151() {
		checkMate("Position151", "r1bk3N/ppp2Bpp/8/3p4/4n3/4qQ2/PPn3KP/RN5R b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test152")
	public void test152() {
		checkMate("Position152", "7N/ppp1k2p/8/3Pp3/3n2pb/2NPB2b/PPP2rPP/R5KR b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test153")
	public void test153() {
		checkMate("Position153", "2k2r1N/ppp3pp/8/3Pp3/3nn1q1/N3Q1P1/PPPP3P/R1B1KR2 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test154")
	public void test154() {
		checkMate("Position154", "r3k3/ppp3p1/8/3Pp2p/2B3n1/2NP1nPK/PPPQbq1P/R1B4R b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test155")
	public void test155() {
		checkMate("Position155", "r4r2/pp2k1pp/1bpp4/4p3/BP1PPnbq/2P5/PB3PPP/RN2QRK1 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test156")
	public void test156() {
		checkMate("Position156", "r4k1N/pp4pp/3p4/6n1/8/6qP/PP1P1Qb1/RNB2BKR b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test157")
	public void test157() {
		checkMate("Position157", "1r3k1r/pPp3p1/3q1n2/2b3p1/4p1b1/1B1P2P1/PPPQ1P1P/RNB2RK1 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test158")
	public void test158() {
		checkMate("Position158", "r5k1/pp3pp1/2b4p/8/3prP2/8/PPPQBqPP/R3R2K b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test159")
	public void test159() {
		checkMate("Position159", "r2qk2r/pp3ppp/n7/3NPb2/2KP1p1N/6nP/PP1B2P1/R2Q3R b kq - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test160")
	public void test160() {
		checkMate("Position160", "rnb1k2r/pppp1ppp/8/8/2B1P2q/3P1P2/PPP1Q2n/RNB3KR b kq - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test161")
	public void test161() {
		checkMate("Position161", "rnb2r1k/p1B1Q1pp/8/1p4N1/3q4/3P4/PPP3PP/RN3n1K b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test162")
	public void test162() {
		checkMate("Position162", "rn2k1nN/ppp4p/8/3B4/3bP2q/5ppR/P1PP2P1/2Q1RK2 b q - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test163")
	public void test163() {
		checkMate("Position163", "r3k2r/pp3p1p/2nq4/P3nbp1/1bP5/1P2N1PP/3BP3/RN1QKB1R b QKqk - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test164")
	public void test164() {
		checkMate("Position164", "2kr3r/ppp2ppp/8/2Q2b2/2P1q3/PP2PnP1/1B1pKP1P/R4B1R b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test165")
	public void test165() {
		checkMate("Position165", "2kr1b2/pp3p2/2n5/4PnP1/2P4q/1Q1p4/PP1BbPBN/R3R1K1 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test166")
	public void test166() {
		checkMate("Position166", "2kr1b1r/pp3ppp/2p5/2n5/P1N2Bb1/2N1PnPP/1P2KP2/R4B1R b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test167")
	public void test167() {
		checkMate("Position167", "r2qk2r/1pp2pp1/p3p3/6Pn/1B1P2PK/1Q2P2P/PP6/R6R b kq - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test168")
	public void test168() {
		checkMate("Position168", "r1bq2k1/pp4pp/2p1p2r/3p4/2PPBPp1/2N1P3/PPQ2P1P/R5RK b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test169")
	public void test169() {
		checkMate("Position169", "r4r1k/1bQ2ppp/pN5q/8/Pp2nP2/2N1B1b1/1PB3P1/R1R3K1 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test170")
	public void test170() {
		checkMate("Position170", "r1b2b1r/1pBq2pp/8/p1kN4/4P3/P2p1Q2/BPpK1PPP/2R5 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test171")
	public void test171() {
		checkMate("Position171", "r1q1k1r1/1pp2R2/3p4/p1nPp1p1/2P1P2P/7b/PPBQN2K/5R2 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test172")
	public void test172() {
		checkMate("Position172", "Q7/2pk1p1p/p2pb3/2p5/4n3/4K3/PPq2PPP/RN5R b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test173")
	public void test173() {
		checkMate("Position173", "5r1r/pppkq3/3p1n2/4p1N1/3nP1P1/1BNPK3/PPP3P1/R2Q4 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test174")
	public void test174() {
		checkMate("Position174", "r3kb1r/p1Q2ppp/4p3/3p1b2/1nPP2n1/5N2/PP2PPPP/R1B1KB1R b kK - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test175")
	public void test175() {
		checkMate("Position175", "r5k1/1bq3pp/p3p3/1pb5/4B1n1/P4rP1/1P2QP1P/R1B2RK1 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test176")
	public void test176() {
		checkMate("Position176", "N1b4r/pp3ppp/3Rn3/3Rp3/k1p5/2P5/PP3PPP/2K5 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test177")
	public void test177() {
		checkMate("Position177", "r2k3N/pp3Bpp/3p1q2/2n1pb2/1QKb4/2NP4/PP4PP/R1B4R b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test178")
	public void test178() {
		checkMate("Position178", "r2qk2r/p1Q1bppp/2b5/1N6/4nPK1/8/PP4PP/R1B1nBNR b kq - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test179")
	public void test179() {
		checkMate("Position179", "1k5r/ppp1q1pp/5n2/r1b2N2/4P3/BPn2N1P/2PP2P1/K1R1QB1R b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test180")
	public void test180() {
		checkMate("Position180", "2rq1bk1/pb3Np1/1pn5/2nN4/8/P2Q4/BP3PPP/4R1K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test181")
	public void test181() {
		checkMate("Position181", "r2b2k1/ppn1r3/1qp1pp1Q/4N3/8/8/PPB2PPP/R4R1K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test182")
	public void test182() {
		checkMate("Position182", "r1bk2nr/ppp2ppp/1b6/n5N1/3P4/2NB3P/P4PP1/R1BqR1K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test183")
	public void test183() {
		checkMate("Position183", "r1bq2kr/pppp2pp/2n5/2b1P1N1/3p4/2P5/PP3nPP/RNBQK2R w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test184")
	public void test184() {
		checkMate("Position184", "r1nqk3/ppppb3/5p1r/n3N2p/2B5/2PQ4/P4PPP/R3R1K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test185")
	public void test185() {
		checkMate("Position185", "r2q2kr/p1p2p1p/1p3p1B/2b1N3/2ppN3/2P5/PP3PPP/R2bR1K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test186")
	public void test186() {
		checkMate("Position186", "r1b1k2N/ppp3pp/2np4/4p3/2B4q/8/PPPP3P/RNB2QKn w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test187")
	public void test187() {
		checkMate("Position187", "2k1rb1r/QpB2ppp/2b1qn2/3N4/4p3/8/PPP3PP/2KR1B1R w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test188")
	public void test188() {
		checkMate("Position188", "r2qnrk1/pbppbp1p/1p4pQ/4B3/3P4/1BN5/PPP3PP/R4RK1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test189")
	public void test189() {
		checkMate("Position189", "1rb3nr/1p1p3p/p2k3b/3Np3/2B1P3/3P1R2/PPP3PP/5RK1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test190")
	public void test190() {
		checkMate("Position190", "r1b2rkq/pppp1pbp/6n1/3N4/2B2Q2/1P6/P1PP2PP/B3RR1K w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test191")
	public void test191() {
		checkMate("Position191", "r2q3r/ppp1b3/7p/5Q2/2nP2pk/8/PPP3P1/R5K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test192")
	public void test192() {
		checkMate("Position192", "rn3bkr/p4qpp/1pN1Qp2/1Bp5/3P4/8/PPP3PP/R4RK1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test193")
	public void test193() {
		checkMate("Position193", "r1b1kb1r/1p1nnppp/4p3/1N1p2BP/3N4/2P3Q1/P13PP1/R3K2R w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test194")
	public void test194() {
		checkMate("Position194", "rn1q1r1k/pb2pp2/1p3n2/3pN1Q1/3P4/2NBP3/PP4P1/R3K3 w Q - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test195")
	public void test195() {
		checkMate("Position195", "r2k1r2/pp1n2Qp/3Npp2/3pPb2/1P6/4P3/1q3PPP/2R1KB1R w K - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test196")
	public void test196() {
		checkMate("Position196", "r2qrk2/1b2bppB/p7/1p1PN3/1n6/8/1B2QPPP/2RR2K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test197")
	public void test197() {
		checkMate("Position197", "r1bqr3/pp1nbkp1/2p1N2p/8/3PP3/1Q6/PP3PPP/R1B2RK1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test198")
	public void test198() {
		checkMate("Position198", "r2q1r2/1b3pk1/4p2R/p2n2N1/PpB3Q1/4n3/NP4PP/R5K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test199")
	public void test199() {
		checkMate("Position199", "r1b3qr/pp1nb1pp/2p1Nn2/7k/3P4/2NQB3/PP3PPP/R3K2R w QK - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test200")
	public void test200() {
		checkMate("Position200", "3nkb1r/2p1p2p/2b1P1p1/5p2/2QP3q/2P1B3/4BPPP/R5K1 w k - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test201")
	public void test201() {
		checkMate("Position201", "rnb4Q/pp1qn1p1/2p1p1k1/3pP1N1/2P2p2/2N5/PP3PPP/R3K2R w QK - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test202")
	public void test202() {
		checkMate("Position202", "1n3rk1/5ppp/4p3/1p2Nb2/1P2N3/3R4/5P1P/r2BK1R1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test203")
	public void test203() {
		checkMate("Position203", "r1b5/pPR5/5kpp/3pN2r/3Pp2q/P3P3/2Q2PPP/5RK1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test204")
	public void test204() {
		checkMate("Position204", "bq2rrk1/7p/2n5/4Q2B/1b1N4/2B5/5RP1/4KR2 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test205")
	public void test205() {
		checkMate("Position205", "1r3q1k/7p/2p2bp1/P3p3/2B3N1/5P2/2K3P1/3Q3R w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test206")
	public void test206() {
		checkMate("Position206", "r5k1/pNp3pp/3pp2r/4p3/4P2q/3P1P2/PPP2P1P/R2QR1K1 b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test207")
	public void test207() {
		checkMate("Position207", "r2k3r/ppp5/2np2q1/2bNp3/4P1b1/1B6/PPPP1PPP/R1B1QRK1 b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test208")
	public void test208() {
		checkMate("Position208", "4rk1r/pPp3p1/3q1n2/2b3p1/4p3/1B3bP1/PPPPNP1P/R1B1QRK1 b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test209")
	public void test209() {
		checkMate("Position209", "r1b1r1k1/p4pp1/2p4p/n7/2PP4/2N2Nq1/PPQKBnP1/R1B3R1 b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test210")
	public void test210() {
		checkMate("Position210", "rnb1r3/ppp2kpp/8/3P1p2/1Q3P1q/5N2/PPPNB2P/R1BK3n b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test211")
	public void test211() {
		checkMate("Position211", "rnbkr3/ppp2Bpp/5b2/7q/2Q2p2/3P1N2/PPP3PP/R1B2K1R b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test212")
	public void test212() {
		checkMate("Position212", "2kr3r/ppp5/4p3/2NpP1bn/3Q1pbq/P7/1PP3PP/R1B1NRK1 b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test213")
	public void test213() {
		checkMate("Position213", "4r3/p4pkp/q7/3Bbb2/P2P1ppP/2N3n1/1PP2KPR/R1BQ4 b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test214")
	public void test214() {
		checkMate("Position214", "r3r1k1/pp3ppp/2p3b1/q2pP3/n4B2/1KP1Q1PB/P1P1RP1P/3R4 b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test215")
	public void test215() {
		checkMate("Position215", "7k/1p4pp/5q2/pP1Qp3/2N1n1P1/2P1P2P/1P3r2/R5K1 b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test216")
	public void test216() {
		checkMate("Position216", "3rk2r/p1pqbppp/2B5/1P6/2P3b1/P5P1/2Q1nP1P/RN3R1K b k - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test217")
	public void test217() {
		checkMate("Position217", "rnbB1rk1/ppp3pp/8/3Pp3/1b2n3/3B4/PP2KPPP/R2Q2NR b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test218")
	public void test218() {
		checkMate("Position218", "r4n1k/pp3p1p/2p4r/3p2p1/3P1N1q/3BPbPP/PPQ2P1K/1R3R2 b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test219")
	public void test219() {
		checkMate("Position219", "r3k1r1/pb3p1p/3bpp2/1P2n3/3N4/3B3P/1P2QPP1/R1B2RK1 b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test220")
	public void test220() {
		checkMate("Position220", "r4nk1/pp3ppp/4r3/1N1p4/3Pp1nq/BP2P3/P3QPP1/R1R2NK1 b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test221")
	public void test221() {
		checkMate("Position221", "r1b2rk1/pppp1Np1/7p/3qP2Q/8/8/P5PP/b1B2R1K w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test222")
	public void test222() {
		checkMate("Position222", "r3r1k1/pp3Rpp/2pp4/8/2P1P2B/5Q2/P1q3PP/5R1K w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test223")
	public void test223() {
		checkMate("Position223", "r4b1r/pppq2pp/2n1b1k1/3n4/2Bp4/5Q2/PPP2PPP/RNB1R1K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test224")
	public void test224() {
		checkMate("Position224", "r1bk1r2/pp2R1p1/3p3p/2p3Q1/3N4/8/PpP2PPP/6K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test225")
	public void test225() {
		checkMate("Position225", "rq1r2k1/1bp2p1p/pp3Bp1/2p1P3/P4PQ1/3B4/6PP/5RK1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test226")
	public void test226() {
		checkMate("Position226", "2rqrb2/p2nk3/bp2pnQp/4B1p1/3P4/P1N5/1P3PPP/1B1RR1K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test227")
	public void test227() {
		checkMate("Position227", "6br/3Np1bk/8/6P1/8/8/1Qp5/KB3r2 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test228")
	public void test228() {
		checkMate("Position228", "2k2r1N/ppp3pp/8/3Pp3/4n1q1/4QnPP/PPPP4/RNBK3R b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test229")
	public void test229() {
		checkMate("Position229", "r3k2N/ppp3pp/5n2/2qPp3/1PBn2b1/8/P1PP1KPP/RNB2Q1R b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test230")
	public void test230() {
		checkMate("Position230", "N5nr/pp1k1ppp/2pp4/2b1p3/2BnPP1q/3P1P2/PPPK3P/R1BQ3R b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test231")
	public void test231() {
		checkMate("Position231", "3rk2r/p1p1bppp/2P5/8/2P5/P4bPK/2Q1nP1P/RN3R2 b k - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test232")
	public void test232() {
		checkMate("Position232", "5rk1/pp4pp/2p1p2q/1P2P3/2P1R3/P4rPb/1B5P/2RQ2K1 b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test233")
	public void test233() {
		checkMate("Position233", "r2k1b1r/pq2npp1/n7/1Np1Q2p/8/3P2PB/PPPB3P/R3K2b w Q - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test234")
	public void test234() {
		checkMate("Position234", "r2k3r/pq3ppp/1p1B4/1B3p2/8/8/PP3PPP/3RR1K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test235")
	public void test235() {
		checkMate("Position235", "r1bq1b1r/4p1pp/1np2nk1/1p4N1/3P3P/p1N1P3/PP3PP1/R1BQK2R w QK - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test236")
	public void test236() {
		checkMate("Position236", "1nr2b1r/1q2kppp/4p3/2p5/B1Q1P1b1/B1P2N2/P4PPP/3R2K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test237")
	public void test237() {
		checkMate("Position237", "r3k2r/ppp2Npp/2n5/3Bp3/4K1bq/8/PPPP3P/RNB1Q2R b kq - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test238")
	public void test238() {
		checkMate("Position238", "r1bq1rk1/pppp1pp1/2n3P1/2b1N3/2B1P3/2NP4/PPP3P1/R2nK2R w KQ - 0 1", 5); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test239")
	public void test239() {
		checkMate("Position239", "r6k/pp2q1pp/2ppb1P1/2bBpp2/4P3/P2P4/1PP2PP1/R2QK2R w KQ - 0 1", 5); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test240")
	public void test240() {
		checkMate("Position240", "5Q2/6pk/1p5p/8/1P1Pp3/q5P1/P4KBP/8 w - - 0 1", 6); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test241")
	public void test241() {
		checkMate("Position241", "8/4Npk1/6p1/P7/1nnP4/5BP1/5r1P/RK2R3 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test242")
	public void test242() {
		checkMate("Position242", "3r1r1k/1b3q2/1p3p1B/8/1P1p3Q/P2B4/5PP1/3R2K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test243")
	public void test243() {
		checkMate("Position243", "2Q3r1/4k1q1/p7/1p1pN3/3P4/2P1N3/PP2bPPR/2K5 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test244")
	public void test244() {
		checkMate("Position244", "4k2r/p3b2p/2r1Q3/2N3q1/1P3pp1/8/6PP/3R2K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test245")
	public void test245() {
		checkMate("Position245", "8/8/8/N2Q4/p7/b1k2p2/2P3q1/3K4 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test246")
	public void test246() {
		checkMate("Position246", "5r2/2Qn1p1k/p3p2r/1p6/4q3/PP6/2P3RR/1K6 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test247")
	public void test247() {
		checkMate("Position247", "6k1/5p2/6p1/pN6/P2bqP2/5RPp/3p3P/3Q3K b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test248")
	public void test248() {
		checkMate("Position248", "4r1k1/5pp1/p2p1q2/P1bP4/2P5/1QN2PPp/1P5P/5R1K b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test249")
	public void test249() {
		checkMate("Position249", "8/3p4/b2P4/1rp1pR2/1p2P2k/1N2P3/5K2/8 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test250")
	public void test250() {
		checkMate("Position250", "rk5r/p2R2pp/Bpp1Np2/8/1q6/8/8/2K5 w - - 0 1", 6); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test251")
	public void test251() {
		checkMate("Position251", "6K1/6P1/2q1k3/8/8/8/8/8 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test252")
	public void test252() {
		checkMate("Position252", "r3r2k/2Q1R3/2p2p1p/3p4/p2q4/PP4R1/2P1K1PP/8 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test253")
	public void test253() {
		checkMate("Position253", "2b2k1r/1p3pp1/5N2/q3N3/8/7P/1P2Q1P1/1K6 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test254")
	public void test254() {
		checkMate("Position254", "2rr1k2/3q1p2/4p1R1/1p6/1P6/3P1QR1/6PK/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test255")
	public void test255() {
		checkMate("Position255", "2Rr3k/2R4p/4p3/q2rp3/8/7P/1PP4Q/1K6 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test256")
	public void test256() {
		checkMate("Position256", "3R2rk/7p/r4p1P/8/1p6/5P2/2P4K/4R3 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test257")
	public void test257() {
		checkMate("Position257", "6k1/p5p1/1p4P1/2pr4/5R1R/4P3/PP2KP2/6r1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test258")
	public void test258() {
		checkMate("Position258", "2k2bnr/Qpp2ppp/q7/8/8/2N1Bn2/PP3PPP/2KR4 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test259")
	public void test259() {
		checkMate("Position259", "2r4k/1p3Bb1/6Qp/3N4/8/q1P5/5PK1/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test260")
	public void test260() {
		checkMate("Position260", "4rk2/6p1/1q1p1b1p/1p6/7Q/1B6/PP5P/5R1K w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test261")
	public void test261() {
		checkMate("Position261", "3r1r2/1R6/pN2q1nk/P2p2pp/1P2pp2/4P1PP/4QP1K/3R4 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test262")
	public void test262() {
		checkMate("Position262", "2r2r2/p4p1k/1p2pP1p/n2pPR2/q5PN/2Pp4/2PQ3P/4R1K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test263")
	public void test263() {
		checkMate("Position263", "3r4/3P3p/8/5Kp1/7k/2p5/1bR3P1/3R4 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test264")
	public void test264() {
		checkMate("Position264", "2r3r1/8/1p6/7p/1P1P1p1P/2nk1N1K/6R1/4R3 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test265")
	public void test265() {
		checkMate("Position265", "8/3R4/4kp2/r4p2/1r3P2/8/5PK1/7R w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test266")
	public void test266() {
		checkMate("Position266", "4rk2/5pp1/1p6/b2R2B1/1q6/8/P3QPP1/5K2 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test267")
	public void test267() {
		checkMate("Position267", "4B3/6pk/4R2p/8/8/7P/2rr2PK/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test268")
	public void test268() {
		checkMate("Position268", "R4bk1/5p1p/5rp1/8/8/4B3/1PP2P2/1K6 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test269")
	public void test269() {
		checkMate("Position269", "r1b3k1/pp1p3p/3p2pB/8/q1P5/1P6/P5PP/4R1K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test270")
	public void test270() {
		checkMate("Position270", "q2br1k1/1b4pp/3Bp3/p6n/1p3R2/3B1N2/PP2QPPP/6K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test271")
	public void test271() {
		checkMate("Position271", "8/1p3p1k/2b2Bpr/8/8/P6R/1Pq3P1/4Q2K w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test272")
	public void test272() {
		checkMate("Position272", "8/p2q1r1k/2b2B1p/5P2/8/P7/R2Q4/6K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test273")
	public void test273() {
		checkMate("Position273", "b3q1rk/5p1p/2n4B/4N3/3P4/2P5/8/1Q2K2R w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test274")
	public void test274() {
		checkMate("Position274", "6r1/5pk1/2r2p2/1p3P2/1P6/2P4R/3B4/7K w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test275")
	public void test275() {
		checkMate("Position275", "r4Br1/p1q2p1k/1p1R2p1/3pP2b/7Q/2p5/P1P1NPPP/6K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test276")
	public void test276() {
		checkMate("Position276", "q5rk/4pRrp/3p2B1/2n5/2P4Q/7P/6p1/5RK1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test277")
	public void test277() {
		checkMate("Position277", "2R5/p1R3b1/1q5k/4n1p1/4B3/6P1/1P5P/7K w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test278")
	public void test278() {
		checkMate("Position278", "br3r2/2qnb2p/p5p1/2pBk3/2Pp3P/3P1QP1/P4P2/R5K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test279")
	public void test279() {
		checkMate("Position279", "r7/2q4p/2Np1k1B/2pP1b1P/2n3R1/2P4K/8/1Q6 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test280")
	public void test280() {
		checkMate("Position280", "2B5/8/8/6bp/3Q1q1k/5P2/6P1/4K3 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test281")
	public void test281() {
		checkMate("Position281", "8/8/q3b1r1/4k3/1p2N3/8/P5P1/1KBQ4 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test282")
	public void test282() {
		checkMate("Position282", "5kr1/6p1/q2b2B1/4n1B1/8/8/5P2/2RQ2K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test283")
	public void test283() {
		checkMate("Position283", "3qk3/1p1n1p2/p3b3/8/3P4/B1PB4/4Q1P1/4K3 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test284")
	public void test284() {
		checkMate("Position284", "4qk2/6p1/1pbPQn2/2p3B1/8/1B6/1P4P1/6K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test285")
	public void test285() {
		checkMate("Position285", "q2k2r1/6p1/1p3n2/1B1b2Q1/7B/8/1P4P1/2R3K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test286")
	public void test286() {
		checkMate("Position286", "3qkb2/2pp1pp1/8/4P3/8/1B6/2P3PP/2Bn1RK1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test287")
	public void test287() {
		checkMate("Position287", "1r2k3/3pbRp1/b1n1p3/p5B1/7P/1P6/2B5/5R1K w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test288")
	public void test288() {
		checkMate("Position288", "5rk1/1b2b2p/2q2n2/5QN1/2p5/2B5/1P4P1/1B2R1K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test289")
	public void test289() {
		checkMate("Position289", "3qkb2/3pp2p/6p1/8/8/2P5/1PQ2PP1/1B2K3 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test290")
	public void test290() {
		checkMate("Position290", "3qkb2/3pp3/7r/7p/8/3B4/1P3PP1/R2QK3 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test291")
	public void test291() {
		checkMate("Position291", "q7/1p6/3B4/6pp/7k/p6P/6P1/6K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test292")
	public void test292() {
		checkMate("Position292", "3N4/8/6k1/8/B2B3K/8/8/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test293")
	public void test293() {
		checkMate("Position293", "6rk/2p4p/3b1n2/3pNpN1/3P1B2/6P1/1P3PKP/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test294")
	public void test294() {
		checkMate("Position294", "1q3bk1/5p1r/7P/8/n5N1/2Q5/1B6/1K6 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test295")
	public void test295() {
		checkMate("Position295", "2rkr3/R7/3Bb3/2p1N1p1/8/8/1P4P1/6K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test296")
	public void test296() {
		checkMate("Position296", "rk1n1n1r/pp2R2p/2p3p1/2N5/1PP3B1/8/P5PP/6K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test297")
	public void test297() {
		checkMate("Position297", "3q2kr/1p3p1p/5b1B/3N4/2P5/8/5KP1/4Q3 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test298")
	public void test298() {
		checkMate("Position298", "1nk5/3b2b1/3p4/B2N1n2/2P1N1p1/6P1/6B1/4K3 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test299")
	public void test299() {
		checkMate("Position299", "2r1r1k1/qp3p1p/4b1N1/8/p1n5/Q1B1PB2/P5PP/1R4K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test300")
	public void test300() {
		checkMate("Position300", "2qr1rk1/5ppb/p1n1p2p/1p3N2/4P3/2B5/PP1Q1PPP/2R1R1K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test301")
	public void test301() {
		checkMate("Position301", "r3r2k/p2b1Rp1/1pp2Np1/3q4/3n4/4Q3/PP4PP/3R2K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test302")
	public void test302() {
		checkMate("Position302", "6rk/Q6p/4N1p1/3b4/R7/2q4P/6P1/7K w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test303")
	public void test303() {
		checkMate("Position303", "8/5pkr/R2N4/8/8/8/8/2K5 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test304")
	public void test304() {
		checkMate("Position304", "4r1k1/1p3pp1/p1b3q1/5N2/1Q2P3/2P5/PP3P2/1K5R w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test305")
	public void test305() {
		checkMate("Position305", "4bkr1/6p1/1q5P/4N3/8/4R3/Q7/6K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test306")
	public void test306() {
		checkMate("Position306", "4kn2/r4p2/8/3N4/q6Q/8/1P6/1K1R4 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test307")
	public void test307() {
		checkMate("Position307", "5nnk/RQ5p/4bb2/3qN3/3P4/8/5PP1/6K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test308")
	public void test308() {
		checkMate("Position308", "4Q3/8/5pqp/7k/8/7K/8/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test309")
	public void test309() {
		checkMate("Position309", "kr1q4/1p4Q1/pP6/3n4/6p1/7P/5PB1/R5K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test310")
	public void test310() {
		checkMate("Position310", "2r5/p2b4/7R/bkqQ1p2/3p4/2P5/P1P2PP1/4K3 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test311")
	public void test311() {
		checkMate("Position311", "6k1/pp2q2p/2b4Q/6N1/8/8/PP1r2PP/5RK1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test312")
	public void test312() {
		checkMate("Position312", "4rk2/4q1p1/2p5/3P4/7Q/2B5/1P6/2K5 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test313")
	public void test313() {
		checkMate("Position313", "6kn/1p3q1p/2n2p1Q/2B5/6P1/3B4/P3P3/K7 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test314")
	public void test314() {
		checkMate("Position314", "b1r1r3/pk3ppp/1p1Q4/8/4q3/4B3/1KP2PPP/R2R4 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test315")
	public void test315() {
		checkMate("Position315", "r3k1r1/pp1b1p1p/4p3/6pq/2P5/Q3PN1P/PB3PP1/5RK1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test316")
	public void test316() {
		checkMate("Position316", "r4rk1/1p4p1/4q3/4P3/8/3Q4/1P6/1K4RR w q - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test317")
	public void test317() {
		checkMate("Position317", "8/8/2N4p/2r1p2k/3p3P/3P2K1/4N3/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test318")
	public void test318() {
		checkMate("Position318", "3r3k/pp3Qpp/2nbB3/6N1/8/8/PqP2PPP/R5K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test319")
	public void test319() {
		checkMate("Position319", "4r1nk/p5pp/1p4b1/2p3N1/2P4Q/1P6/PB2qPPP/5RK1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test320")
	public void test320() {
		checkMate("Position320", "kr1q4/nb1N4/8/8/8/6Q1/PP6/1K5R w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test321")
	public void test321() {
		checkMate("Position321", "2k5/1pp2rp1/n1N5/Q2N1q2/8/8/2P3PP/6K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test322")
	public void test322() {
		checkMate("Position322", "2b5/5N2/3n2r1/7k/5R1p/3N1K2/1P6/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test323")
	public void test323() {
		checkMate("Position323", "6k1/1b1npr1p/p4nNQ/P4N2/4p3/1qp2P2/6P1/K2R3R w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test324")
	public void test324() {
		checkMate("Position324", "4N3/1q6/6b1/2pk1n2/2N5/4P3/2Q1K3/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test325")
	public void test325() {
		checkMate("Position325", "4rr1k/pp2p2p/5b2/n3N2N/3P4/1B6/PP5P/6RK w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test326")
	public void test326() {
		checkMate("Position326", "8/R3Pr2/2nk4/8/3PK3/8/8/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test327")
	public void test327() {
		checkMate("Position327", "2q2k1r/5np1/1p3P2/2b5/2BQ4/8/1P4P1/4R1K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test328")
	public void test328() {
		checkMate("Position328", "7k/4qp1P/p5pQ/1p6/8/8/PPP1r3/2K4R w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test329")
	public void test329() {
		checkMate("Position329", "3b4/R7/4p2k/r3P3/8/5K2/p1B4P/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test330")
	public void test330() {
		checkMate("Position330", "6R1/7p/6bk/7p/q5P1/8/1P6/2K4Q w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test331")
	public void test331() {
		checkMate("Position331", "8/8/8/R5pr/6qk/4Q3/6P1/7K w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test332")
	public void test332() {
		checkMate("Position332", "Q7/6R1/pk1p3p/1p5r/3P1P2/P7/1PP2K2/3q4 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test333")
	public void test333() {
		checkMate("Position333", "R4rk1/2p2pp1/1b6/2qQP3/8/1B6/2P3P1/7K w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test334")
	public void test334() {
		checkMate("Position334", "r4r2/pbR3pk/1p2pp1p/3q4/3P1Q2/4B2P/PP3PP1/5RK1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test335")
	public void test335() {
		checkMate("Position335", "5rk1/b1Q2ppp/8/3B4/3q4/8/5PPP/2R3K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test336")
	public void test336() {
		checkMate("Position336", "6rk/p1n3pp/3N2b1/3rP3/1P6/3R3R/5P2/1B4K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test337")
	public void test337() {
		checkMate("Position337", "2q3rk/6p1/2p1p2p/1p1pB3/1P1P4/4P2P/2Q2PPK/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test338")
	public void test338() {
		checkMate("Position338", "8/2prp1N1/2bk4/8/2K2P2/8/8/7R w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test339")
	public void test339() {
		checkMate("Position339", "8/1p4Q1/p6p/5q1k/5PR1/4r1PK/7P/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test340")
	public void test340() {
		checkMate("Position340", "6k1/pr3p1p/8/4P1B1/8/2n5/P5RP/K7 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test341")
	public void test341() {
		checkMate("Position341", "r1b1r1k1/p4Rpp/1ppq4/4N3/3np3/1Q6/PP4PP/3R2K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test342")
	public void test342() {
		checkMate("Position342", "4qrk1/1p4p1/b1p3Q1/5p1B/8/P5P1/1P3P1R/6K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test343")
	public void test343() {
		checkMate("Position343", "1r3rk1/2q2p1p/5RpQ/7P/8/8/1BP5/2K5 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test344")
	public void test344() {
		checkMate("Position344", "2kr4/1rpbQ2p/3p4/1q1P2R1/7B/6PB/1P6/1K6 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test345")
	public void test345() {
		checkMate("Position345", "8/8/8/8/kp1Q4/1p6/1KP5/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test346")
	public void test346() {
		checkMate("Position346", "3r4/3r4/1pb3p1/2pp1p2/p3k2p/PB2P3/1PP2PPP/4RNK1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test347")
	public void test347() {
		checkMate("Position347", "2N2k2/8/1rpR2B1/1P4K1/q2p4/8/8/8 w - - 0 1  ", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test348")
	public void test348() {
		checkMate("Position348", "4rr2/8/5b1R/2PKB3/k7/8/8/4R3 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test349")
	public void test349() {
		checkMate("Position349", "2r5/5r1p/8/3p1p2/2P5/2PNk3/1K4PP/7R w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test350")
	public void test350() {
		checkMate("Position350", "6bk/2p4p/q2p2pQ/8/8/2P1N3/6PK/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test351")
	public void test351() {
		checkMate("Position351", "1B6/2pp4/3k4/1P6/2K5/8/8/4R3 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test352")
	public void test352() {
		checkMate("Position352", "3r4/8/3k4/5P2/B2K4/8/8/7R w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test353")
	public void test353() {
		checkMate("Position353", "k1b5/p7/BPP5/7p/5q2/8/3P2KB/7Q b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test354")
	public void test354() {
		checkMate("Position354", "8/6Bq/8/b4bKB/4k3/8/8/8 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test355")
	public void test355() {
		checkMate("Position355", "8/kp3p1R/2p3p1/2b3B1/2r1p3/4P3/P4PPP/K7 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test356")
	public void test356() {
		checkMate("Position356", "8/kp1b2r1/p7/5P1P/5R1K/4P1r2/PPRP4/8 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test357")
	public void test357() {
		checkMate("Position357", "7k/5r2/7b/1Q6/n3q1b1/N1p4n/2B1N2R/3KR3 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test358")
	public void test358() {
		checkMate("Position358", "8/2p3k1/1q2b3/8/1r3pP1/3Q1K2/2P1B1R1/8 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test359")
	public void test359() {
		checkMate("Position359", "1r4k1/r5p1/8/3n4/8/6B1/N7/K5Q1 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test360")
	public void test360() {
		checkMate("Position360", "4r1k1/b1R2ppp/pq6/1p2Q3/1P2n3/16N/1B4BP/7K b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test361")
	public void test361() {
		checkMate("Position361", "R2b3k/Qpp3p1/2p4p/6BK/6PP/8/8/8 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test362")
	public void test362() {
		checkMate("Position362", "8/8/b7/6k1/8/1p2K1n1/8/q7 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test363")
	public void test363() {
		checkMate("Position363", "6k1/8/q3b3/1p6/1p6/1P1P4/1PK1B3/2B1Q3 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test364")
	public void test364() {
		checkMate("Position364", "3r3k/4R2p/5N2/pp6/1b6/RP6/PKPp2PP/8 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test365")
	public void test365() {
		checkMate("Position365", "6k1/4p2p/p1N3p1/1P3r2/N1pb4/8/PP4PP/2R2n1K b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test366")
	public void test366() {
		checkMate("Position366", "8/3k2p1/r5p1/1pKpB2p/3Pb2P/2P1P3/r5P1/3R2R1 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test367")
	public void test367() {
		checkMate("Position367", "1b6/7k/5Bp1/7q/3p3Q/1p1P4/1P1KRP2/r7 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test368")
	public void test368() {
		checkMate("Position368", "8/8/8/4PKP1/5n2/6k1/8/q7 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test369")
	public void test369() {
		checkMate("Position369", "r2Bk3/1p3ppr/p1pQ4/1q2P3/5n2/8/P1P2PP1/3RR1K1 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test370")
	public void test370() {
		checkMate("Position370", "7k/q4pp1/5p2/2n3Pb/3Qn3/3p4/BP3B2/2K4R b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test371")
	public void test371() {
		checkMate("Position371", "8/8/5b2/8/1p6/2k5/2P5/KB6 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test372")
	public void test372() {
		checkMate("Position372", "q7/8/4k2b/8/2K5/1nP5/8/8 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test373")
	public void test373() {
		checkMate("Position373", "1k3b1R/1p6/4q3/8/Kn6/B1Q5/8/8 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test374")
	public void test374() {
		checkMate("Position374", "8/1kp3p1/1p4b1/3pp2p/KB6/1P3PP1/P1nrBN2/4R2R b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test375")
	public void test375() {
		checkMate("Position375", "8/5p2/2R5/3K4/1N2n1k1/3B1r2/1b6/8 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test376")
	public void test376() {
		checkMate("Position376", "q5k1/8/8/8/8/7b/5Q1P/r4BKR b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test377")
	public void test377() {
		checkMate("Position377", "8/8/8/8/5b2/8/5Kp1/r2k2rR b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test378")
	public void test378() {
		checkMate("Position378", "1k5r/pp1b1ppp/4p3/q3P3/1n3P2/bPB2N2/2PP2PP/1KR1Q2R b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test379")
	public void test379() {
		checkMate("Position379", "r3r1k1/1p3p2/p7/3P1Q2/P5np/5B2/1P3NPq/R1B2K2 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test380")
	public void test380() {
		checkMate("Position380", "8/pk6/1p6/8/KQR5/PP6/8/r3q3 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test381")
	public void test381() {
		checkMate("Position381", "8/8/8/5n2/2N5/3n4/4N2k/5K2 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test382")
	public void test382() {
		checkMate("Position382", "6q1/n3b3/1NR2p2/1B2rp2/p2K1n1k/N1RP2rP/Q7/5b2 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test383")
	public void test383() {
		checkMate("Position383", "k7/3q1p2/2p5/1b6/P2n1P2/4b1p1/4N1P1/1R1QRK2 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test384")
	public void test384() {
		checkMate("Position384", "8/8/8/2P2R2/1Pp5/P7/K1k5/1Nb5 b - b3 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test385")
	public void test385() {
		checkMate("Position385", "1k5r/1p6/8/2q5/5p2/7P/6PK/2Q4R b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test386")
	public void test386() {
		checkMate("Position386", "k7/pp4p1/6b1/8/q2n4/2B1N3/1PP3P1/1K2Q3 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test387")
	public void test387() {
		checkMate("Position387", "k4r2/pp6/3p4/1b6/4PnPp/P7/1P2Rp1P/R2B1K2 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test388")
	public void test388() {
		checkMate("Position388", "k7/1p6/p1n3q1/8/8/4B2p/1Pb1KP1P/2R1Q3 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test389")
	public void test389() {
		checkMate("Position389", "3nrq2/2k5/2p2p2/p1Np4/P2K1PP1/1Q1P4/8/1R6 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test390")
	public void test390() {
		checkMate("Position390", "1k2r3/1b6/8/8/N3R2R/7n/5r2/2B4K b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test391")
	public void test391() {
		checkMate("Position391", "8/8/8/1k3p2/p4P2/K1pN4/P2b4/8 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test392")
	public void test392() {
		checkMate("Position392", "Q7/2N1p3/qn1bBN2/2R3n1/1pPKP3/5p2/8/1k2rb2 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test393")
	public void test393() {
		checkMate("Position393", "7k/8/b3q3/8/8/4p3/2B4N/2R1K1n1 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test394")
	public void test394() {
		checkMate("Position394", "6k1/2R1Q1p1/5n1p/1p1qpp2/1P1p4/3P2P1/4PP1P/r4BK1 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test395")
	public void test395() {
		checkMate("Position395", "8/kp4bp/p5p1/r1p1p3/P2pP2N/1r1P4/K1Pq2PP/2R1QR2 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test396")
	public void test396() {
		checkMate("Position396", "2kr4/4P1p1/p7/1p1r4/3PRKQq/3R1P2/P7/8 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test397")
	public void test397() {
		checkMate("Position397", "2q3k1/5p2/8/1b2Q1pp/3B4/5PP1/5PKP/8 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test398")
	public void test398() {
		checkMate("Position398", "1k1r4/pppq2p1/8/2B1p1N1/7p/4KP1P/PP1r4/1Q3R1R b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test399")
	public void test399() {
		checkMate("Position399", "8/kr4r1/1p6/2b5/4Qn2/6Rq/5N1P/5RKN b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test400")
	public void test400() {
		checkMate("Position400", "1q5k/8/p7/8/3r3p/P3PB2/P1b5/K1R4Q b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test401")
	public void test401() {
		checkMate("Position401", "1k6/1p1r4/8/8/q4p1Q/2N1bP2/2n3P1/1RB2K2 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test402")
	public void test402() {
		checkMate("Position402", "3N4/6pk/Qpq5/P7/8/b4r2/P1P5/RK6 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test403")
	public void test403() {
		checkMate("Position403", "8/r1q2p1k/5P2/8/8/7N/R6R/1K6 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test404")
	public void test404() {
		checkMate("Position404", "6rk/pb5p/1p3p1Q/2p2p2/3q4/8/PPP3RP/4R2K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test405")
	public void test405() {
		checkMate("Position405", "4Rrk1/1b4pp/8/1rp2N2/2n4B/2P1R3/1P5P/1K6 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test406")
	public void test406() {
		checkMate("Position406", "2Rr3k/2R4p/1q1r1p2/8/1p4P1/7P/PQ5K/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test407")
	public void test407() {
		checkMate("Position407", "r1b2rk1/p4ppp/8/8/4NQq1/8/P5PP/3R1RK1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test408")
	public void test408() {
		checkMate("Position408", "2n2r1k/5Pp1/4p1Pp/2ppP2P/3p3Q/2qP4/p1P5/5R1K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test409")
	public void test409() {
		checkMate("Position409", "5rk1/p4Npp/1p6/3n1R2/3PR3/8/P4KPP/2r5 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test410")
	public void test410() {
		checkMate("Position410", "r1b1rqk1/5p2/5Pp1/ppp1B1Q1/8/1P1P3R/P5PP/5RK1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test411")
	public void test411() {
		checkMate("Position411", "1brr2k1/pp3pp1/7p/8/5q1Q/8/PP2RPPP/1B2R1K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test412")
	public void test412() {
		checkMate("Position412", "6n1/2p2kpp/1r2R3/p4PP1/P1P1R3/2Pr4/5K2/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test413")
	public void test413() {
		checkMate("Position413", "8/1b3pkp/6p1/8/p4N2/Pq2Q1P1/2r4P/3R2K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test414")
	public void test414() {
		checkMate("Position414", "8/6Q1/8/8/8/8/pp1K1PP1/1k6 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test415")
	public void test415() {
		checkMate("Position415", "1k2r3/1pR2ppp/1P6/4N3/3P4/2P1P3/1K6/7b w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test416")
	public void test416() {
		checkMate("Position416", "2r2qrk/1p3ppp/p2p1b2/3NpP1Q/2P1R1R1/1P6/P5PP/6K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test417")
	public void test417() {
		checkMate("Position417", "6rk/r1q3pp/8/4ppN1/4P2N/2b5/B4PP1/Q6K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test418")
	public void test418() {
		checkMate("Position418", "3q4/p3NQpk/3r2pb/8/6P1/P4P2/8/6KR w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test419")
	public void test419() {
		checkMate("Position419", "2r4k/2p3R1/r1b4p/8/4p3/P7/B7/2K3R1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test420")
	public void test420() {
		checkMate("Position420", "5rk1/RR6/5np1/2p5/2P1b3/6qP/1Q4P1/7K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test421")
	public void test421() {
		checkMate("Position421", "4r2k/2r3pp/4B3/q3P3/1n6/1R5Q/4R2P/1K6 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test422")
	public void test422() {
		checkMate("Position422", "3qrk2/1p1b1pp1/4p3/4P3/5N1Q/6P1/1P4K1/2R5 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test423")
	public void test423() {
		checkMate("Position423", "5rk1/1pq2p2/5p2/8/3B1b2/7R/1P3PP1/3Q2K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test424")
	public void test424() {
		checkMate("Position424", "5rk1/2q2p2/6pQ/4R2n/8/1P6/PB6/1K6 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test425")
	public void test425() {
		checkMate("Position425", "1k1r4/n1p1N3/1p1b3q/8/1Q6/7P/6B1/1R5K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test426")
	public void test426() {
		checkMate("Position426", "5rk1/1b3p1p/p5p1/3q4/8/Q3B3/1P3PPP/4R1K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test427")
	public void test427() {
		checkMate("Position427", "r1br1bk1/1p3p1p/p3p2B/4Qp2/2P5/8/2q2PPP/1R1R2K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test428")
	public void test428() {
		checkMate("Position428", "1r4k1/bp3Rpp/p1n5/8/3P3N/1BP3P1/7P/6K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test429")
	public void test429() {
		checkMate("Position429", "8/1p3R2/4B3/b5k1/8/1n4PP/3p2K1/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test430")
	public void test430() {
		checkMate("Position430", "4r2r/1p2bpkp/2n3p1/4P3/8/2P5/B2B3Q/4K3 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test431")
	public void test431() {
		checkMate("Position431", "2q1b3/2p1kp2/4Pp2/8/8/8/5Q2/1B5K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test432")
	public void test432() {
		checkMate("Position432", "3q1k2/2pn4/6Q1/3n1N2/8/8/4K3/1B6 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test433")
	public void test433() {
		checkMate("Position433", "r4r1k/6p1/7p/6N1/1n6/8/B7/K1B4R w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test434")
	public void test434() {
		checkMate("Position434", "1b6/k1p2r2/2B5/KP2p3/7B/8/8/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test435")
	public void test435() {
		checkMate("Position435", "3q4/1p4pk/1np2nNp/8/3P4/2P5/B5PP/1Q4K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test436")
	public void test436() {
		checkMate("Position436", "r1b5/4B3/1p6/1p6/2p1N1Pp/P4P1k/7P/4K3 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test437")
	public void test437() {
		checkMate("Position437", "5k2/3r1p2/5p2/8/3N4/8/B6q/K3R1R1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test438")
	public void test438() {
		checkMate("Position438", "8/R1N5/5pkp/3K2pp/6P1/6P1/8/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test439")
	public void test439() {
		checkMate("Position439", "6n1/r4p1k/7p/q3N3/5Q2/8/1PP5/1K3R2 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test440")
	public void test440() {
		checkMate("Position440", "6r1/1p4pk/7n/1q3P2/4N3/8/1P1Q4/1K4R1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test441")
	public void test441() {
		checkMate("Position441", "2k5/1ppN4/3p3p/5Q2/8/bP5P/P1Pq2r1/1K3R2 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test442")
	public void test442() {
		checkMate("Position442", "5rk1/2p2ppp/2N5/8/R7/7P/1PQ1n1PK/5q2 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test443")
	public void test443() {
		checkMate("Position443", "q4r2/p4ppk/2R1n3/5N2/8/2Q3P1/1P3P2/5K2 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test444")
	public void test444() {
		checkMate("Position444", "r1b2rk1/pp3ppp/8/q1nNP3/2BRQ3/8/PP3PPP/2K4R w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test445")
	public void test445() {
		checkMate("Position445", "3qrk2/p1r2pp1/1p2pb2/nP1bN2Q/3PN3/P6R/5PPP/R5K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test446")
	public void test446() {
		checkMate("Position446", "2r2rk1/5pp1/p3pbP1/1p6/3q4/1B6/PPP1Q3/2K4R w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test447")
	public void test447() {
		checkMate("Position447", "1kr1q3/1p4Q1/1Pp5/8/4p3/6P1/8/R5K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test448")
	public void test448() {
		checkMate("Position448", "3r3k/R7/5P1p/2q5/8/8/Q7/7K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test449")
	public void test449() {
		checkMate("Position449", "1r3rk1/5pp1/6Qp/p1q1pPb1/7P/1B6/PPP5/K2R2R1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test450")
	public void test450() {
		checkMate("Position450", "4rk2/5p2/3q1P2/6R1/8/1p6/1P6/1KQ5 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test451")
	public void test451() {
		checkMate("Position451", "3Q4/kp2n3/2p2q2/1P4p1/1P6/4N1P1/7K/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test452")
	public void test452() {
		checkMate("Position452", "7R/4bQ2/pn4p1/1pqP2kp/6P1/5B2/5PKP/1b6 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test453")
	public void test453() {
		checkMate("Position453", "8/1p6/1P2q3/8/1P2k3/2Pp2P1/5Q2/6K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test454")
	public void test454() {
		checkMate("Position454", "r3rk2/2pR1p2/p3p1q1/6PQ/2p2P2/4P3/5PKP/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test455")
	public void test455() {
		checkMate("Position455", "3q2rk/5p1p/1p5Q/3b1N2/8/P5P1/8/4R1K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test456")
	public void test456() {
		checkMate("Position456", "4kbr1/4n1p1/4Q3/1p6/2q4B/5B2/1P4P1/5R1K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test457")
	public void test457() {
		checkMate("Position457", "k1r5/8/pp4p1/q2PQb2/8/4B2P/6PK/2R5 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test458")
	public void test458() {
		checkMate("Position458", "5k2/5p2/1p2bB2/1P2P3/2q5/8/8/2Q4K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test459")
	public void test459() {
		checkMate("Position459", "r3kb1r/1b1n2pp/pq1pN3/1p1Q2B1/4P3/8/PPP2PPP/R4RK1 w q - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test460")
	public void test460() {
		checkMate("Position460", "1qb3rk/2p2p1p/6pQ/1p6/1R6/B7/1P3PP1/6K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test461")
	public void test461() {
		checkMate("Position461", "1q1r1k2/2R5/8/6K1/8/8/8/2Q5 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test462")
	public void test462() {
		checkMate("Position462", "5k2/b4pq1/3p3Q/1prP4/8/3B4/1P5P/4R2K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test463")
	public void test463() {
		checkMate("Position463", "Q7/2q3bk/6Np/5p2/2p5/Pp6/1PP1r2P/1K4R1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test464")
	public void test464() {
		checkMate("Position464", "8/q1r3p1/1pp4k/6pp/8/P5P1/1PQ1B1PK/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test465")
	public void test465() {
		checkMate("Position465", "5nk1/3b2pp/3qp3/1p2N2Q/8/8/1P4PP/1B5K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test466")
	public void test466() {
		checkMate("Position466", "2q4k/p4Q1p/2n5/8/3b2N1/P6P/5B2/7K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test467")
	public void test467() {
		checkMate("Position467", "8/8/8/2K5/kp6/p6p/P2N4/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test468")
	public void test468() {
		checkMate("Position468", "8/8/8/8/3N4/p7/k1K5/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test469")
	public void test469() {
		checkMate("Position469", "8/8/8/8/6p1/1R6/7N/5K1k w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test470")
	public void test470() {
		checkMate("Position470", "r1b2nrk/pp3ppp/1q2p3/2bpn1N1/5N2/2PQ4/PPB2PPP/R1B2RK1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test471")
	public void test471() {
		checkMate("Position471", "r1b1k2r/2ppnN1p/5n2/pp5Q/4P3/1Bq5/P1P2KP1/R1B4R w qk - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test472")
	public void test472() {
		checkMate("Position472", "kr6/bpQ3pp/p2N4/3N4/3p4/P2b1P2/1P2q1PP/2R3K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test473")
	public void test473() {
		checkMate("Position473", "k1K5/1p6/pP6/R1P5/8/8/P1p5/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test474")
	public void test474() {
		checkMate("Position474", "5q2/p6R/1p5r/1k1pN3/1P3n2/2P5/P4P2/3Q1K2 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test475")
	public void test475() {
		checkMate("Position475", "5nk1/R4N1p/5P1Q/8/2p1q3/8/KP6/6r1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test476")
	public void test476() {
		checkMate("Position476", "1Q6/5pkp/6p1/p2qP3/3n3P/4B1PK/1P6/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test477")
	public void test477() {
		checkMate("Position477", "1r4rb/1R6/4R3/k7/6p1/3N4/1P6/3K4 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test478")
	public void test478() {
		checkMate("Position478", "8/2r2bn1/5Ppk/8/5PP1/R7/2B5/2K5 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test479")
	public void test479() {
		checkMate("Position479", "7r/4K1kp/5N1p/5PP1/8/8/8/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test480")
	public void test480() {
		checkMate("Position480", "2qk3r/2p1nQp1/3p4/3BP3/8/1P5P/6K1/5R2 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test481")
	public void test481() {
		checkMate("Position481", "r3r1k1/4qpp1/5R2/1p6/6Q1/bP6/1BP3P1/1K5R w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test482")
	public void test482() {
		checkMate("Position482", "4R3/1p1r1ppk/2q3b1/8/7P/2B1Q3/1P3P2/6K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test483")
	public void test483() {
		checkMate("Position483", "1k1rr3/1ppq1ppp/1b6/8/8/2P3P1/4QPBP/RR4K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test484")
	public void test484() {
		checkMate("Position484", "3Q1Q2/7k/6p1/4q2p/7P/6P1/7K/q7 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test485")
	public void test485() {
		checkMate("Position485", "2R2n2/3n1prk/5b1p/4qP2/p1p4B/2P4R/K1PQ4/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test486")
	public void test486() {
		checkMate("Position486", "3rkr1R/5p2/4p3/4P2Q/3q4/8/bPB5/2K4R w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test487")
	public void test487() {
		checkMate("Position487", "2r2rk1/pp2qp1p/3b1Qp1/4N3/8/2B5/PP3PPP/5RK1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test488")
	public void test488() {
		checkMate("Position488", "Q7/p2rkrb1/8/8/4N2q/8/1P2R1PB/7K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test489")
	public void test489() {
		checkMate("Position489", "6r1/1R3Nbk/6p1/8/8/2q1Q3/2P5/1K6 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test490")
	public void test490() {
		checkMate("Position490", "3n1k2/5pp1/8/p7/Q3BP2/1P4P1/3q3r/4R1K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test491")
	public void test491() {
		checkMate("Position491", "Q7/2rk1pp1/3b4/5q2/8/8/4BP2/4RK2 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test492")
	public void test492() {
		checkMate("Position492", "r1b1k2r/ppp3pp/1qp1P3/2b2p2/3Qn3/2N2N2/PPPB1PPP/3RK2R w Kk - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test493")
	public void test493() {
		checkMate("Position493", "3r1n1k/3P3p/pp3q2/2pQp3/P1P3B1/3b2R1/1P5P/6K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test494")
	public void test494() {
		checkMate("Position494", "5rk1/1b2bppp/1p2rB2/4P3/8/5P2/1P5K/R5R1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test495")
	public void test495() {
		checkMate("Position495", "r3r2k/1p4Rp/2b2Rp1/4B3/3P4/6P1/1P6/6K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test496")
	public void test496() {
		checkMate("Position496", "8/8/8/5N2/4n3/1K6/p7/k7 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test497")
	public void test497() {
		checkMate("Position497", "8/2p5/2pP4/2NkpK2/2p1N3/2P5/8/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test498")
	public void test498() {
		checkMate("Position498", "3Q4/8/2ppp3/3k4/3N4/8/3K4/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test499")
	public void test499() {
		checkMate("Position499", "8/8/8/p7/B7/kP6/8/1K2B3 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test500")
	public void test500() {
		checkMate("Position500", "k7/Pp6/1K6/5p2/4p2p/7P/6B1/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test501")
	public void test501() {
		checkMate("Position501", "8/8/3p4/pN2R3/k2p4/P2P4/K1P5/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test502")
	public void test502() {
		checkMate("Position502", "7r/7p/7P/8/2Q5/PB2p3/PpN1N3/1K2k3 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test503")
	public void test503() {
		checkMate("Position503", "k4r2/8/p5Pp/3P3Q/4r1R1/1p2q3/1P6/1KR5 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test504")
	public void test504() {
		checkMate("Position504", "1Q2n3/8/K1kpp3/8/8/2q1P3/5P2/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test505")
	public void test505() {
		checkMate("Position505", "8/kpp2Q2/1q5p/P7/1P3p1r/4RPb1/3Bb1P1/RN4K1 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test506")
	public void test506() {
		checkMate("Position506", "1q4k1/8/r5p1/4b1N1/b7/2Pn3Q/RP6/K1R5 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test507")
	public void test507() {
		checkMate("Position507", "1k1r4/1p4pp/4B3/8/1nQN4/1qn5/1P4PP/K4R2 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test508")
	public void test508() {
		checkMate("Position508", "r3r1k1/pp3pbp/3p2p1/2pP3b/2P1P3/2N4q/PP2BP1P/R3QR1K b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test509")
	public void test509() {
		checkMate("Position509", "7k/7p/5q2/8/4Q3/1P1B4/P2p4/1KR5 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test510")
	public void test510() {
		checkMate("Position510", "6r1/5p2/7k/5p1P/5PpK/6P1/8/7R b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test511")
	public void test511() {
		checkMate("Position511", "r4rk1/pp3ppp/1bb1p3/3n4/8/1NP3Pq/PP3PBP/R1BQ1RK1 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test512")
	public void test512() {
		checkMate("Position512", "1kr5/1p3p2/p2Kp3/2pn4/b7/8/P4P2/R3Q3 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test513")
	public void test513() {
		checkMate("Position513", "2r3k1/5pb1/q5p1/8/3B4/2P4Q/r5PR/5RK1 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test514")
	public void test514() {
		checkMate("Position514", "4r1r1/8/6n1/5p2/5P2/5KN1/3R3R/6k1 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test515")
	public void test515() {
		checkMate("Position515", "k3q3/b6b/8/3pP2p/8/P2P4/P2B4/R2KQ3 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test516")
	public void test516() {
		checkMate("Position516", "1k1r4/6p1/5b2/1p6/2n1B3/2r2P2/P7/1KR3NR b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test517")
	public void test517() {
		checkMate("Position517", "r1r3k1/1Q3ppp/p3p3/8/4n3/8/P3qPPP/2R1B1KR b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test518")
	public void test518() {
		checkMate("Position518", "r5k1/pp2bp1p/2p3p1/2P1R3/5n2/2P3Pq/PP1N1PbN/R2Q2K1 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test519")
	public void test519() {
		checkMate("Position519", "k3r3/pRR4p/5bp1/8/5Bb1/6PN/4pPKP/8 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test520")
	public void test520() {
		checkMate("Position520", "6k1/pbR2p1p/1p1p2pQ/8/6qP/6N1/P4PPK/4r3 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test521")
	public void test521() {
		checkMate("Position521", "2k3r1/1bp1qp2/pb6/4p2p/4n3/2P2N2/1PB1QP1P/RN3R1K b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test522")
	public void test522() {
		checkMate("Position522", "8/1pk3p1/p7/3p1b2/8/2P1K3/2q1PP1P/2B1Q3 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test523")
	public void test523() {
		checkMate("Position523", "2r4k/5q1p/8/p3N3/5P2/1n6/KPRQ4/8 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test524")
	public void test524() {
		checkMate("Position524", "7Q/2p5/pp4k1/2pPp3/P2q2r1/KP5P/5RP1/8 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test525")
	public void test525() {
		checkMate("Position525", "q5bk/7p/7P/pp2p3/1P1bP2B/P7/2N5/1KR4R b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test526")
	public void test526() {
		checkMate("Position526", "5k2/4r1p1/1p6/b6P/3p1N2/2nP4/2PK4/2B3R1 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test527")
	public void test527() {
		checkMate("Position527", "k7/1p5q/p2B3p/4p1pR/1PPpBP1R/3Pn3/r5PP/4K3 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test528")
	public void test528() {
		checkMate("Position528", "r5k1/r4qp1/4p3/3b4/1p6/1P3N2/1BP5/1KRQ1R2 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test529")
	public void test529() {
		checkMate("Position529", "8/2b5/4b1k1/B7/4p1PK/Q5PP/4p3/8 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test530")
	public void test530() {
		checkMate("Position530", "1r5q/8/2b5/8/3Q2R1/k4P2/4P3/K7 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test531")
	public void test531() {
		checkMate("Position531", "3b2k1/1p5p/1P5K/7P/4n2P/8/8/8 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test532")
	public void test532() {
		checkMate("Position532", "2k5/Bp3p2/8/2Kp2P1/3P2n1/6Q1/1P2qP2/8 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test533")
	public void test533() {
		checkMate("Position533", "2r3k1/6p1/8/P7/2q5/4P3/PK6/R3Q3 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test534")
	public void test534() {
		checkMate("Position534", "1k3r2/5P2/8/5N2/1p6/1Pb5/K1P5/5R2 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test535")
	public void test535() {
		checkMate("Position535", "k7/pp3p1q/8/2b5/2P1nPQ1/1P2B3/4R3/1K6 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test536")
	public void test536() {
		checkMate("Position536", "4q3/6k1/r6p/2p3b1/3nB3/1P1P3R/P4N2/1K5Q b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test537")
	public void test537() {
		checkMate("Position537", "1k1r4/pp2Q3/8/5p2/2P2P2/1P3n2/2K5/q1R2N2 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test538")
	public void test538() {
		checkMate("Position538", "5rk1/ppp2ppp/1q6/8/n2n4/P2P4/KPP1BQPP/3N1R2 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test539")
	public void test539() {
		checkMate("Position539", "1r3k2/p1p2pp1/2r2Pp1/3Qb3/3P4/1p2P2P/PP1q4/KR3B1R b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test540")
	public void test540() {
		checkMate("Position540", "4r2k/pQ2P2p/P7/2pqb3/3p1p2/8/3B2rP/4RR1K b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test541")
	public void test541() {
		checkMate("Position541", "k1r5/p4p2/2r3p1/3p3p/b2P3P/q2B1Q2/PpP1R1P1/1K3R2 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test542")
	public void test542() {
		checkMate("Position542", "4rk2/p3b3/4qpp1/R2Q3p/1PpPB3/2Pb1PP1/6KP/3n2BR b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test543")
	public void test543() {
		checkMate("Position543", "r7/1kp4b/5n1p/1p2p3/6p1/BP3rR1/KPq1N2P/3Q3R b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test544")
	public void test544() {
		checkMate("Position544", "1kr5/ppp3p1/1q2p3/1B1pP3/3P2P1/1P5r/K1P1Q2P/1Rb4R b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test545")
	public void test545() {
		checkMate("Position545", "1kr5/1p4p1/8/4p3/q5b1/3B2P1/1PPK4/2B1Q2R b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test546")
	public void test546() {
		checkMate("Position546", "1k2rq2/pp4pp/8/4N3/3n2P1/2n2PQ1/PPr4P/K1BR3R b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test547")
	public void test547() {
		checkMate("Position547", "1r4k1/6p1/q3b1P1/8/8/bP6/2P1N2Q/RK3R2 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test548")
	public void test548() {
		checkMate("Position548", "1R5K/8/6r1/8/1P6/6k1/7p/7b b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test549")
	public void test549() {
		checkMate("Position549", "6k1/1p2r1P1/2p5/2N5/1n1r4/2K5/1P6/6RR b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test550")
	public void test550() {
		checkMate("Position550", "r2k4/7b/8/8/3n4/8/PPPB4/K2R4 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test551")
	public void test551() {
		checkMate("Position551", "1k6/5n2/1P6/2K5/2P5/5b2/1q6/8 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test552")
	public void test552() {
		checkMate("Position552", "2k2r2/2p5/1pq5/p1p1n3/P1P4B/1R4Pp/2Q1R3/6K1 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test553")
	public void test553() {
		checkMate("Position553", "1b6/4R3/2B2p2/p4k2/P7/5K2/3r3P/8 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test554")
	public void test554() {
		checkMate("Position554", "k4rr1/p6R/P7/1R3q2/8/8/5p1P/1Q5K w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test555")
	public void test555() {
		checkMate("Position555", "r2q1rb1/4b1kp/2n2pP1/2n1pP1Q/p3P2P/3p2N1/PPp2RBN/R5K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test556")
	public void test556() {
		checkMate("Position556", "2r3k1/Q6R/3p1q1p1/3Pp1N1/pp1n4/7P/P4PP1/6K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test557")
	public void test557() {
		checkMate("Position557", "8/4nr1p/4p1k1/3qP1p1/R7/3p1P2/3Q2K1/7R w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test558")
	public void test558() {
		checkMate("Position558", "6k1/1R6/2pN2pb/2P5/6K1/5P1P/2N1r3/8 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test559")
	public void test559() {
		checkMate("Position559", "3r1r1k/1R5p/7R/N5p1/1p3nP1/1P6/P1P5/1K6 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test560")
	public void test560() {
		checkMate("Position560", "8/3K1PQ1/2p2P1B/2Rbk3/1P6/4N3/2B1Rr2/5n2 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test561")
	public void test561() {
		checkMate("Position561", "QBN5/2p4P/B3p3/1P1p4/3k1p2/1Pp3N1/8/R3K3 w Q - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test562")
	public void test562() {
		checkMate("Position562", "5RK1/7N/6pk/7P/4p1P1/1p6/5B2/1q3n2 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test563")
	public void test563() {
		checkMate("Position563", "5b2/4p1p1/5N2/3p1N2/3p1B1p/2pk2p1/4R3/1R1B2K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test564")
	public void test564() {
		checkMate("Position564", "6k1/3Q2p1/p3N2p/1p5P/1P6/2q2PP1/3n2K1/8 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test565")
	public void test565() {
		checkMate("Position565", "2b1k2r/1ppNbppp/3P4/1B6/8/2P2qP1/1P3P2/4R1K1 w k - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test566")
	public void test566() {
		checkMate("Position566", "6Q1/8/3p3k/5q1p/7K/4P1nP/R7/8 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test567")
	public void test567() {
		checkMate("Position567", "1r2r3/3b3p/8/1p2p3/4P1k1/P7/3Q2KP/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test568")
	public void test568() {
		checkMate("Position568", "6kr/1p2qp1p/p1n3pB/8/2P1N3/6P1/4QP2/6K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test569")
	public void test569() {
		checkMate("Position569", "6k1/pp3ppp/1b6/3Q2Pb/P3PN2/2p3P1/2P3qP/4KR2 b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test570")
	public void test570() {
		checkMate("Position570", "r2r4/p2n1k1B/1p1q1p1Q/2pPp3/2P5/8/PP3PPP/2R3K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test571")
	public void test571() {
		checkMate("Position571", "r4bkr/pp1q2pp/8/6N1/8/2P2Q2/1PN2PPP/2K1R3 w - - 0 1", 5); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test572")
	public void test572() {
		checkMate("Position572", "6K1/8/4Q1bk/6p1/6B1/8/8/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test573")
	public void test573() {
		checkMate("Position573", "8/4R1pk/2pN2r1/2Pb4/3B3P/7K/4R3/3r4 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test574")
	public void test574() {
		checkMate("Position574", "6k1/2p3p1/1p2p2p/1n6/2N5/1P1QP3/r3q1PP/5R1K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test575")
	public void test575() {
		checkMate("Position575", "2B3K1/8/3N1p1p/6pk/5P1P/6P1/7r/5R2 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test576")
	public void test576() {
		checkMate("Position576", "r4b1r/pppQ4/2n1R2p/q7/5P1k/8/PPP3PP/R5K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test577")
	public void test577() {
		checkMate("Position577", "r2bk3/pp4q1/3Q2Np/8/6B1/6K1/P7/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test578")
	public void test578() {
		checkMate("Position578", "7k/1pp5/p2n4/4p1Nn/2q4P/5Q2/PPP5/2K5 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test579")
	public void test579() {
		checkMate("Position579", "8/1N4r1/3p2p1/p2k4/P2P4/3K4/6N1/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test580")
	public void test580() {
		checkMate("Position580", "7K/2p5/n1P3bk/3p2pp/Q5P1/3rB3/5P1P/6r1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test581")
	public void test581() {
		checkMate("Position581", "8/8/ppq3n1/2pRQ3/8/4Pk2/PPb2P2/6K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test582")
	public void test582() {
		checkMate("Position582", "8/2n2pp1/2pp3p/5P1k/p2BP2P/P2P1K2/1rP3P1/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test583")
	public void test583() {
		checkMate("Position583", "r6r/4ppk1/2bp1R2/6N1/p2pP1PQ/3P3P/PPq5/3R3K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test584")
	public void test584() {
		checkMate("Position584", "8/6K1/2Q2B2/5pn1/2N3pk/1P5n/4qP2/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test585")
	public void test585() {
		checkMate("Position585", "1r3k2/Q2n1p2/5p2/3q1N2/1p2p1P1/3p4/PP4P1/2R3K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test586")
	public void test586() {
		checkMate("Position586", "k1n5/pR6/PP1bp3/3pNp2/8/1K2P3/3B3r/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test587")
	public void test587() {
		checkMate("Position587", "5rr1/7k/p1p3Rp/1p1q3P/1P2Q3/5NP1/5PK1/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test588")
	public void test588() {
		checkMate("Position588", "KB1b4/4p3/B4p2/p5p1/7p/3p3k/2R5/3q4 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test589")
	public void test589() {
		checkMate("Position589", "7K/8/5ppk/6p1/5BPP/8/7r/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test590")
	public void test590() {
		checkMate("Position590", "6k1/6p1/p3p1Kp/P3p3/4Pr2/2PPN3/1P2nPPN/R5R1 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test591")
	public void test591() {
		checkMate("Position591", "2r4k/5q1n/2bp1P1R/p7/3B4/1P1Q2N1/1PP3rP/5K2 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test592")
	public void test592() {
		checkMate("Position592", "rnbq1r2/pppp2kp/1b3N2/4N1pQ/2BP4/6p1/PPP4P/R4K1R w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test593")
	public void test593() {
		checkMate("Position593", "r1b1qrk1/ppppnNp1/2n4p/2b1p2Q/2B1P3/3P4/PPP2PPP/RNB1K2R w KQ - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test594")
	public void test594() {
		checkMate("Position594", "r1b1k2r/ppp2ppp/2p5/8/3Qn1q1/8/PPPB2PP/2KR1B1R w kq - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test595")
	public void test595() {
		checkMate("Position595", "rnb1kb1r/pp3ppp/2p5/4q3/4n3/3Q4/PPPB1PPP/2KR1BNR w kq - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test596")
	public void test596() {
		checkMate("Position596", "2kr1bnr/Qp1bpppp/2n5/3q4/8/2P5/PP3PPP/RNB1KB1R b KQ - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test597")
	public void test597() {
		checkMate("Position597", "r1b1k2r/ppp3pp/1qp5/2b1Pp2/3Qn3/5N2/PPPBNPPP/3RK2R w Kkq - 0 1", 6); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test598")
	public void test598() {
		checkMate("Position598", "4r3/p4pkp/q7/3Bbb2/P2P1ppP/2N3n1/1PP2KPR/R1BQ4 b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test599")
	public void test599() {
		checkMate("Position599", "k6r/rb3R1p/NQ4p1/2p2n2/1p6/8/PPn3PP/6K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test600")
	public void test600() {
		checkMate("Position600", "6k1/6p1/p3p1Kp/P3p3/4Pr2/2PPN3/1P2nPPN/R5R1 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test601")
	public void test601() {
		checkMate("Position601", "2r4k/5q1n/2bp1P1R/p7/3B4/1P1Q2N1/1PP3rP/5K2 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test602")
	public void test602() {
		checkMate("Position602", "rnbq1r2/pppp2kp/1b3N2/4N1pQ/2BP4/6p1/PPP4P/R4K1R w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test603")
	public void test603() {
		checkMate("Position603", "6k1/Q5p1/7p/P3B2K/4q3/8/7P/8 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test604")
	public void test604() {
		checkMate("Position604", "1r1r1k2/2R2p1p/8/4P3/3PnN2/8/1q3PQ1/6K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test605")
	public void test605() {
		checkMate("Position605", "Q4b1r/3knn1p/4pPp1/B2pP3/8/8/6PP/6K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test606")
	public void test606() {
		checkMate("Position606", "RQ6/5bkp/1pp5/4p2q/1P2Pp2/3P3P/2PN1br1/5K2 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test607")
	public void test607() {
		checkMate("Position607", "3r2k1/4R2p/6p1/2Q5/2P5/1R3q2/P2r3P/5BK1 b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test608")
	public void test608() {
		checkMate("Position608", "2r2r1k/3bqpp1/3ppN1p/1p6/p2P3R/2R3Q1/PP4PP/6K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test609")
	public void test609() {
		checkMate("Position609", "2r1b2k/p3p2p/3p1pp1/1p1Q4/2q1P3/5P2/PPr3P1/1KBR3R w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test610")
	public void test610() {
		checkMate("Position610", "3R4/r4p2/2b1np2/4N3/6Pk/4P3/5K2/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test611")
	public void test611() {
		checkMate("Position611", "3rr2Q/pq3k2/1p2p2p/2p1P1b1/8/8/PKP1B1PP/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test612")
	public void test612() {
		checkMate("Position612", "8/6p1/p3Q3/1kpn2q1/2N5/2P2P2/1K4P1/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test613")
	public void test613() {
		checkMate("Position613", "4Rrk1/2q2p2/3p2p1/7p/3Q4/5P2/PPr3P1/1K1R4 w - - 0 1", 5); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test614")
	public void test614() {
		checkMate("Position614", "8/1r2bp2/3p2p1/p5k1/1n2R3/2BP4/5PKP/5N2 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test615")
	public void test615() {
		checkMate("Position615", "4r1k1/p2Q2p1/1q5p/8/5n2/1P3P2/1B1p2PP/R4K2 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test616")
	public void test616() {
		checkMate("Position616", "3Q4/1k6/8/1P6/8/8/1P4K1/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test617")
	public void test617() {
		checkMate("Position617", "8/1r3Q1R/r2k4/p7/5p2/1PPp4/5qPK/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test618")
	public void test618() {
		checkMate("Position618", "5r2/6Q1/2p5/2kqP3/2p5/2P2p2/P7/1K6 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test619")
	public void test619() {
		checkMate("Position619", "8/3Q4/1k6/8/1P3K2/8/8/3N4 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test620")
	public void test620() {
		checkMate("Position620", "4r3/2pNqk2/p1Pp3Q/6pb/4P3/P5P1/5PK1/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test621")
	public void test621() {
		checkMate("Position621", "4rb2/3r3p/p7/5p2/2n4k/2Qqp3/PPP4P/1K4R1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test622")
	public void test622() {
		checkMate("Position622", "r5k1/p3Qpbp/2p3p1/1p6/q3bN2/6PP/PP3P2/K2RR3 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test623")
	public void test623() {
		checkMate("Position623", "2r1nr1k/pp1q1p1p/3bpp2/5P2/1P1Q4/P3P3/1B3P1P/R3K1R1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test624")
	public void test624() {
		checkMate("Position624", "4N1QB/1K2k1P1/4N3/8/8/8/8/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test625")
	public void test625() {
		checkMate("Position625", "8/2P1P1P1/3PkP2/8/4K3/8/8/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test626")
	public void test626() {
		checkMate("Position626", "6R1/8/4N3/8/8/7k/8/5K2 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test627")
	public void test627() {
		checkMate("Position627", "3R3K/8/5Q2/8/5N2/1B6/2PB4/1qbk4 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test628")
	public void test628() {
		checkMate("Position628", "8/5p2/4pQ2/4Rb2/6k1/3P4/6PK/6R1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test629")
	public void test629() {
		checkMate("Position629", "2B5/8/8/1n6/1P1k2N1/8/P2B4/2K3NQ w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test630")
	public void test630() {
		checkMate("Position630", "1r5r/kp3p2/6pn/pP1N1b1p/1n2pB2/2R3P1/P6P/3R2K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test631")
	public void test631() {
		checkMate("Position631", "r5rk/pp1b1p1p/1qn2PpQ/5nPP/5P2/1PP5/2B5/R1B1K2R w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test632")
	public void test632() {
		checkMate("Position632", "1r5k/b1pR1B2/p6p/Pp2NP2/8/2P4P/1r2n3/R6K w - - 0 1", 7); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test633")
	public void test633() {
		checkMate("Position633", "8/8/7p/4N1kb/3nPRN1/6Q1/8/3K4 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test634")
	public void test634() {
		checkMate("Position634", "8/8/3n4/QpR5/3p4/1k1B4/p7/K2b4 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test635")
	public void test635() {
		checkMate("Position635", "5Br1/2N1NkP1/7Q/8/5n2/3n4/7K/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test636")
	public void test636() {
		checkMate("Position636", "4RQ2/8/4bB2/4N3/3k4/2N5/1P4n1/6K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test637")
	public void test637() {
		checkMate("Positio637", "r1b1kb1r/1p1n1p2/p1n1N2p/4P1p1/q3N2B/8/P1PQB1PP/1R2K2R w Kkq - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test638")
	public void test638() {
		checkMate("Position638", "1Nb1r3/7Q/2NkP3/R7/8/2K4B/8/8 w Q - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test639")
	public void test639() {
		checkMate("Position639", "1N2R3/5n2/1K1k4/3ppp2/2p5/8/5R2/7Q w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test640")
	public void test640() {
		checkMate("Position640", "7r/6p1/8/6R1/r6k/5P2/3p3K/4b1Q1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test641")
	public void test641() {
		checkMate("Position641", "2b5/6R1/4Nk2/8/3P2Pb/1Q5B/K5n1/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test642")
	public void test642() {
		checkMate("Position642", "5b2/1Q6/1P4R1/3rkP2/8/5R1K/5N2/6B1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test643")
	public void test643() {
		checkMate("Position643", "6nN/1Q6/3pk2n/4p2N/7P/3B4/8/5K2 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test644")
	public void test644() {
		checkMate("Position644", "6K1/8/8/8/1R6/1N6/2kp4/QbN2B2 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test645")
	public void test645() {
		checkMate("Position645", "rn3rk1/pbppq1pp/1p2pb2/4N2Q/3PN3/3B4/PPP2PPP/R3K2R w QK - 0 1", 7); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test646")
	public void test646() {
		checkMate("Position646", "2q1rb1k/prp3pp/1pn1p3/5p1N/2PP3Q/6R1/PP3PPP/R5K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test647")
	public void test647() {
		checkMate("Position647", "8/pb4qk/1p5p/4r1PR/2B1p3/4Qr2/PPP5/1K4R1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test648")
	public void test648() {
		checkMate("Position648", "1r2r1k1/5p2/5Rp1/4Q2p/P2B2qP/1NP5/1KP5/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test650")
	public void test650() {
		checkMate("Position650", "6rk/1p3ppp/q7/pn2QN2/8/PP2P1P1/5P1P/2R3K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test651")
	public void test651() {
		checkMate("Position651", "r6k/ppp4q/2nb1prB/3Bp2Q/4P3/2P4R/PP3PP1/4R1K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test652")
	public void test652() {
		checkMate("Position652", "3rkb1r/1bq2ppp/p3p3/1p2PP2/5P2/P1N1p3/1PP1BB1P/R3QRK1 b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test653")
	public void test653() {
		checkMate("Position653", "6k1/2P3pp/8/4rp2/P4n2/7q/2R1PK2/6Q1 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test654")
	public void test654() {
		checkMate("Position654", "r2qrk2/ppp3pQ/5p2/3Np1b1/2B1P1P1/P2P4/1PP5/1K5R w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test655")
	public void test655() {
		checkMate("Position655", "r1b1k1nr/1p1n1pbp/p1pQ2p1/4p3/2B1PB2/2q2N2/P1P2PPP/1R1R2K1 w - - 0 1", 5); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test656")
	public void test656() {
		checkMate("Position656", "6k1/4p2p/7B/3K4/8/8/8/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test657")
	public void test657() {
		checkMate("Position657", "6k1/pp3pp1/1b5p/5q2/2br4/Q1N3P1/PP3PBP/4R1K1 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test658")
	public void test658() {
		checkMate("Position658", "r1bq3r/pp3pp1/2n1p1k1/2pp2P1/3P1BQ1/2P1P3/PP1N1PP1/R3K2R w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test659")
	public void test659() {
		checkMate("Position659", "q5rk/6rp/1p2Pb2/1B3P2/PP6/4QR2/7P/3R3K b - - 0 1", 6); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test660")
	public void test660() {
		checkMate("Position660", "4R3/Q4Bpk/7p/7b/Pq6/6P1/3r1P1P/6K1 b - - 0 1", 5); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test662")
	public void test662() {
		checkMate("Position662", "8/8/4p2Q/2K1p3/4k3/2p2r2/5RRp/7B w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test663")
	public void test663() {
		checkMate("Position663", "R7/4k3/2n2N2/7Q/8/4B2B/1K6/4R3 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test664")
	public void test664() {
		checkMate("Position664", "r3r1k1/pp1n2Bp/5P2/2Q5/3pb3/3q2P1/P5BP/R4R1K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test665")
	public void test665() {
		checkMate("Position665", "8/7K/2Rn1N2/6k1/6N1/2n4p/8/4QR2 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test666")
	public void test666() {
		checkMate("Position666", "3r3k/4Qp1p/p3p3/1pR5/3qb2P/P5R1/1P3PP1/6K1 w - - 0 1", 8); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test667")
	public void test667() {
		checkMate("Position667", "3Rn2K/8/Q4N2/4k3/4B3/6P1/b6B/b4r2 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test668")
	public void test668() {
		checkMate("Position668", "4Q3/1R6/3p4/3k1NK1/1P6/3p4/3P4/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test669")
	public void test669() {
		checkMate("Position669", "1nq2rk1/r4p1p/4RRp1/2p5/2B5/8/P5PP/5Q1K w - - 0 1", 9); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test670")
	public void test670() {
		checkMate("Position670", "n1k1n2r/Qb1r2q1/1p1p1p2/1PpPpPp1/2P1P1P1/6R1/3B1KB1/R1N5 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test671")
	public void test671() {
		checkMate("Position671", "7k/p2R4/2p2p1P/5K2/1PP5/8/r2B2pb/8 w - - 0 1", 10); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test672")
	public void test672() {
		checkMate("Position672", "2k4r/B1p2pp1/1p4n1/1P6/P1P3q1/4PNbr/2Q2R1P/6RK b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test673")
	public void test673() {
		checkMate("Position673", "r4rk1/1p3pp1/2ppbq2/p1b5/2P5/PPN1P1Q1/1B3PP1/R3K2R w QK - 0 1", 11); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test674")
	public void test674() {
		checkMate("Position674", "b2r4/5Rbk/6pp/qPp1p3/2B2n1N/6NP/5PP1/1Q4K1 w - - 0 1", 13); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test675")
	public void test675() {
		checkMate("Position675", "2r2nk1/1b1r2p1/p3p1p1/1p2P1N1/7Q/7P/qPP2RP1/4R1K1 w - - 0 1", 5); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test676")
	public void test676() {
		checkMate("Position676", "r1bk2r1/1p1nR2p/p2Q1pqB/2pp1N2/3P4/P7/5PPP/2R3K1 w - - 0 1", 6); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test677")
	public void test677() {
		checkMate("Position677", "3rr3/1qb1kp1P/2p1pp2/ppPn4/3P1pQ1/1N4P1/PP4B1/2R1R1K1 w - - 0 1", 6); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test678")
	public void test678() {
		checkMate("Position678", "r1b2r2/1p1nq1b1/1np1p1kp/p7/3PN3/1P2B3/2Q1BPPP/2RR2K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test679")
	public void test679() {
		checkMate("Position679", "1qB3k1/5pp1/1Bb4p/2P4n/1P1p4/P4n2/1N2Q1PP/2R2N1K b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test680")
	public void test680() {
		checkMate("Position680", "2b1rk2/r3bppB/p2Q4/2N5/1qP5/8/6PP/5RBK w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test681")
	public void test681() {
		checkMate("Position681", "3rk1r1/1q3p1p/pn6/1pb1PN2/1Pp5/8/R3Q1PP/1NB4K b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test682")
	public void test682() {
		checkMate("Position682", "r3rbnk/p2bq1np/2p1p2B/6N1/PppPPN1Q/8/1P4BP/3R2RK w - - 0 1", 5); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test683")
	public void test683() {
		checkMate("Position683", "3b1rk1/1pqb1p2/6p1/1P2p3/5R2/2NP2Q2/6PP/5R1K w - - 0 1", 6); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test684")
	public void test684() {
		checkMate("Position684", "8/4KR2/4p3/1N6/2b1kB2/3R4/4np2/Q3n3 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test685")
	public void test685() {
		checkMate("Position685", "r3k3/pB1pp1K1/N3r1p1/3Qp3/8/8/8/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test686")
	public void test686() {
		checkMate("Position686", "2KBB3/8/p7/1n3Q2/k1p5/P2R1p2/6b1/R1N1b3 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test687")
	public void test687() {
		checkMate("Position687", "1Kb5/1p6/R5Bp/3N1nk1/2r1pR1N/B6P/7b/2Q5 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test688")
	public void test688() {
		checkMate("Position688", "8/p7/Q4pN1/K3p3/2r5/3k1B2/2p1N3/2B5 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test689")
	public void test689() {
		checkMate("Position689", "8/1n6/1n6/3p4/1k2p3/8/8/RNBQKB2 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test690")
	public void test690() {
		checkMate("Position690", "6b1/3B2N1/4P1p1/8/6kB/8/K6Q/6Nn w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test691")
	public void test691() {
		checkMate("Position691", "3r4/1p6/7b/8/Q7/3R1R1P/3Bk1N1/1K6 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test692")
	public void test692() {
		checkMate("Position692", "N1K5/3pQ3/n7/kP1R4/b2N4/1p6/1P6/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test693")
	public void test693() {
		checkMate("Position693", "3r3k/p1R1R3/1p3qrp/5p2/7n/P7/1P2QP1P/7K b - - 0 1", 7); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test694")
	public void test694() {
		checkMate("Position694", "5rk1/Q4p2/1Rr1p2p/2P5/3P1qn1/P2B2pP/3R2P1/2B3K1 b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test695")
	public void test695() {
		checkMate("Position695", "2kr3r/1b1p2p1/p1qBpp2/6p1/8/1Q1B3P/P1P2PP1/1R4K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test696")
	public void test696() {
		checkMate("Position696", "q4r1k/5p1p/3pp2Q/1p2b3/8/p4R2/7P/6RK w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test697")
	public void test697() {
		checkMate("Position697", "8/p7/rp6/kp6/1p6/1P6/1P2N3/1K3n2 w - - 0 1", 6); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test698")
	public void test698() {
		checkMate("Position698", "8/3P4/8/8/8/1KN1k1B1/4P3/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test699")
	public void test699() {
		checkMate("Position699", "4rb1k/1b1n2pp/p7/q7/1pB1pN2/1P2Q3/PB4PP/2R3K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test700")
	public void test700() {
		checkMate("Position700", "2nrkb1r/2q2p1p/p2p1p2/1p1QpN1B/4P3/2P5/PP3PPP/R2R2K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test701")
	public void test701() {
		checkMate("Position701", "4r1k1/5pp1/p6p/3n4/Pp2r3/8/1qQB2PP/2NK1B1R b - - 0 1", 8); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test702")
	public void test702() {
		checkMate("Position702", "5r1k/1R4b1/4BqPp/5P2/3p1Q2/6PK/P7/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test703")
	public void test703() {
		checkMate("Position703", "6rk/5p1p/4qb2/p1n1pNr1/PpPpPp2/1P3R1Q/3N2PP/3R3K w - - 0 1", 6); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test704")
	public void test704() {
		checkMate("Position704", "1r3nk1/p5N1/4p1PQ/1n1p4/2p2PP1/q2B4/Pp5R/1K1R4 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test705")
	public void test705() {
		checkMate("Position705", "6rk/1p3ppp/q7/pn2QN2/8/PP2P1P1/5P1P/2R3K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test707")
	public void test707() {
		checkMate("Position707", "8/1K6/4R1Bk/4Bpp1/7n/8/8/7Q w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test708")
	public void test708() {
		checkMate("Position708", "KQ2nrrq/2p5/3p4/8/2p5/2k1B2R/2BR4/n7 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test709")
	public void test709() {
		checkMate("Position709", "8/4b2N/3p4/R2prk1B/Q4N2/3Kp3/8/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test710")
	public void test710() {
		checkMate("Position710", "4r1k1/3b1p2/5qp1/1BPpn2p/7n/r3P1N1/2Q1RPPP/1R3NK1 b - - 0 1", 5); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test711")
	public void test711() {
		checkMate("Position711", "4r3/pp3Bpk/2pB1n1p/2P5/1P2p2r/5bNq/PQ6/R3R1K1 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test712")
	public void test712() {
		checkMate("Position712", "8/1K6/NQ6/1N6/k6b/1rp5/pqB5/2B5 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test713")
	public void test713() {
		checkMate("Position713", "8/8/6B1/5R2/1bN5/p3p3/2k1p2R/Q6K w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test714")
	public void test714() {
		checkMate("Position714", "1Q1BRK2/8/3p1R1N/2q2N2/5k1P/8/6P1/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test715")
	public void test715() {
		checkMate("Position715", "KQ6/8/2R5/3p2p1/3k4/4n2r/1P3N2/4N3 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test716")
	public void test716() {
		checkMate("Position716", "2b5/B1kq3R/8/3QR3/8/8/8/K7 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test717")
	public void test717() {
		checkMate("Position717", "8/3R1K2/2B1N2p/4k1P1/q2p1pQ1/2n4r/4PN2/2b2r2 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test718")
	public void test718() {
		checkMate("Position718", "8/5p2/3Q3b/3B1k1b/5P2/7P/5K2/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test719")
	public void test719() {
		checkMate("Position719", "1r1r4/8/1n6/2p5/2P5/1NK5/R6R/1k6 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test720")
	public void test720() {
		checkMate("Position720", "2kr3r/pR3pp1/B1nq1b1p/5P2/4pBPP/8/PP3P2/2R1K3 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test721")
	public void test721() {
		checkMate("Position721", "r1rR4/5kpp/p7/5p2/q2NpB2/4P3/5PPP/3Q2K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test722")
	public void test722() {
		checkMate("Position722", "8/4n3/3p1nk1/1RpPprNp/2P4P/Q5B1/6P1/7K b - - 0 1", 6); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test723")
	public void test723() {
		checkMate("Position723", "8/4pkPp/7B/3K4/8/8/8/8 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test724")
	public void test724() {
		checkMate("Position724", "1k6/p3qp1p/PB2p3/3bb3/2R1nP2/1Q6/1P4r1/5R1K b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test725")
	public void test725() {
		checkMate("Position725", "1B6/8/6b1/R2p4/2pkN1Q1/8/2P2P2/b2K4 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test726")
	public void test726() {
		checkMate("Position726", "4Qbk1/2qnpp1p/3p2pB/2pP4/4N3/7P/r4PPK/4R3 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test727")
	public void test727() {
		checkMate("Position727", "k7/pb6/1p1Q4/N4q2/5pr1/8/6PP/3R3K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test728")
	public void test728() {
		checkMate("Position728", "7r/pp1qrpk1/2p2Rp1/8/P2pP1PQ/1P1P1R2/2P5/6K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test729")
	public void test729() {
		checkMate("Position729", "8/4n3/3p1nk1/1RpPprNp/2P4P/Q5B1/6P1/7K b - - 0 1", 6); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test730")
	public void test730() {
		checkMate("Position730", "r3k2r/p1qp1ppp/b1pbp3/8/4P1n1/2N2N1P/PPP2PP1/R1BQR1K1 b kq - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test731")
	public void test731() {
		checkMate("Position731", "k7/Pp6/1p6/1p3B2/1p3K2/1p6/8/R7 w - - 0 1", 6); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test732")
	public void test732() {
		checkMate("Position732", "q4r1k/5p1p/3pp2Q/1p2b3/8/p4R2/7P/6RK w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test733")
	public void test733() {
		checkMate("Position733", "8/2N4n/4R3/3p1K1k/7p/5p1R/6P1/4n1q1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test734")
	public void test734() {
		checkMate("Position734", "8/p7/rp6/kp6/1p6/1P6/1P2N3/1K3n2 w - - 0 1", 6); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test735")
	public void test735() {
		checkMate("Position735", "5rk1/1p1b2pp/pq6/3pP3/3N4/P5PP/1P1BKbB1/R2Q3R b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test736")
	public void test736() {
		checkMate("Position736", "6rk/pp1b4/3p3p/3P1p2/2P2Q1P/7K/PP2BR2/6q1 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test737")
	public void test737() {
		checkMate("Position737", "r4rk1/pp3pB1/5b2/2p1nN1P/6Q1/2Pq4/PP3PP1/K6R w - - 0 1", 6); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test738")
	public void test738() {
		checkMate("Position738", "6rk/5p1p/4qb2/p1n1pNr1/PpPpPp2/1P3R1Q/3N2PP/3R3K w - - 0 1", 6); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test739")
	public void test739() {
		checkMate("Position739", "6k1/2P3pp/8/4rp2/P4n2/7q/2R1PK2/6Q1 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test740")
	public void test740() {
		checkMate("Position740", "1Q6/1N3ppk/7p/8/5nqP/1P6/P2R4/7K b - - 0 1", 8); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test741")
	public void test741() {
		checkMate("Position741", "r1b1r1k1/ppq2p1p/3b1p1Q/2n4p/8/3B4/PPP2PPP/R1B2RK1 w - - 0 1", 6); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test742")
	public void test742() {
		checkMate("Position742", "2R5/4bppk/1p1p4/5R1P/4PQ2/5P2/r4q1P/7K w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test745")
	public void test745() {
		checkMate("Position745", "r1b1k1nr/1p1n1pbp/p1pQ2p1/4p3/2B1PB2/2q2N2/P1P2PPP/1R1R2K1 w - - 0 1", 5); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test746")
	public void test746() {
		checkMate("Position746", "1nq2rk1/r4p1p/4RRp1/2p5/2B5/8/P5PP/5Q1K w - - 0 1", 9); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test747")
	public void test747() {
		checkMate("Position747", "2q5/2pp4/3pr3/4pb2/K1p2pn1/2bn1kp1/3pr1R1/6BB w - - 0 1", 12); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test748")
	public void test748() {
		checkMate("Position748", "1q2r1k1/2p1bpp1/8/1r6/8/1B6/1B6/1K2Q2R w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test749")
	public void test749() {
		checkMate("Position749", "3r2k1/1b3p1p/p4PpQ/2q1P3/2p5/8/P1r2RPP/5RK1 b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test750")
	public void test750() {
		checkMate("Position750", "2r4k/p5p1/3Qq2p/4N3/Pp3n2/6P1/PK5P/3R4 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test751")
	public void test751() {
		checkMate("Position751", "k1r5/p6r/1b6/1N3n2/5Qp1/3B1pBq/5P2/R5K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test752")
	public void test752() {
		checkMate("Position752", "5rk1/ppp2ppp/2nr4/5Q2/2B5/5N2/PqPB1PPP/2RKR3 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test753")
	public void test753() {
		checkMate("Position753", "1q2r1k1/2p1bpp1/8/1r6/8/1B6/1B6/1K2Q2R w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test754")
	public void test754() {
		checkMate("Position754", "5rk1/4Rp1p/5QpB/q4n2/8/1P7/r5PP/3R3K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test755")
	public void test755() {
		checkMate("Position755", "8/6B1/8/4p3/3P4/PKP5/1PB5/k7 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test756")
	public void test756() {
		checkMate("Position756", "2r1kbr1/p4p1p/1qN1pB2/1Q2P1p1/3P4/4P3/PP4PP/2R2RK1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test757")
	public void test757() {
		checkMate("Position757", "k7/P7/P7/P7/P7/P7/P7/R3K3 w Q - 0 1", 8); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test758")
	public void test758() {
		checkMate("Position758", "1k1r3r/1b1q1ppp/1R1b1n2/8/8/2R3P1/1PP3BP/2BQ2K1 w Q - 0 1", 6); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test759")
	public void test759() {
		checkMate("Position759", "3r1rk1/pbp3bp/1p2p3/n3Pp2/P2P1P1q/2PBK1N1/1B2Q2P/R5R1 b Q - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test760")
	public void test760() {
		checkMate("Position760", "8/6p1/2p3Pp/4q2P/P1Q5/5p2/2P2k2/2K5 b Q - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test761")
	public void test761() {
		checkMate("Position761", "r1b2b1r/4k3/p1p1pN2/4p2p/8/8/q1PQB1PP/1R2K2R w K - 0 1", 8); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test762")
	public void test762() {
		checkMate("Position762", "r4rk1/1Np1b1pp/1n3p2/1q4N1/1p1nB3/P5P1/bRQPPP1P/2B2RK1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test763")
	public void test763() {
		checkMate("Position763", "6k1/1p3pp1/p6p/P3P3/1P3P1K/3p1qP1/3B4/5bQ1 b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test764")
	public void test764() {
		checkMate("Position764", "3R1rk1/p3n2p/4p2P/1p6/2p2Bq1/P1Q1p3/KPP5/8 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test765")
	public void test765() {
		checkMate("Position765", "r1bqk2r/ppppn2p/3bNpnQ/8/2B1Pp2/P7/2PP2PP/RN2K2R w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test766")
	public void test766() {
		checkMate("Position766", "rn3rk1/p5pp/2p5/3Ppb2/2q5/1Q6/PPPB2PP/R3K1NR b Q - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test767")
	public void test767() {
		checkMate("Position767", "r1b1k1nr/p2p1ppp/n2B4/1p1NPN1P/6P1/3P1Q2/P1P1K3/q5b1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test768")
	public void test768() {
		checkMate("Position768", "r1bq3k/ppp2r1p/2np1p1Q/2bNpp2/2B1P2N/3P4/PPP2PPP/R3K2R w QK - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test769")
	public void test769() {
		checkMate("Position769", "2r1k3/ppp2r1p/3pqB2/7Q/4P3/1P5P/P1P4K/6R1 w QK - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test770")
	public void test770() {
		checkMate("Position770", "2k1r3/pp1rPpQp/6p1/2B5/1n4bP/2P3P1/PqP1NP2/2R1KB1R b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test771")
	public void test771() {
		checkMate("Position771", "3kr3/p1r1bR2/4P2p/1Qp5/3p3p/8/PP4PP/6K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test772")
	public void test772() {
		checkMate("Position772", "8/2p5/2P5/p7/k1K1p3/8/2N5/5B2 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test773")
	public void test773() {
		checkMate("Position773", "6bk/8/3P3K/1n4P1/8/8/8/8 w - - 0 1", 9); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test774")
	public void test774() {
		checkMate("Position774", "8/2N4n/4R3/3p1K1k/7p/5p1R/6P1/4n1q1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test775")
	public void test775() {
		checkMate("Position775", "8/8/8/pp5R/k6p/2K5/1P4p1/8 w - - 0 1", 6); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test776")
	public void test776() {
		checkMate("Position776", "8/3P4/8/8/8/1KN1k1B1/4P3/8 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test777")
	public void test777() {
		checkMate("Position777", "7R/3N4/8/8/3k4/1K3P2/5P2/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test778")
	public void test778() {
		checkMate("Position778", "6rk/1p3Rpp/8/p4P1K/P1B3Q1/7P/5q2/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test779")
	public void test779() {
		checkMate("Position779", "5r2/4Q2p/p2p1Pp1/5k2/2pp3P/2P2b2/P1q5/4K1R1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test780")
	public void test780() {
		checkMate("Position780", "2r2B1k/4p2p/2p5/1q1n4/pp4QN/4p3/P1P3PP/b4R1K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test781")
	public void test781() {
		checkMate("Position781", "4r1k1/rp2Bppp/p1b5/1q2Q3/R7/1P5P/P4PP1/3R2K1 w - - 0 1", 10); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test782")
	public void test782() {
		checkMate("Position782", "r1b2r2/3pNpkp/3pn1p1/2pN3P/2PnP3/q3QP2/4BKP1/1R5R w - - 0 1", 11); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test783")
	public void test783() {
		checkMate("Position783", "1r4r1/5Q2/3q4/3pk3/4p1p1/6P1/PP4BP/4RR1K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test784")
	public void test784() {
		checkMate("Position784", "3k1r2/2pb4/2p3P1/2Np1p2/1P6/4nN1R/2P1q3/Q5K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test785")
	public void test785() {
		checkMate("Position785", "2k5/p1p1q3/2P3p1/3QP3/7p/7P/1PK3P1/8 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test786")
	public void test786() {
		checkMate("Position786", "4r2k/pp2q2b/2p2p1Q/4rP2/P7/1B5P/1P2R1R1/7K w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test787")
	public void test787() {
		checkMate("Position787", "q5k1/1b2R1pp/1p3n2/4BQ2/8/7P/5PPK/4r3 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test788")
	public void test788() {
		checkMate("Position788", "r1bq3Q/pppnk1p1/2n1p1P1/3pPr2/1b1P4/1NP5/PP3PP1/R1B1K3 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test789")
	public void test789() {
		checkMate("Position789", "5nk1/2N2p2/2b2Qp1/p3PpNp/2qP3P/6P1/5P1K/8 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test790")
	public void test790() {
		checkMate("Position790", "2r1rk2/1p2qp1R/4p1p1/1b1pP1N1/p2P4/nBP1Q3/P4PPP/R5K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test791")
	public void test791() {
		checkMate("Position791", "r4rk1/p4pp1/7P/2pp4/3Bn3/8/qPP1QP1P/2KR2R1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test792")
	public void test792() {
		checkMate("Position792", "5r1k/1p1b1p1p/p2ppb2/5P1B/1q6/1Pr3R1/2PQ2PP/5R1K w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test793")
	public void test793() {
		checkMate("Position793", "3rkb2/pp3R1R/8/2p1P3/3pKB2/6P1/PrP2P2/8 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test794")
	public void test794() {
		checkMate("Position794", "4r2k/4R1pp/7N/p6n/qp6/6QP/5PPK/8 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test795")
	public void test795() {
		checkMate("Position795", "r1b1r1k1/pp3pp1/1np5/4q3/2B4N/4P3/PP3PP1/2R2K1R w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test796")
	public void test796() {
		checkMate("Position796", "3r2k1/5p2/2b2Bp1/7p/4p3/5PP1/P3Bq1P/Q3R2K b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test797")
	public void test797() {
		checkMate("Position797", "1qr2bk1/pb3pp1/1pn3np/3N2NQ/8/P7/BP3PPP/2B1R1K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test798")
	public void test798() {
		checkMate("Position798", "4rk2/pb1Q1p2/6p1/3p2p1/2pP4/2q1PPK1/Pr4P1/1B2R2R w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test799")
	public void test799() {
		checkMate("Position799", "4k3/2q2p2/4p3/3bP1Q1/p6R/r6P/6PK/5B2 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test800")
	public void test800() {
		checkMate("Position800", "r2qrb2/p1pn1Qp1/1p4Nk/4PR2/3n4/7N/P5PP/R6K w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test801")
	public void test801() {
		checkMate("Position801", "r2r4/p1p2p1p/n5k1/1p5N/2p2R2/5N2/P1K3PP/5R2 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test802")
	public void test802() {
		checkMate("Position802", "r6k/pppb1B2/6Q1/8/3P4/2P1q3/PKP3PP/7R w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test803")
	public void test803() {
		checkMate("Position803", "r2q3r/ppp5/2n4p/4Pbk1/2BP1Npb/P2QB3/1PP3P1/R5K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test804")
	public void test804() {
		checkMate("Position804", "5rbk/2pq3p/5PQR/p7/3p3R/1P4N1/P5PP/6K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test805")
	public void test805() {
		checkMate("Position805", "3q1rk1/4bp1p/1n2P2Q/1p1p1p2/6r1/Pp2R2N/1B1P2PP/7K w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test806")
	public void test806() {
		checkMate("Position806", "r6k/pppb1B2/6Q1/8/3P4/2P1q3/PKP3PP/7R w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test807")
	public void test807() {
		checkMate("Position807", "2rq1r1k/1b2bp1p/p1nppp1Q/1p3P2/4P1PP/2N2N2/PPP5/1K1R1B1R w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test808")
	public void test808() {
		checkMate("Position808", "qr6/1b1p1krQ/p2Pp1p1/4PP2/1p1B1n2/3B4/PP3K1P/2R2R2 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test809")
	public void test809() {
		checkMate("Position809", "4R3/p2r1q1k/5B1P/6P1/2p4K/3b4/4Q3/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test810")
	public void test810() {
		checkMate("Position810", "1rb3k1/ppN2R1p/2n1P1p1/6p1/6B1/8/PPP3PP/6K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test811")
	public void test811() {
		checkMate("Position811", "rk3q1r/pbp4p/1p3P2/2p1N3/3p2Q1/3P4/PPP3PP/R3R1K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test812")
	public void test812() {
		checkMate("Position812", "r3rk2/5pn1/pb1nq1pR/1p2p1P1/2p1P3/2P2QN1/PPBB1P2/2K4R w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test813")
	public void test813() {
		checkMate("Position813", "5b2/1p3rpk/p1b3Rp/4B1RQ/3P1p1P/7q/5P2/6K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test814")
	public void test814() {
		checkMate("Position814", "5rk1/pR4pp/4p2r/2p1n2q/2P1p3/P1Q1P1P1/1P3P1P/R1B2NK1 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test815")
	public void test815() {
		checkMate("Position815", "Q7/1R5p/2kqr2n/7p/5Pb1/8/P1P2BP1/6K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test816")
	public void test816() {
		checkMate("Position816", "2k4r/ppp2p2/2b2B2/7p/6pP/2P1q1bP/PP3N2/R4QK1 b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test817")
	public void test817() {
		checkMate("Position817", "8/5kB1/3p4/1p3q1p/7P/KPPn2Q1/P7/8 b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test818")
	public void test818() {
		checkMate("Position818", "5b2/1p3rpk/p1b3Rp/4B1RQ/3P1p1P/7q/5P2/6K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test819")
	public void test819() {
		checkMate("Position819", "r3r3/3R1Qp1/pqb1p2k/1p4N1/8/4P3/Pb3PPP/2R3K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test820")
	public void test820() {
		checkMate("Position820", "3rkq1r/1pQ2p1p/p3bPp1/3pR3/8/8/PPP2PP1/1K1R4 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test
	@DisplayName("test821")
	public void test821() {
		checkMate("Position821", "4b3/pkb5/1pp1Bp2/4pPp1/PP2P2r/2PQBq2/8/2KR4 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test822")
	public void test822() {
		checkMate("Position822", "1b6/2p5/8/2K2B2/k7/8/1P6/8 w - - 0 1", 8); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test823")
	public void test823() {
		checkMate("Position823", "5r2/p1p2pk1/6p1/1PN1p3/4b1q1/P3Q1P1/7K/2RR4 b - - 0 1", 5); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test824")
	public void test824() {
		checkMate("Position824", "1R4nr/p1k1ppb1/2p4p/4Pp2/3N1P1B/8/q1P3PP/3Q2K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test825")
	public void test825() {
		checkMate("Position825", "4kr2/3rn2p/1P4p1/2p5/Q1B2P2/8/P2q2PP/4R1K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test826")
	public void test826() {
		checkMate("Position826", "3q1rk1/ppr1pp1p/n5pQ/2pP4/4PP1N/PP1n2PB/7P/1R3RK1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test827")
	public void test827() {
		checkMate("Position827", "5rk1/nq1n1p1p/4p1p1/1p2Q3/3N4/P1BP4/5PPP/5RK1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test828")
	public void test828() {
		checkMate("Position828", "4r2k/2pb1R2/2p4P/3pr1N1/1p6/7P/P1P5/2K4R w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test829")
	public void test829() {
		checkMate("Position829", "3rk2b/5R1P/6B1/8/1P3pN1/7P/P2pbP2/6K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test830")
	public void test830() {
		checkMate("Position830", "3r1r1k/1p3p1p/p2p4/4n1NN/6bQ/1BPq4/P3p1PP/1R5K w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test831")
	public void test831() {
		checkMate("Position831", "8/r7/3pNb2/3R3p/1p2p3/pPk5/P1P3PP/1K6 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test832")
	public void test832() {
		checkMate("Position832", "r1b2n2/2q3rk/p3p2n/1p3p1P/4N3/PN1B1P2/1PPQ4/2K3R1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test8332")
	public void test833() {
		checkMate("Position833", "1Q6/8/3p1pk1/2pP4/1p3K2/5R2/5qP1/4r3 b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test834")
	public void test834() {
		checkMate("Position834", "r2qr1k1/1p3pP1/p2p1np1/2pPp1B1/2PnP1b1/2N2p2/PP1Q4/2KR1BNR w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test8335")
	public void test835() {
		checkMate("Position835", "4r1rk/pQ2P2p/P7/2pqb3/3p1p2/8/3B2PP/4RRK1 b - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test836")
	public void test836() {
		checkMate("Position836", "3n2b1/1pr1r2k/p1p1pQpp/P1P5/2BP1PP1/5K2/1P5R/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test837")
	public void test837() {
		checkMate("Position837", "8/6R1/1p1p4/1P1Np2k/2P1N2p/3P1p1q/1B2PP2/6K1 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test838")
	public void test838() {
		checkMate("Position838", "2q1r3/4pR2/3rQ1pk/p1pnN2p/Pn5B/8/1P3PP/3R3K w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test839")
	public void test839() {
		checkMate("Position839", "1r2r1k1/5p2/5Rp1/4Q2p/P2B2qP/1NP5/1KP5/8 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test840")
	public void test840() {
		checkMate("Position840", "5rk1/2R4p/3QP1p1/3p4/4p3/1P5P/q7/2R3K1 w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test841")
	public void test841() {
		checkMate("Position841", "8/8/2N5/8/8/p7/2K5/k7 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test842")
	public void test842() {
		checkMate("Position842", "1r1qrbk1/3b3p/p2p1pp1/3NnP2/3N4/1Q4BP/PP4P1/1R2R2K w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test843")
	public void test843() {
		checkMate("Position843", "r4b2/p5pk/6rn/2n2N1Q/5PP1/2Bq4/PP6/K1R4R b - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test844")
	public void test844() {
		checkMate("Position844", "r1br2k1/4p1b1/pq2pn2/1p4N1/7Q/3B4/PPP3PP/R4R1K w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test845")
	public void test845() {
		checkMate("Position845", "4qk2/6p1/p7/1p1Qp3/r1P2b2/1K5P/1P6/4RR2 w - - 0 1", 4); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test846")
	public void test846() {
		checkMate("Position846", "3rk1r1/1p5p/p1b1pP2/8/5K2/2B4q/PP2B2P/3RQ2R b - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test847")
	public void test847() {
		checkMate("Position847", "r1nk3r/2b2ppp/p3b3/3NN3/Q2P3q/B2B4/P4PPP/4R1K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test848")
	public void test848() {
		checkMate("Position848", "rn4nr/pppq2bk/7p/5b1P/4NBQ1/3B4/PPP3P1/R3K2R w - - 0 1", 3); //$NON-NLS-1$ //$NON-NLS-2$
	}
	@Test
	@DisplayName("test849")
	public void test849() {
		checkMate("Position849", "2rr1k2/Q3Rpp1/2n1b2p/1BB1q3/3P4/8/P4PPP/6K1 w - - 0 1", 2); //$NON-NLS-1$ //$NON-NLS-2$
	}
}