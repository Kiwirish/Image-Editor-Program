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
import cosc202.andie.tools.SelectTool;
import cosc202.andie.tools.Tool;

public class ToolActions extends MenuActions {

	public ToolActions(AndieController controller, AndieModel model) {
		super("Tools", controller, model);

		actions.add(new SelectToolAction("Select", null, "Select regions", null));
		actions.add(new LineToolAction("Line", null, "Draw a line", null));
		actions.add(new RectangleToolAction("Rectangle", null, "Draw a rectangle", null));
		actions.add(new ElipseToolAction("Elipse", null, "Draw an elipse", null));
	}


	private abstract class ToolAction extends ImageAction {

		private ModelListener updateListener;

		protected ToolAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
			super(name, icon, desc, mnemonic);
			updateListener = () -> {
				setEnabled(model.hasImage() && (model.tool.getTool() == null || !model.tool.getTool().getClass().equals(getToolClass())));
			};
			model.tool.registerActiveToolListener(updateListener);
			model.registerImageStatusListener(updateListener);
			updateListener.update();
		}

		@Override
		public void removeNotify() {
			model.tool.unregisterActiveToolListener(updateListener);
			model.unregisterImageStatusListener(updateListener);
		}

		protected abstract Class<? extends Tool> getToolClass();
		
	}

	public class SelectToolAction extends ToolAction {

		SelectToolAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
			super(name, icon, desc, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			model.tool.setTool(new SelectTool(model, controller));
		}

		protected Class<? extends Tool> getToolClass() { return SelectTool.class; }

	}

	public class LineToolAction extends ToolAction {

		LineToolAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
			super(name, icon, desc, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			model.tool.setTool(new LineTool(model, controller));
		}

		protected Class<? extends Tool> getToolClass() { return LineTool.class; }

	}
	public class RectangleToolAction extends ToolAction {

		RectangleToolAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
			super(name, icon, desc, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			model.tool.setTool(new RectangleTool(model, controller));
		}

		protected Class<? extends Tool> getToolClass() { return RectangleTool.class; }

	}
	public class ElipseToolAction extends ToolAction {

		ElipseToolAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
			super(name, icon, desc, mnemonic);
		}

		public void actionPerformed(ActionEvent e) {
			model.tool.setTool(new ElipseTool(model, controller));
		}

		protected Class<? extends Tool> getToolClass() { return ElipseTool.class; }

	}

}
