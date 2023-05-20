package cosc202.andie.controllers;

import java.awt.Cursor;
import java.util.ArrayList;
import javax.swing.JComponent;

import cosc202.andie.models.AndieModel;

/**
 * <p>
 * ANDIE's main controller, which delegates some tasks to sub-controllers
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">cc by-nc-sa 4.0</a>
 * </p>
 * 
 * @see ActionsController
 * @see ImageIOController
 * @see MacrosController
 * @see OpsController
 * @author Jeb Nicholson
 * @version 1.0
 */
public class AndieController {
	private AndieModel model;
	private JComponent contentPane;

	private ArrayList<ManualZoomListener> zoomListeners = new ArrayList<ManualZoomListener>();

	/** Andie's IO Controller */
	public ImageIOController IO;
	/** Andie's Operations Controller */
	public OpsController operations;
	/** Andie's Macros Controller */
	public MacrosController macros;
	/** Andie's Actions Controller */
	public ActionsController actions;

	/**
	 * Create a new AndieController
	 * @param model The base model
	 */
	public AndieController(AndieModel model) {
		this.model = model;
		this.IO = new ImageIOController(model, this);
		this.operations = new OpsController(model);
		this.macros = new MacrosController(model, this);
		this.actions = new ActionsController(model, this);
	}

	/** Safely closes ANDIE */
	public void closeWindow() {
		if(IO.safeClose()) {
			System.exit(0);
		}
	}

	/** Get the content pane */
	public JComponent getContentPane() {
		return contentPane;
	}

	/** Set the content pane */
	public void setContentPane(JComponent contentPane) {
		this.contentPane = contentPane;
	}

	/** Undoes the last operation */
	public void undo() {
		model.getImage().undo();
	}

	/** Redoes the last operation */
	public void redo() {
		model.getImage().redo();
	}

	/** Register a new ZoomListener */
	public void registerZoomListener(ManualZoomListener listener) {
		zoomListeners.add(listener);
	}

	/** Unregister a ZoomListener */
	public void unregisterZoomListener(ManualZoomListener listener) {
		zoomListeners.remove(listener);
	}

	/** Notify ManualZoomListeners to zoom in */
	public void zoomIn() {
		for (ManualZoomListener listener : zoomListeners) {
			listener.manualZoomIn();
		}
	}

	/** Notify ManualZoomListeners to zoom out */
	public void zoomOut() {
		for (ManualZoomListener listener : zoomListeners) {
			listener.manualZoomOut();
		}
	}

	/** Notify ManualZoomListeners to reset zoom */
	public void resetZoom() {
		for (ManualZoomListener listener : zoomListeners) {
			listener.manualResetZoom();
		}
	}

	/** Listens for zoom events */
	public interface ManualZoomListener {
		/** Zoom in */
		public void manualZoomIn();
		/** Zoom out */
		public void manualZoomOut();
		/** Reset zoom */
		public void manualResetZoom();
	}


	/**
	 * Set the model.mouse cursor
	 * @param cursor The cursor to set
	 */
	public void setCursor(Cursor cursor) {
		model.mouse.setCursor(cursor);
	}

	/** Reset the model.mouse cursor */
	public void resetCursor() {
		model.mouse.setCursor(Cursor.getDefaultCursor());
	}

	/** List the registered listeners (for debugging purposes) */
	public void listListeners() {
		for (ManualZoomListener listener : zoomListeners) {
			System.out.println("ManualZoomListener: " + listener);
		}
	}
}
