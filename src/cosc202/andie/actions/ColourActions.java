package cosc202.andie.actions;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeListener;

import cosc202.andie.ImageAction;
import cosc202.andie.components.PopupSlider;
import cosc202.andie.components.OptionPopup;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.AndieModel.ModelListener;
import cosc202.andie.operations.colour.ConvertToGrey;
import cosc202.andie.operations.colour.BrightnessAndContrast;

import static cosc202.andie.LanguageConfig.msg;

/**
 * <p>
 * Actions provided by the Colour menu.
 * </p>
 * 
 * <p>
 * The Colour menu contains actions that affect the colour of each pixel
 * directly
 * without reference to the rest of the image.
 * This includes conversion to greyscale in the sample code, but more operations
 * will need to be added.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @author Jeb Nicholson
 * @author Oliver Peyroux
 * @version 2.0
 */
public class ColourActions extends MenuActions {


    private ModelListener imageStatusListener;

    /**
     * <p>
     * Create a set of Colour menu actions.
     * </p>
     * 
     * @param model
     * @param controller
     */
    public ColourActions(AndieController controller, AndieModel model) {
        super(msg("Colour_Title"), controller, model);
        actions.add(new ConvertToGreyAction(msg("ConvertToGrey_Title"), null, msg("ConvertToGrey_Desc"),
                Integer.valueOf(KeyEvent.VK_G), null));
        actions.add(new BrightnessAction(msg("Brightness_Title"), null, msg("Brightness_Desc"),
                Integer.valueOf(KeyEvent.VK_B), null));
        actions.add(new ContrastAction(msg("Contrast_Title"), null, msg("Contrast_Desc"), null, null));

        imageStatusListener = () -> {
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
     * Action to convert an image to greyscale.
     * </p>
     * 
     * @see ConvertToGrey
     */
    public class ConvertToGreyAction extends ImageAction {

        /**
         * <p>
         * Create a new convert-to-grey action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
       public ConvertToGreyAction(String name, ImageIcon icon, String desc, Integer mnemonic, KeyStroke keyboardShortcut) {
            super(name, icon, desc, mnemonic, keyboardShortcut);
        }

        /**
         * <p>
         * Callback for when the convert-to-grey action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the ConvertToGreyAction is triggered.
         * It changes the image to greyscale.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            controller.operations.apply(new ConvertToGrey());
        }

    }

    /**
     * <p>
     * Action to adjust an images brightness.
     * </p>
     * 
     * @see BrightnessAction
     */
    public class BrightnessAction extends ImageAction {

        /**
         * <p>
         * Create a new brightness adjustment action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */

       public BrightnessAction(String name, ImageIcon icon, String desc, Integer mnemonic, KeyStroke keyboardShortcut) {
            super(name, icon, desc, mnemonic, keyboardShortcut);
        }

        /**
         * <p>
         * Callback for when the brightness adjustment action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the BrightnessAction is triggered.
         * It adjusts the images brightness.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            PopupSlider slider = new PopupSlider(msg("Brightness_Popup_Label"), -100, 100, 0, "%", 10, 50,1);
            ChangeListener listener = (ev) -> {
                controller.operations.update(new BrightnessAndContrast(slider.getValue(), 0));
            };
            slider.addChangeListener(listener);
            listener.stateChanged(null);

            OptionPopup popup = new OptionPopup(controller.getContentPane(), msg("Brightness_Popup_Title"), new PopupSlider[] { slider });
            controller.operations.end(popup.show() == OptionPopup.OK);
        }
    }

    public class ContrastAction extends ImageAction {

        /**
         * <p>
         * Create a new contrast adjustment action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */

       public  ContrastAction(String name, ImageIcon icon, String desc, Integer mnemonic, KeyStroke keyboardShortcut) {
            super(name, icon, desc, mnemonic, keyboardShortcut);
        }

        /**
         * <p>
         * Callback for when the Contrast adjustment action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the ContrastAction is triggered.
         * It adjusts the images contrast.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            PopupSlider slider = new PopupSlider(msg("Contrast_Popup_Label"),-100,100,0,"%",10,50,1);
            ChangeListener listener = (ev) -> {
                controller.operations.update(new BrightnessAndContrast(0, slider.getValue()));
            };
            slider.addChangeListener(listener);
            listener.stateChanged(null);

            OptionPopup popup = new OptionPopup(controller.getContentPane(),msg("Contrast_Popup_Title"),new PopupSlider[]{slider});
            controller.operations.end(popup.show() == OptionPopup.OK);
        }

    }

}

