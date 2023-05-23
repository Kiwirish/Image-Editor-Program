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
    /**
     * horizontal data field, whether its applied
     * horizontally or vertically.
     */
    boolean horizontal; 

    /**
     * SobelFilter constructor, sets the inputted horizontal
     * to this horizontal data field.
     * @param horizontal
     */
    public SobelFilter(boolean horizontal){
        this.horizontal = horizontal; 
    }


    /**
     *  The draw method, figures out which angle is inputted and selects the kernel for that
     *  then applies the SobelFilter to the image.
     *
     */
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

    /**
     * drawPreview, previews the SobelFilter before it is actually applied to the image
     */
    public BufferedImage drawPreview(BufferedImage input) throws ImageOperationException {
        return draw(input);
    }

    /**
     * operationDescription, gets a description of the operation
     * and returns it. This is used in the macro panel
     */
    @Override
    public String operationDescription() {
        return String.format("Sobel Filter [%s]", horizontal ? "Horizontal" : "Vertical");
    }

}
