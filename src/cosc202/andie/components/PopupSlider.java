package cosc202.andie.components;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeListener;

public class PopupSlider extends JPanel {
	
	private JSlider slider;

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

}
