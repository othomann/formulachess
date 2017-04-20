

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 1

%OPTIONS ACTION, AN=JavaAction.java, GP=java, 
%OPTIONS FILE-PREFIX=java, ESCAPE=$, PREFIX=TokenName, OUTPUT-SIZE=125 ,
%OPTIONS NOGOTO-DEFAULT, SINGLE-PRODUCTIONS, LALR=1 , TABLE=TIME , 
%OPTIONS ERROR_MAPS 
%OPTIONS first follow
%OPTIONS TRACE=FULL ,
%OPTIONS VERBOSE

Options in effect:
    ACTION  ACTFILE-NAME=JavaAction.java  BLOCKB=/.  BLOCKE=./  BYTE  CONFLICTS
    DEFAULT=5  NODEBUG  DEFERRED  NOEDIT  ERROR-MAPS  ESCAPE=$  
    FILE-PREFIX=java  FIRST  FOLLOW  GENERATE-PARSER=JAVA  NOGOTO-DEFAULT  
    HACTFILE-NAME=javahdr.java  HBLOCKB=/:  HBLOCKE=:/  LALR=1  LIST  
    MAX-DISTANCE=30  MIN-DISTANCE=3  NAMES=OPTIMIZED  NONT-CHECK  ORMARK=|  
    OUTPUT-SIZE=125  PREFIX=TokenName  READ-REDUCE  NOSCOPES  NOSHIFT-DEFAULT
    SINGLE-PRODUCTIONS  STACK-SIZE=128  STATES  SUFFIX=  TABLE=TIME  TRACE=FULL
    VERBOSE  WARNINGS  XREF  



 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 2


Defined Symbols:



$putcase
/.------------------------------------------------------------------------------
	    case $rule_number : if (DEBUG) System.out.println("$rule_text");  //$NON-NLS-1$
			
./------------------------------------------------------------------------------


$break
/.------------------------------------------------------------------------------
			
			break;
./------------------------------------------------------------------------------


 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 3


Terminals:


        IntegerLiteral StringLiteral FileName RankName PieceIdentification 
    Start_Tag_Section Start_nag START_VARIATION END_VARIATION DOT 
    END_TAG_SECTION WHITE_VICTORY BLACK_VICTORY DRAW UNKNOWN CAPTURE CHECK 
    CHECKMATE CASTLE_KING_SIDE CASTLE_QUEEN_SIDE PROMOTE EXCELLENT_MOVE 
    VERY_BAD_MOVE BAD_MOVE GOOD_MOVE INTERESTING_MOVE SUSPICIOUS_MOVE 
    GREATER_THAN EN_PASSANT WHITESPACE $eof $error 

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 4


Rules:


0     $acc ::= Goal 
1     Goal ::= GREATER_THAN PGN-database 
2     PGN-database ::= PGN-database PGN-game 
3     PGN-database ::= 
4     PGN-game ::= tag-section movetext-section space 
5     tag-section ::= tag-section tag-pair space 
6     tag-section ::= 
7     tag-pair ::= Start_Tag_Section space tag-value space END_TAG_SECTION 
8     tag-value -> StringLiteral 
9     movetext-section ::= element-sequence game-termination 
10    element-sequence ::= element-sequence element 
11    element-sequence ::= element-sequence recursive-variation 
12    element-sequence ::= 
13    element ::= move-number-indication WhiteMove space 
14    element ::= move-number-indication WhiteMove WHITESPACE 
                  BlackMoveFollowingWhiteMove space 
15    element ::= move-number-indication BlackMove space 
16    WhiteMove ::= InnerSANMove numeric-annotation-glyph 
17    WhiteMove ::= InnerSANMove CHECK numeric-annotation-glyph 
18    WhiteMove ::= InnerSANMove CHECKMATE numeric-annotation-glyph 
19    BlackMove ::= BlackDots InnerSANMove numeric-annotation-glyph 
20    BlackMove ::= BlackDots InnerSANMove CHECK numeric-annotation-glyph 
21    BlackMove ::= BlackDots InnerSANMove CHECKMATE numeric-annotation-glyph 
22    BlackDots ::= DOT DOT space 
23    BlackMoveFollowingWhiteMove ::= WhiteMove 
24    InnerSANMove ::= FileName RankName 
25    InnerSANMove ::= FileName RankName PROMOTE PieceIdentification 
26    InnerSANMove ::= FileName RankName PieceIdentification 
27    InnerSANMove ::= FileName CAPTURE FileName RankName 
28    InnerSANMove ::= FileName CAPTURE FileName RankName EN_PASSANT 
29    InnerSANMove ::= FileName CAPTURE FileName RankName PieceIdentification 
30    InnerSANMove ::= FileName CAPTURE FileName RankName PROMOTE 
                       PieceIdentification 
31    InnerSANMove ::= PieceIdentification FileName RankName 
32    InnerSANMove ::= PieceIdentification FileName FileName RankName 
33    InnerSANMove ::= PieceIdentification RankName FileName RankName 
34    InnerSANMove ::= PieceIdentification CAPTURE FileName RankName 
35    InnerSANMove ::= PieceIdentification FileName CAPTURE FileName RankName 
36    InnerSANMove ::= PieceIdentification RankName CAPTURE FileName RankName 
37    InnerSANMove ::= PieceIdentification FileName RankName CAPTURE FileName 
                       RankName 
38    InnerSANMove ::= CASTLE_KING_SIDE 
39    InnerSANMove ::= CASTLE_QUEEN_SIDE 
40    numeric-annotation-glyph ::= numeric-annotation-glyph Start_nag 
41    numeric-annotation-glyph ::= EXCELLENT_MOVE 
42    numeric-annotation-glyph ::= VERY_BAD_MOVE 
43    numeric-annotation-glyph ::= BAD_MOVE 
44    numeric-annotation-glyph ::= GOOD_MOVE 

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 5


45    numeric-annotation-glyph ::= INTERESTING_MOVE 
46    numeric-annotation-glyph ::= SUSPICIOUS_MOVE 
47    numeric-annotation-glyph ::= 
48    move-number-indication ::= IntegerLiteral DOT space 
49    recursive-variation ::= START_VARIATION space element-sequence 
                              END_VARIATION space 
50    space ::= WHITESPACE 
51    space ::= 
52    game-termination ::= WHITE_VICTORY 
53    game-termination ::= BLACK_VICTORY 
54    game-termination ::= DRAW 
55    game-termination ::= UNKNOWN 

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 6



Cross-reference table:


$error  ==>> 

BAD_MOVE  ==>> 43 

BLACK_VICTORY  ==>> 53 

BlackDots  ==>> 22 

BlackMove  ==>> 19 20 21 

BlackMoveFollowingWhiteMove  ==>> 23 

CAPTURE  ==>> 27 28 29 30 34 35 36 37 

CASTLE_KING_SIDE  ==>> 38 

CASTLE_QUEEN_SIDE  ==>> 39 

CHECK  ==>> 17 20 

CHECKMATE  ==>> 18 21 

DOT  ==>> 22 22 48 

DRAW  ==>> 54 

END_TAG_SECTION  ==>> 7 

END_VARIATION  ==>> 49 

EN_PASSANT  ==>> 28 

EXCELLENT_MOVE  ==>> 41 

FileName  ==>> 24 25 26 27 27 28 28 29 29 30 30 31 32 32 33 34 35 35 36 37 37 

GOOD_MOVE  ==>> 44 

GREATER_THAN  ==>> 1 

