package cosc202.andie;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
public class LanguageConfig {

	public static final int MAORI = 0;
	public static final int ENGLISH = 1;

	public static final Locale[] locales = {new Locale("mi", "NZ"), new Locale("en", "NZ")};

	private static Preferences prefs;
	private static ResourceBundle bundle;
	private static ResourceBundle fallbackBundle;

	private static int currentLanguage = LanguageConfig.ENGLISH;

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

	private static boolean setLanguage(int language) {
		if (language < 0 || language >= locales.length) return false;
		boolean languageChanged = currentLanguage != language;
		currentLanguage = language;
		Locale.setDefault(locales[currentLanguage]);
		return languageChanged;
	}

	private static void updateBundle() {
		try {
			LanguageConfig.bundle = ResourceBundle.getBundle("MessageBundle");
		} catch (MissingResourceException err) {
			setLanguage(LanguageConfig.ENGLISH);
			LanguageConfig.bundle = ResourceBundle.getBundle("MessageBundle");
		}
		LanguageConfig.fallbackBundle = ResourceBundle.getBundle("MessageBundle", locales[LanguageConfig.ENGLISH]);
	}

	public static void changeLanguage(int lang) {  
		if (setLanguage(lang)) {
			prefs.put("language", String.valueOf(currentLanguage));
			updateBundle();
			Andie.relaunchAndie();
		}
	}

	public static int getLanguage() {
		return currentLanguage;
	}

	public static String msg(String key) {
		if (bundle.containsKey(key)) return bundle.getString(key);
		else if (fallbackBundle.containsKey(key)) return fallbackBundle.getString(key);
		else return key;
	}

}
