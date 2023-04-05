package cosc202.andie.actions;

import java.util.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import cosc202.andie.EditableImage;
import cosc202.andie.ImageAction;
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
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class FileActions extends MenuActions {
    
    private FileSaveAction saveAction;
    public FileCloseImageAction imageCloseAction;

    /**
     * <p>
     * Create a set of File menu actions.
     * </p>
     */
    public FileActions() {
        super(msg("File_Title"));
        saveAction = new FileSaveAction(msg("File_Save"), null, msg("File_Save_Desc"), Integer.valueOf(KeyEvent.VK_S));
        imageCloseAction = new FileCloseImageAction(msg("File_Close_Image"), null, msg("File_Close_Image_Desc"), Integer.valueOf(0));

        actions.add(new FileOpenAction(msg("File_Open"), null, msg("File_Open_Desc"), Integer.valueOf(KeyEvent.VK_O)));
        actions.add(saveAction);
        actions.add(new FileSaveAsAction(msg("File_Save_As"), null, msg("File_Save_As_Desc"), Integer.valueOf(KeyEvent.VK_A)));
        actions.add(new FileExportAction(msg("File_Export"), null, msg("File_Export_Desc"), Integer.valueOf(KeyEvent.VK_E)));
        actions.add(imageCloseAction);
        actions.add(new FileExitAction(msg("File_Exit"), null, msg("File_Exit_Desc"), Integer.valueOf(0)));

    }

    /**
     * Gets a new file path, with the extension of the file set to the given extension.
     * If the given file path already has an image extension, it is replaced with the given extension, otherwise the given extension is appended to the file path.
     * @param filePath A string representing the path to a file
     * @param extension A string representing an image extension (e.g. "png")
     * @return A string representing the path to a file with the given extension
     */
    private static String getPathWithImageExtension(String filePath, String extension) {
        String currentExtension = getImageExtension(filePath);
        if (currentExtension.equals("")) {
            return filePath + "." + extension;
        } else {
            return filePath.substring(0, filePath.length() - currentExtension.length()) + extension;
        }
    }

    /**
     * Gets the extension of the given file path, if it is a valid image extension.
     * If the given file path does not have an image extension, or if the extension is not a valid image extension, an empty string is returned.
     * @param filePath A string representing the path to a file
     * @return A string representing the extension of the given file path, or an empty string if the given file path does not have a valid image extension
     */
    private static String getImageExtension(String filePath) {
        int lastIndexOfDot = filePath.lastIndexOf('.');
        if (lastIndexOfDot == -1) return "";
        String extension = filePath.substring(lastIndexOfDot + 1);
        Set<String> validExtensions = new HashSet<String>(Arrays.asList(ImageIO.getWriterFileSuffixes()));
        if (validExtensions.contains(extension)) {
            return extension;
        } else {
            return "";
        }
    }

    /**
     * <p>
     * Action to open an image from file.
     * </p>
     * 
     * @see EditableImage#open(String)
     */
    public class FileOpenAction extends ImageAction {

        /**
         * <p>
         * Create a new file-open action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        FileOpenAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
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

            //Only allow files with image extensions that ImageIO can parse to be opened
            FileNameExtensionFilter filter = new FileNameExtensionFilter(msg("File_filter"), ImageIO.getReaderFileSuffixes()); 
            fileChooser.setFileFilter(filter);

            int result = fileChooser.showOpenDialog(target);

            if (result != JFileChooser.APPROVE_OPTION) return;
            try {
                String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();

                if (!imageCloseAction.safeClose()) return; //Close last image before opening a new one
                target.attemptImageOpen(imageFilepath);

            } catch (IOException e1) {
                JOptionPane.showMessageDialog(null, msg("File_Exception_e1"));
            }
        }

        public void updateState() {}

    }

    /**
     * <p>
     * Action to save an image to its current file location.
     * </p>
     * 
     * @see EditableImage#save()
     */
    public class FileSaveAction extends ImageAction {

        /**
         * <p>
         * Create a new file-save action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        FileSaveAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
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
            save();
        }

        /**
         * <p>
         * Saves the image to its original filepath.
         * </p>
         * @returns true if the image was saved successfully, false otherwise
         */
        public boolean save() {
            if (!target.getImage().hasImage()) { JOptionPane.showMessageDialog(null,msg("File_save_error")); return false; }
            try {
                target.getImage().save();           
                return true;
            } catch (EditableImage.ExtensionException err) {
                JOptionPane.showMessageDialog(null, msg("File_save_extension_exception") + String.join(", ", ImageIO.getWriterFileSuffixes()));
            } catch (IOException err) {
                JOptionPane.showMessageDialog(null, msg("File_save_exception_err"));
            }
            return false;
        }

        public void updateState() {
            setEnabled(target.getImage().hasImage());
        }

    }

    /**
     * <p>
     * Action to save an image to a new file location.
     * </p>
     * 
     * @see EditableImage#saveAs(String)
     */
    public class FileSaveAsAction extends ImageAction {

        /**
         * <p>
         * Create a new file-save-as action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        FileSaveAsAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
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
            if (!target.getImage().hasImage()) { JOptionPane.showMessageDialog(null,msg("File_save_error")); return; }

            FileNameExtensionFilter filter = new FileNameExtensionFilter(msg("File_filter"), ImageIO.getWriterFileSuffixes()); 
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(filter);

            String filepath = target.getImage().getFilepath();
            if (filepath != null) fileChooser.setCurrentDirectory(new File(filepath));
            int result = fileChooser.showSaveDialog(target);

            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String imageFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                    String givenImageExtension = getPathWithImageExtension(imageFilepath, "png");
                    if (givenImageExtension.equals("")) throw new EditableImage.ExtensionException(msg("File_Extension_Exception"));
                    target.getImage().saveAs(givenImageExtension);
                } catch (EditableImage.ExtensionException err) {
                    JOptionPane.showMessageDialog(null, msg("File_Extension_Exception_err") + String.join(", ", ImageIO.getWriterFileSuffixes()));
                } catch (IOException err) {
                    JOptionPane.showMessageDialog(null, msg("File_save_exception_err"));
                }
            }
        }

        public void updateState() {
            setEnabled(target.getImage().hasImage());
        }
    }

    /**
     * <p>
     * Action to close the current image
     * </p>
     */
    public class FileCloseImageAction extends ImageAction {

        /**
         * <p>
         * Create a new file-close-image action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        FileCloseImageAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
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
            safeClose();
        }

        /**
         * Closes the image, offering the user a chance to save first.
         * The user can:
         * a) Cancel. The image will remain open, and false will be returned
         * b) Not save. The image will be closed without saving
         * c) Save. The saveAction.save() will save the image before closing.
         * @return A boolean representing whether the image was closed
         */
        public boolean safeClose() {
            if (target.getImage().getModified()) {
                int result = JOptionPane.showConfirmDialog(null,msg("File_Close_Unsaved_Warning_Message"),msg("File_Close_Unsaved_Warning_Title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null);
                switch (result) {
                    case JOptionPane.CANCEL_OPTION:
                        return false;
                    case JOptionPane.NO_OPTION:
                        break;
                    case JOptionPane.YES_OPTION:
                        if(saveAction.save()) {
                            JOptionPane.showMessageDialog(null,msg("File_Close_Saved_Success"));
                        }
                        break;
                }
            }
            target.getImage().reset();
            target.resetZoom();
            target.repaint();
            target.getParent().revalidate();
            return true;
        }

        public void updateState() {
            setEnabled(target.getImage().hasImage());
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
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        FileExitAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
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
            if(imageCloseAction.safeClose()) {
                System.exit(0);
            }
        }
        public void updateState() { }
    }

    /**
     * <p>
     * Action to export the image
     * </p>
     */
    public class FileExportAction extends ImageAction {

        /**
         * <p>
         * Create a new file-exit action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        FileExportAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
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
            if (!target.getImage().hasImage()) { JOptionPane.showMessageDialog(null,msg("File_Exit_Action_hasImage")); return; }

            String[] writerFormatNames = ImageIO.getWriterFileSuffixes();

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


            int dialogResult = JOptionPane.showOptionDialog(null,panel, msg("File_Exit_Action_Export_Title"), JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE, null, new Object[]{msg("File_Export"), msg("File_Cancel")}, msg("File_Export") + "...");
            if (dialogResult != JOptionPane.OK_OPTION ) return;
            String imageFormat = (String) imageFormatChooser.getSelectedItem();
            
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter(msg("File_filter"), imageFormat));
            fileChooser.setCurrentDirectory(new File(target.getImage().getFilepath()));
            fileChooser.setDialogTitle(msg("File_Export") + "...");

            String filepathWithNewExt = getPathWithImageExtension(target.getImage().getFilepath(), imageFormat);
            fileChooser.setSelectedFile(new File(filepathWithNewExt));
            int fileChooserResult = fileChooser.showDialog(null, msg("File_Export"));
            if (fileChooserResult != JFileChooser.APPROVE_OPTION) return;

            try {
                String exportFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                String validExportFilepath = getPathWithImageExtension(exportFilepath, imageFormat);
                target.getImage().export(validExportFilepath, imageFormat);
            } catch (IOException err) {
                JOptionPane.showMessageDialog(null, msg("File_Exit_Action_IOException"));
                return;
            }
            JOptionPane.showMessageDialog(null, msg("File_Exit_Action_JPane"));
        }

        public void updateState() {
            setEnabled(target.getImage().hasImage());
        }
    }

}
