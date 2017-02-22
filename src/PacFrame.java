import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.JFrame;
/**
 * This class extends the JFrame and coordinate all panels on one frame.
 * @author Jessie, Mira
 */

public class PacFrame extends JFrame {
	
	/**
	 * Game the Frame belongs to
	 * @author Mira
	 */
	Game game;
	/** 
	 * GameController that is added as KeyListener to the frame
	 * @author Mira
	 */
	GameController gameController;
	/**
	 * Main panel which shows the map
	 * @author Jessie
	 */
	PacPanel pacPanelMap;
	/**
	 * Left Panel which shows highscore, points and lifes
	 * @author Jessie
	 */
	PanelLeft panelLeft;
	/**
	 * Right Panel which shows the number of the level and the number of coming fruits
	 * @author Jessie
	 */
	PanelRight panelRight;
	/**
	 * Start Panel which shows the start menu
	 * @author Jessie
	 */
	StartPanel startPanel;
	
	/**
	 * In this method, the character for frame is settled, and all panels are called and located at the Frame. 
	 * Changing user input is also listened by the frame.   
	 * @param game refers to the current game and is saved in the property game.
	 * @param gameController refers to the gameController of the game which delivers the user's input and is saved in the property gameController
	 * @author Jessie, Mira
	 */
	public PacFrame(Game game, GameController gameController) {
		
		this.setTitle("Pacman");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.game = game;
		this.gameController = gameController;
		this.setLayout(new BorderLayout());
		this.startPanel = new StartPanel(game, this);
		this.pacPanelMap = new PacPanel(game);
		this.panelLeft = new PanelLeft(game);
		this.panelRight = new PanelRight(game);
		
		getContentPane().add(panelLeft, BorderLayout.LINE_START);
		getContentPane().add(startPanel, BorderLayout.CENTER);
		getContentPane().add(panelRight, BorderLayout.LINE_END);
		this.addKeyListener(this.gameController);
		this.pack();
	}
	
	/**
	 * This method changes the central Panel. It is invoked when the frame switches from the start menu to the game or the other way around.
	 * @param i indicates which Panel is to be added and which is to be removed. Two values are possible: 0 for the switch from start menu to game and 1 for the switch from game to start menu.
	 * @author Mira
	 */
	public void changeCentralPanel(int i){
		if(i == 0) {
			getContentPane().remove(startPanel);
			getContentPane().add(pacPanelMap, BorderLayout.CENTER);
			this.requestFocusInWindow();
			this.pack();
		} else if (i == 1) {
			getContentPane().remove(pacPanelMap);
			getContentPane().add(startPanel, BorderLayout.CENTER);
			this.pack();
		}
	}
	
}
