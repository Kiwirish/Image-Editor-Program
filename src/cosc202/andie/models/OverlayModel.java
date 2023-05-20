package cosc202.andie.models;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import cosc202.andie.models.AndieModel.ModelListener;


public class OverlayModel {
	
	AndieModel model;

	private Dimension size;
	private Rectangle imageBounds;
	private BufferedImage overlayImage;

	private ModelListener imageStatusListener;

	private ArrayList<ModelListener> overlayListeners = new ArrayList<ModelListener>();
	private ArrayList<OverlayDrawer> overlayDrawers = new ArrayList<OverlayDrawer>();

	public OverlayModel(AndieModel model) {
		this.model = model;
		init();
		imageStatusListener = ()-> { 
				if (!model.hasImage()) 
				init();
		};
		model.registerImageStatusListener(imageStatusListener);
	}

	private void init() {
		this.overlayImage = null;
		this.imageBounds = null;
		this.size = null;
	}

	public BufferedImage getOverlay() {
		return overlayImage;
	}

	public void setSize(Dimension size) {
		this.size = size;
		overlayImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		repaint();
	}
	public Dimension getSize() {
		return size;
	}
	public void setImageBounds(Rectangle imageBounds) {
		this.imageBounds = imageBounds;
		repaint();
	}
	public Rectangle getImageBounds() {
		return imageBounds;
	}

	public double getImageScale() {
		if (imageBounds == null || !model.hasImage()) return 1;
		return (double) imageBounds.width / model.getWorkingImage().getWidth();
	}

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

	public void registerOverlayDrawer(OverlayDrawer drawer) {
		overlayDrawers.add(drawer);
	}
	public void registerOverlayListener(ModelListener listener) {
		overlayListeners.add(listener);
	}
	public void unregisterOverlayListener(ModelListener listener) {
		overlayListeners.remove(listener);
	}
	public void unregisterOverlayDrawer(OverlayDrawer drawer) {
		overlayDrawers.remove(drawer);
	}

	public void notifyRemove() {
		model.unregisterImageStatusListener(imageStatusListener);
	}

	public interface OverlayDrawer {
		public void drawOverlay(Graphics2D g);
	}

	public void listListeners() {
		for (ModelListener listener : overlayListeners) {
			System.out.println("OverlayListener: " + listener);
		}
		for (OverlayDrawer drawer : overlayDrawers) {
			System.out.println("OverlayDrawer: " + drawer);
		}
	}
}
