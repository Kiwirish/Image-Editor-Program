package cosc202.andie;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.*;

import cosc202.andie.components.ImagePanView;
import cosc202.andie.components.WelcomeBlank;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.EditableImage;
import cosc202.andie.models.AndieModel.ModelListener;

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
public class ContentPanel extends JPanel {
    
    /**
     * The image to display in the ImagePanel.
     */
    private ImagePanView ipv;

    public ContentPanel(AndieController controller, AndieModel model) {
        super();
        this.setLayout(new BorderLayout());

        ipv = null;

        ModelListener isl = () -> {
            if (model.hasImage() && ipv == null) {
                removeAll();
                ipv = new ImagePanView(model.getWorkingImage());
                controller.registerZoomListener(ipv);
                add(ipv, BorderLayout.CENTER);
                ipv.addMouseMotionListener(new MouseMotionListener() {
                    public void mouseDragged(MouseEvent e) { model.mouse.mouseDragged(ipv.convertPoint(e.getPoint()),e); }
                    public void mouseMoved(MouseEvent e) { model.mouse.mouseMoved(ipv.convertPoint(e.getPoint()),e); }
                });
                ipv.addMouseListener(new MouseListener() {
                    public void mouseClicked(MouseEvent e) { model.mouse.mouseClicked(ipv.convertPoint(e.getPoint()),e); }
                    public void mousePressed(MouseEvent e) { model.mouse.mouseDown(ipv.convertPoint(e.getPoint()),e); }
                    public void mouseReleased(MouseEvent e) { model.mouse.mouseUp(ipv.convertPoint(e.getPoint()),e); }
                    public void mouseEntered(MouseEvent e) { }
                    public void mouseExited(MouseEvent e) { }
                });

            } else {
                removeAll();
                ipv = null;
                this.add(new WelcomeBlank(), BorderLayout.CENTER);
            }
            this.revalidate();
        };

        ModelListener wil = () -> {
            if (model.hasImage() && ipv != null) {
                ipv.updateImage(model.getWorkingImage());
            } 
        };

        model.registerImageStatusListener(isl);
        model.registerWorkingImageListener(wil);
        isl.update();

        //Open dropped image files
        setDropTarget(new DropTarget() {
            public void drop(DropTargetDropEvent evt) {
                if (model.hasImage()) return; //Don't allow dropping if an image is already open
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    if (!evt.getTransferable().isDataFlavorSupported(DataFlavor.javaFileListFlavor)) 
                        return;
                    List<?> list = (List<?>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    String filepath = ((File)list.get(0)).getAbsolutePath();
                    controller.IO.dropOpen(filepath);

                    evt.getDropTargetContext().dropComplete(true);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,msg("Generic_File_Open_Error"));
                } catch (UnsupportedFlavorException e) {
                    JOptionPane.showMessageDialog(null,msg("Generic_File_Open_Error"));
                }
            }
        });
    }
}
