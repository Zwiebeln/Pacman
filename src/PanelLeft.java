import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * This class is the panel at the left side of PacFrame, to show the Scores and lives.
 * @author Mira, Jessie
 */

public class PanelLeft extends JPanel{
	/**
	 * Game the Panel refers to
	 * @author Mira
	 */
	Game game;

	/**
	 * To set the size and color of this left panel. 
	 * @param game refers to the current game and is saved in the property game.
	 * @author Mira
	 */
	public PanelLeft(Game game) {
		this.game = game;
		this.setPreferredSize(new Dimension(6*game.dotsPerField, (game.map.length*game.dotsPerField)));
		this.setBackground(Color.black);
	}

	/**
	 * Paint the scores and lives
	 * @author Mira
	 */
	public void paint(Graphics g){
		super.paint(g);
		if (game.gameIsInStartMenu) {
			return;
		}
		this.showHighscore(g);
		this.showPoints(g);
		this.drawLifes(g);	
	}
	
	
	
	/**
	 * Show lives on the left Panel with Pacman figures
	 * @author Mira, Jessie
	 */
	public void drawLifes(Graphics g){
		int currentLine = 11;
		g.setColor(Color.white);
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 28));
		g.drawString("Lifes", game.dotsPerField, game.dotsPerField*currentLine);
		currentLine ++;
		
		int lifes = game.pacman.lifes;
		int column = 1;
		int row = 0;
		while (lifes != 0){
			if (column>3) {
				column = 1;
				row += game.dotsPerField;
			}
			g.setColor(Color.yellow);
			g.fillArc(game.dotsPerField*column, game.dotsPerField*currentLine + row + game.dotsPerField/2, 20, 20, 25, 310);
			column++;
			lifes--;
		}
	}
	
	/**
	 * Show the points that Pacman got.
	 * @author Mira
	 */
	public void showPoints(Graphics g){
		int currentLine = 6;
		g.setColor(Color.white);
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 28));
		g.drawString("Score", game.dotsPerField, game.dotsPerField*currentLine);
		currentLine ++;
		
		int score = game.pacman.score;
		g.setColor(Color.yellow);
		g.drawString(String.valueOf(score), game.dotsPerField, game.dotsPerField*currentLine + game.dotsPerField/2);
	}
	
	public void showHighscore(Graphics g) {
		int currentLine = 2;
		g.setColor(Color.white);
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 28));
		g.drawString("Highscore", game.dotsPerField, game.dotsPerField*currentLine);
		currentLine ++;
		
		int score = game.pacman.highscore;
		g.setColor(Color.yellow);
		g.drawString(String.valueOf(score), game.dotsPerField, game.dotsPerField*currentLine + game.dotsPerField/2);
	}
}
