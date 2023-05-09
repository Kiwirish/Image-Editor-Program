package cosc202.andie.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

import cosc202.andie.controllers.AndieController.ManualZoomListener;

public class PanView extends JPanel implements MouseWheelListener, MouseMotionListener, ComponentListener, ManualZoomListener {
	JPanel cp;
	JComponent content;
	JScrollPane sp;
	double zoom = 1.0;
	Point mousePositionInThis;
	Point2D.Double mouseIpRatio;
	Dimension contentSize;
	double zoomFactor;

	public PanView(JComponent content, Dimension contentSize) {
		super();
		this.setLayout(new BorderLayout());

		cp = new JPanel();
		cp.setBackground(new Color(0x222222));
		cp.setLayout(new GridBagLayout());

		this.content = content;
		cp.add(content, new GridBagConstraints());

		sp = new JScrollPane(cp);
		sp.setBorder(null);
		sp.getHorizontalScrollBar().setUI(null);
		sp.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
		sp.getVerticalScrollBar().setUI(null);
		sp.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

		cp.addMouseWheelListener(this);
		cp.addMouseMotionListener(this);

		this.addComponentListener(this);

		this.add(sp, BorderLayout.CENTER);
		this.contentSize = contentSize;
		SwingUtilities.invokeLater(() -> {
			recalculateZoomFactor();
			resetView();
			mousePositionInThis = new Point(this.getWidth() / 2, this.getHeight() / 2);
		});
	}

	private void setZoom(double zoom) {
		this.zoom = zoom;

		int ipWidth = (int) (contentSize.width * zoom * zoomFactor);
		int ipHeight = (int) (contentSize.height * zoom * zoomFactor);
		int cpWidth = ipWidth + this.getWidth() * 2;
		int cpHeight = ipHeight + this.getHeight() * 2;

		content.setPreferredSize(new Dimension(ipWidth, ipHeight));
		cp.setPreferredSize(new Dimension(cpWidth, cpHeight));
		cp.revalidate();
	}


	private void syncMousePosition() {
		if (mousePositionInThis == null) return; 
		SwingUtilities.invokeLater(() -> {
			Point newMouseInIp = new Point((int) (mouseIpRatio.x * content.getWidth()),
					(int) (mouseIpRatio.y * content.getHeight()));
			Point newMouseInCp = SwingUtilities.convertPoint(content, newMouseInIp, cp);
			Point newViewportDifference = new Point(newMouseInCp.x - mousePositionInThis.x,
					newMouseInCp.y - mousePositionInThis.y);
			sp.getViewport().setViewPosition(newViewportDifference);
		});
	}

	private double getZoom() {
		return this.zoom;
	}

	//TODO: //BUG: why is it that 3 invokeLater's are needed to do the intial view reset? 
	//TODO: //BUG: The image is jerky when zooming in and out. 

	public void resetView() {
		zoom = 1;
		setZoom(zoom);
		SwingUtilities.invokeLater(() -> {
		SwingUtilities.invokeLater(() -> {
		SwingUtilities.invokeLater(() -> {
			// centers the viewport
			JViewport vp = sp.getViewport();
			Rectangle viewRect = vp.getViewRect();
			Dimension viewSize = vp.getViewSize();
			Point p = new Point((viewSize.width - viewRect.width) / 2, (viewSize.height - viewRect.height) / 2);
			vp.setViewPosition(p);

			SwingUtilities.invokeLater(() -> {
				updateMouseInIpRatio();
			});
		});
		});
		});
	}

	public void setContentSize(Dimension size) {
		contentSize = size;
		recalculateZoomFactor();
		setZoom(zoom);
	}

	private void recalculateZoomFactor() {
		int margin = 20;
		double widthFactor = (double) Math.max(this.getWidth() - margin,1) / contentSize.width;
		double heightFactor = (double) Math.max(this.getHeight() - margin,1) / contentSize.height;
		zoomFactor = Math.min(widthFactor, heightFactor);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
		if ((isWindows && !e.isControlDown()) || (!isWindows && !e.isMetaDown())) {
			sp.dispatchEvent(e);
			updateMouseInIpRatio();
			return;
		}
		double rotation = e.getPreciseWheelRotation();
		double currentLog = Math.log(getZoom() / 100);
		currentLog += rotation * 0.02;
		double newZoom = Math.exp(currentLog) * 100;

		setZoom(newZoom);
		syncMousePosition();
	}

	private void updateMouseInIpRatio() {
		if (mousePositionInThis == null) return; 
		Point mouseInIp = SwingUtilities.convertPoint(this, mousePositionInThis, content);
		double mouseIpRatioX = (double) mouseInIp.x / content.getWidth();
		double mouseIpRatioY = (double) mouseInIp.y / content.getHeight();
		mouseIpRatio = new Point2D.Double(mouseIpRatioX, mouseIpRatioY);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mousePositionInThis = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), this);
		updateMouseInIpRatio();
	}

	@Override
	public void componentResized(ComponentEvent e) {
		recalculateZoomFactor();
		setZoom(zoom);
	}

	@Override
	public void manualZoomIn() {
		setZoom(zoom+0.1);
		updateMouseInIpRatio();
	}

	@Override
	public void manualZoomOut() {
		setZoom(zoom-0.1);
		updateMouseInIpRatio();
	}

	@Override
	public void manualResetZoom() {
		resetView();
	}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void componentMoved(ComponentEvent e) { }

	@Override
	public void componentShown(ComponentEvent e) { }

	@Override
	public void componentHidden(ComponentEvent e) { }


}
