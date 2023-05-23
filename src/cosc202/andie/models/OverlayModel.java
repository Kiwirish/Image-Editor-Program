package cosc202.andie.models;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import cosc202.andie.models.AndieModel.ModelListener;
import cosc202.andie.tools.SelectTool;


/**
 * <p>
 * The overlay model for ANDIE. Handles the overlay (currently exclusively used for the selection rectangle)
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">cc by-nc-sa 4.0</a>
 * </p>
 * 
 * @see AndieModel
 * @see SelectTool
 * 
 * @author Jeb Nicholson
 * @version 1.0
 */
public class OverlayModel {
	
	private AndieModel model;

	private Dimension size;
	private Rectangle imageBounds;
	private BufferedImage overlayImage;

	private ModelListener imageStatusListener;

	private ArrayList<ModelListener> overlayListeners = new ArrayList<ModelListener>();
	private ArrayList<OverlayDrawer> overlayDrawers = new ArrayList<OverlayDrawer>();

	/**
	 * Creates a new OverlayModel
	 * @param model The base AndieModel
	 */
	public OverlayModel(AndieModel model) {
		this.model = model;
		init();
		imageStatusListener = ()-> { 
				if (!model.hasImage()) 
				init();
		};
		model.registerImageStatusListener(imageStatusListener);
	}

	/** Initialize default values */
	private void init() {
		this.overlayImage = null;
		this.imageBounds = null;
		this.size = null;
	}

	/**
	 * Get the overlay image
	 * @return The overlay image
	 */
	public BufferedImage getOverlay() {
		return overlayImage;
	}

	/**
	 * Set the size of the overlay
	 * @param size The size of the overlay
	 */
	public void setSize(Dimension size) {
		this.size = size;
		overlayImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		repaint();
	}

	/**
	 * Get the size of the overlay
	 * @return The size of the overlay
	 */
	public Dimension getSize() {
		return size;
	}

	/**
	 * Set the bounds of the image within the overlay
	 * @param imageBounds The bounds of the image
	 */
	public void setImageBounds(Rectangle imageBounds) {
		this.imageBounds = imageBounds;
		repaint();
	}

	/**
	 * Get the bounds of the image within the overlay
	 * @return The bounds of the image
	 */
	public Rectangle getImageBounds() {
		return imageBounds;
	}

	/**
	 * Get the scale of the image within the overlay (Multiply by this scale to convert distances from image-space to overlay-space)
	 * @return The scale of the image
	 */
	public double getImageScale() {
		if (imageBounds == null || !model.hasImage()) return 1;
		return (double) imageBounds.width / model.getWorkingImage().getWidth();
	}

	/**
	 * Repaints the overlay
	 */
	public void repaint() {
		if (overlayImage == null) return;
		Graphics2D g = overlayImage.createGraphics();
		//Clear the image 
		g.setPaint(new Color(0, 0, 0, 0));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
		g.fillRect(0, 0, size.width, size.height);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

		for (OverlayDrawer drawer : overlayDrawers) {
			drawer.drawOverlay(g);
		}
		g.dispose();
		for (ModelListener listener : overlayListeners) {
			listener.update();
		}
	}

	/**
	 * Registers an overlay drawer (Which will be used when the overlay is repainted)
	 * @param drawer The overlay drawer to register
	 */
	public void registerOverlayDrawer(OverlayDrawer drawer) {
		overlayDrawers.add(drawer);
	}

	/**
	 * Registers an overlay listener (Which will be notified when the overlay is repainted)
	 * @param listener The overlay listener to register
	 */
	public void registerOverlayListener(ModelListener listener) {
		overlayListeners.add(listener);
	}
	
	/**
	 * Unregisters an overlay listener
	 * @param listener The overlay listener to unregister
	 */
	public void unregisterOverlayListener(ModelListener listener) {
		overlayListeners.remove(listener);
	}

	/**
	 * Unregisters an overlay drawer
	 * @param drawer The overlay drawer to unregister
	 */
	public void unregisterOverlayDrawer(OverlayDrawer drawer) {
		overlayDrawers.remove(drawer);
	}

	/**
	 * Notifies the overlay model that the image has been removed
	 * (Unregisters the image status listener)
	 */
	public void notifyRemove() {
		model.unregisterImageStatusListener(imageStatusListener);
	}

	/** An interface that defines a function for adding to the overlay on redraw */
	public interface OverlayDrawer {
		/** Called when the overlay is being redrawn. Should paint relevant information onto the overlay
		 * @param g The graphics context to draw on
		 */
		public void drawOverlay(Graphics2D g);
	}

	/** List listeners. For debugging purposes */
	public void listListeners() {
		for (ModelListener listener : overlayListeners) {
			System.out.println("OverlayListener: " + listener);
		}
		for (OverlayDrawer drawer : overlayDrawers) {
			System.out.println("OverlayDrawer: " + drawer);
		}
	}
}
