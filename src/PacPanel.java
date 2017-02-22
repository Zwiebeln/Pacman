import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;

import javax.swing.JPanel;

/**
 * This class draws the main Panel of the game.
 * @author Jessie, Mira
 */
public class PacPanel extends JPanel  {
	
	/**
	 * Game the Panel refers to
	 * @author Mira
	 */
	Game game;
	
	/** 
	 * Get map information from the main class, to setle down the size for the PacPanel.
	 * @param game refers to the current game and is saved in the property game.
	 * @author Mira
	 */
	public PacPanel (Game game) {
		this.game = game;
		this.setPreferredSize(new Dimension((game.map[0].length*game.dotsPerField), (game.map.length*game.dotsPerField)));
	}

	/**
	 * Paint all staffs on the PacPanel.
	 * @author Mira, Jessie
	 */
	public void paint(Graphics g) {
		super.paint(g);
		/**
		 *  By this loop, the map is analyzed and classified, and each grid calls the suitable method to
		 *  paint it. 
		 *  Walls and background, which don't affect the game dynamically are called non items.
		 *  Other types, like dots, big dots and fruits that have influence on the game, are called items. 
		 *  @author Mira, Jessie 
		 */
		for(int i=0; i<game.map.length; i++) {
			for(int j=0; j<game.map[0].length; j++) {
				if(game.map[i][j] instanceof NoItemField) {
					NoItemField field = (NoItemField) game.map[i][j];
					if (field.fieldType == FieldType.WALL) {
						this.drawWall(j*game.dotsPerField, i*game.dotsPerField, g);
					}else{
						this.setBackground(Color.BLACK);
					}
				} else {
					ItemField field = (ItemField) game.map[i][j];
					switch (field.item) {
					case DOT : this.drawGold((j*game.dotsPerField+game.dotsPerField/2)-4, (i*game.dotsPerField+game.dotsPerField/2)-4, g, 0);
					break;
					case BIGDOT : this.drawGold((j*game.dotsPerField+game.dotsPerField/2)-7, (i*game.dotsPerField+game.dotsPerField/2)-7, g, 1);
					break;
					case FRUIT : if(game.fruitAvailable && game.fruitVisible) this.drawFruit((j*game.dotsPerField+game.dotsPerField/2)-10, (i*game.dotsPerField+game.dotsPerField/2)-10, g);
					break;
					}
				}
			}
		}
		/**
		 * Read information from class Game. When Pacman gets eaten but the game is not over, the Pacman is painted again at it's initial place.
		 */
		if (game.gameMessage != "Game Over") this.drawPacman(game.pacman.position.x-14, game.pacman.position.y-14, g, game.pacman.direction);
		for (int i=0; i<game.ghosts.size(); i++) {
			this.drawGhost(game.ghosts.get(i).position.x-14, game.ghosts.get(i).position.y-14, g, game.ghosts.get(i).character, game.ghosts.get(i).movePattern, game.ghosts.get(i).isBlinking);
		}
		if (!game.gameIsRunning) {
			drawMessage(game.pacmanStartPosition.x, game.pacmanStartPosition.y, game.gameMessage, g);
		}
		
	}
	

	
	private int directionAngle;
	/**
	 * To ensure the movement of Pacman's mouth, each Pacman is drawn by two fillledArc. With the change of mouthDegree,
	 * Pacman's mouth can move with its pace.  
	 * @param game refers to the current game and is saved in the property game.
	 * @author Jessie 
	 */

	public void drawPacman(int x, int y, Graphics g, Direction direction){
		g.setColor(Color.yellow);
		
		switch(direction){
		case UP:
			directionAngle=90;
			break;
		case RIGHT:
			directionAngle=0;
			break; 
		case DOWN:
			directionAngle=-90;
			break;
		case LEFT:
			directionAngle=180;
		}
		
		g.fillArc(x, y, 29, 29, game.pacman.mouthDegree+directionAngle, 180-game.pacman.mouthDegree);
		g.fillArc(x, y, 29, 29, 180+directionAngle, 180-game.pacman.mouthDegree);
	}
	
	
	
