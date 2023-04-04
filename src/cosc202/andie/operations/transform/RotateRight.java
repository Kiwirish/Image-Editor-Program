package cosc202.andie.operations.transform;

import java.awt.image.*;
import cosc202.andie.ImageOperation;

/**
 * <p>
 * ImageOperation to rotate an image Right .
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
public class RotateRight implements ImageOperation, java.io.Serializable{

    public RotateRight(){
    }

    /** Applys the Rotate to the image 
     * @param input Image to be rotated 
    */
    public BufferedImage apply(BufferedImage input){
    int pixels [][] = new int [input.getWidth()][input.getHeight()];
        for(int i = 0; i < input.getWidth(); i++){
            for(int j = 0; j < input.getHeight(); j++){
                pixels[i][j] = input.getRGB(i, j);
            }
        }

        BufferedImage output = new BufferedImage(input.getHeight(), input.getWidth(), input.getType());

        int x = 0;
        for(int i = 0; i < output.getWidth(); i++){
            int y = output.getHeight()-1;
            for(int j = 0; j < output.getHeight(); j++){
                output.setRGB(x, y, pixels[j][i]);
                y--;
            }
            x++;
        }

        return output;
    }

}
    
