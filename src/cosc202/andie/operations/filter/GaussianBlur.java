package cosc202.andie.operations.filter;

import java.awt.image.*;

import cosc202.andie.ImageOperation;
import cosc202.andie.Utils;

/**
 * <p>
 * ImageOperation to apply a Gaussian blur filter.
 * </p>
 * 
 * <p>
 * A gaussian blur filter blurs an image through implementing by a convoloution.
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
public class GaussianBlur implements ImageOperation {
    
    /**
     * The size of filter to apply. A radius of 1 is a 3x3 filter, a radius of 2 a 5x5 filter, and so forth.
     */
    private int radius;

    /**
     * <p>
     * Construct a Gaussian Blur filter with the given size.
     * </p>
     * 
     * <p>
     * The size of the filter is the 'radius' of the convolution kernel used.
     * A size of 1 is a 3x3 filter, 2 is 5x5, and so on.
     * Larger filters give a stronger blurring effect.
     * </p>
     * 
     * @param radius The radius of the newly constructed MeanFilter
     */
    public GaussianBlur(int radius) {
        this.radius = radius;    
    }

    /**
     * <p>
     * Construct a Gaussian Blur filter with the default size.
     * </p
     * >
     * <p>
     * By default, a Gaussian Blur filter has radius 1.
     * </p>
     * 
     * @see MeanFilter(int)
     */
    GaussianBlur() {
        this(1);
    }

    /**
     * <p>
     * Apply a Gaussian Blur filter to an image.
     * </p>
     * 
     * <p>
     * As with many filters, the Gaussian filter is implemented via convolution.
     * The size of the convolution kernel is specified by the {@link radius}.  
     * Larger radius' lead to stronger blurring.
     * </p>
     * 
     * @param input The image to apply the Gaussian filter to.
     * @return The resulting (blurred)) image.
     */
    public BufferedImage draw(BufferedImage input) throws ImageOperationException {

        //check for illegal argument 
        if (input == null){
            throw new IllegalArgumentException("Image to apply Mean filter to does not exist");
        }
        //assuming acceptable image is selected as input: 

        // set size using users radius and create array of this size
        int size = (2 * radius + 1) * (2 * radius + 1);
        float[] array = new float[size];

        // loop over each pixel within the kernel 
        // value of x and y are the distance from the centre of the kernel, so x,y=0 at centre
        // for each kernel pixel, the x and y distance (and radius) are used to call the 
        // gaussianEquation method and added to an array.
        // also, sum all the elements in this array to later divide through by to get 
        // the 'normalised' kernel
        int index = 0;
        float arrayTotal = 0;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++, index++) {
                float kernelValue = (float)gaussianEquation(x, y, radius);
                array[index] = kernelValue;
                arrayTotal += array[index];
            }
        }

        // loop over each element of this kernelValue filled array and divide 
        // it by the arrayTotal (sum of the array), then add the result to newkernelArray
        float[] newKernelArray = new float[size]; 
        for(int i = 0 ; i <= size-1; i++){
            float value = array[i] / arrayTotal;
            newKernelArray[i] = value;
        }   

        //create enlarged image with all existing argb pixel values of old image set to the new images values 
        BufferedImage enlargedImage = Utils.expandEdges(input, radius);

        // Implement convolution on new enlargedImage from input 

        Kernel kernel = new Kernel(2*radius+1, 2*radius+1, newKernelArray);
        ConvolveOp convOp = new ConvolveOp(kernel);
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        convOp.filter(enlargedImage, output);

        // return final MeanFiltered output iamge
        return output; 

    }
    // gaussian equation method for easy access 
    public double gaussianEquation(int x, int y, int radius){
        double sigma = 1.0 / 3.0 * radius;
        double twoSigmaSquared = 2*sigma*sigma; 
        double oneOverTwoPiSigmaSquared = 1 / (Math.PI * twoSigmaSquared);
        double exponent = - ( Math.pow(x,2) + Math.pow(y,2) )/ twoSigmaSquared;
    
        double result = oneOverTwoPiSigmaSquared * Math.exp(exponent);
        return result;
    }
    /**
     * drawPreview, previews the GaussianBlur before it is actually applied to the image
     */
    public BufferedImage drawPreview(BufferedImage input) throws ImageOperationException {
        return draw(input);
    }
    /**
     * operationDescription, gets a description of the operation
     * and returns it. This is used in the macro panel
     * 
     */
    @Override
    public String operationDescription() {
        return String.format("Mean Filter [Radius: %dpx]", radius);
    }


}