/**
 * Enumeration of all possible types a field without an item can have.
 * The four possible values are wall, ghoststart, corridor and ghostexit.
 * @author Mira
 */
public enum FieldType {
	/**
	 * Type of a field representing a wall.
	 */
	WALL,
	/**
	 * Type of a field representing the start positions of the ghosts.
	 */
	GHOSTSTART,
	/**
	 * Type of a field representing the corridor pacman can use to get directly from the left side to the right side of the map and the other way round.
	 */
	CORRIDOR,
	/**
	 * Type of a field representing the field directly in front of the ghost's house.
	 */
	GHOSTEXIT
}
