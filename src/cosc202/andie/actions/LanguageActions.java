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
        actions.add(new EnglishLanguage(msg("English_Title"), null , msg("English_Desc"), null));
        actions.add(new MaoriLanguage(msg("Maori_Title"), null, msg("Maori_Desc"), null));
    }

    public JMenu createMenu() {
        JMenu sizeMenu = new JMenu(msg("Language_Title"));

        for(Action action: actions) {
            sizeMenu.add(new JMenuItem(action));
        }

        return sizeMenu;
    }

    public class EnglishLanguage extends ImageAction{
            EnglishLanguage(String name, ImageIcon icon, String desc, Integer mnemonic){
                super(name, icon, desc, mnemonic);
            } 
            public void actionPerformed(ActionEvent e) {
                LanguageConfig.setLanguage(LanguageConfig.ENGLISH);
            }
    }
    public class MaoriLanguage extends ImageAction{
            MaoriLanguage(String name, ImageIcon icon, String desc, Integer mnemonic){
                super(name, icon, desc, mnemonic);
            } 
            public void actionPerformed(ActionEvent e) {
                LanguageConfig.setLanguage(LanguageConfig.MAORI);
            }
    }

}
