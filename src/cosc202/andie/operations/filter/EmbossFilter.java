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
 *  Emboss filter creates the effect ofthe image being pressced into or rasied 
 *  out of a sheet of paper, there are eight possible filter kernels to 
 *  work with depending on the direction of embossing they simulate. 
 *  It is implemented using the kernel  as an input parameter 
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
    
    // data field declaring kernel float array to store the inputted kernel 
    // when EmbosFilter is called. The values of this kernel will depend on what the 
    // user picks within the menu/controller interface, ie N, E, S, W, inbetween directions,
    // or a Horizontal or Vetical sobel filter. 

    float[] kernel; 

    // construct EmbossFilter object using input kernel float array chosen by user at menu/controller interface
    public EmbossFilter(float[] kernel){
        this.kernel = kernel; 
    }
    // *********************************************
    // below code up until the manual convolution is code used in my prior filters to pad and extend filter edges
    // *********************************************


    public BufferedImage draw(BufferedImage input) throws ImageOperationException {
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

        // Implement convolution on new enlargedImage using input kernel 
        // Do so by creating the enlarged output image, iterate over each pixel 
        // in the enlarged input image (excluding the padded border), and then perform a convolution operation on each pixel
        // It iterates over each value in the kernel and matches each one to a corresponding 
        // pixel in the input image, then sums the product of the kernel value and pixel value 
        // Bitwise operations are used to extract the ARGB values. After the convolution, 
        // the ARGB values are adjusted then combined into a single integer again and sets the pixel in the output 
        // image to this ARGB combined integer value
        // OR 
        // simply use ConvolveOp built in class with input kernel? 
        // Issue with this is that the 'edge pixels' are not handled, therefore 
        // manual convolution must be done. 

        BufferedImage enlargedOutput = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);

        // Kernel embossKernel = new Kernel(3,3,kernel);

        // ConvolveOp convOp = new ConvolveOp(embossKernel)

        // convOp.filter(enlargedImage, enlargedOutput)

        for(int y = r ; y < enlargedImage.getHeight()- r - 1 ; ++y){
            for(int x = r ; x < enlargedImage.getWidth() - r - 1 ; ++x){

                int argb = enlargedImage.getRGB(x,y); 
                int rResult = 0; 
                int gResult = 0; 
                int bResult = 0; 
                int a = (argb & 0xFF000000) >> 24; 
                // loop to cycle through each element of the 3x3 convolution kernel, as the radius is set to 1, the expre4ssion is just equivalent to 9
                for(int k = 0 ; k < ((1+(2*r))*(1+(2*r))); k++) {

                    int i = k % 3; 
                    int j = k / 3; 
                    int iargb = enlargedImage.getRGB((x + i -1), (y + j - 1));
                    int rValue = (iargb & 0x00FF0000) >> 16;
                    int gValue = (iargb & 0x0000FF00) >> 8;
                    int bValue = (iargb & 0x000000FF);


                    rResult += kernel[k] * rValue; 
                    gResult += kernel[k] * gValue; 
                    bResult += kernel[k] * bValue; 
                }
                // calculate output pixel color values 
                int outputA = a;

                int outputR = rResult + 127; 
                if(outputR > 255){
                    outputR = 255; 
                }

                int outputG = gResult + 127; 
                if(outputG > 255){
                    outputG = 255; 
                }

                int outputB = bResult + 127; 
                if(outputB > 255){
                    outputB = 255; 
                }

                int outputARGB = (outputA << 24) | (outputR << 16) | (outputG << 8) | (outputB);

                enlargedOutput.setRGB(x, y, outputARGB);

            }
        }


        // Trim enlarged image and return this trimmed output as the final output of the meanFilter method
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);

        for(int y = 0 ; y < input.getHeight(); ++y){
            for(int x = 0; x < input.getWidth(); ++x){
                output.setRGB(x, y, enlargedOutput.getRGB(x, y)); 
            }
        }
        // return final MeanFiltered output iamge
        return output; 


    }// end draw method 

    public BufferedImage drawPreview(BufferedImage input) throws ImageOperationException {
        return draw(input);
    }

}
