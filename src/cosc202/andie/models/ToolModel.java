package cosc202.andie.models;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import cosc202.andie.models.AndieModel.ModelListener;
import cosc202.andie.models.OverlayModel.OverlayDrawer;
import cosc202.andie.tools.Tool;

public class ToolModel {
	private AndieModel model;
	private Tool tool;

	private Color fillColor;
	private Color strokeColor;
	private int strokeWidth;

	private Rectangle selection;

	private ArrayList<ModelListener> activeToolListeners = new ArrayList<ModelListener>();

	public ToolModel(AndieModel model) {
		this.model = model;
		this.tool = null;
		this.strokeColor = Color.BLACK;
		this.fillColor = Color.WHITE;
		this.strokeWidth = 12;
		this.selection = null;

		OverlayDrawer overlayDrawer = new OverlayDrawer() {
			public void drawOverlay(Graphics2D g) {
				if (selection != null) {
					Rectangle imageBounds = model.overlay.getImageBounds();
					double imageScale = model.overlay.getImageScale();

					Rectangle adjustedSelection = new Rectangle(
						(int) (selection.x * imageScale)+imageBounds.x,
						(int) (selection.y * imageScale)+imageBounds.y,
						(int) (selection.width * imageScale),
						(int) (selection.height * imageScale)
					);

					g.setColor(Color.gray);
					g.setStroke(new BasicStroke(1));
					g.drawRect(adjustedSelection.x, adjustedSelection.y, adjustedSelection.width, adjustedSelection.height);

					int dashDuration = 400; //ms
					int dashLength = 5;
					float dashPhase = ((float)(dashDuration-(System.currentTimeMillis() % dashDuration)) / dashDuration) * 2 * dashLength ;
					g.setColor(Color.white);
					g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, new float[]{dashLength, dashLength}, dashPhase));
					g.drawRect(adjustedSelection.x, adjustedSelection.y, adjustedSelection.width, adjustedSelection.height);
					// g.setStroke(new BasicStroke(3));
				}
			}
		};

		model.overlay.registerOverlayDrawer(overlayDrawer);

		TimerTask repaintOverlayTask = new TimerTask(){
			public void run(){
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						if (selection != null)
							model.overlay.repaint();
					}
				});
			}
		};

		model.getTimer().schedule(repaintOverlayTask, 0, 1000/20);

	}

	public Tool getTool() {
		return tool;
	}

	public void setTool(Tool tool) {
		if (this.tool != null) this.tool.deactivateTool();
		this.tool = tool;
		tool.activateTool();
		notifyActiveToolListeners();
	}

	public void unsetTool() {
		tool.deactivateTool();
		tool = null;
		notifyActiveToolListeners();
	}

	public Color getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor(Color strokeColor) {
		this.strokeColor = strokeColor;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public int getStrokeWidth() {
		return strokeWidth;
	}

	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	public void unsetSelection() {
		this.selection = null;
		model.overlay.repaint();
	}

	public void setSelection(Rectangle rectangle) {
		if (rectangle.width == 0 || rectangle.height == 0) {
			unsetSelection();
			return;
		}
		this.selection = rectangle;
		model.overlay.repaint();
	}
	
	public void restrictSelection() {
		//Restricts the selection to be within the image bounds
		if (!model.hasImage() || selection == null) return;
		Dimension imageSize = new Dimension(model.getWorkingImage().getWidth(), model.getWorkingImage().getHeight());
		if (selection.x < 0) selection.x = 0;
		if (selection.y < 0) selection.y = 0;
		if (selection.x + selection.width > imageSize.width) selection.width = imageSize.width - selection.x;
		if (selection.y + selection.height > imageSize.height) selection.height = imageSize.height - selection.y;
		model.overlay.repaint();
	}

	public void registerActiveToolListener(ModelListener listener) {
		activeToolListeners.add(listener);
	}
	
	public void unregisterActiveToolListener(ModelListener listener) {
		activeToolListeners.remove(listener);
	}

	public void notifyActiveToolListeners() {
		for (ModelListener listener : activeToolListeners) {
			listener.update();
		}
	}
}