Goal  ==>> 1 

INTERESTING_MOVE  ==>> 45 

InnerSANMove  ==>> 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 

IntegerLiteral  ==>> 48 

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 7



PGN-database  ==>> 2 3 

PGN-game  ==>> 4 

PROMOTE  ==>> 25 30 

PieceIdentification  ==>> 25 26 29 30 31 32 33 34 35 36 37 

RankName  ==>> 24 25 26 27 28 29 30 31 32 33 33 34 35 36 36 37 37 

START_VARIATION  ==>> 49 

SUSPICIOUS_MOVE  ==>> 46 

Start_Tag_Section  ==>> 7 

Start_nag  ==>> 40 

StringLiteral  ==>> 8 

UNKNOWN  ==>> 55 

VERY_BAD_MOVE  ==>> 42 

WHITESPACE  ==>> 14 50 

WHITE_VICTORY  ==>> 52 

WhiteMove  ==>> 16 17 18 

element  ==>> 13 14 15 

element-sequence  ==>> 10 11 12 

game-termination  ==>> 52 53 54 55 

move-number-indication  ==>> 48 

movetext-section  ==>> 9 

numeric-annotation-glyph  ==>> 40 41 42 43 44 45 46 47 

recursive-variation  ==>> 49 

space  ==>> 50 51 

tag-pair  ==>> 7 

tag-section  ==>> 5 6 


 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 8


tag-value  ==>> 8 



 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 9


First map for non-terminals:


$acc  ==>> GREATER_THAN 

Goal  ==>> GREATER_THAN 

PGN-database  ==>> $empty IntegerLiteral Start_Tag_Section START_VARIATION 
    WHITE_VICTORY BLACK_VICTORY DRAW UNKNOWN 

PGN-game  ==>> IntegerLiteral Start_Tag_Section START_VARIATION WHITE_VICTORY 
    BLACK_VICTORY DRAW UNKNOWN 

tag-section  ==>> $empty Start_Tag_Section 

movetext-section  ==>> IntegerLiteral START_VARIATION WHITE_VICTORY 
    BLACK_VICTORY DRAW UNKNOWN 

space  ==>> $empty WHITESPACE 

tag-pair  ==>> Start_Tag_Section 

tag-value  ==>> StringLiteral 

element-sequence  ==>> $empty IntegerLiteral START_VARIATION 

game-termination  ==>> WHITE_VICTORY BLACK_VICTORY DRAW UNKNOWN 

element  ==>> IntegerLiteral 

recursive-variation  ==>> START_VARIATION 

move-number-indication  ==>> IntegerLiteral 

WhiteMove  ==>> FileName PieceIdentification CASTLE_KING_SIDE CASTLE_QUEEN_SIDE 

BlackMoveFollowingWhiteMove  ==>> FileName PieceIdentification CASTLE_KING_SIDE 
    CASTLE_QUEEN_SIDE 

BlackMove  ==>> DOT 

InnerSANMove  ==>> FileName PieceIdentification CASTLE_KING_SIDE 
    CASTLE_QUEEN_SIDE 

numeric-annotation-glyph  ==>> $empty Start_nag EXCELLENT_MOVE VERY_BAD_MOVE 
    BAD_MOVE GOOD_MOVE INTERESTING_MOVE SUSPICIOUS_MOVE 

BlackDots  ==>> DOT 


 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 10


Follow Map:


$acc  ==>> $eof 

Goal  ==>> $eof 

PGN-database  ==>> IntegerLiteral Start_Tag_Section START_VARIATION 
    WHITE_VICTORY BLACK_VICTORY DRAW UNKNOWN $eof 

PGN-game  ==>> IntegerLiteral Start_Tag_Section START_VARIATION WHITE_VICTORY 
    BLACK_VICTORY DRAW UNKNOWN $eof 

tag-section  ==>> IntegerLiteral Start_Tag_Section START_VARIATION 
    WHITE_VICTORY BLACK_VICTORY DRAW UNKNOWN 

movetext-section  ==>> IntegerLiteral Start_Tag_Section START_VARIATION 
    WHITE_VICTORY BLACK_VICTORY DRAW UNKNOWN WHITESPACE $eof 

space  ==>> IntegerLiteral StringLiteral FileName PieceIdentification 
    Start_Tag_Section START_VARIATION END_VARIATION DOT END_TAG_SECTION 
    WHITE_VICTORY BLACK_VICTORY DRAW UNKNOWN CASTLE_KING_SIDE 
    CASTLE_QUEEN_SIDE $eof 

tag-pair  ==>> IntegerLiteral Start_Tag_Section START_VARIATION WHITE_VICTORY 
    BLACK_VICTORY DRAW UNKNOWN WHITESPACE 

tag-value  ==>> END_TAG_SECTION WHITESPACE 

element-sequence  ==>> IntegerLiteral START_VARIATION END_VARIATION 
    WHITE_VICTORY BLACK_VICTORY DRAW UNKNOWN 

game-termination  ==>> IntegerLiteral Start_Tag_Section START_VARIATION 
    WHITE_VICTORY BLACK_VICTORY DRAW UNKNOWN WHITESPACE $eof 

element  ==>> IntegerLiteral START_VARIATION END_VARIATION WHITE_VICTORY 
    BLACK_VICTORY DRAW UNKNOWN 

recursive-variation  ==>> IntegerLiteral START_VARIATION END_VARIATION 
    WHITE_VICTORY BLACK_VICTORY DRAW UNKNOWN 

move-number-indication  ==>> FileName PieceIdentification DOT CASTLE_KING_SIDE 
    CASTLE_QUEEN_SIDE 

WhiteMove  ==>> IntegerLiteral START_VARIATION END_VARIATION WHITE_VICTORY 
    BLACK_VICTORY DRAW UNKNOWN WHITESPACE 

BlackMoveFollowingWhiteMove  ==>> IntegerLiteral START_VARIATION END_VARIATION 
    WHITE_VICTORY BLACK_VICTORY DRAW UNKNOWN WHITESPACE 

BlackMove  ==>> IntegerLiteral START_VARIATION END_VARIATION WHITE_VICTORY 

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 11


    BLACK_VICTORY DRAW UNKNOWN WHITESPACE 

InnerSANMove  ==>> IntegerLiteral Start_nag START_VARIATION END_VARIATION 
    WHITE_VICTORY BLACK_VICTORY DRAW UNKNOWN CHECK CHECKMATE EXCELLENT_MOVE 
    VERY_BAD_MOVE BAD_MOVE GOOD_MOVE INTERESTING_MOVE SUSPICIOUS_MOVE 
    WHITESPACE 

numeric-annotation-glyph  ==>> IntegerLiteral Start_nag START_VARIATION 
    END_VARIATION WHITE_VICTORY BLACK_VICTORY DRAW UNKNOWN WHITESPACE 

BlackDots  ==>> FileName PieceIdentification CASTLE_KING_SIDE 
    CASTLE_QUEEN_SIDE 


 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 12


Name map:

