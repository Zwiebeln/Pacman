import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.Timer;

/**
 * This is the central class of the game 'Pacman'.
 * It controls the flow of the game by initializing the map, determining the movement of all figures, testing possible collisions,
 * and managing the score, the lifes and the level.
 * @author Mira
 */
public class Game implements ActionListener {

	/**
	 * The Pacman instance
	 */
	PacMan pacman;
	/**
	 * The start position of Pacman
	 */
	Position pacmanStartPosition;
	/**
	 * The list of all instances of the class Ghost
	 */
	List<Ghost> ghosts = new LinkedList<Ghost>();
	/**
	 * The start positions of all the ghosts
	 */
	List<Position> ghostStartPositions = new LinkedList<Position>();
	/**
	 * The position of the field right in front of the ghost house
	 */
	Position inFrontOfGhostHouse;
	/**
	 * The map represented by a two-dimensional array of Fields
	 */
	Field[][] map;
	/**
	 * The frame
	 */
	PacFrame pacFrame;
	/**
	 * The game controller that takes the player's input
	 */
	GameController gameController;
	/**
	 * The string, which is used to build the map
	 */
	String mapPlan;
	
	/**
	 * The size of each field in pixel
	 */
	int dotsPerField;					
	/**
	 * The number of points, which Pacman needs to get an extra Life
	 */
	int scoreNeededToGetNewLife = 800;		
	/**
	 * The number of all existing dots in a level
	 */
	int absoluteNumberOfDots;
	/**
	 * The number of all dots, that have not be eaten by Pacman yet and are still visible on the map
	 */
	int currentNumberOfDots = 0;
	/**
	 * The number of the current level starting by 0
	 */
	int numberOfLevel = 0;
	/**
	 * The number of all extra lifes Pacman has got so far
	 */
	int alreadyGotLifes = 0;                 
	/**
	 * The number of all fruits coming in a level
	 */
	int fruitsInThisLevel = 3;
	/**
	 * The number of the fruits that have already appeared
	 */
	int countedFruits = 0;
	
	/**
	 * true if game is in the start menu
	 */
	Boolean gameIsInStartMenu = true;
	/**
	 * true if the game is running 
	 */
	Boolean gameIsRunning;
	/**
	 * Message that is printed during the countdown
	 */
	String gameMessage = "Level";
	/**
	 * true if a fruit is available for Pacman
	 */
	Boolean fruitAvailable;
	/**
	 * true if a fruit is visible. This boolean is used to let the fruit be blinking
	 */
	Boolean fruitVisible;
	/**
	 * Counter to control the change of a fruits visibility while the fruit is available
	 */
	int counterForFruitTimer;
	
	/**
	 * Timer that controls the movement of the game while the game is running
	 */
	Timer moveTimer;
	/**
	 * Timer that controls the countdown
	 */
	Timer countdownTimer;
	/**
	 * Timer that controls the appearing of a fruit
	 */
	Timer fruitTimer;
	/**
	 * Timer that leads back to the start menu
	 */
	Timer gameOverTimer;
	
	/**
	 * Constructor initializes all properties. The map is build following the given string
	 * @param map represents the map to build by a string
	 * @param gameController is an instance of GameController used to take the player's input 
	 */
	public Game(String map, GameController gameController) {
		
		this.dotsPerField = 29;					
		this.mapPlan = map;
		this.initMap(map);
		for (int i=0; i<ghostStartPositions.size(); i++) {
			this.ghosts.add(new Ghost(ghostStartPositions.get(i), GhostCharacter.values()[i], this.map[0].length*dotsPerField, this.map.length*dotsPerField));
		}
		this.absoluteNumberOfDots = currentNumberOfDots;
		this.pacman = new PacMan(3, this.pacmanStartPosition, 30);
		this.gameController = gameController;
		this.pacFrame = new PacFrame(this, gameController);
		this.gameIsRunning = false;
		this.fruitAvailable = false;
		this.fruitVisible = true;
		this.counterForFruitTimer = 0;
		
		this.moveTimer = new Timer(100, this);
		moveTimer.setActionCommand("move");
		this.countdownTimer = new Timer(1000, this);
		countdownTimer.setActionCommand("countdown");
		countdownTimer.setInitialDelay(1500);
		this.fruitTimer = new Timer(15000, this);
		fruitTimer.setActionCommand("fruit");
		fruitTimer.setRepeats(false);
		this.gameOverTimer = new Timer(3500, this);
		gameOverTimer.setActionCommand("gameOver");
		gameOverTimer.setRepeats(false);
	}
	
