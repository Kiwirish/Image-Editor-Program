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
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import cosc202.andie.actions.TransformActions.CropAction;
import cosc202.andie.controllers.AndieController;
import cosc202.andie.models.AndieModel;

public class AndieView {
	private AndieController controller;
	private AndieModel model;
	private JFrame frame;

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

        JPanel contentPane = new JPanel();
        frame.setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        // The main content area is an ImagePanel
				ContentPanel imagePanel = new ContentPanel(controller, model);

        contentPane.add(imagePanel, BorderLayout.CENTER);

        // Add in menus for various types of action the user may perform.
        MenuBar menuBar = new MenuBar(controller, model);
        frame.setJMenuBar(menuBar);
		
		JToolBar button = new JToolBar("Button" + 10);


				// ----------- Macros Panel -------------
				MacrosPanel macrosPanel = new MacrosPanel(controller, model);
				frame.add(macrosPanel, BorderLayout.EAST);
				// ----------- End Macros Panel -------------

		ImageIcon icon = new ImageIcon(Andie.class.getClassLoader().getResource("Exit.png"));
		Image img = icon.getImage();
        Image newimg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon icon2 = new ImageIcon(newimg);    
        JButton exitButton = new JButton(icon2);
        button.add(exitButton);
		exitButton.addActionListener((e) -> System.exit(0));

		ImageIcon crop = new ImageIcon(Andie.class.getClassLoader().getResource("Crop.png"));
		Image img2 = crop.getImage();
        Image newimg2 = img2.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon crop2 = new ImageIcon(newimg2);    
        JButton cropButton = new JButton(crop2);
        button.add(cropButton);
		//cropButton.addActionListener(Crop);


		ImageIcon rotate = new ImageIcon(Andie.class.getClassLoader().getResource("acRotate.png"));
		Image rotateimg = rotate.getImage();
        Image newrotateimg = rotateimg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon rotate2 = new ImageIcon(newrotateimg);    
        JButton rotateButton = new JButton(rotate2);
        button.add(rotateButton);

		ImageIcon crotate = new ImageIcon(Andie.class.getClassLoader().getResource("cRotate.png"));
		Image crotateimg = crotate.getImage();
        Image cnewrotateimg = crotateimg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon crotate2 = new ImageIcon(cnewrotateimg);    
        JButton crotateButton = new JButton(crotate2);
        button.add(crotateButton);

		frame.add(button, BorderLayout.WEST);

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
