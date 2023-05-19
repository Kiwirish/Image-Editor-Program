package cosc202.andie.actions;

import java.awt.event.*;
import javax.swing.*;

//import cosc202.andie.EditableImage;
import cosc202.andie.ImageAction;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.AndieModel.ModelListener;

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
public class MacroActions extends MenuActions {

    public MacroActions(AndieController controller, AndieModel model) {
        super("Macros", controller, model);
        actions.add(new ShowHideMacrosAction("Show Macros Panel", null, "Show or hide the macros panel", null));
        actions.add(new RecordMacroAction("Start Recording", null, "Start or Stop recording a macro", null));
        actions.add(new ApplyMacroAction("Apply a Macro", null, "Apply a macro from a file to the image", null));
    }

    public class ShowHideMacrosAction extends ImageAction {
        private ModelListener macrosUpdateListener;

        public  ShowHideMacrosAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
            macrosUpdateListener = () -> {
                this.putValue(Action.NAME, model.macros.getMacrosViewOpen() ? "Hide Macros Panel" : "Show Macros Panel");
            };
            model.macros.registerMacrosUpdateListener(macrosUpdateListener);
            macrosUpdateListener.update();
        }

        @Override
        public void removeNotify() {
            model.macros.unregisterMacrosUpdateListener(macrosUpdateListener);
        }

        public void actionPerformed(ActionEvent e) {
            controller.macros.setMacrosViewOpen(!model.macros.getMacrosViewOpen());
        }
    }

    public class RecordMacroAction extends ImageAction {
        private ModelListener macrosUpdateListener;
        private ModelListener imageStatusListener;

        public RecordMacroAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
            macrosUpdateListener = () -> {
                this.putValue(Action.NAME, model.macros.getRecording() ? "Stop Recording" : "Start Recording");
            };
            imageStatusListener = () -> {
                this.setEnabled(model.hasImage());
            };
            model.macros.registerMacrosUpdateListener(macrosUpdateListener);
            model.registerImageStatusListener(imageStatusListener);
            macrosUpdateListener.update();
            imageStatusListener.update();
        }

        @Override
        public void removeNotify() {
            model.macros.unregisterMacrosUpdateListener(macrosUpdateListener);
            model.unregisterImageStatusListener(imageStatusListener);
        }

        public void actionPerformed(ActionEvent e) {
            if (model.macros.getRecording()) {
                controller.macros.stopMacroRecording();
            } else {
                controller.macros.startMacroRecording();
            }
        }
    }

    public class ApplyMacroAction extends ImageAction {
        private ModelListener imageStatusListener;

        public ApplyMacroAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
            imageStatusListener = () -> {
                this.setEnabled(model.hasImage());
            };
            model.registerImageStatusListener(imageStatusListener);
            imageStatusListener.update();
        }

        @Override
        public void removeNotify() {
            model.unregisterImageStatusListener(imageStatusListener);
        }

        public void actionPerformed(ActionEvent e) {
            controller.macros.applyExistingMacro();
        }
    }

}
