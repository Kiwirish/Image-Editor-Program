package cosc202.andie;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * <p>
 * Helper class for managing the language of the application.
 * </p>
 * 
 * <p>
 * Sets the active language, reads the language message bundle, and provides access to strings in the bundle, through the {@code msg()} method.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Jeb Nicholson
 * @author Oliver Peyroux
 * @version 2.0
 */
public class LanguageConfig {

	//These are the language codes for the supported languages
	public static enum Language {
		MAORI ("mi","NZ"), 
		ENGLISH ("en","NZ"),
		FRENCH ("fr","FR"), 
		GERMAN ("de","DE"),
		SPANISH ("es","ES"),
		TURKISH ("tr","TR"), 
		ITALIAN ("it","IT");

		private Locale locale;
		Language(String language, String country) {
			locale = new Locale(language, country);
		}
		public Locale getLocale() {
			return locale;
		}
	}

	private static ArrayList<LanguageListener> languageListeners = new ArrayList<LanguageListener>();

	private static final String bundleBaseName = "languages/MessageBundle";

	private static Preferences prefs;
	private static ResourceBundle bundle;
	private static ResourceBundle fallbackBundle;

	private static Language currentLanguage = LanguageConfig.Language.ENGLISH;

	/**
	 * Called when the application starts, to set the language to the last used language
	 * Reads the user's preferences, and if no language has been set, it defaults to English
	 */
	public static void init() {
		LanguageConfig.prefs = Preferences.userNodeForPackage(LanguageConfig.class);

		Language retrievedLanguage = null;
		try {
			retrievedLanguage = Language.valueOf(prefs.get("language", String.valueOf(LanguageConfig.Language.ENGLISH)));
		} catch (IllegalArgumentException err) {
			retrievedLanguage = LanguageConfig.Language.ENGLISH;
		}

		setLanguage(retrievedLanguage);
		updateBundle();
	}

	/**
	 * Set the language to the language code specified
	 * @param language the language code to set 
	 * @return true if the language was changed, false otherwise (invalid language code, or language already set)
	 */
	private static boolean setLanguage(Language language) {
		if (language == null) return false;
		boolean languageChanged = currentLanguage != language;
		currentLanguage = language;
		Locale.setDefault(currentLanguage.getLocale());
		return languageChanged;
	}

	/**
	 * Reads a new message bundle for the current language
	 * Defaults to English if the message bundle for the current language is not found
	 * English is also read as a fallback language, in case a given key is missing in the current language
	 */
	private static void updateBundle() {
		try {
			LanguageConfig.bundle = ResourceBundle.getBundle(LanguageConfig.bundleBaseName);
		} catch (MissingResourceException err) {
			setLanguage(LanguageConfig.Language.ENGLISH);
			LanguageConfig.bundle = ResourceBundle.getBundle(LanguageConfig.bundleBaseName);
		}
		LanguageConfig.fallbackBundle = ResourceBundle.getBundle(LanguageConfig.bundleBaseName, Language.ENGLISH.getLocale());
	}

	/**
	 * Changes the interface's language to the language code specified
	 * Updates the language, saves the new language to the user's preferences, and relaunches ANDIE.
	 * @param lang the language code to change to
	 */
	public static void changeLanguage(Language lang) {  
		if (setLanguage(lang)) {
			prefs.put("language", String.valueOf(currentLanguage));
			updateBundle();
			notifyLanguageListeners();
			// Andie.relaunchAndie();
		}
	}

	/**
	 * Gets the current language
	 * @return the current language 
	 */
	public static Language getLanguage() {
		return currentLanguage;
	}

	/**
	 * Get the string associated with the given key in the current language's message bundle
	 * @param key the key to look up
	 * @return the string associated with the given key, or the key itself if the key is not found
	 */
	public static String msg(String key) {
		if (bundle.containsKey(key)) return bundle.getString(key);
		else if (fallbackBundle.containsKey(key)) return fallbackBundle.getString(key);
		else return key;
	}

	public static void registerLanguageListener(LanguageListener listener) {
		languageListeners.add(listener);
	}
	public static void notifyLanguageListeners() {
		for (LanguageListener listener : languageListeners) {
			listener.update();
		}
	}

	public interface LanguageListener extends EventListener {
		public void update();
	}

}
