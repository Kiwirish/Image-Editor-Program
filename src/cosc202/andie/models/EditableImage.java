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
import cosc202.andie.Utils;
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
    private ArrayList<OperationListener> operationListeners = new ArrayList<OperationListener>();

    /** The original image. This should never be altered by ANDIE. */
    private BufferedImage original;
    /** The current image, the result of applying {@link ops} to {@link original}. */
    private BufferedImage current;
    /** The sequence of operations currently applied to the image. */
    private Stack<ImageOperation> ops;
    /** A memory of 'undone' operations to support 'redo'. */
    private Stack<ImageOperation> redoOps;
    /** The ops applied as of last save */
    private String lastSavedOps;

    /**
     * <p>
     * Create a new EditableImage.
     * </p>
     * @param image The original image 
     * @param serializedOps A string representation of the operations to be applied to the image
     */
    public EditableImage(BufferedImage image, String serializedOps) {
        this.original = image;
        this.ops = stringToOps(serializedOps);
        redoOps = new Stack<ImageOperation>();
        this.refresh();
        lastSavedOps = opsToString(ops);
    }

    /**
     * Create a new EditableImage.
     * @param image The original image
     */
    public EditableImage(BufferedImage image) {
        this.original = image;
        this.ops = new Stack<ImageOperation>();
        redoOps = new Stack<ImageOperation>();
        this.refresh();
        lastSavedOps = opsToString(ops);
    }

    /**
     * Get the size of the current image
     * @return The size of the current image
     */
    public Dimension getSize() {
        return new Dimension(current.getWidth(), current.getHeight());
    }

    /**
     * Informs the EditableImage that its changes have been saved.
     * <p> Used to determine if the image has been modified since the last save. </p>
     */
    public void saved() {
        lastSavedOps = opsToString(ops);
        notifyImageListeners(imageListeners);
    }

    /**
     * Gets the current image's operations as an Base64 encoded string
     * @return The operations as an Base64 encoded string
     */
    public String getOpsString() {
        return opsToString(ops);
    }

    /**
     * Does the current image have any operations applied to it?
     * @return true if there are operations
     */
    public boolean hasOps() {
        return !ops.empty();
    }

    /**
     * Creates an appropriately rendered image of the current image for export
     * <p> If the current image has transparency, and the format does not support transparency,
     * the image will be rendered on a white background. </p>
     * @param format The image format to export as. (pngs and gifs support transparency, jpgs do not)
     * @return The image to export
     */
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
        return Utils.deepCopy(current);
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
            current = imageOperation.draw(current);
            notifyImageListeners(imageListeners);
        } catch (ImageOperationException ex) {
            JOptionPane.showMessageDialog(null, msg("Apply_Exception") + "\n" + ex.getMessage(), msg("Apply_Exception_Title"), JOptionPane.WARNING_MESSAGE);
            return false;
        }
        ops.add(imageOperation);
        redoOps.clear();
        notifyImageListeners(imageListeners);
        for (OperationListener listener : operationListeners) {
            listener.operationApplied(imageOperation);
        }
        return true;
    }

    /**
     * Registers a listener to be notified when the image content changes.
     * @param listener The listener to register
     */
	public void registerImageListener(ImageListener listener) {
		imageListeners.add(listener);
	}

    /**
     * Unregisters a listener from being notified when the image content changes.
     * @param listener The listener to unregister
     */
    public void unregisterImageListener(ImageListener listener) {
        imageListeners.remove(listener);
    }

    /**
     * Notifies all registered listeners that the image has changed.
     * @param listeners The listeners to notify
     */
	private void notifyImageListeners(ArrayList<ImageListener> listeners) {
		for (ImageListener listener : listeners) {
			listener.update();
		}
    }

    /**
     * Registers a listener to be notified when an operation is applied to the image.
     * @param listener The listener to register
     */
    public void registerOperationListener(OperationListener listener) {
        operationListeners.add(listener);
    }

    /**
     * Unregisters a listener from being notified when an operation is applied to the image.
     * @param listener The listener to unregister
     */
    public void unregisterOperationListener(OperationListener listener) {
        operationListeners.remove(listener);
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
        notifyImageListeners(imageListeners);
        for (OperationListener listener : operationListeners) {
            listener.operationRemoved();
        }
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
            current = operationToRedo.draw(current);
        } catch (ImageOperationException ex) {
            // No operation should fail when being redone.
            return;
        } finally {
            ops.add(operationToRedo);
            notifyImageListeners(imageListeners);
            for (OperationListener listener : operationListeners) {
                listener.operationApplied(operationToRedo);
            }
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
     * Get the original image before any operations have been applied.
     * @return
     */
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
        current = Utils.deepCopy(original);
        try {
            for (ImageOperation op: ops) {
                current = op.draw(current);
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
        return !getOpsString().equals(lastSavedOps);
    }

    /**
     * <p>
     * Are two {@link BufferedImage}s equal, pixel for pixel?
     * </p>
     * @param image1 The first {@link BufferedImage} to compare
     * @param image2 The second {@link BufferedImage} to compare
     * @return True if the images are equal, otherwise false.
     */
    static public boolean bufferedImagesAreEqual(BufferedImage image1, BufferedImage image2) {
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

    /**
     * Converts a stack of {@link ImageOperation}s to a Base64 encoded string.
     * @param ops The stack of {@link ImageOperation}s to convert
     * @return A Base64 encoded string representing the stack of {@link ImageOperation}s
     */
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

    /**
     * Converts a Base64 encoded string to a stack of {@link ImageOperation}s.
     * <p> If the string cannot be decoded, an empty stack is returned. </p>
     * @param s The Base64 encoded string to convert
     * @return A stack of {@link ImageOperation}s represented by the string
     */
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

    /** A listener for image content update events */
	public interface ImageListener extends EventListener {
        /** Called when the image content has been updated. */
		public void update();
	}

    /** A listener for image operation additions and removals */
	public interface OperationListener extends EventListener {
        /**
         * Called when an operation is applied to the image.
         * @param operation The operation that was applied
         */
		public void operationApplied(ImageOperation operation);
        
        /** Called when an operation is removed from the image. */
        public void operationRemoved();
	}
}