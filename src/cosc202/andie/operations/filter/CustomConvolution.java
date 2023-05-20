package cosc202.andie.operations.filter;

import java.awt.image.*;

import cosc202.andie.Utils;

public class CustomConvolution {
    
    public static BufferedImage applyKernel(BufferedImage input, float[] kernel) {
        //set radius
        int kernelDiameter = (int)Math.sqrt(kernel.length);
        int r = (int)Math.floor(kernelDiameter / 2);
    
        //create enlarged image with all existing argb pixel values of old image set to the new images values 
        BufferedImage enlargedImage = Utils.expandEdges(input, r);

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

        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);

        for(int y = 0 ; y < output.getHeight(); ++y){
            for(int x = 0 ; x < output.getWidth(); ++x){

                int argb = enlargedImage.getRGB(x+r,y+r); 
                int rResult = 0; 
                int gResult = 0; 
                int bResult = 0; 
                int a = (argb & 0xFF000000) >> 24; 
                for(int k = 0 ; k < kernelDiameter*kernelDiameter; k++) {

                    int i = (k % kernelDiameter) -1; 
                    int j = (k / kernelDiameter) -1; 
                    int iargb = enlargedImage.getRGB((x + r + i), (y + r + j));
                    int rValue = (iargb & 0x00FF0000) >> 16;
                    int gValue = (iargb & 0x0000FF00) >> 8;
                    int bValue = (iargb & 0x000000FF);

                    rResult += kernel[k] * rValue; 
                    gResult += kernel[k] * gValue; 
                    bResult += kernel[k] * bValue; 
                }
                // calculate output pixel color values 
                int outputA = a;

                int outputR = Math.min(Math.max((rResult+128), 0), 255);
                int outputG = Math.min(Math.max((gResult+128), 0), 255);
                int outputB = Math.min(Math.max((bResult+128), 0), 255);

                int outputARGB = (outputA << 24) | (outputR << 16) | (outputG << 8) | (outputB);

                output.setRGB(x, y, outputARGB);
            }
        }

        return output; 

    }

}
