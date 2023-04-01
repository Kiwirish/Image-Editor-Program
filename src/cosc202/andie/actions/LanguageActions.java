package cosc202.andie.actions;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

import cosc202.andie.ImageAction;
import cosc202.andie.LanguageConfig;

import static cosc202.andie.LanguageConfig.msg;

public class LanguageActions {
    protected ArrayList<Action> actions;

    public LanguageActions(){
        actions = new ArrayList<Action>();
        actions.add(new LanguageAction(msg("English_Title"), null , msg("English_Desc"), null, LanguageConfig.ENGLISH));
        actions.add(new LanguageAction(msg("Maori_Title"), null , msg("Maori_Desc"), null, LanguageConfig.MAORI));
    }

    public JMenu createMenu() {
        JMenu sizeMenu = new JMenu(msg("Language_Title"));
        ButtonGroup group = new ButtonGroup();

        for(Action action: actions) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(action);
            group.add(item);
            sizeMenu.add(item);
        }

        return sizeMenu;
    }

    public class LanguageAction extends ImageAction{
            int language;
            LanguageAction(String name, ImageIcon icon, String desc, Integer mnemonic, int language){
                super(name, icon, desc, mnemonic);
                this.language = language;
                // putValue(Action.SELECTED_KEY, LanguageConfig.getLanguage() == language);
                setEnabled(LanguageConfig.getLanguage() != language);
            } 
            public void actionPerformed(ActionEvent e) {
                LanguageConfig.changeLanguage(language);
            }
    }

}
