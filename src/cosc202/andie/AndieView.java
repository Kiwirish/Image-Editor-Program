package cosc202.andie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.colorchooser.AbstractColorChooserPanel;

import cosc202.andie.components.OptionPopup;
import cosc202.andie.components.PopupSlider;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.AndieModel.ModelListener;

/**
 * <p>
 * View class for the ANDIE program.
 * </p>
 * 
 * <p>
 * Handles the creation of the Swing GUI for the program, including the main JFrame (window).
 * </p>
 * 
 * <p>
 * Create Jtoolbar and adds button and add functions to buttons
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @see AndieModel
 * @see AndieController
 * @see ContentPanel
 * @see MenuBar
 * 
 * @author Jeb Nicholson
 * @author Bernard Pieters
 * @version 1.0
 */
public class AndieView {
	private AndieController controller;
	private AndieModel model;
	private JFrame frame;
	private ModelListener filepathListener;

	/**
	 * Creates a new AndieView object.
	 * @param controller The base controller for the program.
	 * @param model The base model for the program.
	 */
	public AndieView(AndieController controller, AndieModel model) {
		this.controller = controller;
		this.model = model;
	}

	/**
	 * Creates the main JFrame for the program, and adds all of the necessary components.
	 */
	public void createAndieView() {
        frame = new JFrame("ANDIE");
				Dimension frameSize = model.getFrameSize();
        if (frameSize == null) {
					frameSize = new Dimension(700, 550);
					model.setFrameSize(frameSize);
				}
        frame.setPreferredSize(frameSize);
				//Listen for window resize events
				frame.addComponentListener(new ComponentAdapter() {
					public void componentResized(ComponentEvent evt) {
						Dimension newSize = frame.getSize();
						model.setFrameSize(newSize);
					}
				});

				try {
					Image iconImage = ImageIO.read(Andie.class.getClassLoader().getResource("assets/icon.png"));
					frame.setIconImage(iconImage);
				} catch (IOException e) {}

				filepathListener = ()->{
					String filepath = model.getImageFilepath();
					if (filepath == null) {
						frame.setTitle("ANDIE");
					} else {
						frame.setTitle("ANDIE | " + Paths.get(filepath).getFileName().toString());
					}
				};
				model.registerFilepathListener(filepathListener);
				filepathListener.update();

        JPanel contentPane = new JPanel();
				
        frame.setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        // The main content area is an ImagePanel
				ContentPanel imagePanel = new ContentPanel(controller, model);

        contentPane.add(imagePanel, BorderLayout.CENTER);

        // Add in menus for various types of action the user may perform.
        MenuBar menuBar = new MenuBar(controller, model);
        frame.setJMenuBar(menuBar);
		
		
				// ----------- Macros Panel -----------------
				MacrosPanel macrosPanel = new MacrosPanel(controller, model);
				frame.add(macrosPanel, BorderLayout.EAST);
				// ----------- End Macros Panel -------------


		JToolBar toolbar = new JToolBar("Button" + 10);
		toolbar.setOrientation(JToolBar.VERTICAL);

		// ImageIcon icon = new ImageIcon(Andie.class.getClassLoader().getResource("assets/Exit.png"));
		// Image img = icon.getImage();
    //     Image newimg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    //     ImageIcon icon2 = new ImageIcon(newimg);    
    //     JButton exitButton = new JButton(icon2);
		// exitButton.addActionListener((e) -> controller.closeWindow());

		ImageIcon crop = new ImageIcon(Andie.class.getClassLoader().getResource("assets/crop.png"));
		Image img2 = crop.getImage();
		Image newimg2 = img2.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		ImageIcon crop2 = new ImageIcon(newimg2);    
		JButton cropButton = new JButton(controller.actions.transformActions.cropAction);
		cropButton.setText(null);
		cropButton.setIcon(crop2);
		cropButton.addActionListener((e) -> System.out.println("Crop"));

		ImageIcon select = new ImageIcon(Andie.class.getClassLoader().getResource("assets/Select.png"));
		Image selectimg = select.getImage();
		Image selectnewimg = selectimg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		ImageIcon select2 = new ImageIcon(selectnewimg);    
		JButton selectButton = new JButton(controller.actions.toolActions.selectToolAction);
		selectButton.setText(null);
		selectButton.setIcon(select2);
		selectButton.addActionListener((e) -> System.out.println("Select"));

		ImageIcon Line = new ImageIcon(Andie.class.getClassLoader().getResource("assets/line.png"));
		Image Lineimg = Line.getImage();
		Image Linenewimg = Lineimg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		ImageIcon Line2 = new ImageIcon(Linenewimg);    
		JButton LineButton = new JButton(controller.actions.toolActions.lineToolAction);
		LineButton.setText(null);
		LineButton.setIcon(Line2);

		LineButton.addActionListener((e) -> System.out.println("Line"));

		ImageIcon Rect = new ImageIcon(Andie.class.getClassLoader().getResource("assets/rect.png"));
		Image Rectimg = Rect.getImage();
		Image Rectnewimg = Rectimg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		ImageIcon Rect2 = new ImageIcon(Rectnewimg);    
		JButton RectButton = new JButton(controller.actions.toolActions.rectangleToolAction);
		RectButton.setText(null);
		RectButton.setIcon(Rect2);

		RectButton.addActionListener((e) -> System.out.println("Rectangle"));

		ImageIcon oval = new ImageIcon(Andie.class.getClassLoader().getResource("assets/oval.png"));
		Image ovalimg = oval.getImage();
		Image ovalnewimg = ovalimg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		ImageIcon oval2 = new ImageIcon(ovalnewimg);    
		JButton ovalButton = new JButton(controller.actions.toolActions.rectangleToolAction);
		ovalButton.setText(null);
		ovalButton.setIcon(oval2);

		ovalButton.addActionListener((e) -> System.out.println("oval"));

		ImageIcon rotate = new ImageIcon(Andie.class.getClassLoader().getResource("assets/acRotate.png"));
		Image rotateimg = rotate.getImage();
		Image newrotateimg = rotateimg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		ImageIcon rotate2 = new ImageIcon(newrotateimg);    
		JButton rotateButton = new JButton(controller.actions.transformActions.rotateLeftAction);
		rotateButton.setText(null);
		rotateButton.setIcon(rotate2);

		rotateButton.addActionListener((e) -> System.out.println("Anti Clockwise Rotate"));

		ImageIcon crotate = new ImageIcon(Andie.class.getClassLoader().getResource("assets/cRotate.png"));
		Image crotateimg = crotate.getImage();
		Image cnewrotateimg = crotateimg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		ImageIcon crotate2 = new ImageIcon(cnewrotateimg);    
		JButton crotateButton = new JButton(controller.actions.transformActions.rotateRightAction);
		crotateButton.setText(null);
		crotateButton.setIcon(crotate2);

		crotateButton.addActionListener((e) -> System.out.println("Clockwise Rotate"));

		ImageIcon Undo = new ImageIcon(Andie.class.getClassLoader().getResource("assets/undo.png"));
		Image Undoimg = Undo.getImage();
		Image Undonewimg = Undoimg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		ImageIcon Undo2 = new ImageIcon(Undonewimg);    
		JButton UndoButton = new JButton(controller.actions.editActions.undoAction);
		UndoButton.setText(null);
		UndoButton.setIcon(Undo2);

		selectButton.addActionListener((e) -> System.out.println("Undo"));


		ImageIcon Redo = new ImageIcon(Andie.class.getClassLoader().getResource("assets/redo.png"));
		Image Redoimg = Redo.getImage();
        Image Redonewimg = Redoimg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon Redo2 = new ImageIcon(Redonewimg);    
        JButton RedoButton = new JButton(controller.actions.editActions.redoAction);
				RedoButton.setText(null);
				RedoButton.setIcon(Redo2);
        
		selectButton.addActionListener((e) -> System.out.println("Redo"));

		ImageIcon Colour = new ImageIcon(Andie.class.getClassLoader().getResource("assets/Cwheel.png"));
		Image Colourimg = Colour.getImage();
        Image Colournewimg = Colourimg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon Colour2 = new ImageIcon(Colournewimg);    
        JButton ColourButton = new JButton();
				ColourButton.setText(null);
				ColourButton.setIcon(Colour2);
        
		ColourButton.addActionListener((e) -> System.out.println("Colour wheel"));
		ColourButton.addActionListener((e) -> colourPicker());
		frame.add(toolbar, BorderLayout.SOUTH);

		ImageIcon FillColor = new ImageIcon(Andie.class.getClassLoader().getResource("assets/fill.png"));
		Image FillColorimg = FillColor.getImage();
        Image FillColornewimg = FillColorimg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon FillColor2 = new ImageIcon(FillColornewimg);    
        JButton FillColorButton = new JButton();
				FillColorButton.setText(null);
				FillColorButton.setIcon(FillColor2);
        
		FillColorButton.addActionListener((e) -> System.out.println("fill colour wheel"));
		FillColorButton.addActionListener((e) -> fillPicker());
		frame.add(toolbar, BorderLayout.WEST);

		ImageIcon StrokeWidth = new ImageIcon(Andie.class.getClassLoader().getResource("assets/BWidth.png"));
		Image StrokeWidthimg = StrokeWidth.getImage();
		Image StrokeWidthnewimg = StrokeWidthimg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon StrokeWidth2 = new ImageIcon(StrokeWidthnewimg);    
        JButton StrokeWidthButton = new JButton();
		StrokeWidthButton.setText(null);
		StrokeWidthButton.setIcon(StrokeWidth2);
        
		StrokeWidthButton.addActionListener((e) -> System.out.println("Set Brush Width"));
		StrokeWidthButton.addActionListener((e) -> widthPicker());
		frame.add(toolbar, BorderLayout.WEST);
		

		/* adding the buttons to toolbar  */
		// button.add(exitButton);
		toolbar.add(selectButton);
		toolbar.add(UndoButton);
		toolbar.add(RedoButton);
		toolbar.add(StrokeWidthButton);
		toolbar.add(FillColorButton);
		//button.add(ColourButton);
		//button.add(rotateButton);
		toolbar.add(crotateButton);
		toolbar.add(ovalButton);
		toolbar.add(RectButton);
		toolbar.add(LineButton);
		toolbar.add(cropButton);

        frame.pack();

				Point framePosition = model.getFrameLocation();
        if (framePosition == null) {
					frame.setLocationRelativeTo(null);
					model.setFrameLocation(frame.getLocation());
				} else {
					frame.setLocation(framePosition);
				}
				//Listen for window move events
				frame.addComponentListener(new ComponentAdapter() {
					public void componentMoved(ComponentEvent evt) {
						Point newPosition = frame.getLocation();
						model.setFrameLocation(newPosition);
					}
				});

        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

				controller.setContentPane(contentPane);
        WindowListener exitListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
							controller.closeWindow();
            }
        };
        frame.addWindowListener(exitListener);
	}
	public void widthPicker() {
		
			PopupSlider slider = new PopupSlider("Stroke Width label", 1, 100, 5, "px", 10, 50, 1);
			OptionPopup popup = new OptionPopup(frame.getContentPane(),"Resize_Popup_Title", new PopupSlider[] { slider });
			

			if(popup.show() == OptionPopup.OK){model.tool.setStrokeWidth(slider.getValue());};
			

			
		}
	

	public void fillPicker() {

		Color initialcolor = Color.RED;
		JColorChooser jC = new JColorChooser(initialcolor);
		jC.setPreviewPanel(new JPanel());
		for(AbstractColorChooserPanel panel : jC.getChooserPanels()){
			if(!panel.getDisplayName().equals("HSL")){
					jC.removeChooserPanel(panel);
			}
		}
		JDialog dialog = JColorChooser.createDialog(this.frame,"Choose Fill color", true, jC, (ofdk)->{ model.tool.setFillColor(jC.getColor());}, null);
		dialog.setVisible(true);
		

	}

	public void colourPicker () {
		

		Color initialcolor = Color.RED;
		JColorChooser jC = new JColorChooser(initialcolor);
		jC.setPreviewPanel(new JPanel());
		for(AbstractColorChooserPanel panel : jC.getChooserPanels()){
			if(!panel.getDisplayName().equals("HSL")){
					jC.removeChooserPanel(panel);
			}
		}
		JDialog dialog = JColorChooser.createDialog(this.frame,"Choose stroke color", true, jC, (ofdk)->{ model.tool.setStrokeColor(jC.getColor());}, null);
		dialog.setVisible(true);
	}

	public void closeView() {
		frame.dispose();
	}	
}
