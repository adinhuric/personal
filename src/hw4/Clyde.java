package hw4;


import java.util.Random;

import api.Descriptor;
import api.Direction;
import api.Location;
import api.MazeMap;


public class Clyde extends Ghosts{
	
	/**
	 * Constructs a new Clyde with the given maze, base speed, home direction,
	 * scatter target and random number.
	 * @param maze
	 * @param home
	 * @param baseSpeed
	 * @param homeDirection
	 * @param scatterTarget
	 * @param rand
	 */
	public Clyde (MazeMap maze, Location home, double baseSpeed, Direction homeDirection, Location scatterTarget, Random rand) {
		
		super(maze, home, baseSpeed, homeDirection,scatterTarget, rand);
	}
	
	
	/**
	 * Gets the chase target for Clyde which is different base on how far Clyde
	 * is from the player. If the distance is greater than 8 than he will go to
	 * the players location. Else he will go to the scatter target.
	 * @param d
	 * @param scatterTarge
	 */
	@Override
	protected Location getChase(Descriptor d, Location scatterTarget) {
		Location clyde = getCurrentLocation();
		
		double distance = Math.sqrt(
				Math.pow(clyde.row() - d.getPlayerLocation().row(), 2) + Math.pow(clyde.col() - d.getPlayerLocation().col(), 2));
		if(distance > 8 ) {
			return d.getPlayerLocation();
		}
		else return scatterTarget;
		
	}
		
		
}

	

