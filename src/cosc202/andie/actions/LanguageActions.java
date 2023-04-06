package cosc202.andie.actions;

import java.awt.event.*;
import javax.swing.*;

import cosc202.andie.ImageAction;
import cosc202.andie.LanguageConfig;

import static cosc202.andie.LanguageConfig.msg;
/**
 * <p>
 * Actions provided by the Language menu.
 * </p>
 * 
 * <p>
 * The Language menu contains actions that update the chosen language in the andie app.
 * </p>
 * 
 * @author Oliver Peyroux
 * @version 1.0
 */
public class LanguageActions extends MenuActions {
    /**
     * <p>
     * Create a set of Language menu actions.
     * </p>
     */
    public LanguageActions(){
        super(msg("Language_Title"));
        actions.add(new LanguageAction(msg("English_Title"), null , msg("English_Desc"), null, LanguageConfig.ENGLISH));
        actions.add(new LanguageAction(msg("Maori_Title"), null , msg("Maori_Desc"), null, LanguageConfig.MAORI));
        actions.add(new LanguageAction(msg("French_Title"), null , msg("French_Desc"), null, LanguageConfig.FRENCH));
        actions.add(new LanguageAction(msg("German_Title"), null , msg("German_Desc"), null, LanguageConfig.GERMAN));
        actions.add(new LanguageAction(msg("Spanish_Title"), null , msg("Spanish_Desc"), null, LanguageConfig.SPANISH));
        actions.add(new LanguageAction(msg("Turkish_Title"), null , msg("Turkish_Desc"), null, LanguageConfig.TURKISH));
        actions.add(new LanguageAction(msg("Italian_Title"), null , msg("Italian_Desc"), null, LanguageConfig.ITALIAN));

    }
    /**
    * <p>
    * Action to apply a LanguageAction
    * </p>
    * 
    * @see SharpenFilter
    */
    public class LanguageAction extends ImageAction{
            int language;
            LanguageAction(String name, ImageIcon icon, String desc, Integer mnemonic, int language){
                super(name, icon, desc, mnemonic);
                this.language = language;
            } 

            public void actionPerformed(ActionEvent e) {
                LanguageConfig.changeLanguage(language);
            }

            public void updateState() {
                setEnabled(LanguageConfig.getLanguage() != language);
            }
    }

}