$empty ::= $empty
IntegerLiteral ::= IntegerLiteral
StringLiteral ::= StringLiteral
FileName ::= FileName
RankName ::= RankName
PieceIdentification ::= PieceIdentification
Start_Tag_Section ::= Start_Tag_Section
Start_nag ::= Start_nag
START_VARIATION ::= (
END_VARIATION ::= )
DOT ::= .
END_TAG_SECTION ::= ]
WHITE_VICTORY ::= 1-0
BLACK_VICTORY ::= 0-1
DRAW ::= 1/2-1/2
UNKNOWN ::= *
CAPTURE ::= x
CHECK ::= +
CHECKMATE ::= #
CASTLE_KING_SIDE ::= O-O
CASTLE_QUEEN_SIDE ::= O-O-O
PROMOTE ::= =
EXCELLENT_MOVE ::= !!
VERY_BAD_MOVE ::= ??
BAD_MOVE ::= ?
GOOD_MOVE ::= !
INTERESTING_MOVE ::= !?
SUSPICIOUS_MOVE ::= ?!
GREATER_THAN ::= >
EN_PASSANT ::= EN_PASSANT
WHITESPACE ::= WHITESPACE
$eof ::= $eof
$error ::= $error
Goal ::= Goal
PGN-game ::= PGN-game
movetext-section ::= movetext-section
tag-pair ::= tag-pair
tag-value ::= tag-value
game-termination ::= game-termination
element ::= element
recursive-variation ::= recursive-variation
move-number-indication ::= move-number-indication
WhiteMove ::= WhiteMove
BlackMoveFollowingWhiteMove ::= BlackMoveFollowingWhiteMove
BlackMove ::= BlackMove
InnerSANMove ::= InnerSANMove
BlackDots ::= BlackDots
PGN-database ::= 
tag-section ::= 

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 13


space ::= 
element-sequence ::= 
numeric-annotation-glyph ::= 

This grammar is LALR(1).


 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 14


Number of Terminals: 32
Number of Nonterminals: 19
Number of Productions: 56
Number of Single Productions: 1
Number of Items: 196
Number of States: 59
Number of Shift actions: 41
Number of Goto actions: 26
Number of Shift/Reduce actions: 74
Number of Goto/Reduce actions: 15
Number of Reduce actions: 243
Number of Shift-Reduce conflicts: 0
Number of Reduce-Reduce conflicts: 0


 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 15

Shift STATES: 


STATE 1 ------------------------------------------------------------------------
( )

$acc ::= .Goal 

Goal ::= .GREATER_THAN PGN-database 

GREATER_THAN    Shift  3

Goal            Goto   2


STATE 2 ------------------------------------------------------------------------
( 1 )

$acc ::= Goal . (0)

$eof    Accept


STATE 3 ------------------------------------------------------------------------
( 1 )

Goal ::= GREATER_THAN  .PGN-database 

PGN-database ::= .PGN-database PGN-game 
PGN-database ::= . (3)

PGN-database         Goto   4

IntegerLiteral       Reduce 3
Start_Tag_Section    Reduce 3
START_VARIATION      Reduce 3
WHITE_VICTORY        Reduce 3
BLACK_VICTORY        Reduce 3
DRAW                 Reduce 3
UNKNOWN              Reduce 3
$eof                 Reduce 3

Default reduction to rule  3


STATE 4 ------------------------------------------------------------------------
( 3 )

Goal ::= GREATER_THAN PGN-database . (1)
PGN-database ::= PGN-database  .PGN-game 

PGN-game ::= .tag-section movetext-section space 

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 16


tag-section ::= .tag-section tag-pair space 
tag-section ::= . (6)

tag-section          Goto   5
PGN-game             Gt/Rd  2

$eof                 Reduce 1
IntegerLiteral       Reduce 6
Start_Tag_Section    Reduce 6
START_VARIATION      Reduce 6
WHITE_VICTORY        Reduce 6
BLACK_VICTORY        Reduce 6
DRAW                 Reduce 6
UNKNOWN              Reduce 6

Default reduction to rule  6


STATE 5 ------------------------------------------------------------------------
( 4 )

PGN-game ::= tag-section  .movetext-section space 
tag-section ::= tag-section  .tag-pair space 

tag-pair ::= .Start_Tag_Section space tag-value space END_TAG_SECTION 
movetext-section ::= .element-sequence game-termination 
element-sequence ::= .element-sequence element 
element-sequence ::= .element-sequence recursive-variation 
element-sequence ::= . (12)

Start_Tag_Section    Shift  9

element-sequence     Goto   8
movetext-section     Goto   7
tag-pair             Goto   6

IntegerLiteral       Reduce 12
START_VARIATION      Reduce 12
WHITE_VICTORY        Reduce 12
BLACK_VICTORY        Reduce 12
DRAW                 Reduce 12
UNKNOWN              Reduce 12

Default reduction to rule  12


STATE 6 ------------------------------------------------------------------------
( 5 )

tag-section ::= tag-section tag-pair  .space 


 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 17


space ::= .WHITESPACE 
space ::= . (51)

WHITESPACE           Sh/Rd  50

space                Gt/Rd  5

IntegerLiteral       Reduce 51
Start_Tag_Section    Reduce 51
START_VARIATION      Reduce 51
WHITE_VICTORY        Reduce 51
BLACK_VICTORY        Reduce 51
DRAW                 Reduce 51
UNKNOWN              Reduce 51

Default reduction to rule  51


STATE 7 ------------------------------------------------------------------------
( 5 )

PGN-game ::= tag-section movetext-section  .space 

space ::= .WHITESPACE 
space ::= . (51)

WHITESPACE           Sh/Rd  50

space                Gt/Rd  4

IntegerLiteral       Reduce 51
Start_Tag_Section    Reduce 51
START_VARIATION      Reduce 51
WHITE_VICTORY        Reduce 51
BLACK_VICTORY        Reduce 51
DRAW                 Reduce 51
UNKNOWN              Reduce 51
$eof                 Reduce 51

Default reduction to rule  51


STATE 8 ------------------------------------------------------------------------
( 5 )

movetext-section ::= element-sequence  .game-termination 
element-sequence ::= element-sequence  .element 
element-sequence ::= element-sequence  .recursive-variation 

element ::= .move-number-indication WhiteMove space 
element ::= .move-number-indication WhiteMove WHITESPACE 

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 18


            BlackMoveFollowingWhiteMove space 
element ::= .move-number-indication BlackMove space 
move-number-indication ::= .IntegerLiteral DOT space 
recursive-variation ::= .START_VARIATION space element-sequence END_VARIATION 
                        space 
game-termination ::= .WHITE_VICTORY 
game-termination ::= .BLACK_VICTORY 
game-termination ::= .DRAW 
game-termination ::= .UNKNOWN 

UNKNOWN                   Sh/Rd  55
DRAW                      Sh/Rd  54
BLACK_VICTORY             Sh/Rd  53
WHITE_VICTORY             Sh/Rd  52
IntegerLiteral            Shift  11
START_VARIATION           Shift  12

move-number-indication    Goto   10
game-termination          Gt/Rd  9
element                   Gt/Rd  10
recursive-variation       Gt/Rd  11


STATE 9 ------------------------------------------------------------------------
( 5 )

tag-pair ::= Start_Tag_Section  .space tag-value space END_TAG_SECTION 

space ::= .WHITESPACE 
space ::= . (51)

WHITESPACE       Sh/Rd  50

space            Goto   13

StringLiteral    Reduce 51

Default reduction to rule  51


STATE 10 -----------------------------------------------------------------------
( 8 35 )

element ::= move-number-indication  .WhiteMove space 
element ::= move-number-indication  .WhiteMove WHITESPACE 
            BlackMoveFollowingWhiteMove space 
element ::= move-number-indication  .BlackMove space 

