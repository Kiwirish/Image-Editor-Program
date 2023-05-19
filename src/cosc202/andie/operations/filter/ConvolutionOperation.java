package cosc202.andie.operations.filter;

import java.awt.image.*;
import cosc202.andie.ImageOperation.ImageOperationException;

public class ConvolutionOperation {
    
    // data field declaring kernel float array to store the inputted kernel 
    // when EmbosFilter is called. The values of this kernel will depend on what the 
    // user picks within the menu/controller interface, ie N, E, S, W, inbetween directions,
    // or a Horizontal or Vetical sobel filter. 

    float[] kernel; 

    // construct EmbossFilter object using input kernel float array chosen by user at menu/controller interface
    public ConvolutionOperation(float[] kernel){
        this.kernel = kernel; 
    }


    public BufferedImage negativeResults(BufferedImage input) {
        //check for illegal argument 
        if (input == null){
            throw new IllegalArgumentException("Image to apply Sharpen filter to does not exist");
        }
        int r = 1; //set radius to 1 
        int h = input.getHeight(); // set height variable for easy access in corner/edge loops
        int w = input.getWidth(); // set height variable for easy access in corner/edge loops

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

        //BufferedImage enlargedOutput = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);

        for(int y = r ; y < h - r - 1 ; ++y){
            for(int x = r ; x < w - r - 1 ; ++x){

                int argb = input.getRGB(x,y); 
                int rResult = 0; 
                int gResult = 0; 
                int bResult = 0; 
                int a = (argb & 0xFF000000) >> 24; 
                // loop to cycle through each element of the 3x3 convolution kernel, as the radius is set to 1, the expre4ssion is just equivalent to 9
                for(int k = 0 ; k < ((1+(2*r))*(1+(2*r))); k++) {

                    int i = k % 3; 
                    int j = k / 3; 
                    int iargb = input.getRGB((x + i -1), (y + j - 1));
                    int rValue = (iargb & 0x00FF0000) >> 16;
                    int gValue = (iargb & 0x0000FF00) >> 8;
                    int bValue = (iargb & 0x000000FF);


                    rResult += kernel[k] * rValue; 
                    gResult += kernel[k] * gValue; 
                    bResult += kernel[k] * bValue; 
                }
                // calculate output pixel color values 
                int outputA = a;
                
                //method that takes this int value and just changes it?
                //e.g outputR = NegativeResults(outputR);

                //For these filters the result might be positive or negative, and zero is often the ‘average’ result. The
                //usual way to display the results is to shift the output so that zero becomes a mid-value (e.g. 127 or 128); 

                //For these filters the result might be positive or negative, and zero is often the ‘average’ result. The
                //usual way to display the results is to shift the output so that zero becomes a mid-value (e.g. 127 or 128);
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

                input.setRGB(x, y, outputARGB);

            }
        }
        // Trim enlarged image and return this trimmed output as the final output of the meanFilter method
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);

        for(int y = 0 ; y < input.getHeight(); ++y){
            for(int x = 0; x < input.getWidth(); ++x){
                output.setRGB(x, y, input.getRGB(x, y)); 
            }
        }
        
        return output; 


    }

    public BufferedImage drawPreview(BufferedImage input) throws ImageOperationException {
        return negativeResults(input);
    }


}


