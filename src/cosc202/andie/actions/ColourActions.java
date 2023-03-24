package cosc202.andie.actions;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

import cosc202.andie.ImageAction;
import cosc202.andie.operations.colour.ConvertToGrey;
import cosc202.andie.operations.colour.BrightnessAndContrast;

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
        actions.add(new ConvertToGreyAction("Greyscale", null, "Convert to greyscale ('G')", Integer.valueOf(KeyEvent.VK_G))); 
        actions.add(new BrightnessAndContrastAdjustment("BrightnessAndContrastAdjustment", null,"Adjust Image Brightness and Contrrast ('B')", Integer.valueOf(KeyEvent.VK_B)));
    }

    /**
     * <p>
     * Create a menu contianing the list of Colour actions.
     * </p>
     * 
     * @return The colour menu UI element.
     */
    public JMenu createMenu() {
        JMenu fileMenu = new JMenu("Colour");

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
     * @see BrightnessAndContrastAdjustment
     */
    public class BrightnessAndContrastAdjustment extends ImageAction {

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
        private int brightness;
        private int contrast;


        BrightnessAndContrastAdjustment(String name, ImageIcon icon, String desc, Integer mnemonic) {
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


            SpinnerNumberModel brightnessModel = new SpinnerNumberModel(brightness, -100, 100, 25);
            SpinnerNumberModel contrastModel = new SpinnerNumberModel(contrast, -100, 100, 25);
            JSpinner brightnessSpinner = new JSpinner(brightnessModel);
            JSpinner contrastSpinner = new JSpinner(contrastModel);
            Object[] arrayOfShit = new Object[]{brightnessSpinner, contrastSpinner};
            int option = JOptionPane.showOptionDialog(null, "                Brightness       Contrast", "Brightness and Constrast Adjustment", 
            JOptionPane.OK_CANCEL_OPTION, 
            //JOptionPane.QUESTION_MESSAGE, null, arrayOfShit, null);
            JOptionPane.PLAIN_MESSAGE, null, arrayOfShit, null);

            // Check the return value from the dialog box.
            
            //need to update this so their is a OK and CANCEL option?????
            if (option == JOptionPane.CLOSED_OPTION) {

                brightness = brightnessModel.getNumber().intValue();
                contrast = contrastModel.getNumber().intValue();
                System.out.println(brightness);
                System.out.println(contrast);
                return;
            } 
            target.getImage().apply(new BrightnessAndContrast(brightness,contrast)); 
            target.repaint();
            target.getParent().revalidate();
        }

    }

}
