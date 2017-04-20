 	// This method is part of an automatic generation : do NOT edit-modify  
	protected void consumeRule(int act) {
	  switch ( act ) {
	    case 1 : if (DEBUG) System.out.println("Goal ::= GREATER_THAN PGN-database");  //$NON-NLS-1$
			 consumeGoal(); 			
			break; 
	    case 2 : if (DEBUG) System.out.println("PGN-database ::= PGN-database PGN-game");  //$NON-NLS-1$
			 consumePGNDatabase(); 			
			break; 
	    case 3 : if (DEBUG) System.out.println("PGN-database ::=");  //$NON-NLS-1$
			 consumeEmptyPGNDatabase(); 			
			break; 
	    case 4 : if (DEBUG) System.out.println("PGN-game ::= tag-section movetext-section space");  //$NON-NLS-1$
			 consumePGNGame(); 			
			break; 
	    case 5 : if (DEBUG) System.out.println("tag-section ::= tag-section tag-pair space");  //$NON-NLS-1$
			 consumeTagSection(); 			
			break; 
	    case 6 : if (DEBUG) System.out.println("tag-section ::=");  //$NON-NLS-1$
			 consumeEmptyTagSection(); 			
			break; 
	    case 7 : if (DEBUG) System.out.println("tag-pair ::= Start_Tag_Section space tag-value space...");  //$NON-NLS-1$
			 consumeTagPair(); 			
			break; 
	    case 9 : if (DEBUG) System.out.println("movetext-section ::= element-sequence game-termination");  //$NON-NLS-1$
			 consumeMoveTextSection(); 			
			break; 
	    case 10 : if (DEBUG) System.out.println("element-sequence ::= element-sequence element");  //$NON-NLS-1$
			 consumeElementSequence(); 			
			break; 
	    case 11 : if (DEBUG) System.out.println("element-sequence ::= element-sequence recursive-variation");  //$NON-NLS-1$
			 consumeElementSequenceWithRecursiveVariation(); 			
			break; 
	    case 12 : if (DEBUG) System.out.println("element-sequence ::=");  //$NON-NLS-1$
			 consumeEmptyElementSequence(); 			
			break; 
	    case 13 : if (DEBUG) System.out.println("element ::= move-number-indication WhiteMove space");  //$NON-NLS-1$
			 consumeElementSingleMove(); 			
			break; 
	    case 14 : if (DEBUG) System.out.println("element ::= move-number-indication WhiteMove WHITESPACE...");  //$NON-NLS-1$
			 consumeElementTwoMoves(); 			
			break; 
	    case 15 : if (DEBUG) System.out.println("element ::= move-number-indication BlackMove space");  //$NON-NLS-1$
			 consumeElementBlackMove(); 			
			break; 
	    case 16 : if (DEBUG) System.out.println("WhiteMove ::= InnerSANMove numeric-annotation-glyph");  //$NON-NLS-1$
			 consumeWhiteMove(); 			
			break; 
	    case 17 : if (DEBUG) System.out.println("WhiteMove ::= InnerSANMove CHECK numeric-annotation-glyph");  //$NON-NLS-1$
			 consumeWhiteMoveWithCheck(); 			
			break; 
	    case 18 : if (DEBUG) System.out.println("WhiteMove ::= InnerSANMove CHECKMATE...");  //$NON-NLS-1$
			 consumeWhiteMoveWithCheckMate(); 			
			break; 
	    case 19 : if (DEBUG) System.out.println("BlackMove ::= BlackDots InnerSANMove...");  //$NON-NLS-1$
			 consumeBlackMove(); 			
			break; 
	    case 20 : if (DEBUG) System.out.println("BlackMove ::= BlackDots InnerSANMove CHECK...");  //$NON-NLS-1$
			 consumeBlackMoveWithCheck(); 			
			break; 
	    case 21 : if (DEBUG) System.out.println("BlackMove ::= BlackDots InnerSANMove CHECKMATE...");  //$NON-NLS-1$
			 consumeBlackMoveWithCheckMate(); 			
			break; 
	    case 23 : if (DEBUG) System.out.println("BlackMoveFollowingWhiteMove ::= WhiteMove");  //$NON-NLS-1$
			 consumeBlackMoveFollowingWhiteMove(); 			
			break; 
	    case 24 : if (DEBUG) System.out.println("InnerSANMove ::= FileName RankName");  //$NON-NLS-1$
			 consumePawnMove(); 			
			break; 
	    case 25 : if (DEBUG) System.out.println("InnerSANMove ::= FileName RankName PROMOTE...");  //$NON-NLS-1$
			 consumePawnMoveWithPromotion(); 			
			break; 
	    case 26 : if (DEBUG) System.out.println("InnerSANMove ::= FileName RankName PieceIdentification");  //$NON-NLS-1$
			 consumePawnMoveWithPromotion(); 			
			break; 
	    case 27 : if (DEBUG) System.out.println("InnerSANMove ::= FileName CAPTURE FileName RankName");  //$NON-NLS-1$
			 consumePawnMoveWithCapture(); 			
			break; 
	    case 28 : if (DEBUG) System.out.println("InnerSANMove ::= FileName CAPTURE FileName RankName...");  //$NON-NLS-1$
			 consumePawnMoveWithCapture(); 			
			break; 
	    case 29 : if (DEBUG) System.out.println("InnerSANMove ::= FileName CAPTURE FileName RankName...");  //$NON-NLS-1$
			 consumePawnMoveWithCaptureAndPromotion(); 			
			break; 
	    case 30 : if (DEBUG) System.out.println("InnerSANMove ::= FileName CAPTURE FileName RankName PROMOTE");  //$NON-NLS-1$
			 consumePawnMoveWithCaptureAndPromotion(); 			
			break; 
	    case 31 : if (DEBUG) System.out.println("InnerSANMove ::= PieceIdentification FileName RankName");  //$NON-NLS-1$
			 consumePieceMove(); 			
			break; 
	    case 32 : if (DEBUG) System.out.println("InnerSANMove ::= PieceIdentification FileName FileName...");  //$NON-NLS-1$
			 consumePieceMoveWithFileNameAmbiguity(); 			
			break; 
	    case 33 : if (DEBUG) System.out.println("InnerSANMove ::= PieceIdentification RankName FileName...");  //$NON-NLS-1$
			 consumePieceMoveWithRankNameAmbiguity(); 			
			break; 
	    case 34 : if (DEBUG) System.out.println("InnerSANMove ::= PieceIdentification CAPTURE FileName...");  //$NON-NLS-1$
			 consumePieceMoveWithCapture(); 			
			break; 
	    case 35 : if (DEBUG) System.out.println("InnerSANMove ::= PieceIdentification FileName CAPTURE...");  //$NON-NLS-1$
			 consumePieceMoveWithCaptureAndFileNameAmbiguity(); 			
			break; 
	    case 36 : if (DEBUG) System.out.println("InnerSANMove ::= PieceIdentification RankName CAPTURE...");  //$NON-NLS-1$
			 consumePieceMoveWithCaptureAndRankNameAmbiguity(); 			
			break; 
	    case 37 : if (DEBUG) System.out.println("InnerSANMove ::= PieceIdentification FileName RankName...");  //$NON-NLS-1$
			 consumePieceMoveWithCaptureAndDoubleAmbiguity(); 			
			break; 
	    case 38 : if (DEBUG) System.out.println("InnerSANMove ::= CASTLE_KING_SIDE");  //$NON-NLS-1$
			 consumeCastleKingSide(); 			
			break; 
	    case 39 : if (DEBUG) System.out.println("InnerSANMove ::= CASTLE_QUEEN_SIDE");  //$NON-NLS-1$
			 consumeCastleQueenSide(); 			
			break; 
	    case 40 : if (DEBUG) System.out.println("numeric-annotation-glyph ::= numeric-annotation-glyph...");  //$NON-NLS-1$
			 consumeNAG(); 			
			break; 
	    case 41 : if (DEBUG) System.out.println("numeric-annotation-glyph ::= EXCELLENT_MOVE");  //$NON-NLS-1$
			 consumeExcellentMoveNAG(); 			
			break; 
	    case 42 : if (DEBUG) System.out.println("numeric-annotation-glyph ::= VERY_BAD_MOVE");  //$NON-NLS-1$
			 consumeVeryBadMoveNAG(); 			
			break; 
	    case 43 : if (DEBUG) System.out.println("numeric-annotation-glyph ::= BAD_MOVE");  //$NON-NLS-1$
			 consumeBadMoveNAG(); 			
			break; 
	    case 44 : if (DEBUG) System.out.println("numeric-annotation-glyph ::= GOOD_MOVE");  //$NON-NLS-1$
			 consumeGoodMoveMoveNAG(); 			
			break; 
	    case 45 : if (DEBUG) System.out.println("numeric-annotation-glyph ::= INTERESTING_MOVE");  //$NON-NLS-1$
			 consumeInterestingMoveNAG(); 			
			break; 
	    case 46 : if (DEBUG) System.out.println("numeric-annotation-glyph ::= SUSPICIOUS_MOVE");  //$NON-NLS-1$
			 consumeSuspiciousMoveNAG(); 			
			break; 
	    case 47 : if (DEBUG) System.out.println("numeric-annotation-glyph ::=");  //$NON-NLS-1$
			 consumeEmptyNAG(); 			
			break; 
	    case 48 : if (DEBUG) System.out.println("move-number-indication ::= IntegerLiteral DOT space");  //$NON-NLS-1$
			 consumeMoveIndication(); 			
			break; 
	    case 49 : if (DEBUG) System.out.println("recursive-variation ::= START_VARIATION space...");  //$NON-NLS-1$
			 consumeRecursiveVariation(); 			
			break; 
	    case 52 : if (DEBUG) System.out.println("game-termination ::= WHITE_VICTORY");  //$NON-NLS-1$
			 consumeWhiteVictory(); 			
			break; 
	    case 53 : if (DEBUG) System.out.println("game-termination ::= BLACK_VICTORY");  //$NON-NLS-1$
			 consumeBlackVictory(); 			
			break; 
	    case 54 : if (DEBUG) System.out.println("game-termination ::= DRAW");  //$NON-NLS-1$
			 consumeDraw(); 			
			break; 
	    case 55 : if (DEBUG) System.out.println("game-termination ::= UNKNOWN");  //$NON-NLS-1$
			 consumeUnknownResult(); 			
			break; 
		}
	}
