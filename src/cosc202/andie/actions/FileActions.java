package cosc202.andie.actions;

import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import cosc202.andie.ImageAction;
import cosc202.andie.Utils;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.AndieModel.ModelListener;

import javax.imageio.*;

import static cosc202.andie.LanguageConfig.msg;

/**
 * <p>
 * Actions provided by the File menu.
 * </p>
 * 
 * <p>
 * The File menu is very common across applications,
 * and there are several items that the user will expect to find here.
 * Opening and saving files is an obvious one, but also exiting the program.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @author Jeb Nicholson
 * @version 2.0
 */
public class FileActions extends MenuActions {

    public FileActions(AndieController controller, AndieModel model) {
        super(msg("File_Title"), controller, model);

        actions.add(new FileOpenAction(msg("File_Open"), null, msg("File_Open_Desc"), Integer.valueOf(KeyEvent.VK_O)));
        actions.add(new FileSaveAction(msg("File_Save"), null, msg("File_Save_Desc"), Integer.valueOf(KeyEvent.VK_S)));
        actions.add(new FileSaveAsAction(msg("File_Save_As"), null, msg("File_Save_As_Desc"), Integer.valueOf(KeyEvent.VK_A)));
        actions.add(new FileExportAction(msg("File_Export"), null, msg("File_Export_Desc"), Integer.valueOf(KeyEvent.VK_E)));
        actions.add(new FileCloseImageAction(msg("File_Close_Image"), null, msg("File_Close_Image_Desc"), Integer.valueOf(0)));
        actions.add(new FileExitAction(msg("File_Exit"), null, msg("File_Exit_Desc"), Integer.valueOf(0)));
    }

    /**
     * <p>
     * Action to open an image from file.
     * </p>
     * 
     */
    public class FileOpenAction extends ImageAction {

        /**
         * <p>
         * Create a new file-open action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
       public  FileOpenAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the file-open action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileOpenAction is triggered.
         * It prompts the user to select a file and opens it as an image.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();

            // Only allow files with image extensions that ImageIO can parse to be opened
            FileNameExtensionFilter filter = new FileNameExtensionFilter(msg("File_filter"), ImageIO.getReaderFileSuffixes());
            fileChooser.setFileFilter(filter);
            int result = fileChooser.showOpenDialog(null);

            if (result != JFileChooser.APPROVE_OPTION)
                return;
            String imageFilepath = fileChooser.getSelectedFile().getAbsolutePath();

            if (!controller.IO.safeClose())
                return; // Close last image before opening a new one
            controller.IO.safeOpen(imageFilepath);

        }

    }

    /**
     * <p>
     * Action to save an image to its current file location.
     * </p>
     * 
     */
    public class FileSaveAction extends ImageAction {

        private ModelListener imageListener;

        /**
         * <p>
         * Create a new file-save action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
       public  FileSaveAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
            imageListener = ()-> {
                setEnabled(model.hasImage() && model.getImage().getModified());
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
         * Callback for when the file-save action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileSaveAction is triggered.
         * It saves the image to its original filepath.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            controller.IO.save();
        }

    }

    /**
     * <p>
     * Action to save an image to a new file location.
     * </p>
     * 
     */
    public class FileSaveAsAction extends ImageAction {

        private ModelListener imageStatusListener;

        /**
         * <p>
         * Create a new file-save-as action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
       public  FileSaveAsAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
            imageStatusListener = ()-> {
                setEnabled(model.hasImage());
            };
            model.registerImageStatusListener(imageStatusListener);
            imageStatusListener.update();
        }

        @Override
        public void removeNotify() {
            model.unregisterImageStatusListener(imageStatusListener);
        }

        /**
         * <p>
         * Callback for when the file-save-as action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileSaveAsAction is triggered.
         * It prompts the user to select a file and saves the image to it.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            FileNameExtensionFilter filter = new FileNameExtensionFilter(msg("File_filter"),
                    ImageIO.getWriterFileSuffixes());
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(filter);

            String filepath = model.getImageFilepath();
            if (filepath != null)
                fileChooser.setSelectedFile(new File(filepath));

            int result = fileChooser.showSaveDialog(null);
            if (result != JFileChooser.APPROVE_OPTION)
                return;
            String imageFilepath = fileChooser.getSelectedFile().getAbsolutePath();

            controller.IO.saveAs(imageFilepath);
        }
    }

    /**
     * <p>
     * Action to close the current image
     * </p>
     */
    public class FileCloseImageAction extends ImageAction {

