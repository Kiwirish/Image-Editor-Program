package cosc202.andie.operations.transform;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import cosc202.andie.ImageOperation;

public class Crop implements ImageOperation {
    
	Point location;
	Dimension size;

	public Crop(Point location, Dimension size){
		this.location = location;
		this.size = size;
	}

	public BufferedImage draw(BufferedImage input) throws ImageOperationException {
		BufferedImage output = new BufferedImage(size.width, size.height, input.getType());
		Graphics2D g2d = output.createGraphics();
		g2d.drawImage(input, -location.x, -location.y, input.getWidth(), input.getHeight(), null);
		g2d.dispose();
		return output;
	}
	public BufferedImage drawPreview(BufferedImage input) throws ImageOperationException {
		return draw(input);
	}
} 
