package cosc202.andie;
import java.awt.image.*;
import java.util.*;

public class MedianFilter implements ImageOperation, java.io.Serializable {
    
    /* MedianFilter is not done as a convolution
    like GaussianBlur and SharpenFilter 
    It must be done similarly to ConvertToGrey class in terms 
    of individual pixel access 
    */

    private int radius; 


    MedianFilter(int radius){
        this.radius = radius; 
    }
    //default constructor sets radius to 1 
    MedianFilter(){
        this(1);
    }

    public BufferedImage apply(BufferedImage input){

        int size = (2*radius+1) * (2*radius+1);
        float[] alphas = new float[size];
        float[] reds = new float[size];
        float[] blues = new float[size];
        float[] greens = new float[size];

        //access and separate out individual pixel values
        //into different colour channels like in ConvertToGrey
        //Create list for each value of each colour to be stored in
        //then loop over this and take the middle value to use 
        //for the final output image 
        for (int y = 0; y < input.getHeight(); ++y) {
            for (int x = 0; x < input.getWidth(); ++x) {
                int index=0;
                int argb = input.getRGB(x, y);
                int a = (argb & 0xFF000000) >> 24;
                int r = (argb & 0x00FF0000) >> 16;
                int g = (argb & 0x0000FF00) >> 8;
                int b = (argb & 0x000000FF);
                
                alphas[index] = a;
                reds[index] = r;
                blues[index] = b;
                greens[index] = g;

                index++;
            
            }    
        }
        //sort each array 

        //find middle value to use in output image        
        

    } 


}
