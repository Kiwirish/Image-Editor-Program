package cosc202.andie.operations.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import cosc202.andie.ImageOperation;

public class Elipse implements ImageOperation {

	private Rectangle rect;
	private Color strokeColor;
	private Color fillColor;
	private int strokeWidth;

	public Elipse(Rectangle rect, Color strokeColor, Color fillColor, int strokeWidth) {
		this.rect = rect;
		this.strokeColor = strokeColor;
		this.fillColor = fillColor;
		this.strokeWidth = strokeWidth;
	}

	@Override
	public BufferedImage draw(BufferedImage input) throws ImageOperationException {
		Graphics2D g = input.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(fillColor);
		g.fillOval(rect.x, rect.y, rect.width, rect.height);
		g.setColor(strokeColor);
		g.setStroke(new BasicStroke(strokeWidth));
		g.drawOval(rect.x, rect.y, rect.width, rect.height);
		g.dispose();
		return input;
	}

	@Override
	public BufferedImage drawPreview(BufferedImage input) throws ImageOperationException {
		return draw(input);
	}

	@Override
	public String operationDescription() {
		return String.format("Elipse [X:%d, Y:%d, Width:%dpx, Height:%dpx] [Fill: #%x, Stroke: #%x, Stroke Width: %dpx]", rect.x,
				rect.y, rect.width, rect.height, fillColor.getRGB(), strokeColor.getRGB(), strokeWidth);
	}

}
