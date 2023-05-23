package cosc202.andie;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import cosc202.andie.components.ImagePanView;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.AndieModel.ModelListener;

/**
 * <p>
 * This class is responsible for displaying the image, overlay image, the zooming, and handling the mouse.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Jeb Nicholson
 * @version 2.0
 */
public class ImagePanel extends JLayeredPane {
	private ImagePanView ipv;
	private JPanel overlayPanel;
	private BufferedImage overlayImage;

	private ModelListener workingImageListener;
	private ModelListener overlayImageListener;
	private ModelListener cursorListener;

	private AndieModel model;
	private AndieController controller;

	/**
	 * Create a new ImagePanel.
	 * <p>Handles mouse events, and listens for changes to the working image and overlay image.</p>
	 * @param controller The base controller
	 * @param model The base model
	 */
	public ImagePanel(AndieController controller, AndieModel model) {
		super();
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		this.model = model;
		this.controller = controller;

		ipv = new ImagePanView(model.getWorkingImage());

		overlayPanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (overlayImage != null) {
					g.drawImage(overlayImage, 0, 0, null);
				}
			}
		};
		overlayPanel.setOpaque(false);

		add(ipv, Integer.valueOf(1));
		layout.putConstraint(SpringLayout.NORTH, ipv, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, ipv, 0, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, ipv, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, ipv, 0, SpringLayout.WEST, this);
		add(overlayPanel, Integer.valueOf(2));
		layout.putConstraint(SpringLayout.NORTH, overlayPanel, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.SOUTH, overlayPanel, 0, SpringLayout.SOUTH, this);
		layout.putConstraint(SpringLayout.EAST, overlayPanel, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, overlayPanel, 0, SpringLayout.WEST, this);

		controller.registerZoomListener(ipv);

		ipv.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) { model.mouse.mouseDragged(ipv.convertPoint(e.getPoint()), e); }
			public void mouseMoved(MouseEvent e) { model.mouse.mouseMoved(ipv.convertPoint(e.getPoint()), e); }
		});
		ipv.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) { model.mouse.mouseClicked(ipv.convertPoint(e.getPoint()), e); }
			public void mousePressed(MouseEvent e) { model.mouse.mouseDown(ipv.convertPoint(e.getPoint()), e); }
			public void mouseReleased(MouseEvent e) { model.mouse.mouseUp(ipv.convertPoint(e.getPoint()), e); }
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});

		workingImageListener = () -> {
			if (model.hasImage()) 
				ipv.updateImage(model.getWorkingImage());
		};
		workingImageListener.update();
		model.registerWorkingImageListener(workingImageListener);

		overlayImageListener = () -> {
			overlayImage = model.overlay.getOverlay();
			overlayPanel.repaint();
		};
		overlayImageListener.update();
		model.overlay.registerOverlayListener(overlayImageListener);

		cursorListener = () -> {
			this.setCursor(model.mouse.getCursor());
		};

		cursorListener.update();
		model.mouse.registerCursorListener(cursorListener);

		//listen for component resize
		overlayPanel.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent evt) {
				model.overlay.setSize(overlayPanel.getSize());
			}
		});	

		ipv.registerViewRectListener((Rectangle rect) -> {
			model.overlay.setImageBounds(rect);
		});

	}

	/**
	 * Notify the ImagePanel that it is being removed from the GUI.
	 * <p> Removes the listeners from the model and controller.</p>
	 */
	public void removeNotify() {
		super.removeNotify();
		model.unregisterWorkingImageListener(workingImageListener);
		model.overlay.unregisterOverlayListener(overlayImageListener);
		controller.unregisterZoomListener(ipv);
		model.mouse.unregisterCursorListener(cursorListener);
	}
}
