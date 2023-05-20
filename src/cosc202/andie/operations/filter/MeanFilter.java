package cosc202.andie.operations.filter;

import java.awt.image.*;
import java.util.*;

import cosc202.andie.ImageOperation;
import cosc202.andie.Utils;

/**
 * <p>
 * ImageOperation to apply a Mean (simple blur) filter.
 * </p>
 * 
 * <p>
 * A Mean filter blurs an image by replacing each pixel by the average of the
 * pixels in a surrounding neighbourhood, and can be implemented by a convoloution.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @see java.awt.image.ConvolveOp
 * @author Steven Mills
 * @version 1.0
 */
public class MeanFilter implements ImageOperation {
    
    /**
     * The size of filter to apply. A radius of 1 is a 3x3 filter, a radius of 2 a 5x5 filter, and so forth.
     */
    private int radius;

    /**
     * <p>
     * Construct a Mean filter with the given size.
     * </p>
     * 
     * <p>
     * The size of the filter is the 'radius' of the convolution kernel used.
     * A size of 1 is a 3x3 filter, 2 is 5x5, and so on.
     * Larger filters give a stronger blurring effect.
     * </p>
     * 
     * @param radius The radius of the newly constructed MeanFilter
     */
    public MeanFilter(int radius) {
        this.radius = radius;    
    }

    /**
     * <p>
     * Construct a Mean filter with the default size.
     * </p
     * >
     * <p>
     * By default, a Mean filter has radius 1.
     * </p>
     * 
     * @see MeanFilter(int)
     */
    MeanFilter() {
        this(1);
    }

    /**
     * <p>
     * Apply a Mean filter to an image.
     * </p>
     * 
     * <p>
     * As with many filters, the Mean filter is implemented via convolution.
     * The size of the convolution kernel is specified by the {@link radius}.  
     * Larger radii lead to stronger blurring.
     * </p>
     * 
     * @param input The image to apply the Mean filter to.
     * @return The resulting (blurred)) image.
     */
    public BufferedImage draw(BufferedImage input) throws ImageOperationException {

        //check for illegal argument 
        if (input == null){
            throw new IllegalArgumentException("Image to apply Mean filter to does not exist");
        }
        //assuming acceptable image is selected as input
        
        //create enlarged image with all existing argb pixel values of old image set to the new images values 
        BufferedImage enlargedImage = Utils.expandEdges(input, radius);

        // Implement convolution on new enlargedImage from input 

        int size = (2*radius+1) * (2*radius+1);
        float [] array = new float[size];
        Arrays.fill(array, 1.0f/size);

        Kernel kernel = new Kernel(2*radius+1, 2*radius+1, array);
        ConvolveOp convOp = new ConvolveOp(kernel);
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        convOp.filter(enlargedImage, output);

        return output; 

    }

    public BufferedImage drawPreview(BufferedImage input) throws ImageOperationException {
        return draw(input);
    }

    @Override
    public String operationDescription() {
        return String.format("Mean Filter [Radius: %dpx]", radius);
    }
    

}
