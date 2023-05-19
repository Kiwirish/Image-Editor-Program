package cosc202.andie;

import javax.swing.JMenuBar;

import cosc202.andie.actions.ColourActions;
import cosc202.andie.actions.EditActions;
import cosc202.andie.actions.FileActions;
import cosc202.andie.actions.FilterActions;
import cosc202.andie.actions.LanguageActions;
import cosc202.andie.actions.MacroActions;
import cosc202.andie.actions.ToolActions;
import cosc202.andie.actions.TransformActions;
import cosc202.andie.actions.ViewActions;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;

public class MenuBar extends JMenuBar {
	AndieController controller;
	AndieModel model;
	public MenuBar(AndieController controller, AndieModel model) {
		super();
		this.controller = controller;
		this.model = model;

		controller.actions.fileActions = new FileActions(controller, model);
		this.add(controller.actions.fileActions.createMenu());
		controller.actions.editActions = new EditActions(controller, model);
		this.add(controller.actions.editActions.createMenu());
		controller.actions.viewActions = new ViewActions(controller, model);
		this.add(controller.actions.viewActions.createMenu());
		controller.actions.transformActions = new TransformActions(controller, model);
		this.add(controller.actions.transformActions.createMenu());
		controller.actions.filterActions = new FilterActions(controller, model);
		this.add(controller.actions.filterActions.createMenu());
		controller.actions.colourActions = new ColourActions(controller, model);
		this.add(controller.actions.colourActions.createMenu());
		controller.actions.toolActions = new ToolActions(controller, model);
		this.add(controller.actions.toolActions.createMenu());
		controller.actions.macroActions =	new MacroActions(controller, model);
		this.add(controller.actions.macroActions.createMenu());
		controller.actions.languageActions = new LanguageActions(controller, model);
		this.add(controller.actions.languageActions.createMenu());

	}

}
