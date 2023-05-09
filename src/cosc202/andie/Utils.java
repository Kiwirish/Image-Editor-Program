package cosc202.andie;

import javax.imageio.ImageIO;

public class Utils {
	public static boolean hasValidFileExtension(String filepath) {
		String fileExtension = getFileExtension(filepath);
		for (String ext : ImageIO.getWriterFileSuffixes()) {
			if (ext.equals(fileExtension)) return true;
		}
		return false;
	}
	public static String getFileExtension(String filepath) {
			int lastIndexOfDot = filepath.lastIndexOf('.');
			if (lastIndexOfDot == -1) return null;
			return filepath.substring(lastIndexOfDot + 1);
	}
	public static String withFileExtension(String filepath, String extension) {
		if (getFileExtension(filepath).equals(extension)) return filepath;
		return filepath + "." + extension;
	}
	public static String withUpdatedFileExtension(String filepath, String newExtension) {
		String currentExt = getFileExtension(filepath);
		String modifiedFilepath = filepath.substring(0, filepath.length() - currentExt.length()-1);
		return modifiedFilepath + "." + newExtension;
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
