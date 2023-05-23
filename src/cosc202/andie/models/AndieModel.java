package cosc202.andie.models;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.EventListener;

import javax.imageio.ImageIO;


import cosc202.andie.ImageOperation;
import cosc202.andie.Utils;
import cosc202.andie.Utils.ExtensionException;
import cosc202.andie.models.EditableImage.ImageListener;
import cosc202.andie.models.EditableImage.OperationListener;

/**
 * <p>
 * The main model for ANDIE. Keeps track of ANDIE's state, including window information, the current image, and every other model.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">cc by-nc-sa 4.0</a>
 * </p>
 * 
 * @see EditableImage
 * @see Operations
 * @see MouseModel
 * @see OverlayModel
 * @see ToolModel
 * @see MacrosModel
 * 
 * @author Jeb Nicholson
 * @version 1.0
 */
public class AndieModel {

	/** True if running on Mac OS */
	public static boolean IS_MAC = System.getProperty("os.name").toLowerCase().startsWith("mac");
	public static int CONTROL = IS_MAC ? InputEvent.META_DOWN_MASK : InputEvent.CTRL_DOWN_MASK;

	private EditableImage image;
	private BufferedImage previewImage;
	private boolean isImageOpen = false;
	private String imageFilepath;

	private Dimension frameSize;
	private Point frameLocation;

	private ArrayList<ModelListener> imageStatusListeners = new ArrayList<ModelListener>();
	private ArrayList<ModelListener> workingImageListeners = new ArrayList<ModelListener>();
	private ArrayList<ModelListener> imageListeners = new ArrayList<ModelListener>();
	private ArrayList<ModelListener> filepathListeners = new ArrayList<ModelListener>();
	private ArrayList<OperationListener> imageOperationListeners = new ArrayList<OperationListener>();

	private ImageListener imageListener;
	private OperationListener imageOperationListener;

	/** The operations model */
	public Operations operations;
	/** The mouse model */
	public MouseModel mouse;
	/** The overlay model */
	public OverlayModel overlay;
	/** The tool model */
	public ToolModel tool;
	/** The macros model */
	public MacrosModel macros;

	/** Create a new AndieModel */
	public AndieModel() {
		this.operations = new Operations(this);
		this.mouse = new MouseModel(this);
		this.overlay = new OverlayModel(this);
		this.tool = new ToolModel(this);
		this.macros = new MacrosModel(this);
		init();
	}

	/** Initialize the model without an image */
	public void init() {
		image = null;
		previewImage = null;
		isImageOpen = false;
		imageFilepath = null;

		notifyListeners(imageStatusListeners);
	}

	/**
	 * Get the current image's filepath;
	 * @return The current image's filepath
	 */
	public String getImageFilepath() {
		return imageFilepath;
	}

	/**
	 * Get the size of ANDIE's frame
	 * @return The size of ANDIE's frame
	 */
	public Dimension getFrameSize() {
		return frameSize;
	}

	/**
	 * Set the size of ANDIE's frame
	 * @param frameSize The size of ANDIE's frame
	 */
	public void setFrameSize(Dimension frameSize) {
		this.frameSize = frameSize;
	}

	/**
	 * Get the location of ANDIE's frame
	 * @return The location of ANDIE's frame
	 */
	public Point getFrameLocation() {
		return frameLocation;
	}

	/**
	 * Set the location of ANDIE's frame
	 * @param frameLocation The location of ANDIE's frame
	 */
	public void setFrameLocation(Point frameLocation) {
		this.frameLocation = frameLocation;
	}

	/**
	 * Get the current {@link EditableImage}
	 * @return The current {@link EditableImage}
	 */
	public EditableImage getImage() {
		return image;
	}

	/**
	 * <p>Get the current working image (The image that should be displayed)</p>
	 * <p>If a preview image is available, it will be returned. Otherwise, the current image will be returned.</p>
	 * @return The current working image
	 */
	public BufferedImage getWorkingImage() {
		if (previewImage != null)
			return previewImage;
		return image.getCurrentImage();
	}

