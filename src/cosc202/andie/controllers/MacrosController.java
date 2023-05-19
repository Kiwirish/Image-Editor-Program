package cosc202.andie.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import cosc202.andie.ImageOperation;
import cosc202.andie.Utils;
import cosc202.andie.models.AndieModel;

public class MacrosController {

	private AndieModel model;
	private AndieController controller;

	public MacrosController(AndieModel model, AndieController controller) {
		this.model = model;
		this.controller = controller;
	}

	public void setMacrosViewOpen(boolean open) {
		model.macros.setMacrosViewOpen(open);
	}

	public void startMacroRecording() {
		if (!model.hasImage()) return;
		model.macros.startRecording();
	}

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
