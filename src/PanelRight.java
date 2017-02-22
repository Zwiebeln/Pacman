import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;

import javax.swing.JPanel;

/**
 * This class is the Panel at the left side of Pacpanel, draw the Level and fruit information.
 * @author Mira, Jessie
 */

public class PanelRight extends JPanel {

	/**
	 * Game the Panel refers to
	 * @author Mira
	 */
	Game game;
	/**
	 * To set the size and color of this right panel.
	 * @param game refers to the current game and is saved in the property game.
	 * @author Mira
	 */
	public PanelRight(Game game) {
		this.game = game;
		this.setPreferredSize(new Dimension(6*game.dotsPerField, (game.map.length*game.dotsPerField)));
		this.setBackground(Color.black);
	}
	
	/**
	 * Paint the level and fruits
	 * @author Jessie
	 */
	public void paint(Graphics g){
		super.paint(g);
		if (game.gameIsInStartMenu) {
			return;
		}
		this.showLevel(g);
		this.showFruits(g);
	}
	

	/**
	 * Import an image and draw the fruit
	 * @author Jessie
	 */
	public void drawFruit(int x, int y, Graphics g){
		Image fruit= Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/fruit.png"));
		g.drawImage(fruit, x, y, 20, 20, this);
	}
	
	/**
	 * Shows the level information. DotPerField from class game is used to settle down the position.
	 * @author Mira
	 */
	public void showLevel(Graphics g){
		int currentLine = 2;
		g.setColor(Color.white);
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 28));
		g.drawString("Level", game.dotsPerField, game.dotsPerField*currentLine);
		currentLine ++;
		
		int level = game.numberOfLevel + 1;
		g.setColor(Color.yellow);
		g.drawString(String.valueOf(level), game.dotsPerField, game.dotsPerField*currentLine + game.dotsPerField/2);
	}
	
	/**
	 * Shows the fruit information. The for each loop is used to ensure each row only draw one word.
	 * @author Mira
	 */
	public void showFruits(Graphics g){
		int currentLine = 10;
		g.setColor(Color.white);
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
		for(String word : "Fruits coming in this Level".split(" ")) {
			g.drawString(word, game.dotsPerField, game.dotsPerField*currentLine);
			currentLine ++;
		}
		
		int numOfFruits = game.fruitsInThisLevel - game.countedFruits;
		int column = 1;
		int row = 0;
		while (numOfFruits != 0){
			if (column>3) {
				column = 1;
				row += game.dotsPerField;
			}
			drawFruit(game.dotsPerField*column, game.dotsPerField*currentLine + row + game.dotsPerField/2, g);
			column++;
			numOfFruits--;
		}
	}

}
