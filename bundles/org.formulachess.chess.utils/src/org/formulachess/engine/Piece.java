package org.formulachess.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Piece {
	UNDEFINED(0), WHITE_KING(1), WHITE_QUEEN(2), WHITE_ROOK(3), WHITE_BISHOP(4), WHITE_KNIGHT(5), WHITE_PAWN(6), EMPTY(
			7), BLACK_KING(9), BLACK_QUEEN(10), BLACK_ROOK(11), BLACK_BISHOP(12), BLACK_KNIGHT(13), BLACK_PAWN(14);

	int value;

	Piece(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static Piece getPiece(int i) {
		switch (i) {
		case 1:
			return WHITE_KING;
		case 2:
			return WHITE_QUEEN;
		case 3:
			return WHITE_ROOK;
		case 4:
			return WHITE_BISHOP;
		case 5:
			return WHITE_KNIGHT;
		case 6:
			return WHITE_PAWN;
		case 7:
			return EMPTY;
		case 9:
			return BLACK_KING;
		case 10:
			return BLACK_QUEEN;
		case 11:
			return BLACK_ROOK;
		case 12:
			return BLACK_BISHOP;
		case 13:
			return BLACK_KNIGHT;
		case 14:
			return BLACK_PAWN;
		default:
			return UNDEFINED;
		}
	}

	public static List<Piece> getBlackPromotionPieces() {
		List<Piece> blackPieces = new ArrayList<>();
		blackPieces.add(BLACK_QUEEN);
		blackPieces.add(BLACK_ROOK);
		blackPieces.add(BLACK_BISHOP);
		blackPieces.add(BLACK_KNIGHT);
		return Collections.unmodifiableList(blackPieces);
	}

	public static List<Piece> getWhitePromotionPieces() {
		List<Piece> whitePieces = new ArrayList<>();
		whitePieces.add(WHITE_QUEEN);
		whitePieces.add(WHITE_ROOK);
		whitePieces.add(WHITE_BISHOP);
		whitePieces.add(WHITE_KNIGHT);
		return Collections.unmodifiableList(whitePieces);
	}

	public boolean isBlack() {
		return this.getValue() > EMPTY.getValue();
	}

	public boolean isWhite() {
		return this.getValue() < EMPTY.getValue();
	}

	public boolean isWhiteOrEmpty() {
		return this.getValue() <= EMPTY.getValue();
	}

	public boolean isBlackOrEmpty() {
		return this.getValue() >= EMPTY.getValue();
	}
}
