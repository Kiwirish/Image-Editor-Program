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

public class TransformActions extends MenuActions {

    public TransformActions(){
        super(msg("Transform_Title"));
        actions.add(new ResizeAction(msg("TransformResize_Title"), null, msg("TransformResize_Desc"), Integer.valueOf(KeyEvent.VK_R)));
        actions.add(new RotateLeftAction(msg("TransformRotateClockwise_Title"), null, msg("TransformRotateClockwise_Desc"), Integer.valueOf(KeyEvent.VK_H)));
        actions.add(new RotateRightAction(msg("TransformRotateAntiClockwise_Title"), null, msg("TransformRotateAntiClockwise_Desc"), Integer.valueOf(KeyEvent.VK_H)));
        actions.add(new FlipHorizontalAction(msg("TransformFlipHorizontal_Title"), null, msg("TransformFlipHorizontal_Desc"), Integer.valueOf(KeyEvent.VK_F1)));
        actions.add(new FlipVerticalAction(msg("TransformFlipVertical_Title"), null, msg("TransformFlipVertical_Desc"), Integer.valueOf(KeyEvent.VK_F1)));

    }
    public class FlipVerticalAction extends ImageAction{
        FlipVerticalAction(String name, ImageIcon icon, String desc, Integer mnemonic){
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
    public class FlipHorizontalAction extends ImageAction{
        FlipHorizontalAction(String name, ImageIcon icon, String desc, Integer mnemonic){
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
    public class ResizeAction extends ImageAction{
        ResizeAction(String name, ImageIcon icon, String desc, Integer mnemonic){
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
    public class RotateRightAction extends ImageAction{
        RotateRightAction(String name, ImageIcon icon, String desc, Integer mnemonic){
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
    public class RotateLeftAction extends ImageAction{
        RotateLeftAction(String name, ImageIcon icon, String desc, Integer mnemonic){
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
                target.getImage().apply(new RotateRight());
                target.getImage().apply(new RotateRight());
                target.getImage().apply(new RotateRight());
                //BUG: Applying the RotateRight operation 3 times adds 3 operations to the operations stack -  .apply() should be called once
                //Possible solution: Have RotateRight() accept a boolean "clockwise"
                target.repaint();
                target.getParent().revalidate();
        }
        public void updateState() {
            setEnabled(target.getImage().hasImage());
        }
    }
}
