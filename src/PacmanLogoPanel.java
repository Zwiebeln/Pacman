import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * This panel contains the heading "Pacman" that is printed in the start menu
 * @author Jessie
 */
public class PacmanLogoPanel extends JPanel {

	/**
	 * Game the Panel belongs to
	 */
	Game game;
	
	int x= 50;
	int y= 250;
	
	/**
	 * Get information from class Game to settle down the size of this panel.
	 * @param game refers to the current game and is saved in the property game.
	 */
	public PacmanLogoPanel(Game game) {
		this.game = game;
		this.setPreferredSize(new Dimension((game.map[0].length*game.dotsPerField), (game.map.length*game.dotsPerField)/2));
	}
	
	/**
	 * paints the heading for the start menu
	 */
	public void paint (Graphics g) {
		Font pacman= new Font("Comic Sans MS", Font.BOLD, 100);
		
		g.setFont(pacman);
		g.setColor(Color.ORANGE);
		g.drawString("P", x, 150);
		g.drawString("A", x+50, 150);
		g.setColor(Color.YELLOW);
		g.fillArc(x+130, 80, 70, 70, 25, 310);	
		g.setColor(Color.ORANGE);
		g.drawString("M", x+215, 150);
		g.drawString("A", x+310, 150);
		g.drawString("N", x+380, 150);
	}
}
