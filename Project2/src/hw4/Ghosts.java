package hw4;

import static api.Direction.DOWN;
import static api.Direction.LEFT;
import static api.Direction.RIGHT;
import static api.Direction.UP;

import java.util.ArrayList;
import java.util.Random;

import api.Actor;
import api.Descriptor;
import api.Direction;
import api.Location;
import api.MazeMap;
import api.Mode;

public abstract class Ghosts implements Actor {

	/**
	 * Margin of error for comparing exact position to centerline of cell.
	 */
	protected static final double ERR = .001;

	/**
	 * Maze configuration.
	 */
	private MazeMap maze;

	/**
	 * Initial location on reset().
	 */
	private Location home;

	/**
	 * Initial direction on reset().
	 */
	private Direction homeDirection;

	/**
	 * Current direction of travel.
	 */
	private Direction currentDirection;

	/**
	 * Basic speed increment, used to determine currentIncrement.
	 */
	private double baseIncrement;

	/**
	 * Current speed increment, added in direction of travel each frame.
	 */
	private double currentIncrement;

	/**
	 * Row (y) coordinate, in units of cells. The row number for the currently
	 * occupied cell is always the int portion of this value.
	 */
	private double rowExact;

	/**
	 * Column (x) coordinate, in units of cells. The column number for the currently
	 * occupied cell is always the int portion of this value.
	 */
	private double colExact;

	/**
	 * Allows to generate a random number for frightened mode.
	 */
	private Random random;

	/**
	 * Mode the ghost is in.
	 */
	private Mode mode;

	/**
	 * Next location that the ghost is going to.
	 */
	private Location nextLoc;
	/**
	 * Target location for the ghost when it is in scatter mode.
	 */
	private Location scatterTarget;
	/*
	 * If there is a direction change then next direction will be the next direction
	 * which current direction will become.
	 */
	private Direction nextDir;

	
	
	public Ghosts(MazeMap maze, Location home, double baseSpeed, Direction homeDirection, Location scatterTarget,
			Random rand) {
		this.maze = maze;
		this.home = home;
		this.baseIncrement = baseSpeed;
		this.homeDirection = homeDirection;
		this.scatterTarget = scatterTarget;
		currentDirection = homeDirection;
		random = rand;

		rowExact = home.row() + .5;
		colExact = home.col() + .5;
		nextDir = null;
		mode = Mode.INACTIVE;
	}