	/**
	 * This method is invoked by the constructor an initializes the map by reading the given string and adding Fields to the map respectively
	 * @param mapPlan is the string representing the map to be build
	 */
	private void initMap(String mapPlan) {
		
		String[] rows = mapPlan.split("\n");
		int numOfRows = rows.length;
		int numOfColumns = rows[0].length(); //hier wird von gleicher Zeilenlänge ausgegangen. Besser wäre den String tatsächlich zu validieren
		this.map = new Field[numOfRows][numOfColumns];
		
		for(int i=0; i<numOfRows; i++) {
			for(int j=0; j<numOfColumns; j++) {
				
				switch (rows[i].charAt(j)) {
				case 'w' : this.map[i][j] = new NoItemField(FieldType.WALL); break;
				case 'n' : this.map[i][j] = new ItemField(Item.NONE); break;
				case 't' : this.map[i][j] = new NoItemField(FieldType.GHOSTEXIT); 
				this.inFrontOfGhostHouse = new Position(j*dotsPerField+dotsPerField/2, i*dotsPerField+dotsPerField/2); break; 
				case 'f' : this.map[i][j] = new ItemField(Item.FRUIT);
				this.pacmanStartPosition = new Position(j*dotsPerField+dotsPerField/2, i*dotsPerField+dotsPerField/2); break;	
				case 'g' : this.map[i][j] = new NoItemField(FieldType.GHOSTSTART);
				this.ghostStartPositions.add(new Position(j*dotsPerField+dotsPerField/2, i*dotsPerField+dotsPerField/2)); break;		//hier wird von genau 4 Startpositionen für Geister ausgegangen. Es sollte besser getestet werden ob numOfGhostStartPosition < 4 ist
				case 'c' : this.map[i][j] = new NoItemField(FieldType.CORRIDOR); break;
				case 'd' : this.map[i][j] = new ItemField(Item.DOT); 
				currentNumberOfDots++; break;
				case 'D' : this.map[i][j] = new ItemField(Item.BIGDOT); break;	
				}	
				
			}
		}
	}
	
	@Override
	/**
	 * This method determines the reaction to a event.
	 * If the current event has been fired by the move-Timer the function move() is invoked,
	 * if the current event has been fired by the countdown-Timer the function countdown() is invoked
	 * and if the current event has been fired by the fruit-Timer the value of fruitAvailable is set to false
	 * and the counterForFruitTimer is set to 0.
	 */
	public void actionPerformed (ActionEvent e) {	
		//Game soll nur auf die move()-action, die vom Timer geworfen wird reagieren
		String message = e.getActionCommand();
		switch(message) {
		case "move": move(); break;
		case "countdown": countdown(); break;
		case "fruit": this.fruitAvailable = false; counterForFruitTimer = 0; break;
		case "gameOver":  gameOver(); break;
		}
	}
	
	/**
	 * This method is invoked when the game is over. The game is over when Pacman lost all his lifes or the 10th Level has been passed.
	 * The method resets all necessary values and leads the user back to the start menu.
	 */
	private void gameOver() {
		this.gameIsInStartMenu = true;
		this.numberOfLevel=0;
		this.pacman.position = this.pacmanStartPosition;
		this.pacman.direction = Direction.LEFT;
		this.pacman.score = 0;
		this.pacman.lifes = 3;
		pacman.stepsPerMove = 4;
		for(int i=0; i<4; i++) {
			Ghost ghost = ghosts.get(i);
			ghost.position = ghostStartPositions.get(i);
			ghost.stepsPerMove = Ghost.speed[0];
			ghost.normalStepsPerMove = ghost.stepsPerMove;
		}
		this.resetDots();
		this.numberOfLevel = 0;
		this.alreadyGotLifes = 0;
		this.countedFruits = 0;
		this.gameMessage = "Level";
		this.pacFrame.changeCentralPanel(1); 
		this.pacFrame.repaint();
	}
	
