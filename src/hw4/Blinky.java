package hw4;

import java.util.Random;

import api.Actor;
import api.Descriptor;
import api.Direction;
import api.Location;
import api.MazeCell;
import api.MazeMap;
import api.Mode;
import api.PacmanGame;
import java.lang.Math;

import static api.Direction.RIGHT;
import static api.Direction.UP;
import static api.Direction.DOWN;
import static api.Direction.LEFT;

public class Blinky extends Ghosts {

	/**
	 * Constructs a new Blinky with the given maze, base speed, home direction,
	 * scatter target and random number.
	 * 
	 * I organized the class hierarchy by putting all the methods of the ghosts into
	 * a abstract class and then also making a get chase location for each ghost
	 * since they require a different target and called the method when the mode was
	 * chase.
	 * @param maze
	 * @param home
	 * @param baseSpeed
	 * @param homeDirection
	 * @param scatterTarget
	 * @param rand
	 */
	public Blinky(MazeMap maze, Location home, double baseSpeed, Direction homeDirection, Location scatterTarget,
			Random rand) {

		super(maze, home, baseSpeed, homeDirection,scatterTarget, rand);
	}
	
	
	/**
	 * Gets the chase target for blinky which is just the players location.
	 * @param d
	 * @param scatterTarge
	 */
	@Override
	protected Location getChase(Descriptor d, Location scatterTarget) {
		return d.getPlayerLocation();
		
	}
	
	

	
}
