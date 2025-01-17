package cosc202.andie.operations.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import cosc202.andie.ImageOperation;

/**
 * <p>
 * Class to draw a Rectangle shape 
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">cc by-nc-sa 4.0</a>
 * </p>
 * 
 * @author Jeb Nicholson
 * @version 1.0
 */

public class RectangleShape implements ImageOperation  {

	private Rectangle rect;
	private Color strokeColor;
	private Color fillColor;
	private int strokeWidth;

	/** Constructor for Rectangle Shape */
	public RectangleShape(Rectangle rectangle, Color strokeColor, Color fillColor, int strokeWidth) {
		this.rect = rectangle;
		this.strokeColor = strokeColor;
		this.fillColor = fillColor;
		this.strokeWidth = strokeWidth;
	}

	@Override
	public BufferedImage draw(BufferedImage input) throws ImageOperationException {
		Graphics2D g = input.createGraphics();
		g.setColor(fillColor);
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
		g.setColor(strokeColor);
		g.setStroke(new BasicStroke(strokeWidth));
		g.drawRect(rect.x, rect.y, rect.width, rect.height);
		g.dispose();
		return input;
	}

	@Override
	public BufferedImage drawPreview(BufferedImage input) throws ImageOperationException {
		return draw(input);
	}

	@Override
	public String operationDescription() {
		return String.format("Rectangle [X:%d, Y:%d, Width:%dpx, Height:%dpx] [Fill: #%x, Stroke: #%x, Stroke Width: %dpx]", rect.x,
				rect.y, rect.width, rect.height, fillColor.getRGB(), strokeColor.getRGB(), strokeWidth);
	}

	
}
