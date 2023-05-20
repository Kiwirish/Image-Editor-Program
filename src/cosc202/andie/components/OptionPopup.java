package cosc202.andie.components;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 * <p>
 * A dialog box with a number of sliders
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">cc by-nc-sa 4.0</a>
 * </p>
 * 
 * @see PopupSlider
 * @see JOptionPane
 * @author Jeb Nicholson
 * @version 1.0
 */
public class OptionPopup {

	/** The title of the dialog window */
	private String title;
	/** The sliders to show */
	private JComponent[] components;
	/** The parent component of the dialog window */
	private Component parent;


	public static final int OK = 0;
	public static final int CANCEL = 1;


	/**
	 * Create a new PopupWithSliders with the given settings
	 * @param parent The parent component of the dialog window
	 * @param title The title of the dialog window
	 * @param sliders The sliders to show
	 */
	public OptionPopup(Component parent, String title, JComponent[] components) {
		this.title = title;
		this.components = components;
		this.parent = parent;
	}

	/**
	 * Trigger the display of the popup
	 * @return {@link #OK} if the user clicked OK, {@link #CANCEL} if the user clicked cancel or closed the window.
	 */
	public int show() {
		JPanel contents = new JPanel();
		contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
		for (JComponent component: components) {
			contents.add(component);
		}
		int result = JOptionPane.showOptionDialog(parent, contents, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

		if (result == JOptionPane.YES_OPTION) return OK;
		return CANCEL;
	}

}