	/**
	 * Is there an image open?
	 * @return True if there is an image open
	 */
	public boolean hasImage() {
		return isImageOpen;
	}

	/**
	 * Is there a preview image?
	 * @return True if there is a preview image
	 */
	public boolean isPreviewing() {
		return previewImage != null;
	}

	/** Closes the current image and notifies the relevant listeners */
	public void closeImage() {
		image.unregisterImageListener(imageListener);
		image.unregisterOperationListener(imageOperationListener);
		init();
		notifyListeners(imageStatusListeners);
		notifyListeners(workingImageListeners);
		notifyListeners(imageListeners);
		notifyListeners(filepathListeners);
	}

	/**
	 * Open a new image and notifies the relevant listeners
	 * @param filepath The filepath of the image to open
	 * @throws IOException If there is an error reading the image
	 */
	public void openImage(String filepath) throws IOException {
		// Open image at filepath
		BufferedImage newImage = ImageIO.read(new File(filepath));
		File operationsFile = new File(filepath + ".ops");
		String opsString = "";
		if (operationsFile.exists()) {
			try {
				opsString = Utils.readString(operationsFile, Charset.defaultCharset());
			} catch (IOException e) {
				opsString = "";
			}
		}

		image = opsString != null ? new EditableImage(newImage, opsString) : new EditableImage(newImage);

		this.imageFilepath = filepath;
		isImageOpen = true;
		notifyListeners(imageStatusListeners);
		notifyListeners(workingImageListeners);
		notifyListeners(imageListeners);
		notifyListeners(filepathListeners);

		imageListener = () -> {
			previewImage = null;
			notifyListeners(workingImageListeners);
			notifyListeners(imageListeners);
		};

		image.registerImageListener(imageListener);

		imageOperationListener = new OperationListener() {
			public void operationApplied(ImageOperation operation) {
				for (OperationListener listener : imageOperationListeners) {
					listener.operationApplied(operation);
				}
			}

			public void operationRemoved() {
				for (OperationListener listener : imageOperationListeners) {
					listener.operationRemoved();
				}
			}
		};

		image.registerOperationListener(imageOperationListener);
	}

	/**
	 * Save the current image to a given filepath
	 * @param filepath The filepath to save the image to
	 * @throws ExtensionException If the filepath does not have a valid extension
	 * @throws IOException If there is an error writing the image
	 */
	private void saveImage(String filepath) throws ExtensionException, IOException {
		if (!Utils.hasValidFileExtension(filepath)) {
			throw new ExtensionException("SAVE_EXCEPTION");
		}
		String extension = Utils.getFileExtension(filepath);
		if (!ImageIO.write(image.getOriginalImage(), extension, new File(filepath))) {
			throw new IOException("SAVE_EXCEPTION");
		}
		;
		String opsFilename = filepath + ".ops";
		Utils.writeString(new File(opsFilename), image.getOpsString(), Charset.defaultCharset());
		this.imageFilepath = filepath;
		notifyListeners(filepathListeners);
		image.saved();
	}

	/**
	 * Save the current image to the current filepath
	 * @see #saveImage(String)
	 * @throws ExtensionException If the filepath does not have a valid extension
	 * @throws IOException If there is an error writing the image
	 */
	public void saveImage() throws ExtensionException, IOException {
		saveImage(imageFilepath);
	}

	/**
	 * Save the current image to a given filepath
	 * @see #saveImage(String)
	 * @param filepath The filepath to save the image to
	 * @throws ExtensionException If the filepath does not have a valid extension
	 * @throws IOException If there is an error writing the image
	 */
	public void saveImageAs(String filepath) throws ExtensionException, IOException {
		saveImage(filepath);
	}

