package cosc202.andie.tools;

import java.awt.Cursor;

import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;

import cosc202.andie.models.ToolModel;

/**
 * <p>
 * The Tool class is an abstract class that all tools in Andie extend.
 * </p>
 * 
 * <p>
 * Tools support a custom cursor, and are "activated" and "deactivated" when they become/stop being the active tool.
 * Passed the controller and the model, tools can do almost anything while active.
 * <p> 
 * 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">cc by-nc-sa 4.0</a>
 * </p>
 * 
 * @see AndieModel
 * @see ElipseTool
 * @see RectangleTool
 * @see LineTool
 * @see ToolModel
 * 
 * @author Jeb Nicholson
 * @version 1.0
 */
public abstract class Tool {
	protected AndieModel model;
	protected AndieController controller;

	/**
	 * Create a new Tool
	 * @param model The base AndieModel
	 * @param controller The base AndieController
	 */
	public Tool(AndieModel model, AndieController controller) {
		this.model = model;
		this.controller = controller;
	}

	/**
	 * Get the cursor that this tool would like to use when hovering the image panel
	 * @return The cursor
	 */
	public Cursor getCursor() {
		return Cursor.getDefaultCursor();
	}

	/**
	 * Activate the tool (Sets up listeners and sets cursor)
	 */
	public void activateTool() {
		controller.setCursor(getCursor());
	}

	/**
	 * Deactivate the tool (Removes listeners and resets cursor)
	 */
	public void deactivateTool() {
		controller.resetCursor();
	};
}