WhiteMove ::= .InnerSANMove numeric-annotation-glyph 
WhiteMove ::= .InnerSANMove CHECK numeric-annotation-glyph 
WhiteMove ::= .InnerSANMove CHECKMATE numeric-annotation-glyph 

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 19


BlackMove ::= .BlackDots InnerSANMove numeric-annotation-glyph 
BlackMove ::= .BlackDots InnerSANMove CHECK numeric-annotation-glyph 
BlackMove ::= .BlackDots InnerSANMove CHECKMATE numeric-annotation-glyph 
BlackDots ::= .DOT DOT space 
InnerSANMove ::= .FileName RankName 
InnerSANMove ::= .FileName RankName PROMOTE PieceIdentification 
InnerSANMove ::= .FileName RankName PieceIdentification 
InnerSANMove ::= .FileName CAPTURE FileName RankName 
InnerSANMove ::= .FileName CAPTURE FileName RankName EN_PASSANT 
InnerSANMove ::= .FileName CAPTURE FileName RankName PieceIdentification 
InnerSANMove ::= .FileName CAPTURE FileName RankName PROMOTE 
                 PieceIdentification 
InnerSANMove ::= .PieceIdentification FileName RankName 
InnerSANMove ::= .PieceIdentification FileName FileName RankName 
InnerSANMove ::= .PieceIdentification RankName FileName RankName 
InnerSANMove ::= .PieceIdentification CAPTURE FileName RankName 
InnerSANMove ::= .PieceIdentification FileName CAPTURE FileName RankName 
InnerSANMove ::= .PieceIdentification RankName CAPTURE FileName RankName 
InnerSANMove ::= .PieceIdentification FileName RankName CAPTURE FileName 
                 RankName 
InnerSANMove ::= .CASTLE_KING_SIDE 
InnerSANMove ::= .CASTLE_QUEEN_SIDE 

CASTLE_QUEEN_SIDE      Sh/Rd  39
CASTLE_KING_SIDE       Sh/Rd  38
PieceIdentification    Shift  17
FileName               Shift  18
DOT                    Shift  20

BlackDots              Goto   19
InnerSANMove           Goto   16
WhiteMove              Goto   15
BlackMove              Goto   14


STATE 11 -----------------------------------------------------------------------
( 8 35 )

move-number-indication ::= IntegerLiteral  .DOT space 

DOT    Shift  21


STATE 12 -----------------------------------------------------------------------
( 8 35 )

recursive-variation ::= START_VARIATION  .space element-sequence END_VARIATION 
                        space 

space ::= .WHITESPACE 
space ::= . (51)

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 20



WHITESPACE         Sh/Rd  50

space              Goto   22

IntegerLiteral     Reduce 51
START_VARIATION    Reduce 51
END_VARIATION      Reduce 51

Default reduction to rule  51


STATE 13 -----------------------------------------------------------------------
( 9 )

tag-pair ::= Start_Tag_Section space  .tag-value space END_TAG_SECTION 

StringLiteral    Shift  23

tag-value        Goto   23


STATE 14 -----------------------------------------------------------------------
( 10 )

element ::= move-number-indication BlackMove  .space 

space ::= .WHITESPACE 
space ::= . (51)

WHITESPACE         Sh/Rd  50

space              Gt/Rd  15

IntegerLiteral     Reduce 51
START_VARIATION    Reduce 51
END_VARIATION      Reduce 51
WHITE_VICTORY      Reduce 51
BLACK_VICTORY      Reduce 51
DRAW               Reduce 51
UNKNOWN            Reduce 51

Default reduction to rule  51


STATE 15 -----------------------------------------------------------------------
( 10 )

element ::= move-number-indication WhiteMove  .space 
element ::= move-number-indication WhiteMove  .WHITESPACE 
            BlackMoveFollowingWhiteMove space 

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 21



space ::= .WHITESPACE 
space ::= . (51)

WHITESPACE         Shift  24

space              Gt/Rd  13

IntegerLiteral     Reduce 51
START_VARIATION    Reduce 51
END_VARIATION      Reduce 51
WHITE_VICTORY      Reduce 51
BLACK_VICTORY      Reduce 51
DRAW               Reduce 51
UNKNOWN            Reduce 51

Default reduction to rule  51


STATE 16 -----------------------------------------------------------------------
( 10 24 )

WhiteMove ::= InnerSANMove  .numeric-annotation-glyph 
WhiteMove ::= InnerSANMove  .CHECK numeric-annotation-glyph 
WhiteMove ::= InnerSANMove  .CHECKMATE numeric-annotation-glyph 

numeric-annotation-glyph ::= .numeric-annotation-glyph Start_nag 
numeric-annotation-glyph ::= .EXCELLENT_MOVE 
numeric-annotation-glyph ::= .VERY_BAD_MOVE 
numeric-annotation-glyph ::= .BAD_MOVE 
numeric-annotation-glyph ::= .GOOD_MOVE 
numeric-annotation-glyph ::= .INTERESTING_MOVE 
numeric-annotation-glyph ::= .SUSPICIOUS_MOVE 
numeric-annotation-glyph ::= . (47)

CHECKMATE                   Shift  25
CHECK                       Shift  26
SUSPICIOUS_MOVE             Sh/Rd  46
INTERESTING_MOVE            Sh/Rd  45
GOOD_MOVE                   Sh/Rd  44
BAD_MOVE                    Sh/Rd  43
VERY_BAD_MOVE               Sh/Rd  42
EXCELLENT_MOVE              Sh/Rd  41

numeric-annotation-glyph    Goto   27

IntegerLiteral              Reduce 47
Start_nag                   Reduce 47
START_VARIATION             Reduce 47
END_VARIATION               Reduce 47
WHITE_VICTORY               Reduce 47

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 22


BLACK_VICTORY               Reduce 47
DRAW                        Reduce 47
UNKNOWN                     Reduce 47
WHITESPACE                  Reduce 47

Default reduction to rule  47


STATE 17 -----------------------------------------------------------------------
( 10 19 24 )

InnerSANMove ::= PieceIdentification  .FileName RankName 
InnerSANMove ::= PieceIdentification  .FileName FileName RankName 
InnerSANMove ::= PieceIdentification  .RankName FileName RankName 
InnerSANMove ::= PieceIdentification  .CAPTURE FileName RankName 
InnerSANMove ::= PieceIdentification  .FileName CAPTURE FileName RankName 
InnerSANMove ::= PieceIdentification  .RankName CAPTURE FileName RankName 
InnerSANMove ::= PieceIdentification  .FileName RankName CAPTURE FileName 
                 RankName 

CAPTURE     Shift  28
RankName    Shift  29
FileName    Shift  30


STATE 18 -----------------------------------------------------------------------
( 10 19 24 )

InnerSANMove ::= FileName  .RankName 
InnerSANMove ::= FileName  .RankName PROMOTE PieceIdentification 
InnerSANMove ::= FileName  .RankName PieceIdentification 
InnerSANMove ::= FileName  .CAPTURE FileName RankName 
InnerSANMove ::= FileName  .CAPTURE FileName RankName EN_PASSANT 
InnerSANMove ::= FileName  .CAPTURE FileName RankName PieceIdentification 
InnerSANMove ::= FileName  .CAPTURE FileName RankName PROMOTE 
                 PieceIdentification 

CAPTURE     Shift  31
RankName    Shift  32


STATE 19 -----------------------------------------------------------------------
( 10 )

