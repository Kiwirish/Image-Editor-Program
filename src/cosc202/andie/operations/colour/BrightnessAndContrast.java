package cosc202.andie.operations.colour;

import java.awt.image.*;

import cosc202.andie.ImageOperation;
/**
 * <p>
 * ImageOperation to adjust an images brightness and contrast.
 * </p>
 * 
 * <p>
 * The images produced by this operation have adjusted rgb values,
 * which causes the contrast or brightness to change depending
 *  on the adjustment.
 * </p>
 * 
 * @author Oliver Peyroux
 * @version 1.0
 */
public class BrightnessAndContrast implements ImageOperation  {

    private int brightness;
    private int contrast;

    /**
     * <p>
     * Create a new BrightnessAndContrast operation.
     * </p>
     */
    public BrightnessAndContrast(int brightness, int contrast) {
        this.brightness = brightness;
        this.contrast = contrast;
    }
    //Construct with default size
    BrightnessAndContrast(){
        this(0, 0);
    }
    
    /**
     * <p>
     * Apply BrightnessAndContrast adjustment to an image.
     * </p>
     * 
     * <p>
     * The conversion of the red, green, and blue values uses a calculation,
     * with inserted brightness and contrast values.
     * </p>
     * 
     * @param input The image to be adjusted
     * @return The resulting adjusted image.
     */
    public BufferedImage draw(BufferedImage input) throws ImageOperationException {

        //brightness change b
        //contrast change c
        //if pixel v is in range [0,255], this gets updated to new value v'

        for (int y = 0; y < input.getHeight(); ++y) {
            for (int x = 0; x < input.getWidth(); ++x) {

                int pixel = input.getRGB(x, y);
                int a = (pixel & 0xFF000000) >> 24;
                int r = (pixel & 0x00FF0000) >> 16;
                int g = (pixel & 0x0000FF00) >> 8;
                int b = (pixel & 0x000000FF);

                int red = (int)((1 + (contrast / 100.0)) * (r - 127.5) + 127.5 * (1 + (brightness/100.0)));   
                int green = (int)((1 + (contrast / 100.0 )) * (g - 127.5) + 127.5 * (1 + (brightness/100.0)));
                int blue = (int)((1 + (contrast / 100.0 )) * (b - 127.5) + 127.5 * (1 + (brightness/100.0)));
                //need it to be within 0-255 for red,green,blue. (hence code below)
                red = (int)Math.max(0, Math.min(255, red));
                green = (int)Math.max(0, Math.min(255, green));
                blue = (int)Math.max(0, Math.min(255, blue));

                pixel = (a << 24) | (red << 16) | (green << 8) | blue;
                input.setRGB(x, y, pixel);
            }
        }
                    
        return input;
        
    }

    /**
     * drawPreview, previews the BrightnessAndContrast action before it is actually applied to the image
     */
    public BufferedImage drawPreview(BufferedImage input) throws ImageOperationException {
        return draw(input);
    }

    /**
     * operationDescription, gets a description of the operation
     * and returns it. This is used in the macro panel
     */
    @Override
    public String operationDescription() {
        String brightness = (this.brightness >= 0 ? "+" + this.brightness : "" + this.brightness) + "%";
        String contrast = (this.contrast >= 0 ? "+" + this.contrast : "" + this.contrast) + "%";
        return String.format("Brightness & Contrast filter [Brightness: %s, Contrast: %s]", brightness, contrast);
    }
}
