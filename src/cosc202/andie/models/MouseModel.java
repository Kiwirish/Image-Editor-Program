package cosc202.andie.models;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import cosc202.andie.models.AndieModel.ModelListener;

public class MouseModel {

	AndieModel model;

	private ArrayList<MouseModelListener> mouseModelListeners = new ArrayList<MouseModelListener>();
	private ArrayList<ModelListener> cursorListeners = new ArrayList<ModelListener>();
	private Cursor cursor;

	public MouseModel(AndieModel model) {
		this.model = model;
		cursor = Cursor.getDefaultCursor();
	}
	public void registerMouseModelListener(MouseModelListener listener) {
		mouseModelListeners.add(listener);
	}
	public void unregisterMouseModelListener(MouseModelListener listener) {
		mouseModelListeners.remove(listener);
	}

	public void registerCursorListener(ModelListener listener) {
		cursorListeners.add(listener);
	}
	public void unregisterCursorListener(ModelListener listener) {
		cursorListeners.remove(listener);
	}
	public void notifyCursorListeners() {
		for (ModelListener listener : cursorListeners) {
			listener.update();
		}
	}

	public Cursor getCursor() {
		return this.cursor;
	}
	public void setCursor(Cursor cursor) {
		this.cursor = cursor;
		notifyCursorListeners();
	}

	// All positions are with respect to the current working image
	public void mouseMoved(Point position, MouseEvent e) {
		ArrayList<MouseModelListener> listeners = new ArrayList<MouseModelListener>(mouseModelListeners);
		for (MouseModelListener listener : listeners) {
			listener.mouseMoved(new MouseStatus(position, e));
		}
	}
	public void mouseDragged(Point position, MouseEvent e) {
		ArrayList<MouseModelListener> listeners = new ArrayList<MouseModelListener>(mouseModelListeners);
		for (MouseModelListener listener : listeners) {
			listener.mouseDragged(new MouseStatus(position, e));
		}
	}
	public void mouseClicked(Point position, MouseEvent e) {
		ArrayList<MouseModelListener> listeners = new ArrayList<MouseModelListener>(mouseModelListeners);
		for (MouseModelListener listener : listeners) {
			listener.mouseClicked(new MouseStatus(position, e));
		}
	}
	public void mouseUp(Point position, MouseEvent e) {
		ArrayList<MouseModelListener> listeners = new ArrayList<MouseModelListener>(mouseModelListeners);
		for (MouseModelListener listener : listeners) {
			listener.mouseUp(new MouseStatus(position, e));
		}
	}
	public void mouseDown(Point position, MouseEvent e) {
		ArrayList<MouseModelListener> listeners = new ArrayList<MouseModelListener>(mouseModelListeners);
		for (MouseModelListener listener : listeners) {
			listener.mouseDown(new MouseStatus(position, e));
		}
	}

	public interface MouseModelListener {
		public void mouseMoved(MouseStatus status);
		public void mouseDragged(MouseStatus status);
		public void mouseClicked(MouseStatus status);
		public void mouseUp(MouseStatus status);
		public void mouseDown(MouseStatus status);
	}

	public class MouseStatus {
		public Point position;
		public boolean isShiftDown;
		public boolean isCommtrolDown;
		public boolean isAltDown;
		
		public MouseStatus(Point position, boolean isShiftDown, boolean isCommtrolDown, boolean isAltDown) {
			this.position = position;
		}
		public MouseStatus(Point position, MouseEvent e) {
			this.position = position;
			this.isShiftDown = e.isShiftDown();
			this.isCommtrolDown = AndieModel.IS_MAC ? e.isMetaDown() : e.isControlDown();
			this.isAltDown = e.isAltDown();
		}
	}

	public void listListeners() {
		for (MouseModelListener listener : mouseModelListeners) {
			System.out.println("MouseListener: " + listener);
		}
	}

}
