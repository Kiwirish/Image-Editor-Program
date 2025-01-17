package cosc202.andie.actions;

import java.awt.Rectangle;
import java.awt.event.*;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.event.ChangeListener;

//import cosc202.andie.EditableImage;
import cosc202.andie.ImageAction;
import cosc202.andie.components.PopupSlider;
import cosc202.andie.components.OptionPopup;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.AndieModel.ModelListener;
import cosc202.andie.operations.transform.Crop;
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
 * The transform menu contains action the affect the size and orientation of the
 * image.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @author Jeb Nicholson
 * @author Bernard Pieters
 * @version 2.0
 */
public class TransformActions extends MenuActions {

    public ResizeAction resizeAction;
    public RotateRightAction rotateRightAction;
    public RotateLeftAction rotateLeftAction;
    public Rotate180Action rotate180Action;
    public FlipHorizontalAction flipHorizontalAction;
    public FlipVerticalAction flipVerticalAction;
    public CropAction cropAction;

    public TransformActions(AndieController controller, AndieModel model) {
        super(msg("Transform_Title"), controller, model);

        resizeAction = new ResizeAction(msg("TransformResize_Title"), msg("TransformResize_Desc"), Integer.valueOf(KeyEvent.VK_R), null);
        rotateRightAction = new RotateRightAction(msg("TransformRotateClockwise_Title"), msg("TransformRotateClockwise_Desc"), Integer.valueOf(KeyEvent.VK_H), null);
        rotateLeftAction = new RotateLeftAction(msg("TransformRotateAntiClockwise_Title"), msg("TransformRotateAntiClockwise_Desc"), Integer.valueOf(KeyEvent.VK_H), null);
        rotate180Action = new Rotate180Action(msg("TransformRotate180_Title"), msg("TransformRotate180_Desc"), Integer.valueOf(KeyEvent.VK_H), null);
        flipHorizontalAction = new FlipHorizontalAction(msg("TransformFlipHorizontal_Title"), msg("TransformFlipHorizontal_Desc"), Integer.valueOf(KeyEvent.VK_F1), null);
        flipVerticalAction = new FlipVerticalAction(msg("TransformFlipVertical_Title"), msg("TransformFlipVertical_Desc"), Integer.valueOf(KeyEvent.VK_F1), null);
        cropAction = new CropAction(msg("Crop_Title"), msg("Crop_Desc"), null, null);

        actions.addAll(Arrays.asList(resizeAction, rotateRightAction, rotateLeftAction, rotate180Action, flipHorizontalAction, flipVerticalAction, cropAction));
    }

    /** action to apply flip vertical */
    public class FlipVerticalAction extends ImageAction {
        private ModelListener imageStatusListener;

       public  FlipVerticalAction(String name, String desc, Integer mnemonic, KeyStroke keyboardShortcut) {
            super(name, desc, mnemonic, keyboardShortcut);
            imageStatusListener = () -> {
                setEnabled(model.hasImage());
            };
            model.registerImageStatusListener(imageStatusListener);
            imageStatusListener.update();
        }

        @Override
        public void removeNotify() {
            model.unregisterImageStatusListener(imageStatusListener);
        }

        /** Call back for when FlipVerticalAction is triggered */
        public void actionPerformed(ActionEvent e) {
            controller.operations.apply(new FlipVertical());
        }
    }

    /** action to apply the flip horizontal */
    public class FlipHorizontalAction extends ImageAction {
        private ModelListener imageStatusListener;

       public  FlipHorizontalAction(String name, String desc, Integer mnemonic, KeyStroke keyboardShortcut) {
            super(name, desc, mnemonic, keyboardShortcut);
            imageStatusListener = () -> {
                setEnabled(model.hasImage());
            };
            model.registerImageStatusListener(imageStatusListener);
            imageStatusListener.update();
        }

        @Override
        public void removeNotify() {
            model.unregisterImageStatusListener(imageStatusListener);
        }

        /** Call back for when FlipHorizontalAction is triggered */
        public void actionPerformed(ActionEvent e) {
            controller.operations.apply(new FlipHorizontal());
        }
    }

    /** to apply the Resize */
    public class ResizeAction extends ImageAction {
        private ModelListener imageStatusListener;

