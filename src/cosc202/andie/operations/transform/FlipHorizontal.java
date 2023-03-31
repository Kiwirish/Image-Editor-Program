package cosc202.andie.operations.transform;


import java.awt.image.*;
import cosc202.andie.ImageOperation;

public class FlipHorizontal implements ImageOperation, java.io.Serializable{
    

    public FlipHorizontal(){}
            

    public BufferedImage apply(BufferedImage input){
    
    int pixels [][] = new int [input.getWidth()][input.getHeight()];
        for(int i = 0; i < input.getWidth(); i++){
            for(int j = 0; j < input.getHeight(); j++){
                pixels[i][j] = input.getRGB(i, j);
            }
        }
        int x = 0;
        for(int i = input.getWidth()-1; i >=0; i--){
            int y = 0;
            for(int j = 0; j < input.getHeight(); j++){
                input.setRGB(x, y, pixels[i][j]);
                y++;
            }
            x++;
        }

        return input;
    }
 }

