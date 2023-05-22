package cosc202.andie.models;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import cosc202.andie.models.AndieModel.ModelListener;

/**
 * <p>
 * The mouse model for ANDIE. Keeps track of the mouse position and notifies listeners when the mouse moves. Also keeps track of the cursor and notifies listeners when the cursor changes.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">cc by-nc-sa 4.0</a>
 * </p>
 * 
 * @see AndieModel
 * 
 * @author Jeb Nicholson
 * @version 1.0
 */
public class MouseModel {

	private AndieModel model;

	private ArrayList<MouseModelListener> mouseModelListeners = new ArrayList<MouseModelListener>();
	private ArrayList<ModelListener> cursorListeners = new ArrayList<ModelListener>();
	private Cursor cursor;

	private ModelListener imageStatusListener;

	/**
	 * Create a new MouseModel
	 * @param model The base AndieModel
	 */
	public MouseModel(AndieModel model) {
		this.model = model;
		init();
		imageStatusListener = () -> {
			if (!model.hasImage())
				init();
		};
		model.registerImageStatusListener(imageStatusListener);
	}

	/** Initialize the mouse model */
	private void init() {
		cursor = Cursor.getDefaultCursor();
	}

	/**
	 * Register a new mouse model listener
	 * @param listener The listener to register
	 */
	public void registerMouseModelListener(MouseModelListener listener) {
		mouseModelListeners.add(listener);
	}

	/**
	 * Unregister a mouse model listener
	 * @param listener The listener to unregister
	 */
	public void unregisterMouseModelListener(MouseModelListener listener) {
		mouseModelListeners.remove(listener);
	}

	/**
	 * Register a new cursor listener
	 * @param listener The listener to register
	 */
	public void registerCursorListener(ModelListener listener) {
		cursorListeners.add(listener);
	}

	/**
	 * Unregister a cursor listener
	 * @param listener The listener to unregister
	 */
	public void unregisterCursorListener(ModelListener listener) {
		cursorListeners.remove(listener);
	}

	/** Notify all cursor listeners */
	public void notifyCursorListeners() {
		for (ModelListener listener : cursorListeners) {
			listener.update();
		}
	}

	/**
	 * Get the current cursor
	 * @return The current cursor
	 */
	public Cursor getCursor() {
		return this.cursor;
	}

	/**
	 * Set the current cursor
	 * @param cursor The new cursor
	 */
	public void setCursor(Cursor cursor) {
		this.cursor = cursor;
		notifyCursorListeners();
	}

	// All positions are with respect to the current working image

	/**
	 * Notify the MouseModel of a mouseMoved event
	 * @param position The position of the mouse relative to the current working image
	 * @param e The MouseEvent
	 */
	public void mouseMoved(Point position, MouseEvent e) {
		ArrayList<MouseModelListener> listeners = new ArrayList<MouseModelListener>(mouseModelListeners);
		for (MouseModelListener listener : listeners) {
			listener.mouseMoved(new MouseStatus(position, e));
		}
	}

	/**
	 * Notify the MouseModel of a mouseDragged event
	 * @param position The position of the mouse relative to the current working image
	 * @param e The MouseEvent
	 */
	public void mouseDragged(Point position, MouseEvent e) {
		ArrayList<MouseModelListener> listeners = new ArrayList<MouseModelListener>(mouseModelListeners);
		for (MouseModelListener listener : listeners) {
			listener.mouseDragged(new MouseStatus(position, e));
		}
	}

	/**
	 * Notify the MouseModel of a mouseClicked event
	 * @param position The position of the mouse relative to the current working image
	 * @param e The MouseEvent
	 */
	public void mouseClicked(Point position, MouseEvent e) {
		ArrayList<MouseModelListener> listeners = new ArrayList<MouseModelListener>(mouseModelListeners);
		for (MouseModelListener listener : listeners) {
			listener.mouseClicked(new MouseStatus(position, e));
		}
	}

	/**
	 * Notify the MouseModel of a mouseUp event
	 * @param position The position of the mouse relative to the current working image
	 * @param e The MouseEvent
	 */
	public void mouseUp(Point position, MouseEvent e) {
		ArrayList<MouseModelListener> listeners = new ArrayList<MouseModelListener>(mouseModelListeners);
		for (MouseModelListener listener : listeners) {
			listener.mouseUp(new MouseStatus(position, e));
		}
	}

	/**
	 * Notify the MouseModel of a mouseDown event
	 * @param position The position of the mouse relative to the current working image
	 * @param e The MouseEvent
	 */
	public void mouseDown(Point position, MouseEvent e) {
		ArrayList<MouseModelListener> listeners = new ArrayList<MouseModelListener>(mouseModelListeners);
		for (MouseModelListener listener : listeners) {
			listener.mouseDown(new MouseStatus(position, e));
		}
	}

	/** A listener for {@link MouseStatus} updates */
	public interface MouseModelListener {
		/**
		 * Called when the mouse moves
		 * @param status The new mouse status
		 */
		public void mouseMoved(MouseStatus status);
		/**
		 * Called when the mouse is dragged
		 * @param status The new mouse status
		 */
		public void mouseDragged(MouseStatus status);
		/**
		 * Called when the mouse is clicked
		 * @param status The new mouse status
		 */
		public void mouseClicked(MouseStatus status);
		/**
		 * Called when the mouse is released
		 * @param status The new mouse status
		 */
		public void mouseUp(MouseStatus status);
		/**
		 * Called when the mouse is pressed
		 * @param status The new mouse status
		 */
		public void mouseDown(MouseStatus status);
	}

	/** This class represents the relevant current state of the mouse */
	public class MouseStatus {
		/** The mouse's position, given relative to the current working image */
		public Point position;
		/** Whether the shift key is down */
		public boolean isShiftDown;
		/** Whether the command/control key is down */
		public boolean isCommtrolDown;
		/** Whether the alt key is down */
		public boolean isAltDown;
		
		/**
		 * Create a new MouseStatus with the given parameters
		 * @param position The mouse's position, given relative to the current working image
		 * @param isShiftDown Whether the shift key is down
		 * @param isCommtrolDown Whether the command/control key is down
		 * @param isAltDown Whether the alt key is down
		 */
		public MouseStatus(Point position, boolean isShiftDown, boolean isCommtrolDown, boolean isAltDown) {
			this.position = position;
		}

		/**
		 * Create a new MouseStatus with the given parameters
		 * @param position The mouse's position, given relative to the current working image
		 * @param e The MouseEvent
		 */
		public MouseStatus(Point position, MouseEvent e) {
			this.position = position;
			this.isShiftDown = e.isShiftDown();
			this.isCommtrolDown = AndieModel.IS_MAC ? e.isMetaDown() : e.isControlDown();
			this.isAltDown = e.isAltDown();
		}
	}

	/** Notify the MouseModel that it's being removed. Unregisters relevant listeners */
	public void notifyRemove() {
		model.unregisterImageStatusListener(imageStatusListener);
	}

	/** List the listeners in use. For debugging purposes */
	public void listListeners() {
		for (MouseModelListener listener : mouseModelListeners) {
			System.out.println("MouseListener: " + listener);
		}
	}

}