	/**
	 * This method is invoked when the countdown-Timer has fired an event.
	 * It changes the message that is to be printed during the countdown 
	 * and after the last output it stops the countdown-Timer and starts the move-Timer of the game and the getout-Timer of all ghosts.
	 * Furthermore it sets the boolean gameIsRunning to true.
	 */
	private void countdown() {
		this.pacFrame.repaint();
		
		switch(this.gameMessage) {
		case "Level": gameMessage = "3"; break;
		case "3": gameMessage = "2"; break;
		case "2": gameMessage = "1"; break;
		case "1": gameMessage = "Start!"; break;
		case "Start!": gameMessage = "3"; 
		countdownTimer.stop(); 
		moveTimer.start();
		for (Ghost ghost : this.ghosts) {
			ghost.movePattern = GhostMovePattern.GETOUT;
			ghost.getoutTimer.start();
		}
		gameIsRunning = true; 	
		break;
		}
	}
	
	/**
	 * This method is invoked when the move-Timer has fired an event.
	 * At first it iterates over the list of ghosts and selects those who can change their position in this move
	 * (all ghost that don't have a timeout at this moment are selected).
	 * The method invokes the function testAndSetPosition for all figures and after the new positions are set it tests possible collisions.
	 * Furthermore it controls the movement of Pacman's mouth and the visibility of a possible fruit.
	 * At the end of this method repaint() is invoked on pacFrame.
	 */
	private void move() {
		List<Ghost> moveableGhosts = new LinkedList<Ghost>();
		for(Ghost ghost : this.ghosts) {
			if(ghost.movePattern != GhostMovePattern.TIMEOUT) moveableGhosts.add(ghost);
		}
		
		testAndSetPosition(pacman, false, null, null, gameController.getWantedDirection());
		for(Ghost ghost : moveableGhosts) {
			testAndSetPosition(ghost, false, pacman.position, inFrontOfGhostHouse, null);
			if(ghost.movePattern == GhostMovePattern.ESCAPE) ghost.blinking();
		}
		
		testCollisions();
		
		pacmanMouthMovement();
		
		if(fruitAvailable) {
			counterForFruitTimer ++;
			if (counterForFruitTimer%3==0) fruitVisible = !fruitVisible;
		}

		this.pacFrame.repaint();
	}
	
	/**
	 * This method tests and sets the positions of all figures.
	 * Therefore it first gets the wanted positions and the tests these positions by invoking the method positionIsValid @see positionIsValid
	 * If the wanted position is valid it is set as the new position of the figure.
	 * If the wanted position is not valid and the current figure is an instance of Ghost the instance has to render a new position excluding the invalid one.
	 * If else the wanted position is not valid, but the current figure is Pacman, Pacman's direction doesn't change. 
	 * So if the player has made a mistake by choosing an invalid direction Pacman continues moving in his old direction.
	 * @param figure represents the figure whose position has to be tested
	 * @param lastDirectionFailed true if the last rendered direction was invalid. 
	 * This parameter is used to decide whether Pacman can render a new direction or has to keep his old one.
	 * @param pacmanPosition represents the position of Pacman needed by the ghosts to render their new direction and position.
	 * @param inFrontOfGhostHouse represents the position right in front of the ghost's house.
	 * @param wantedDirectionOfPacman represents the wanted direction of Pacman, which is given from the GameController to the method renderPosition so that Pacman can calculate his new position following the input of the player.
	 */
	public void testAndSetPosition (Figure figure, Boolean lastDirectionFailed, Position pacmanPosition, Position inFrontOfGhostHouse, Direction wantedDirectionOfPacman) {
		Position wantedPosition = figure.renderPosition(dotsPerField, lastDirectionFailed, pacmanPosition, inFrontOfGhostHouse, wantedDirectionOfPacman);
		if (positionIsValid(wantedPosition, figure)) {
			if(figure instanceof Ghost) {
				((Ghost)figure).invalidDirections.clear();
			}
			figure.direction = figure.wantedDirection;
			figure.setPostion(wantedPosition);
		}
		else if(figure instanceof Ghost) {
			((Ghost) figure).invalidDirections.add(((Ghost) figure).wantedDirection);
			testAndSetPosition(figure, true, pacmanPosition, inFrontOfGhostHouse, null);
		}
		else if(figure instanceof PacMan && !lastDirectionFailed) {
			testAndSetPosition(figure, true, null, null, wantedDirectionOfPacman);
		}
	}
	
