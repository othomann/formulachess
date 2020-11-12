--main options
%options ACTION, AN=JavaAction.java, GP=java, 
%options FILE-PREFIX=java, ESCAPE=$, PREFIX=TOKEN_NAME_, OUTPUT-SIZE=125 ,
%options NOGOTO-DEFAULT, SINGLE-PRODUCTIONS, LALR=1 , TABLE=TIME , 

--error recovering options.....
%options ERROR_MAPS 

--grammar understanding options
%options first follow
%options TRACE=FULL ,
%options VERBOSE

--Usefull macros helping reading/writing semantic actions
$Define 
$putCase 
/.	    case $rule_number : if (DEBUG) System.out.println("$rule_text");  //$NON-NLS-1$
			./

$break
/.
			
			break;./

-- here it starts really ------------------------------------------
$Terminals

	INTEGER
	STRING
	
	COLUMN
	RANK
	PIECE_ID
	START_TAG_SECTION
	START_NAG
	
	Comment

	START_VARIATION
	END_VARIATION
	DOT
	
	END_TAG_SECTION
	WHITE_VICTORY
	BLACK_VICTORY
	DRAW
	UNKNOWN
	CAPTURE
	CHECK
	CHECKMATE
	CASTLE_KING_SIDE
	CASTLE_QUEEN_SIDE
	PROMOTE
	EXCELLENT_MOVE
	VERY_BAD_MOVE
	BAD_MOVE
	GOOD_MOVE
	INTERESTING_MOVE
	SUSPICIOUS_MOVE
	GREATER_THAN
	EN_PASSANT
	WHITESPACE
	
$Start
	Goal 
$Rules

