package cosc202.andie.operations.transform;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import cosc202.andie.ImageOperation;

/**
 * <p>
 * ImageOperation to Resize an image.
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

public class Resize implements ImageOperation, java.io.Serializable{

    private int option;


    /** @param option percentage to resize the image by */
    public Resize(int option){
        this.option = option;
    }
    /** applys the resize to the image 
     *  @param input image to be manipulated
     */
    public BufferedImage apply(BufferedImage input){
        
        int scaledWidth = (int)(input.getWidth() * ((float)option/100));
        int scaledHeight = (int)(input.getHeight() * ((float)option/100));
        if(scaledWidth <= 1){
            scaledWidth = 1;
        }
        if(scaledHeight <= 1){
            scaledHeight = 1;
        }

        BufferedImage output = new BufferedImage(scaledWidth, scaledHeight, input.getType());
        
        Graphics2D graphics2D = output.createGraphics();
        graphics2D.drawImage(input,0,0,scaledWidth , scaledHeight, null);

        return output;
    }

}
