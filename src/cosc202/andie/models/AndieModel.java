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
import javax.imageio.ImageIO;

import cosc202.andie.ImageOperation;
import cosc202.andie.Utils;
import cosc202.andie.Utils.ExtensionException;

public class AndieModel {

	EditableImage image;
	boolean isImageOpen = false;
	String imageFilepath;

	Dimension frameSize;
	Point frameLocation;

	ArrayList<ModelListener> imageStatusListeners = new ArrayList<ModelListener>();
	ArrayList<ModelListener> imageListeners = new ArrayList<ModelListener>();

	public AndieModel() {
	}

	public void init() {
		image = null;
		isImageOpen = false;
		notifyListeners(imageStatusListeners);
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
	public boolean hasImage() {
		return isImageOpen;
	}

	public void closeImage() {
		image = null;
		isImageOpen = false;
		notifyListeners(imageStatusListeners);
		notifyListeners(imageListeners);
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
		notifyListeners(imageListeners);
		image.registerImageListener(() -> {
			notifyListeners(imageListeners);
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
	public void registerImageListener(ModelListener listener) {
		imageListeners.add(listener);
	}

	public void applyFilter(ImageOperation filter) {
		this.getImage().apply(filter);
	}

	public interface ModelListener extends EventListener {
		public void update();
	}

	public void removeAllListeners() {
		imageStatusListeners.clear();
		imageListeners.clear();
	}
}
