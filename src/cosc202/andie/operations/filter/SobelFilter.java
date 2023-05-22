package cosc202.andie.operations.filter;

import java.awt.image.*;

import cosc202.andie.ImageOperation;

/**
 * <p>
 * ImageOperation to apply an Sobel filter.
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

public class SobelFilter implements ImageOperation  {
    
    boolean horizontal;; 

    public SobelFilter(boolean horizontal){
        this.horizontal = horizontal; 
    }


    public BufferedImage draw(BufferedImage input) throws ImageOperationException {
        //Figure out the kernel
        float[] kernel = new float[9];
        if (horizontal) {
            kernel[0] = -1/2;
            kernel[2] = 1/2;
            kernel[3] = -1;
            kernel[5] = 1;
            kernel[6] = -1/2;
            kernel[8] = 1/2;
        } else {
            kernel[0] = -1/2;
            kernel[1] = -1;
            kernel[2] = -1/2;
            kernel[6] = 1/2;
            kernel[7] = 1;
            kernel[8] = 1/2;
        }

        return CustomConvolution.applyKernel(input, kernel);

    }// end draw method 

    public BufferedImage drawPreview(BufferedImage input) throws ImageOperationException {
        return draw(input);
    }

    @Override
    public String operationDescription() {
        return String.format("Sobel Filter [%s]", horizontal ? "Horizontal" : "Vertical");
    }

}
