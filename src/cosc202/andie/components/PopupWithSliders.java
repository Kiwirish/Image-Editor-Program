package cosc202.andie.components;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PopupWithSliders {

	private String title;
	private PopupSlider[] sliders;

	public static final int OK = 0;
	public static final int CANCEL = 1;

	public PopupWithSliders(String title, PopupSlider[] sliders) {
		this.title = title;
		this.sliders = sliders;
	}

	public int show() {
		JPanel contents = new JPanel();
		contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
		for (PopupSlider slider: sliders) {
			contents.add(slider);
		}
		int result = JOptionPane.showOptionDialog(null, contents, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

		if (result == JOptionPane.YES_OPTION) return OK;
		return CANCEL;
	}

}