/. 	// This method is part of an automatic generation : do NOT edit-modify  
	protected void consumeRule(int act) {
	  switch ( act ) {
./

Goal ::= GREATER_THAN PGN-database
 /.$putCase consumeGoal(); $break ./
 
PGN-database ::= PGN-database PGN-game
 /.$putCase consumePGNDatabase(); $break ./
PGN-database ::= $empty
/.$putCase consumeEmptyPGNDatabase(); $break ./

PGN-game ::= tag-section movetext-section space
/.$putCase consumePGNGame(); $break ./

tag-section ::= space tag-section tag-pair space
/.$putCase consumeTagSection(); $break ./
tag-section ::= space tag-section tag-pair space
/.$putCase consumeTagSection(); $break ./
tag-section ::= $empty
/.$putCase consumeEmptyTagSection(); $break ./

tag-pair ::= START_TAG_SECTION space tag-value space END_TAG_SECTION
/.$putCase consumeTagPair(); $break ./

tag-value -> STRING

movetext-section ::= element-sequence game-termination
/.$putCase consumeMoveTextSection(); $break ./

element-sequence ::= element-sequence element
/.$putCase consumeElementSequence(); $break ./
element-sequence ::= element-sequence recursive-variation
/.$putCase consumeElementSequenceWithRecursiveVariation(); $break ./
element-sequence ::= $empty
/.$putCase consumeEmptyElementSequence(); $break ./

element ::= move-number-indication WhiteMove space
/.$putCase consumeElementSingleMove(); $break ./
element ::= move-number-indication WhiteMove WHITESPACE BlackMoveFollowingWhiteMove space
/.$putCase consumeElementTwoMoves(); $break ./
element ::= move-number-indication BlackMove space
/.$putCase consumeElementBlackMove(); $break ./
element ::= Comment space
/.$putCase consumeComment(); $break ./

WhiteMove ::= InnerSANMove numeric-annotation-glyph
/.$putCase consumeWhiteMove(); $break ./
WhiteMove ::= InnerSANMove CHECK numeric-annotation-glyph
/.$putCase consumeWhiteMoveWithCheck(); $break ./
WhiteMove ::= InnerSANMove CHECKMATE numeric-annotation-glyph
/.$putCase consumeWhiteMoveWithCheckMate(); $break ./
BlackMove ::= BlackDots InnerSANMove numeric-annotation-glyph
/.$putCase consumeBlackMove(); $break ./
BlackMove ::= BlackDots InnerSANMove CHECK numeric-annotation-glyph
/.$putCase consumeBlackMoveWithCheck(); $break ./
BlackMove ::= BlackDots InnerSANMove CHECKMATE numeric-annotation-glyph
/.$putCase consumeBlackMoveWithCheckMate(); $break ./

BlackDots -> DOT DOT space

BlackMoveFollowingWhiteMove ::= WhiteMove
/.$putCase consumeBlackMoveFollowingWhiteMove(); $break ./

InnerSANMove ::= COLUMN RANK
/.$putCase consumePawnMove(); $break ./
InnerSANMove ::= COLUMN RANK PROMOTE PIECE_ID
/.$putCase consumePawnMoveWithPromotion(); $break ./
InnerSANMove ::= COLUMN RANK PIECE_ID
/.$putCase consumePawnMoveWithPromotion(); $break ./
InnerSANMove ::= COLUMN CAPTURE COLUMN RANK
/.$putCase consumePawnMoveWithCapture(); $break ./
InnerSANMove ::= COLUMN CAPTURE COLUMN RANK EN_PASSANT
/.$putCase consumePawnMoveWithCapture(); $break ./
InnerSANMove ::= COLUMN CAPTURE COLUMN RANK PIECE_ID
/.$putCase consumePawnMoveWithCaptureAndPromotion(); $break ./
InnerSANMove ::= COLUMN CAPTURE COLUMN RANK PROMOTE PIECE_ID
/.$putCase consumePawnMoveWithCaptureAndPromotion(); $break ./
InnerSANMove ::= PIECE_ID COLUMN RANK
/.$putCase consumePieceMove(); $break ./
InnerSANMove ::= PIECE_ID COLUMN COLUMN RANK
/.$putCase consumePieceMoveWithCOLUMNAmbiguity(); $break ./
InnerSANMove ::= PIECE_ID RANK COLUMN RANK
/.$putCase consumePieceMoveWithRANKAmbiguity(); $break ./
InnerSANMove ::= PIECE_ID CAPTURE COLUMN RANK
/.$putCase consumePieceMoveWithCapture(); $break ./
InnerSANMove ::= PIECE_ID COLUMN CAPTURE COLUMN RANK
/.$putCase consumePieceMoveWithCaptureAndCOLUMNAmbiguity(); $break ./
InnerSANMove ::= PIECE_ID RANK CAPTURE COLUMN RANK
/.$putCase consumePieceMoveWithCaptureAndRANKAmbiguity(); $break ./
InnerSANMove ::= PIECE_ID COLUMN RANK CAPTURE COLUMN RANK
/.$putCase consumePieceMoveWithCaptureAndDoubleAmbiguity(); $break ./
InnerSANMove ::= CASTLE_KING_SIDE
/.$putCase consumeCastleKingSide(); $break ./
InnerSANMove ::= CASTLE_QUEEN_SIDE
/.$putCase consumeCastleQueenSide(); $break ./

numeric-annotation-glyph ::= numeric-annotation-glyph START_NAG
/.$putCase consumeNAG(); $break ./
numeric-annotation-glyph ::= EXCELLENT_MOVE
/.$putCase consumeExcellentMoveNAG(); $break ./
numeric-annotation-glyph ::= VERY_BAD_MOVE
/.$putCase consumeVeryBadMoveNAG(); $break ./
numeric-annotation-glyph ::= BAD_MOVE
/.$putCase consumeBadMoveNAG(); $break ./
numeric-annotation-glyph ::= GOOD_MOVE
/.$putCase consumeGoodMoveMoveNAG(); $break ./
numeric-annotation-glyph ::= INTERESTING_MOVE
/.$putCase consumeInterestingMoveNAG(); $break ./
numeric-annotation-glyph ::= SUSPICIOUS_MOVE
/.$putCase consumeSuspiciousMoveNAG(); $break ./
numeric-annotation-glyph ::= $empty
/.$putCase consumeEmptyNAG(); $break ./

move-number-indication ::= INTEGER DOT space
/.$putCase consumeMoveIndication(); $break ./

recursive-variation ::= START_VARIATION space element-sequence END_VARIATION space
/.$putCase consumeRecursiveVariation(); $break ./

space ::= $empty
space ::= WHITESPACE

game-termination ::= WHITE_VICTORY
/.$putCase consumeWhiteVictory(); $break ./
game-termination ::= BLACK_VICTORY
/.$putCase consumeBlackVictory(); $break ./
game-termination ::= DRAW
/.$putCase consumeDraw(); $break ./
game-termination ::= UNKNOWN
/.$putCase consumeUnknownResult(); $break ./

/.		}
	}./

---------------------------------------------------------------------------------------
$names

-- BodyMarker ::= '"class Identifier { ... MethodHeader "'

-- void ::= 'void'

START_VARIATION ::= '('
END_VARIATION ::= ')'
DOT ::= '.'
END_TAG_SECTION ::= ']'
WHITE_VICTORY ::= '1-0'
BLACK_VICTORY ::= '0-1'
DRAW ::= '1/2-1/2'
UNKNOWN ::= '*'
CAPTURE ::= 'x'
CHECK ::= '+'
CHECKMATE ::= '#'
CASTLE_KING_SIDE ::= 'O-O'
CASTLE_QUEEN_SIDE ::= 'O-O-O'
PROMOTE ::= '='
EXCELLENT_MOVE ::= '!!'
VERY_BAD_MOVE ::= '??'
BAD_MOVE ::= '?'
GOOD_MOVE ::= '!'
INTERESTING_MOVE ::= '!?'
SUSPICIOUS_MOVE ::= '?!'
GREATER_THAN ::= '>'

$end
-- need a carriage return after the $end
