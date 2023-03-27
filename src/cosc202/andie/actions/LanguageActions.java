package cosc202.andie.actions;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.prefs.Preferences;

import cosc202.andie.EditableImage;
import cosc202.andie.ImageAction;
import cosc202.andie.operations.transform.Resize;

public class LanguageActions {
    protected ArrayList<Action> actions;

    public LanguageActions(){
            actions = new ArrayList<Action>();
            actions.add(new EnglishLanguage("English" , null , null, null));
            actions.add(new MaoriLanguage("Maori", null, null, null));

        
    }
    /*public String maoriLanguage(String input){

        Preferences prefs = Preferences.userNodeForPackage(App.class);
        Locale.setDefault(new Locale(prefs.get("language", "en"),
        prefs.get("country", "NZ")));
        ResourceBundle bundle = ResourceBundle.getBundle("MessageBundle");
        prefs.put("language", "mi");
        prefs.put("country", "NZ"); 
        
        return bundle.getString(input);
    } */
    public static String englishLanguage(String input){

        Preferences prefs = Preferences.userNodeForPackage(LanguageActions.class);
        Locale.setDefault(new Locale(prefs.get("language", "en"),
        prefs.get("country", "NZ")));
        ResourceBundle bundle = ResourceBundle.getBundle("MessageBundle");
        prefs.put("language", "mi");
        prefs.put("country", "NZ"); 
        
        return bundle.getString(input);
    }




    public JMenu createMenu() {
        JMenu sizeMenu = new JMenu("Language");

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
                //set langauge to English
            }
    }

    public class MaoriLanguage extends ImageAction{
        MaoriLanguage(String name, ImageIcon icon, String desc, Integer mnemonic){
            super(name, icon, desc, mnemonic);
        } 
        public void actionPerformed(ActionEvent e) {
            //set langauge to Maori
        }
}

}