	/**
	 * Increments the ghost by either the current increment or by the distance to 
	 * center. It increments by distance to center only if the ghost is turning
	 * this is so the ghost will go into the center of the cell. Once it does go into 
	 * the center of the cell it then will change the current direction to the 
	 * next direction. If the ghost ends up going into the next location cell than it
	 * will calculate the next cell.
	 */
	public void update(Descriptor d) {

		if (currentDirection == null || mode == Mode.INACTIVE) {
			return;
		}
		double currentIn = currentIncrement;
		boolean changeDir = false;

		// when direction changes you must check to see if you are at the center
		// of the cell. If you aren't you check to see if the distance to the
		// center is less than the current increment if it is you increment by
		// the distance from the center so you end up in the center of the cell.
		if (nextDir != currentDirection && nextDir != null && distanceToCenter() < currentIncrement) {
			{
				currentIncrement = distanceToCenter();
				changeDir = true;
			}
		}

		if (currentDirection == Direction.UP) {
			rowExact -= currentIncrement;
		} else if (currentDirection == Direction.DOWN) {
			rowExact += currentIncrement;
		} else if (currentDirection == Direction.LEFT) {
			 if (colExact - currentIncrement - 0.5 < 0)
		        {
		          colExact = maze.getNumColumns() + (colExact - currentIncrement - 0.5);
		        }
			 else colExact -= currentIncrement;
		} else if (currentDirection == Direction.RIGHT) {
			  if (colExact + currentIncrement + 0.5 >= maze.getNumColumns())
		        {
		          colExact = colExact + currentIncrement + 0.5 - maze.getNumColumns();
		        }
			  else colExact += currentIncrement;
		}

		if ((int) rowExact == nextLoc.row()
				&& (currentDirection == Direction.UP || currentDirection == Direction.DOWN)) {
			calculateNextCell(d);
		} else if ((int) colExact == nextLoc.col()
				&& (currentDirection == Direction.LEFT || currentDirection == Direction.RIGHT)) {
			calculateNextCell(d);
		}

		setRowExact(rowExact);
		setColExact(colExact);
		if (changeDir == true) {
			currentDirection = nextDir;
			nextDir = null;
			currentIncrement = currentIn;
		}
	}

	
	/**
	 * Calculate what the next cell the ghost will travel to which will also depend on 
	 * the current mode of the ghost. If the ghost is past the midpoint then it will
	 * not do anything. 
	 * @param d
	 */
	public void calculateNextCell(Descriptor d) {

		if (getCurrentLocation() == null || mode == Mode.INACTIVE) {
			return;
		}

		Location currentLoc = getCurrentLocation();

		// check to see if past midpoint
		if (currentDirection == Direction.UP && rowExact - currentLoc.row() < .5) {
			return;
		}
		if (currentDirection == Direction.DOWN && rowExact - currentLoc.row() > .5) {
			return;
		}
		if (currentDirection == Direction.LEFT && colExact - currentLoc.col() > .5) {
			return;
		}
		if (currentDirection == Direction.RIGHT && colExact - currentLoc.col() < .5) {
			return;
		}

		if (mode == Mode.SCATTER) {
			distanceCalculator(scatterTarget);
		}

		else if (mode == Mode.CHASE) {
			distanceCalculator(getChase(d,scatterTarget));
		}

		else if (mode == Mode.FRIGHTENED) {

			ArrayList<Location> notWalls = new ArrayList<>();
			
			if(!maze.isWall(currentLoc.row() - 1, currentLoc.col())) {
				Location up = new Location(currentLoc.row() - 1, currentLoc.col());
				notWalls.add(up);
			}
			else if(!maze.isWall(currentLoc.row() + 1, currentLoc.col())) {
				Location down = new Location(currentLoc.row() + 1, currentLoc.col());
				notWalls.add(down);
			}
			else if(!maze.isWall(currentLoc.row(), currentLoc.col() - 1)) {
				Location left = new Location(currentLoc.row(), currentLoc.col() - 1);
				notWalls.add(left);
			}
			else if(!maze.isWall(currentLoc.row(), currentLoc.col() + 1)) {
				Location right = new Location(currentLoc.row(), currentLoc.col() + 1);
				notWalls.add(right);
			}
			
			int randomPick = random.nextInt(notWalls.size());
			
			Location randomLoc = new Location(notWalls.get(randomPick).row(), notWalls.get(randomPick).col());
			distanceCalculator(randomLoc);


		} else if (mode == Mode.DEAD) {
			distanceCalculator(home);
		}

	}
	
	/**
	 * Returns the next cell.
	 */
	public Location getNextCell() {
		return nextLoc;
	}
	
	/**
	 * Resets the ghost.
	 */
	public void reset() {
		mode = Mode.INACTIVE;
		getCurrentLocation().equals(home);
		setRowExact(home.row() + .5);
		setColExact(home.col() + .5);
		currentIncrement = baseIncrement;
		currentDirection = homeDirection;
	}

	/**
	 * Returns the base increment.
	 */
	public double getBaseIncrement() {
		return baseIncrement;
	}
	
	/**
	 * Returns the current increment of the ghost.
	 */
	public double getCurrentIncrement() {
		return currentIncrement;
	}
	/**
	 * Returns the current direction of the ghost.
	 */
	public Direction getCurrentDirection() {
		return currentDirection;
	}
	/**
	 * Returns the direction of the home.
	 */
	public Direction getHomeDirection() {
		return homeDirection;
	}
	
	/**
	 * Returns the current location of the ghost 
	 */
	public Location getCurrentLocation() {
		return new Location((int) rowExact, (int) colExact);
	}
	
