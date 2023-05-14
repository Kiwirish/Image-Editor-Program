package cosc202.andie;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import cosc202.andie.components.ImagePanView;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.AndieModel.ModelListener;

public class ImagePanel extends JPanel {
	private ImagePanView ipv;

	public ImagePanel(AndieController controller, AndieModel model) {
		super();
		this.setLayout(new BorderLayout());

		ipv = new ImagePanView(model.getWorkingImage());
		controller.registerZoomListener(ipv);

		add(ipv, BorderLayout.CENTER);

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

		ModelListener wil = () -> {
			if (model.hasImage() && ipv != null) {
			ipv.updateImage(model.getWorkingImage());
			}
		};

		model.registerWorkingImageListener(wil);
	}
}