	/**
	 * This method determines the movement of Pacman's mouth.
	 */
	private void pacmanMouthMovement() {
		if(pacman.mouthClosed) {
			pacman.mouthDegree += 15;
			pacman.mouthClosed = false;
		}
		else {
			pacman.mouthDegree -= 15;
			pacman.mouthClosed = true;
		}
	}
	
	/**
	 * This method validates a given position regarding the map.
	 * First it checks if the current figure wants to go through the corridor. This is only possible if the figure is Pacman, so if a ghost tries to walk through the corridor the method returns false. 
	 * Then it checks if the figure wants to enter or reenter the ghost's house and if so, returns false.
	 * At last the method checks if the wanted position is to close to a wall and if so, returns false.
	 * Only if all test pass the wanted position is valid and the method returns true.
	 */
	private Boolean positionIsValid (Position wantedPosition, Figure figure) {
		
		int i = wantedPosition.y/dotsPerField;
		int j = wantedPosition.x/dotsPerField;
		
		int iCurrent = figure.position.y/dotsPerField;
		int jCurrent = figure.position.x/dotsPerField;
		
		//Checks if the wanted Position is out of range or if it belongs to a NoItemField with the fieldtype CORRIDOR.
		if (i<0 || i>=map.length) return false;
		
		if(j<0 || j>=map[0].length) {
			
			//only Pacman can use the corridor
			if(figure instanceof Ghost) return false;
			
			if (j<0 && map[i][j+1] instanceof NoItemField) {
				NoItemField field = (NoItemField) map[i][j+1];
				if (field.fieldType == FieldType.CORRIDOR) {
					wantedPosition.x = (map[0].length*dotsPerField - 1);
					return true;
				}
			}
			else if (j>=map[0].length && map[i][j-1] instanceof NoItemField) {
				NoItemField field = (NoItemField) map[i][j-1];
				if (field.fieldType == FieldType.CORRIDOR) {
					wantedPosition.x = 0;
					return true;
				}
			} 
			return false;
		}
		
		
		//checks whether a ghost tries to use the corridor. If so, returns false.
		if (figure instanceof Ghost && map[i][j] instanceof NoItemField && ((NoItemField)map[i][j]).fieldType == FieldType.CORRIDOR) {
			if (wantedPosition.x < 5 || wantedPosition.x > (map[0].length*dotsPerField -5)) return false;
		}
		
		
		//checks whether the figure tries to enter the ghost's house. If so, returns false, because it is not allowed to go back into the house after coming out.
		if (map[i][j] instanceof NoItemField && ((NoItemField)map[i][j]).fieldType == FieldType.GHOSTSTART)
			if(map[iCurrent][jCurrent] instanceof NoItemField && ((NoItemField)map[iCurrent][jCurrent]).fieldType != FieldType.GHOSTSTART) {
				return false;
		}
		
		
		//checks whether the wanted position is too close to a wall. If so, returns false.
		if(j-1>=0 && map[i][j-1] instanceof NoItemField) {
			NoItemField leftField = (NoItemField) map[i][j-1];
			if(leftField.fieldType == FieldType.WALL && wantedPosition.x < (dotsPerField*j + dotsPerField/2)) return false;
		}
		if(j+1<map[i].length && map[i][j+1] instanceof NoItemField) {
			NoItemField rightField = (NoItemField) map[i][j+1];
			if(rightField.fieldType == FieldType.WALL && wantedPosition.x > (dotsPerField*j + dotsPerField/2)) return false;
		}
		if(i-1>=0 && map[i-1][j] instanceof NoItemField) {
			NoItemField upField = (NoItemField) map[i-1][j];
			if(upField.fieldType == FieldType.WALL && wantedPosition.y < (dotsPerField*i + dotsPerField/2)) return false;
		}
		if(i+1<map.length && map[i+1][j] instanceof NoItemField) {
			NoItemField downField = (NoItemField) map[i+1][j];
			if(downField.fieldType == FieldType.WALL && wantedPosition.y > (dotsPerField*i + dotsPerField/2)) return false;
		} 
		
		//if all tests have passes, the method returns true
		return true;
	}
	
	
	/**
	 * This method test all possible collisions of Pacman after all figures have made their move.
	 * First it checks if Pacman is colliding with a ghost. If this is the case the move pattern of the ghost determines the consequence.
	 * If the ghost is in the escape-move-pattern he gets eaten and the method ghostGotEaten is invoked.
	 * If he is in one of the other move pattern Pacman gets eaten and the method pacmanGotEaten is invoked.
	 * If there is no collision with a ghost the method checks if Pacman is on an ItemField an if their is an Item to eat.
	 * If so, the method ItemGotEaten is invoked.
	 */
	private void testCollisions() {
		
		for (Ghost ghost : this.ghosts) {
			if (Math.abs(ghost.position.x-pacman.position.x) < 24 && Math.abs(ghost.position.y-pacman.position.y) < 24) {
				if(ghost.movePattern != GhostMovePattern.ESCAPE) {
					pacManGotEaten();
					return;
				} else {
					ghostGotEaten(ghost); 
				}
			}
		}
		
		int j = pacman.position.x/dotsPerField;		//j-value of the field Pacman stands on (x-axis)
		int i = pacman.position.y/dotsPerField;		//i-value of the field Pacman stands on (y-axis)
		
		if (map[i][j] instanceof ItemField) {
			ItemField field = (ItemField) map[i][j];
			if (field.item != Item.NONE) {
				itemGotEaten(field.item, i, j);
			}
		}
	}
	
