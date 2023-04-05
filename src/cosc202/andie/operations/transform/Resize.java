package cosc202.andie.operations.transform;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import cosc202.andie.ImageOperation;

import static cosc202.andie.LanguageConfig.msg;

public class Resize implements ImageOperation, java.io.Serializable{

    private int option;

    public Resize(int option){
        this.option = option;
    }

    public BufferedImage apply(BufferedImage input) throws ImageOperationException {

        double scale = (double)option/100;
        int scaledWidth = (int)Math.max((input.getWidth() * scale),1);
        int scaledHeight = (int)Math.max((input.getHeight() * scale),1);

        if (scaledWidth > 20000 || scaledHeight > 20000)
            throw new ImageOperationException(msg("Resize_Too_Large_Error"));

        BufferedImage output = new BufferedImage(scaledWidth, scaledHeight, input.getType());
        
        Graphics2D graphics2D = output.createGraphics();
        graphics2D.drawImage(input,0,0,scaledWidth , scaledHeight, null);

        return output;
    }

}
