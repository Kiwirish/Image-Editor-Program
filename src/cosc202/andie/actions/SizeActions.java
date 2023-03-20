package cosc202.andie.actions;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

import cosc202.andie.EditableImage;
import cosc202.andie.ImageAction;

public class SizeActions {
    protected ArrayList<Action> actions;

    public SizeActions(){
            actions = new ArrayList<Action>();
            actions.add(new SizeTestAction("Test" , null , "Testing", Integer.valueOf(KeyEvent.VK_T)));
    }
    public JMenu createMenu() {
        JMenu sizeMenu = new JMenu("Size");

        for(Action action: actions) {
            sizeMenu.add(new JMenuItem(action));
        }

        return sizeMenu;
    }

    public class SizeTestAction extends ImageAction{
            SizeTestAction(String name, ImageIcon icon, String desc, Integer mnemonic){
                super(name, icon, desc, mnemonic);
            } 
            public void actionPerformed(ActionEvent e) {
                System.out.println("Test worked");
            }
    }
}