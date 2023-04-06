package cosc202.andie;

import java.util.*;
import java.io.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.*;
import javax.imageio.*;
import javax.swing.JOptionPane;

import cosc202.andie.ImageOperation.ImageOperationException;

import static cosc202.andie.LanguageConfig.msg;

/**
 * <p>
 * An image with a set of operations applied to it.
 * </p>
 * 
 * <p>
 * The EditableImage represents an image with a series of operations applied to it.
 * It is fairly core to the ANDIE program, being the central data structure.
 * The operations are applied to a copy of the original image so that they can be undone.
 * THis is what is meant by "A Non-Destructive Image Editor" - you can always undo back to the original image.
 * </p>
 * 
 * <p>
 * Internally the EditableImage has two {@link BufferedImage}s - the original image 
 * and the result of applying the current set of operations to it. 
 * The operations themselves are stored on a {@link Stack}, with a second {@link Stack} 
 * being used to allow undone operations to be redone.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @author Jeb Nicholson
 * @version 2.0
 */
public class EditableImage {

    /** The original image. This should never be altered by ANDIE. */
    private BufferedImage original;
    /** The current image, the result of applying {@link ops} to {@link original}. */
    private BufferedImage current;
    /** The sequence of operations currently applied to the image. */
    private Stack<ImageOperation> ops;
    /** A memory of 'undone' operations to support 'redo'. */
    private Stack<ImageOperation> redoOps;
    /** The file where the original image is stored/ */
    private String imageFilename;
    /** The file where the operation sequence is stored. */
    private String opsFilename;
    /** Whether the image has been modified since it was last saved/opened */
    private BufferedImage lastSavedImage;

    /**
     * <p>
     * Create a new EditableImage.
     * </p>
     * 
     * <p>
     * A new EditableImage has no image (it is a null reference), and an empty stack of operations.
     * </p>
     */
    public EditableImage() {
        reset();
    }

    /**
     * Resets the image
     */
    public void reset() {
        original = null;
        current = null;
        ops = new Stack<ImageOperation>();
        redoOps = new Stack<ImageOperation>();
        imageFilename = null;
        opsFilename = null;
        lastSavedImage = null;
    }

    /**
     * <p>
     * Check if there is an image loaded.
     * </p>
     * 
     * @return True if there is an image, false otherwise.
     */
    public boolean hasImage() {
        return current != null;
    }

    /**
     * <p>
     * Get the current image's file path.
     * @return The current image's file path, or null if there is no image.
     */
    public String getFilepath() {
        return imageFilename;
    }

    /**
     * <p>
     * Make a 'deep' copy of a BufferedImage. 
     * </p>
     * 
     * <p>
     * Object instances in Java are accessed via references, which means that assignment does
     * not copy an object, it merely makes another reference to the original.
     * In order to make an independent copy, the {@code clone()} method is generally used.
     * {@link BufferedImage} does not implement {@link Cloneable} interface, and so the 
     * {@code clone()} method is not accessible.
     * </p>
     * 
     * <p>
     * This method makes a cloned copy of a BufferedImage.
     * This requires knoweldge of some details about the internals of the BufferedImage,
     * but essentially comes down to making a new BufferedImage made up of copies of
     * the internal parts of the input.
     * </p>
     * 
     * <p>
     * This code is taken from StackOverflow:
     * <a href="https://stackoverflow.com/a/3514297">https://stackoverflow.com/a/3514297</a>
     * in response to 
     * <a href="https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage">https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage</a>.
     * Code by Klark used under the CC BY-SA 2.5 license.
     * </p>
     * 
     * <p>
     * This method (only) is released under <a href="https://creativecommons.org/licenses/by-sa/2.5/">CC BY-SA 2.5</a>
     * </p>
     * 
     * @param bi The BufferedImage to copy.
     * @return A deep copy of the input.
     */
    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
    
