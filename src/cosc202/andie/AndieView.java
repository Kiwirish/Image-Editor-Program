package cosc202.andie;

import java.awt.BorderLayout;
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
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;
import cosc202.andie.models.AndieModel.ModelListener;

public class AndieView {
	private AndieController controller;
	private AndieModel model;
	private JFrame frame;
	private ModelListener filepathListener;

	public AndieView(AndieController controller, AndieModel model) {
		this.controller = controller;
		this.model = model;
	}

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
					Image iconImage = ImageIO.read(Andie.class.getClassLoader().getResource("icon.png"));
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
		
		


				// ----------- Macros Panel -------------
				MacrosPanel macrosPanel = new MacrosPanel(controller, model);
				frame.add(macrosPanel, BorderLayout.EAST);
				// ----------- End Macros Panel -------------

		JToolBar button = new JToolBar("Button" + 10);

		ImageIcon icon = new ImageIcon(Andie.class.getClassLoader().getResource("Exit.png"));
		Image img = icon.getImage();
        Image newimg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon icon2 = new ImageIcon(newimg);    
        JButton exitButton = new JButton(icon2);
        button.add(exitButton);
		exitButton.addActionListener((e) -> controller.closeWindow());

		ImageIcon crop = new ImageIcon(Andie.class.getClassLoader().getResource("Crop.png"));
		Image img2 = crop.getImage();
        Image newimg2 = img2.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon crop2 = new ImageIcon(newimg2);    
        JButton cropButton = new JButton(controller.actions.transformActions.cropAction);
				cropButton.setText(null);
				cropButton.setIcon(crop2);
        button.add(cropButton);
		cropButton.addActionListener((e) -> System.out.println("Crop"));
		// cropButton.addActionListener((e) -> );

		ImageIcon select = new ImageIcon(Andie.class.getClassLoader().getResource("Select.png"));
		Image selectimg = select.getImage();
        Image selectnewimg = selectimg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon select2 = new ImageIcon(selectnewimg);    
        JButton selectButton = new JButton(controller.actions.toolActions.selectToolAction);
				selectButton.setText(null);
				selectButton.setIcon(select2);
        button.add(selectButton);
		selectButton.addActionListener((e) -> System.out.println("Select"));

		ImageIcon rotate = new ImageIcon(Andie.class.getClassLoader().getResource("acRotate.png"));
		Image rotateimg = rotate.getImage();
        Image newrotateimg = rotateimg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon rotate2 = new ImageIcon(newrotateimg);    
        JButton rotateButton = new JButton(controller.actions.transformActions.rotateLeftAction);
				rotateButton.setText(null);
				rotateButton.setIcon(rotate2);
        button.add(rotateButton);
		rotateButton.addActionListener((e) -> System.out.println("Anti Clockwise Rotate"));

		ImageIcon crotate = new ImageIcon(Andie.class.getClassLoader().getResource("cRotate.png"));
		Image crotateimg = crotate.getImage();
        Image cnewrotateimg = crotateimg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon crotate2 = new ImageIcon(cnewrotateimg);    
        JButton crotateButton = new JButton(controller.actions.transformActions.rotateRightAction);
				crotateButton.setText(null);
				crotateButton.setIcon(crotate2);
        button.add(crotateButton);
		crotateButton.addActionListener((e) -> System.out.println("Clockwise Rotate"));

		ImageIcon Undo = new ImageIcon(Andie.class.getClassLoader().getResource("undo.png"));
		Image Undoimg = Undo.getImage();
        Image Undonewimg = Undoimg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon Undo2 = new ImageIcon(Undonewimg);    
        JButton UndoButton = new JButton(controller.actions.editActions.undoAction);
				UndoButton.setText(null);
				UndoButton.setIcon(Undo2);
        button.add(UndoButton);
		selectButton.addActionListener((e) -> System.out.println("Undo"));


		ImageIcon Redo = new ImageIcon(Andie.class.getClassLoader().getResource("redo.png"));
		Image Redoimg = Redo.getImage();
        Image Redonewimg = Redoimg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon Redo2 = new ImageIcon(Redonewimg);    
        JButton RedoButton = new JButton(controller.actions.editActions.redoAction);
				RedoButton.setText(null);
				RedoButton.setIcon(Redo2);
        button.add(RedoButton);
		selectButton.addActionListener((e) -> System.out.println("Redo"));

		frame.add(button, BorderLayout.SOUTH);

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
	public void closeView() {
		frame.dispose();
	}	
}
