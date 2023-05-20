package cosc202.andie.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import cosc202.andie.ImageOperation;
import cosc202.andie.Utils;
import cosc202.andie.models.AndieModel;

/**
 * <p>
 * The controller for Macros
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
public class MacrosController {

	private AndieModel model;
	private AndieController controller;

	/**
	 * Create a new MacrosController
	 * @param model The base model
	 * @param controller The base controller
	 */
	public MacrosController(AndieModel model, AndieController controller) {
		this.model = model;
		this.controller = controller;
	}

	/**
	 * Set whether the macros view is open
	 * @param open Whether the macros view is open
	 */
	public void setMacrosViewOpen(boolean open) {
		model.macros.setMacrosViewOpen(open);
	}

	/** Start recording a new macro */
	public void startMacroRecording() {
		if (!model.hasImage()) return;
		model.macros.startRecording();
	}

	/** Stop recording a macro. Will prompt the user to save the macro, if there is one. */ 
	public void stopMacroRecording() {
		model.macros.stopRecording();
		if (!model.hasImage()) return;
		ArrayList<ImageOperation> macro = model.macros.getMacroOperations();
		if (macro.isEmpty())
			return;
		String macroOpsString = model.macros.getMacroOpsString();

		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Andie Macro files", "macro");
		fileChooser.setFileFilter(filter);
		fileChooser.setSelectedFile(new File(Utils.withFileExtension(model.getImageFilepath(), "macro")));
		int result = fileChooser.showSaveDialog(controller.getContentPane());

		if (result != JFileChooser.APPROVE_OPTION)
			return;
		String macroFilepath = fileChooser.getSelectedFile().getAbsolutePath();
		macroFilepath = Utils.withFileExtension(macroFilepath, "macro");

		try {
			Files.writeString(new File(macroFilepath).toPath(), macroOpsString);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(controller.getContentPane(), "IO Error saving macro file.");
			return;
		}
		JOptionPane.showMessageDialog(controller.getContentPane(), "Successfully saved macro file.");
	}

	/** Apply an existing macro. Will prompt the user to pick a .macro file from their system */
	public void applyExistingMacro() {
		JFileChooser fileChooser = new JFileChooser();

		FileNameExtensionFilter filter = new FileNameExtensionFilter("Andie Macro files", "macro");
		fileChooser.setFileFilter(filter);
		int result = fileChooser.showOpenDialog(controller.getContentPane());

		if (result != JFileChooser.APPROVE_OPTION)
			return;
		String macroFilepath = fileChooser.getSelectedFile().getAbsolutePath();

		File macroFile = new File(macroFilepath);
		try {
			if (!macroFile.exists())
				throw new IOException();
			String macroString = Files.readString(macroFile.toPath(), Charset.defaultCharset());
			model.macros.applyMacroString(macroString);

			JOptionPane.showMessageDialog(controller.getContentPane(), "Macro applied successfully.");

		} catch (IOException e) {
			JOptionPane.showMessageDialog(controller.getContentPane(), "IO Error reading macro file.");
		}
	}
}
