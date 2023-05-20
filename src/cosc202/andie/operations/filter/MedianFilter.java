package cosc202.andie.operations.filter;

import java.awt.image.*;
import java.util.*;

import cosc202.andie.ImageOperation;
import cosc202.andie.Utils;

/**
 * <p>
 * ImageOperation to apply a Median filter.
 * </p>
 * 
 * <p>
 * A median filter 'removes noise'. It takes a local neighborhoods pixel values and sorts them 
 * The middle value (the median) from each sorted list of pixel values is the new pixel value to be used in output.
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
public class MedianFilter implements ImageOperation {
    
    /**
     * The size of filter to apply. A radius of 1 is a 3x3 filter, a radius of 2 a 5x5 filter, and so forth.
     */
    private int radius;

    /**
     * <p>
     * Construct a Median filter local neighborhood with the given size.
     * </p>
     * 
     * 
     * @param radius The radius of the newly constructed MedianFilter
     */
    public MedianFilter(int radius) {
        this.radius = radius;    
    }

    /**
     * <p>
     * Construct a Median filter local neighborhood with the default size.
     * </p
     * >
     * <p>
     * By default, a Median filter has radius 1.
     * </p>
     * 
     * @see MedianFilter(int)
     */
    MedianFilter() {
        this(1);
    }

    /**
     * <p>
     * Apply a Median filter to an image.
     * </p>
     * 
     * <p>
     * As with many filters, the Median filter is implemented via convolution.
     * The size of the convolution kernel is specified by the {@link radius}.  
     * Larger radii lead to stronger blurring.
     * </p>
     * 
     * @param input The image to apply the Median filter to.
     * @return The resulting (median filtered)) image.
     */
    public BufferedImage draw(BufferedImage input) throws ImageOperationException {

        //check for illegal argument 
        if (input == null){
            throw new IllegalArgumentException("Image to apply Median filter to does not exist");
        }
        //assuming acceptable image is selected as input:
        
        //set size of image based off radius input 
        int size = (2 * radius + 1) * (2 * radius + 1);
        
        int r = radius; // as original radius accessed later
        
        //create enlarged image with all existing argb pixel values of old image set to the new images values 
        BufferedImage enlargedImage = Utils.expandEdges(input, r);

        // As this filter is not done via a convolution,
        // process each argb channel of each pixel separately. 
        // For each pixel location of argb's, store in separate lists of values for 
        // alpha, red, green and blue. 
        // Sort through these lists and find the middle value (median) for the output image. 

        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);

        int[] alphas = new int[size];
        int[] reds = new int[size];
        int[] greens = new int[size];
        int[] blues = new int[size];

        // outer for loop to access each pixel
        // for each pixel, an inner loop is created to iterate over all neighboring pixels to 
        // the current pixel the outer loop is on - only iterate within the size of the window. 
        for(int y = 0; y < input.getHeight(); y++){
            if (Thread.interrupted()) throw new RuntimeException("Interrupted"); // check for interrupt (Because median filter is slow)
            for(int x = 0; x < input.getWidth(); x++){
                for (int i = 0; i < size; i++){
                    int nx = i % (2 * r + 1) - r;
                    int ny = i / (2 * r + 1) - r;
                    int argb = enlargedImage.getRGB(x+nx+r, y+ny+r);
                    int alpha = (argb & 0xFF000000) >> 24;
                    int red = (argb & 0x00FF0000) >> 16;
                    int green = (argb & 0x0000FF00) >> 8;
                    int blue = (argb & 0x000000FF);
                    alphas[i] = alpha;
                    reds[i] = red;
                    greens[i] = green;
                    blues[i] = blue;
                    // }
                } 
                Arrays.sort(alphas);
                Arrays.sort(reds);
                Arrays.sort(greens);
                Arrays.sort(blues);
                int newA = alphas[size / 2];
                int newR = reds[size / 2];
                int newG = greens[size / 2];
                int newB = blues[size / 2];

                // combine the new argb values into a new argb value and set the enlargedOutput pixels to these values
                int newARGB = (newA << 24) | (newR << 16) | (newG << 8) | (newB);
                output.setRGB(x, y, newARGB);
            }
        }

        return output; 
    }

    public BufferedImage drawPreview(BufferedImage input) throws ImageOperationException {
        return draw(input);
    }

    @Override
    public String operationDescription() {
        return String.format("Median Filter [Radius: %dpx]", radius);
    }
    

}
