package cosc202.andie.operations.transform;

import java.awt.Image;
import java.awt.image.*;
import java.io.File;
import javax.imageio.ImageIO;

import cosc202.andie.ImageOperation;


public class Resize implements ImageOperation, java.io.Serializable{
    private int percentage;

    public Resize(int percentage){
            this.percentage = percentage;
    }

    public BufferedImage apply(BufferedImage input){
        
        //Image output = (BufferedImage) input.getScaledInstance((input.getWidth()*percentage)/100, (input.getHeight()*percentage)/100, Image.SCALE_SMOOTH);
        BufferedImage result = new BufferedImage((input.getWidth()*percentage)/100, (input.getHeight()*percentage)/100, BufferedImage.TYPE_3BYTE_BGR);
        result.createGraphics().drawImage(result, 0 ,0,null);
        
        return result;
    }
    
}
