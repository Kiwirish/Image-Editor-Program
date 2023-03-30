package cosc202.andie.operations.transform;


import java.awt.image.*;
import cosc202.andie.ImageOperation;


public class FlipVertical implements ImageOperation, java.io.Serializable{

    public FlipVertical(){
    }

    public BufferedImage apply(BufferedImage input){
    int pixels [][] = new int [input.getWidth()][input.getHeight()];
        for(int i = 0; i < input.getWidth(); i++){
            for(int j = 0; j < input.getHeight(); j++){
                pixels[i][j] = input.getRGB(i, j);
            }
        }
        int x = 0;
        for(int i = 0; i < input.getWidth(); i++){
            int y = 0;
            for(int j = input.getHeight()-1; j >= 0; j--){
                input.setRGB(x, y, pixels[i][j]);
                y++;
            }
            x++;
        }

        return input;
    }
    
}
