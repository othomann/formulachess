package org.formulachess.pgn;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.formulachess.pgn.ast.ASTNode;
import org.formulachess.pgn.ast.Castle;
import org.formulachess.pgn.ast.GameTermination;
import org.formulachess.pgn.ast.Move;
import org.formulachess.pgn.ast.MoveText;
import org.formulachess.pgn.ast.PGNDatabase;
import org.formulachess.pgn.ast.PGNGame;
import org.formulachess.pgn.ast.PawnMove;
import org.formulachess.pgn.ast.PieceMove;
import org.formulachess.pgn.ast.TagPair;
import org.formulachess.pgn.ast.TagSection;
import org.formulachess.pgn.ast.Variation;
import org.formulachess.util.Util;

/**
 * @author oliviert
 *
 *         To change this generated comment edit the template variable
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class Parser implements ParserBasicInformation, TerminalSymbols {
	private static final Logger MyLogger = Logger.getLogger(Parser.class.getCanonicalName());
	private static final boolean DEBUG = false;
	// internal data for the automat
	protected static final int StackIncrement = 255;
	protected int stateStackTop;
	protected int[] stack = new int[StackIncrement];

	// AST management
	private ASTNode[] nodeStack = new ASTNode[StackIncrement];
	private int nodeStackPointer = 0;
	private char[][] nodeInformation = new char[StackIncrement][];
	private int nodeInformationPointer = 0;
	private int variationCounter = 0;
	private int[] variationMovesNumber = new int[StackIncrement];
	private int currentMoveIndication;
	private PGNDatabase pgnDatabase;

	// scanner token
	private Scanner scanner;

	private int currentToken;
	private int firstToken; // handle for multiple parsing goals

	protected boolean hasReportedError;
	protected int lastErrorEndPosition;

	// internal tables for the automaton.
	private static final byte[] rhs = { 0, 2, 2, 0, 3, 3, 0, 5, 1, 2, 2, 2, 0, 3, 5, 3, 2, 3, 3, 3, 4, 4, 3, 1, 2, 4, 3,
			4, 5, 5, 6, 3, 4, 4, 4, 5, 5, 6, 1, 1, 2, 1, 1, 1, 1, 1, 1, 0, 3, 5, 1, 0, 1, 1, 1, 1 };

	private static final short[] check_table = { -8, -16, -17, 1, 0, -3, 4, -5, 7, 8, 9, 10, 11, 12, 0, -6, 14, 2, 0,
			19, 20, -4, 21, 22, -10, 1, 0, 0, 0, 5, -29, 1, 0, 0, 34, 35, 36, 37, 31, 15, 16, 17, 0, -22, 14, 24, 0, 0,
			-33, -24, 1, 0, 0, 0, 5, 7, 8, 9, 10, 11, 12, 0, -11, 0, 0, 16, 17, 0, 0, 21, 22, -55, -25, 0, 0, 0, 5, 15,
			0, 7, 8, 9, 10, 11, 12, 0, -7, 0, 2, 0, -9, -26, 2, 0, 23, -1, 0, 0, 7, 8, 9, 10, 11, 12, 0, -48, -19, 1,
			-28, 1, 39, 5, 7, 8, 9, 10, 11, 12, 0, -49, -35, -20, 16, 17, 0, -13, 7, 8, 9, 10, 11, 12, 0, 38, 0, 0, 15,
			-30, 1, 19, 20, 4, -12, 0, 2, 0, 0, 0, 0, -18, -2, 14, 32, 4, -14, 30, 2, 0, -15, -36, 2, 0, -21, 14, 2, 0,
			-23, -32, 2, 0, 0, 0, 5, -34, 24, 2, 0, -37, 0, 2, 0, -51, -27, 2, 0, -31, 1, -38, 6, -39, 23, -45, 33, 6,
			-40, 6, -41, 1, 4, -42, -43, 1, -44, 4, -46, 14, 4, -47, 4, -50, -52, 0, 5, -53, 4, 6, -56, 4, -54, 1, -57,
			-58, 6, -59, 0, 4, 6, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	private static final char[] lhs = { 0, 40, 41, 41, 42, 43, 43, 45, 46, 44, 25, 25, 25, 26, 26, 26, 29, 29, 29, 49,
			49, 49, 50, 48, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 13, 13, 13, 13, 13, 13, 13,
			13, 28, 27, 3, 3, 47, 47, 47, 47,

			281, 47, 281, 193, 281, 3, 86, 12, 322, 323, 324, 325, 326, 327, 238, 51, 164, 331, 5, 118, 198, 6, 147,
			128, 281, 205, 10, 11, 80, 58, 281, 255, 56, 281, 333, 334, 335, 336, 146, 177, 319, 320, 57, 12, 252, 1,
			77, 9, 47, 50, 205, 142, 71, 214, 58, 322, 323, 324, 325, 326, 327, 265, 281, 2, 63, 319, 320, 57, 176, 175,
			161, 27, 47, 210, 162, 281, 310, 218, 23, 322, 323, 324, 325, 326, 327, 243, 51, 281, 331, 4, 51, 47, 331,
			181, 279, 281, 281, 233, 322, 323, 324, 325, 326, 327, 245, 47, 281, 205, 281, 250, 309, 58, 322, 323, 324,
			325, 326, 327, 272, 47, 281, 281, 319, 320, 104, 281, 322, 323, 324, 325, 326, 327, 276, 61, 281, 206, 229,
			281, 258, 118, 198, 247, 51, 281, 331, 99, 10, 11, 80, 281, 281, 256, 237, 223, 51, 222, 331, 15, 51, 281,
			105, 13, 51, 241, 331, 48, 51, 24, 331, 215, 281, 222, 307, 51, 280, 331, 22, 51, 281, 331, 14, 51, 16, 331,
			49, 281, 260, 18, 321, 17, 263, 31, 288, 321, 281, 321, 281, 266, 315, 281, 281, 269, 281, 314, 281, 274,
			313, 281, 127, 19, 281, 281, 306, 281, 317, 321, 21, 316, 281, 277, 20, 281, 321, 281, 281, 318, 321, 281,
			311 };

	private static final char[] action = lhs;

	public final int getFirstToken() {
		return this.firstToken;
	}

	protected static int tAction(int state, int sym) {
		return action[check(state + sym) == sym ? state + sym : state];
	}

	protected static short check(int i) {
		return check_table[i - (NUM_RULES + 1)];
	}

	protected static int ntAction(int state, int sym) {
		return action[state + sym];
	}

	/*
	 * main loop of the automat When a rule is reduced, the method consumeRule(int)
	 * is called with the number of the consumed rule. When a terminal is consumed,
	 * the method consumeToken(int) is called in order to remember (when needed) the
	 * consumed token
	 */
	// (int)asr[asi(act)]
	// name[symbol_index[currentKind]]
	protected void parse() {
		int act = START_STATE;
		try {
			this.hasReportedError = false;
			this.stateStackTop = -1;
			this.currentToken = getFirstToken();
			ProcessTerminals: for (;;) {
				this.stateStackTop++;
				if (this.stateStackTop == this.stack.length) {
					// resize
					int oldStackLength = this.stack.length;
					System.arraycopy(this.stack, 0, this.stack = new int[oldStackLength + StackIncrement], 0,
							oldStackLength);
				}
				this.stack[this.stateStackTop] = act;

				act = tAction(act, this.currentToken);

				if (act == ERROR_ACTION) {
					break ProcessTerminals;
				}
				if (act <= NUM_RULES) {
					this.stateStackTop--;
				} else if (act > ERROR_ACTION) { /* shift-reduce */
					try {
						consumeToken(this.currentToken);
						this.currentToken = this.scanner.getNextToken();
					} catch (InvalidInputException e) {
						MyLogger.log(Level.SEVERE, "Contains error", e);
						this.hasReportedError = true;
						return;
					}
					act -= ERROR_ACTION;
				} else if (act < ACCEPT_ACTION) { /* shift */
					try {
						consumeToken(this.currentToken);
						this.currentToken = this.scanner.getNextToken();
					} catch (InvalidInputException e) {
						MyLogger.log(Level.SEVERE, "Contains errors", e);
						this.hasReportedError = true;
						return;
					}
					continue ProcessTerminals;
				} else
					break ProcessTerminals;

				do { /* reduce */
					consumeRule(act);
					this.stateStackTop -= (rhs[act] - 1);
					act = ntAction(this.stack[this.stateStackTop], lhs[act]);
				} while (act <= NUM_RULES);
			}
		} finally {
			endParse(act);
		}
	}

	protected void reportSyntaxError(int actValue, int currentKindValue, int stateStackTopValue) {
		// nothing to do
	}

	/*
	 * Syntax error was detected. Will attempt to perform some recovery action in
	 * order to resume to the regular parse loop.
	 */
	protected boolean resumeOnSyntaxError() {
		return false;
	}

	protected void consumeToken(int type) throws InvalidInputException {
		// attach comments to node
		if (!this.scanner.getComments().isEmpty()) {
			if (this.nodeInformationPointer != 0) {
				throw new InvalidInputException("Comment should not be there"); //$NON-NLS-1$
			}
			this.nodeStack[this.nodeStackPointer - 1].setComments(this.scanner.getComments());
			this.scanner.clearComments();
		}
		switch (type) {
			case TokenNameStart_Tag_Section:
			case TokenNameStringLiteral:
			case TokenNamePieceIdentification:
			case TokenNameBAD_MOVE:
			case TokenNameEXCELLENT_MOVE:
			case TokenNameVERY_BAD_MOVE:
			case TokenNameSUSPICIOUS_MOVE:
			case TokenNameINTERESTING_MOVE:
			case TokenNameStart_nag:
			case TokenNameGOOD_MOVE:
			case TokenNameFileName:
			case TokenNameRankName:
				pushOnNodeInformationStack(this.scanner.getCurrentTokenSource());
				break;
			case TokenNameIntegerLiteral:
				try {
					this.currentMoveIndication = Integer.parseInt(new String(this.scanner.getCurrentTokenSource()));
				} catch (NumberFormatException e) {
					this.currentMoveIndication = -1;
				}
				break;
			case TokenNameSTART_VARIATION:
				this.variationCounter++;
				break;
			case TokenNameEND_VARIATION:
				this.variationCounter--;
				break;
			default:
		}
	}

	// This method is part of an automatic generation : do NOT edit-modify
	protected void consumeRule(int act) {
		switch (act) {
			case 1:
				if (DEBUG)
					MyLogger.log(Level.INFO, "Goal ::= GREATER_THAN PGN-database"); //$NON-NLS-1$
				consumeGoal();
				break;
			case 2:
				if (DEBUG)
					MyLogger.log(Level.INFO, "PGN-database ::= PGN-database PGN-game"); //$NON-NLS-1$
				consumePGNDatabase();
				break;
			case 3:
				if (DEBUG)
					MyLogger.log(Level.INFO, "PGN-database ::="); //$NON-NLS-1$
				consumeEmptyPGNDatabase();
				break;
			case 4:
				if (DEBUG)
					MyLogger.log(Level.INFO, "PGN-game ::= tag-section movetext-section space"); //$NON-NLS-1$
				consumePGNGame();
				break;
			case 5:
				if (DEBUG)
					MyLogger.log(Level.INFO, "tag-section ::= tag-section tag-pair space"); //$NON-NLS-1$
				consumeTagSection();
				break;
			case 6:
				if (DEBUG)
					MyLogger.log(Level.INFO, "tag-section ::="); //$NON-NLS-1$
				consumeEmptyTagSection();
				break;
			case 7:
				if (DEBUG)
					MyLogger.log(Level.INFO, "tag-pair ::= Start_Tag_Section space tag-value space..."); //$NON-NLS-1$
				consumeTagPair();
				break;
			case 9:
				if (DEBUG)
					MyLogger.log(Level.INFO, "movetext-section ::= element-sequence game-termination"); //$NON-NLS-1$
				consumeMoveTextSection();
				break;
			case 10:
				if (DEBUG)
					MyLogger.log(Level.INFO, "element-sequence ::= element-sequence element"); //$NON-NLS-1$
				consumeElementSequence();
				break;
			case 11:
				if (DEBUG)
					MyLogger.log(Level.INFO, "element-sequence ::= element-sequence recursive-variation"); //$NON-NLS-1$
				consumeElementSequenceWithRecursiveVariation();
				break;
			case 12:
				if (DEBUG)
					MyLogger.log(Level.INFO, "element-sequence ::="); //$NON-NLS-1$
				consumeEmptyElementSequence();
				break;
			case 13:
				if (DEBUG)
					MyLogger.log(Level.INFO, "element ::= move-number-indication WhiteMove space"); //$NON-NLS-1$
				consumeElementSingleMove();
				break;
			case 14:
				if (DEBUG)
					MyLogger.log(Level.INFO, "element ::= move-number-indication WhiteMove WHITESPACE..."); //$NON-NLS-1$
				consumeElementTwoMoves();
				break;
			case 15:
				if (DEBUG)
					MyLogger.log(Level.INFO, "element ::= move-number-indication BlackMove space"); //$NON-NLS-1$
				consumeElementBlackMove();
				break;
			case 16:
				if (DEBUG)
					MyLogger.log(Level.INFO, "WhiteMove ::= InnerSANMove numeric-annotation-glyph"); //$NON-NLS-1$
				consumeWhiteMove();
				break;
			case 17:
				if (DEBUG)
					MyLogger.log(Level.INFO, "WhiteMove ::= InnerSANMove CHECK numeric-annotation-glyph"); //$NON-NLS-1$
				consumeWhiteMoveWithCheck();
				break;
			case 18:
				if (DEBUG)
					MyLogger.log(Level.INFO, "WhiteMove ::= InnerSANMove CHECKMATE..."); //$NON-NLS-1$
				consumeWhiteMoveWithCheckMate();
				break;
			case 19:
				if (DEBUG)
					MyLogger.log(Level.INFO, "BlackMove ::= BlackDots InnerSANMove..."); //$NON-NLS-1$
				consumeBlackMove();
				break;
			case 20:
				if (DEBUG)
					MyLogger.log(Level.INFO, "BlackMove ::= BlackDots InnerSANMove CHECK..."); //$NON-NLS-1$
				consumeBlackMoveWithCheck();
				break;
			case 21:
				if (DEBUG)
					MyLogger.log(Level.INFO, "BlackMove ::= BlackDots InnerSANMove CHECKMATE..."); //$NON-NLS-1$
				consumeBlackMoveWithCheckMate();
				break;
			case 23:
				if (DEBUG)
					MyLogger.log(Level.INFO, "BlackMoveFollowingWhiteMove ::= WhiteMove"); //$NON-NLS-1$
				consumeBlackMoveFollowingWhiteMove();
				break;
			case 24:
				if (DEBUG)
					MyLogger.log(Level.INFO, "InnerSANMove ::= FileName RankName"); //$NON-NLS-1$
				consumePawnMove();
				break;
			case 25:
				if (DEBUG)
					MyLogger.log(Level.INFO, "InnerSANMove ::= FileName RankName PROMOTE..."); //$NON-NLS-1$
				consumePawnMoveWithPromotion();
				break;
			case 26:
				if (DEBUG)
					MyLogger.log(Level.INFO, "InnerSANMove ::= FileName RankName PieceIdentification"); //$NON-NLS-1$
				consumePawnMoveWithPromotion();
				break;
			case 27:
				if (DEBUG)
					MyLogger.log(Level.INFO, "InnerSANMove ::= FileName CAPTURE FileName RankName"); //$NON-NLS-1$
				consumePawnMoveWithCapture();
				break;
			case 28:
				if (DEBUG)
					MyLogger.log(Level.INFO, "InnerSANMove ::= FileName CAPTURE FileName RankName..."); //$NON-NLS-1$
				consumePawnMoveWithCapture();
				break;
			case 29:
				if (DEBUG)
					MyLogger.log(Level.INFO, "InnerSANMove ::= FileName CAPTURE FileName RankName..."); //$NON-NLS-1$
				consumePawnMoveWithCaptureAndPromotion();
				break;
			case 30:
				if (DEBUG)
					MyLogger.log(Level.INFO, "InnerSANMove ::= FileName CAPTURE FileName RankName PROMOTE"); //$NON-NLS-1$
				consumePawnMoveWithCaptureAndPromotion();
				break;
			case 31:
				if (DEBUG)
					MyLogger.log(Level.INFO, "InnerSANMove ::= PieceIdentification FileName RankName"); //$NON-NLS-1$
				consumePieceMove();
				break;
			case 32:
				if (DEBUG)
					MyLogger.log(Level.INFO, "InnerSANMove ::= PieceIdentification FileName FileName..."); //$NON-NLS-1$
				consumePieceMoveWithFileNameAmbiguity();
				break;
			case 33:
				if (DEBUG)
					MyLogger.log(Level.INFO, "InnerSANMove ::= PieceIdentification RankName FileName..."); //$NON-NLS-1$
				consumePieceMoveWithRankNameAmbiguity();
				break;
			case 34:
				if (DEBUG)
					MyLogger.log(Level.INFO, "InnerSANMove ::= PieceIdentification CAPTURE FileName..."); //$NON-NLS-1$
				consumePieceMoveWithCapture();
				break;
			case 35:
				if (DEBUG)
					MyLogger.log(Level.INFO, "InnerSANMove ::= PieceIdentification FileName CAPTURE..."); //$NON-NLS-1$
				consumePieceMoveWithCaptureAndFileNameAmbiguity();
				break;
			case 36:
				if (DEBUG)
					MyLogger.log(Level.INFO, "InnerSANMove ::= PieceIdentification RankName CAPTURE..."); //$NON-NLS-1$
				consumePieceMoveWithCaptureAndRankNameAmbiguity();
				break;
			case 37:
				if (DEBUG)
					MyLogger.log(Level.INFO, "InnerSANMove ::= PieceIdentification FileName RankName..."); //$NON-NLS-1$
				consumePieceMoveWithCaptureAndDoubleAmbiguity();
				break;
			case 38:
				if (DEBUG)
					MyLogger.log(Level.INFO, "InnerSANMove ::= CASTLE_KING_SIDE"); //$NON-NLS-1$
				consumeCastleKingSide();
				break;
			case 39:
				if (DEBUG)
					MyLogger.log(Level.INFO, "InnerSANMove ::= CASTLE_QUEEN_SIDE"); //$NON-NLS-1$
				consumeCastleQueenSide();
				break;
			case 40:
				if (DEBUG)
					MyLogger.log(Level.INFO, "numeric-annotation-glyph ::= numeric-annotation-glyph..."); //$NON-NLS-1$
				consumeNAG();
				break;
			case 41:
				if (DEBUG)
					MyLogger.log(Level.INFO, "numeric-annotation-glyph ::= EXCELLENT_MOVE"); //$NON-NLS-1$
				consumeExcellentMoveNAG();
				break;
			case 42:
				if (DEBUG)
					MyLogger.log(Level.INFO, "numeric-annotation-glyph ::= VERY_BAD_MOVE"); //$NON-NLS-1$
				consumeVeryBadMoveNAG();
				break;
			case 43:
				if (DEBUG)
					MyLogger.log(Level.INFO, "numeric-annotation-glyph ::= BAD_MOVE"); //$NON-NLS-1$
				consumeBadMoveNAG();
				break;
			case 44:
				if (DEBUG)
					MyLogger.log(Level.INFO, "numeric-annotation-glyph ::= GOOD_MOVE"); //$NON-NLS-1$
				consumeGoodMoveMoveNAG();
				break;
			case 45:
				if (DEBUG)
					MyLogger.log(Level.INFO, "numeric-annotation-glyph ::= INTERESTING_MOVE"); //$NON-NLS-1$
				consumeInterestingMoveNAG();
				break;
			case 46:
				if (DEBUG)
					MyLogger.log(Level.INFO, "numeric-annotation-glyph ::= SUSPICIOUS_MOVE"); //$NON-NLS-1$
				consumeSuspiciousMoveNAG();
				break;
			case 47:
				if (DEBUG)
					MyLogger.log(Level.INFO, "numeric-annotation-glyph ::="); //$NON-NLS-1$
				consumeEmptyNAG();
				break;
			case 48:
				if (DEBUG)
					MyLogger.log(Level.INFO, "move-number-indication ::= IntegerLiteral DOT space"); //$NON-NLS-1$
				consumeMoveIndication();
				break;
			case 49:
				if (DEBUG)
					MyLogger.log(Level.INFO, "recursive-variation ::= START_VARIATION space..."); //$NON-NLS-1$
				consumeRecursiveVariation();
				break;
			case 52:
				if (DEBUG)
					MyLogger.log(Level.INFO, "game-termination ::= WHITE_VICTORY"); //$NON-NLS-1$
				consumeWhiteVictory();
				break;
			case 53:
				if (DEBUG)
					MyLogger.log(Level.INFO, "game-termination ::= BLACK_VICTORY"); //$NON-NLS-1$
				consumeBlackVictory();
				break;
			case 54:
				if (DEBUG)
					MyLogger.log(Level.INFO, "game-termination ::= DRAW"); //$NON-NLS-1$
				consumeDraw();
				break;
			case 55:
				if (DEBUG)
					MyLogger.log(Level.INFO, "game-termination ::= UNKNOWN"); //$NON-NLS-1$
				consumeUnknownResult();
				break;
			default:
		}
	}

	protected void endParse(int act) {
		if (act == ERROR_ACTION || this.hasReportedError) {
			if (DEBUG) {
				MyLogger.log(Level.INFO, "ERROR"); //$NON-NLS-1$
				MyLogger.log(Level.INFO, String.valueOf(this.scanner));
			}
			this.pgnDatabase = null;
		}
	}

	public void initialize() {
		// nothing to do
	}

	private void goForPGNDatabase() {
		this.firstToken = TokenNameGREATER_THAN;
	}

	public PGNDatabase parse(char[] source) {
		/* automaton initialization */
		initialize();
		goForPGNDatabase();

		int i = 0;
		for (int max = source.length; i < max; i++) {
			if (Character.isWhitespace(source[i])) {
				continue;
			}
			break;
		}

		/* scanner initialization */
		this.scanner = new Scanner();
		this.scanner.resetTo(i, source.length);
		this.scanner.setSource(source);

		parse();
		return this.pgnDatabase;
	}

	public PGNDatabase parseZipFile(ZipFile zipFile) {
		try {
			/* automaton initialization */
			initialize();
			goForPGNDatabase();

			Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
			ZipEntry entry = enumeration.nextElement();
			char[] source = Util.getZipEntryCharContent(entry, zipFile);
			/* scanner initialization */
			this.scanner = new Scanner();
			this.scanner.resetTo(0, source.length);
			this.scanner.setSource(source);

			parse();
		} catch (java.io.IOException e) {
			MyLogger.log(Level.SEVERE, "IOException", e);
		}
		return this.pgnDatabase;
	}

	private void consumePGNDatabase() {
		((PGNDatabase) this.nodeStack[this.nodeStackPointer - 2])
				.addPGNGame((PGNGame) this.nodeStack[--this.nodeStackPointer]);
	}

	private void consumeEmptyPGNDatabase() {
		pushOnNodeStack(this.pgnDatabase = new PGNDatabase());
	}

	private void consumePGNGame() {
		pushOnNodeStack(new PGNGame((MoveText) this.nodeStack[--this.nodeStackPointer],
				(TagSection) this.nodeStack[--this.nodeStackPointer]));
	}

	private void consumeTagSection() {
		((TagSection) this.nodeStack[this.nodeStackPointer - 2])
				.addTagPair((TagPair) this.nodeStack[--this.nodeStackPointer]);
	}

	private void consumeEmptyTagSection() {
		pushOnNodeStack(new TagSection());
	}

	private void consumeEmptyNAG() {
		// nothing to do
	}

	/**
	 * tag-pair ::= Start_Tag_Section tag-value END_TAG_SECTION
	 */
	private void consumeTagPair() {
		pushOnNodeStack(new TagPair(this.nodeInformation[this.nodeInformationPointer - 2],
				this.nodeInformation[this.nodeInformationPointer - 1]));
		this.nodeInformationPointer -= 2;
	}

	private void consumeMoveTextSection() {
		GameTermination gameTermination = (GameTermination) this.nodeStack[--this.nodeStackPointer];
		Move[] moves = null;
		int movesCounter = this.variationMovesNumber[this.variationCounter];
		this.variationMovesNumber[this.variationCounter] = 0;
		if (movesCounter != 0) {
			moves = new Move[movesCounter];
			System.arraycopy(this.nodeStack, this.nodeStackPointer - movesCounter, moves, 0, movesCounter);
			this.nodeStackPointer -= movesCounter;
		}
		MoveText moveText = (MoveText) this.nodeStack[this.nodeStackPointer - 1];
		moveText.setMoves(moves);
		initializeParents(moves);
		moveText.setGameTermination(gameTermination);
	}

	private void initializeParents(Move[] moves) {
		if (moves == null) {
			return;
		}
		for (int i = moves.length - 1; i > 0; i--) {
			moves[i].setParent(moves[i - 1]);
		}
	}

	private void consumeElementSequence() {
		// nothing to do
	}

	private void consumeElementSequenceWithRecursiveVariation() {
		// nothing to do
	}

	/**
	 * Method consumeCastleKingSide.
	 */
	private void consumeCastleKingSide() {
		pushOnNodeStack(new Castle(true));
		this.variationMovesNumber[this.variationCounter]++;
	}

	/**
	 * Method consumeUnknownResult.
	 */
	private void consumeUnknownResult() {
		pushOnNodeStack(GameTermination.UNKNOWN);
	}

	/**
	 * Method consumeDraw.
	 */
	private void consumeDraw() {
		pushOnNodeStack(GameTermination.DRAW);
	}

	/**
	 * Method consumeBlackVictory.
	 */
	private void consumeBlackVictory() {
		pushOnNodeStack(GameTermination.BLACK_VICTORY);
	}

	/**
	 * Method consumeWhiteVictory.
	 */
	private void consumeWhiteVictory() {
		pushOnNodeStack(GameTermination.WHITE_VICTORY);
	}

	/**
	 * Method consumeRecursiveVariation.
	 */
	private void consumeRecursiveVariation() {
		int movesNumber = this.variationMovesNumber[this.variationCounter + 1];
		this.variationMovesNumber[this.variationCounter + 1] = 0;
		Move[] moves = new Move[movesNumber];
		this.nodeStackPointer -= movesNumber;
		System.arraycopy(this.nodeStack, this.nodeStackPointer, moves, 0, movesNumber);
		this.nodeStackPointer--;
		initializeParents(moves);
		((Move) this.nodeStack[this.nodeStackPointer - 1]).addVariation(new Variation(moves));
	}

	/**
	 * Method consumeMoveIndication.
	 */
	private void consumeMoveIndication() {
		// nothing to do
	}

	/**
	 * Method consumeSuspiciousMoveNAG.
	 */
	private void consumeSuspiciousMoveNAG() {
		((Move) this.nodeStack[this.nodeStackPointer - 1]).setNag(this.nodeInformation[--this.nodeInformationPointer]);
	}

	/**
	 * Method consumeInterestingMoveNAG.
	 */
	private void consumeInterestingMoveNAG() {
		((Move) this.nodeStack[this.nodeStackPointer - 1]).setNag(this.nodeInformation[--this.nodeInformationPointer]);
	}

	private void consumeGoal() {
		this.nodeStackPointer--;
	}

	/**
	 * Method consumeGoodMoveMoveNAG.
	 */
	private void consumeGoodMoveMoveNAG() {
		((Move) this.nodeStack[this.nodeStackPointer - 1]).setNag(this.nodeInformation[--this.nodeInformationPointer]);
	}

	/**
	 * Method consumeBadMoveNAG.
	 */
	private void consumeBadMoveNAG() {
		((Move) this.nodeStack[this.nodeStackPointer - 1]).setNag(this.nodeInformation[--this.nodeInformationPointer]);
	}

	/**
	 * Method consumeVeryBadMoveNAG.
	 */
	private void consumeVeryBadMoveNAG() {
		((Move) this.nodeStack[this.nodeStackPointer - 1]).setNag(this.nodeInformation[--this.nodeInformationPointer]);
	}

	/**
	 * Method consumeExcellentMoveNAG.
	 */
	private void consumeExcellentMoveNAG() {
		((Move) this.nodeStack[this.nodeStackPointer - 1]).setNag(this.nodeInformation[--this.nodeInformationPointer]);
	}

	/**
	 * Method consumeNAG.
	 */
	private void consumeNAG() {
		((Move) this.nodeStack[this.nodeStackPointer - 1]).setNag(this.nodeInformation[--this.nodeInformationPointer]);
	}

	/**
	 * Method consumeCastleQueenSide.
	 */
	private void consumeCastleQueenSide() {
		pushOnNodeStack(new Castle(false));
		this.variationMovesNumber[this.variationCounter]++;
	}

	/**
	 * Method consumePieceMoveWithCaptureAndDoubleAmbiguity.
	 */
	private void consumePieceMoveWithCaptureAndDoubleAmbiguity() {
		PieceMove pieceMove = new PieceMove();
		pieceMove.setEndingRank(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setEndingFile(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setStartingRank(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setStartingFile(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setPieceIdentification(this.nodeInformation[--this.nodeInformationPointer][0]);
		this.variationMovesNumber[this.variationCounter]++;
		pushOnNodeStack(pieceMove);
	}

	/**
	 * Method consumePieceMoveWithCaptureAndRankNameAmbiguity.
	 */
	private void consumePieceMoveWithCaptureAndRankNameAmbiguity() {
		PieceMove pieceMove = new PieceMove();
		pieceMove.setEndingRank(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setEndingFile(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setStartingRank(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setPieceIdentification(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setIsCapture(true);
		this.variationMovesNumber[this.variationCounter]++;
		pushOnNodeStack(pieceMove);
	}

	/**
	 * Method consumePieceMoveWithCaptureAndFileNameAmbiguity.
	 */
	private void consumePieceMoveWithCaptureAndFileNameAmbiguity() {
		PieceMove pieceMove = new PieceMove();
		pieceMove.setEndingRank(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setEndingFile(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setStartingFile(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setPieceIdentification(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setIsCapture(true);
		this.variationMovesNumber[this.variationCounter]++;
		pushOnNodeStack(pieceMove);
	}

	/**
	 * Method consumePieceMoveWithCapture.
	 */
	private void consumePieceMoveWithCapture() {
		PieceMove pieceMove = new PieceMove();
		pieceMove.setEndingRank(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setEndingFile(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setPieceIdentification(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setIsCapture(true);
		this.variationMovesNumber[this.variationCounter]++;
		pushOnNodeStack(pieceMove);
	}

	/**
	 * Method consumePieceMoveWithRankNameAmbiguity.
	 */
	private void consumePieceMoveWithRankNameAmbiguity() {
		PieceMove pieceMove = new PieceMove();
		pieceMove.setEndingRank(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setEndingFile(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setStartingRank(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setPieceIdentification(this.nodeInformation[--this.nodeInformationPointer][0]);
		this.variationMovesNumber[this.variationCounter]++;
		pushOnNodeStack(pieceMove);
	}

	/**
	 * Method consumePieceMoveWithFileNameAmbiguity.
	 */
	private void consumePieceMoveWithFileNameAmbiguity() {
		PieceMove pieceMove = new PieceMove();
		pieceMove.setEndingRank(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setEndingFile(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setStartingFile(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setPieceIdentification(this.nodeInformation[--this.nodeInformationPointer][0]);
		this.variationMovesNumber[this.variationCounter]++;
		pushOnNodeStack(pieceMove);
	}

	/**
	 * Method consumePieceMove.
	 */
	private void consumePieceMove() {
		PieceMove pieceMove = new PieceMove();
		pieceMove.setEndingRank(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setEndingFile(this.nodeInformation[--this.nodeInformationPointer][0]);
		pieceMove.setPieceIdentification(this.nodeInformation[--this.nodeInformationPointer][0]);
		this.variationMovesNumber[this.variationCounter]++;
		pushOnNodeStack(pieceMove);
	}

	/**
	 * Method consumePawnMoveWithCaptureAndPromotion.
	 */
	private void consumePawnMoveWithCaptureAndPromotion() {
		PawnMove pawnMove = new PawnMove();
		pawnMove.setPromotedPiece(this.nodeInformation[--this.nodeInformationPointer][0]);
		pawnMove.setEndingRank(this.nodeInformation[--this.nodeInformationPointer][0]);
		pawnMove.setEndingFile(this.nodeInformation[--this.nodeInformationPointer][0]);
		pawnMove.setStartingFile(this.nodeInformation[--this.nodeInformationPointer][0]);
		pawnMove.setIsCapture(true);
		pawnMove.setIsPromotion(true);
		this.variationMovesNumber[this.variationCounter]++;
		pushOnNodeStack(pawnMove);
	}

	/**
	 * Method consumePawnMoveWithCapture.
	 */
	private void consumePawnMoveWithCapture() {
		PawnMove pawnMove = new PawnMove();
		pawnMove.setEndingRank(this.nodeInformation[--this.nodeInformationPointer][0]);
		pawnMove.setEndingFile(this.nodeInformation[--this.nodeInformationPointer][0]);
		pawnMove.setStartingFile(this.nodeInformation[--this.nodeInformationPointer][0]);
		pawnMove.setIsCapture(true);
		this.variationMovesNumber[this.variationCounter]++;
		pushOnNodeStack(pawnMove);
	}

	/**
	 * Method consumePawnMoveWithPromotion.
	 */
	private void consumePawnMoveWithPromotion() {
		PawnMove pawnMove = new PawnMove();
		pawnMove.setPromotedPiece(this.nodeInformation[--this.nodeInformationPointer][0]);
		pawnMove.setEndingRank(this.nodeInformation[--this.nodeInformationPointer][0]);
		pawnMove.setEndingFile(this.nodeInformation[--this.nodeInformationPointer][0]);
		pawnMove.setIsPromotion(true);
		this.variationMovesNumber[this.variationCounter]++;
		pushOnNodeStack(pawnMove);
	}

	/**
	 * Method consumePawnMove.
	 */
	private void consumePawnMove() {
		PawnMove pawnMove = new PawnMove();
		pawnMove.setEndingRank(this.nodeInformation[--this.nodeInformationPointer][0]);
		pawnMove.setEndingFile(this.nodeInformation[--this.nodeInformationPointer][0]);
		this.variationMovesNumber[this.variationCounter]++;
		pushOnNodeStack(pawnMove);
	}

	/**
	 * Method consumeBlackMoveFollowingWhiteMove.
	 */
	private void consumeBlackMoveFollowingWhiteMove() {
		// nothing to do
	}

	/**
	 * Method consumeBlackMoveWithCheckMate.
	 */
	private void consumeBlackMoveWithCheckMate() {
		((Move) this.nodeStack[this.nodeStackPointer - 1]).setIsCheckMate(true);
	}

	/**
	 * Method consumeBlackMoveWithCheck.
	 */
	private void consumeBlackMoveWithCheck() {
		((Move) this.nodeStack[this.nodeStackPointer - 1]).setIsCheck(true);
	}

	/**
	 * Method consumeBlackMove.
	 */
	private void consumeBlackMove() {
		// nothing to do
	}

	/**
	 * Method consumeWhiteMoveWithCheckMate.
	 */
	private void consumeWhiteMoveWithCheckMate() {
		((Move) this.nodeStack[this.nodeStackPointer - 1]).setIsCheckMate(true);
	}

	/**
	 * Method consumeWhiteMoveWithCheck.
	 */
	private void consumeWhiteMoveWithCheck() {
		((Move) this.nodeStack[this.nodeStackPointer - 1]).setIsCheck(true);
	}

	/**
	 * Method consumeWhiteMove.
	 */
	private void consumeWhiteMove() {
		// nothing to do
	}

	/**
	 * Method consumeElementBlackMove.
	 */
	private void consumeElementBlackMove() {
		Move move = (Move) this.nodeStack[this.nodeStackPointer - 1];
		move.setMoveIndication(this.currentMoveIndication);
		move.setIsWhiteMove(false);
	}

	/**
	 * Method consumeElementTwoMoves.
	 */
	private void consumeElementTwoMoves() {
		Move move = (Move) this.nodeStack[this.nodeStackPointer - 1];
		move.setMoveIndication(this.currentMoveIndication);
		move.setIsWhiteMove(false);
		move = (Move) this.nodeStack[this.nodeStackPointer - 2];
		move.setMoveIndication(this.currentMoveIndication);
		move.setIsWhiteMove(true);
	}

	/**
	 * Method consumeEmptyElementSequence.
	 */
	private void consumeEmptyElementSequence() {
		pushOnNodeStack(new MoveText());
	}

	/**
	 * Method consumeElementSingleMove.
	 */
	private void consumeElementSingleMove() {
		Move move = (Move) this.nodeStack[this.nodeStackPointer - 1];
		move.setMoveIndication(this.currentMoveIndication);
		move.setIsWhiteMove(true);
	}

	private void pushOnNodeStack(ASTNode astNode) {
		if (this.nodeStackPointer == this.nodeStack.length) {
			System.arraycopy(this.nodeStack, 0, this.nodeStack = new ASTNode[this.nodeStackPointer + StackIncrement], 0,
					this.nodeStackPointer);
		}
		this.nodeStack[this.nodeStackPointer++] = astNode;
	}

	private void pushOnNodeInformationStack(char[] info) {
		if (this.nodeInformationPointer == this.nodeInformation.length) {
			System.arraycopy(this.nodeInformation, 0,
					this.nodeInformation = new char[this.nodeInformationPointer + StackIncrement][], 0,
					this.nodeInformationPointer);
		}
		this.nodeInformation[this.nodeInformationPointer++] = info;
	}
}
