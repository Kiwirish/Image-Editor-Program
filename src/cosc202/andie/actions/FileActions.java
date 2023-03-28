package cosc202.andie.actions;

import java.util.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import java.util.prefs.Preferences;

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
public class FileActions {
    
    /** A list of actions for the File menu. */
    protected ArrayList<Action> actions;
    private FileSaveAction saveAction;
    public FileExitAction exitAction;

    /**
     * <p>
     * Create a set of File menu actions.
     * </p>
     */
    public FileActions() {

        saveAction = new FileSaveAction(msg("File_Save"), null, msg("File_Save_Desc"), Integer.valueOf(KeyEvent.VK_S));
        exitAction = new FileExitAction(msg("File_Exit"), null, msg("File_Exit_Desc"), Integer.valueOf(0));

        actions = new ArrayList<Action>();

        actions.add(new FileOpenAction(msg("File_Open"), null, msg("File_Open_Desc"), Integer.valueOf(KeyEvent.VK_O)));
        actions.add(saveAction);
        actions.add(new FileSaveAsAction(msg("File_Save_As"), null, msg("File_Save_As_Desc"), Integer.valueOf(KeyEvent.VK_A)));
        actions.add(new FileExportAction(msg("File_Export"), null, msg("File_Export_Desc"), Integer.valueOf(KeyEvent.VK_E)));
        actions.add(exitAction);

    }

    /**
     * <p>
     * Create a menu contianing the list of File actions.
     * </p>
     * 
     * @return The File menu UI element.
     */
    public JMenu createMenu() {
        JMenu fileMenu = new JMenu(msg("File_Title"));

        for(Action action: actions) {
            fileMenu.add(new JMenuItem(action));
        }

        return fileMenu;
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
                target.attemptImageOpen(imageFilepath);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(null, msg("File_Exception_e1"));
            }
        }

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
                    String givenImageExtension = getImageExtension(imageFilepath);
                    if (givenImageExtension.equals("")) throw new EditableImage.ExtensionException(msg("File_Extension_Exception"));
                    target.getImage().saveAs(givenImageExtension);
                } catch (EditableImage.ExtensionException err) {
                    JOptionPane.showMessageDialog(null, msg("File_Extension_Exception_err") + String.join(", ", ImageIO.getWriterFileSuffixes()));
                } catch (IOException err) {
                    JOptionPane.showMessageDialog(null, msg("File_save_exception_err"));
                }
            }
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
            exit();
        }

        /**
         * Exit the program, asking the user if they want to save their image first.
         */
        public void exit() {
            //Ask the user if they want to save their image before quitting
            if (!target.getImage().getModified()) System.exit(0);

            int result = JOptionPane.showConfirmDialog(null, msg("File_Exit_JPane_Desc"), msg("File_Exit_JPane"), JOptionPane.YES_NO_CANCEL_OPTION);
            switch (result) {
                case JOptionPane.CANCEL_OPTION:
                    return;
                case JOptionPane.NO_OPTION:
                    System.exit(0);
                    break;
                case JOptionPane.YES_OPTION:
                    if(saveAction.save()) {
                        JOptionPane.showMessageDialog(null,msg("File_Exit_JPane_YES"));
                        System.exit(0);
                    }
                    break;
            }
        }

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
            JLabel label = new JLabel("Choose Image Format to export: ");
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


            int dialogResult = JOptionPane.showOptionDialog(null,panel, "Export Image", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE, null, new Object[]{"Export", "Cancel"}, "Export...");
            if (dialogResult != JOptionPane.OK_OPTION ) return;
            String imageFormat = (String) imageFormatChooser.getSelectedItem();
            
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Images", imageFormat));
            fileChooser.setCurrentDirectory(new File(target.getImage().getFilepath()));
            fileChooser.setDialogTitle("Export...");

            String filepathWithNewExt = getPathWithImageExtension(target.getImage().getFilepath(), imageFormat);
            fileChooser.setSelectedFile(new File(filepathWithNewExt));
            int fileChooserResult = fileChooser.showDialog(null, "Export");
            if (fileChooserResult != JFileChooser.APPROVE_OPTION) return;

            try {
                String exportFilepath = fileChooser.getSelectedFile().getCanonicalPath();
                String validExportFilepath = getPathWithImageExtension(exportFilepath, imageFormat);
                target.getImage().export(validExportFilepath, imageFormat);
            } catch (IOException err) {
                JOptionPane.showMessageDialog(null, "An error occured while exporting. Please ensure you have permission to write to this file location.");
                return;
            }
            JOptionPane.showMessageDialog(null, "Image exported!");
        }
    }

}
