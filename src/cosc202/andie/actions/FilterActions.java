package cosc202.andie.actions;

import java.awt.event.*;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import cosc202.andie.ImageAction;
import cosc202.andie.components.PopupSlider;
import cosc202.andie.components.OptionPopup;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.AndieModel.ModelListener;
import cosc202.andie.operations.filter.GaussianBlur;
import cosc202.andie.operations.filter.MeanFilter;
import cosc202.andie.operations.filter.MedianFilter;
import cosc202.andie.operations.filter.SharpenFilter;
import cosc202.andie.operations.filter.SobelFilter;
import cosc202.andie.operations.filter.EmbossFilter;

import static cosc202.andie.LanguageConfig.msg;

/**
 * <p>
 * Actions provided by the Filter menu.
 * </p>
 * 
 * <p>
 * The Filter menu contains actions that update each pixel in an image based on
 * some small local neighbourhood.
 * This includes a mean filter (a simple blur) in the sample code, but more
 * operations will need to be added.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @author Jeb Nicholson
 * @author Blake Leahy
 * @version 2.0
 */
public class FilterActions extends MenuActions {

    private ModelListener imageStatusListener;

    public SharpenFilterAction sharpenFilterAction;
    public GaussianBlurFilterAction gaussianBlurFilterAction;
    public MedianFilterAction medianFilterAction;
    public MeanFilterAction meanFilterAction;
    public EmbossFilterAction embossFilterAction;
    public SobelFilterAction sobelFilterAction;

