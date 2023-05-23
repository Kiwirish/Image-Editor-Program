package cosc202.andie.models;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import cosc202.andie.models.AndieModel.ModelListener;
import cosc202.andie.models.OverlayModel.OverlayDrawer;
import cosc202.andie.tools.Tool;

/**
 * <p>
 * The tool model for ANDIE. Handles the current tool, stroke width, stroke and fill colors, the selection rectangle, and drawing the selection rectangle on the overlay.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">cc by-nc-sa 4.0</a>
 * </p>
 * 
 * @see AndieModel
 * @see Tool
 * 
 * @author Jeb Nicholson
 * @version 1.0
 */
public class ToolModel {
	private AndieModel model;
	private Tool tool;

	private Color fillColor;
	private Color strokeColor;
	private int strokeWidth;

	private Rectangle selection;

	private ArrayList<ModelListener> activeToolListeners = new ArrayList<ModelListener>();
	private ArrayList<ModelListener> selectionListeners = new ArrayList<ModelListener>();

	private OverlayDrawer overlayDrawer;
	private ModelListener workingImageListener;
	private ModelListener imageListener;
	private ModelListener imageStatusListener;
	private Dimension lastImageSize;
	private boolean pauseSelectionPaint;

	private Timer timer;

	/**
	 * Creates a new ToolModel
	 * @param model The base AndieModel
	 */
	public ToolModel(AndieModel model) {
		this.model = model;
		this.timer = new Timer();
		init();

		this.pauseSelectionPaint = false;

		overlayDrawer = new OverlayDrawer() {
			public void drawOverlay(Graphics2D g) {
				if (selection != null && !pauseSelectionPaint) {
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
				}
			}
		};

		model.overlay.registerOverlayDrawer(overlayDrawer);

		TimerTask repaintOverlayTask = new TimerTask(){
			public void run(){
				SwingUtilities.invokeLater(() -> {
					if (selection != null && !pauseSelectionPaint)
						model.overlay.repaint();
				});
			}
		};

		this.timer.schedule(repaintOverlayTask, 0, 1000/20);

		workingImageListener = () -> {
			pauseSelectionPaint = model.isPreviewing();
		};

		model.registerWorkingImageListener(workingImageListener);

		//Restricts selection on image size change
		imageListener = () -> {
			if (model.hasImage()) {
				Dimension imageSize = model.getImage().getSize();
				if (lastImageSize == null || !imageSize.equals(lastImageSize)) {
					restrictSelection();
					lastImageSize = new Dimension(model.getWorkingImage().getWidth(), model.getWorkingImage().getHeight());
				}
			}
		};
		model.registerImageListener(imageListener);

		imageStatusListener = () -> {
			if (!model.hasImage()) {
				init();
			}
		};

		model.registerImageStatusListener(imageStatusListener);

	}

	/** Initializes the values to their defaults, and deactivates the current tool if it is active. */ 
	private void init() {
		if (this.tool != null)
			this.tool.deactivateTool();
		this.tool = null;
		this.strokeColor = Color.BLACK;
		this.fillColor = Color.WHITE;
		this.strokeWidth = 12;
		this.selection = null;
		this.lastImageSize = null;
	}

	 /**
		* Get the currently selected tool
		* @return The currently selected tool
		*/
	public Tool getTool() {
		return tool;
	}

	/**
	 * Sets the current tool
	 * @param tool The tool to set
	 */
	public void setTool(Tool tool) {
		if (this.tool != null) this.tool.deactivateTool();
		this.tool = tool;
		tool.activateTool();
		notifyActiveToolListeners();
	}

	/**
	 * Unsets the current tool (So that no tool is selected)
	 */
	public void unsetTool() {
		tool.deactivateTool();
		tool = null;
		notifyActiveToolListeners();
	}

	/**
	 * Get the current stroke color
	 * @return The current stroke color
	 */
	public Color getStrokeColor() {
		return strokeColor;
	}

