package org.formulachess.pgn;

/**
 * An interface that contains static declarations for some basic information
 * about the parser such as the number of rules in the grammar, the starting
 * state, etc...
 */
public interface ParserBasicInformation {

	public static final int ERROR_SYMBOL = 51;
	public static final int MAX_NAME_LENGTH = 27;
	public static final int NUM_STATES = 59;
	public static final int NT_OFFSET = 52;
	public static final int SCOPE_UBOUND = -1;
	public static final int SCOPE_SIZE = 0;
	public static final int LA_STATE_OFFSET = 281;
	public static final int MAX_LA = 1;
	public static final int NUM_RULES = 55;
	public static final int NUM_TERMINALS = 32;
	public static final int NUM_NON_TERMINALS = 20;
	public static final int NUM_SYMBOLS = 52;
	public static final int START_STATE = 151;
	public static final int EOFT_SYMBOL = 24;
	public static final int EOLT_SYMBOL = 24;
	public static final int ACCEPT_ACTION = 280;
	public static final int ERROR_ACTION = 281;
}
