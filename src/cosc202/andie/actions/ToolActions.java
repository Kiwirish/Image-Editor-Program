package cosc202.andie.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

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

		ArrayList<ToolAction> toolActions = new ArrayList<ToolAction>();

		toolActions.add(new SelectToolAction("Select", null, "Select regions", null));
		toolActions.add(new LineToolAction("Line", null, "Draw a line", null));
		toolActions.add(new RectangleToolAction("Rectangle", null, "Draw a rectangle", null));
		toolActions.add(new ElipseToolAction("Elipse", null, "Draw an elipse", null));

		actions.addAll(toolActions);

		ModelListener listener = () -> {
			for (ToolAction toolAction : toolActions) {
				toolAction.updateState(model.tool.getTool(), model.hasImage());
			}
		};

		model.tool.registerActiveToolListener(listener);
		model.registerImageStatusListener(listener);
		listener.update();
	}


	private abstract class ToolAction extends ImageAction {


		protected ToolAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
			super(name, icon, desc, mnemonic);
		}

		protected abstract Class<? extends Tool> getToolClass();

		public void updateState(Tool activeTool, boolean hasImage) {
			setEnabled((activeTool == null || !activeTool.getClass().equals(getToolClass())) && hasImage);
		}
		
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