	/**
	 * Draw the ghosts with rounds and rectangles. And if a big dot is eaten, the ghost would change its move pattern and flashed
	 * in color blue and white.
	 * @author Jessie
	 */
	public void drawGhost(int x, int y, Graphics g, GhostCharacter character, GhostMovePattern movePattern, Boolean isBlinking){
		
		switch(character){
		case ORANGE:
			g.setColor(Color.orange);
			break;
		case PINK:
			g.setColor(Color.PINK);
			break;
		case GRAY:
			g.setColor(Color.gray);
			break;
		case CYAN:
			g.setColor(Color.CYAN);
			break;
		}
	
		if (movePattern == GhostMovePattern.ESCAPE) {
			if(isBlinking) g.setColor(Color.white);
			else g.setColor(Color.blue);
		}
		
		g.fillOval(x, y, 30, 30);
		g.fillRect(x, y+15, 30, 15);
		g.fillOval(x, y+25, 10, 10);
		g.fillOval(x+10, y+25, 10, 10);
		g.fillOval(x+20, y+25, 10, 10);
		
		//draw the eyes of ghost.
		g.setColor(Color.white);
		g.fillOval(x+2, y+07, 11, 11);
		g.fillOval(x+16, y+07, 11, 11);
		g.setColor(Color.BLACK);
		g.fillOval(x+7, y+10, 5, 5);
		g.fillOval(x+21, y+10, 5, 5);
	}

	
	/**
	 * Paint the dots. type 0 for small dot and type1 for big dot.
	 * @author Jessie
	 */
	public void drawGold(int x, int y, Graphics g, int type){
		
		g.setColor(Color.ORANGE);
		switch(type){
		case 0:
			g.fillOval(x, y, 8, 8);
			break;
		case 1:
			g.fillOval(x, y, 14, 14);
		}
	}

	/**
	 * Paint the fruit with an imported image.
	 * @author Jessie
	 */
	
	public void drawFruit(int x, int y, Graphics g){
		Image fruit= Toolkit.getDefaultToolkit().getImage(Panel.class
				.getResource("/fruit.png"));
		g.drawImage(fruit, x, y, 20, 20, this);
	}
	
	/**
	 *Paint the wall with an imported image.
	 *@author Jessie
	 */
	public void drawWall(int x, int y, Graphics g){
		
		Image wall= Toolkit.getDefaultToolkit().getImage(Panel.class
				.getResource("/Wall.png"));
		g.drawImage(wall, x, y, game.dotsPerField, game.dotsPerField, this);
		
	}
	
	
	/**
	 * Draw the message at the beginning of the game and each time the Pacman got eaten. Includes the level
	 * of the game, and the count-down message
	 * @author Mira
	 */
	public void drawMessage (int x, int y, String message, Graphics g) {
		
		if(message == "Level") message = message + " " + (game.numberOfLevel+1);
		
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 64));
		FontMetrics fontMetrics = g.getFontMetrics();
		
		g.setColor(Color.black);
		g.drawString(message, x-fontMetrics.stringWidth(message)/2 -3, y+fontMetrics.getHeight()/4 -3);
		g.drawString(message, x-fontMetrics.stringWidth(message)/2 +3, y+fontMetrics.getHeight()/4 -3);
		g.drawString(message, x-fontMetrics.stringWidth(message)/2 +3, y+fontMetrics.getHeight()/4 +3);
		g.drawString(message, x-fontMetrics.stringWidth(message)/2 -3, y+fontMetrics.getHeight()/4 +3);
		
		g.setColor(Color.CYAN);
		g.drawString(message, x-fontMetrics.stringWidth(message)/2,y+fontMetrics.getHeight()/4);
		
	}
	
 
	
}


