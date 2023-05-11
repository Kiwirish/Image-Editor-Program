package cosc202.andie.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import cosc202.andie.controllers.AndieController.ManualZoomListener;

public class ImagePanView extends JPanel
		implements MouseWheelListener, MouseMotionListener, ComponentListener, ManualZoomListener {

	private Point2D.Double mousePosition;
	private Point2D.Double viewportOffset;
	private BufferedImage image;
	private double zoom;
	private Dimension imageDimensions;

	private static final int SCROLL_TYPE_PAN = 0;
	private static final int SCROLL_TYPE_ZOOM = 1;

	private double lastRotation = 0;
	private int lastScrollType = SCROLL_TYPE_PAN;
	private long lastScrollTime = 0;

	public ImagePanView(BufferedImage image) {
		super();

		this.image = image;
		this.imageDimensions = new Dimension(image.getWidth(), image.getHeight());

		this.addMouseWheelListener(this);
		this.addMouseMotionListener(this);
		this.addComponentListener(this);
		this.setBackground(new Color(0x1E1E1E));

		viewportOffset = new Point2D.Double(0, 0);
		mousePosition = new Point2D.Double(0, 0);
		this.zoom = 1;

		SwingUtilities.invokeLater(() -> {
			SwingUtilities.invokeLater(() -> {
				resetView();
			});
		});
	}

	/**
	 * <p>Updates the image to be displayed by the view, and repaints the view.</p>
	 * <p>It will center the viewport if the new image is of different dimensions.</p>
	 * @param newImage The new image to display
	 */
	public void updateImage(BufferedImage newImage) {
		boolean sizeChanged = newImage.getWidth() != imageDimensions.getWidth() || newImage.getHeight() != imageDimensions.getHeight();
		this.imageDimensions = new Dimension(newImage.getWidth(), newImage.getHeight());
		this.image = newImage;
		if (sizeChanged)
			centerViewport();
		repaint();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		double rotation = e.getPreciseWheelRotation();

		/*
		 * MacOS scrolling interia causes problems. Ideally, it could be disabled, but
		 * this doesn't appear to be possible.
		 * There is no built in way to differentiate between MouseWheelEvents generated
		 * by the user and those generated by the MacOS's mouse inertia. Instead, we
		 * make an educated guess based on how the mouse rotation compares to it's last
		 * value.
		 * If there is a long gap between events, we assume that an event comes from the
		 * user.
		 */

		int scrollType = lastScrollType;
		long time = System.currentTimeMillis();
		double rotationDelta = Math.abs(rotation - lastRotation); // In inertial scrolling,
		double rotationDeltaPercent = lastRotation != 0 ? Math.abs(rotationDelta / lastRotation) : 1;
		boolean possiblyInteria = (rotationDelta < 0.3 || rotationDeltaPercent < 0.2) && (time - lastScrollTime) < 400;
		if (!possiblyInteria) {
			boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
			scrollType = ((isWindows && e.isControlDown()) || (!isWindows && e.isMetaDown()) && !e.isShiftDown())
					? SCROLL_TYPE_ZOOM
					: SCROLL_TYPE_PAN;
			lastScrollType = scrollType;

		}
		lastRotation = rotation;
		lastScrollTime = time;

		if (scrollType == SCROLL_TYPE_PAN) {
			Point2D.Double pan = null;
			if (e.getModifiersEx() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
				pan = new Point2D.Double(0, rotation * -5);
			} else if (e.getModifiersEx() == MouseWheelEvent.SHIFT_DOWN_MASK) {
				pan = new Point2D.Double(rotation * -5, 0);
			}
			if (pan == null)
				return;

			moveViewport(pan);
		} else if (scrollType == SCROLL_TYPE_ZOOM) {
			zoomByLinear(rotation * 0.02, mousePosition);
		}
	}

	private void zoomByLinear(double linearDeltaZoom, Point2D.Double zoomCenter) {
		double currentLog = Math.log(this.zoom / 100);
		currentLog += linearDeltaZoom;
		double newZoom = Math.exp(currentLog) * 100;
		updateZoom(newZoom, zoomCenter);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Point mousePos = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), this);
		mousePosition = new Point2D.Double(mousePos.getX(), mousePos.getY());
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// If the mousePosition is 0,0 then the mouse may not have been moved, so set it
		// to the center of the screen as a good default
		if (mousePosition.getX() == 0 && mousePosition.getY() == 0)
			mousePosition = new Point2D.Double(this.getWidth() / 2, this.getHeight() / 2);

		updateViewport(viewportOffset);
	}

	private void centerViewport() {
		Point2D.Double thisSize = new Point2D.Double(this.getWidth(), this.getHeight());
		Point2D.Double imageSize = new Point2D.Double(image.getWidth(), image.getHeight());
		Point2D.Double viewportOffset = new Point2D.Double((thisSize.getX() - imageSize.getX() * this.zoom) / 2,
				(thisSize.getY() - imageSize.getY() * this.zoom) / 2);
		updateViewport(viewportOffset);
	}

	public void resetView() {
		Point2D.Double margin = new Point2D.Double(20, 20);
		Point2D.Double thisSize = new Point2D.Double(this.getWidth(), this.getHeight());
		Point2D.Double imageSize = new Point2D.Double(image.getWidth(), image.getHeight());
		double newZoom = Math.min((thisSize.getX() - 2 * margin.getX()) / imageSize.getX(),
				(thisSize.getY() - 2 * margin.getY()) / imageSize.getY());

		this.zoom = getZoomClamp(newZoom);
		centerViewport();
		repaint();
	}

	private double getZoomClamp(double newZoom) {
		double minZoom = 100f / Math.max(image.getWidth(), image.getHeight());
		double maxZoom = 16;
		return Math.min(Math.max(newZoom, minZoom), maxZoom);
	}

	private void updateZoom(double newZoom, Point2D.Double anchorPos) {
		double zoomBoundary = getZoomClamp(newZoom);
		// The zoom clamp shouldn't affect zooms going in the right direction
		if (newZoom > this.zoom){ //Zooming in 
			if (this.zoom >= zoomBoundary) return; //not allowed
			if (newZoom >= zoomBoundary) newZoom = zoomBoundary; //clamp to boundary
		}else if (newZoom < this.zoom){ //Zooming out
			if (this.zoom <= zoomBoundary) return; //not allowed
			if (newZoom <= zoomBoundary) newZoom = zoomBoundary; //clamp to boundary
		}


		Point2D.Double anchorInImage = new Point2D.Double(anchorPos.getX() - viewportOffset.getX(),
				anchorPos.getY() - viewportOffset.getY());
		Point2D.Double imageSize = new Point2D.Double(image.getWidth() * zoom, image.getHeight() * zoom);
		Point2D.Double anchorInImagePercent = new Point2D.Double(anchorInImage.getX() / imageSize.getX(),
				anchorInImage.getY() / imageSize.getY());

		Point2D.Double newImageSize = new Point2D.Double(image.getWidth() * newZoom, image.getHeight() * newZoom);
		Point2D.Double newAnchorInImage = new Point2D.Double(newImageSize.getX() * anchorInImagePercent.getX(),
				newImageSize.getY() * anchorInImagePercent.getY());
		Point2D.Double newViewportOffset = new Point2D.Double(anchorPos.getX() - newAnchorInImage.getX(),
				anchorPos.getY() - newAnchorInImage.getY());

		this.zoom = newZoom;
		updateViewport(newViewportOffset);
		repaint();
	}

	private void moveViewport(Double pan) {
		Point2D.Double newViewportOffset = new Point2D.Double(viewportOffset.getX() + pan.getX(),
				viewportOffset.getY() + pan.getY());

		updateViewport(newViewportOffset);
		repaint();
	}

	private void updateViewport(Point2D.Double newViewportOffset) {
		Point2D.Double thisSize = new Point2D.Double(this.getWidth(), this.getHeight());
		Point2D.Double imageSize = new Point2D.Double(image.getWidth() * zoom, image.getHeight() * zoom);

		Point2D.Double clampedViewportOffset = new Point2D.Double(
				Math.min(Math.max(newViewportOffset.getX(), -imageSize.getX()), thisSize.getX()),
				Math.min(Math.max(newViewportOffset.getY(), -imageSize.getY()), thisSize.getY()));
		viewportOffset = clampedViewportOffset;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.image, (int) viewportOffset.getX(), (int) viewportOffset.getY(),
				(int) (this.image.getWidth() * zoom), (int) (this.image.getHeight() * zoom), null);
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void manualZoomIn() {
		Point2D.Double thisSize = new Point2D.Double(this.getWidth(), this.getHeight());
		Point2D.Double thisCenter = new Point2D.Double(thisSize.getX() / 2, thisSize.getY() / 2);
		zoomByLinear(0.5, thisCenter);
	}

	@Override
	public void manualZoomOut() {
		Point2D.Double thisSize = new Point2D.Double(this.getWidth(), this.getHeight());
		Point2D.Double thisCenter = new Point2D.Double(thisSize.getX() / 2, thisSize.getY() / 2);
		zoomByLinear(-0.5, thisCenter);
	}

	@Override
	public void manualResetZoom() {
		resetView();
	}

}