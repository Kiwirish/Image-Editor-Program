package cosc202.andie.operations.transform;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import cosc202.andie.ImageOperation;

public class Crop implements ImageOperation {
    
	Point location;
	Point size;

	public Crop(Point location, Point size){
		this.location = location;
		this.size = size;
	}

	public BufferedImage draw(BufferedImage input) throws ImageOperationException {
		//Crop input image
			BufferedImage output = new BufferedImage(size.x, size.y, input.getType());
			Graphics2D g2d = output.createGraphics();
			g2d.drawImage(input, -location.x, -location.y, input.getWidth(), input.getHeight(), null);
			g2d.dispose();
			return output;
	}
	public BufferedImage drawPreview(BufferedImage input) throws ImageOperationException {
		Graphics2D g2d = input.createGraphics();
		//Overlay a grey on areas not in the crop area
		g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, input.getWidth(), location.y);
		g2d.fillRect(0, location.y + size.y, input.getWidth(), input.getHeight() - (location.y + size.y));
		g2d.fillRect(0, location.y, location.x, size.y);
		g2d.fillRect(location.x + size.x, location.y, input.getWidth() - (location.x + size.x), size.y);

		g2d.setComposite(AlphaComposite.SrcOver);
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(Color.BLACK);

		g2d.drawRect(location.x, location.y, size.x, size.y);
		g2d.dispose();
		return input;
	}
} 
