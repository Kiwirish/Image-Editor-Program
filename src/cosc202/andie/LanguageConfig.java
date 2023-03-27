package cosc202.andie;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;

public class LanguageConfig {

	public static final int MAORI = 0;
	public static final int ENGLISH = 1;

	private static Preferences prefs;
	private static ResourceBundle bundle;

	public LanguageConfig() {}

	public static void init() {
		LanguageConfig.prefs = Preferences.userNodeForPackage(LanguageConfig.class);

		Locale.setDefault(new Locale(prefs.get("language", "en"),prefs.get("country", "NZ")));
		LanguageConfig.bundle = ResourceBundle.getBundle("MessageBundle");
	}
	public static void setLanguage(int lang) {  
		switch(lang) {
			case MAORI:
				prefs.put("language", "mi");
				prefs.put("country", "NZ");
				break;
			case ENGLISH:
				prefs.put("language", "en");
				prefs.put("country", "NZ");
				break;
			default:
				break;
		}

		JOptionPane.showMessageDialog(null, msg("LANGUAGE_CHANGE_RESTART_ALERT"), msg("LANGUAGE_CHANGE_RESTART_TITLE"), JOptionPane.INFORMATION_MESSAGE);

	}

	public static String msg(String key) {
		return bundle.getString(key);
	}

}
