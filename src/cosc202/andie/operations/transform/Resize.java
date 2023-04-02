package cosc202.andie.operations.transform;

import java.awt.Graphics2D;
import java.awt.image.*;

import cosc202.andie.ImageOperation;


public class Resize implements ImageOperation, java.io.Serializable{
    private int percentage;

    public Resize(int percentage){
        this.percentage = percentage;
    }

    public BufferedImage apply(BufferedImage input){
        
        int scaledWidth = (int)(input.getWidth() * ((float)percentage/100));
        int scaledHeight = (int)(input.getHeight() * ((float)percentage/100));

        BufferedImage output = new BufferedImage(scaledWidth, scaledHeight, input.getType());
        
        Graphics2D graphics2D = output.createGraphics();
        graphics2D.drawImage(input,0,0,scaledWidth , scaledHeight, null);

        return output;
    }

}
