package cosc202.andie.tools;

import java.awt.Cursor;

import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;

public abstract class Tool {
	protected AndieModel model;
	protected AndieController controller;

	public Tool(AndieModel model, AndieController controller) {
		this.model = model;
		this.controller = controller;
	}
	public Cursor getCursor() {
		return Cursor.getDefaultCursor();
	}
	//Called when the tool is activated
	public void activateTool() {
		controller.setCursor(getCursor());
	}
	//Called when the tool is deactivated
	public void deactivateTool() {
		controller.resetCursor();
	};
}
