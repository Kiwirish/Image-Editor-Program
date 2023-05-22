package cosc202.andie.actions;

import java.awt.event.*;
import javax.swing.*;

import cosc202.andie.ImageAction;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.AndieModel.ModelListener;

import static cosc202.andie.LanguageConfig.msg;

/**
 * <p>
 * Actions provided by the View menu.
 * </p>
 * 
 * <p>
 * The View menu contains actions that affect how the image is displayed in the application.
 * These actions do not affect the contents of the image itself, just the way it is displayed.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @author Jeb Nicholson
 * @version 2.0
 */
public class ViewActions extends MenuActions {

    private ModelListener imageStatusListener;

    /**
     * <p>
     * Create a set of View menu actions.
     * </p>
     * @param model
     * @param controller
     */
    public ViewActions(AndieController controller, AndieModel model) {
        super(msg("View_Title"), controller, model);
        actions.add(new ZoomInAction(msg("ZoomIn_Title"), null, msg("ZoomIn_Desc"), Integer.valueOf(KeyEvent.VK_PLUS), null));
        actions.add(new ZoomOutAction(msg("ZoomOut_Title"), null, msg("ZoomOut_Desc"), Integer.valueOf(KeyEvent.VK_MINUS), null));
        actions.add(new ResetZoomAction(msg("ZoomReset_Title"), null, msg("ZoomReset_Desc"), Integer.valueOf(KeyEvent.VK_2), null));

        imageStatusListener = ()-> {
            for (ImageAction action : actions) {
                action.setEnabled(model.hasImage());
            }
        };
        model.registerImageStatusListener(imageStatusListener);
        imageStatusListener.update();
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        model.unregisterImageStatusListener(imageStatusListener);
    }

    /**
     * <p>
     * Action to zoom in on an image.
     * </p>
     * 
     * <p>
     * Note that this action only affects the way the image is displayed, not its actual contents.
     * </p>
     */
    public class ZoomInAction extends ImageAction {

        /**
         * <p>
         * Create a new zoom-in action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
       public  ZoomInAction(String name, ImageIcon icon, String desc, Integer mnemonic, KeyStroke keyboardShortcut) {
            super(name, icon, desc, mnemonic, keyboardShortcut);
        }

        /**
         * <p>
         * Callback for when the zoom-in action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the ZoomInAction is triggered.
         * It increases the zoom level by 10%, to a maximum of 200%.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            controller.zoomIn();
        }
    }

    /**
     * <p>
     * Action to zoom out of an image.
     * </p>
     * 
     * <p>
     * Note that this action only affects the way the image is displayed, not its actual contents.
     * </p>
     */
    public class ZoomOutAction extends ImageAction {

        /**
         * <p>
         * Create a new zoom-out action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
       public  ZoomOutAction(String name, ImageIcon icon, String desc, Integer mnemonic, KeyStroke keyboardShortcut) {
            super(name, icon, desc, mnemonic, keyboardShortcut);
        }

        /**
         * <p>
         * Callback for when the zoom-iout action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the ZoomOutAction is triggered.
         * It decreases the zoom level by 10%, to a minimum of 50%.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            controller.zoomOut();
        }
    }

    /**
     * <p>
     * Action to reset the zoom level to fill the frame
     * </p>
     * 
     * <p>
     * Note that this action only affects the way the image is displayed, not its actual contents.
     * </p>
     */
    public class ResetZoomAction extends ImageAction {

        /**
         * <p>
         * Create a new reset-zoom action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
       public  ResetZoomAction(String name, ImageIcon icon, String desc, Integer mnemonic, KeyStroke keyboardShortcut) {
            super(name, icon, desc, mnemonic, keyboardShortcut);
        }

        /**
         * <p>
         * Callback for when the reset-zoom action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the ResetZoomAction is triggered.
         * It resets the Zoom level to fill the panel.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            controller.resetZoom();
        }
    }

}
