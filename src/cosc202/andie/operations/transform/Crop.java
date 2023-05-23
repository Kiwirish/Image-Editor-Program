package cosc202.andie.operations.transform;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import cosc202.andie.ImageOperation;

/**
 * <p>
 * Crop, implements the ImageOperation class. The main purpose of the Crop class
 * is to crop the inputed image to a set size.
 * </p>
 * 
 * <p>
 * The draw method in Crop is what actually applies the crop to the image, 
 * the size of the crop is chosen from the select tool.
 * </p>
 * 
 * 
 * @author Oliver Peyroux
 * @version 1.0
 */
public class Crop implements ImageOperation {
    
	Point location;
	Dimension size;

	/**
	 * Crop Constructor, sets the inputted location and size parameters to the
	 * data fields in this class
	 * 
	 * @param location
	 * @param size
	 */
	public Crop(Point location, Dimension size){
		this.location = location;
		this.size = size;
	}

	/**
	 * draw method, just gets the inputted image and crops it to the size 
	 * selected from the select tool
	 * @return the output of the cropped image.
	 */
	public BufferedImage draw(BufferedImage input) throws ImageOperationException {
		BufferedImage output = new BufferedImage(size.width, size.height, input.getType());
		Graphics2D g2d = output.createGraphics();
		g2d.drawImage(input, -location.x, -location.y, input.getWidth(), input.getHeight(), null);
		g2d.dispose();
		return output;
	}
	/**
     * drawPreview, previews the crop before it is actually applied to the image
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
		return String.format("Crop [X:%d, Y:%d, Width:%dpx, Height:%dpx]", location.x, location.y, size.width, size.height);
	}
} 
