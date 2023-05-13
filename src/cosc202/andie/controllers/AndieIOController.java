package cosc202.andie.controllers;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import cosc202.andie.Utils;
import cosc202.andie.Utils.ExtensionException;
import cosc202.andie.models.AndieModel;

import static cosc202.andie.LanguageConfig.msg;

import java.io.IOException;

public class AndieIOController {
	private AndieModel model;
	private AndieController controller;
	public AndieIOController(AndieModel model, AndieController controller) {
		this.model = model;
		this.controller = controller;
	}
	
	public void dropOpen(String filepath) {
		safeOpen(filepath);
	}

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

	public boolean safeOpen(String filePath) {
		try {
			model.openImage(filePath);
			return true;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(controller.getContentPane(), msg("File_Exception_e1"));
			return false;
		}
	}

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
