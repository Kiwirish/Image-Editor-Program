package cosc202.andie.actions;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

//import cosc202.andie.EditableImage;
import cosc202.andie.ImageAction;
import cosc202.andie.operations.transform.Resize;

import static cosc202.andie.LanguageConfig.msg;

public class SizeActions {
    protected ArrayList<Action> actions;

    public SizeActions(){
            actions = new ArrayList<Action>();
            actions.add(new SizeTestAction(msg("SizeTest_Title") , null , msg("SizeTest_Desc"), Integer.valueOf(KeyEvent.VK_T)));
            actions.add(new SizeResizeAction(msg("SizeResize_Title"), null, msg("SizeResize_Desc"), Integer.valueOf(KeyEvent.VK_R)));
    }
    public JMenu createMenu() {
        JMenu sizeMenu = new JMenu(msg("Size_Title"));

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
                System.out.println(msg("SizeTest_Action"));
            }
    }

    public class SizeResizeAction extends ImageAction{
            SizeResizeAction(String name, ImageIcon icon, String desc, Integer mnemonic){
                super(name, icon, desc, mnemonic);
            }
            public void actionPerformed(ActionEvent e) {
                    target.getImage().apply(new Resize());
                    target.repaint();
                    target.getParent().revalidate();
            }
    }
}