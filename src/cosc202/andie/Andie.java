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
 * It also creates the {@link AndieModel} and {@link AndieController}, used to manage the program.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @see AndieView
 * @see AndieModel
 * @see AndieController
 * @see LanguageConfig
 * 
 * @author Jeb Nicholson
 * @version 3.0
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
     * <p>
     * Initializes the {@link LanguageConfig}, {@link AndieModel}, {@link AndieController}, and then calls {@link #launchView()}.
     * </p>
     * 
     * <p>
     * Listens for language changes, which trigger a restart of the view, so that components can update their text
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
                controller.actions.init();
                launchView();
            });

        });

    }

    /**
     * Launch the view
     */
    private static void launchView() {
        view = new AndieView(controller,model);
        view.createAndieView();
    }

}
