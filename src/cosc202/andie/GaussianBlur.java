package cosc202.andie;

import java.awt.image.*;
import java.util.*;

public class GaussianBlur implements ImageOperation, java.io.Serializable {

    //data field declaration
    private int radius; 

    //constructor 
    GaussianBlur(int radius){
        this.radius = radius; 
    }
    //default constructor set radius to 1 
    GaussianBlur(){
        this(1);
    }

    public BufferedImage apply(BufferedImage input){ 

        int size = (2*radius+1) * (2*radius+1);
        //standard deviation of sigma is set to one third
        float sigma = radius/3.0f;
        //total to sum data values to be divided out so evenly blurred
        float total = 0.0f; 
        //array to store data in, is the length of the matrix size
        float[]data = new float[size]; 
        // The gaussian equation uses 2*sigma*sigma twice, so set a variable for this 
        float twoSigmaSquared = 2.0f * sigma * sigma;
        // The gaussian equation uses 1/twoSquaredSigma * pi, so rootSigma
        float rootSigma = (float) Math.sqrt(twoSigmaSquared * Math.PI);

        //for every data point, calculate the distance using Gaussian equation and variables previously set, 
        //filling the data array with new values for each value looped over
        //add this value to the total/
        for(int y = -radius; y <= radius; y++){
            for(int x = -radius ; x<=radius ; x++){
                int index = (y+radius)+size+radius+x; 
                //Gaussian equation calculation to get distance to apply 
                float distance = ( (x*x) + (y*y) ) / twoSigmaSquared;
                data[index] = (float)Math.exp(-distance)/rootSigma;
                total = total + data[index];
            }
        } 

        //set all the data values to their current value dividede
        // out by the total taken from prior loop 
        for(int i = 0 ; i < data.length ; i++){
            data[i] = data[i]/total; 
        }

        //create new Kernel passing in the size and data array 
        Kernel kernel = new Kernel(size, size, data);
        //Perform convolution on the new and inputted BufferedImage
        ConvolveOp convOp = new ConvolveOp(kernel);
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
        convOp.filter(input,output);

        return output; 

    }

}