	/**
	 * Set the current stroke color
	 * @param strokeColor The color to set
	 */
	public void setStrokeColor(Color strokeColor) {
		this.strokeColor = strokeColor;
	}

	/**
	 * Get the current fill color
	 * @return The current fill color
	 */
	public Color getFillColor() {
		return fillColor;
	}

	/**
	 * Set the current fill color
	 * @param fillColor The color to set
	 */
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	/**
	 * Get the current stroke width
	 * @return
	 */
	public int getStrokeWidth() {
		return strokeWidth;
	}

	/**
	 * Set the current stroke width
	 * @param strokeWidth The width to set
	 */
	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
	}

	/** Unselect the current selection */
	public void unsetSelection() {
		this.selection = null;
		model.overlay.repaint();
		notifySelectionListeners();
	}

	/**
	 * Set the current selection
	 * @param rectangle The selection to set (given as a rectangle relative to the image)
	 */
	public void setSelection(Rectangle rectangle) {
		if (rectangle.width == 0 || rectangle.height == 0) {
			unsetSelection();
			return;
		}
		this.selection = rectangle;
		model.overlay.repaint();
		notifySelectionListeners();
	}

	/**
	 * Get the current selection
	 * @return The current selection
	 */
	public Rectangle getSelection() {
		return selection;
	}
	
	/**
	 * Restrict the current selection to be within the image bounds
	 */
	public void restrictSelection() {
		//Restricts the selection to be within the image bounds
		if (!model.hasImage() || selection == null) return;
		Dimension imageSize = new Dimension(model.getWorkingImage().getWidth(), model.getWorkingImage().getHeight());
		if (selection.x < 0) selection.x = 0;
		if (selection.y < 0) selection.y = 0;
		if (selection.x + selection.width > imageSize.width) selection.width = imageSize.width - selection.x;
		if (selection.y + selection.height > imageSize.height) selection.height = imageSize.height - selection.y;
		if (selection.width == 0 || selection.height == 0) unsetSelection();
		model.overlay.repaint();
	}

	/**
	 * Register a listener to be notified when the active tool changes
	 * @param listener The listener to register
	 */
	public void registerActiveToolListener(ModelListener listener) {
		activeToolListeners.add(listener);
	}
	
	/**
	 * Unregister a listener to be notified when the active tool changes
	 * @param listener The listener to unregister
	 */
	public void unregisterActiveToolListener(ModelListener listener) {
		activeToolListeners.remove(listener);
	}

	/**
	 * Register a listener to be notified when the selection changes
	 * @param listener The listener to register
	 */
	public void registerSelectionListener(ModelListener listener) {
		selectionListeners.add(listener);
	}

	/**
	 * Unregister a listener to be notified when the selection changes
	 * @param listener The listener to unregister
	 */
	public void unregisterSelectionListener(ModelListener listener) {
		selectionListeners.remove(listener);
	}

	/**
	 * Notify all active tool listeners that the active tool has changed
	 */
	public void notifyActiveToolListeners() {
		for (ModelListener listener : activeToolListeners) {
			listener.update();
		}
	}

	/**
	 * Notify all selection listeners that the selection has changed
	 */
	public void notifySelectionListeners() {
		for (ModelListener listener : selectionListeners) {
			listener.update();
		}
	}

	/** List the listeners. Used for debugging purposes */
	public void listListeners() {
		for (ModelListener listener : activeToolListeners) {
			System.out.println("Active Tool Listener: " + listener);
		}
		for (ModelListener listener : selectionListeners) {
			System.out.println("Selection Listener: " + listener);
		}
	}

	/** Notify the ToolModel that it has been removed */
	public void notifyRemove() {
		model.overlay.unregisterOverlayDrawer(overlayDrawer);
		model.unregisterWorkingImageListener(workingImageListener);
		model.unregisterImageListener(imageListener);
		model.unregisterImageStatusListener(imageStatusListener);
		this.timer.cancel();
	}

}
