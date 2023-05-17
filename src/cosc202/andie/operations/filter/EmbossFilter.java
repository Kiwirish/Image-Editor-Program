package cosc202.andie.operations.filter;

import java.awt.image.*;
import java.util.*;

import cosc202.andie.ImageOperation;

/**
 * <p>
 * ImageOperation to apply an Emboss filter.
 * </p>
 * 
 * <p>
 *  Emboss filter implemented using the kernel  as an input parameter 
 *  as there are 8 possible choices for which direction the image 
 *  is to be pressed in.  North, East, South, West and all directions inbetween, 
 *  totalling to 8. 
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

public class EmbossFilter implements ImageOperation  {
    

    float[] kernel; 

    public EmbossFilter(float[] kernel){
        this.kernel = kernel; 
    }

     //check for illegal argument 
     if (input == null){
        throw new IllegalArgumentException("Image to apply Sharpen filter to does not exist");
    }
    //set radius to 1 
    int r = 1; 
    
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

    








}
