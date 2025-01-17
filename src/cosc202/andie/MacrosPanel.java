package cosc202.andie;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.AndieModel.ModelListener;

import static cosc202.andie.LanguageConfig.msg;

/**
 * <p>
 * The MacrosPanel displays the current macro operations and allows the user to start and stop recording macros.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Jeb Nicholson
 * @version 1.0
 */
public class MacrosPanel extends JPanel {
	private AndieModel model;
	private AndieController controller;

	private ModelListener macrosUpdateListener;
	private JPanel operationsList;
	private JLabel controlsPanelLabel;

	/**
	 * Create a new MacrosPanel. Registers a listener for changes to the macros.
	 * @param controller The base controller
	 * @param model The base model
	 */
	public MacrosPanel(AndieController controller, AndieModel model) {
		super();
		this.model = model;
		this.controller = controller;
		this.setMinimumSize(new Dimension(180,0));
		this.setPreferredSize(this.getMinimumSize());

		initComponents();

		macrosUpdateListener = () -> {
			this.setVisible(model.macros.getMacrosViewOpen());
			recalculateOperationsList(model.macros.getMacroOperations());
			this.controlsPanelLabel.setText(model.macros.getRecording() ? msg("Macros_Recording") : msg("Macros_NotRecording"));
		};

		model.macros.registerMacrosUpdateListener(macrosUpdateListener);
		model.registerImageStatusListener(macrosUpdateListener);
		macrosUpdateListener.update();
	}

	/** Initialize the components of the panel. */
	private void initComponents() {
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.setBackground(new Color(0x202020));
		this.setLayout(new BorderLayout());

		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
		titlePanel.setOpaque(false);
		JLabel title = new JLabel(msg("Macros_Title"));
		title.setFont(title.getFont().deriveFont(15f).deriveFont(Font.BOLD));
		title.setAlignmentX(CENTER_ALIGNMENT);
		title.setForeground(Color.WHITE);
		titlePanel.add(title);
		//Add vertical space
		titlePanel.add(new JLabel(" "));
		this.add(titlePanel, BorderLayout.NORTH);

		operationsList = new JPanel();
		operationsList.setLayout(new SpringLayout());
		operationsList.setBackground(new Color(0x101010));
		this.add(operationsList, BorderLayout.CENTER);

		JPanel controlsPanel = new JPanel();
		controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
		controlsPanel.setOpaque(false);

		//Add vertical space
		controlsPanel.add(new JLabel(" "));

		controlsPanelLabel = new JLabel(msg("Macros_NotRecording"));
		controlsPanelLabel.setForeground(Color.WHITE);
		controlsPanelLabel.setAlignmentX(CENTER_ALIGNMENT);
		controlsPanel.add(controlsPanelLabel);


		//Add vertical space
		controlsPanel.add(new JLabel(" "));
		JButton recordButton = new JButton(controller.actions.macroActions.recordMacroAction);
		recordButton.setAlignmentX(CENTER_ALIGNMENT);
		recordButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, recordButton.getMaximumSize().height));
		controlsPanel.add(recordButton);
		JButton applyMacroButton = new JButton(controller.actions.macroActions.applyMacroAction);
		applyMacroButton.setAlignmentX(CENTER_ALIGNMENT);
		applyMacroButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, applyMacroButton.getMaximumSize().height));
		controlsPanel.add(applyMacroButton);

		this.add(controlsPanel, BorderLayout.SOUTH);
	}

	/**
	 * Recalculate the list of operations to display. Called when the list of operations changes.
	 * @param macroOperations The list of operations to display
	 */
	private void recalculateOperationsList(ArrayList<ImageOperation> macroOperations) {
		operationsList.removeAll();
		if (macroOperations.isEmpty()) {
			operationsList.setVisible(false);
		} else {
			operationsList.setVisible(true);
		}
		JLabel lastLabel = null;
		for (int i = 0; i < macroOperations.size(); i++) {
			ImageOperation operation = macroOperations.get(i);
			JLabel operationLabel = new JLabel(String.format("%d. %s", i + 1, operation.operationDescription()));
			operationLabel.setToolTipText(operation.operationDescription());
			operationLabel.setBackground(i % 2 == 0 ? new Color(0x101010) : new Color(0x202020));
			operationLabel.setForeground(new Color(0xF9F9F9));
			operationLabel.setOpaque(true);
			// operationLabel.setPreferredSize(new Dimension(operationLabel.getPreferredSize().getWidth(), ));
			operationLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			operationLabel.setFont(operationLabel.getFont().deriveFont(10f));
			((SpringLayout)operationsList.getLayout()).putConstraint(SpringLayout.WEST, operationLabel, 0, SpringLayout.WEST, operationsList);
			((SpringLayout)operationsList.getLayout()).putConstraint(SpringLayout.EAST, operationLabel, 0, SpringLayout.EAST, operationsList);
			if (lastLabel != null)
				((SpringLayout)operationsList.getLayout()).putConstraint(SpringLayout.NORTH, operationLabel, 0, SpringLayout.SOUTH, lastLabel);
			operationsList.add(operationLabel);

			lastLabel = operationLabel;
		}
		operationsList.revalidate();
		operationsList.repaint();
	}

	/**
	 * Notifies the panel that it has been orphaned. Unregisters the model listeners
	 */
	@Override
	public void removeNotify() {
		super.removeNotify();
		model.macros.unregisterMacrosUpdateListener(macrosUpdateListener);
		model.unregisterImageStatusListener(macrosUpdateListener);
	}

	/**
	 * Paints the panel. Draws a line on the left side.
	 * @param g The graphics object to paint with
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)(g.create());
		g2d.setColor(new Color(0xF9F9F9));
		g2d.setStroke(new BasicStroke(3));
		g2d.drawLine(0, 0, 0, this.getHeight());
	}

}
