package cosc202.andie.models;

import java.util.*;
import java.io.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.*;
import javax.swing.JOptionPane;

import cosc202.andie.ImageOperation;
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

	private ArrayList<ImageListener> imageListeners = new ArrayList<ImageListener>();

    /** The original image. This should never be altered by ANDIE. */
    private BufferedImage original;
    /** The current image, the result of applying {@link ops} to {@link original}. */
    private BufferedImage current;
    /** The sequence of operations currently applied to the image. */
    private Stack<ImageOperation> ops;
    /** A memory of 'undone' operations to support 'redo'. */
    private Stack<ImageOperation> redoOps;
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
    public EditableImage(BufferedImage image, String serializedOps) {
        this.original = image;
        this.ops = stringToOps(serializedOps);
        redoOps = new Stack<ImageOperation>();
        this.refresh();
        lastSavedImage = deepCopy(current);
    }

    public EditableImage(BufferedImage image) {
        this.original = image;
        this.ops = new Stack<ImageOperation>();
        redoOps = new Stack<ImageOperation>();
        this.refresh();
        lastSavedImage = deepCopy(current);
    }


    public Dimension getSize() {
        return new Dimension(current.getWidth(), current.getHeight());
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

    public void saved() {
        lastSavedImage = deepCopy(current);
    }

    public String getOpsString() {
        return opsToString(ops);
    }
    public boolean hasOps() {
        return !ops.empty();
    }

    public BufferedImage getExportImage(String format) {
        boolean formatSupportsTransparency = format == "png" || format == "gif";
        if (!formatSupportsTransparency && current.getTransparency() != Transparency.OPAQUE) {
            BufferedImage exportImage = new BufferedImage(current.getWidth(), current.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = exportImage.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, exportImage.getWidth(), exportImage.getHeight());
            g.drawImage(current, 0, 0, null);
            g.dispose();
            return exportImage;
        }
        return deepCopy(current);
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
            notifyListeners(imageListeners);
        } catch (ImageOperationException ex) {
            JOptionPane.showMessageDialog(null, msg("Apply_Exception") + "\n" + ex.getMessage(), msg("Apply_Exception_Title"), JOptionPane.WARNING_MESSAGE);
            return false;
        }
        ops.add(imageOperation);
        redoOps.clear();
        notifyListeners(imageListeners);
        return true;
    }

	public void registerImageListener(ImageListener listener) {
		imageListeners.add(listener);
	}
	private void notifyListeners(ArrayList<ImageListener> listeners) {
		for (ImageListener listener : listeners) {
			listener.update();
		}
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
        notifyListeners(imageListeners);
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
            notifyListeners(imageListeners);
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

    public BufferedImage getOriginalImage() {
        return original;
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

    public static String opsToString(Stack<ImageOperation> ops) {
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objOut = new ObjectOutputStream(bytesOut);
            objOut.writeObject(ops);
            objOut.close();
        } catch (IOException ex) {
            return "";
        };
        return Base64.getEncoder().encodeToString(bytesOut.toByteArray()); 
    }

    public static Stack<ImageOperation> stringToOps(String s) {
        try {
        byte[] bytes = Base64.getDecoder().decode(s);
            ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(bytes));
            @SuppressWarnings("unchecked")
            Stack<ImageOperation> ops = (Stack<ImageOperation>)objIn.readObject();
            objIn.close();
            return ops;
        } catch (Exception e) {
            return new Stack<ImageOperation>();
        }
    }

	public interface ImageListener extends EventListener {
		public void update();
	}
}