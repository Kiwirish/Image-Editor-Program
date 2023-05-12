package cosc202.andie.operations.filter;

import java.awt.image.*;

import cosc202.andie.ImageOperation;

/**
 * <p>
 * ImageOperation to apply a Sharpen filter.
 * </p>
 * 
 * <p>
 * A Sharpen filter blurs an image by replacing each pixel by the average of the
 * pixels in a surrounding neighbourhood, and can be implemented by a convoloution.
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
public class SharpenFilter implements ImageOperation {
    
    //construct a sharpenFilter 
    public SharpenFilter(){ }


    /**
     * <p>
     * Apply a sharpen filter to an image.
     * </p>
     * 
     * <p>
     * As with many filters, the sharpen filter is implemented via convolution.
     * The size of the convolution kernel is 3x3 as given.  
     * </p>
     * 
     * @param input The image to apply the Sharpen filter to.
     * @return The resulting (sharpened)) image.
     */
    public BufferedImage draw(BufferedImage input) throws ImageOperationException {

        //check for illegal argument 
        if (input == null){
            throw new IllegalArgumentException("Image to apply Sharpen filter to does not exist");
        }
        //set radius to 1 
        int r = 1; 
        
        //create enlarged image with all existing argb pixel values of old image set to the new images values 
        BufferedImage enlargedImage = new BufferedImage(input.getWidth() + r * 2, input.getHeight() + r * 2, input.getType());
        
        for(int y = 0; y <input.getHeight(); ++y){
            for(int x = 0 ; x < input.getWidth(); x++){
                enlargedImage.setRGB((x + r), (y + r), input.getRGB(x,y));
            }
        }

        int h = input.getHeight(); // set height variable for easy access in corner/edge loops
        int w = input.getWidth(); // set height variable for easy access in corner/edge loops

        //Padding:

        // set the corner pixels of the enlarged image's argb values
        
        //top left corner padding 
        for(int y = -r; y < 0; ++y){
            for(int x = -r ; x < 0 ; ++x){
                enlargedImage.setRGB((x + r), (y + r), input.getRGB(0,0));
            }
        }

        //top right corner padding 
        for(int y = -r; y < 0; ++y){
            for(int x = 0; x < r; ++x){
                enlargedImage.setRGB((w + x + r), (y + r), input.getRGB((w - 1), 0));
            }
        }

        //Bottom left corner padding
        for(int y = 0; y < r; ++y){
            for(int x = -r; x < 0; ++x){
                enlargedImage.setRGB((x + r), (h + y + r), input.getRGB(0, (h - 1)));
            }
        }

        //Bottom right corner padding
        for(int y = 0; y < r; ++y){
            for(int x = 0; x < r; ++x){
                enlargedImage.setRGB((w + x + r), (h + y + r), input.getRGB((w - 1), (h - 1)));
            }
        }

        // set the enlarged image's edges to the argb values 

        // top edge 
        for(int y = 0 ; y < r; y++){
            for(int x = 0; x < w ; ++x){
                enlargedImage.setRGB((x + r), y, input.getRGB(x,0)); 
            }
        }

        // bottom edge 
        for(int y = 0 ; y < r; ++y){
            for(int x = 0; x < w ; ++x){
                enlargedImage.setRGB((x + r), (y + r + h) , input.getRGB(x, (h - 1))); 
            }
        }

        // Left edge 
        for(int y = 0 ; y < h; ++y){
            for(int x = 0; x < r ; ++x){
                enlargedImage.setRGB((x + r), y, input.getRGB(0, y)); 
            }
        }

        // right edge 
        for(int y = 0 ; y < h; ++y){
            for(int x = 0; x < r ; ++x){
                enlargedImage.setRGB((x + r + w), (y + r), input.getRGB((w - 1), y)); 
            }
        }

        // Implement convolution on new enlargedImage from input 
        // below is supplied code

        // kernel values in 3x3 array 
        float[] array = { 0, -1 / 2.0f, 0,
                        -1 / 2.0f, 3, -1 / 2.0f,
                        0, -1 / 2.0f, 0 };

        Kernel kernel = new Kernel(3 ,3 , array);
        ConvolveOp convOp = new ConvolveOp(kernel);
        BufferedImage enlargedOutput = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);
        convOp.filter(enlargedImage, enlargedOutput);

        // Trim enlarged image and return this trimmed output as the final output of the meanFilter method
        BufferedImage output = new BufferedImage(input.getColorModel(), input.copyData(null), input.isAlphaPremultiplied(), null);

        for(int y = 0 ; y < input.getHeight(); ++y){
            for(int x = 0; x < input.getWidth(); ++x){
                output.setRGB(x, y, enlargedOutput.getRGB(x, y)); 
            }
        }
        // return final MeanFiltered output iamge
        return output; 



    }

    @Override
    public BufferedImage drawPreview(BufferedImage input) throws ImageOperationException {
        return draw(input);
    }
    

}