BlackMove ::= BlackDots  .InnerSANMove numeric-annotation-glyph 
BlackMove ::= BlackDots  .InnerSANMove CHECK numeric-annotation-glyph 
BlackMove ::= BlackDots  .InnerSANMove CHECKMATE numeric-annotation-glyph 

InnerSANMove ::= .FileName RankName 
InnerSANMove ::= .FileName RankName PROMOTE PieceIdentification 
InnerSANMove ::= .FileName RankName PieceIdentification 

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 23


InnerSANMove ::= .FileName CAPTURE FileName RankName 
InnerSANMove ::= .FileName CAPTURE FileName RankName EN_PASSANT 
InnerSANMove ::= .FileName CAPTURE FileName RankName PieceIdentification 
InnerSANMove ::= .FileName CAPTURE FileName RankName PROMOTE 
                 PieceIdentification 
InnerSANMove ::= .PieceIdentification FileName RankName 
InnerSANMove ::= .PieceIdentification FileName FileName RankName 
InnerSANMove ::= .PieceIdentification RankName FileName RankName 
InnerSANMove ::= .PieceIdentification CAPTURE FileName RankName 
InnerSANMove ::= .PieceIdentification FileName CAPTURE FileName RankName 
InnerSANMove ::= .PieceIdentification RankName CAPTURE FileName RankName 
InnerSANMove ::= .PieceIdentification FileName RankName CAPTURE FileName 
                 RankName 
InnerSANMove ::= .CASTLE_KING_SIDE 
InnerSANMove ::= .CASTLE_QUEEN_SIDE 

CASTLE_QUEEN_SIDE      Sh/Rd  39
CASTLE_KING_SIDE       Sh/Rd  38
PieceIdentification    Shift  17
FileName               Shift  18

InnerSANMove           Goto   33


STATE 20 -----------------------------------------------------------------------
( 10 )

BlackDots ::= DOT  .DOT space 

DOT    Shift  34


STATE 21 -----------------------------------------------------------------------
( 11 )

move-number-indication ::= IntegerLiteral DOT  .space 

space ::= .WHITESPACE 
space ::= . (51)

WHITESPACE             Sh/Rd  50

space                  Gt/Rd  48

FileName               Reduce 51
PieceIdentification    Reduce 51
DOT                    Reduce 51
CASTLE_KING_SIDE       Reduce 51
CASTLE_QUEEN_SIDE      Reduce 51

Default reduction to rule  51

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 24




STATE 22 -----------------------------------------------------------------------
( 12 )

recursive-variation ::= START_VARIATION space  .element-sequence END_VARIATION 
                        space 

element-sequence ::= .element-sequence element 
element-sequence ::= .element-sequence recursive-variation 
element-sequence ::= . (12)

element-sequence    Goto   35

IntegerLiteral      Reduce 12
START_VARIATION     Reduce 12
END_VARIATION       Reduce 12

Default reduction to rule  12


STATE 23 -----------------------------------------------------------------------
( 13 )

tag-pair ::= Start_Tag_Section space tag-value  .space END_TAG_SECTION 

space ::= .WHITESPACE 
space ::= . (51)

WHITESPACE         Sh/Rd  50

space              Goto   36

END_TAG_SECTION    Reduce 51

Default reduction to rule  51


STATE 24 -----------------------------------------------------------------------
( 15 )

element ::= move-number-indication WhiteMove WHITESPACE  .
            BlackMoveFollowingWhiteMove space 
space ::= WHITESPACE . (50)

WhiteMove ::= .InnerSANMove numeric-annotation-glyph 
WhiteMove ::= .InnerSANMove CHECK numeric-annotation-glyph 
WhiteMove ::= .InnerSANMove CHECKMATE numeric-annotation-glyph 
BlackMoveFollowingWhiteMove ::= .WhiteMove 
InnerSANMove ::= .FileName RankName 
InnerSANMove ::= .FileName RankName PROMOTE PieceIdentification 

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 25


InnerSANMove ::= .FileName RankName PieceIdentification 
InnerSANMove ::= .FileName CAPTURE FileName RankName 
InnerSANMove ::= .FileName CAPTURE FileName RankName EN_PASSANT 
InnerSANMove ::= .FileName CAPTURE FileName RankName PieceIdentification 
InnerSANMove ::= .FileName CAPTURE FileName RankName PROMOTE 
                 PieceIdentification 
InnerSANMove ::= .PieceIdentification FileName RankName 
InnerSANMove ::= .PieceIdentification FileName FileName RankName 
InnerSANMove ::= .PieceIdentification RankName FileName RankName 
InnerSANMove ::= .PieceIdentification CAPTURE FileName RankName 
InnerSANMove ::= .PieceIdentification FileName CAPTURE FileName RankName 
InnerSANMove ::= .PieceIdentification RankName CAPTURE FileName RankName 
InnerSANMove ::= .PieceIdentification FileName RankName CAPTURE FileName 
                 RankName 
InnerSANMove ::= .CASTLE_KING_SIDE 
InnerSANMove ::= .CASTLE_QUEEN_SIDE 

CASTLE_QUEEN_SIDE              Sh/Rd  39
CASTLE_KING_SIDE               Sh/Rd  38
PieceIdentification            Shift  17
FileName                       Shift  18

InnerSANMove                   Goto   16
WhiteMove                      Gt/Rd  23
BlackMoveFollowingWhiteMove    Goto   37

IntegerLiteral                 Reduce 50
START_VARIATION                Reduce 50
END_VARIATION                  Reduce 50
WHITE_VICTORY                  Reduce 50
BLACK_VICTORY                  Reduce 50
DRAW                           Reduce 50
UNKNOWN                        Reduce 50

Default reduction to rule  50


STATE 25 -----------------------------------------------------------------------
( 16 )

WhiteMove ::= InnerSANMove CHECKMATE  .numeric-annotation-glyph 

numeric-annotation-glyph ::= .numeric-annotation-glyph Start_nag 
numeric-annotation-glyph ::= .EXCELLENT_MOVE 
numeric-annotation-glyph ::= .VERY_BAD_MOVE 
numeric-annotation-glyph ::= .BAD_MOVE 
numeric-annotation-glyph ::= .GOOD_MOVE 
numeric-annotation-glyph ::= .INTERESTING_MOVE 
numeric-annotation-glyph ::= .SUSPICIOUS_MOVE 
numeric-annotation-glyph ::= . (47)


 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 26


SUSPICIOUS_MOVE             Sh/Rd  46
INTERESTING_MOVE            Sh/Rd  45
GOOD_MOVE                   Sh/Rd  44
BAD_MOVE                    Sh/Rd  43
VERY_BAD_MOVE               Sh/Rd  42
EXCELLENT_MOVE              Sh/Rd  41

numeric-annotation-glyph    Goto   38

IntegerLiteral              Reduce 47
Start_nag                   Reduce 47
START_VARIATION             Reduce 47
END_VARIATION               Reduce 47
WHITE_VICTORY               Reduce 47
BLACK_VICTORY               Reduce 47
DRAW                        Reduce 47
UNKNOWN                     Reduce 47
WHITESPACE                  Reduce 47

Default reduction to rule  47


STATE 26 -----------------------------------------------------------------------
( 16 )

WhiteMove ::= InnerSANMove CHECK  .numeric-annotation-glyph 

