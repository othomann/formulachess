package org.formulachess.chess.puzzles;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.formulachess.views.ChessPuzzles;

public class PuzzlePerspective implements IPerspectiveFactory {
    public static final String ID_PERSPECTIVE = "org.formulachess.chess.puzzles.PuzzlePerspective"; //$NON-NLS-1$

    /* (non-Javadoc)
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
	public void createInitialLayout(IPageLayout layout) {
        layout.setEditorAreaVisible(false);
        layout.addStandaloneView(
            ChessPuzzles.ID_VIEW,
            false,
            IPageLayout.TOP,
            IPageLayout.RATIO_MAX,
            layout.getEditorArea());
        layout.addPerspectiveShortcut(ID_PERSPECTIVE);
        layout.addShowViewShortcut(ChessPuzzles.ID_VIEW);
	}
}
