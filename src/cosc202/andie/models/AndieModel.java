package cosc202.andie.models;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Timer;

import javax.imageio.ImageIO;

import cosc202.andie.Utils;
import cosc202.andie.Utils.ExtensionException;

/* 
 * //BUG: Listeners from both the model and controller may not be getting deregistered when components no longer need them.
 * Given enough open/closes of an image, this would use up too much memory.
 * (Some listeners contain BufferedImages, which are not being garbage collected)
 * //TODO: Investigate Lapsed Listener Problem & WeakReferences
 */

public class AndieModel {

	public static boolean IS_MAC = System.getProperty("os.name").toLowerCase().startsWith("mac");

	private EditableImage image;
	private BufferedImage previewImage;
	private boolean isImageOpen = false;
	private String imageFilepath;

	private Dimension frameSize;
	private Point frameLocation;

	private ArrayList<ModelListener> imageStatusListeners = new ArrayList<ModelListener>();
	private ArrayList<ModelListener> workingImageListeners = new ArrayList<ModelListener>();

	private Timer timer = null;

	public Operations operations;
	public MouseModel mouse;
	public OverlayModel overlay;
	public ToolModel tool;

	public AndieModel() {
		init();
	}

	public void init() {
		if (timer != null) timer.cancel();
		timer = new Timer();

		this.operations = new Operations(this);
		this.mouse = new MouseModel(this);
		this.overlay = new OverlayModel(this);
		this.tool = new ToolModel(this);
		image = null;
		previewImage = null;
		isImageOpen = false;
		notifyListeners(imageStatusListeners);
	}

	public Timer getTimer() {
		return timer;
	}

	public String getImageFilepath() {
		return imageFilepath;
	}
	public Dimension getFrameSize() {
		return frameSize;
	}
	public void setFrameSize(Dimension frameSize) {
		this.frameSize = frameSize;
	}
	public Point getFrameLocation() {
		return frameLocation;
	}
	public void setFrameLocation(Point frameLocation) {
		this.frameLocation = frameLocation;
	}
	public EditableImage getImage() {
		return image;
	}
	public BufferedImage getWorkingImage() {
		if (previewImage != null)
			return previewImage;
		return image.getCurrentImage();
	}
	public boolean hasImage() {
		return isImageOpen;
	}

	public void closeImage() {
		init();
		notifyListeners(imageStatusListeners);
		notifyListeners(workingImageListeners);
	}

	public void openImage(String filepath) throws IOException {
		//Open image at filepath
		BufferedImage newImage = ImageIO.read(new File(filepath));
		File operationsFile = new File(filepath + ".ops");
		String opsString = "";
		if (operationsFile.exists()) {
			opsString = Files.readString(operationsFile.toPath(), Charset.defaultCharset());
		}

		image = opsString != null ? new EditableImage(newImage,opsString) : new EditableImage(newImage);

		this.imageFilepath = filepath;
		isImageOpen = true;
		notifyListeners(imageStatusListeners);
		notifyListeners(workingImageListeners);
		image.registerImageListener(() -> {
			previewImage = null;
			notifyListeners(workingImageListeners);
		});
	}

	private void saveImage(String filepath) throws ExtensionException, IOException {
		if (!Utils.hasValidFileExtension(filepath)) {
				throw new ExtensionException("SAVE_EXCEPTION");
		}
		String extension = Utils.getFileExtension(filepath);
		if (!ImageIO.write(image.getOriginalImage(), extension, new File(filepath))) {
				throw new IOException("SAVE_EXCEPTION");
		};
		if (image.hasOps()){
			String opsFilename = filepath + ".ops";
			Files.writeString(new File(opsFilename).toPath(),image.getOpsString());
		}
		this.imageFilepath = filepath;
		image.saved();
	}

	public void saveImage() throws ExtensionException, IOException {

		saveImage(imageFilepath);
	}

	public void saveImageAs(String filepath) throws ExtensionException, IOException {
		saveImage(filepath);
	}

	public void exportImage(String filepath, String format) throws IOException {
		BufferedImage exportImage = image.getExportImage(format);
		File exportFile = new File(filepath);
		boolean result = ImageIO.write(exportImage, format, exportFile);
		if (!result) throw new IOException();
	}

	private void notifyListeners(ArrayList<ModelListener> listeners) {
		for (ModelListener listener : listeners) {
			listener.update();
		}
	}
	public void registerImageStatusListener(ModelListener listener) {
		imageStatusListeners.add(listener);
	}
	public void unregisterImageStatusListener(ModelListener listener) {
		imageStatusListeners.remove(listener);
	}
	public void registerWorkingImageListener(ModelListener listener) {
		workingImageListeners.add(listener);
	}
	public void unregisterWorkingImageListener(ModelListener listener) {
		workingImageListeners.remove(listener);
	}
	public interface ModelListener extends EventListener {
		public void update();
	}

	public void removeAllListeners() {
		imageStatusListeners.clear();
		workingImageListeners.clear();
	}

	public void setPreviewImage(BufferedImage image) {
		previewImage = image;
		notifyListeners(workingImageListeners);
	}

	public void clearPreviewImage() {
		previewImage = null;
		notifyListeners(workingImageListeners);
	}

}
