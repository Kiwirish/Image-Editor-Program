package cosc202.andie.operations.filter;

import java.awt.image.*;
import java.util.*;

import cosc202.andie.ImageOperation;

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
public class MeanFilter implements ImageOperation, java.io.Serializable {
    
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
    public BufferedImage apply(BufferedImage input) {

        //check for illegal argument 
        if (input == null){
            throw new IllegalArgumentException("Image to apply Mean filter to does not exist");
        }
        //assuming acceptable image is selected as input
        int r = radius; // as original radius accessed later
        
        //create enlarged image with all existing argb pixel values of old image set to the new images values 
        BufferedImage enlargedImage = new BufferedImage(input.getWidth() + r * 2, input.getHeight() + r * 2, input.getType());
        
        for(int y = 0; y <input.getHeight(); ++y){
            for(int x = 0 ; x < input.getWidth(); x++){
                enlargedImage.setRGB((x + r), (y + r), input.getRGB(x,y));
            }
        }

        int h = input.getHeight(); // set height variable for easy access in corner/edge loops
        int w = input.getWidth(); // set height variable for easy access in corner/edge loops

        //Padding:

        // set the corner pixels of the enlarged image's argb values
        
        //top left corner padding 
        for(int y = -r; y < 0; ++y){
            for(int x = -r ; x < 0 ; ++x){
                enlargedImage.setRGB((x + r), (y + r), input.getRGB(0,0));
            }
        }

        //top right corner padding 
        for(int y = -r; y < 0; ++y){
            for(int x = 0; x < r; ++x){
                enlargedImage.setRGB((w + x + r), (y + r), input.getRGB((w - 1), 0));
            }
        }

        //Bottom left corner padding
        for(int y = 0; y < r; ++y){
            for(int x = -r; x < 0; ++x){
                enlargedImage.setRGB((x + r), (h + y + r), input.getRGB(0, (h - 1)));
            }
        }

        //Bottom right corner padding
        for(int y = 0; y < r; ++y){
            for(int x = 0; x < r; ++x){
                enlargedImage.setRGB((w + x + r), (h + y + r), input.getRGB((w - 1), (h - 1)));
            }
        }

        // set the enlarged image's edges to the argb values 

        // top edge 
        for(int y = 0 ; y < r; y++){
            for(int x = 0; x < w ; ++x){
                enlargedImage.setRGB((x + r), y, input.getRGB(x,0)); 
            }
        }

        // bottom edge 
        for(int y = 0 ; y < r; ++y){
            for(int x = 0; x < w ; ++x){
                enlargedImage.setRGB((x + r), (y + r + h) , input.getRGB(x, (h - 1))); 
            }
        }

        // Left edge 
        for(int y = 0 ; y < h; ++y){
            for(int x = 0; x < r ; ++x){
                enlargedImage.setRGB((x + r), y, input.getRGB(0, y)); 
            }
        }

        // right edge 
        for(int y = 0 ; y < h; ++y){
            for(int x = 0; x < r ; ++x){
                enlargedImage.setRGB((x + r + w), (y + r), input.getRGB((w - 1), y)); 
            }
        }

        // Implement convolution on new enlargedImage from input 
        // below is supplied code

        int size = (2*radius+1) * (2*radius+1);
        float [] array = new float[size];
        Arrays.fill(array, 1.0f/size);

        Kernel kernel = new Kernel(2*radius+1, 2*radius+1, array);
        ConvolveOp convOp = new ConvolveOp(kernel);
        BufferedImage enlargedOutput = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        convOp.filter(enlargedImage, enlargedOutput);

        // Trim enlarged image and return this trimmed output as the final output of the meanFilter method
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);

        for(int y = 0 ; y < input.getHeight(); ++y){
            for(int x = 0; x < input.getWidth(); ++x){
                output.setRGB(x, y, enlargedOutput.getRGB(x, y)); 
            }
        }
        // return final MeanFiltered output iamge
        return output; 



    }
    

}
