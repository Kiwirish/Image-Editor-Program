package cosc202.andie.operations.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;

import cosc202.andie.ImageOperation;

/**
 * <p>
 * Class to draw a Line 
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">cc by-nc-sa 4.0</a>
 * </p>
 * 
 * @author Jeb Nicholson
 * @version 1.0
 */

public class Line implements ImageOperation  {

	private Point p1;
	private Point p2;
	private Color color;
	private int width;

	/** Constuctor for the line */
	public Line(Point p1, Point p2, Color color, int width) {
		this.p1 = p1;
		this.p2 = p2;
		this.color = color;
		this.width = width;
	}

	@Override
	public BufferedImage draw(BufferedImage input) throws ImageOperationException {
		Graphics2D g = input.createGraphics();
		// Enable anti-aliasing only if the input has a png color palette with enough colors
		if (input.getColorModel().getPixelSize() > 4)
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(color);
		g.setStroke(new BasicStroke(width));
		g.drawLine(p1.x, p1.y, p2.x, p2.y);
		g.dispose();
		return input;
	}

	@Override
	public BufferedImage drawPreview(BufferedImage input) throws ImageOperationException {
		return draw(input);
	}

	@Override
	public String operationDescription() {
		return String.format("Line [From: (%d, %d), To: (%d, %d)] [Stroke: #%x, Stroke Width: %dpx]", p1.x, p1.y, p2.x, p2.y,
				color.getRGB(), width);
	}

	
}
