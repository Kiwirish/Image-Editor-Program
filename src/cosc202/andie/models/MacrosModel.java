package cosc202.andie.models;

import java.util.ArrayList;
import java.util.Stack;

import cosc202.andie.ImageOperation;
import cosc202.andie.models.AndieModel.ModelListener;
import cosc202.andie.models.EditableImage.OperationListener;
import cosc202.andie.controllers.MacrosController;
import cosc202.andie.MacrosPanel;

/**
 * <p>
 * The macros model for ANDIE. Keeps track of the current macro, whether the macros view is open, and notifies listeners when the macro changes.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">cc by-nc-sa 4.0</a>
 * </p>
 * 
 * @see AndieModel
 * @see MacrosController
 * @see MacrosPanel
 * 
 * @author Jeb Nicholson
 * @version 1.0
 */
public class MacrosModel {

	private AndieModel model;
	private boolean macrosViewOpen;

	private ArrayList<ModelListener> macrosUpdateListeners = new ArrayList<ModelListener>();
	private ArrayList<ImageOperation> macroOperations = new ArrayList<ImageOperation>();

	private OperationListener operationListener;
	private ModelListener imageStatusListener;
	private boolean recording;

	/**
	 * Create a new MacrosModel
	 * @param model The base AndieModel
	 */
	public MacrosModel(AndieModel model) {
		this.model = model;
		this.macrosViewOpen = false;
		this.recording = false;

		operationListener = new OperationListener() {
			public void operationApplied(ImageOperation operation) {
				if (!recording)
					return;
				macroOperations.add(operation);
				notifyMacrosUpdateListeners();
			}
			public void operationRemoved() {
				if (!recording || macroOperations.isEmpty())
					return;
				macroOperations.remove(macroOperations.size() - 1);
				notifyMacrosUpdateListeners();
			}
		};

		model.registerImageOperationListener(operationListener);
		imageStatusListener = () -> {
			if (!model.hasImage()) {
				this.recording = false;
				this.macroOperations.clear();
				notifyMacrosUpdateListeners();
			}
		};
		model.registerImageStatusListener(imageStatusListener);
	}

	/**
	 * Set whether the macros view is open, and notify listeners
	 * @param open Whether the macros view is open
	 */
	public void setMacrosViewOpen(boolean open) {
		if (open == this.macrosViewOpen)
			return;
		this.macrosViewOpen = open;
		notifyMacrosUpdateListeners();
	}

	/**
	 * Get whether the macros view is open
	 * @return Whether the macros view is open
	 */
	public boolean getMacrosViewOpen() {
		return this.macrosViewOpen;
	}

	/**
	 * Register a listener to be notified the macros model state changes
	 * @param listener The listener to be notified
	 */
	public void registerMacrosUpdateListener(ModelListener listener) {
		macrosUpdateListeners.add(listener);
	}

	/**
	 * Unregister a listener from being notified the macros model state changes
	 * @param listener The listener to be unregistered
	 */
	public void unregisterMacrosUpdateListener(ModelListener listener) {
		macrosUpdateListeners.remove(listener);
	}

	/** Notify all listeners that the macros model state has changed */
	public void notifyMacrosUpdateListeners() {
		ArrayList<ModelListener> listeners = new ArrayList<ModelListener>(macrosUpdateListeners);
		for (ModelListener listener : listeners) {
			listener.update();
		}
	}

	/**
	 * Get whether the macros model is currently recording
	 * @return Whether the macros model is currently recording
	 */
	public boolean getRecording() {
		return this.recording;
	}

	/** Start recording a new macro */
	public void startRecording() {
		macroOperations.clear();
		this.recording = true;
		notifyMacrosUpdateListeners();
	}

	/** Stop recording the current macro */
	public void stopRecording() {
		this.recording = false;
		notifyMacrosUpdateListeners();
	}

	/**
	 * Get a list of the operations in the current macro
	 * @return A list of the operations in the current macro
	 */
	public ArrayList<ImageOperation> getMacroOperations() {
		return macroOperations;
	}

	/**
	 * Get a string representation of the operations in the current macro
	 * @return A string representation of the operations in the current macro
	 */
	public String getMacroOpsString() {
		Stack<ImageOperation> macroOpsStack = new Stack<ImageOperation>();
		macroOpsStack.addAll(macroOperations);
		String opsString = EditableImage.opsToString(macroOpsStack);
		return opsString;
	}


	/**
	 * Apply the operations in the given string to the current image
	 * @param opsString The string representation of the operations to apply
	 * @return Whether the operations were successfully applied
	 */
	public boolean applyMacroString(String opsString) {
		Stack<ImageOperation> macroOpsStack = EditableImage.stringToOps(opsString);
		for (ImageOperation op : macroOpsStack) {
			if (!model.operations.apply(op)) return false;
		}
		return true;
	}

	/** Notify the model that it is being removed. Unregisters relevant listeners */
	public void notifyRemove() {
		this.model.unregisterImageOperationListener(operationListener);
		this.model.unregisterImageStatusListener(imageStatusListener);
	}

}
