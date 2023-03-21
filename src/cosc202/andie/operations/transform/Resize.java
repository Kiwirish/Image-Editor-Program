package cosc202.andie.operations.transform;

import java.awt.image.*;

import cosc202.andie.ImageOperation;

public class Resize implements ImageOperation, java.io.Serializable{

    public Resize(){}

    public BufferedImage apply(BufferedImage input){
        BufferedImage result = (BufferedImage) input.getScaledInstance(1, 1, 1);
        return result;
    }
    
}