    /**
     * <p>
     * Create a set of Filter menu actions.
     * </p>
     * 
     * @param model
     * @param controller
     */
    public FilterActions(AndieController controller, AndieModel model) {
        super(msg("Filter_Title"), controller, model);

        sharpenFilterAction = new SharpenFilterAction(msg("SharpenFilter_Title"), msg("SharpenFilter_Desc"), Integer.valueOf(KeyEvent.VK_S), null);
        gaussianBlurFilterAction = new GaussianBlurFilterAction(msg("GaussianBlurFilter_Title"), msg("GaussianBlurFilter_Desc"), Integer.valueOf(KeyEvent.VK_G), null);
        medianFilterAction = new MedianFilterAction(msg("MedianFilter_Title"), msg("MedianFilter_Desc"), Integer.valueOf(KeyEvent.VK_D), null);
        meanFilterAction = new MeanFilterAction(msg("MeanFilter_Title"), msg("MeanFilter_Desc"), Integer.valueOf(KeyEvent.VK_M), null);
        embossFilterAction = new EmbossFilterAction(msg("EmbossFilter_Title"), msg("EmbossFilter_Desc"), Integer.valueOf(KeyEvent.VK_E), null);
        sobelFilterAction = new SobelFilterAction(msg("SobelFilter_Title"), msg("SobelFilter_Desc"), Integer.valueOf(KeyEvent.VK_B), null);

        actions.addAll(Arrays.asList(sharpenFilterAction, gaussianBlurFilterAction, medianFilterAction,
                meanFilterAction, embossFilterAction, sobelFilterAction));

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
     * Action to blur an image with a mean filter.
     * </p>
     * 
     * @see MeanFilter
     */
    public class MeanFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new mean-filter action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        public MeanFilterAction(String name, String desc, Integer mnemonic,
                KeyStroke keyboardShortcut) {
            super(name, desc, mnemonic, keyboardShortcut);
        }

        /**
         * <p>
         * Callback for when the mean-filter action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the MeanFilterAction is triggered.
         * It prompts the user for a filter radius, then applys an appropriately sized
         * {@link MeanFilter}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            PopupSlider slider = new PopupSlider(msg("Radius_Popup_Label"), 1, 10, 1, "px", 1, 5, 1);
            ChangeListener listener = ((ev) -> {
                controller.operations.update(new MeanFilter(slider.getValue()));
            });
            slider.addChangeListener(listener);
            listener.stateChanged(null);

            OptionPopup popup = new OptionPopup(controller.getContentPane(), msg("MeanFilter_Popup_Title"),
                    new PopupSlider[] { slider });
            controller.operations.end(popup.show() == OptionPopup.OK);
        }

    }

    /**
     * <p>
     * Action to apply SharpenFilter filter
     * </p>
     * 
     * @see SharpenFilter
     */
    public class SharpenFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new sharpen-filter action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        public SharpenFilterAction(String name, String desc, Integer mnemonic,
                KeyStroke keyboardShortcut) {
            super(name, desc, mnemonic, keyboardShortcut);
        }

        /**
         * <p>
         * Callback for when the SharpenFilterAction is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the SharpenFilterAction is triggered.
         * It prompts the user for a filter radius, then applys an appropriately sized
         * {@link SharpenFilter}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            controller.operations.apply(new SharpenFilter());
        }

    }

    /**
     * <p>
     * Action to apply GaussianBlur filter
     * </p>
     * 
     * @see GaussianBlur
     */
    public class GaussianBlurFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new GaussianBlur-filter action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        public GaussianBlurFilterAction(String name, String desc, Integer mnemonic,
                KeyStroke keyboardShortcut) {
            super(name, desc, mnemonic, keyboardShortcut);
        }

        /**
         * <p>
         * Callback for when the gaussian-blur action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the GaussianBlurFilterAction is triggered.
         * It prompts the user for a filter radius, then applys an appropriately sized
         * {@link GaussianBlur}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            PopupSlider slider = new PopupSlider(msg("Radius_Popup_Label"), 2, 10, 2, "px", 1, 5, 1);
            ChangeListener listener = (ev) -> {
                controller.operations.update(new GaussianBlur(slider.getValue()));
            };
            slider.addChangeListener(listener);
            listener.stateChanged(null);

            OptionPopup popup = new OptionPopup(controller.getContentPane(), msg("GaussianBlurFilter_Popup_Title"),
                    new PopupSlider[] { slider });
            controller.operations.end(popup.show() == OptionPopup.OK);
        }
    }

    /**
     * <p>
     * Action to apply median filter
     * </p>
     * 
     * @see MedianFilter
     */
    public class MedianFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new meidan-filter action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
        public MedianFilterAction(String name, String desc, Integer mnemonic,
                KeyStroke keyboardShortcut) {
            super(name, desc, mnemonic, keyboardShortcut);
        }

        /**
         * <p>
         * Callback for when the median-filter action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the MedianFilterAction is triggered.
         * It prompts the user for a filter radius, then applys an appropriately sized
         * {@link MedianFilter}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            PopupSlider slider = new PopupSlider(msg("Radius_Popup_Label"), 1, 5, 1, "px", 1, 5, 1);
            ChangeListener listener = (ev) -> {
                controller.operations.update(new MedianFilter(slider.getValue()));
            };
            slider.addChangeListener(listener);
            listener.stateChanged(null);

            OptionPopup popup = new OptionPopup(controller.getContentPane(), msg("MedianFilter_Popup_Title"),
                    new PopupSlider[] { slider });
            controller.operations.end(popup.show() == OptionPopup.OK);
        }

    }

    public class EmbossFilterAction extends ImageAction {

        public EmbossFilterAction(String name, String desc, Integer mnemonic,
                KeyStroke keyboardShortcut) {
            super(name, desc, mnemonic, keyboardShortcut);
        }

        public void actionPerformed(ActionEvent e) {
            PopupSlider slider = new PopupSlider(msg("EmbossFilter_Popup_Msg"), 45, 360, 45, "˚", 45, 45, 45);
            ChangeListener listener = (ev) -> {
                controller.operations.update(new EmbossFilter(slider.getValue()));
            };
            slider.addChangeListener(listener);
            listener.stateChanged(null);

            OptionPopup popup = new OptionPopup(controller.getContentPane(), msg("EmbossFilter_Popup_Title"),
                    new PopupSlider[] { slider });
            controller.operations.end(popup.show() == OptionPopup.OK);
        }

    }

    public class SobelFilterAction extends ImageAction {

        public SobelFilterAction(String name, String desc, Integer mnemonic,
                KeyStroke keyboardShortcut) {
            super(name, desc, mnemonic, keyboardShortcut);
        }

        public void actionPerformed(ActionEvent e) {
            JCheckBox checkBox = new JCheckBox(msg("SobelFilter_Popup_Msg"));
            ChangeListener listener = new ChangeListener() {
                Boolean lastValue = null;

                public void stateChanged(ChangeEvent e) {
                    if (lastValue == null || lastValue != checkBox.isSelected()) { // Checkboxes need to be debounced
                                                                                   // for some godforsaken reason
                        lastValue = checkBox.isSelected();
                        controller.operations.update(new SobelFilter(checkBox.isSelected()));
                    }
                }
            };
            checkBox.addChangeListener(listener);
            listener.stateChanged(null);

            OptionPopup popup = new OptionPopup(controller.getContentPane(), msg("SobelFilter_Popup_Title"),
                    new JComponent[] { checkBox });
            controller.operations.end(popup.show() == OptionPopup.OK);
        }

    }

}