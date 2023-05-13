package cosc202.andie.tools;

import java.awt.Dimension;
import java.awt.Point;

import cosc202.andie.ImageOperation;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.MouseModel.MouseModelListener;
import cosc202.andie.models.MouseModel.MouseStatus;
import cosc202.andie.operations.shapes.Rectangle;

public class RectangleTool extends Tool {

	private MouseModelListener listener;

	public RectangleTool(AndieModel model, AndieController controller) {
		super(model, controller);
	}

	@Override
	public void activateTool() {
		super.activateTool();
		listener = new MouseModelListener() {
			Point p = null;
			public void mouseMoved(MouseStatus status) { }
			public void mouseDragged(MouseStatus status) {
				if (p == null) 
					return;
				controller.operations.update(getOp(p, status.position, status.isShiftDown, status.isCommtrolDown));
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
		};
		model.mouse.registerMouseModelListener(listener);
				
	}

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
		return new Rectangle(p, d, model.tool.getStrokeColor(), model.tool.getFillColor(), model.tool.getStrokeWidth());
	}

	@Override
	public void deactivateTool() {
		super.deactivateTool();
		model.mouse.deregisiterMouseModelListener(listener);
	}

}
