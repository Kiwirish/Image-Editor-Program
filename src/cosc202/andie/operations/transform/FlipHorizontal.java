package cosc202.andie.operations.transform;


import java.awt.image.*;
import cosc202.andie.ImageOperation;

public class FlipHorizontal implements ImageOperation, java.io.Serializable{
    

    public FlipHorizontal(){}
            

    public BufferedImage apply(BufferedImage input){
    
    
        
        for(int i = input.getWidth(); i > 0; i--){
            for(int j = input.getHeight(); j > 0; j--){
                int left = input.getRGB(i,j);
                int right = input.getRGB(input.getWidth()-(i-1),j);

                input.setRGB(i, j, right);
                input.setRGB(input.getWidth()-(i-1), j, left);
            }
        }

        return input;
    }
 }

