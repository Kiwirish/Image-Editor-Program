package cosc202.andie;

import javax.swing.JMenuBar;


import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.actions.MenuActions;

import cosc202.andie.controllers.ActionsController;

/**
 * <p>
 * The Menubar for Andie. Contains all of the menus for the actions.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @see ActionsController
 * @see MenuActions
 * 
 * @author Jeb Nicholson
 * @version 1.0
 */
public class MenuBar extends JMenuBar {
	AndieController controller;
	AndieModel model;
	public MenuBar(AndieController controller, AndieModel model) {
		super();
		this.controller = controller;
		this.model = model;

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
