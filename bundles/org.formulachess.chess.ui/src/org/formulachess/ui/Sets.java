package org.formulachess.ui;

public class Sets {
	public static final int BOARD_WIDTH_ORIGIN_INDEX = 0;
	public static final int BOARD_HEIGHT_ORIGIN_INDEX = 1;
	public static final int BOARD_WIDTH_INDEX = 2;
	public static final int BOARD_HEIGHT_INDEX = 3;
	public static final int SQUARE_WIDTH_INDEX = 4;
	public static final int SQUARE_HEIGHT_INDEX = 5;
	public static final int BOARD_WIDTH_DELTA_INDEX = 6;
	public static final int BOARD_HEIGHT_DELTA_INDEX = 7;
	public static final int SWITCH_BUTTON_WIDTH_SIZE_INDEX = 8;
	public static final int SWITCH_BUTTON_HEIGHT_SIZE_INDEX = 9;
	public static final int FONT_SIZE_INDEX = 10;
	public static final int FONT_WIDTH_DELTA_INDEX = 11;
	public static final int FONT_HEIGHT_DELTA_INDEX = 12;

	public static final String SET2_PATH = "set2"; //$NON-NLS-1$
	public static final String SET3_PATH = "set3"; //$NON-NLS-1$
	
	private static final int[] SET2 = {
			0,   // BOARD_WIDTH_ORIGIN_INDEX
			0,   // BOARD_HEIGHT_ORIGIN_INDEX
			232, // BOARD_WIDTH_INDEX
			232, // BOARD_HEIGHT_INDEX
			29,  // SQUARE_WIDTH_INDEX
			29,  // SQUARE_HEIGHT_INDEX
			0,   // BOARD_WIDTH_DELTA_INDEX
			0,   // BOARD_HEIGHT_DELTA_INDEX
			30,  // SWITCH_BUTTON_WIDTH_SIZE_INDEX
			30,  // SWITCH_BUTTON_HEIGHT_SIZE_INDEX
			18,   // FONT_SIZE_INDEX,
			10,   // FONT_WIDTH_DELTA_INDEX
			5     // FONT_HEIGHT_DELTA_INDEX
	};
	
	private static final int[] SET3 = {
			0,   // BOARD_WIDTH_ORIGIN_INDEX
			0,   // BOARD_HEIGHT_ORIGIN_INDEX
			384, // BOARD_WIDTH_INDEX
			384, // BOARD_HEIGHT_INDEX
			48,  // SQUARE_WIDTH_INDEX
			48,  // SQUARE_HEIGHT_INDEX
			0,   // BOARD_WIDTH_DELTA_INDEX
			0,   // BOARD_HEIGHT_DELTA_INDEX
			30,  // SWITCH_BUTTON_WIDTH_SIZE_INDEX
			30,  // SWITCH_BUTTON_HEIGHT_SIZE_INDEX
			18,   // FONT_SIZE_INDEX,
			10,   // FONT_WIDTH_DELTA_INDEX
			5     // FONT_HEIGHT_DELTA_INDEX
	};
	
	public static int[] getSet(String setName) {
		switch(setName) {
			case SET2_PATH :
				return SET2;
			case SET3_PATH :
				return SET3;
			default:
				return null;
		}
	}
}
