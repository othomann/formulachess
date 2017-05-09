package org.formulachess.ui;

/**
 * 
 * // put the splash screen up final int SPLASH_X = 869; final int SPLASH_Y =
 * 98; Rectangle screenSize = display.getBounds();
 * 
 * Shell splash = new Shell(SWT.NO_TRIM); GridData splashData = new GridData();
 * splashData.verticalAlignment = GridData.FILL;
 * splashData.grabExcessVerticalSpace = false; splashData.horizontalAlignment =
 * GridData.FILL; splashData.grabExcessHorizontalSpace = false;
 * splashData.widthHint = SPLASH_X; splashData.heightHint = SPLASH_Y;
 * 
 * splash.setLayoutData(splashData); splash.setLayout(new FillLayout());
 * splash.setLocation((screenSize.width - SPLASH_X) / 2, (screenSize.height -
 * SPLASH_Y) / 2);
 * 
 * Image splashImage = new Image(null, LOGO_IMAGE); Label splashLogo = new
 * Label(splash, SWT.NULL); splashLogo.setImage(splashImage); splash.pack();
 * 
 * splash.open();
 * 
 * // Create the app window // ....
 * 
 * // Kill the splash screen splash.dispose();
 */
public class SplashScreen {
	// nothing to do
}
