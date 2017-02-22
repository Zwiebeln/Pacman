import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Timer;

/**
 * This class represents the ghosts.
 * @author Mira, Anna
 */
public class Ghost extends Figure implements ActionListener {
	/**
	 * Contains the start position of an instance
	 * @author Mira
	 */
	Position startPosition;
	/**
	 * Specifies the Character of an instance 
	 * @author Mira
	 */
	GhostCharacter character;
	/**
	 * Holds the move pattern of an instance
	 * @author Mira
	 */
	GhostMovePattern movePattern;
	/**
	 * Counter to control the rendering of new directions, which does happen every fourth move.
	 * @author Mira
	 */
	int countdownToRenderNewDirection = 0;
	/**
	 * List that holds directions that have already been tried and tested as invalid.
	 * @author Mira
	 */
	List<Direction> invalidDirections;
	/**
	 * Position that represents the target of a ghost while it follows the move pattern DISPERSE.
	 * @author Mira
	 */
	Position targetWhileDisperse;
	
	/**
	 * Integer that holds the normal speed, so that after slowing down during the phase of escaping a ghost can return to it's old speed.
	 * @author Mira
	 */
	int normalStepsPerMove;
	
	/**
	 * Array of integers representing the speed in each level.
	 * @author Mira
	 */
	static public int[] speed = {4, 4, 4, 5, 5, 6, 6, 7, 7, 7};	
	/**
	 * Array of integers representing the duration of escaping in each level.
	 * @author Mira
	 */
	static private int[] escapeDuration = {15, 12, 12, 12, 10, 10, 10, 8, 8, 6};				
	/**
	 * Array of integers representing the duration of hunting in each level.
	 * @author Mira
	 */
	static private int[] huntDuration = {15, 15, 15, 15, 15, 17, 17, 17, 17, 19};
	/**
	 * Array of integers representing the duration of dispersing in each level.
	 * @author Mira
	 */
	static private int [] disperseDuration = {15, 15, 12, 12, 12, 12, 12, 12, 10, 10};
	
	/**
	 * true if a ghost is blinking
	 * @author Mira
	 */
	Boolean isBlinking = false;
	/**
	 * Counter to control the blinking of a ghost.
	 * @author Mira
	 */
	int blinkingCounter = 0;
	
	/**
	 * Timer that controls the duration of the move pattern GETOUT
	 * @author Mira
	 */
	Timer getoutTimer;
	/**
	 * Timer that controls the duration of the move pattern DISPERSE
	 * @author Mira
	 */
	Timer disperseTimer;
	/**
	 * Timer that controls the duration of the move pattern HUNT
	 * @author Mira
	 */
	Timer huntTimer;
	/**
	 * Timer that controls the duration of the move pattern ESCAPE
	 * @author Mira
	 */
	Timer escapeTimer;
	/**
	 * Timer that controls the duration of the move pattern TIMEOUT
	 * @author Mira
	 */
	Timer timeoutTimer;
	
	/**
	 * Initializes the ghost with a specified character and start position.
	 * @param position represents the start position of the ghost
	 * @param character represents the character of the ghost
	 * @param x represents the x-coordinate of the position that becomes the target of the ghost when it is dispersing
	 * @param y represents the y-coordinate of the position that becomes the target of the ghost when it is dispersing
	 * @author Mira
	 */
	public Ghost(Position position, GhostCharacter character, int x, int y) {
		this.position = position;
		this.startPosition = position;
		this.direction = Direction.UP;
		this.wantedDirection = direction;
		this.invalidDirections = new LinkedList<Direction>();
		this.stepsPerMove = speed[0];
		this.normalStepsPerMove = stepsPerMove;
		this.character = character;
		switch (character) {
		case ORANGE: this.targetWhileDisperse = new Position(0, 0); break;
		case PINK: this.targetWhileDisperse = new Position(x, 0); break;
		case GRAY: this.targetWhileDisperse = new Position(x, y); break;
		case CYAN: this.targetWhileDisperse = new Position(0, y); break;
		}
		this.movePattern = GhostMovePattern.GETOUT;
		
		this.getoutTimer = new Timer(1500, this);
		getoutTimer.setActionCommand("disperse");
		getoutTimer.setRepeats(false);
		this.disperseTimer = new Timer(15000, this);
		disperseTimer.setActionCommand("hunt");
		disperseTimer.setRepeats(false);
		this.huntTimer = new Timer(15000, this);
		huntTimer.setActionCommand("disperse");
		huntTimer.setRepeats(false);
		this.escapeTimer = new Timer(15000, this);
		escapeTimer.setActionCommand("hunt");
		escapeTimer.setRepeats(false);
		this.timeoutTimer = new Timer(10000, this);
		timeoutTimer.setActionCommand("getout");
		timeoutTimer.setRepeats(false);
	}
	
	/**
	 * This method controls the blinking of a ghost when it is escaping.
	 * @author Mira
	 */
	public void blinking() {
		blinkingCounter++;
		if (blinkingCounter == 2) {
			isBlinking = !isBlinking;
			blinkingCounter = 0;
		}
	}
	
	/**
	 * This method updated the properties of a ghost to a new level. With increasing level the ghost become faster, 
	 * the duration of dispersing and escaping decreases and the duration of hunting increases.
	 * @param level represents the number of the level to which the properties of the ghost are updated.
	 * @author Mira
	 */
	public void updateToNewLevel(int level) {
		this.stepsPerMove = speed[level];
		this.normalStepsPerMove = stepsPerMove;
		this.disperseTimer.setDelay(disperseDuration[level]*1000);
		this.escapeTimer.setDelay(escapeDuration[level]*1000);
		this.huntTimer.setDelay(huntDuration[level]*1000);
	}
	
