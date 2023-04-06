package cosc202.andie;

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
 * @version 1.0
 */
public class LanguageConfig {

	//These are the language codes for the supported languages
	public static final int MAORI = 0;
	public static final int ENGLISH = 1;
	public static final int FRENCH = 2;
	public static final int GERMAN = 3;
	public static final int SPANISH = 4;
	public static final int TURKISH = 5;
	public static final int ITALIAN = 6;

	//Each language code represents an index in the locales array, which maps language codes to Locale objects
	public static final Locale[] locales = {new Locale("mi", "NZ"), 
	new Locale("en", "NZ"), new Locale("fr", "FR"), 
	new Locale("de", "DE"), new Locale("es", "ES"), 
	new Locale("tr", "TR"), new Locale("it", "IT")};

	private static Preferences prefs;
	private static ResourceBundle bundle;
	private static ResourceBundle fallbackBundle;

	private static int currentLanguage = LanguageConfig.ENGLISH;

	/**
	 * Called when the application starts, to set the language to the last used language
	 * Reads the user's preferences, and if no language has been set, it defaults to English
	 */
	public static void init() {
		LanguageConfig.prefs = Preferences.userNodeForPackage(LanguageConfig.class);

		int retrievedLanguage = -1;
		try {
			retrievedLanguage = Integer.valueOf(prefs.get("language", String.valueOf(LanguageConfig.ENGLISH)));
		} catch (NumberFormatException err) {
			retrievedLanguage = LanguageConfig.ENGLISH;
		}

		setLanguage(retrievedLanguage);
		updateBundle();
	}

	/**
	 * Set the language to the language code specified
	 * @param language the language code to set 
	 * @return true if the language was changed, false otherwise (invalid language code, or language already set)
	 */
	private static boolean setLanguage(int language) {
		if (language < 0 || language >= locales.length) return false;
		boolean languageChanged = currentLanguage != language;
		currentLanguage = language;
		Locale.setDefault(locales[currentLanguage]);
		return languageChanged;
	}

	/**
	 * Reads a new message bundle for the current language
	 * Defaults to English if the message bundle for the current language is not found
	 * English is also read as a fallback language, in case a given key is missing in the current language
	 */
	private static void updateBundle() {
		try {
			LanguageConfig.bundle = ResourceBundle.getBundle("MessageBundle");
		} catch (MissingResourceException err) {
			setLanguage(LanguageConfig.ENGLISH);
			LanguageConfig.bundle = ResourceBundle.getBundle("MessageBundle");
		}
		LanguageConfig.fallbackBundle = ResourceBundle.getBundle("MessageBundle", locales[LanguageConfig.ENGLISH]);
	}

	/**
	 * Changes the interface's language to the language code specified
	 * Updates the language, saves the new language to the user's preferences, and relaunches ANDIE.
	 * @param lang the language code to change to
	 */
	public static void changeLanguage(int lang) {  
		if (setLanguage(lang)) {
			prefs.put("language", String.valueOf(currentLanguage));
			updateBundle();
			Andie.relaunchAndie();
		}
	}

	/**
	 * Gets the current language code
	 * @return the current language code
	 */
	public static int getLanguage() {
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

}
