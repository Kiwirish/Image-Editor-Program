package cosc202.andie;

import java.awt.Graphics;

import javax.swing.JPanel;

import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;

public class ImagePanel extends JPanel {
	AndieModel model;
	AndieController controller;
	ImagePanel(AndieController controller, AndieModel model) {
		this.controller = controller;
		this.model = model;
		this.model.registerImageListener(() -> {
			this.repaint();
		});
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (model.hasImage()) {
			g.drawImage(model.getImage().getCurrentImage(), 0, 0, this.getWidth(), this.getHeight(), null);
		}
	}
}
