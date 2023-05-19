package cosc202.andie.operations.transform;


import java.awt.image.*;
import cosc202.andie.ImageOperation;

/**
 * <p>
 * ImageOperation to Flip an image vertical.
 * </p>
 * 
 * <p>
 * Swaps the pixels then over ride the old image. 
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

public class FlipVertical implements ImageOperation{

    public FlipVertical(){
    }

    /** Applys the Flip vertical to the image */
    public BufferedImage draw(BufferedImage input) throws ImageOperationException {
    int pixels [][] = new int [input.getWidth()][input.getHeight()];
        for(int i = 0; i < input.getWidth(); i++){
            for(int j = 0; j < input.getHeight(); j++){
                pixels[i][j] = input.getRGB(i, j);
            }
        }
        int x = 0;
        for(int i = 0; i < input.getWidth(); i++){
            int y = 0;
            for(int j = input.getHeight()-1; j >= 0; j--){
                input.setRGB(x, y, pixels[i][j]);
                y++;
            }
            x++;
        }

        return input;
    }

    public BufferedImage drawPreview(BufferedImage input) throws ImageOperationException {
        return draw(input);
    }

    @Override
    public String operationDescription() {
        return "Vertical Flip";
    }
    
}
