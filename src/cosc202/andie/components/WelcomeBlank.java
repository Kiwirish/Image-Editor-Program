package cosc202.andie.components;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;
import static cosc202.andie.LanguageConfig.msg;

public class WelcomeBlank extends JPanel {
	
	@Override
	public void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(0x1E1E1E));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		String message = msg("Andie_Welcome_Message");
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.setColor(Color.GRAY);
		int messageWidth = g.getFontMetrics().stringWidth(message);
		g.drawString(message, (int) Math.round((this.getWidth() - messageWidth) / 2), (int) Math.round(this.getHeight() / 2));
		String subMessage = msg("Andie_Welcome_Submessage");
		g.setFont(new Font("Arial", Font.PLAIN, 15));
		int subMessageWidth = g.getFontMetrics().stringWidth(subMessage);
		int subMessageHeight = g.getFontMetrics().getHeight();
		g.drawString(subMessage, (int) Math.round((this.getWidth() - subMessageWidth) / 2), (int) Math.round(this.getHeight() / 2 + subMessageHeight + 15));
	}
}
