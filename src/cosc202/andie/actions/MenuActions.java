package cosc202.andie.actions;

import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import cosc202.andie.ImageAction;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;

/**
 * <p>
 * Abstract class for handling top level menus that relate to the image.
 * </p>
 * 
 * <p>
 * Handles the creation of the top level menu, and updating the state of the menu items when the menu is selected.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @see ImageAction
 * @see JMenu
 * @author Jeb Nicholson
 * @version 1.0
 */
public abstract class MenuActions {
	protected ArrayList<ImageAction> actions;
	protected final String menuTitle;
	protected AndieController controller;
	protected AndieModel model;

	/**
	 * Create MenuActions, with a given menu title
	 * @param menuTitle The title of the parent menubar item
	 */
	public MenuActions(String menuTitle, AndieController controller, AndieModel model) {
		actions = new ArrayList<ImageAction>();
		this.menuTitle = menuTitle;
		this.controller = controller;
		this.model = model;
	}

	/**
	 * <p>
	 * Create a menu containing the ImageActions.
	 * </p>
	 * <p>
	 * When the menu is selected, the updateState() method of each ImageAction is called.
	 * </p>
	 * 
	 * @return The the JMenu UI element
	 */
	public JMenu createMenu() {
		JMenu menu = new JMenu(menuTitle) {
			@Override
			public void removeNotify() {
				super.removeNotify();
				MenuActions.this.removeNotify();
			}
		};
		for (ImageAction action: actions) {
			menu.add(new JMenuItem(action));
		}
		return menu;
	}

	/** Called when the parent JMenu is removed */
	public void removeNotify() {
		for (ImageAction action: actions) {
			action.removeNotify();
		}
	}
}
