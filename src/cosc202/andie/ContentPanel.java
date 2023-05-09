package cosc202.andie;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.*;

import cosc202.andie.components.PanView;
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
    private ImagePanel imagePanel;
    private PanView panView;

    public ContentPanel(AndieController controller, AndieModel model) {
        super();
        this.setLayout(new BorderLayout());

        ModelListener isl = () -> {
            if (model.hasImage()) {
                removeAll();

                imagePanel = new ImagePanel(controller, model);
                panView = new PanView(imagePanel, model.getImage().getSize());

                controller.registerZoomListener(panView);
                add(panView, BorderLayout.CENTER);
                panView.resetView();
            } else {
                removeAll();
                panView = null;
                ContentPanel.this.add(new WelcomeBlank(), BorderLayout.CENTER);
            }
            this.revalidate();
        };

        ModelListener il = () -> {
            if (model.hasImage() && panView != null) {
                panView.setContentSize(model.getImage().getSize());
            } 
        };

        model.registerImageStatusListener(isl);
        model.registerImageListener(il);
        isl.update();
        il.update();


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
