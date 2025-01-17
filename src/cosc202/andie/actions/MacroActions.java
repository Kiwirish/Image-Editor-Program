package cosc202.andie.actions;

import java.awt.event.*;
import java.util.Arrays;

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

    public ShowHideMacrosAction showHideMacrosAction;
    public RecordMacroAction recordMacroAction;
    public ApplyMacroAction applyMacroAction;


    public MacroActions(AndieController controller, AndieModel model) {
        super(msg("Macros"), controller, model);

        showHideMacrosAction = new ShowHideMacrosAction(msg("ShowHide_Title"), msg("ShowHide_Desc"), null, null);
        recordMacroAction = new RecordMacroAction(msg("RecordMacro_Title"), msg("RecordMacro_Desc"), null, null);
        applyMacroAction = new ApplyMacroAction(msg("ApplyMacro_Title"), msg("ApplyMacro_Desc"), null, null);

        actions.addAll(Arrays.asList(showHideMacrosAction, recordMacroAction, applyMacroAction));
    }

    public class ShowHideMacrosAction extends ImageAction {
        private ModelListener macrosUpdateListener;

        public  ShowHideMacrosAction(String name, String desc, Integer mnemonic, KeyStroke keyboardShortcut) {
            super(name, desc, mnemonic, keyboardShortcut);
            macrosUpdateListener = () -> {
                this.putValue(Action.NAME, model.macros.getMacrosViewOpen() ? msg("HideMacros_Panel") : msg("ShowMacros_Panel"));
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

        public RecordMacroAction(String name, String desc, Integer mnemonic, KeyStroke keyboardShortcut) {
            super(name, desc, mnemonic, keyboardShortcut);
            macrosUpdateListener = () -> {
                this.putValue(Action.NAME, model.macros.getRecording() ? msg("Stop_Recording") : msg("Start_Recording"));
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

        public ApplyMacroAction(String name, String desc, Integer mnemonic, KeyStroke keyboardShortcut) {
            super(name, desc, mnemonic, keyboardShortcut);
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
