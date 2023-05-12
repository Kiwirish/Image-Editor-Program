package cosc202.andie;

import javax.swing.*;

/**
 * <p>
 * Abstract class representing actions the user might take in the interface.
 * </p>
 * 
 * <p>
 * This class uses Java's AbstractAction approach for Actions that can be applied to images.
 * The key difference from a generic AbstractAction is that an ImageAction contains a reference
 * to an image (via an ImagePanel interface element).
 * </p>
 * 
 * <p>
 * A distinction should be made between an ImageAction and an {@link ImageOperation}.
 * An ImageOperation is applied to an image in order to change it in some way.
 * An ImageAction provides support for the user doing something to an image in the user interface.
 * ImageActions may apply an ImageOperation, but do not have to do so. 
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
public abstract class ImageAction extends AbstractAction {
   
    /**
     * <p>
     * Constructor for ImageActions.
     * </p>
     * 
     * <p>
     * To construct an ImageAction you provide the information needed to integrate it with the interface.
     * </p>
     * 
     * @param name The name of the action (ignored if null).
     * @param icon An icon to use to represent the action (ignored if null).
     * @param desc A brief description of the action  (ignored if null).
     * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
     */
    protected ImageAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
        super(name, icon);
        if (desc != null) {
           putValue(SHORT_DESCRIPTION, desc);
        }
        if (mnemonic != null) {
            putValue(MNEMONIC_KEY, mnemonic);
        }
    }
}
