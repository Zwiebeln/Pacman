/**
 * Superclass for the moving figures Pacman and the four Ghosts.
 * The class contains common properties like position, direction and stepsPerMove and provides methods to render a figure's new Position,
 * to set this new position and to set a new direction.
 * @author Mira
 */
public abstract class Figure {
	/**
	 * Contains the position of a figure.
	 */
	protected Position position;
	/**
	 * Contains the direction of a figure.
	 */
	protected Direction direction;
	/**
	 * Contains the wanted direction of a figure. This value has to be validated by the class Game before it can be set as new direction.
	 */
	protected Direction wantedDirection;
	/**
	 * Contains the steps per move of a figure as an integer. This value determines the speed of the figure.
	 */
	protected int stepsPerMove;
	
	
	/**
	 * This method renders the new position of a figure.
	 * At first method determines the wanted direction of a figure and than uses this value to calculate the new position.
	 * This position is modified till it is an a center line of the current field the figure stands on.
	 * @param dotsPerField is representing the size of the fields
	 * @param lastDirectionFailed declares whether the last rendered direction has lead to an invalid position or not
	 * @param pacmanPosition contains Pacman's position, which is used by the ghosts to render their wanted direction (Towards Pacman while they are hunting and in the opposite direction while they are escaping)
	 * @param inFrontOfGhostHouse contains the position right in front of the Ghost's house, which is used by the ghosts to render their wanted direction while they are getting out of their house
	 * @param wantedDirectionOfPacman contains the wanted direction of Pacman
	 * @return The method returns the new wanted position of a figure, which is then to be validated by the class Game.
	 */
	public Position renderPosition(int dotsPerField, Boolean lastDirectionFailed, Position pacmanPosition, Position inFrontOfGhostHouse, Direction wantedDirectionOfPacman) {
		
		if(this instanceof PacMan) {
			if(lastDirectionFailed) {
				wantedDirection = direction;
			} else {
				wantedDirection = wantedDirectionOfPacman;
			}
		} else if (this instanceof Ghost){
			if(lastDirectionFailed) {
				((Ghost)this).countdownToRenderNewDirection = 0;
			}
			wantedDirection = ((Ghost)this).renderDirection(pacmanPosition, inFrontOfGhostHouse);
		}
		
		Position wantedPosition = new Position(position.x, position.y);
		
		int j = wantedPosition.x/dotsPerField;	//j-Wert des Feldes auf dem die Figur aktuell steht (x-Achse)
		int i = wantedPosition.y/dotsPerField; 	//i-Wert des Feldes auf dem die Figur aktuell steht (y-Achse)
		
		if(wantedDirection == Direction.UP) {
			wantedPosition.y -= stepsPerMove;
			while (wantedPosition.x<(j*dotsPerField+dotsPerField/2)) {
				wantedPosition.x++;
			}
			while (wantedPosition.x>(j*dotsPerField+dotsPerField/2)) {
				wantedPosition.x--;
			}
		} else if (wantedDirection == Direction.DOWN) {
			wantedPosition.y += stepsPerMove;
			while (wantedPosition.x<(j*dotsPerField+dotsPerField/2)) {
				wantedPosition.x++;
			}
			while (wantedPosition.x>(j*dotsPerField+dotsPerField/2)) {
				wantedPosition.x--;
			}
		} else if (wantedDirection == Direction.LEFT) {
			wantedPosition.x -= stepsPerMove;
			while (wantedPosition.y<(i*dotsPerField+dotsPerField/2)) {
				wantedPosition.y++;
			}
			while (wantedPosition.y>(i*dotsPerField+dotsPerField/2)) {
				wantedPosition.y--;
			}
		} else if (wantedDirection == Direction.RIGHT) {
			wantedPosition.x += stepsPerMove;
			while (wantedPosition.y<(i*dotsPerField+dotsPerField/2)) {
				wantedPosition.y++;
			}
			while (wantedPosition.y>(i*dotsPerField+dotsPerField/2)) {
				wantedPosition.y--;
			}
		}
		return wantedPosition;
	}
	
	/**
	 * This method sets the specified position as the position of the figure.
	 * @param position contains the position that is to be set
	 */
	public void setPostion(Position position) {
		this.position = position;
	}
	
	/**
	 * This method sets the specified direction as the direction of the figure.
	 * @param direction contains the direction that is to be set
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

}
