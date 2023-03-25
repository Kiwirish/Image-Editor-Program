package cosc202.andie;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.*;

/**
 * <p>
 * UI display element for {@link EditableImage}s.
 * </p>
 * 
 * <p>
 * This class extends {@link JPanel} to allow for rendering of an image, as well as zooming
 * in and out. 
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class ImagePanel extends JPanel {
    
    /**
     * The image to display in the ImagePanel.
     */
    private EditableImage image;

    /**
     * <p>
     * The zoom-level of the current view.
     * A scale of 1.0 represents actual size; 0.5 is zoomed out to half size; 1.5 is zoomed in to one-and-a-half size; and so forth.
     * </p>
     * 
     * <p>
     * Note that the scale is internally represented as a multiplier, but externally as a percentage.
     * </p>
     */
    private double scale;

    /**
     * <p>
     * Create a new ImagePanel.
     * </p>
     * 
     * <p>
     * Newly created ImagePanels have a default zoom level of 100%
     * ImagePanels are also set up to accept dropped files, and will attempt to open them as images. 
     * </p>
     */
    public ImagePanel() {
        image = new EditableImage();
        scale = 1.0;

        //Open dropped image files
        setDropTarget(new DropTarget() {
            public void drop(DropTargetDropEvent evt) {
                if (getImage().hasImage()) return; //Don't allow dropping if an image is already open
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    if (!evt.getTransferable().isDataFlavorSupported(DataFlavor.javaFileListFlavor)) 
                        return;

                    List<?> list = (List<?>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    String filepath = ((File)list.get(0)).getCanonicalPath();
                    attemptImageOpen(filepath);

                    evt.getDropTargetContext().dropComplete(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error opening file");
                }
            }
        });

    }

    public void attemptImageOpen(String filepath) {
        try {
            getImage().open(filepath);
            resetZoom();
            repaint();
            getParent().revalidate();
        } catch (IOException err) {
            JOptionPane.showMessageDialog(null, "Error Opening image. Please choose a valid image file.");
        }
    }

    /**
     * <p>
     * Get the currently displayed image
     * </p>
     *
     * @return the image currently displayed.
     */
    public EditableImage getImage() {
        return image;
    }

    /**
     * <p>
     * Get the current zoom level as a percentage.
     * </p>
     * 
     * <p>
     * The percentage zoom is used for the external interface, where 100% is the original size, 50% is half-size, etc. 
     * </p>
     * @return The current zoom level as a percentage.
     */
    public double getZoom() {
        return 100*scale;
    }

    /**
     * <p>
     * Set the current zoom level as a percentage.
     * </p>
     * 
     * <p>
     * The percentage zoom is used for the external interface, where 100% is the original size, 50% is half-size, etc. 
     * The zoom level is restricted to the range [50, 200].
     * </p>
     * @param zoomPercent The new zoom level as a percentage.
     */
    public void setZoom(double zoomPercent) {
        if (zoomPercent < 10) {
            zoomPercent = 50;
        }
        if (zoomPercent > 500) {
            zoomPercent = 200;
        }
        scale = zoomPercent / 100;
    }

    public void resetZoom() {
        int imageWidth = image.getCurrentImage().getWidth();
        int imageHeight = image.getCurrentImage().getHeight();
        int panelWidth = (int)this.getParent().getWidth();
        int panelHeight = (int)this.getParent().getHeight();
        double scaleWidth = (double) panelWidth / imageWidth;
        double scaleHeight = (double) panelHeight / imageHeight;
        double newScale = Math.min(scaleWidth, scaleHeight);
        scale = newScale;
        repaint();
    }


    /**
     * <p>
     * Gets the preferred size of this component for UI layout.
     * </p>
     * 
     * <p>
     * The preferred size is the size of the image (scaled by zoom level), or a default size if no image is present.
     * </p>
     * 
     * @return The preferred size of this component.
     */
    @Override
    public Dimension getPreferredSize() {
        if (image.hasImage()) {
            return new Dimension((int) Math.round(image.getCurrentImage().getWidth()*scale), 
                                 (int) Math.round(image.getCurrentImage().getHeight()*scale));
        } else {
            return new Dimension(600, 450);
        }
    }

    /**
     * <p>
     * (Re)draw the component in the GUI.
     * </p>
     * 
     * @param g The Graphics component to draw the image on.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2  = (Graphics2D) g.create();
        if (image.hasImage()) {
            g2.scale(scale, scale);
            g2.drawImage(image.getCurrentImage(), null, 0, 0);
        } else {
            drawWelcomeImage(g2);
        }
        g2.dispose();

    }

    private void drawWelcomeImage(Graphics2D g) {
            String message = "Welcome to ANDIE";
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.setColor(Color.GRAY);
            int messageWidth = g.getFontMetrics().stringWidth(message);
            g.drawString(message, (int) Math.round((this.getWidth() - messageWidth) / 2), (int) Math.round(this.getHeight() / 2));
            String subMessage = "Drag an image in to get started";
            g.setFont(new Font("Arial", Font.PLAIN, 15));
            int subMessageWidth = g.getFontMetrics().stringWidth(subMessage);
            int subMessageHeight = g.getFontMetrics().getHeight();
            g.drawString(subMessage, (int) Math.round((this.getWidth() - subMessageWidth) / 2), (int) Math.round(this.getHeight() / 2 + subMessageHeight + 15));
    }

}
