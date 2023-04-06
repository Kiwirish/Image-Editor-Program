package cosc202.andie;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.*;
import static cosc202.andie.LanguageConfig.msg;

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
 * @author Jeb Nicholson
 * @version 2.0
 */
public class ImagePanel extends JPanel implements MouseWheelListener {
    
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

        addMouseWheelListener(this);

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
                    JOptionPane.showMessageDialog(null,msg("Generic_File_Open_Error"));
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
            JOptionPane.showMessageDialog(null,msg("Invalid_Image_Error_Message"));
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
            zoomPercent = 10;
        }
        if (zoomPercent > 500) {
            zoomPercent = 500;
        }
        scale = zoomPercent / 100;
        repaint();
        revalidate();
    }

    public void resetZoom() {
        if (!image.hasImage()) {
            setZoom(100);
            return;
        };
        int imageWidth = image.getCurrentImage().getWidth();
        int imageHeight = image.getCurrentImage().getHeight();
        int panelWidth = (int)this.getParent().getWidth();
        int panelHeight = (int)this.getParent().getHeight();
        double scaleWidth = (double) panelWidth / imageWidth;
        double scaleHeight = (double) panelHeight / imageHeight;
        double newScale = Math.min(scaleWidth, scaleHeight);
        double newZoom = newScale * 100;
        setZoom(newZoom);
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
        Dimension containerSize = getParent().getSize();
        if (!image.hasImage()) return containerSize;
        Dimension imageSize = new Dimension((int)Math.round(image.getCurrentImage().getWidth()*scale), (int)Math.round(image.getCurrentImage().getHeight()*scale));

        // Preferred size should take the largest width and height of the image and the container
        int width = Math.max(containerSize.width, imageSize.width);
        int height = Math.max(containerSize.height, imageSize.height);
        return new Dimension(width, height);
        // }
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
        g2.setColor(new Color(0x1a1b1c));
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());

        if (image.hasImage()) {
            drawCurrentImage(g2);
        } else {
            drawNothingOpenImage(g2);
        }

        g2.dispose();
    }

    private void drawNothingOpenImage(Graphics2D g) {
        String message = msg("Andie_Welcome_Message");
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.GRAY);
        int messageWidth = g.getFontMetrics().stringWidth(message);
        g.drawString(message, (int) Math.round((this.getWidth() - messageWidth) / 2), (int) Math.round(this.getHeight() / 2));
        String subMessage = msg("Andie_Welcome_Submessage");
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        int subMessageWidth = g.getFontMetrics().stringWidth(subMessage);
        int subMessageHeight = g.getFontMetrics().getHeight();
        g.drawString(subMessage, (int) Math.round((this.getWidth() - subMessageWidth) / 2), (int) Math.round(this.getHeight() / 2 + subMessageHeight + 15));
    }
    private void drawCurrentImage(Graphics2D g) {
        AffineTransform oldTransform = g.getTransform();
        g.scale(scale, scale);
        //reset g transform to 1,1  
        //Draw the image centered in the panel
        double imageWidth = image.getCurrentImage().getWidth();
        double imageHeight = image.getCurrentImage().getHeight();
        double drawableWidth = this.getWidth()/scale;
        double drawableHeight = this.getHeight()/scale;
        int x = (int) Math.round((drawableWidth - imageWidth) / 2);
        int y = (int) Math.round((drawableHeight - imageHeight) / 2);

        g.drawImage(image.getCurrentImage(), null, x, y);

        g.setTransform(oldTransform);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        if (isWindows) {
            if (!e.isControlDown()) {getParent().dispatchEvent(e); return;}
        } else {
            if (!e.isMetaDown()) {getParent().dispatchEvent(e); return;}
        }
        double rotation = e.getPreciseWheelRotation();
        double currentLog = Math.log(getZoom()/100);
        currentLog += rotation*0.02;
        double newZoom = Math.exp(currentLog)*100;
        setZoom(newZoom);
    }
}
