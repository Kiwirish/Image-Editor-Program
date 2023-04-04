package cosc202.andie.operations.transform;

import java.awt.image.*;
import cosc202.andie.ImageOperation;


public class Rotate180 implements ImageOperation, java.io.Serializable{

    public Rotate180(){
    }

    public BufferedImage method(BufferedImage input){
        int pixels [][] = new int [input.getWidth()][input.getHeight()];
        for(int i = 0; i < input.getWidth(); i++){
            for(int j = 0; j < input.getHeight(); j++){
                pixels[i][j] = input.getRGB(i, j);
            }
        }

        BufferedImage output = new BufferedImage(input.getHeight(), input.getWidth(), input.getType());

        int x = output.getWidth()-1;
        for(int i = 0; i < output.getWidth(); i++){
            int y = 0;
            for(int j = 0; j < output.getHeight(); j++){
                output.setRGB(x, y, pixels[j][i]);
                y++;
            }
            x--;
        }

// comment to push properly
        return output;
    }

    public BufferedImage apply(BufferedImage input){

        return method(method(input));
    }


}