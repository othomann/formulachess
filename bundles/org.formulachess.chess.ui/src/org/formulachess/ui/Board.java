package org.formulachess.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.formulachess.engine.ChessEngine;

public class Board extends Composite {

	static class SwitchMouseAdapter extends MouseAdapter {
		private Board board;

		public SwitchMouseAdapter(Board board) {
			this.board = board;
		}

		@Override
		public void mouseDown(MouseEvent e) {
			this.board.boardCanvas.switchSide();
			this.board.colCoordsCanvas.switchSide();
			this.board.rowCoordsCanvas.switchSide();
			this.board.boardCanvas.updateBuffer();
			this.board.boardCanvas.redraw();
			this.board.colCoordsCanvas.redraw();
			this.board.rowCoordsCanvas.redraw();
		}
	}

	// layout management
	private Point preferredSize;
	RowCoordsCanvas rowCoordsCanvas;
	BoardCanvas boardCanvas;
	ColCoordsCanvas colCoordsCanvas;
	SwitchCanvas switchCanvas;

	public Board(Composite parent, ChessEngine chessEngine, int[] settings, int whitePosition,
			ImageFactory imageFactory, int style) {
		super(parent, style);
		this.preferredSize = new Point(settings[Sets.BOARD_WIDTH_INDEX] + settings[Sets.SWITCH_BUTTON_WIDTH_SIZE_INDEX],
				settings[Sets.BOARD_HEIGHT_INDEX] + settings[Sets.SWITCH_BUTTON_HEIGHT_SIZE_INDEX]);
		final Color backgroundColor = getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
		this.setBackground(backgroundColor);

		this.rowCoordsCanvas = new RowCoordsCanvas(this, settings);
		this.boardCanvas = new BoardCanvas(chessEngine, this, whitePosition, imageFactory, settings);
		this.colCoordsCanvas = new ColCoordsCanvas(this, settings);
		this.colCoordsCanvas.init(true);
		this.rowCoordsCanvas.init(true);

		this.switchCanvas = new SwitchCanvas(this, imageFactory);
		this.switchCanvas.switchButton.addMouseListener(new SwitchMouseAdapter(this));

		this.setLayout(new FormLayout());
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 2);
		formData.top = new FormAttachment(0, 2);
		this.rowCoordsCanvas.setLayoutData(formData);

		FormData formData1 = new FormData();
		formData1.left = new FormAttachment(this.rowCoordsCanvas, 0);
		formData1.top = new FormAttachment(0, 2);
		this.boardCanvas.setLayoutData(formData1);

		FormData formData2 = new FormData();
		formData2.top = new FormAttachment(this.rowCoordsCanvas, 0);
		formData2.left = new FormAttachment(0, 2);
		formData2.bottom = new FormAttachment(100, -2);
		this.switchCanvas.setLayoutData(formData2);

		FormData formData3 = new FormData();
		formData3.top = new FormAttachment(this.boardCanvas, 0);
		formData3.left = new FormAttachment(this.switchCanvas, 0);
		formData3.right = new FormAttachment(100, 0);
		formData3.bottom = new FormAttachment(100, 0);
		this.colCoordsCanvas.setLayoutData(formData3);

		pack();
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		return this.preferredSize;
	}

	@Override
	public void dispose() {
		this.boardCanvas.dispose();
		this.colCoordsCanvas.dispose();
		this.rowCoordsCanvas.dispose();
		this.switchCanvas.dispose();
		super.dispose();
	}
}
