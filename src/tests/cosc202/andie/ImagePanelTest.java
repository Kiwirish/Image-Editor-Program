package tests.cosc202.andie;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Stack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import cosc202.andie.ImageOperation;
import cosc202.andie.ImagePanel;

public class ImagePanelTest {
	@Test
	void testInitialZoom() {
		ImagePanel testPanel = new ImagePanel();
		Assertions. assertEquals(100.0, testPanel.getZoom());	
	}
}
