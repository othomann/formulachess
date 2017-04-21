package org.formulachess.chess.puzzles;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ChessPuzzleWorkbenchAdvisor extends WorkbenchAdvisor {

	public String getInitialWindowPerspectiveId() {
		return PuzzlePerspective.ID_PERSPECTIVE;
	}

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new ChessPuzzleWorkbenchWindowsAdvisor(configurer);
	}
}
