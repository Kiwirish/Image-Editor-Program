package cosc202.andie.operations.transform;

import java.awt.Graphics2D;
import java.awt.image.*;
import java.io.File;
import javax.imageio.ImageIO;

import cosc202.andie.ImageOperation;


public class Resize implements ImageOperation, java.io.Serializable{
    private int percentage;

    private int option;

    public Resize(int option){
        this.option = option;
    }

    public BufferedImage apply(BufferedImage input){
        
        int scaledWidth = (int)(input.getWidth() * ((float)option/100));
        int scaledHeight = (int)(input.getHeight() * ((float)option/100));

        BufferedImage output = new BufferedImage(scaledWidth, scaledHeight, input.getType());
        
        Graphics2D graphics2D = output.createGraphics();
        graphics2D.drawImage(input,0,0,scaledWidth , scaledHeight, null);

        return output;
    }

}
