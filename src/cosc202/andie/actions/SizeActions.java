package cosc202.andie.actions;

import java.awt.event.*;
import javax.swing.*;

//import cosc202.andie.EditableImage;
import cosc202.andie.ImageAction;
import cosc202.andie.components.PopupSlider;
import cosc202.andie.components.PopupWithSliders;
import cosc202.andie.operations.transform.FlipHorizontal;
import cosc202.andie.operations.transform.FlipVertical;
import cosc202.andie.operations.transform.Resize;
import cosc202.andie.operations.transform.RotateRight;

import static cosc202.andie.LanguageConfig.msg;

public class SizeActions extends MenuActions {

    public SizeActions(){
            actions = new ArrayList<Action>();
            actions.add(new SizeResizeAction(msg("SizeResize_Title"), null, msg("SizeResize_Desc"), Integer.valueOf(KeyEvent.VK_R)));
            actions.add(new SizeRotateRightAction(msg("SizeRotateRight_Title"), null, msg("SizeRotate_Desc"), Integer.valueOf(KeyEvent.VK_H)));
            actions.add(new SizeRotateLeftAction(msg("SizeRotateLeft_Title"), null, msg("SizeRotate_Desc"), Integer.valueOf(KeyEvent.VK_H)));
            actions.add(new SizeFlipHorizontalAction(msg("SizeFlipHorizontal_Title"), null, msg("SizeFlipHorizontal_Desc"), Integer.valueOf(KeyEvent.VK_F1)));
            actions.add(new SizeFlipVerticalAction(msg("SizeFlipVertical_Title"), null, msg("SizeFlipVertical_Desc"), Integer.valueOf(KeyEvent.VK_F1)));

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
        public void updateState() {
            setEnabled(target.getImage().hasImage());
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
        public void updateState() {
            setEnabled(target.getImage().hasImage());
        }
}

    public class SizeResizeAction extends ImageAction{
        SizeResizeAction(String name, ImageIcon icon, String desc, Integer mnemonic){
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            PopupSlider slider = new PopupSlider(msg("Resize_Popup_Label"),1,300,100,"%",10,50);
            PopupWithSliders popup = new PopupWithSliders(msg("Resize_Popup_Title"),new PopupSlider[]{slider});
            if (popup.show() == PopupWithSliders.OK) {
                int scale = slider.getValue();
                target.getImage().apply(new Resize(scale));
                target.repaint();
                target.getParent().revalidate();
            }
    }
    public class SizeRotateRightAction extends ImageAction{
        SizeRotateRightAction(String name, ImageIcon icon, String desc, Integer mnemonic){
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            
                target.getImage().apply(new RotateRight());
                target.repaint();
                target.getParent().revalidate();
        }
        public void updateState() {
            setEnabled(target.getImage().hasImage());
        }
}   
    public class SizeRotateLeftAction extends ImageAction{
        SizeRotateLeftAction(String name, ImageIcon icon, String desc, Integer mnemonic){
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
        
                target.getImage().apply(new RotateRight());
                target.getImage().apply(new RotateRight());
                target.getImage().apply(new RotateRight());
                target.repaint();
                target.getParent().revalidate();
        }
        public void updateState() {
            setEnabled(target.getImage().hasImage());
        }
}

        public void updateState() {
            setEnabled(target.getImage().hasImage());
        }
    }

}