		private ModelListener imageStatusListener;

        /**
         * <p>
         * Create a new file-close-image action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
       public  FileCloseImageAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
            imageStatusListener = ()-> {
                setEnabled(model.hasImage());
            };
            model.registerImageStatusListener(imageStatusListener);
            imageStatusListener.update();
        }

        @Override
        public void removeNotify() {
            model.unregisterImageStatusListener(imageStatusListener);
        }

        /**
         * <p>
         * Callback for when the file-close-image action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileCloseImageAction is triggered.
         * It closes the current image.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            controller.IO.safeClose();
        }
    }

    /**
     * <p>
     * Action to quit the ANDIE application.
     * </p>
     */
    public class FileExitAction extends ImageAction {

        /**
         * <p>
         * Create a new file-exit action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
       public  FileExitAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the file-exit action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileExitAction is triggered.
         * It quits the program.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (controller.IO.safeClose()) {
                System.exit(0);
            }
        }
    }

    /**
     * <p>
     * Action to export the image
     * </p>
     */
    public class FileExportAction extends ImageAction {

		private ModelListener imageStatusListener;

        /**
         * <p>
         * Create a new file-export action.
         * </p>
         * 
         * @param name     The name of the action (ignored if null).
         * @param icon     An icon to use to represent the action (ignored if null).
         * @param desc     A brief description of the action (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
         */
       public  FileExportAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
            imageStatusListener = ()-> {
                setEnabled(model.hasImage());
            };
            model.registerImageStatusListener(imageStatusListener);
            imageStatusListener.update();
        }

		@Override
		public void removeNotify() {
			model.unregisterImageStatusListener(imageStatusListener);
		}

        /**
         * <p>
         * Callback for when the file-export action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the FileExportAction is triggered.
         * It shows an export dialog box, and exports the image.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            if (!model.hasImage())
                return;

            String[] writerFormatNames = { "png", "jpg", "gif", "bmp", "tiff" };

            JPanel panel = new JPanel();
            JComboBox<String> imageFormatChooser = new JComboBox<String>(writerFormatNames);
            JLabel label = new JLabel(msg("File_Exit_Action_label"));
            SpringLayout layout = new SpringLayout();
            panel.setLayout(layout);
            panel.add(label);
            panel.add(imageFormatChooser);

            layout.putConstraint(SpringLayout.WEST, label, 5, SpringLayout.WEST, panel);
            layout.putConstraint(SpringLayout.NORTH, label, 5, SpringLayout.NORTH, panel);
            layout.putConstraint(SpringLayout.NORTH, imageFormatChooser, 10, SpringLayout.SOUTH, label);
            layout.putConstraint(SpringLayout.WEST, imageFormatChooser, 0, SpringLayout.WEST, label);
            layout.putConstraint(SpringLayout.EAST, imageFormatChooser, 0, SpringLayout.EAST, label);
            layout.putConstraint(SpringLayout.EAST, panel, 5, SpringLayout.EAST, label);
            layout.putConstraint(SpringLayout.SOUTH, panel, 10, SpringLayout.SOUTH, imageFormatChooser);

            int dialogResult = JOptionPane.showOptionDialog(null, panel, msg("File_Exit_Action_Export_Title"),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                    new Object[] { msg("File_Export"), msg("File_Cancel") }, msg("File_Export") + "...");
            if (dialogResult != JOptionPane.OK_OPTION)
                return;
            String imageFormat = (String) imageFormatChooser.getSelectedItem();

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter(msg("File_filter"), imageFormat));
            fileChooser.setCurrentDirectory(new File(model.getImageFilepath()));
            fileChooser.setDialogTitle(msg("File_Export") + "...");

            String filepathWithNewExt = Utils.withUpdatedFileExtension(model.getImageFilepath(), imageFormat);
            fileChooser.setSelectedFile(new File(filepathWithNewExt));
            int fileChooserResult = fileChooser.showDialog(null, msg("File_Export"));
            if (fileChooserResult != JFileChooser.APPROVE_OPTION)
                return;

            String exportFilepath = fileChooser.getSelectedFile().getAbsolutePath();
            if (controller.IO.export(exportFilepath, imageFormat)) {
                JOptionPane.showMessageDialog(null, msg("File_Exit_Action_JPane"));
            }
        }
    }

}
