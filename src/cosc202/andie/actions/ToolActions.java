package cosc202.andie.actions;

import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.KeyStroke;

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

	public ToolAction selectToolAction;
	public ToolAction lineToolAction;
	public ToolAction rectangleToolAction;
	public ToolAction elipseToolAction;

	public ToolActions(AndieController controller, AndieModel model) {
		super("Tools", controller, model);

		selectToolAction = new ToolAction("Select", "Select regions", null, null, SelectTool.class);
		lineToolAction = new ToolAction("Line", "Draw a line", null, null, LineTool.class);
		rectangleToolAction = new ToolAction("Rectangle", "Draw a rectangle", null, null, RectangleTool.class);
		elipseToolAction = new ToolAction("Elipse", "Draw an elipse", null, null, ElipseTool.class);

		actions.addAll(Arrays.asList(selectToolAction, lineToolAction, rectangleToolAction, elipseToolAction));

	}


	public class ToolAction extends ImageAction {

		private ModelListener updateListener;
		private Class<? extends Tool> toolClass;

		public ToolAction(String name, String desc, Integer mnemonic, KeyStroke keyboardShortcut, Class<? extends Tool> toolClass) {
			super(name, desc, mnemonic, keyboardShortcut);
			this.toolClass = toolClass;
			updateListener = () -> {
				setEnabled(model.hasImage() && (model.tool.getTool() == null || !model.tool.getTool().getClass().equals(toolClass)));
			};
			model.tool.registerActiveToolListener(updateListener);
			model.registerImageStatusListener(updateListener);
			updateListener.update();
		}

		public void actionPerformed(ActionEvent e) {
			try {
				model.tool.setTool(toolClass.getConstructor(AndieModel.class, AndieController.class).newInstance(model, controller));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void removeNotify() {
			model.tool.unregisterActiveToolListener(updateListener);
			model.unregisterImageStatusListener(updateListener);
		}
		
	}
}
