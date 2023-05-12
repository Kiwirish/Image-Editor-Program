package cosc202.andie;

import javax.swing.JMenuBar;

import cosc202.andie.actions.ColourActions;
import cosc202.andie.actions.EditActions;
import cosc202.andie.actions.FileActions;
import cosc202.andie.actions.FilterActions;
import cosc202.andie.actions.LanguageActions;
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

		FileActions fileActions = new FileActions(controller, model);
		this.add(fileActions.createMenu());
		EditActions editActions = new EditActions(controller, model);
		this.add(editActions.createMenu());
		ViewActions viewActions = new ViewActions(controller, model);
		this.add(viewActions.createMenu());
		TransformActions sizeActions = new TransformActions(controller, model);
		this.add(sizeActions.createMenu());
		FilterActions filterActions = new FilterActions(controller, model);
		this.add(filterActions.createMenu());
		ColourActions colourActions = new ColourActions(controller, model);
		this.add(colourActions.createMenu());
		LanguageActions languageActions = new LanguageActions(controller, model);
		this.add(languageActions.createMenu());
	}

}