numeric-annotation-glyph ::= .numeric-annotation-glyph Start_nag 
numeric-annotation-glyph ::= .EXCELLENT_MOVE 
numeric-annotation-glyph ::= .VERY_BAD_MOVE 
numeric-annotation-glyph ::= .BAD_MOVE 
numeric-annotation-glyph ::= .GOOD_MOVE 
numeric-annotation-glyph ::= .INTERESTING_MOVE 
numeric-annotation-glyph ::= .SUSPICIOUS_MOVE 
numeric-annotation-glyph ::= . (47)

SUSPICIOUS_MOVE             Sh/Rd  46
INTERESTING_MOVE            Sh/Rd  45
GOOD_MOVE                   Sh/Rd  44
BAD_MOVE                    Sh/Rd  43
VERY_BAD_MOVE               Sh/Rd  42
EXCELLENT_MOVE              Sh/Rd  41

numeric-annotation-glyph    Goto   39

IntegerLiteral              Reduce 47
Start_nag                   Reduce 47
START_VARIATION             Reduce 47
END_VARIATION               Reduce 47
WHITE_VICTORY               Reduce 47
BLACK_VICTORY               Reduce 47

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 27


DRAW                        Reduce 47
UNKNOWN                     Reduce 47
WHITESPACE                  Reduce 47

Default reduction to rule  47


STATE 27 -----------------------------------------------------------------------
( 16 )

WhiteMove ::= InnerSANMove numeric-annotation-glyph . (16)
numeric-annotation-glyph ::= numeric-annotation-glyph  .Start_nag 

Start_nag          Sh/Rd  40

IntegerLiteral     Reduce 16
START_VARIATION    Reduce 16
END_VARIATION      Reduce 16
WHITE_VICTORY      Reduce 16
BLACK_VICTORY      Reduce 16
DRAW               Reduce 16
UNKNOWN            Reduce 16
WHITESPACE         Reduce 16

Default reduction to rule  16


STATE 28 -----------------------------------------------------------------------
( 17 )

InnerSANMove ::= PieceIdentification CAPTURE  .FileName RankName 

FileName    Shift  40


STATE 29 -----------------------------------------------------------------------
( 17 )

InnerSANMove ::= PieceIdentification RankName  .FileName RankName 
InnerSANMove ::= PieceIdentification RankName  .CAPTURE FileName RankName 

CAPTURE     Shift  41
FileName    Shift  42


STATE 30 -----------------------------------------------------------------------
( 17 )

InnerSANMove ::= PieceIdentification FileName  .RankName 
InnerSANMove ::= PieceIdentification FileName  .FileName RankName 
InnerSANMove ::= PieceIdentification FileName  .CAPTURE FileName RankName 

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 28


InnerSANMove ::= PieceIdentification FileName  .RankName CAPTURE FileName 
                 RankName 

CAPTURE     Shift  43
FileName    Shift  44
RankName    Shift  45


STATE 31 -----------------------------------------------------------------------
( 18 )

InnerSANMove ::= FileName CAPTURE  .FileName RankName 
InnerSANMove ::= FileName CAPTURE  .FileName RankName EN_PASSANT 
InnerSANMove ::= FileName CAPTURE  .FileName RankName PieceIdentification 
InnerSANMove ::= FileName CAPTURE  .FileName RankName PROMOTE 
                 PieceIdentification 

FileName    Shift  46


STATE 32 -----------------------------------------------------------------------
( 18 )

InnerSANMove ::= FileName RankName . (24)
InnerSANMove ::= FileName RankName  .PROMOTE PieceIdentification 
InnerSANMove ::= FileName RankName  .PieceIdentification 

PieceIdentification    Sh/Rd  26
PROMOTE                Shift  47

IntegerLiteral         Reduce 24
Start_nag              Reduce 24
START_VARIATION        Reduce 24
END_VARIATION          Reduce 24
WHITE_VICTORY          Reduce 24
BLACK_VICTORY          Reduce 24
DRAW                   Reduce 24
UNKNOWN                Reduce 24
CHECK                  Reduce 24
CHECKMATE              Reduce 24
EXCELLENT_MOVE         Reduce 24
VERY_BAD_MOVE          Reduce 24
BAD_MOVE               Reduce 24
GOOD_MOVE              Reduce 24
INTERESTING_MOVE       Reduce 24
SUSPICIOUS_MOVE        Reduce 24
WHITESPACE             Reduce 24

Default reduction to rule  24


STATE 33 -----------------------------------------------------------------------
( 19 )


 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 29


BlackMove ::= BlackDots InnerSANMove  .numeric-annotation-glyph 
BlackMove ::= BlackDots InnerSANMove  .CHECK numeric-annotation-glyph 
BlackMove ::= BlackDots InnerSANMove  .CHECKMATE numeric-annotation-glyph 

numeric-annotation-glyph ::= .numeric-annotation-glyph Start_nag 
numeric-annotation-glyph ::= .EXCELLENT_MOVE 
numeric-annotation-glyph ::= .VERY_BAD_MOVE 
numeric-annotation-glyph ::= .BAD_MOVE 
numeric-annotation-glyph ::= .GOOD_MOVE 
numeric-annotation-glyph ::= .INTERESTING_MOVE 
numeric-annotation-glyph ::= .SUSPICIOUS_MOVE 
numeric-annotation-glyph ::= . (47)

CHECKMATE                   Shift  48
CHECK                       Shift  49
SUSPICIOUS_MOVE             Sh/Rd  46
INTERESTING_MOVE            Sh/Rd  45
GOOD_MOVE                   Sh/Rd  44
BAD_MOVE                    Sh/Rd  43
VERY_BAD_MOVE               Sh/Rd  42
EXCELLENT_MOVE              Sh/Rd  41

numeric-annotation-glyph    Goto   50

IntegerLiteral              Reduce 47
Start_nag                   Reduce 47
START_VARIATION             Reduce 47
END_VARIATION               Reduce 47
WHITE_VICTORY               Reduce 47
BLACK_VICTORY               Reduce 47
DRAW                        Reduce 47
UNKNOWN                     Reduce 47
WHITESPACE                  Reduce 47

Default reduction to rule  47


STATE 34 -----------------------------------------------------------------------
( 20 )

BlackDots ::= DOT DOT  .space 

space ::= .WHITESPACE 
space ::= . (51)

WHITESPACE             Sh/Rd  50

space                  Gt/Rd  22

FileName               Reduce 51
PieceIdentification    Reduce 51

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 30


CASTLE_KING_SIDE       Reduce 51
CASTLE_QUEEN_SIDE      Reduce 51

Default reduction to rule  51


STATE 35 -----------------------------------------------------------------------
( 22 )

element-sequence ::= element-sequence  .element 
element-sequence ::= element-sequence  .recursive-variation 
recursive-variation ::= START_VARIATION space element-sequence  .END_VARIATION 
                        space 

element ::= .move-number-indication WhiteMove space 
element ::= .move-number-indication WhiteMove WHITESPACE 
            BlackMoveFollowingWhiteMove space 
element ::= .move-number-indication BlackMove space 
move-number-indication ::= .IntegerLiteral DOT space 
recursive-variation ::= .START_VARIATION space element-sequence END_VARIATION 
                        space 

END_VARIATION             Shift  51
IntegerLiteral            Shift  11
START_VARIATION           Shift  12

move-number-indication    Goto   10
element                   Gt/Rd  10
recursive-variation       Gt/Rd  11


STATE 36 -----------------------------------------------------------------------
( 23 )

tag-pair ::= Start_Tag_Section space tag-value space  .END_TAG_SECTION 

END_TAG_SECTION    Sh/Rd  7


STATE 37 -----------------------------------------------------------------------
( 24 )

