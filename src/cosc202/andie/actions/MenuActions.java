package cosc202.andie.actions;

import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import cosc202.andie.ImageAction;

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

		/**
		 * Create MenuActions, with a given menu title
		 * @param menuTitle The title of the parent menubar item
		 */
    public MenuActions(String menuTitle) {
        actions = new ArrayList<ImageAction>();
				this.menuTitle = menuTitle;
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
        JMenu editMenu = new JMenu(menuTitle);

        for (ImageAction action: actions) {
            editMenu.add(new JMenuItem(action));
        }

				editMenu.addMenuListener(new MenuListener() {
					@Override
					public void menuSelected(MenuEvent e) {
						for (ImageAction action: actions) {
							action.updateState();
						}
					}
					@Override
					public void menuDeselected(MenuEvent e) {}
					@Override
					public void menuCanceled(MenuEvent e) {}

				});

        return editMenu;
    }
}
