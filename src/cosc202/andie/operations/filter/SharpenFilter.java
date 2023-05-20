package cosc202.andie.operations.filter;

import java.awt.image.*;

import cosc202.andie.ImageOperation;
import cosc202.andie.Utils;

/**
 * <p>
 * ImageOperation to apply a Sharpen filter.
 * </p>
 * 
 * <p>
 * A Sharpen filter blurs an image by replacing each pixel by the average of the
 * pixels in a surrounding neighbourhood, and can be implemented by a convoloution.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @see java.awt.image.ConvolveOp
 * @author Blake Leahy
 * @version 1.0
 */
public class SharpenFilter implements ImageOperation  {
    
    //construct a sharpenFilter 
    public SharpenFilter(){ }


    /**
     * <p>
     * Apply a sharpen filter to an image.
     * </p>
     * 
     * <p>
     * As with many filters, the sharpen filter is implemented via convolution.
     * The size of the convolution kernel is 3x3 as given.  
     * </p>
     * 
     * @param input The image to apply the Sharpen filter to.
     * @return The resulting (sharpened)) image.
     */
    public BufferedImage draw(BufferedImage input) throws ImageOperationException {

        //check for illegal argument 
        if (input == null){
            throw new IllegalArgumentException("Image to apply Sharpen filter to does not exist");
        }
        
        //create enlarged image with all existing argb pixel values of old image set to the new images values 
        BufferedImage enlargedImage = Utils.expandEdges(input, 1);

        // Implement convolution on new enlargedImage from input 
        // kernel values in 3x3 array 
        float[] array = { 0, -1 / 2.0f, 0,
                        -1 / 2.0f, 3, -1 / 2.0f,
                        0, -1 / 2.0f, 0 };

        Kernel kernel = new Kernel(3 ,3 , array);

        ConvolveOp convOp = new ConvolveOp(kernel);
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        convOp.filter(enlargedImage, output);

        return output; 
    }
    
    @Override
    public String operationDescription() {
        return "Sharpen Filter";
    }

    @Override
    public BufferedImage drawPreview(BufferedImage input) throws ImageOperationException {
        return draw(input);
    }

}
