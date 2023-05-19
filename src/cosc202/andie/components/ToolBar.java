package cosc202.andie.components;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

public class ToolBar extends JFrame{

    JToolBar button = new JToolBar("Button" + 10);

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
    JButton cropButton = new JButton(controller.actions.macroActions.new RecordMacroAction("Crop", crop2 , "Crop", null));
    button.add(cropButton);
    cropButton.addActionListener((e) -> System.out.println("Crop"));
    cropButton.addActionListener((e) -> );



    ImageIcon rotate = new ImageIcon(Andie.class.getClassLoader().getResource("acRotate.png"));
    Image rotateimg = rotate.getImage();
    Image newrotateimg = rotateimg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    ImageIcon rotate2 = new ImageIcon(newrotateimg);    
    JButton rotateButton = new JButton(rotate2);
    button.add(rotateButton);
    rotateButton.addActionListener((e) -> System.out.println("Anti Clockwise Rotate"));

    ImageIcon crotate = new ImageIcon(Andie.class.getClassLoader().getResource("cRotate.png"));
    Image crotateimg = crotate.getImage();
    Image cnewrotateimg = crotateimg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
    ImageIcon crotate2 = new ImageIcon(cnewrotateimg);    
    JButton crotateButton = new JButton(crotate2);
    button.add(crotateButton);
    crotateButton.addActionListener((e) -> System.out.println("Clockwise Rotate"));


    frame.add(button, BorderLayout.SOUTH);

    }
}