package cosc202.andie;

import javax.swing.JMenuBar;

import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;

public class MenuBar extends JMenuBar {
	AndieController controller;
	AndieModel model;
	public MenuBar(AndieController controller, AndieModel model) {
		super();
		this.controller = controller;
		this.model = model;

		// FileActions fileActions = new FileActions(controller, model);
		// this.add(fileActions.createMenu());
		// EditActions editActions = new EditActions(controller, model);
		// this.add(editActions.createMenu());
		// ViewActions viewActions = new ViewActions(controller, model);
		// this.add(viewActions.createMenu());
		// TransformActions sizeActions = new TransformActions(controller, model);
		// this.add(sizeActions.createMenu());
		// FilterActions filterActions = new FilterActions(controller, model);
		// this.add(filterActions.createMenu());
		// ColourActions colourActions = new ColourActions(controller, model);
		// this.add(colourActions.createMenu());
		// ToolActions toolActions = new ToolActions(controller, model);
		// this.add(toolActions.createMenu());
		// MacroActions macroActions =	new MacroActions(controller, model);
		// this.add(macroActions.createMenu());
		// LanguageActions languageActions = new LanguageActions(controller, model);
		// this.add(languageActions.createMenu());
		this.add(controller.actions.fileActions.createMenu());
		this.add(controller.actions.editActions.createMenu());
		this.add(controller.actions.viewActions.createMenu());
		this.add(controller.actions.transformActions.createMenu());
		this.add(controller.actions.filterActions.createMenu());
		this.add(controller.actions.colourActions.createMenu());
		this.add(controller.actions.toolActions.createMenu());
		this.add(controller.actions.macroActions.createMenu());
		this.add(controller.actions.languageActions.createMenu());
	}

}