	/**
	 * This method renders the direction a ghost wants to move in. 
	 * 
	 * Depending on his current move pattern a specified position is set as the target. 
	 * If the ghost is hunting the target is Pacman's position.
	 * 
	 * The method creates a list of the best directions to take in descending order.
	 * It returns the first direction that is not contained in the list of invalid directions.
	 * 
	 * @param pacmanPosition represents the current position of Pacman.
	 * @param inFrontOfGhostHouse represents the position right in front of the ghost's house.
	 * @return The method returns the new direction of the ghost.
	 * @author Anna, Mira
	 */
	public Direction renderDirection(Position pacmanPosition, Position inFrontOfGhostHouse) {
		Direction renderedDirection = direction;
		
		Position target = this.position;
		
		switch(this.movePattern) {
		case DISPERSE: target = this.targetWhileDisperse; break;
		case GETOUT: target = inFrontOfGhostHouse; break;
		case HUNT: target = pacmanPosition; break;
		case ESCAPE: target = pacmanPosition; break;
		}
		
		int differenceX = position.x-target.x;
		int differenceY = position.y-target.y;
					
		if (countdownToRenderNewDirection == 0) {
			countdownToRenderNewDirection = 3;
			
			List<Direction> listOfBestDirections = new LinkedList<Direction>();
			
			int[][] directionKey = {{0,2,3,1}, {0,3,2,1}, {1,2,3,0}, {1,3,2,0}, {2,0,1,3}, {3,0,1,2}, {2,1,0,3}, {3,1,0,2}};
			
			for(int rankOfDirection = 0; rankOfDirection<4; rankOfDirection++) {
				if ((Math.abs(differenceX)<Math.abs(differenceY)) && differenceY>=0){
					if (differenceX>=0) listOfBestDirections.add(Direction.values()[directionKey[0][rankOfDirection]]);
					else listOfBestDirections.add(Direction.values()[directionKey[1][rankOfDirection]]);
				} else if((Math.abs(differenceX)<Math.abs(differenceY)) && differenceY<0){
					if (differenceX>=0) listOfBestDirections.add(Direction.values()[directionKey[2][rankOfDirection]]);
					else listOfBestDirections.add(Direction.values()[directionKey[3][rankOfDirection]]);
				} else if((Math.abs(differenceX)>=Math.abs(differenceY)) && differenceY>=0){
					if (differenceX>=0) listOfBestDirections.add(Direction.values()[directionKey[4][rankOfDirection]]);
					else listOfBestDirections.add(Direction.values()[directionKey[5][rankOfDirection]]);
				} else if((Math.abs(differenceX)>=Math.abs(differenceY)) && differenceY<0){
					if (differenceX>=0) listOfBestDirections.add(Direction.values()[directionKey[6][rankOfDirection]]);
					else listOfBestDirections.add(Direction.values()[directionKey[7][rankOfDirection]]);
				}
			}
			
			if(movePattern == GhostMovePattern.ESCAPE) Collections.reverse(listOfBestDirections);
			
			Direction oppositeDirection = direction;
			
			switch (direction) {
			case UP: oppositeDirection = Direction.DOWN; break;
			case DOWN: oppositeDirection = Direction.UP; break;
			case LEFT: oppositeDirection = Direction.RIGHT; break;
			case RIGHT: oppositeDirection = Direction.LEFT; break;
			}
			
			for(int i=0; i<4; i++) {
				Direction possibleDirection = listOfBestDirections.get(i);
				if(!invalidDirections.contains(possibleDirection) && possibleDirection != oppositeDirection) {
					renderedDirection = listOfBestDirections.get(i);
					break;
				} else renderedDirection = oppositeDirection;
			}
			return renderedDirection;

		} else {
			countdownToRenderNewDirection--;
			return direction;
		}
	}
	
	@Override
	/**
	 * This method determines the reaction to a event.
	 * If the current event has the action command "disperse" the move pattern is set to DISPERSE and the disperse-Timer is started.
	 * If the current event has the action command "hunt" the move pattern is set to HUNT and the hunt-Timer is started.
	 * If the current event has the action command "getout" the move pattern is set to GETOUT and the getout-Timer is started.
	 * @author Mira
	 */
	public void actionPerformed(ActionEvent e) {
		String message = e.getActionCommand();
		switch(message) {
		case "disperse": this.movePattern = GhostMovePattern.DISPERSE; this.disperseTimer.start(); normalizeSpeed(); break;
		case "hunt": this.movePattern = GhostMovePattern.HUNT; this.huntTimer.start(); normalizeSpeed(); break;
		case "getout": this.movePattern = GhostMovePattern.GETOUT; this.getoutTimer.start(); normalizeSpeed(); break;
		}
	}
	
	/**
	 * This method decreases the speed of a ghost of a quater.
	 * @author Anna
	 */
	public void decreaseSpeed() {
		stepsPerMove -= stepsPerMove/4;
	}
	
	/**
	 * This method normalizes a ghost's speed when it stops escaping
	 * @author Mira
	 */
	public void normalizeSpeed() {
		this.stepsPerMove = this.normalStepsPerMove;
	}
	
	/**
	 * This method stops all currently running Timers of a ghost.
	 * @author Mira
	 */
	public void stopRunningTimer() {
		switch(movePattern) {
		case ESCAPE: escapeTimer.stop(); break;
		case HUNT: huntTimer.stop(); break;
		case DISPERSE: disperseTimer.stop(); break;
		case TIMEOUT: timeoutTimer.stop(); break;
		case GETOUT: getoutTimer.stop(); break;
		}
	}
}