	/**
	 * This method is invoked if Pacman has eaten an Item.
	 * 
	 * If it is a normal dot, the method increases Pacman's points of 1 and checks if there are still dots on the map. 
	 * If there are no more dot an new Level begins by invoking the method loadNewLevel().
	 * Furthermore the value of currentDots is decreased.
	 * 
	 * If it is a big dot, the method increases Pacman's points of 20 and changes the ghost's move pattern to ESCAPE.
	 * 
	 * If it is a fruit, the method increases Pacman's points of 50 and sets the boolean fruitAvailable to false.
	 * 
	 * After all the method checks if Pacman has enough points to get an extra life.
	 * 
	 * @param item represents the eaten item
	 * @param i represents the i-value of the current field in the map
	 * @param jrepresents the j-value of the current field in the map
	 */
	private void itemGotEaten(Item item, int i, int j) {
		
		if (item == Item.DOT) {
			map[i][j] = new ItemField(Item.NONE);
			pacman.increasePoints(1);
			currentNumberOfDots--;
			if (currentNumberOfDots < (fruitsInThisLevel-countedFruits)*absoluteNumberOfDots/(fruitsInThisLevel+1)) {
				countedFruits++;
				fruitAvailable = true;
				fruitTimer.start();
			}
			if (currentNumberOfDots == 0) {
				loadNewLevel();
				return;
			}
		}
		
		if (item == Item.BIGDOT) {
			map[i][j] = new ItemField(Item.NONE);
			pacman.increasePoints(20);
			for (Ghost ghost : this.ghosts) {
				switch (ghost.movePattern) {
				case ESCAPE: ghost.escapeTimer.stop(); break;
				case HUNT: ghost.huntTimer.stop(); break;
				case DISPERSE: ghost.disperseTimer.stop(); break;
				case GETOUT: ghost.getoutTimer.stop(); break;
				}
				if (ghost.movePattern != GhostMovePattern.TIMEOUT) {
					ghost.movePattern = GhostMovePattern.ESCAPE;
					ghost.decreaseSpeed();
					ghost.escapeTimer.start();
				}
			}
		}
		
		if (item == Item.FRUIT && fruitAvailable) {
			fruitTimer.stop();
			fruitAvailable = false;
			pacman.increasePoints(50);
		}
		
		if ((pacman.score/scoreNeededToGetNewLife - alreadyGotLifes) > 0) {
			pacman.increaseLifes();
			alreadyGotLifes++;
		}
	}
	
	/**
	 * This method is invoked when a ghost got eaten.
	 * It changes the ghost's move pattern to TIMEOUT and sets his position back to the ghost house where he has to stay for a specified time (depending on the level).
	 * Pacman's points are increased of 100.
	 * @param ghost represents the ghost that got eaten.
	 */
	private void ghostGotEaten(Ghost ghost) {
		ghost.escapeTimer.stop();
		ghost.movePattern = GhostMovePattern.TIMEOUT;
		ghost.position = ghost.startPosition;
		pacman.increasePoints(100);
		ghost.normalizeSpeed();
		ghost.timeoutTimer.start();
	}
	
