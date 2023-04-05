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
public class Rotate180 implements ImageOperation, java.io.Serializable{

    public Rotate180(){
    }

    /** Applys the Rotate to the image 
     * @param input Image to be rotated 
    */
    public BufferedImage method(BufferedImage input){
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

// comment to push properly
        return output;
    }

    /** runs method twice for two 90 degree turns */
    public BufferedImage apply(BufferedImage input){

        return method(method(input));
    }


}