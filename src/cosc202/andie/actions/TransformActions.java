package cosc202.andie.actions;

import java.awt.event.*;
import javax.swing.*;

//import cosc202.andie.EditableImage;
import cosc202.andie.ImageAction;
import cosc202.andie.components.PopupSlider;
import cosc202.andie.components.PopupWithSliders;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.AndieModel.ModelListener;
import cosc202.andie.operations.transform.FlipHorizontal;
import cosc202.andie.operations.transform.FlipVertical;
import cosc202.andie.operations.transform.Resize;
import cosc202.andie.operations.transform.RotateLeft;
import cosc202.andie.operations.transform.RotateRight;
import cosc202.andie.operations.transform.Rotate180;

import static cosc202.andie.LanguageConfig.msg;

/**
 * <p>
 * Actions provided by the Transform menu.
 * </p>
 * 
 * <p>
 * The transform menu contains action the affect the size and orientation of the image.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @author Jeb Nicholson
 * @author Bernard Pieters
 * @version 2.0
 */
public class TransformActions extends MenuActions {

    public TransformActions(AndieController controller, AndieModel model){
        super(msg("Transform_Title"), controller, model);
        actions.add(new ResizeAction(msg("TransformResize_Title"), null, msg("TransformResize_Desc"), Integer.valueOf(KeyEvent.VK_R)));
        actions.add(new RotateRightAction(msg("TransformRotateClockwise_Title"), null, msg("TransformRotateClockwise_Desc"), Integer.valueOf(KeyEvent.VK_H)));
        actions.add(new RotateLeftAction(msg("TransformRotateAntiClockwise_Title"), null, msg("TransformRotateAntiClockwise_Desc"), Integer.valueOf(KeyEvent.VK_H)));
        actions.add(new Rotate180Action(msg("TransformRotate180_Title"), null, msg("TransformRotate180_Desc"), Integer.valueOf(KeyEvent.VK_H)));
        actions.add(new FlipHorizontalAction(msg("TransformFlipHorizontal_Title"), null, msg("TransformFlipHorizontal_Desc"), Integer.valueOf(KeyEvent.VK_F1)));
        actions.add(new FlipVerticalAction(msg("TransformFlipVertical_Title"), null, msg("TransformFlipVertical_Desc"), Integer.valueOf(KeyEvent.VK_F1)));

        ModelListener isl = ()-> {
            for (ImageAction action : actions) {
                action.setEnabled(model.hasImage());
            }
        };
        model.registerImageStatusListener(isl);
        isl.update();

    }

    /** action to apply flip vertical */
    public class FlipVerticalAction extends ImageAction{
        /**
         * <p>
         * Create a new FlipVerticalAction.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        FlipVerticalAction(String name, ImageIcon icon, String desc, Integer mnemonic){
            super(name, icon, desc, mnemonic);
        } 
        /** Call back for when FlipVerticalAction is triggered */
        public void actionPerformed(ActionEvent e) {
            controller.operations.apply(new FlipVertical());
        }
    }
    /** action to apply the flip horizontal */
    public class FlipHorizontalAction extends ImageAction{
        FlipHorizontalAction(String name, ImageIcon icon, String desc, Integer mnemonic){
            super(name, icon, desc, mnemonic);
        } 
         /** Call back for when FlipHorizontalAction is triggered */
        public void actionPerformed(ActionEvent e) {
            controller.operations.apply(new FlipHorizontal());
        }
    }
    /** to apply the Resize  */
    public class ResizeAction extends ImageAction{
        ResizeAction(String name, ImageIcon icon, String desc, Integer mnemonic){
            super(name, icon, desc, mnemonic);
        }
         /** Call back for when ResizeAction is triggered */
        public void actionPerformed(ActionEvent e) {
            PopupSlider slider = new PopupSlider(msg("Resize_Popup_Label"),1,300,100,"%",10,50);
            slider.addChangeListener((ev)->{
                controller.operations.update(new Resize(slider.getValue()));
            });
            PopupWithSliders popup = new PopupWithSliders(controller.getPopupParent(),msg("Resize_Popup_Title"),new PopupSlider[]{slider});
            controller.operations.end(popup.show() == PopupWithSliders.OK);
        }
    }
    /** action to apply the Rotate right filter */
    public class RotateRightAction extends ImageAction{
        RotateRightAction(String name, ImageIcon icon, String desc, Integer mnemonic){
            super(name, icon, desc, mnemonic);
        }
        /** Call back for when RotateRightAction is triggered */
        public void actionPerformed(ActionEvent e) {
            controller.operations.apply(new RotateRight());
        }
    }   
    /** action to apply the rotate left  */
    public class RotateLeftAction extends ImageAction{
        RotateLeftAction(String name, ImageIcon icon, String desc, Integer mnemonic){
            super(name, icon, desc, mnemonic);
        }
         /** Call back for when RotateLeftAction is triggered */
        public void actionPerformed(ActionEvent e) {
            controller.operations.apply(new RotateLeft());
        }
    }
    /** action to apply the rotate 180 */
    public class Rotate180Action extends ImageAction{
        Rotate180Action(String name, ImageIcon icon, String desc, Integer mnemonic){
            super(name, icon, desc, mnemonic);
        }
         /** Call back for when Rotate180Action is triggered */
        public void actionPerformed(ActionEvent e) {
            controller.operations.apply(new Rotate180());
        }
    }
}
