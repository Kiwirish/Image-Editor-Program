package cosc202.andie.actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import cosc202.andie.ImageAction;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.AndieModel.ModelListener;
import cosc202.andie.tools.ElipseTool;
import cosc202.andie.tools.LineTool;
import cosc202.andie.tools.RectangleTool;

public class ToolActions extends MenuActions {

	public ToolActions(AndieController controller, AndieModel model) {
		super("Tools", controller, model);

		actions.add(new LineToolAction("Line", null, "Draw a line", null));
		actions.add(new RectangleToolAction("Rectangle", null, "Draw a rectangle", null));
		actions.add(new ElipseToolAction("Elipse", null, "Draw an elipse", null));

		ModelListener isl = () -> {
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
			if (model.tool.getTool() instanceof LineTool)
				model.tool.unsetTool();
			else
				model.tool.setTool(new LineTool(model, controller));
		}

	}
	public class RectangleToolAction extends ImageAction {

		RectangleToolAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
			super(name, icon, desc, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			if (model.tool.getTool() instanceof RectangleTool)
				model.tool.unsetTool();
			else
				model.tool.setTool(new RectangleTool(model, controller));
		}

	}
	public class ElipseToolAction extends ImageAction {

		ElipseToolAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
			super(name, icon, desc, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			if (model.tool.getTool() instanceof ElipseTool)
				model.tool.unsetTool();
			else
				model.tool.setTool(new ElipseTool(model, controller));
		}

	}

}