element ::= move-number-indication WhiteMove WHITESPACE 
            BlackMoveFollowingWhiteMove  .space 

space ::= .WHITESPACE 
space ::= . (51)

WHITESPACE         Sh/Rd  50

space              Gt/Rd  14

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 31



IntegerLiteral     Reduce 51
START_VARIATION    Reduce 51
END_VARIATION      Reduce 51
WHITE_VICTORY      Reduce 51
BLACK_VICTORY      Reduce 51
DRAW               Reduce 51
UNKNOWN            Reduce 51

Default reduction to rule  51


STATE 38 -----------------------------------------------------------------------
( 25 )

WhiteMove ::= InnerSANMove CHECKMATE numeric-annotation-glyph . (18)
numeric-annotation-glyph ::= numeric-annotation-glyph  .Start_nag 

Start_nag          Sh/Rd  40

IntegerLiteral     Reduce 18
START_VARIATION    Reduce 18
END_VARIATION      Reduce 18
WHITE_VICTORY      Reduce 18
BLACK_VICTORY      Reduce 18
DRAW               Reduce 18
UNKNOWN            Reduce 18
WHITESPACE         Reduce 18

Default reduction to rule  18


STATE 39 -----------------------------------------------------------------------
( 26 )

WhiteMove ::= InnerSANMove CHECK numeric-annotation-glyph . (17)
numeric-annotation-glyph ::= numeric-annotation-glyph  .Start_nag 

Start_nag          Sh/Rd  40

IntegerLiteral     Reduce 17
START_VARIATION    Reduce 17
END_VARIATION      Reduce 17
WHITE_VICTORY      Reduce 17
BLACK_VICTORY      Reduce 17
DRAW               Reduce 17
UNKNOWN            Reduce 17
WHITESPACE         Reduce 17

Default reduction to rule  17


STATE 40 -----------------------------------------------------------------------
( 28 )


 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 32


InnerSANMove ::= PieceIdentification CAPTURE FileName  .RankName 

RankName    Sh/Rd  34


STATE 41 -----------------------------------------------------------------------
( 29 )

InnerSANMove ::= PieceIdentification RankName CAPTURE  .FileName RankName 

FileName    Shift  52


STATE 42 -----------------------------------------------------------------------
( 29 )

InnerSANMove ::= PieceIdentification RankName FileName  .RankName 

RankName    Sh/Rd  33


STATE 43 -----------------------------------------------------------------------
( 30 )

InnerSANMove ::= PieceIdentification FileName CAPTURE  .FileName RankName 

FileName    Shift  53


STATE 44 -----------------------------------------------------------------------
( 30 )

InnerSANMove ::= PieceIdentification FileName FileName  .RankName 

RankName    Sh/Rd  32


STATE 45 -----------------------------------------------------------------------
( 30 )

InnerSANMove ::= PieceIdentification FileName RankName . (31)
InnerSANMove ::= PieceIdentification FileName RankName  .CAPTURE FileName 
                 RankName 

CAPTURE             Shift  54

IntegerLiteral      Reduce 31
Start_nag           Reduce 31
START_VARIATION     Reduce 31
END_VARIATION       Reduce 31
WHITE_VICTORY       Reduce 31

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 33


BLACK_VICTORY       Reduce 31
DRAW                Reduce 31
UNKNOWN             Reduce 31
CHECK               Reduce 31
CHECKMATE           Reduce 31
EXCELLENT_MOVE      Reduce 31
VERY_BAD_MOVE       Reduce 31
BAD_MOVE            Reduce 31
GOOD_MOVE           Reduce 31
INTERESTING_MOVE    Reduce 31
SUSPICIOUS_MOVE     Reduce 31
WHITESPACE          Reduce 31

Default reduction to rule  31


STATE 46 -----------------------------------------------------------------------
( 31 )

InnerSANMove ::= FileName CAPTURE FileName  .RankName 
InnerSANMove ::= FileName CAPTURE FileName  .RankName EN_PASSANT 
InnerSANMove ::= FileName CAPTURE FileName  .RankName PieceIdentification 
InnerSANMove ::= FileName CAPTURE FileName  .RankName PROMOTE 
                 PieceIdentification 

RankName    Shift  55


STATE 47 -----------------------------------------------------------------------
( 32 )

InnerSANMove ::= FileName RankName PROMOTE  .PieceIdentification 

PieceIdentification    Sh/Rd  25


STATE 48 -----------------------------------------------------------------------
( 33 )

BlackMove ::= BlackDots InnerSANMove CHECKMATE  .numeric-annotation-glyph 

numeric-annotation-glyph ::= .numeric-annotation-glyph Start_nag 
numeric-annotation-glyph ::= .EXCELLENT_MOVE 
numeric-annotation-glyph ::= .VERY_BAD_MOVE 
numeric-annotation-glyph ::= .BAD_MOVE 
numeric-annotation-glyph ::= .GOOD_MOVE 
numeric-annotation-glyph ::= .INTERESTING_MOVE 
numeric-annotation-glyph ::= .SUSPICIOUS_MOVE 
numeric-annotation-glyph ::= . (47)

SUSPICIOUS_MOVE             Sh/Rd  46

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 34


INTERESTING_MOVE            Sh/Rd  45
GOOD_MOVE                   Sh/Rd  44
BAD_MOVE                    Sh/Rd  43
VERY_BAD_MOVE               Sh/Rd  42
EXCELLENT_MOVE              Sh/Rd  41

numeric-annotation-glyph    Goto   56

IntegerLiteral              Reduce 47
Start_nag                   Reduce 47
START_VARIATION             Reduce 47
END_VARIATION               Reduce 47
WHITE_VICTORY               Reduce 47
BLACK_VICTORY               Reduce 47
DRAW                        Reduce 47
UNKNOWN                     Reduce 47
WHITESPACE                  Reduce 47

Default reduction to rule  47


STATE 49 -----------------------------------------------------------------------
( 33 )

BlackMove ::= BlackDots InnerSANMove CHECK  .numeric-annotation-glyph 

numeric-annotation-glyph ::= .numeric-annotation-glyph Start_nag 
numeric-annotation-glyph ::= .EXCELLENT_MOVE 
numeric-annotation-glyph ::= .VERY_BAD_MOVE 
numeric-annotation-glyph ::= .BAD_MOVE 
numeric-annotation-glyph ::= .GOOD_MOVE 
numeric-annotation-glyph ::= .INTERESTING_MOVE 
numeric-annotation-glyph ::= .SUSPICIOUS_MOVE 
numeric-annotation-glyph ::= . (47)

SUSPICIOUS_MOVE             Sh/Rd  46
INTERESTING_MOVE            Sh/Rd  45
GOOD_MOVE                   Sh/Rd  44
BAD_MOVE                    Sh/Rd  43
VERY_BAD_MOVE               Sh/Rd  42
EXCELLENT_MOVE              Sh/Rd  41

numeric-annotation-glyph    Goto   57

IntegerLiteral              Reduce 47
Start_nag                   Reduce 47
START_VARIATION             Reduce 47
END_VARIATION               Reduce 47
WHITE_VICTORY               Reduce 47
BLACK_VICTORY               Reduce 47
DRAW                        Reduce 47

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 35


UNKNOWN                     Reduce 47
WHITESPACE                  Reduce 47

Default reduction to rule  47


STATE 50 -----------------------------------------------------------------------
( 33 )

