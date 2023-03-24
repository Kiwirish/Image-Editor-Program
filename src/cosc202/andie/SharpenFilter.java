package cosc202.andie;
import java.awt.image.*;
import java.util.*;

public class SharpenFilter implements ImageOperation, java.io.Serializable {
    
    //data field declaration
    private int radius; 


    //constructor to choose radius to operate on 
    SharpenFilter(int radius){
        this.radius = radius; 
    }

    //constructor to set default 
    //radius to 1 so the image operation is 
    // done on a 3x3 space 
    SharpenFilter(){
        this(1);
    }


    /* 
     * Image operation to apply a sharpen filter
     * 
     * A sharpen filter sharpens the image by replacing each pixel  
     * with 
     */

     /* The sharpen filter is implemented via convolution alike to the MeanFilter
      * The size of the convolution kernal is 
      @param input the image to apply the sharpen filter to 
      @return the sharpened image result 
      A different convolution kernel is to be appleid to this
      as it is manipluating the matrix values separateely
     */
    public BufferedImage apply(BufferedImage input){ 
        
        //
        int size = (2*radius+1) * (2*radius+1);
        float [] array = new float[size];

        //-1.0 used as it is sharpening - bluring used +
        Arrays.fill(array, -1.0f/size);

        //set central element to higher value for sharpen 
        // etc 
        array[size/2] = size-1; 
        // create kernel and set height and width to 
        // 2radius+1 and pass in array of image matrix values 
        Kernel kernel = new Kernel(2*radius+1, 2*radius+1, array);
        // Convolve the kernel 
        ConvolveOp convOp = new ConvolveOp(kernel);
        // create new image which is a copy of the inputted image
        // taking all the data
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        // perform a convolution on the two images which have a set 
        // size to apply the filter to depending on the kernel previously passed in 
        convOp.filter(input, output);

        return output;


    }
    
}
