package cosc202.andie.actions;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

//import cosc202.andie.EditableImage;
import cosc202.andie.ImageAction;
import cosc202.andie.operations.transform.FlipHorizontal;
import cosc202.andie.operations.transform.FlipVertical;
import cosc202.andie.operations.transform.Resize;
import cosc202.andie.operations.transform.Rotate;

import static cosc202.andie.LanguageConfig.msg;

public class SizeActions {
    protected ArrayList<Action> actions;

    public SizeActions(){
            actions = new ArrayList<Action>();
            actions.add(new SizeTestAction(msg("SizeTest_Title") , null , msg("SizeTest_Desc"), Integer.valueOf(KeyEvent.VK_T)));
            actions.add(new SizeResizeAction(msg("SizeResize_Title"), null, msg("SizeResize_Desc"), Integer.valueOf(KeyEvent.VK_R)));
            //actions.add(new SizeRotateAction(msg("SizeRotate_Title"), null, msg("SizeRotate_Desc"), Integer.valueOf(KeyEvent.VK_H)));
            actions.add(new SizeFlipHorizontalAction(msg("SizeFlipHorizontalAction_Title"), null, msg("SizeFlipHorizontalAction_Desc"), Integer.valueOf(KeyEvent.VK_F1)));
            actions.add(new SizeFlipVerticalAction(msg("SizeFlipVerticalAction_Title"), null, msg("SizeFlipVerticalAction_Desc"), Integer.valueOf(KeyEvent.VK_F1)));

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

    public class SizeFlipVerticalAction extends ImageAction{
        SizeFlipVerticalAction(String name, ImageIcon icon, String desc, Integer mnemonic){
            super(name, icon, desc, mnemonic);
        } 
        public void actionPerformed(ActionEvent e) {
            target.getImage().apply(new FlipVertical());
            target.repaint();
            target.getParent().revalidate();
    }
}
    public class SizeFlipHorizontalAction extends ImageAction{
        SizeFlipHorizontalAction(String name, ImageIcon icon, String desc, Integer mnemonic){
            super(name, icon, desc, mnemonic);
        } 
        public void actionPerformed(ActionEvent e) {
            target.getImage().apply(new FlipHorizontal());
            target.repaint();
            target.getParent().revalidate();
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

}