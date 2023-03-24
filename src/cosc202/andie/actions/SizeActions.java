package cosc202.andie.actions;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;


import cosc202.andie.ImageAction;
import cosc202.andie.operations.transform.Flip;
import cosc202.andie.operations.transform.Resize;

public class SizeActions {
    protected ArrayList<Action> actions;

    public SizeActions(){
            actions = new ArrayList<Action>();
            actions.add(new SizeTestAction("Test" , null , "Testing", Integer.valueOf(KeyEvent.VK_T)));
            actions.add(new SizeResizeAction("Resize", null, "Scale", Integer.valueOf(KeyEvent.VK_R)));
            actions.add(new SizeFlipAction("Flip", null, "Rotate vertical or horizontal", Integer.valueOf(KeyEvent.VK_F)));
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

    public class SizeResizeAction extends ImageAction{
            SizeResizeAction(String name, ImageIcon icon, String desc, Integer mnemonic){
                super(name, icon, desc, mnemonic);
            }

            public void actionPerformed(ActionEvent e) {
            SpinnerNumberModel radiusModel = new SpinnerNumberModel(1, 1, 1000, 1);
            JSpinner radiusSpinner = new JSpinner(radiusModel);
            int invalid = JOptionPane.showOptionDialog(null, radiusSpinner, "Enter new size (Percentage)", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            int option = radiusModel.getNumber().intValue();
                    target.getImage().apply(new Resize(option));
                    target.repaint();
                    target.getParent().revalidate();
            }
    }

    public class SizeFlipAction extends ImageAction{
        SizeFlipAction(String name, ImageIcon icon, String desc, Integer mnemonic){
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
        
        String option = "left";
        
                target.getImage().apply(new Flip(option));
                target.repaint();
                target.getParent().revalidate();
        }
}
}