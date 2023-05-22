package cosc202.andie.actions;

import java.awt.event.*;
import java.util.Arrays;

import javax.swing.*;

import cosc202.andie.ImageAction;
import cosc202.andie.LanguageConfig;
import cosc202.andie.LanguageConfig.LanguageListener;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;

import static cosc202.andie.LanguageConfig.msg;
import static cosc202.andie.LanguageConfig.Language;
/**
 * <p>
 * Actions provided by the Language menu.
 * </p>
 * 
 * <p>
 * The Language menu contains actions that set the language of the application. The action corresponding to the current language is disabled.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @see LanguageConfig
 * @author Jeb Nicholson
 * @author Oliver Peyroux
 * @version 1.0
 */
public class LanguageActions extends MenuActions {

    LanguageAction setEnglishAction;
    LanguageAction setMaoriAction;
    LanguageAction setFrenchAction;
    LanguageAction setGermanAction;
    LanguageAction setSpanishAction;
    LanguageAction setTurkishAction;
    LanguageAction setItalianAction;


    /**
     * Create a set of language actions
     * @param model
     * @param controller
     */
    public LanguageActions(AndieController controller, AndieModel model){
        super(msg("Language_Title"), controller, model);

        setEnglishAction = new LanguageAction(msg("English_Title"), msg("English_Desc"), null, null, Language.ENGLISH);
        setMaoriAction = new LanguageAction(msg("Maori_Title"), msg("Maori_Desc"), null, null, Language.MAORI);
        setFrenchAction = new LanguageAction(msg("French_Title"), msg("French_Desc"), null, null, Language.FRENCH);
        setGermanAction = new LanguageAction(msg("German_Title"), msg("German_Desc"), null, null, Language.GERMAN);
        setSpanishAction = new LanguageAction(msg("Spanish_Title"), msg("Spanish_Desc"), null, null, Language.SPANISH);
        setTurkishAction = new LanguageAction(msg("Turkish_Title"), msg("Turkish_Desc"), null, null, Language.TURKISH);
        setItalianAction = new LanguageAction(msg("Italian_Title"), msg("Italian_Desc"), null, null, Language.ITALIAN);
        
        actions.addAll(Arrays.asList(setEnglishAction, setMaoriAction, setFrenchAction, setGermanAction, setSpanishAction, setTurkishAction, setItalianAction));
    }

    /**
     * Action to change the language
     */
    public class LanguageAction extends ImageAction{

        private Language language;
        private LanguageListener languageListener;

        /**
         * <p>
         * Create a new language action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         * @param language The key for the language to change to
         */
       public LanguageAction(String name, String desc, Integer mnemonic, KeyStroke keyboardShortcut, Language language){
            super(name, desc, mnemonic, keyboardShortcut);
            this.language = language;
            languageListener = ()->{
                setEnabled(LanguageConfig.getLanguage() != language);
            };
            LanguageConfig.registerLanguageListener(languageListener);
            languageListener.update();
        } 

        public void removeNotify(){
            LanguageConfig.unregisterLanguageListener(languageListener);
        }

        /**
         * Set ANDIE's current language to the language specified in the constructor
         */
        public void actionPerformed(ActionEvent e) {
            LanguageConfig.changeLanguage(language);
        }
    }

}
