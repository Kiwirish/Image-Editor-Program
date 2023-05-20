package cosc202.andie.controllers;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.io.IOException;

import cosc202.andie.Utils;
import cosc202.andie.Utils.ExtensionException;
import cosc202.andie.models.AndieModel;

import static cosc202.andie.LanguageConfig.msg;


/**
 * <p>
 * The controller for image IO operations (Saving, opening, exporting)
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">cc by-nc-sa 4.0</a>
 * </p>
 * 
 * @see AndieController
 * @author Jeb Nicholson
 * @version 1.0
 */
public class ImageIOController {
	private AndieModel model;
	private AndieController controller;

	/**
	 * Create a new ImageIOController
	 * @param model The base model
	 * @param controller The base controller
	 */
	public ImageIOController(AndieModel model, AndieController controller) {
		this.model = model;
		this.controller = controller;
	}

	/**
	 * Attempt to open a dropped-in file, using {@link #safeOpen(String)}
	 * @param filepath The filepath of the dropped-in file
	 */
	public void dropOpen(String filepath) {
		safeOpen(filepath);
	}

	/**
	 * Safely close the current image, prompting the user to save if the image has been modified
	 * @return Whether the image was closed
	 */
	public boolean safeClose() {
		if (!model.hasImage()) return true;
		if (model.getImage().getModified()) {
			int result = JOptionPane.showConfirmDialog(controller.getContentPane(),msg("File_Close_Unsaved_Warning_Message"),msg("File_Close_Unsaved_Warning_Title"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null);
			switch (result) {
				case JOptionPane.CANCEL_OPTION:
					return false;
				case JOptionPane.NO_OPTION:
					break;
				case JOptionPane.YES_OPTION:
					if(save()) {
						JOptionPane.showMessageDialog(controller.getContentPane(),msg("File_Close_Saved_Success"));
					}
					break;
			}
		}
		model.closeImage();
		return true;
	}

	/**
	 * Save the current image, showing an error message if the image has not been loaded, or something goes wrong with the file extension or IO
	 * @see AndieModel#saveImage()
	 * @return Whether the image was saved
	 */
	public boolean save() {
		if (!model.hasImage()) { JOptionPane.showMessageDialog(controller.getContentPane(),msg("File_save_error")); return false; }
		try {
			model.saveImage();
			return true;
		} catch (ExtensionException err) {
			JOptionPane.showMessageDialog(controller.getContentPane(), msg("File_save_extension_exception") + String.join(", ", ImageIO.getWriterFileSuffixes()));
		} catch (IOException err) {
			JOptionPane.showMessageDialog(controller.getContentPane(), msg("File_save_exception_err"));
		}
		return false;
	}

	/**
	 * Saves the image at a specified filepath, showing an error message if the image has not been loaded, or something goes wrong with the file extension or IO
	 * @see AndieModel#saveImageAs(String)
	 * @param imageFilepath The filepath to save the image to
	 * @return Whether the image was saved
	 */
	public boolean saveAs(String imageFilepath) {
		if (!model.hasImage()) { JOptionPane.showMessageDialog(controller.getContentPane(),msg("File_save_error")); return false; }
		if (Utils.getFileExtension(imageFilepath) == null) {
			String newExtension = Utils.getFileExtension(model.getImageFilepath());
			newExtension = newExtension == null ? "png" : newExtension;
			imageFilepath = Utils.withFileExtension(imageFilepath, newExtension);
		}
		try {
			model.saveImageAs(imageFilepath);
			return true;
		} catch (ExtensionException err) {
			JOptionPane.showMessageDialog(controller.getContentPane(), msg("File_save_extension_exception") + String.join(", ", ImageIO.getWriterFileSuffixes()));
		} catch (IOException err) {
			JOptionPane.showMessageDialog(controller.getContentPane(), msg("File_save_exception_err"));
		}
		return false;
	}

	/**
	 * Open an image, showing an error message if the image cannot be opened
	 * @see AndieModel#openImage(String)
	 * @param filePath The filepath of the image to open
	 * @return Whether the image was opened
	 */
	public boolean safeOpen(String filePath) {
		try {
			model.openImage(filePath);
			return true;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(controller.getContentPane(), msg("File_Exception_e1"));
			return false;
		}
	}

	/**
	 * Export the current image, showing an error message if there is an IO error
	 * @param exportFilepath The filepath to export the image to
	 * @param imageFormat The image format to export the image as
	 * @return Whether the image was exported
	 */
	public boolean export(String exportFilepath, String imageFormat) {
		String cleanPath = Utils.withFileExtension(exportFilepath, imageFormat);
		try {
			model.exportImage(cleanPath, imageFormat);
		} catch (IOException err) {
			JOptionPane.showMessageDialog(controller.getContentPane(), msg("File_IO_Exception_err"));
			return false;
		}
		return true;
	}

}
