package cosc202.andie.tools;

import java.awt.Cursor;
import java.awt.Point;

import cosc202.andie.ImageOperation;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.MouseModel.MouseModelListener;
import cosc202.andie.models.MouseModel.MouseStatus;
import cosc202.andie.operations.shapes.Line;
import cosc202.andie.models.ToolModel;

/**
 * <p>
 * Line tool. Draws a line as the user drags the mouse. 
 * </p>
 * 
 * <p>
 * Holding shift while dragging will force the rectangle to be a square. Holding control while dragging will force the rectangle to be drawn from the center.
 * <p> 
 * 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">cc by-nc-sa 4.0</a>
 * </p>
 * 
 * @see Tool
 * @see Line
 * @see ToolModel
 * 
 * @author Jeb Nicholson
 * @version 1.0
 */
public class LineTool extends Tool {

	private MouseModelListener listener;

	/**
	 * Create a new LineTool
	 * @param model The base AndieModel
	 * @param controller The base AndieController
	 */
	public LineTool(AndieModel model, AndieController controller) {
		super(model, controller);
	}

	/**
	 * Get the cursor that this tool would like to use when hovering the image panel (Crosshair)
	 */
	public Cursor getCursor() {
		return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
	}

	/**
	 * Activate the tool.
	 * <p> This method will register a MouseModelListener that will listen for mouse events and draw the line as the user drags the mouse. </p>
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
				controller.operations.update(getOp(p1, status.position, status.isShiftDown), false);
			}
			public void mouseClicked(MouseStatus status) { }
			public void mouseUp(MouseStatus status) {
				if (p1 == null) 
					return;
				controller.operations.apply(getOp(p1, status.position, status.isShiftDown));
			}
			public void mouseDown(MouseStatus status) {
				p1 = status.position;
			}
		};
		model.mouse.registerMouseModelListener(listener);
				
	}

	/**
	 * Gets the line operation that would be applied if the user dragged from p1 to p2, given whether the user wants to snap
	 * @param p1 The first point
	 * @param p2 The second point
	 * @param fixAngle Whether the user wants to snap to the nearest 45 degrees
	 * @return The line operation.
	 */
	private ImageOperation getOp(Point p1, Point p2, boolean fixAngle) {
		Point np2 = new Point(p2);
		if (fixAngle) { //Fixes angle to nearest 45 degrees
			double angle = Math.atan2(np2.y - p1.y, np2.x - p1.x);
			angle = Math.round(angle * 4 / Math.PI) * Math.PI / 4;
			double dist = Math.sqrt(Math.pow(np2.x - p1.x, 2) + Math.pow(np2.y - p1.y, 2));
			np2 = new Point((int)(p1.x + Math.cos(angle) * dist), (int)(p1.y + Math.sin(angle) * dist));
		}

		return new Line(p1, np2, model.tool.getStrokeColor(), model.tool.getStrokeWidth());
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