	/**
	 * Export the current image to a given filepath, with a given format
	 * @param filepath The filepath to export the image to
	 * @param format The format to export the image in
	 * @throws IOException If there is an error writing the image
	 */
	public void exportImage(String filepath, String format) throws IOException {
		BufferedImage exportImage = image.getExportImage(format);
		File exportFile = new File(filepath);
		boolean result = ImageIO.write(exportImage, format, exportFile);
		if (!result)
			throw new IOException();
	}

	/**
	 * Notifys all listeners
	 * @param listeners The listeners to notify
	 */
	private void notifyListeners(ArrayList<ModelListener> listeners) {
		for (ModelListener listener : listeners) {
			listener.update();
		}
	}

	/**
	 * Registers a listener to be notified when the image status changes
	 * @param listener The listener to register
	 */
	public void registerImageStatusListener(ModelListener listener) {
		imageStatusListeners.add(listener);
	}

	/**
	 * Unregisters a listener to be notified when the image status changes
	 * @param listener The listener to unregister
	 */
	public void unregisterImageStatusListener(ModelListener listener) {
		imageStatusListeners.remove(listener);
	}

	/**
	 * Registers a listener to be notified when the working image changes
	 * @param listener The listener to register
	 */
	public void registerWorkingImageListener(ModelListener listener) {
		workingImageListeners.add(listener);
	}

	/**
	 * Unregisters a listener to be notified when the working image changes
	 * @param listener The listener to unregister
	 */
	public void unregisterWorkingImageListener(ModelListener listener) {
		workingImageListeners.remove(listener);
	}

	/**
	 * Registers a listener to be notified when the image changes
	 * @param listener The listener to register
	 */
	public void registerImageListener(ModelListener listener) {
		imageListeners.add(listener);
	}

	/**
	 * Unregisters a listener to be notified when the image changes
	 * @param listener The listener to unregister
	 */
	public void unregisterImageListener(ModelListener listener) {
		imageListeners.remove(listener);
	}

	/**
	 * Registers a listener to be notified when an operation is applied to the image
	 * @param listener The listener to register
	 */
	public void registerImageOperationListener(OperationListener listener) {
		imageOperationListeners.add(listener);
	}

	/**
	 * Unregisters a listener to be notified when an operation is applied to the image
	 * @param listener The listener to unregister
	 */
	public void unregisterImageOperationListener(OperationListener listener) {
		imageOperationListeners.remove(listener);
	}

	/**
	 * Registers a listener to be notified when the filepath changes
	 * @param listener The listener to register
	 */
	public void registerFilepathListener(ModelListener listener) {
		filepathListeners.add(listener);
	}

	/**
	 * Unregisters a listener to be notified when the filepath changes
	 * @param listener The listener to unregister
	 */
	public void unregisterFilepathListener(ModelListener listener) {
		filepathListeners.remove(listener);
	}

	/* A listener for model updates */
	public interface ModelListener extends EventListener {
		/** Called when the model is updated */
		public void update();
	}

	/**
	 * Sets the preview image, and notifies relevant listeners
	 * @param image The image to set the preview image to
	 */
	public void setPreviewImage(BufferedImage image) {
		previewImage = image;
		notifyListeners(workingImageListeners);
	}

	/**
	 * Clears the preview image, and notifies relevant listeners
	 */
	public void clearPreviewImage() {
		previewImage = null;
		notifyListeners(workingImageListeners);
	}

	/** Lists the model's listeners (for debugging purposes) */
	public void listListeners() {
		for (ModelListener listener : imageStatusListeners) {
			System.out.println("Image Status Listener: " + listener);
		}
		for (ModelListener listener : workingImageListeners) {
			System.out.println("Working Image Listener: " + listener);
		}
		for (ModelListener listener : imageListeners) {
			System.out.println("Image Listener: " + listener);
		}
		tool.listListeners();
		mouse.listListeners();
		overlay.listListeners();
	}
}
