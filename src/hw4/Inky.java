package hw4;

import java.util.Random;

import api.Descriptor;
import api.Direction;
import api.Location;
import api.MazeMap;

public class Inky extends Ghosts {

	/**
	 * Constructs a new Inky with the given maze, base speed, home direction,
	 * scatter target and random number.
	 * @param maze
	 * @param home
	 * @param baseSpeed
	 * @param homeDirection
	 * @param scatterTarget
	 * @param rand
	 */
	public Inky(MazeMap maze, Location home, double baseSpeed, Direction homeDirection, Location scatterTarget, Random rand) {
		
		super(maze, home, baseSpeed, homeDirection,scatterTarget, rand);	
	}
	/**
	 * Gets the chase target for Inky which is based off the direction of the 
	 * player and it saves a location that is 2 cells in front of where he is
	 * facing. It also saves another location which is just Blinkys location.
	 * It the takes the difference in their row and col and it multiplies them
	 * by 2.
	 * @param d
	 * @param scatterTarge
	 */
	@Override
	protected Location getChase(Descriptor d, Location scatterTarget) {
		Location blinky = d.getBlinkyLocation();
		Location newLoc = null;
		if (d.getPlayerDirection() == Direction.UP) {
			Location up = new Location(d.getPlayerLocation().row() - 2, d.getPlayerLocation().col() + 2);
			newLoc = new Location(2*(up.row()-blinky.row()),2*(up.col()-blinky.col()));
			
		} else if (d.getPlayerDirection() == Direction.DOWN) {
			Location down = new Location(d.getPlayerLocation().row() + 2, d.getPlayerLocation().col());
			newLoc = new Location(2*(down.row()-blinky.row()),2*(down.col()-blinky.col()));

		} else if (d.getPlayerDirection() == Direction.LEFT) {
			Location left = new Location(d.getPlayerLocation().row(), d.getPlayerLocation().col() - 2);
			newLoc = new Location(2*(left.row()-blinky.row()),2*(left.col()-blinky.col()));

		} else if (d.getPlayerDirection() == Direction.RIGHT) {
			Location right = new Location(d.getPlayerLocation().row(), d.getPlayerLocation().col() + 2);
			newLoc = new Location(2*(right.row()-blinky.row()),2*(right.col()-blinky.col()));
		}
		
		
		return newLoc;
	}

}
