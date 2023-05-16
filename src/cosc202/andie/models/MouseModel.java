package cosc202.andie.models;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MouseModel {

	AndieModel model;

	ArrayList<MouseModelListener> mouseModelListeners = new ArrayList<MouseModelListener>();

	public MouseModel(AndieModel model) {
		this.model = model;
	}
	public void registerMouseModelListener(MouseModelListener listener) {
		mouseModelListeners.add(listener);
	}
	public void unregisterMouseModelListener(MouseModelListener listener) {
		mouseModelListeners.remove(listener);
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

}
