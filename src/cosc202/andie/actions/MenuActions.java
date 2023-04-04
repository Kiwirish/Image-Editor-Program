package cosc202.andie.actions;

import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import cosc202.andie.ImageAction;

public abstract class MenuActions {
	protected ArrayList<ImageAction> actions;
	protected final String menuTitle;

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
