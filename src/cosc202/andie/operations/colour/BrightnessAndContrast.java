package cosc202.andie.operations.colour;

import java.awt.image.*;

import cosc202.andie.ImageOperation;

public class BrightnessAndContrast implements ImageOperation, java.io.Serializable  {
    private int brightness;
    private int contrast;

    public BrightnessAndContrast(int brightness, int contrast) {
        this.brightness = brightness;
        this.contrast = contrast;
    }

    public BufferedImage apply(BufferedImage input) {

        //brightness change b
        //contrast change c
        //if pixel v is in range [0,255], this gets updated to new value v'
  
        

        for (int y = 0; y < input.getHeight(); ++y) {
            for (int x = 0; x < input.getWidth(); ++x) {
                int rgb = input.getRGB(x, y);
                System.out.print(rgb);
                double editedV = (1 + contrast / 100) * (rgb - 127.5) + 127.5 * (1 + brightness /100);

                //int a = (argb & 0xFF000000) >> 24;
                int r = input.getRed();
                int g = input.getRed();
                int b = (rgb & 0x000000FF);

                //int adjusted = (int) Math.round(0.3*r + 0.6*g + 0.1*b);

                //rgb = (a << 24) | (grey << 16) | (grey << 8) | grey;
                //input.setRGB(x, y, rgb); 
            }
            System.out.println();
        }
        
        return input;
    }   
}
