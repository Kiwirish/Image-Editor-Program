package cosc202.andie;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import javax.imageio.ImageIO;

public class Utils {
	public static boolean hasValidFileExtension(String filepath) {
		String fileExtension = getFileExtension(filepath);
		for (String ext : ImageIO.getWriterFileSuffixes()) {
			if (ext.equals(fileExtension))
				return true;
		}
		return false;
	}

	public static String getFileExtension(String filepath) {
		int lastIndexOfDot = filepath.lastIndexOf('.');
		if (lastIndexOfDot == -1)
			return null;
		return filepath.substring(lastIndexOfDot + 1);
	}

	public static String withFileExtension(String filepath, String extension) {
		if (getFileExtension(filepath).equals(extension))
			return filepath;
		return filepath + "." + extension;
	}

	public static String withUpdatedFileExtension(String filepath, String newExtension) {
		String currentExt = getFileExtension(filepath);
		String modifiedFilepath = filepath.substring(0, filepath.length() - currentExt.length() - 1);
		return modifiedFilepath + "." + newExtension;
	}

	/**
	 * <p>
	 * Make a 'deep' copy of a BufferedImage.
	 * </p>
	 * 
	 * <p>
	 * Object instances in Java are accessed via references, which means that
	 * assignment does
	 * not copy an object, it merely makes another reference to the original.
	 * In order to make an independent copy, the {@code clone()} method is generally
	 * used.
	 * {@link BufferedImage} does not implement {@link Cloneable} interface, and so
	 * the
	 * {@code clone()} method is not accessible.
	 * </p>
	 * 
	 * <p>
	 * This method makes a cloned copy of a BufferedImage.
	 * This requires knoweldge of some details about the internals of the
	 * BufferedImage,
	 * but essentially comes down to making a new BufferedImage made up of copies of
	 * the internal parts of the input.
	 * </p>
	 * 
	 * <p>
	 * This code is taken from StackOverflow:
	 * <a href=
	 * "https://stackoverflow.com/a/3514297">https://stackoverflow.com/a/3514297</a>
	 * in response to
	 * <a href=
	 * "https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage">https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage</a>.
	 * Code by Klark used under the CC BY-SA 2.5 license.
	 * </p>
	 * 
	 * <p>
	 * This method (only) is released under
	 * <a href="https://creativecommons.org/licenses/by-sa/2.5/">CC BY-SA 2.5</a>
	 * </p>
	 * 
	 * @param bi The BufferedImage to copy.
	 * @return A deep copy of the input.
	 */
	public static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	/**
	 * A checked Exception for when a file extension is not supported.
	 */
	public static class ExtensionException extends Exception {
		public ExtensionException(String message) {
			super(message);
		}
	}
}
