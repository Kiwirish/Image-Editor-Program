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
 * @see OptionPopup
 * @see JSlider
 * @author Jeb Nicholson
 * @version 1.0
 */
public class PopupSlider extends JPanel {
	
	private JSlider slider;
	private int lastSliderValue;
	private int minimum;
	private int stepSize;

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
	public PopupSlider(String message, int minimum, int maximum, int initialValue, String units, int minorTickSpace, int majorTickSpace, int stepSize) {
		super();
		if ((maximum-minimum) % stepSize != 0 || (initialValue-minimum) % stepSize != 0 || minorTickSpace % stepSize != 0 || majorTickSpace % stepSize != 0) {
			throw new IllegalArgumentException("All values must be divisible by stepSize");
		}
		slider = new JSlider(0,(maximum-minimum)/stepSize,(initialValue-minimum)/stepSize);
		lastSliderValue = (initialValue-minimum)/stepSize;
		slider.setMinorTickSpacing(minorTickSpace/stepSize);
		slider.setMajorTickSpacing(majorTickSpace/stepSize);
		slider.setPaintTicks(true);
		slider.setSnapToTicks(((maximum-minimum)/stepSize) < 50);
		this.minimum = minimum;
		this.stepSize = stepSize;
	
		JLabel valueLabel = new JLabel();
		valueLabel.setFont(valueLabel.getFont().deriveFont(Font.BOLD, 12f));

		ChangeListener listener = e -> {
			valueLabel.setText(String.format("%d%s", this.getValue(), units));
		};
		slider.addChangeListener(listener);
		listener.stateChanged(null);

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
		return slider.getValue()*stepSize+minimum;
	}

	public void addChangeListener(ChangeListener listener) {
		slider.addChangeListener((ev)->{
			if (slider.getValue()!=lastSliderValue) {
				lastSliderValue = slider.getValue();
				listener.stateChanged(ev);
			}
		});
	}
	public void removeChangeListener(ChangeListener listener) {
		slider.removeChangeListener(listener);
	}
}
