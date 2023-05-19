package cosc202.andie.operations.filter;

import java.awt.image.*;
import java.util.*;

import cosc202.andie.ImageOperation;

/**
 * <p>
 * ImageOperation to apply an Emboss filter.
 * </p>
 * 
 * <p>
 *  Emboss filter creates the effect ofthe image being pressced into or rasied 
 *  out of a sheet of paper, there are eight possible filter kernels to 
 *  work with depending on the direction of embossing they simulate. 
 *  It is implemented using the kernel  as an input parameter 
 *  as there are 8 possible choices for which direction the image 
 *  is to be pressed in.  North, East, South, West and all directions inbetween, 
 *  totalling to 8. 
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @see java.awt.image.ConvolveOp
 * @author Blake Leahy
 * @version 1.0
 */

public class EmbossFilter implements ImageOperation  {
    
    // data field declaring kernel float array to store the inputted kernel 
    // when EmbosFilter is called. The values of this kernel will depend on what the 
    // user picks within the menu/controller interface, ie N, E, S, W, inbetween directions,
    // or a Horizontal or Vetical sobel filter. 

    int angle; 


    // construct EmbossFilter object using input kernel float array chosen by user at menu/controller interface
    public EmbossFilter(int angle){
        this.angle = angle; 





    }
    // *********************************************
    // below code up until the manual convolution is code used in my prior filters to pad and extend filter edges
    // *********************************************


    public BufferedImage draw(BufferedImage input) throws ImageOperationException {
        //FIGURE out the kernel
        float[] kernel = new float[9];

        if(angle == 0){ //N
            kernel[1] = 1;
            kernel[7] = -1;
        }else if (angle == 45){ //NE 
            kernel[2] = 1;
            kernel[6] = -1;
        }else if (angle == 90){ //E
            kernel[1] = -1;
            kernel[7] = 1;
        }else if(angle == 135){ //SE
            kernel[0] = -1;
            kernel[8] = 1;
        }else if(angle == 180){ // S
            kernel[1] = -1;
            kernel[7] = 1;
        }else if(angle == 225 ){ // SW 
            kernel[2] = -1;
            kernel[6] = 1;        
        }else if(angle == 270){ // W 
            kernel[3] = 1;
            kernel[5] = -1;
        }else { // must be NW
            kernel[0] = 1;
            kernel[8] = -1;       
        }

        return EmbossSobelHelper.applyKernel(input, kernel);


    }// end draw method 

    public BufferedImage drawPreview(BufferedImage input) throws ImageOperationException {
        return draw(input);
    }

    @Override
    public String operationDescription() {
        return String.format("Emboss Filter [Angle: %ddeg]", angle);
    }


}
