package cosc202.andie.operations.transform;

import java.awt.image.*;
import cosc202.andie.ImageOperation;

/**
 * <p>
 * ImageOperation to rotate an image 180 degrees .
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
public class Rotate180 implements ImageOperation{

    public Rotate180(){
    }

    /** Applys the Rotate to the image 
     * runs method twice for two 90 degree turns 
     * @param input Image to be rotated 
    */
    public BufferedImage draw(BufferedImage input) throws ImageOperationException {
        return new RotateLeft().draw(new RotateLeft().draw(input));
    }

    public BufferedImage drawPreview(BufferedImage input) throws ImageOperationException {
        return draw(input);
    }

    @Override
    public String operationDescription() {
        return "180 Degree Rotation";
    }


}