package hw4;

import java.util.Random;

import api.Descriptor;
import api.Direction;
import api.Location;
import api.MazeMap;

public class Pinky extends Ghosts {
	
	/**
	 * Constructs a new Blinky with the given maze, base speed, home direction,
	 * scatter target and random number.
	 * @param maze
	 * @param home
	 * @param baseSpeed
	 * @param homeDirection
	 * @param scatterTarget
	 * @param rand
	 */
	public Pinky(MazeMap maze, Location home, double baseSpeed, Direction homeDirection, Location scatterTarget,
			Random rand) {

		super(maze, home, baseSpeed, homeDirection, scatterTarget, rand);
	}
	/**
	 * Gets the chase target for Pinky which is just 4 cells in front of where the
	 * player is facing. If the direction is up then it is 4 in front and 4 to 
	 * the left.
	 * @param d
	 * @param scatterTarge
	 */
	@Override
	protected Location getChase(Descriptor d, Location scatterTarget) {

		if (d.getPlayerDirection() == Direction.UP) {
			Location up = new Location(d.getPlayerLocation().row() - 4, d.getPlayerLocation().col() - 4);
			return up;
		} else if (d.getPlayerDirection() == Direction.DOWN) {
			Location down = new Location(d.getPlayerLocation().row() + 4, d.getPlayerLocation().col());
			return down;
		} else if (d.getPlayerDirection() == Direction.LEFT) {
			Location left = new Location(d.getPlayerLocation().row(), d.getPlayerLocation().col() - 4);
			return left;
		} else if (d.getPlayerDirection() == Direction.RIGHT) {
			Location right = new Location(d.getPlayerLocation().row(), d.getPlayerLocation().col() + 4);
			return right;
		}
		return null;
	}

}
