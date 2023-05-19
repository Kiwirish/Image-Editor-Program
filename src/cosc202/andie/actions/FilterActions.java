package cosc202.andie.actions;

import java.awt.event.*;
import javax.swing.*;

import cosc202.andie.ImageAction;
import cosc202.andie.components.PopupSlider;
import cosc202.andie.components.PopupWithSliders;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.AndieModel.ModelListener;
import cosc202.andie.operations.filter.GaussianBlur;
import cosc202.andie.operations.filter.MeanFilter;
import cosc202.andie.operations.filter.MedianFilter;
import cosc202.andie.operations.filter.NegativeResults;
import cosc202.andie.operations.filter.SharpenFilter;

import static cosc202.andie.LanguageConfig.msg;

/**
 * <p>
 * Actions provided by the Filter menu.
 * </p>
 * 
 * <p>
 * The Filter menu contains actions that update each pixel in an image based on
 * some small local neighbourhood. 
 * This includes a mean filter (a simple blur) in the sample code, but more operations will need to be added.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @author Jeb Nicholson
 * @author Blake Leahy
 * @version 2.0
 */
public class FilterActions extends MenuActions {

    private ModelListener imageStatusListener;
    
    /**
     * <p>
     * Create a set of Filter menu actions.
     * </p>
     * @param model
     * @param controller
     */
    public FilterActions(AndieController controller, AndieModel model) {
        super(msg("Filter_Title"), controller, model);
        actions.add(new SharpenFilterAction(msg("SharpenFilter_Title"), null, msg("SharpenFilter_Desc"), Integer.valueOf(KeyEvent.VK_S)));
        actions.add(new GaussianBlurFilterAction(msg("GaussianBlurFilter_Title"), null, msg("GaussianBlurFilter_Desc"), Integer.valueOf(KeyEvent.VK_G)));
        actions.add(new MedianFilterAction(msg("MedianFilter_Title"), null, msg("MedianFilter_Desc"), Integer.valueOf(KeyEvent.VK_E)));
        actions.add(new MeanFilterAction(msg("MeanFilter_Title"), null, msg("MeanFilter_Desc"), Integer.valueOf(KeyEvent.VK_M)));
        actions.add(new NegativeFilterAction("Negative Filter", null, null, null));
        // need to add an emboss menu to select N,E,S,W Emboss filters 
        // need to add a sobel filters menu to select Horizontal or Vertical edge detection filters

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
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
       public  MeanFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the mean-filter action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the MeanFilterAction is triggered.
         * It prompts the user for a filter radius, then applys an appropriately sized {@link MeanFilter}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            PopupSlider slider = new PopupSlider(msg("Radius_Popup_Label"),1,10,1,"px",1,5);
            slider.addChangeListener((ev)->{
                controller.operations.update(new MeanFilter(slider.getValue()));
            });
            PopupWithSliders popup = new PopupWithSliders(controller.getContentPane(),msg("MeanFilter_Popup_Title"),new PopupSlider[]{slider});
            controller.operations.end(popup.show() == PopupWithSliders.OK);
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
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
       public  SharpenFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the SharpenFilterAction is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the SharpenFilterAction is triggered.
         * It prompts the user for a filter radius, then applys an appropriately sized {@link SharpenFilter}.
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
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
       public  GaussianBlurFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the gaussian-blur action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the GaussianBlurFilterAction is triggered.
         * It prompts the user for a filter radius, then applys an appropriately sized {@link GaussianBlur}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            PopupSlider slider = new PopupSlider(msg("Radius_Popup_Label"),2,10,2,"px",1,5);
            slider.addChangeListener((ev)->{
                controller.operations.update(new GaussianBlur(slider.getValue()));
            });
            PopupWithSliders popup = new PopupWithSliders(controller.getContentPane(), msg("GaussianBlurFilter_Popup_Title"),new PopupSlider[]{slider});
            controller.operations.end(popup.show() == PopupWithSliders.OK);
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
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
       public  MedianFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the median-filter action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the MedianFilterAction is triggered.
         * It prompts the user for a filter radius, then applys an appropriately sized {@link MedianFilter}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            PopupSlider slider = new PopupSlider(msg("Radius_Popup_Label"),1,5,1,"px",1,5);
            slider.addChangeListener((ev)->{
                controller.operations.update(new MedianFilter(slider.getValue()));
            });
            PopupWithSliders popup = new PopupWithSliders(controller.getContentPane(),msg("MedianFilter_Popup_Title"),new PopupSlider[]{slider});
            controller.operations.end(popup.show() == PopupWithSliders.OK);
        }

    }
        /**
    * <p>
    * Action to apply SharpenFilter filter 
    * </p>
    * 
    * @see SharpenFilter
    */
    public class NegativeFilterAction extends ImageAction {

        /**
         * <p>
         * Create a new sharpen-filter action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
       public  NegativeFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the SharpenFilterAction is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the SharpenFilterAction is triggered.
         * It prompts the user for a filter radius, then applys an appropriately sized {@link SharpenFilter}.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            controller.operations.apply(new NegativeResults());
        }

    }
    
    public class EmbossFilterAction extends ImageAction { 
        

       public  EmbossFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

       

        public void actionPerformed(ActionEvent e) {

            float[] kernel = new float[9]; 
        
            if(e.getActionCommand().equals("N Emboss")){
                kernel[1] = 1;
                kernel[7] = -1;
            }else if(e.getActionCommand().equals("E Emboss")){
                kernel[3] = -1;
                kernel[5] = 1;
            }else if(e.getActionCommand().equals("S Emboss")){
                kernel[1] = -1;
                kernel[7] = 1;
            }else if(e.getActionCommand().equals("W Emboss")){    
                kernel[3] = 1;
                kernel[5] = -1;
            }else if(e.getActionCommand().equals("NW Emboss")){ 
                kernel[0] = 1;
                kernel[8] = -1;
            }else if(e.getActionCommand().equals("NE Emboss")){ 
                kernel[2] = 1;
                kernel[6] = -1;
            }else if(e.getActionCommand().equals("SE Emboss")){ 
                kernel[0] = -1;
                kernel[8] = 1;
            }else if(e.getActionCommand().equals("SW Emboss")){ 
                kernel[2] = -1;
                kernel[6] = 1;
            }else if(e.getActionCommand().equals("Horizontal")){ 
                kernel[0] = -1/2;
                kernel[2] = 1/2;
                kernel[3] = -1;
                kernel[5] = 1;
                kernel[6] = -1/2;
                kernel[8] = 1/2;
            }else if(e.getActionCommand().equals("Vertical")){ 
                kernel[0] = -1/2;
                kernel[1] = -1;
                kernel[2] = -1/2;
                kernel[6] = 1/2;
                kernel[7] = 1;
                kernel[8] = 1/2;
            }
        }






        //     PopupSlider slider = new PopupSlider(msg("Radius_Popup_Label"),1,5,1,"px",1,5);
        //     slider.addChangeListener((ev)->{
        //         controller.operations.update(new EmbossFilter(kernel));
        //     });
        //     PopupWithSliders popup = new PopupWithSliders(controller.getContentPane(),msg("MedianFilter_Popup_Title"),new PopupSlider[]{slider});
        //     controller.operations.end(popup.show() == PopupWithSliders.OK);
        
    }

}