       public  ResizeAction(String name, String desc, Integer mnemonic, KeyStroke keyboardShortcut) {
            super(name, desc, mnemonic, keyboardShortcut);
            imageStatusListener = () -> {
                setEnabled(model.hasImage());
            };
            model.registerImageStatusListener(imageStatusListener);
            imageStatusListener.update();
        }

        @Override
        public void removeNotify() {
            model.unregisterImageStatusListener(imageStatusListener);
        }

        /** Call back for when ResizeAction is triggered */
        public void actionPerformed(ActionEvent e) {
            PopupSlider slider = new PopupSlider(msg("Resize_Popup_Label"), 1, 300, 100, "%", 10, 50, 1);
            ChangeListener listener = (ev)->{
                controller.operations.update(new Resize(slider.getValue()));
            };
            slider.addChangeListener(listener);
            listener.stateChanged(null);

            OptionPopup popup = new OptionPopup(controller.getContentPane(), msg("Resize_Popup_Title"),
                    new PopupSlider[] { slider });
            controller.operations.end(popup.show() == OptionPopup.OK);
        }
    }

    /** action to apply the Rotate right filter */
    public class RotateRightAction extends ImageAction {
        private ModelListener imageStatusListener;

       public  RotateRightAction(String name, String desc, Integer mnemonic, KeyStroke keyboardShortcut) {
            super(name, desc, mnemonic, keyboardShortcut);
            imageStatusListener = () -> {
                setEnabled(model.hasImage());
            };
            model.registerImageStatusListener(imageStatusListener);
            imageStatusListener.update();
        }

        @Override
        public void removeNotify() {
            model.unregisterImageStatusListener(imageStatusListener);
        }

        /** Call back for when RotateRightAction is triggered */
        public void actionPerformed(ActionEvent e) {
            controller.operations.apply(new RotateRight());
        }
    }

    /** action to apply the rotate left */
    public class RotateLeftAction extends ImageAction {
        private ModelListener imageStatusListener;

       public  RotateLeftAction(String name, String desc, Integer mnemonic, KeyStroke keyboardShortcut) {
            super(name, desc, mnemonic, keyboardShortcut);
            imageStatusListener = () -> {
                setEnabled(model.hasImage());
            };
            model.registerImageStatusListener(imageStatusListener);
            imageStatusListener.update();
        }

        @Override
        public void removeNotify() {
            model.unregisterImageStatusListener(imageStatusListener);
        }

        /** Call back for when RotateLeftAction is triggered */
        public void actionPerformed(ActionEvent e) {
            controller.operations.apply(new RotateLeft());
        }
    }

    /** action to apply the rotate 180 */
    public class Rotate180Action extends ImageAction {
        private ModelListener imageStatusListener;

       public  Rotate180Action(String name, String desc, Integer mnemonic, KeyStroke keyboardShortcut) {
            super(name, desc, mnemonic, keyboardShortcut);
            imageStatusListener = () -> {
                setEnabled(model.hasImage());
            };
            model.registerImageStatusListener(imageStatusListener);
            imageStatusListener.update();
        }

        @Override
        public void removeNotify() {
            model.unregisterImageStatusListener(imageStatusListener);
        }

        /** Call back for when Rotate180Action is triggered */
        public void actionPerformed(ActionEvent e) {
            controller.operations.apply(new Rotate180());
        }
    }

    public class CropAction extends ImageAction {
        private ModelListener updateListener;

       public  CropAction(String name, String desc, Integer mnemonic, KeyStroke keyboardShortcut) {
            super(name, desc, mnemonic, keyboardShortcut);
            updateListener = () -> {
                setEnabled(model.hasImage() && model.tool.getSelection() != null);
            };
            model.registerImageStatusListener(updateListener);
            model.tool.registerSelectionListener(updateListener);
            updateListener.update();
        }

        @Override
        public void removeNotify() {
            model.unregisterImageStatusListener(updateListener);
            model.tool.unregisterSelectionListener(updateListener);
        }

        /** Call back for when CropAction is triggered */
        public void actionPerformed(ActionEvent e) {
            Rectangle selection = model.tool.getSelection();
            if (selection == null)
                return;
            model.tool.unsetSelection();
            controller.operations.apply(new Crop(selection.getLocation(), selection.getSize()));
        }
    }
}