    /**
     * <p>
     * Open an image from a file.
     * </p>
     * 
     * <p>
     * Opens an image from the specified file.
     * Also tries to open a set of operations from the file with <code>.ops</code> added.
     * So if you open <code>some/path/to/image.png</code>, this method will also try to
     * read the operations from <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @param filePath The file to open the image from.
     * @throws IOException The image cannot be read
     */
    public void open(String filePath) throws IOException {
        imageFilename = filePath;
        opsFilename = imageFilename + ".ops";
        File imageFile = new File(imageFilename);
        BufferedImage newImage = ImageIO.read(imageFile);
        if (newImage == null) throw new IOException(msg("Open_Exception"));
        Stack<ImageOperation> newOps = new Stack<ImageOperation>();
        
        try {
            FileInputStream fileIn = new FileInputStream(this.opsFilename);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);
            // Silence the Java compiler warning about type casting.
            // Understanding the cause of the warning is way beyond
            // the scope of COSC202, but if you're interested, it has
            // to do with "type erasure" in Java: the compiler cannot
            // produce code that fails at this point in all cases in
            // which there is actually a type mismatch for one of the
            // elements within the Stack, i.e., a non-ImageOperation.
            @SuppressWarnings("unchecked")
            Stack<ImageOperation> opsFromFile = (Stack<ImageOperation>) objIn.readObject();
            newOps = opsFromFile;
            objIn.close();
            fileIn.close();
        } catch (Exception ex) { }
        //newOps will be empty unless there existed a valid .ops file.
        openNewImage(newImage, newOps);
    }

    public void openNewImage(BufferedImage image, Stack<ImageOperation> ops) {
        original = image;
        this.ops = ops;
        redoOps.clear();
        this.refresh();
        lastSavedImage = deepCopy(current);
    }

    /**
     * <p>
     * Save an image to file.
     * </p>
     * 
     * <p>
     * Saves an image to the file it was opened from, or the most recent file saved as.
     * Also saves a set of operations from the file with <code>.ops</code> added.
     * So if you save to <code>some/path/to/image.png</code>, this method will also save
     * the current operations to <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @throws IOException If the immage cannot be written
     * @throws ExtensionException If the file extension is not a valid image file format
     */
    public void save() throws IOException, ExtensionException {
        if (this.opsFilename == null) {
            this.opsFilename = this.imageFilename + ".ops";
        }
        // Write image file based on file extension
        int lastIndexOfDot = imageFilename.lastIndexOf(".");
        if (lastIndexOfDot == -1) 
            throw new ExtensionException(msg("Save_Exception") + imageFilename);
        String extension = imageFilename.substring(1+lastIndexOfDot).toLowerCase();
        if (!ImageIO.write(original, extension, new File(imageFilename))) {
            throw new ExtensionException(msg("Save_Exception") + extension);
        };
        // Write operations file
        FileOutputStream fileOut = new FileOutputStream(this.opsFilename);
        ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
        objOut.writeObject(this.ops);
        objOut.close();
        fileOut.close();
        lastSavedImage = deepCopy(current);
    }


    /**
     * <p>
     * Save an image to a speficied file.
     * </p>
     * 
     * <p>
     * Saves an image to the file provided as a parameter.
     * Also saves a set of operations from the file with <code>.ops</code> added.
     * So if you save to <code>some/path/to/image.png</code>, this method will also save
     * the current operations to <code>some/path/to/image.png.ops</code>.
     * </p>
     * 
     * @param imageFilename The file location to save the image to.
     * @throws IOException If The image cannot be written
     */
    public void saveAs(String imageFilename) throws IOException, ExtensionException {
        this.imageFilename = imageFilename;
        this.opsFilename = imageFilename + ".ops";
        save();
    }

    /**
     * <p>
     * Export the image to a file.
     * </p>
     * @param exportFilePath The file to export the image to.
     * @param format The format to export the image as.
     * @throws IOException If the image cannot be written
     */
    public void export(String exportFilePath, String format) throws IOException {
        boolean formatSupportsTransparency = format == "png" || format == "gif";
        BufferedImage exportImage;
        if (!formatSupportsTransparency && current.getTransparency() != Transparency.OPAQUE) {
            exportImage = new BufferedImage(current.getWidth(), current.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = exportImage.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, exportImage.getWidth(), exportImage.getHeight());
            g.drawImage(current, 0, 0, null);
            g.dispose();
        } else {
            exportImage = deepCopy(current);
        }
        File exportFile = new File(exportFilePath);
        boolean result = ImageIO.write(exportImage, format, exportFile);
        if (!result) throw new IOException();
    }

    /**
     * <p>
     * Apply an {@link ImageOperation} to this image.
     * This should only be called once per when the operation is originally applied.
     * If the operation fails, a warning message will be shown, and the operation will not be applied.
     * </p>
     * 
     * @param imageOperation The operation to apply.
     * @return true if the operation was applied, and false otherwise.
     */
    public boolean apply(ImageOperation imageOperation) {
        try {
            current = imageOperation.apply(current);
        } catch (ImageOperationException ex) {
            JOptionPane.showMessageDialog(null, msg("Apply_Exception") + "\n" + ex.getMessage(), msg("Apply_Exception_Title"), JOptionPane.WARNING_MESSAGE);
            return false;
        }
        ops.add(imageOperation);
        redoOps.clear();
        return true;
    }

    /**
     * <p>
     * Undo the last {@link ImageOperation} applied to the image.
     * </p>
     */
    public void undo() {
        if (ops.isEmpty()) return;
        redoOps.push(ops.pop());
        refresh();
    }

    /**
     * <p>
     * Reapply the most recently {@link undo}ne {@link ImageOperation} to the image.
     * </p>
     */
    public void redo()  {
        if (redoOps.isEmpty()) return;
        ImageOperation operationToRedo = redoOps.pop();
        try {
            current = operationToRedo.apply(current);
        } catch (ImageOperationException ex) {
            // No operation should fail when being redone.
            return;
        } finally {
            ops.add(operationToRedo);
        }
    }

    /**
    * Returns true if there are undoable operations, and false otherwise.
    */
    public boolean undoable() {
        return !ops.isEmpty();
    }

    /**
    * Returns true if there are redoable operations, and false otherwise.
    */
    public boolean redoable() {
        return !redoOps.isEmpty();
    }


    /**
     * <p>
     * Get the current image after the operations have been applied.
     * </p>
     * 
     * @return The result of applying all of the current operations to the {@link original} image.
     */
    public BufferedImage getCurrentImage() {
        return current;
    }

    /**
     * <p>
     * Reapply the current list of operations to the original.
     * </p>
     * 
     * <p>
     * While the latest version of the image is stored in {@link current}, this
     * method makes a fresh copy of the original and applies the operations to it in sequence.
     * This is useful when undoing changes to the image, or in any other case where {@link current}
     * cannot be easily incrementally updated. 
     * </p>
     */
    private void refresh()  {
        current = deepCopy(original);
        try {
            for (ImageOperation op: ops) {
                current = op.apply(current);
            }
        } catch (ImageOperationException ex) {
            // This should never happen, since the operations have already been applied once.
            return;
        }
    }

    /**
     * Whether the image has been modified
     * @return True if the image has been modified since the last save / open, otherwise false.
     */
    public boolean getModified() {
        return !bufferedImagesAreEqual(lastSavedImage, current);
    }
    /**
     * A checked Exception for when a file extension is not supported.
     */
    public static class ExtensionException extends Exception {
        public ExtensionException(String message) {
            super(message);
        }
    }

    /**
     * <p>
     * Are two {@link BufferedImage}s equal, pixel for pixel?
     * </p>
     * @param image1 The first {@link BufferedImage} to compare
     * @param image2 The second {@link BufferedImage} to compare
     * @return True if the images are equal, otherwise false.
     */
    static boolean bufferedImagesAreEqual(BufferedImage image1, BufferedImage image2) {
        if (image1 == null && image2 == null) return true;
        if (image1 == null || image2 == null) return false;
        if (image1.getWidth() != image2.getWidth() || image1.getHeight() != image2.getHeight()) return false;
        for (int x = 0; x < image1.getWidth(); x++) {
            for (int y = 0; y < image1.getHeight(); y++) {
                if (image1.getRGB(x, y) != image2.getRGB(x, y))
                    return false;
            }
        }
        return true;
    }
}