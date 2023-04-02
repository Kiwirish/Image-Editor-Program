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

import static cosc202.andie.LanguageConfig.msg;

public class SizeActions extends MenuActions {

    public SizeActions(){
        super(msg("Size_Title"));
        actions.add(new SizeResizeAction(msg("SizeResize_Title"), null, msg("SizeResize_Desc"), Integer.valueOf(KeyEvent.VK_R)));
        //actions.add(new SizeRotateAction(msg("SizeRotate_Title"), null, msg("SizeRotate_Desc"), Integer.valueOf(KeyEvent.VK_H)));
        actions.add(new SizeFlipHorizontalAction(msg("SizeFlipHorizontalAction_Title"), null, msg("SizeFlipHorizontalAction_Desc"), Integer.valueOf(KeyEvent.VK_F1)));
        actions.add(new SizeFlipVerticalAction(msg("SizeFlipVerticalAction_Title"), null, msg("SizeFlipVerticalAction_Desc"), Integer.valueOf(KeyEvent.VK_F1)));

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

        public void updateState() {
            setEnabled(target.getImage().hasImage());
        }
    }

}