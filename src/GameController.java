import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 
 * @author Jessie, Mira
 * This class takes the users input, that controls the game, especially the movement of Pacman.
 */
public class GameController implements KeyListener {
	/**
	 * Property to safe the wanted direction of Pacman, determined by the users input.
	 * @author Mira
	 */
	private Direction wantedDirection;
	
	/**
	 * gets the value of the property wantedDirection
	 * @return returns the wanted direction
	 * @author Mira
	 */
	public Direction getWantedDirection() {
		return wantedDirection;
	}

	/**
	 * sets the value of the property wantedDirection
	 * @param wantedDirection specifies the value to be set
	 * @author Mira
	 */
	public void setWantedDirection(Direction wantedDirection) {
		this.wantedDirection = wantedDirection;

	}

	/**
	 * Initializes the property wantedDirection with the value LEFT.
	 * @author Mira
	 */
	public GameController() {
		this.wantedDirection = Direction.LEFT;
	}
	
	@Override
	/**
	 * Gets the user's input and safes in the field wantedDirection.
	 * @author Jessie
	 */
	public void keyPressed(KeyEvent e) { 
		if(e.getKeyCode()==KeyEvent.VK_UP){
			wantedDirection= Direction.UP;	
		} else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
			wantedDirection= Direction.RIGHT;	
		} else if(e.getKeyCode()==KeyEvent.VK_DOWN){
			wantedDirection= Direction.DOWN;		
		} else if(e.getKeyCode()==KeyEvent.VK_LEFT){
			wantedDirection= Direction.LEFT ;	
		}	
	}

	@Override
	public void keyReleased(KeyEvent arg0) {	
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

}
