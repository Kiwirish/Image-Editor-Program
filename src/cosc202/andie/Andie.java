package cosc202.andie;
// blakes comment 
// blakes second change 
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;
//import javax.swing.border.LineBorder;
import javax.swing.text.Position;

import cosc202.andie.actions.ColourActions;
import cosc202.andie.actions.EditActions;
import cosc202.andie.actions.FileActions;
import cosc202.andie.actions.FilterActions;
import cosc202.andie.actions.SizeActions;
import cosc202.andie.actions.ViewActions;
import cosc202.andie.actions.LanguageActions;

import javax.imageio.*;

/**
 * <p>
 * Main class for A Non-Destructive Image Editor (ANDIE).
 * </p>
 * 
 * <p>
 * This class is the entry point for the program.
 * It creates a Graphical User Interface (GUI) that provides access to various image editing and processing operations.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 *
 * 
 */
public class Andie {

    private static ImagePanel imagePanel;
    private static JFrame frame;

    /**
     * 
     * <p>
     * This method sets up an interface consisting of an active image (an {@code ImagePanel})
     * and various menus which can be used to trigger operations to load, save, edit, etc. 
     * These operations are implemented {@link ImageOperation}s and triggered via 
     * {@code ImageAction}s grouped by their general purpose into menus.
     * </p>
     * 
     * @see ImagePanel
     * @see ImageAction
     * @see ImageOperation
     * @see FileActions
     * @see EditActions
     * @see ViewActions
     * @see FilterActions
     * @see ColourActions
     * 
     * @param size {@code Dimension} specifying the size of the window to be created. Defaults to 700x550 if null.
     * @param position {@code Point} specifying the position of the window to be created. Defaults to the centre of the screen if null.
     * 
     * @throws Exception if something goes wrong.
     */
    private static void setup(Dimension size, Point position) throws Exception {

        frame = new JFrame("ANDIE");
        if (size == null) size = new Dimension(700, 550);
        frame.setPreferredSize(size);
        Image image = ImageIO.read(Andie.class.getClassLoader().getResource("icon.png"));
        frame.setIconImage(image);

        JPanel contentPane = new JPanel();
        frame.setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        // The main content area is an ImagePanel
        // Inside a scroll pane
        JScrollPane scrollPane = new JScrollPane(imagePanel); 
        scrollPane.setBorder(null);

        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Add in menus for various types of action the user may perform.
        JMenuBar menuBar = new JMenuBar();

        // File menus are pretty standard, so things that usually go in File menus go here.
        FileActions fileActions = new FileActions();
        menuBar.add(fileActions.createMenu());
        // Likewise Edit menus are very common, so should be clear what might go here.
        EditActions editActions = new EditActions();
        menuBar.add(editActions.createMenu());
        // View actions control how the image is displayed, but do not alter its actual content
        ViewActions viewActions = new ViewActions();
        menuBar.add(viewActions.createMenu());
        // Size actions transform the image 
        SizeActions sizeActions = new SizeActions();
        menuBar.add(sizeActions.createMenu());
        // Filters apply a per-pixel operation to the image, generally based on a local window
        FilterActions filterActions = new FilterActions();
        menuBar.add(filterActions.createMenu());
        // Actions that affect the representation of colour in the image
        ColourActions colourActions = new ColourActions();
        menuBar.add(colourActions.createMenu());
        //Actions that change the language of the code
        LanguageActions languageActions = new LanguageActions();
        menuBar.add(languageActions.createMenu());
        
        frame.setJMenuBar(menuBar);
        frame.pack();
        if (position == null) frame.setLocationRelativeTo(null);
        else frame.setLocation(position);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        WindowListener exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fileActions.exitAction.exit();
            }
        };
        frame.addWindowListener(exitListener);
    }


    /**
     * <p>
     * Initialises variables and launches Andie.
     * </p>
     */
    public static void launchAndie() {
        LanguageConfig.init();
        imagePanel = new ImagePanel();
        ImageAction.setTarget(imagePanel);

        try {
            setup(null,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * Disposes of the current frame, and runs setup again, preserving the size and position of the frame.
     * </p>
     */
    public static void relaunchAndie() {
        Dimension size = frame.getSize();
        Point position = frame.getLocation();
        frame.dispose();

        try {
            setup(size,position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * Main entry point to the ANDIE program.
     * </p>
     * 
     * <p>
     * Launches Andie in a separate thread.
     * As a result, this is essentially a wrapper around {@code launchAndie()}.
     * </p>
     * 
     * @param args Command line arguments, not currently used
     * @throws Exception If something goes awry
     * @see #createAndShowGUI()
     */
    public static void main(String[] args) throws Exception {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                launchAndie();
            }
        });
    }

}
