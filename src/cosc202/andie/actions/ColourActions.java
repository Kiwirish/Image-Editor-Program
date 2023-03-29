package cosc202.andie.actions;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

import cosc202.andie.ImageAction;
import cosc202.andie.operations.colour.ConvertToGrey;
import cosc202.andie.operations.colour.BrightnessAndContrast;

import static cosc202.andie.LanguageConfig.msg;

/**
 * <p>
 * Actions provided by the Colour menu.
 * </p>
 * 
 * <p>
 * The Colour menu contains actions that affect the colour of each pixel directly 
 * without reference to the rest of the image.
 * This includes conversion to greyscale in the sample code, but more operations will need to be added.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class ColourActions {
    
    /** A list of actions for the Colour menu. */
    protected ArrayList<Action> actions;

    /**
     * <p>
     * Create a set of Colour menu actions.
     * </p>
     */
    public ColourActions() {
        actions = new ArrayList<Action>();
        actions.add(new ConvertToGreyAction(msg("ConvertToGrey_Title"), null, msg("ConvertToGrey_Desc"), Integer.valueOf(KeyEvent.VK_G))); 
        actions.add(new BrightnessAction(msg("Brightness_Title"), null,msg("Brightness_Desc"), Integer.valueOf(KeyEvent.VK_B)));
        actions.add(new ContrastAction(msg("Contrast_Title"), null,msg("Contrast_Desc"), null));
    }

    /**
     * <p>
     * Create a menu contianing the list of Colour actions.
     * </p>
     * 
     * @return The colour menu UI element.
     */
    public JMenu createMenu() {
        JMenu fileMenu = new JMenu(msg("Colour_Title"));

        for(Action action: actions) {
            fileMenu.add(new JMenuItem(action));
        }

        return fileMenu;
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
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        ConvertToGreyAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
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
            target.getImage().apply(new ConvertToGrey());
            target.repaint();
            target.getParent().revalidate();
        }

    }

    /**
     * <p>
     * Action to adjust an images brightness and contrast.
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
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */

        BrightnessAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
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

            int brightness = 0;

            SpinnerNumberModel brightnessModel = new SpinnerNumberModel(0, -100, 100, 1);
            JSpinner brightnessSpinner = new JSpinner(brightnessModel);
            int brightnessOption = JOptionPane.showOptionDialog(null, brightnessSpinner, msg("Brightness_Action"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null); 
            
            if (brightnessOption == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (brightnessOption == JOptionPane.OK_OPTION) {
                brightness = brightnessModel.getNumber().intValue();
            }

            target.getImage().apply(new BrightnessAndContrast(brightness,0)); 
            target.repaint();
            target.getParent().revalidate();
        }

    }
    public class ContrastAction extends ImageAction {

        /**
         * <p>
         * Create a new brightness adjustment action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */

        ContrastAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
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

            int contrast = 0;

            SpinnerNumberModel contrastModel = new SpinnerNumberModel(0, -100, 100, 1);
            JSpinner contrastSpinner = new JSpinner(contrastModel);
            int contrastOption = JOptionPane.showOptionDialog(null, contrastSpinner, msg("Contrast_Action"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null); 
            
            if (contrastOption == JOptionPane.CANCEL_OPTION) {
                return;
            } else if (contrastOption == JOptionPane.OK_OPTION) {
                contrast = contrastModel.getNumber().intValue();
            }

            target.getImage().apply(new BrightnessAndContrast(0,contrast)); 
            target.repaint();
            target.getParent().revalidate();
        }
    }

}
