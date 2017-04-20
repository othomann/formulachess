package org.formulachess.pgn;

public interface TerminalSymbols {

	public final static int TokenNameIntegerLiteral = 19,
			TokenNameStringLiteral = 30, TokenNameFileName = 1,
			TokenNameRankName = 4, TokenNamePieceIdentification = 5,
			TokenNameStart_Tag_Section = 31, TokenNameStart_nag = 6,
			TokenNameSTART_VARIATION = 20, TokenNameEND_VARIATION = 32,
			TokenNameDOT = 15, TokenNameEND_TAG_SECTION = 33,
			TokenNameWHITE_VICTORY = 34, TokenNameBLACK_VICTORY = 35,
			TokenNameDRAW = 36, TokenNameUNKNOWN = 37, TokenNameCAPTURE = 14,
			TokenNameCHECK = 21, TokenNameCHECKMATE = 22,
			TokenNameCASTLE_KING_SIDE = 16, TokenNameCASTLE_QUEEN_SIDE = 17,
			TokenNamePROMOTE = 23, TokenNameEXCELLENT_MOVE = 7,
			TokenNameVERY_BAD_MOVE = 8, TokenNameBAD_MOVE = 9,
			TokenNameGOOD_MOVE = 10, TokenNameINTERESTING_MOVE = 11,
			TokenNameSUSPICIOUS_MOVE = 12, TokenNameGREATER_THAN = 38,
			TokenNameEN_PASSANT = 39, TokenNameWHITESPACE = 2,
			TokenNameEOF = 24, TokenNameERROR = 51;
}
