package cosc202.andie.operations.transform;

import java.awt.image.*;
import cosc202.andie.ImageOperation;
/**
 * <p>
 * ImageOperation to rotate an image right .
 * </p>
 * 
 * <p>
 * Swaps the pixels then overrides the old image. 
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @see java.awt.image.ConvolveOp
 * @author Bernard Pieters
 * @version 1.0
*/

public class RotateRight implements ImageOperation{

    public RotateRight(){
    }
    /** the draw method, applies the rotate right filter
     *  to the inputted image.
     * 
     * @param input Image to be rotated 
     */
    public BufferedImage draw(BufferedImage input) throws ImageOperationException {

    int pixels [][] = new int [input.getWidth()][input.getHeight()];
        for(int i = 0; i < input.getWidth(); i++){
            for(int j = 0; j < input.getHeight(); j++){
                pixels[i][j] = input.getRGB(i, j);
            }
        }

        BufferedImage output = new BufferedImage(input.getHeight(), input.getWidth(), input.getType());

        int x = output.getWidth()-1;
        for(int i = 0; i < output.getWidth(); i++){
            int y = 0;
            for(int j = 0; j < output.getHeight(); j++){
                output.setRGB(x, y, pixels[j][i]);
                y++;
            }
            x--;
        }

        return output;
    }
    /**
     * drawPreview, previews the Right Rotation before it is actually applied to the image
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
        return "90 Degree Clockwise Rotation";
    }

}