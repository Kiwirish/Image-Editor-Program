package cosc202.andie.actions;

import java.awt.event.*;
import javax.swing.*;

import cosc202.andie.ImageAction;
import cosc202.andie.LanguageConfig;

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

    /**
     * Create a set of language actions
     */
    public LanguageActions(){
        super(msg("Language_Title"));
        actions.add(new LanguageAction(msg("English_Title"), null , msg("English_Desc"), null, Language.ENGLISH));
        actions.add(new LanguageAction(msg("Maori_Title"), null , msg("Maori_Desc"), null, Language.MAORI));
        actions.add(new LanguageAction(msg("French_Title"), null , msg("French_Desc"), null, Language.FRENCH));
        actions.add(new LanguageAction(msg("German_Title"), null , msg("German_Desc"), null, Language.GERMAN));
        actions.add(new LanguageAction(msg("Spanish_Title"), null , msg("Spanish_Desc"), null, Language.SPANISH));
        actions.add(new LanguageAction(msg("Turkish_Title"), null , msg("Turkish_Desc"), null, Language.TURKISH));
        actions.add(new LanguageAction(msg("Italian_Title"), null , msg("Italian_Desc"), null, Language.ITALIAN));

    }

    /**
     * Action to change the language
     */
    public class LanguageAction extends ImageAction{
        private Language language;
        /**
         * <p>
         * Create a new language action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         * @param language The key for the language to change to
         */
        LanguageAction(String name, ImageIcon icon, String desc, Integer mnemonic, Language language){
            super(name, icon, desc, mnemonic);
            this.language = language;
        } 

        /**
         * Set ANDIE's current language to the language specified in the constructor
         */
        public void actionPerformed(ActionEvent e) {
            LanguageConfig.changeLanguage(language);
        }

        public void updateState() {
            setEnabled(LanguageConfig.getLanguage() != language);
        }
    }

}
