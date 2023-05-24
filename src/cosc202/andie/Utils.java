package cosc202.andie;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;

import javax.imageio.ImageIO;


/**
 * <p>
 * Various utility methods used throughout the program.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Jeb Nicholson
 * @author Steven Mills (Deepcopy)
 * @version 1.0
 */
public class Utils {

	/**
	 * Does a given filepath end in a valid image writer file extension?
	 * @param filepath The filepath to check
	 * @return True if the filepath ends in a valid image writer file extension
	 */
	public static boolean hasValidFileExtension(String filepath) {
		String fileExtension = getFileExtension(filepath);
		for (String ext : ImageIO.getWriterFileSuffixes()) {
			if (ext.equals(fileExtension))
				return true;
		}
		return false;
	}

	/**
	 * Get the file extension of a given filepath.
	 * @param filepath The filepath to check
	 * @return The file extension of the filepath, or null if there is no file extension
	 */
	public static String getFileExtension(String filepath) {
		int lastIndexOfDot = filepath.lastIndexOf('.');
		if (lastIndexOfDot == -1)
			return null;
		return filepath.substring(lastIndexOfDot + 1);
	}

	/**
	 * Get a filepath with a given extension appended. If the filepath already has the given extension, return the filepath unchanged.
	 * @param filepath The filepath to check
	 * @param extension The extension to add
	 * @return The filepath with the given extension
	 */
	public static String withFileExtension(String filepath, String extension) {
		String currentExt = getFileExtension(filepath);
		if (currentExt != null && currentExt.equals(extension))
			return filepath;
		return filepath + "." + extension;
	}

	/**
	 * Get a filepath with a given extension replacing the current extension. If the filepath has no extension, it is appended.
	 * @param filepath The filepath to check
	 * @param newExtension The extension to add
	 * @return The filepath with the given extension
	 */
	public static String withUpdatedFileExtension(String filepath, String newExtension) {
		String currentExt = getFileExtension(filepath);
		if (currentExt == null)
			return filepath + "." + newExtension;
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
	 * Read a file as string
	 * @param file The file to read
	 * @param encoding The character encoding to use
	 * @return The contents of the file as a string
	 * @throws IOException If there is an error reading the file
	 */
	public static String readString(File file, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(file.toPath());
		return new String(encoded, encoding);
	}

	/**
	 * Write a string to a file
	 * @param file The file to write to
	 * @param content The string to write
	 * @param encoding The character encoding to use
	 * @throws IOException If there is an error writing the file
	 */
	public static void writeString(File file, String content, Charset encoding) throws IOException {
		byte[] encoded = content.getBytes(encoding);
		Files.write(file.toPath(), encoded);
	}

	// /**
	//  * Load an image from a file, or null if there is an error.
	//  * @param file The file to load from
	//  * @return The image, or null if there is an error
	//  */
	// public static BufferedImage loadResourceRelative(String relativeResource) {
	// 	try {
	// 		return ImageIO.read(Andie.class.getClassLoader().getResourceAsStream(relativeResource));
	// 	} catch (IOException e) {
	// 		return null;
	// 	}
	// }

	/**
	 * A checked Exception for when a file extension is not supported.
	 */
	public static class ExtensionException extends Exception {
		public ExtensionException(String message) {
			super(message);
		}
	}

	/**
	 * Creates a new BufferedImage with each edge expanded by r pixels, taking the color values from the nearest pixel.
	 * @param input The BufferedImage to expand.
	 * @param r The number of pixels to expand each edge by.
	 * @return A new BufferedImage with the expanded edges.
	 */
	public static BufferedImage expandEdges(BufferedImage input, int r) {
		BufferedImage output = new BufferedImage(input.getWidth() + 2 * r, input.getHeight() + 2 * r, input.getType());
		Graphics g = output.createGraphics();
		g.drawImage(input, r, r, null);
		g.dispose();

		int h = input.getHeight();
		int w = input.getWidth();

		// Top left corner padding 
		for(int y = -r; y < 0; ++y)
				for(int x = -r ; x < 0 ; ++x)
						output.setRGB((x + r), (y + r), input.getRGB(0,0));
		// Top right corner padding 
		for(int y = -r; y < 0; ++y)
				for(int x = 0; x < r; ++x)
						output.setRGB((w + x + r), (y + r), input.getRGB((w - 1), 0));
		// Bottom left corner padding
		for(int y = 0; y < r; ++y)
				for(int x = -r; x < 0; ++x)
						output.setRGB((x + r), (h + y + r), input.getRGB(0, (h - 1)));
		// Bottom right corner padding
		for(int y = 0; y < r; ++y)
				for(int x = 0; x < r; ++x)
						output.setRGB((w + x + r), (h + y + r), input.getRGB((w - 1), (h - 1)));
		// Top edge 
		for(int y = 0 ; y < r; y++)
				for(int x = 0; x < w ; ++x)
						output.setRGB((x + r), y, input.getRGB(x,0)); 
		// Bottom edge
		for(int y = 0 ; y < r; ++y)
				for(int x = 0; x < w ; ++x)
						output.setRGB((x + r), (y + r + h) , input.getRGB(x, (h - 1))); 
		// Left edge 
		for(int y = 0 ; y < h; ++y)
				for(int x = 0; x < r ; ++x)
						output.setRGB(x, y+r, input.getRGB(0, y)); 
		// Right edge 
		for(int y = 0 ; y < h; ++y)
				for(int x = 0; x < r ; ++x)
						output.setRGB((x + r + w), (y + r), input.getRGB((w - 1), y)); 

		return output;
	}

}
