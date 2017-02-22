
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This class draws the menu for the game. 
 * @author Jessie, Mira
 *
 */
public class StartPanel extends JPanel implements ActionListener{
	
	/**
	 * Game the Panel belongs to
	 * @author Mira
	 */
	Game game;
	/**
	 * Frame the Panel belongs to
	 * @author Mira
	 */
	PacFrame frame;
	/**
	 * Panel for the buttons of the start menu
	 * @author Mira
	 */
	JPanel buttonPanel;
	/**
	 * Panel for the heading of the start menu
	 * @author Mira
	 */
	JPanel pacmanLogoPanel;
	/**
	 * JButton representing the start button
	 * @author Jessie
	 */
	JButton startButton;
	/**
	 * JButton representing the quit button
	 * @author Jessie
	 */
	JButton quitButton;
	
	
	/**
	 * Initializes the two contained Panel, one for the heading, the other for the buttons of the start menu
	 * @param game refers to the current game and is saved in the property game.
	 * @author Jessie, Mira
	 */
	public StartPanel (Game game, PacFrame frame) {
		this.game = game;
		this.frame = frame;
		this.setLayout(new BorderLayout());
		this.setBackground(Color.BLACK);
		
		ImageIcon startIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/start.png")));
		this.startButton = new JButton(startIcon);
		this.startButton.setPreferredSize(new Dimension(startIcon.getIconWidth(), startIcon.getIconHeight()));
		this.startButton.addActionListener(this);
		ImageIcon quitIcon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/quit.png")));
		this.quitButton = new JButton(quitIcon);
		this.quitButton.setPreferredSize(new Dimension(quitIcon.getIconWidth(), quitIcon.getIconHeight()));
		this.quitButton.addActionListener(this);
		
		this.buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(1, 1));
		buttonPanel.setBackground(Color.black);
		this.buttonPanel.add(startButton);
		this.buttonPanel.add(quitButton);

		this.pacmanLogoPanel = new PacmanLogoPanel(game);
		
		this.add(pacmanLogoPanel, BorderLayout.NORTH);
		this.add(buttonPanel, BorderLayout.CENTER);
	}

//	/**
//	 * paint title and the menu icons on the panel
//	 * @author Jessie
//	 */
//	public void paint(Graphics g){
//		super.paint(g);
//	}
//	
	
	
	/**
	 * Gets information from the menu to start or quit the game.
	 * @author Mira, Jessie
	 */
	public void actionPerformed(ActionEvent e) {
			
			if(e.getSource()==startButton){
				this.frame.changeCentralPanel(0);
				this.game.gameIsInStartMenu = false;
				this.frame.repaint();
				game.countdownTimer.start();
			}
			if(e.getSource()==quitButton){
				System.exit(0);
			}
		}
	
}
