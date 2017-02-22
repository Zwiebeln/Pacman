/**
 * This class represents Pacman
 * @author Anna, Mira
 */
public class PacMan extends Figure {
	/**
	 * Integer representing the number of lifes Pacman has
	 * @author Mira
	 */
	int lifes;
	/**
	 * Integer representing the current score of Pacman
	 * @author Mira
	 */
	int score;
	/**
	 * Pacman's highscore in this session
	 * @author Mira
	 */
	int highscore = 0;
	/**
	 * Integer representing the degree of Pacman's mouth. It is used to control Pacman's mouth movement
	 * @author Mira
	 */
	int mouthDegree;
	/**
	 * true if Pacman's mouth is open. Used to control Pacman's mouth movement.
	 * @author Mira
	 */
	boolean mouthClosed = false;
	
	/**
	 * Initializes Pacman's lifes, position and mouthDegree with the specified values.
	 * @param lifes representing the number of lifes Pacman has.
	 * @param position representing Pacman's position.
	 * @param mouthDegree representing the degree of Pacman's mouth.
	 * @author Mira
	 */
	public PacMan(int lifes, Position position, int mouthDegree) {
		this.lifes = lifes;
		this.score = 0;
		this.direction = Direction.LEFT;
		this.stepsPerMove = 4;
		this.position = position;
		this.mouthDegree = mouthDegree;
	}
	
	/**
	 * This method updates Pacman's speed when a new level is loaded.
	 * @param level representing the number of the new level.
	 * @author Mira
	 */
	public void updateToNewLevel(int level) {
		if(level == 6) this.stepsPerMove = 5;
	}
	
	/**
	 * This method increases Pacman's point of the specified value.
	 * @param points representing the value of which Pacman's points are increased.
	 * @author Anna
	 */
	public void increasePoints(int points) {
		this.score = score + points;
		if (this.score > this.highscore) this.highscore = this.score;
	}
	
	/**
	 * This method increases Pacman's lifes of 1.
	 * @author Anna
	 */
	public void increaseLifes() {
		lifes++;
	}
	
	/**
	 * This method decreases Pacman's lifes of 1.
	 * @author Anna
	 */
	public void decreaseLifes() {
		lifes--;
	}
}
