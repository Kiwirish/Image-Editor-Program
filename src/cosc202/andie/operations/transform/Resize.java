package cosc202.andie.operations.transform;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import cosc202.andie.ImageOperation;

import static cosc202.andie.LanguageConfig.msg;
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

public class Resize implements ImageOperation{

    private int option;


    /** @param option percentage to resize the image by */
    public Resize(int option){
        this.option = option;
    }

    /** 
     * The draw method applies the Resize  to the image 
     * 
     * @param input Image to be resized
    */
    public BufferedImage draw(BufferedImage input) throws ImageOperationException {

        double scale = (double)option/100;
        int scaledWidth = (int)Math.max((input.getWidth() * scale),1);
        int scaledHeight = (int)Math.max((input.getHeight() * scale),1);

        if (scaledWidth > 20000 || scaledHeight > 20000)
            throw new ImageOperationException(msg("Resize_Too_Large_Error"));

        BufferedImage output = new BufferedImage(scaledWidth, scaledHeight, input.getType());
        
        Graphics2D graphics2D = output.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(input,0,0,scaledWidth, scaledHeight, null);

        return output;
    }

    /**
     * drawPreview, previews the Resize before it is actually applied to the image
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
        return String.format("Resize [%d%%]", option);
    }

}
