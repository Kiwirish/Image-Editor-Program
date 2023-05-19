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

import cosc202.andie.actions.MacroActions.ApplyMacroAction;
import cosc202.andie.actions.MacroActions.RecordMacroAction;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.AndieModel.ModelListener;

public class MacrosPanel extends JPanel {
	private AndieModel model;
	private AndieController controller;

	private ModelListener macrosUpdateListener;
	private JPanel operationsList;
	private JLabel controlsPanelLabel;

	private RecordMacroAction recordMacroAction;
	private ApplyMacroAction applyMacroAction;

	public MacrosPanel(AndieController controller, AndieModel model) {
		super();
		this.model = model;
		this.controller = controller;
		this.setMinimumSize(new Dimension(180,0));
		this.setPreferredSize(this.getMinimumSize());

		initComponents();
		//BUG: MacrosPanel operations persist after closing the file

		macrosUpdateListener = () -> {
			this.setVisible(model.macros.getMacrosViewOpen());
			recalculateOperationsList(model.macros.getMacroOperations());
			this.controlsPanelLabel.setText(model.macros.getRecording() ? "Recording" : "Not Recording");
		};

		model.macros.registerMacrosUpdateListener(macrosUpdateListener);
		macrosUpdateListener.update();
	}

	private void initComponents() {
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		this.setBackground(new Color(0x202020));
		this.setLayout(new BorderLayout());

		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
		titlePanel.setOpaque(false);
		JLabel title = new JLabel("Macros");
		title.setFont(title.getFont().deriveFont(15f).deriveFont(Font.BOLD));
		title.setAlignmentX(CENTER_ALIGNMENT);
		title.setForeground(Color.WHITE);
		titlePanel.add(title);
		this.add(titlePanel, BorderLayout.NORTH);

		operationsList = new JPanel();
		operationsList.setLayout(new SpringLayout());
		operationsList.setBackground(new Color(0x101010));
		this.add(operationsList, BorderLayout.CENTER);

		JPanel controlsPanel = new JPanel();
		controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
		controlsPanel.setOpaque(false);
		controlsPanelLabel = new JLabel("Not currently recording");
		controlsPanelLabel.setForeground(Color.WHITE);
		controlsPanelLabel.setAlignmentX(CENTER_ALIGNMENT);
		controlsPanel.add(controlsPanelLabel);

		recordMacroAction = controller.actions.macroActions.new RecordMacroAction("Start Recording", null, "Start recording a Macro", null);
		applyMacroAction = controller.actions.macroActions.new ApplyMacroAction("Apply a Macro", null, "Apply a macro from a file", null);

		//Add vertical space
		controlsPanel.add(new JLabel(" "));
		JButton recordButton = new JButton(recordMacroAction);
		recordButton.setAlignmentX(CENTER_ALIGNMENT);
		recordButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, recordButton.getMaximumSize().height));
		controlsPanel.add(recordButton);
		JButton applyMacroButton = new JButton(applyMacroAction);
		applyMacroButton.setAlignmentX(CENTER_ALIGNMENT);
		applyMacroButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, applyMacroButton.getMaximumSize().height));
		controlsPanel.add(applyMacroButton);

		this.add(controlsPanel, BorderLayout.SOUTH);
	}

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

	@Override
	public void removeNotify() {
		super.removeNotify();
		model.macros.unregisterMacrosUpdateListener(macrosUpdateListener);
		recordMacroAction.removeNotify();
		applyMacroAction.removeNotify();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)(g.create());
		g2d.setColor(new Color(0xF9F9F9));
		g2d.setStroke(new BasicStroke(3));
		g2d.drawLine(0, 0, 0, this.getHeight());
	}

}