	/**
	 * Returns the location of home.
	 */
	public Location getHomeLocation() {
		return home;
	}
	/**
	 * Sets the mode and enters the calculate next cell method. Also changes the speed
	 * if needed to based on the mode.
	 */
	public void setMode(Mode mode, Descriptor desc) {
		this.mode = mode;
		calculateNextCell(desc);
		if (mode == Mode.FRIGHTENED) {
			currentIncrement = baseIncrement * 2 / 3;
		}

		else if (mode == Mode.DEAD) {
			currentIncrement = baseIncrement * 2;
		}

	}
	/*
	 * returns the current mode of the ghost.
	 */
	public Mode getMode() {
		return mode;
	}
	/*
	 * 
	 */
	public double getRowExact() {
		return rowExact;
	}
	/*
	 * returns the exact column the ghost is on.
	 */
	public double getColExact() {
		return colExact;
	}
	/*
	 * sets the exact row.
	 */
	public void setRowExact(double r) {
		rowExact = r;
	}
	/*
	 * sets the exact column.
	 */
	public void setColExact(double c) {
		colExact = c;
	}

	/*
	 * Sets the direction.
	 */
	public void setDirection(Direction dir) {
		currentDirection = dir;
	}

	
	/*
	 * Finds the closest of the surrounding cells to the target using the distance formula.
	 */
	public void distanceCalculator(Location target) {

		Location currentLoc = getCurrentLocation();

		double lowest = 100;

		double up = Math.sqrt(
				Math.pow(target.row() - (currentLoc.row() - 1), 2) + Math.pow(target.col() - currentLoc.col(), 2));
		double down = Math.sqrt(
				Math.pow(target.row() - (currentLoc.row() + 1), 2) + Math.pow(target.col() - currentLoc.col(), 2));
		double left = Math.sqrt(
				Math.pow(target.row() - currentLoc.row(), 2) + Math.pow(target.col() - (currentLoc.col() - 1), 2));
		double right = Math.sqrt(
				Math.pow(target.row() - currentLoc.row(), 2) + Math.pow(target.col() - (currentLoc.col() + 1), 2));

		// checking up cell calc
		if (lowest > up && currentDirection != Direction.DOWN && !maze.isWall(currentLoc.row() - 1, currentLoc.col())) {
			nextDir = Direction.UP;
			lowest = up;
			nextLoc = new Location(currentLoc.row() - 1, currentLoc.col());
		}
		// checking down cell calc 
		if (lowest > down && currentDirection != Direction.UP && !maze.isWall(currentLoc.row() + 1, currentLoc.col())) {
			nextDir = Direction.DOWN;
			lowest = down;
			nextLoc = new Location(currentLoc.row() + 1, currentLoc.col());
		}
		// checking left cell calc
		if (lowest > left && currentDirection != Direction.RIGHT
				&& !maze.isWall(currentLoc.row(), currentLoc.col() - 1)) {
			nextDir = Direction.LEFT;
			lowest = left;
			nextLoc = new Location(currentLoc.row(), currentLoc.col() - 1);
		}
		// checking right cell calc
		if (lowest > right && currentDirection != Direction.LEFT
				&& !maze.isWall(currentLoc.row(), currentLoc.col() + 1)) {
			nextDir = Direction.RIGHT;
			nextLoc = new Location(currentLoc.row(), currentLoc.col() + 1);
		}

	}

	/**
	 * Determines the difference between current position and center of current
	 * cell, in the direction of travel.
	 */
	private double distanceToCenter() {
		double colPos = getColExact();
		double rowPos = getRowExact();
		switch (getCurrentDirection()) {
		case LEFT:
			return colPos - ((int) colPos) - 0.5;
		case RIGHT:
			return 0.5 - (colPos - ((int) colPos));
		case UP:
			return rowPos - ((int) rowPos) - 0.5;
		case DOWN:
			return 0.5 - (rowPos - ((int) rowPos));
		}
		return 0;
	}
	
	/**
	 * Returns the 
	 * @param d
	 * @param scatterTarget
	 * @return
	 */
	protected abstract Location getChase(Descriptor d, Location scatterTarget);

}