	/**
	 * This method is invoked when Pacman got eaten.
	 * It stops the game by invoking the method stopGame() and decreases Pacman's lifes of 1. 
	 * 
	 * Then it checks if Pacman has still a life. 
	 * If he still has one all figures are set back to their start positions and the countdown-Timer is started by invoking the method restartGame().
	 * If not the message "Game Over" is displayed.
	 */
	private void pacManGotEaten() {
		stopGame();
		pacman.decreaseLifes();
		if (pacman.lifes > 0) {
			restartGame();
		} else {
			this.gameMessage = "Game Over!";
			this.pacFrame.repaint();
			this.gameOverTimer.start();
		}
	}
	
	/**
	 * This method restarts a Game by reseting all figure's positions and starting the countdown-Timer.
	 */
	private void restartGame() {
		pacman.setPostion(pacmanStartPosition);
		pacman.setDirection(Direction.LEFT);
		for (Ghost ghost : ghosts) ghost.position = ghost.startPosition;
		countdownTimer.start();
	}
	
	/**
	 * This method stops a Game by stoping the move-Timer and all running timers of the ghosts.
	 */
	private void stopGame() {
		moveTimer.stop();
		gameIsRunning = false;
		for(Ghost ghost: this.ghosts) ghost.stopRunningTimer();
	}
	
	/**
	 * This method loads a new level.
	 * First the game is stoped by invoking the method stopGame().
	 * 
	 * The the number of the level is increased of 1 and if the 10th level is not yet reached some values of the figures are update for the next level and all dots are reset.
	 * Then the game is restarted by invoking the method restartGame().
	 * 
	 * If the 10th Level is reached the message "You Win!" is displayed and the game is over.
	 */
	private void loadNewLevel() {
		stopGame();
		
		numberOfLevel++;
		
		if (numberOfLevel < 10) {
			for (Ghost ghost : this.ghosts) ghost.updateToNewLevel(numberOfLevel);
			this.pacman.updateToNewLevel(numberOfLevel);
			resetDots();
			countedFruits = 0;
			this.gameMessage = "Level";
			restartGame();
		} else {
			this.gameMessage = "You Win!";
			this.pacFrame.repaint();
			this.gameOverTimer.start();
		}
	}
	
	/**
	 * This method is invoked when a new level is loaded. It resets all dots are reset.
	 */
	public void resetDots() {
		currentNumberOfDots = absoluteNumberOfDots;
		
		String[] rows = this.mapPlan.split("\n");
		int numOfRows = rows.length;
		int numOfColumns = rows[0].length(); 
		
		for(int i=0; i<numOfRows; i++) {
			for(int j=0; j<numOfColumns; j++) {
				switch (rows[i].charAt(j)) {
				case 'd' : ((ItemField)this.map[i][j]).item = Item.DOT; break;
				case 'D' : ((ItemField)this.map[i][j]).item = Item.BIGDOT; break;
				case 'f' : ((ItemField)this.map[i][j]).item = Item.FRUIT; break;	
				}	
			}
		}
	}
	
	
	public static void main(String[] args) {
		Game game = new Game("wwwwwwwwwwwwwwwwwww\n"+
				"wddddddddwddddddddw\n"+
				"wdwwdwwwdwdwwwdwwDw\n"+
				"wdddddddddddddddddw\n"+
				"wdwwdwdwwwwwdwdwwdw\n"+
				"wddddwdddwdddwddddw\n"+
				"wwwwdwwwnwnwwwdwwww\n"+
				"wwwwdwnnntnnnwdwwww\n"+
				"wwwwdwnwwgwwnwdwwww\n"+
				"cnnndnnwgggwnndnnnc\n"+
				"wwwwdwnwwwwwnwdwwww\n"+
				"wwwwdwnnnfnnnwdwwww\n"+
				"wwwwdwnwwwwwnwdwwww\n"+
				"wddddddddwddddddddw\n"+
				"wDwwdwwwdwdwwwdwwDw\n"+
				"wddwdddddddddddwddw\n"+
				"wwdwdwdwwwwwdwdwdww\n"+
				"wddddwdddwdddwddddw\n"+
				"wdwwwwwwdwdwwwwwwdw\n"+
				"wdddddddddddddddddw\n"+
				"wwwwwwwwwwwwwwwwwww\n",
				new GameController());
	}	
}
