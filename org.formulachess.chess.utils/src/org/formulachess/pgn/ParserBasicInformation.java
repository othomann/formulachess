package org.formulachess.pgn;

/** An interface that contains static declarations for some basic information
 * about the parser such as the number of rules in the grammar, the starting state, etc...
 */
public interface ParserBasicInformation {

    public final static int
	    ERROR_SYMBOL      = 51,
	    MAX_NAME_LENGTH   = 27,
	    NUM_STATES        = 59,
	    NT_OFFSET         = 52,
	    SCOPE_UBOUND      = -1,
	    SCOPE_SIZE        = 0,
	    LA_STATE_OFFSET   = 281,
	    MAX_LA            = 1,
	    NUM_RULES         = 55,
	    NUM_TERMINALS     = 32,
	    NUM_NON_TERMINALS = 20,
	    NUM_SYMBOLS       = 52,
	    START_STATE       = 151,
	    EOFT_SYMBOL       = 24,
	    EOLT_SYMBOL       = 24,
	    ACCEPT_ACTION     = 280,
	    ERROR_ACTION      = 281;
}
