package cosc202.andie.operations.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import cosc202.andie.ImageOperation;

public class Rectangle implements ImageOperation  {

	private Point position;
	private Dimension size;
	private Color strokeColor;
	private Color fillColor;
	private int strokeWidth;

	public Rectangle(Point position, Dimension size, Color strokeColor, Color fillColor, int strokeWidth) {
		this.position = position;
		this.size = size;
		this.strokeColor = strokeColor;
		this.fillColor = fillColor;
		this.strokeWidth = strokeWidth;
	}

	@Override
	public BufferedImage draw(BufferedImage input) throws ImageOperationException {
		Graphics2D g = input.createGraphics();
		g.setColor(fillColor);
		g.fillRect(position.x, position.y, size.width, size.height);
		g.setColor(strokeColor);
		g.setStroke(new BasicStroke(strokeWidth));
		g.drawRect(position.x, position.y, size.width, size.height);
		g.dispose();
		return input;
	}

	@Override
	public BufferedImage drawPreview(BufferedImage input) throws ImageOperationException {
		return draw(input);
	}

	
}
