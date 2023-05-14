package cosc202.andie.tools;

import java.awt.Point;

import cosc202.andie.ImageOperation;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.MouseModel.MouseModelListener;
import cosc202.andie.models.MouseModel.MouseStatus;
import cosc202.andie.operations.shapes.Line;

public class LineTool extends Tool {

	private MouseModelListener listener;

	public LineTool(AndieModel model, AndieController controller) {
		super(model, controller);
	}

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

	@Override
	public void deactivateTool() {
		super.deactivateTool();
		model.mouse.deregisiterMouseModelListener(listener);
	}

}
