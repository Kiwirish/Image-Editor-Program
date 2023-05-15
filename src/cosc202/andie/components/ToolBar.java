package cosc202.andie.components;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

public class ToolBar extends JFrame{

        public ToolBar(){
                initUI();
        }

        private void initUI(){
            //createMenuBar();
            createToolBar();

            setTitle("1st ed toolBar");
            setSize(300, 200);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }

        private void createToolBar(){
            JToolBar toolBar = new JToolBar();
            ImageIcon icon = new ImageIcon(ToolBar.class.getClassLoader().getResource("Resources/Sign-check-icon.png"));

            Image img = icon.getImage();
            Image newimg = img.getScaledInstance(30, 30, DO_NOTHING_ON_CLOSE);
            ImageIcon icon2 = new ImageIcon(newimg);
            
            JButton exitButton = new JButton(icon2);
            toolBar.add(exitButton);

            exitButton.addActionListener((e) -> System.exit(0));
            add(toolBar, BorderLayout.NORTH);

        }

        public static void main(String[] args){
                EventQueue.invokeLater(() -> {
                    ToolBar ex = new ToolBar();
                    ex.setVisible(true);
                });
        }

}