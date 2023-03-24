package cosc202.andie.operations.transform;



import java.awt.image.*;

import cosc202.andie.ImageOperation;

public class Flip implements ImageOperation, java.io.Serializable{

    private String direction;

    public Flip(String direction){
            this.direction = direction;
    }

    public BufferedImage apply(BufferedImage input){
        
        return input;
    }
    
}
