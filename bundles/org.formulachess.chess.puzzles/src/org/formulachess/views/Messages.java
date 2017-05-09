/*
 * Created on 2003-05-06
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package org.formulachess.views;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Messages {

	private static final Logger MY_LOGGER = Logger.getLogger(Messages.class.getCanonicalName());
	private static final String BUNDLE_NAME = "org.formulachess.views.messages"; //$NON-NLS-1$

	private ResourceBundle resourceBundle;

	/**
	 * 
	 */
	public Messages(Locale locale) {
		this.resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
	}

	public void setLocale(Locale newLocale) {
		this.resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, newLocale);
	}

	/**
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		try {
			return this.resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			MY_LOGGER.log(Level.SEVERE, "Missing key", e);
			return '!' + key + '!';
		}
	}
}