BlackMove ::= BlackDots InnerSANMove numeric-annotation-glyph . (19)
numeric-annotation-glyph ::= numeric-annotation-glyph  .Start_nag 

Start_nag          Sh/Rd  40

IntegerLiteral     Reduce 19
START_VARIATION    Reduce 19
END_VARIATION      Reduce 19
WHITE_VICTORY      Reduce 19
BLACK_VICTORY      Reduce 19
DRAW               Reduce 19
UNKNOWN            Reduce 19
WHITESPACE         Reduce 19

Default reduction to rule  19


STATE 51 -----------------------------------------------------------------------
( 35 )

recursive-variation ::= START_VARIATION space element-sequence END_VARIATION  .
                        space 

space ::= .WHITESPACE 
space ::= . (51)

WHITESPACE         Sh/Rd  50

space              Gt/Rd  49

IntegerLiteral     Reduce 51
START_VARIATION    Reduce 51
END_VARIATION      Reduce 51
WHITE_VICTORY      Reduce 51
BLACK_VICTORY      Reduce 51
DRAW               Reduce 51
UNKNOWN            Reduce 51

Default reduction to rule  51


STATE 52 -----------------------------------------------------------------------
( 41 )


 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 36


InnerSANMove ::= PieceIdentification RankName CAPTURE FileName  .RankName 

RankName    Sh/Rd  36


STATE 53 -----------------------------------------------------------------------
( 43 )

InnerSANMove ::= PieceIdentification FileName CAPTURE FileName  .RankName 

RankName    Sh/Rd  35


STATE 54 -----------------------------------------------------------------------
( 45 )

InnerSANMove ::= PieceIdentification FileName RankName CAPTURE  .FileName 
                 RankName 

FileName    Shift  58


STATE 55 -----------------------------------------------------------------------
( 46 )

InnerSANMove ::= FileName CAPTURE FileName RankName . (27)
InnerSANMove ::= FileName CAPTURE FileName RankName  .EN_PASSANT 
InnerSANMove ::= FileName CAPTURE FileName RankName  .PieceIdentification 
InnerSANMove ::= FileName CAPTURE FileName RankName  .PROMOTE 
                 PieceIdentification 

PROMOTE                Shift  59
PieceIdentification    Sh/Rd  29
EN_PASSANT             Sh/Rd  28

IntegerLiteral         Reduce 27
Start_nag              Reduce 27
START_VARIATION        Reduce 27
END_VARIATION          Reduce 27
WHITE_VICTORY          Reduce 27
BLACK_VICTORY          Reduce 27
DRAW                   Reduce 27
UNKNOWN                Reduce 27
CHECK                  Reduce 27
CHECKMATE              Reduce 27
EXCELLENT_MOVE         Reduce 27
VERY_BAD_MOVE          Reduce 27
BAD_MOVE               Reduce 27
GOOD_MOVE              Reduce 27
INTERESTING_MOVE       Reduce 27
SUSPICIOUS_MOVE        Reduce 27

 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 37


WHITESPACE             Reduce 27

Default reduction to rule  27


STATE 56 -----------------------------------------------------------------------
( 48 )

BlackMove ::= BlackDots InnerSANMove CHECKMATE numeric-annotation-glyph . (21)
numeric-annotation-glyph ::= numeric-annotation-glyph  .Start_nag 

Start_nag          Sh/Rd  40

IntegerLiteral     Reduce 21
START_VARIATION    Reduce 21
END_VARIATION      Reduce 21
WHITE_VICTORY      Reduce 21
BLACK_VICTORY      Reduce 21
DRAW               Reduce 21
UNKNOWN            Reduce 21
WHITESPACE         Reduce 21

Default reduction to rule  21


STATE 57 -----------------------------------------------------------------------
( 49 )

BlackMove ::= BlackDots InnerSANMove CHECK numeric-annotation-glyph . (20)
numeric-annotation-glyph ::= numeric-annotation-glyph  .Start_nag 

Start_nag          Sh/Rd  40

IntegerLiteral     Reduce 20
START_VARIATION    Reduce 20
END_VARIATION      Reduce 20
WHITE_VICTORY      Reduce 20
BLACK_VICTORY      Reduce 20
DRAW               Reduce 20
UNKNOWN            Reduce 20
WHITESPACE         Reduce 20

Default reduction to rule  20


STATE 58 -----------------------------------------------------------------------
( 54 )

InnerSANMove ::= PieceIdentification FileName RankName CAPTURE FileName  .
                 RankName 


 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 38


RankName    Sh/Rd  37


STATE 59 -----------------------------------------------------------------------
( 55 )

InnerSANMove ::= FileName CAPTURE FileName RankName PROMOTE  .
                 PieceIdentification 

PieceIdentification    Sh/Rd  30


 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 39


Number of Reductions saved by default: 241
Length of Check table: 276
Length of Action table: 229
Number of entries in Action Table: 217
Percentage of increase: 5.5%
Highest symbol in Check Table: 51
Storage Required for Tables: 734 Bytes, 1K
Storage Required for Rules: 110 Bytes


Actions in Compressed Tables:
     Number of Shifts: 41
     Number of Shift/Reduces: 74
     Number of Gotos: 26
     Number of Goto/Reduces: 15
     Number of Reduces: 2
     Number of Defaults: 31

Error maps storage:
    Storage required for ACTION_SYMBOLS_BASE map: 118 Bytes
    Storage required for ACTION_SYMBOLS_RANGE map: 99 Bytes
    Storage required for NACTION_SYMBOLS_BASE map: 118 Bytes
    Storage required for NACTION_SYMBOLS_RANGE map: 52 Bytes
    Storage required for SYMBOL_INDEX map: 52 Bytes
    Storage required for STRING_BUFFER map: 680 Bytes

***Warning: Base Check vector contains value > 127. 16-bit words used.
Escaped symbol $eof is an invalid C variable.

Escaped symbol $error is an invalid C variable.



 IBM Research Jikes Parser Generator    0.03 Mon Oct 03 22:54:19 2005       Page 40


Mapping of new state numbers into original numbers:

   56  ==>>      8
   57  ==>>     16
   58  ==>>     17
   61  ==>>      3
   63  ==>>      5
   71  ==>>      6
   77  ==>>      4
   80  ==>>     10
   86  ==>>     29
   99  ==>>     22
  104  ==>>     33
  105  ==>>     24
  118  ==>>     11
  127  ==>>     55
  128  ==>>     25
  142  ==>>      7
  146  ==>>      9
  147  ==>>     26
  151  ==>>      1
  161  ==>>     48
  162  ==>>     19
  164  ==>>     28
  175  ==>>     49
  176  ==>>     35
  177  ==>>     20
  181  ==>>     13
  193  ==>>     30
  198  ==>>     12
  205  ==>>     18
  206  ==>>      2
  210  ==>>     14
  214  ==>>     15
  215  ==>>     36
  218  ==>>     21
  222  ==>>     23
  223  ==>>     32
  229  ==>>     34
  233  ==>>     37
  237  ==>>     51
  238  ==>>     27
  241  ==>>     31
  243  ==>>     38
  245  ==>>     39
  247  ==>>     45
  250  ==>>     40
  252  ==>>     41
  255  ==>>     42
  256  ==>>     43
  258  ==>>     44
  260  ==>>     46
  263  ==>>     47
  265  ==>>     50
  266  ==>>     52
  269  ==>>     53
  272  ==>>     56
  274  ==>>     54
  276  ==>>     57
  277  ==>>     58
  279  ==>>     59
