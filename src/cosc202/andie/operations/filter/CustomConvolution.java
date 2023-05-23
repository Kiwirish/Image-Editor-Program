package cosc202.andie.operations.filter;

import java.awt.image.*;

import cosc202.andie.Utils;

/**
 * <p>
 * CustomConvolution, applies a custom convolution that allows for negative results in pixels
 * </p>
 * 
 * <p>
 *  CustomConvolution has an applyKernel method that creates a custom convolution, 
 *  by iterating over each pixel. This method allows "negative results", by
 *  adding the mid-value to the raw result of the filters. The image is also enlarged
 *  in the process.
 * </p>
 * 
 * 
 * @author Oliver Peyroux
 * @version 1.0
 */
public class CustomConvolution {
    /**
     * <p>
     * applyKernel, applies the custom convolution to the image
     * </p>
     * 
     * @param input
     * @param kernel
     * @return the resulting Custom Convolution
     */
    
    public static BufferedImage applyKernel(BufferedImage input, float[] kernel) {
        //set radius
        int kernelDiameter = (int)Math.sqrt(kernel.length);
        int r = (int)Math.floor(kernelDiameter / 2);
    
        //create enlarged image with all existing argb pixel values of old image set to the new images values 
        BufferedImage enlargedImage = Utils.expandEdges(input, r);

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
