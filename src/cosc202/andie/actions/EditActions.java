package cosc202.andie.actions;

import java.awt.event.*;
import javax.swing.*;

import cosc202.andie.ImageAction;
import cosc202.andie.ImageOperation;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.EditableImage;
import cosc202.andie.models.AndieModel.ModelListener;

import static cosc202.andie.LanguageConfig.msg;
import static cosc202.andie.models.AndieModel.COMTROL;

 /**
 * <p>
 * Actions provided by the Edit menu.
 * </p>
 * 
 * <p>
 * The Edit menu is very common across a wide range of applications.
 * There are a lot of operations that a user might expect to see here.
 * In the sample code there are Undo and Redo actions, but more may need to be added.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @author Jeb Nicholson
 * @version 1.0
 */
public class EditActions extends MenuActions{
    
    /**
     * <p>
     * Create a set of Edit menu actions.
     * </p>
     * @param model
     * @param controller
     */
    public EditActions(AndieController controller, AndieModel model) {
        super(msg("Edit_Title"), controller, model);
        actions.add(new UndoAction(msg("Undo_Title"), null, msg("Undo_Desc"), Integer.valueOf(KeyEvent.VK_Z), KeyStroke.getKeyStroke(KeyEvent.VK_Z, COMTROL)));
        actions.add(new RedoAction(msg("Redo_Title"), null, msg("Redo_Desc"), Integer.valueOf(KeyEvent.VK_Y), KeyStroke.getKeyStroke(KeyEvent.VK_Y, COMTROL)));
    }

    /**
     * <p>
     * Action to undo an {@link ImageOperation}.
     * </p>
     * 
     * @see EditableImage#undo()
     */
    public class UndoAction extends ImageAction {

        private ModelListener imageListener;

        /**
         * <p>
         * Create a new undo action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
       public  UndoAction(String name, ImageIcon icon, String desc, Integer mnemonic, KeyStroke keyboardShortcut) {
            super(name, icon, desc, mnemonic, keyboardShortcut);
            imageListener = () -> {
                setEnabled(model.hasImage() && model.getImage().undoable());
            };
            model.registerImageListener(imageListener);   
            imageListener.update();
        }

        public void removeNotify() {
            model.unregisterImageListener(imageListener);
        }

        /**
         * <p>
         * Callback for when the undo action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the UndoAction is triggered.
         * It undoes the most recently applied operation.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            controller.undo();
        }
    }

     /**
     * <p>
     * Action to redo an {@link ImageOperation}.
     * </p>
     * 
     * @see EditableImage#redo()
     */   
    public class RedoAction extends ImageAction {

        private ModelListener imageListener;

        /**
         * <p>
         * Create a new redo action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
       public  RedoAction(String name, ImageIcon icon, String desc, Integer mnemonic, KeyStroke keyboardShortcut) {
            super(name, icon, desc, mnemonic, keyboardShortcut);
            imageListener = () -> {
                setEnabled(model.hasImage() && model.getImage().redoable());
            };
            model.registerImageListener(imageListener);
            imageListener.update();
        }

        @Override
        public void removeNotify() {
            model.unregisterImageListener(imageListener);
        }

        
        /**
         * <p>
         * Callback for when the redo action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the RedoAction is triggered.
         * It redoes the most recently undone operation.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            controller.redo();
        }
    }

}
