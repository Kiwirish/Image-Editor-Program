package cosc202.andie.controllers;

import java.util.ArrayList;

import javax.swing.JComponent;

import cosc202.andie.models.AndieModel;

public class AndieController {
	private AndieModel model;
	private JComponent popupParent;

	public AndieIOController IO;
	public AndieOperationsController operations;

	public AndieController(AndieModel model) {
		this.model = model;
		this.IO = new AndieIOController(model, this);
		this.operations = new AndieOperationsController(model);
	}
	public void closeWindow() {
		if(IO.safeClose()) {
			System.exit(0);
		}
	}
	public JComponent getPopupParent() {
		return popupParent;
	}
	public void setPopupParent(JComponent popupParent) {
		this.popupParent = popupParent;
	}
	public void undo() {
		model.getImage().undo();
	}
	public void redo() {
		model.getImage().redo();
	}

	private ArrayList<ManualZoomListener> zoomListeners = new ArrayList<ManualZoomListener>();

	public void registerZoomListener(ManualZoomListener listener) {
		zoomListeners.add(listener);
	}
	public void deregisterZoomListener(ManualZoomListener listener) {
		zoomListeners.remove(listener);
	}
	public void zoomIn() {
		for (ManualZoomListener listener : zoomListeners) {
			listener.manualZoomIn();
		}
	}
	public void zoomOut() {
		for (ManualZoomListener listener : zoomListeners) {
			listener.manualZoomOut();
		}
	}
	public void resetZoom() {
		for (ManualZoomListener listener : zoomListeners) {
			listener.manualResetZoom();
		}
	}
	public interface ManualZoomListener {
		public void manualZoomIn();
		public void manualZoomOut();
		public void manualResetZoom();
	}
	public void removeAllListeners() {
		zoomListeners.clear();
	}
}
