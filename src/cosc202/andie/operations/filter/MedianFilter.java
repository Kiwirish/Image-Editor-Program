package cosc202.andie.operations.filter;

import java.awt.image.*;
import java.util.*;

import cosc202.andie.ImageOperation;

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
public class MedianFilter implements ImageOperation, java.io.Serializable {
    
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
        BufferedImage enlargedImage = new BufferedImage(input.getWidth() + r * 2, input.getHeight() + r * 2, input.getType());
        
        for(int y = 0; y < input.getHeight(); y++){
            for(int x = 0; x < input.getWidth(); x++){
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

        // As this filter is not done via a convolution,
        // process each argb channel of each pixel separately. 
        // For each pixel location of argb's, store in separate lists of values for 
        // alpha, red, green and blue. 
        // Sort through these lists and find the middle value (median) for the output image. 

        
        BufferedImage enlargedOutput = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        // arrayLists to store argb values to sort through later 
        ArrayList<Integer> aList = new ArrayList<Integer>();
        ArrayList<Integer> rList = new ArrayList<Integer>();
        ArrayList<Integer> gList = new ArrayList<Integer>();
        ArrayList<Integer> bList = new ArrayList<Integer>();

        // outer for loop to access each pixel
        // for each pixel, an inner loop is created to iterate over all neighboring pixels to 
        // the current pixel the outer loop is on - only iterate within the size of the window. 
        for(int y = r ; y <= input.getHeight() - r - 1 ; y++){
            for(int x = r ; x <= input.getWidth() - r - 1 ; x++){
                // clear lists? 
                aList.clear();
                rList.clear();
                gList.clear();
                bList.clear();
                for(int i = -r ; i <= r ; i++){
                    for(int j = -r ; j <= r ; j++){
                        int argb = 0;
                        int pixel_x = x + i;
                        int pixel_y = y + j;
                        argb = input.getRGB(pixel_x, pixel_y);
                        int alpha = (argb & 0xFF000000) >> 24;
                        int red = (argb & 0x00FF0000) >> 16;
                        int green = (argb & 0x0000FF00) >> 8;
                        int blue = (argb & 0x000000FF);
                        aList.add(alpha);
                        rList.add(red);
                        gList.add(green);
                        bList.add(blue);
                    }
                } 
                // after neighboring pixels iterated over and list's filled, sort these four lists in ascending order
                // set the middle value (median) as the new argb values then apply these to enlargedOutput image 
                Collections.sort(aList);
                Collections.sort(rList);
                Collections.sort(gList);
                Collections.sort(bList);
                int newA = aList.get((size + 1) / 2);
                int newR = rList.get((size + 1) / 2);
                int newG = gList.get((size + 1) / 2);
                int newB = bList.get((size + 1) / 2);

                // combine the new argb values into a new argb value and set the enlargedOutput pixels to these values
                int newARGB = (newA << 24) | (newR << 16) | (newG << 8) | (newB);
                enlargedOutput.setRGB(x, y, newARGB);



            }
        }

        // Trim enlarged image and return this trimmed output as the final output of the apply class 
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);

        for(int y = 0 ; y < input.getHeight(); y++){
            for(int x = 0; x < input.getWidth(); x++){
                output.setRGB(x, y, enlargedOutput.getRGB(x, y)); 
            }
        }
        // return final medianFiltered output iamge
        return output; 



    }


    public BufferedImage drawPreview(BufferedImage input) throws ImageOperationException {
        return draw(input);
    }
    

}
