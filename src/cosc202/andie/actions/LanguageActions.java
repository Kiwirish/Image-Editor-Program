package cosc202.andie.actions;

import java.awt.event.*;
import javax.swing.*;

import cosc202.andie.ImageAction;
import cosc202.andie.LanguageConfig;

import static cosc202.andie.LanguageConfig.msg;

public class LanguageActions extends MenuActions {

    public LanguageActions(){
        super(msg("Language_Title"));
        actions.add(new LanguageAction(msg("English_Title"), null , msg("English_Desc"), null, LanguageConfig.ENGLISH));
        actions.add(new LanguageAction(msg("Maori_Title"), null , msg("Maori_Desc"), null, LanguageConfig.MAORI));
    }

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
