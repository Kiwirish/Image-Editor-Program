package cosc202.andie;

import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;

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
 * @author Jeb Nicholson
 * @version 2.0
 *
 * 
 */
public class Andie {

    private static AndieModel model;
    private static AndieController controller;
    private static AndieView view;

    /**
     * <p>
     * Main entry point to the ANDIE program.
     * </p>
     * 
     * @param args Command line arguments, not currently used
     * @throws Exception If something goes awry
     * @see #launchView()
     */
    public static void main(String[] args) throws Exception {
        LanguageConfig.init();
        javax.swing.SwingUtilities.invokeLater(()->{

            model = new AndieModel();
            controller = new AndieController(model);

            launchView();

            LanguageConfig.registerLanguageListener(()->{
                view.closeView();
                launchView();
            });

        });

    }

    private static void launchView() {
        view = new AndieView(controller,model);
        view.createAndieView();
    }

}
