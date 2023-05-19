package cosc202.andie.controllers;

import java.awt.Cursor;
import java.util.ArrayList;

import javax.swing.JComponent;

import cosc202.andie.models.AndieModel;

public class AndieController {
	private AndieModel model;
	private JComponent contentPane;

	private ArrayList<ManualZoomListener> zoomListeners = new ArrayList<ManualZoomListener>();

	public ImageIOController IO;
	public OpsController operations;
	public MacrosController macros;
	public ActionsController actions;

	public AndieController(AndieModel model) {
		this.model = model;
		this.IO = new ImageIOController(model, this);
		this.operations = new OpsController(model);
		this.macros = new MacrosController(model, this);
		this.actions = new ActionsController(model, this);
	}
	public void closeWindow() {
		if(IO.safeClose()) {
			System.exit(0);
		}
	}
	public JComponent getContentPane() {
		return contentPane;
	}
	public void setContentPane(JComponent contentPane) {
		this.contentPane = contentPane;
	}
	public void undo() {
		model.getImage().undo();
	}
	public void redo() {
		model.getImage().redo();
	}


	public void registerZoomListener(ManualZoomListener listener) {
		zoomListeners.add(listener);
	}
	public void unregisterZoomListener(ManualZoomListener listener) {
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


	public void setCursor(Cursor cursor) {
		model.mouse.setCursor(cursor);
	}
	public void resetCursor() {
		model.mouse.setCursor(Cursor.getDefaultCursor());
	}



	public void listListeners() {
		for (ManualZoomListener listener : zoomListeners) {
			System.out.println("ManualZoomListener: " + listener);
		}
	}
}
