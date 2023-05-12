package cosc202.andie.actions;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import cosc202.andie.ImageAction;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.MouseModel;
import cosc202.andie.models.AndieModel.ModelListener;
import cosc202.andie.models.MouseModel.MouseModelListener;
import cosc202.andie.operations.shapes.Line;

public class ToolActions extends MenuActions {
	
	public ToolActions(AndieController controller, AndieModel model) {
		super("Tools", controller, model);

		actions.add(new LineToolAction("Line", null, "Draw a line", null));
		
		ModelListener isl = ()-> {
				for (ImageAction action : actions) {
						action.setEnabled(model.hasImage());
				}
		};
		model.registerImageStatusListener(isl);
		isl.update();
	}

	public class LineToolAction extends ImageAction {

			LineToolAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
					super(name, icon, desc, mnemonic);
			}

			public void actionPerformed(ActionEvent e) {
				MouseModelListener listener = new MouseModel.MouseModelListener() {
					Point p1 = null;
					public void mouseMoved(Point position) { }
					public void mouseDragged(Point position) {
						if (p1 == null) 
							return;
						controller.operations.update(new Line(p1, position, new Color(0xFF0000), 5));
					}
					public void mouseClicked(Point position) { }
					public void mouseUp(Point position) {
						if (p1 == null) 
							return;
						controller.operations.apply(new Line(p1, position, new Color(0xFF0000), 5));
						model.mouse.deregisiterMouseModelListener(this);
					}
					public void mouseDown(Point position) {
						p1 = position;
					}
				};
				model.mouse.registerMouseModelListener(listener);
				
			}

    }

}
