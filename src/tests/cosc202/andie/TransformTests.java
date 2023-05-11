package tests.cosc202.andie;

import java.awt.image.BufferedImage;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import cosc202.andie.LanguageConfig;
import cosc202.andie.ImageOperation.ImageOperationException;
import cosc202.andie.operations.transform.FlipHorizontal;
import cosc202.andie.operations.transform.FlipVertical;
import cosc202.andie.operations.transform.Resize;
import cosc202.andie.operations.transform.RotateRight;

/**
 * <p>
 * Tests for the Transform operations.
 * </p>
 * 
 * @see cosc202.andie.operations.transform
 * @author Jeb Nicholson
 */
public class TransformTests {
	@Test	
	public void testResizeScales() throws Exception{
		LanguageConfig.init();

		BufferedImage testImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		Resize resize = new Resize(150);
		BufferedImage resized = resize.draw(testImage);
		Assertions.assertEquals(150, resized.getWidth());

		resize = new Resize(0);
		resized = resize.draw(testImage);
		Assertions.assertEquals(1, resized.getWidth());

		resize = new Resize(-20);
		resized = resize.draw(testImage);
		Assertions.assertEquals(1, resized.getWidth());

		resize = new Resize(100000);
		final Resize resize2 = resize;
		Assertions.assertThrows(ImageOperationException.class, () -> resize2.draw(testImage));
	}

	@Test
	public void testFlipHorizontal() throws Exception{
		LanguageConfig.init();

		BufferedImage testImage = new BufferedImage(50, 100, BufferedImage.TYPE_INT_ARGB);
		testImage.setRGB(0, 0, 0xFF0000);
		FlipHorizontal flip = new FlipHorizontal();
		BufferedImage flipped = flip.draw(testImage);

		Assertions.assertEquals(50, flipped.getWidth());
		Assertions.assertEquals(100, flipped.getHeight());
		Assertions.assertEquals(0xFF0000, flipped.getRGB(testImage.getWidth()-1, 0));

		testImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		flipped = flip.draw(testImage);
		Assertions.assertEquals(1, flipped.getWidth());
		Assertions.assertEquals(1, flipped.getHeight());
	}

	@Test
	public void testFlipVertical() throws Exception{
		LanguageConfig.init();

		BufferedImage testImage = new BufferedImage(50, 100, BufferedImage.TYPE_INT_ARGB);
		testImage.setRGB(0, 0, 0xFF0000);
		FlipVertical flip = new FlipVertical();
		BufferedImage flipped = flip.draw(testImage);
		Assertions.assertEquals(50, flipped.getWidth());
		Assertions.assertEquals(100, flipped.getHeight());
		Assertions.assertEquals(0xFF0000, flipped.getRGB(0, testImage.getHeight()-1));
	}

	@Test
	public void testRotateRight() throws Exception{

		BufferedImage testImage = new BufferedImage(50, 100, BufferedImage.TYPE_INT_ARGB);
		testImage.setRGB(0, 0, 0xFF0000);
		RotateRight rotate = new RotateRight();
		BufferedImage rotated = rotate.draw(testImage);
		Assertions.assertEquals(100,rotated.getWidth());
		Assertions.assertEquals(50,rotated.getHeight());
		Assertions.assertEquals(0xFF0000,rotated.getRGB(rotated.getWidth()-1, 0));
	}
}
