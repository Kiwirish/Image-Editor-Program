package cosc202.andie.tools;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.MouseModel.MouseModelListener;
import cosc202.andie.models.MouseModel.MouseStatus;

import cosc202.andie.models.ToolModel;

/**
 * <p>
 * Select tool. Sets the selection rectangle as the user drags the mouse.
 * </p>
 * 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">cc by-nc-sa 4.0</a>
 * 
 * @see Tool
 * @see ToolModel
 * @author Jeb Nicholson
 * @version 1.0
 */
public class SelectTool extends Tool {

	private MouseModelListener listener;

	/**
	 * Create a new SelectTool
	 * @param model The base AndieModel
	 * @param controller The base AndieController
	 */
	public SelectTool(AndieModel model, AndieController controller) {
		super(model, controller);
	}

	/**
	 * Activate the tool.
	 * <p> This method will register a MouseModelListener that will listen for mouse events and set the selection rectangle as the user drags the mouse. </p>
	 * <p> The selection will be restricted to the image bounds when the user releases the mouse </p>
	 */
	@Override
	public void activateTool() {
		super.activateTool();

		listener = new MouseModelListener() {
			Point p1 = null;
			public void mouseMoved(MouseStatus status) { }
			public void mouseDragged(MouseStatus status) {
				if (p1 == null) 
					return;
				Point p = new Point(Math.min(p1.x, status.position.x), Math.min(p1.y, status.position.y));
				Dimension d = new Dimension(Math.abs(p1.x - status.position.x), Math.abs(p1.y - status.position.y));
				model.tool.setSelection(new Rectangle(p, d));
			}
			public void mouseClicked(MouseStatus status) { }
			public void mouseUp(MouseStatus status) {
				Point p = new Point(Math.min(p1.x, status.position.x), Math.min(p1.y, status.position.y));
				Dimension d = new Dimension(Math.abs(p1.x - status.position.x), Math.abs(p1.y - status.position.y));
				model.tool.setSelection(new Rectangle(p, d));
				model.tool.restrictSelection();
			}
			public void mouseDown(MouseStatus status) {
				p1 = status.position;
			}
		};
		model.mouse.registerMouseModelListener(listener);
	}


	/**
	 * Deactivate the tool (Removes listeners)
	 */
	@Override
	public void deactivateTool() {
		super.deactivateTool();
		model.mouse.unregisterMouseModelListener(listener);
	}
	
}
