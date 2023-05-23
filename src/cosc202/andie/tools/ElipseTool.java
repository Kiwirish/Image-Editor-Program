package cosc202.andie.tools;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import cosc202.andie.ImageOperation;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.MouseModel.MouseModelListener;
import cosc202.andie.models.MouseModel.MouseStatus;
import cosc202.andie.operations.shapes.Elipse;
import cosc202.andie.models.ToolModel;

/**
 * <p>
 * Elipse tool. Draws an elipse as the user drags the mouse. 
 * </p>
 * 
 * <p>
 * Holding shift while dragging will force the elipse to be a circle. Holding control while dragging will force the elipse to be drawn from the center.
 * <p> 
 * 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">cc by-nc-sa 4.0</a>
 * </p>
 * 
 * @see Tool
 * @see Elipse
 * @see ToolModel
 * 
 * @author Jeb Nicholson
 * @version 1.0
 */
public class ElipseTool extends Tool {

	private MouseModelListener listener;

	/**
	 * Create a new ElipseTool
	 * @param model The base AndieModel
	 * @param controller The base AndieController
	 */
	public ElipseTool(AndieModel model, AndieController controller) {
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
	 * <p> This method will register a MouseModelListener that will listen for mouse events and draw the elipse as the user drags the mouse. </p>
	 */
	@Override
	public void activateTool() {
		super.activateTool();
		listener = new MouseModelListener() {
			Point p = null;
			public void mouseDragged(MouseStatus status) {
				if (p == null) 
					return;
				controller.operations.update(getOp(p, status.position, status.isShiftDown, status.isCommtrolDown), false);
			}
			public void mouseClicked(MouseStatus status) { }
			public void mouseUp(MouseStatus status) {
				if (p == null) 
					return;
				controller.operations.apply(getOp(p, status.position, status.isShiftDown, status.isCommtrolDown));
			}
			public void mouseDown(MouseStatus status) {
				p = status.position;
			}
			public void mouseMoved(MouseStatus status) { }
		};
		model.mouse.registerMouseModelListener(listener);
				
	}

	/**
	 * Gets the elipse operation that would be applied if the user dragged from p1 to p2, given whether the user wants to fix the ratio as 1:1, and whether the user wants to draw from the center
	 * @param p1 The first point
	 * @param p2 The second point
	 * @param fixRatio Whether the user wants to fix the ratio as 1:1
	 * @param centerp1 Whether the user wants to draw from the center of p1
	 * @return The elipse operation.
	 */
	private ImageOperation getOp(Point p1, Point p2, boolean fixRatio, boolean centerp1) {
		Point np1 = new Point(p1);
		Point np2 = new Point(p2);

		if (centerp1 && fixRatio) {
			int diameter = (int)Math.max(Math.abs(np2.x - np1.x), Math.abs(np2.y - np1.y));
			boolean negX = np2.x < np1.x;
			boolean negY = np2.y < np1.y;
			np2 = new Point(np1.x + diameter * (negX ? -1 : 1), np1.y + diameter * (negY ? -1 : 1));
			np1 = new Point(np1.x - (np2.x - np1.x), np1.y - (np2.y - np1.y));
		} else if (centerp1) np1 = new Point(np1.x - (np2.x - np1.x), np1.y - (np2.y - np1.y));
		else if (fixRatio) {
			int diameter = (int)Math.max(Math.abs(np2.x - np1.x), Math.abs(np2.y - np1.y));
			boolean negX = np2.x < np1.x;
			boolean negY = np2.y < np1.y;
			np2 = new Point(np1.x + diameter * (negX ? -1 : 1), np1.y + diameter * (negY ? -1 : 1));
		}

		Point p = new Point(Math.min(np1.x, np2.x), Math.min(np1.y, np2.y));
		Dimension d = new Dimension(Math.abs(np1.x - np2.x), Math.abs(np1.y - np2.y));
		return new Elipse(new Rectangle(p, d), model.tool.getStrokeColor(), model.tool.getFillColor(), model.tool.getStrokeWidth());
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
