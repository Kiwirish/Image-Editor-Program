package cosc202.andie;

import java.awt.image.BufferedImage;

/**
 * <p>
 * Interface for operations to be applied to images.
 * </p>
 * 
 * <p>
 * Classes implementing ImageOperation represent operations that can be applied to images.
 * Each operation takes an image as input and provides an updated image as output.
 * </p>
 * 
 * <p>
 * A distinction should be made between an ImageOperation and an {@link ImageAction}.
 * An ImageOperation is applied to an image in order to change it in some way.
 * An ImageAction provides support for the user doing something to an image in the user interface.
 * ImageActions may apply an ImageOperation, but do not have to do so. 
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
public interface ImageOperation extends java.io.Serializable {

    /**
     * Processes the "good" version of the operation on the input image.
     * <p> Called when the user "applies" the operation. </p>
     * @param input The image to be processed
     * @return The processed image
     * @throws ImageOperationException If the operation fails
     */
    public BufferedImage draw(BufferedImage input) throws ImageOperationException;

    /**
     * Processes the "preview" version of the operation on the input image.
     * <p> Called when the user "previews" the operation. </p>
     * @param input The image to be processed
     * @return The processed image
     * @throws ImageOperationException If the operation fails
     */
    public BufferedImage drawPreview(BufferedImage input) throws ImageOperationException;

    /**
     * Get a description of the operation, including any parameters.
     * @return A description of the operation
     */
    public String operationDescription();

    /**
     * An exception to be thrown when an ImageOperation fails.
     */
    public class ImageOperationException extends Exception {
        public ImageOperationException(String message) {
            super(message);
        }
    }

}
