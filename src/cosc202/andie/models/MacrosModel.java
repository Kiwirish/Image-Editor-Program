package cosc202.andie.models;

import java.util.ArrayList;
import java.util.Stack;

import cosc202.andie.ImageOperation;
import cosc202.andie.models.AndieModel.ModelListener;
import cosc202.andie.models.EditableImage.OperationListener;

public class MacrosModel {

	private AndieModel model;
	private boolean macrosViewOpen;

	private ArrayList<ModelListener> macrosUpdateListeners = new ArrayList<ModelListener>();
	private ArrayList<ImageOperation> macroOperations = new ArrayList<ImageOperation>();

	private OperationListener operationListener;
	private boolean opListenerRegistered;
	private ModelListener imageStatusListener;
	private boolean recording;

	public MacrosModel(AndieModel model) {
		this.model = model;
		this.macrosViewOpen = false;
		this.recording = false;
		this.opListenerRegistered = false;


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

		imageStatusListener = () -> {
			if (!this.opListenerRegistered && model.hasImage()) {
				model.getImage().registerOperationListener(operationListener);
				this.opListenerRegistered = true;
			} else if (this.opListenerRegistered && !model.hasImage()) {
				model.getImage().unregisterOperationListener(operationListener);
				this.opListenerRegistered = false;
			}
		};

		model.registerImageStatusListener(imageStatusListener);
	}

	public void setMacrosViewOpen(boolean open) {
		if (open == this.macrosViewOpen)
			return;
		this.macrosViewOpen = open;
		notifyMacrosUpdateListeners();
	}

	public boolean getMacrosViewOpen() {
		return this.macrosViewOpen;
	}

	public void registerMacrosUpdateListener(ModelListener listener) {
		macrosUpdateListeners.add(listener);
	}

	public void unregisterMacrosUpdateListener(ModelListener listener) {
		macrosUpdateListeners.remove(listener);
	}

	public void notifyMacrosUpdateListeners() {
		ArrayList<ModelListener> listeners = new ArrayList<ModelListener>(macrosUpdateListeners);
		for (ModelListener listener : listeners) {
			listener.update();
		}
	}

	public boolean getRecording() {
		return this.recording;
	}

	public void startRecording() {
		macroOperations.clear();
		this.recording = true;
		notifyMacrosUpdateListeners();
	}

	public void stopRecording() {
		this.recording = false;
		notifyMacrosUpdateListeners();
	}

	public ArrayList<ImageOperation> getMacroOperations() {
		return macroOperations;
	}

	public String getMacroOpsString() {
		Stack<ImageOperation> macroOpsStack = new Stack<ImageOperation>();
		macroOpsStack.addAll(macroOperations);
		String opsString = EditableImage.opsToString(macroOpsStack);
		return opsString;
	}

	public void applyMacroString(String opsString) {
		Stack<ImageOperation> macroOpsStack = EditableImage.stringToOps(opsString);
		for (ImageOperation op : macroOpsStack) {
			model.operations.apply(op);
		}
	}

	public void notifyRemove() {
		if (this.model.getImage() != null) {
			this.model.getImage().unregisterOperationListener(operationListener);
			this.opListenerRegistered = false;
		}
		this.model.unregisterImageStatusListener(imageStatusListener);
	}

}
