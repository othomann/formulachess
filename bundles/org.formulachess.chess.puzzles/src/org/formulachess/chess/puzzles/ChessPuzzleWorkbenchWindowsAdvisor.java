package org.formulachess.chess.puzzles;

import java.util.Locale;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.formulachess.views.Messages;

public class ChessPuzzleWorkbenchWindowsAdvisor extends WorkbenchWindowAdvisor {
	public ChessPuzzleWorkbenchWindowsAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}
	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(850, 670));
		configurer.setShowFastViewBars(false);
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(false);
		configurer.setShowMenuBar(false);
		configurer.setShowPerspectiveBar(false);
		configurer.setShowProgressIndicator(false);
		configurer.setTitle(new Messages(Locale.getDefault()).getString("chesspuzzles.title")); //$NON-NLS-1$
	}
}
