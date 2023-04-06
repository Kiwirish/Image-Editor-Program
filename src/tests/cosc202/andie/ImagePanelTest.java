package tests.cosc202.andie;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import cosc202.andie.ImagePanel;

/**
 * <p>
 * Tests for the ImagePanel
 * </p>
 * 
 * @see ImagePanel
 */
public class ImagePanelTest {
	@Test
	void testInitialZoom() {
		ImagePanel testPanel = new ImagePanel();
		Assertions. assertEquals(100.0, testPanel.getZoom());	
	}
}
