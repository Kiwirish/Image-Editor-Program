package cosc202.andie.components;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeListener;

/**
 * <p>
 * A slider with a message label, and a label showing the current value
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">cc by-nc-sa 4.0</a>
 * </p>
 * 
 * @see PopupWithSliders
 * @see JSlider
 * @author Jeb Nicholson
 * @version 1.0
 */
public class PopupSlider extends JPanel {
	
	private JSlider slider;

	/**
	 * Create a new PopupSlider with the given settings
	 * @param message The message to display above the slider
	 * @param minimum The minimum value that the slider may be set to
	 * @param maximum The maximum value that the slider may be set to
	 * @param initialValue The intital value of the slider, between minimum and maximum
	 * @param units The units to display after the value (e.g. "px", or "%")
	 * @param minorTickSpace The spacing between minor ticks
	 * @param majorTickSpace The spacing between major ticks
	 */
	public PopupSlider(String message, int minimum, int maximum, int initialValue, String units, int minorTickSpace, int majorTickSpace) {
		super();
		slider = new JSlider(minimum,maximum,initialValue);
		slider.setMinorTickSpacing(minorTickSpace);
		slider.setMajorTickSpacing(majorTickSpace);
		slider.setPaintTicks(true);
		slider.setSnapToTicks((maximum-minimum) < 50);
	
		JLabel valueLabel = new JLabel();
		//Set valueLabel font to bold, 16pt
		valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 12f));

		ChangeListener listener = e -> {
			valueLabel.setText(String.format("%d%s", slider.getValue(), units));
		};
		listener.stateChanged(null);
		slider.addChangeListener(listener);

		JLabel label = new JLabel(message);
		label.setFont(label.getFont().deriveFont(Font.PLAIN, 12f));

		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);

		layout.putConstraint(SpringLayout.WEST, label, 7, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, label, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, valueLabel, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, valueLabel, -7, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, slider, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, slider, 2, SpringLayout.SOUTH, label);
		layout.putConstraint(SpringLayout.EAST, this, 0, SpringLayout.EAST, slider);
		layout.putConstraint(SpringLayout.SOUTH, this, 0, SpringLayout.SOUTH, slider);

		this.add(label);
		this.add(slider);
		this.add(valueLabel);
	}

	public int getValue() {
		return slider.getValue();
	}

	public void addChangeListener(ChangeListener listener) {
		slider.addChangeListener(listener);
		listener.stateChanged(null);
	}
	public void removeChangeListener(ChangeListener listener) {
		slider.removeChangeListener(listener);
	}
}
