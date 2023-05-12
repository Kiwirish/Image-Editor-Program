package cosc202.andie.models;

import java.awt.Point;
import java.util.ArrayList;

public class MouseModel {
	ArrayList<MouseModelListener> mouseModelListeners = new ArrayList<MouseModelListener>();
	public MouseModel(AndieModel model) {
		
	}
	public void registerMouseModelListener(MouseModelListener listener) {
		mouseModelListeners.add(listener);
	}
	public void deregisiterMouseModelListener(MouseModelListener listener) {
		mouseModelListeners.remove(listener);
	}

	// All positions are with respect to the current working image
	public void mouseMoved(Point position) {
		ArrayList<MouseModelListener> listeners = new ArrayList<MouseModelListener>(mouseModelListeners);
		for (MouseModelListener listener : listeners) {
			listener.mouseMoved(position);
		}
	}
	public void mouseDragged(Point position) {
		ArrayList<MouseModelListener> listeners = new ArrayList<MouseModelListener>(mouseModelListeners);
		for (MouseModelListener listener : listeners) {
			listener.mouseDragged(position);
		}
	}
	public void mouseClicked(Point position) {
		ArrayList<MouseModelListener> listeners = new ArrayList<MouseModelListener>(mouseModelListeners);
		for (MouseModelListener listener : listeners) {
			listener.mouseClicked(position);
		}
	}
	public void mouseUp(Point position) {
		ArrayList<MouseModelListener> listeners = new ArrayList<MouseModelListener>(mouseModelListeners);
		for (MouseModelListener listener : listeners) {
			listener.mouseUp(position);
		}
	}
	public void mouseDown(Point position) {
		ArrayList<MouseModelListener> listeners = new ArrayList<MouseModelListener>(mouseModelListeners);
		for (MouseModelListener listener : listeners) {
			listener.mouseDown(position);
		}
	}

	public interface MouseModelListener {
		public void mouseMoved(Point position);
		public void mouseDragged(Point position);
		public void mouseClicked(Point position);
		public void mouseUp(Point position);
		public void mouseDown(Point position);
	}

}
