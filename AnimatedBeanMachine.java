import java.util.*;
import java.io.*;
import java.awt.*;

public class AnimatedBeanMachine extends BeanMachine {
	protected char[][] grid;
	final char border = '*';
	final char centerPeg = '&';
	final char peg = '^';
	final char ball = '%';
	final char blank = ' ';
	final char ballLanded = '$';
	int numRows, numCols;
	protected AnimatedBall[] animatedBalls;
	
	
	public AnimatedBeanMachine() {
		super();
		animatedBalls = new AnimatedBall[paths.length]; //Create an array of AnimatedBalls, then fill each AnimatedBall object with its path. Earlier code can (and probably should) be refactored to have a Ball class that AnimatedBall inherits from with path variable, but I don't want to risk breaking it.
		for (int i = 0; i < paths.length; i++) {
			animatedBalls[i] = new AnimatedBall(paths[i]);
		}
		numCols = 4 * slots.length - 1; //Each slot takes up 3 spaces, with 1 space of seperation. (So 4 slots means 4 * 3 spaces, with 3 "seperation" spaces)
		numRows = 4 * slots.length; //With 4 slots, there are 3 rows of pegs that each take up 3 rows, with 1 vertical space of seperation between pegs, then 3 empty spaces above the first peg, and 2 spaces for the borders. This simplifies to 4 * slots.length
		grid = new char[numRows][numCols]; //create a grid with the above dimensions
		for (char[] column: grid) {
			Arrays.fill(column, blank);
		}
	}
	
	public void animate() {
		DrawingPanel panel = new DrawingPanel(numCols, numRows);
		panel.zoom(30);
		Graphics g = panel.getGraphics();
		setup(g);
	
		do { //Continue animating until there are no balls in the machine.
			lowerBallHeight();
			updateBalls(g);
			flashColor();
			cleanup(); //This winds up being called whenever a ball is at the bottom of a slot
			lowerBallHeight();
			updateBalls(g);
			flashColorCleanup();
			leftOrRight();
			updateBalls(g);
			leftOrRight();
			addBalls(); //put at bottom for flow of animation. If at top, it is added but doesn't immediately appear, meaning it appears 1 space below where it should. This of course is less efficient for the above codes, but the sleep in redraw is more signifigant than any optimization that can be done. 
			updateBalls(g);
		} while(containsBall());
	}
	
	private void addBalls() { //adds first ball that wasn't already added.
		boolean addedABall = false;
		for (int i = 0; i < animatedBalls.length && addedABall == false; i++) {
			if (animatedBalls[i].getWasAdded() == false && animatedBalls[i].getWasRemoved() == false) {
				animatedBalls[i].addBall();
				addedABall = true;
			}
		}
	}
	
	private void cleanup() {//Removes any balls that are at the bottom of a slot.
		for (int i = 0; i < animatedBalls.length ; i++) {
			if(animatedBalls[i].getPosition()[0] == numRows - 2 && animatedBalls[i].getWasRemoved() == false) {
				animatedBalls[i].removeBall();
			}
		}
	}
	
	private boolean containsBall() { //returns true or false if the grid contains a ball in it.
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j] == ball) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void flashColor() { //used to make a short flash of green when ball lands at bottom of slot
		for (int i = 0; i < animatedBalls.length ; i++) {
			if(animatedBalls[i].getPosition()[0] == numRows - 2 && animatedBalls[i].getWasRemoved() == false) {
				grid[animatedBalls[i].getPosition()[0] + 1][animatedBalls[i].getPosition()[1]] = ballLanded;
			}
		}
	}
	
	private void flashColorCleanup() { //turns lit squares back into normal squares.
		for (int j = 0; j < grid[numRows - 2].length; j++) {
			if (grid[numRows - 1][j] == ballLanded) {
				grid[numRows - 1][j] = border;
			}
		}
	}
	
	private void leftOrRight() { //moves a ball left or right depending on its path and how many times it already moved.
		for (int i = 0; i < animatedBalls.length && animatedBalls[i].getWasAdded() == true; i++) {
			if(animatedBalls[i].getWasRemoved() == false) {	
				animatedBalls[i].leftOrRight();
			}
		} 
	}
	
	private void lowerBallHeight() { //lowers the height of all added balls by 1
		for (int i = 0; i < animatedBalls.length && animatedBalls[i].getWasAdded() == true; i++) {
			if(animatedBalls[i].getWasRemoved() == false) {
				animatedBalls[i].dropBall();
			}
		}
	}
	
	private void updateBalls(Graphics g) { //removes all old data for ball positions from grid, then puts new positions into grid. Definitely 100% not an efficient solution, but sleep in redraw consumes far more time.
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j] == ball) {
					grid[i][j] = blank;
				}
			}
		}
		for (int j = 0; j < animatedBalls.length; j++) {
				if (animatedBalls[j].getWasAdded() == true && animatedBalls[j].getWasRemoved() == false) { //if the ball wasn't removed and was added, put it back on the board
					grid[animatedBalls[j].getPosition()[0]][animatedBalls[j].getPosition()[1]] = ball;
				}
		}
		redraw(g);
	}
	
	private void redraw(Graphics g) { //draws and updates the animation. This should be the only method directly interacting with DrawingPanel.
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				switch (grid[i][j]) {
					case blank:
						g.setColor(Color.white);
						g.fillRect(j, i, 1, 1);
						break;
					case ball:
						g.setColor(Color.black);
						g.fillRect(j, i, 1, 1);
						break;
					case peg:
						g.setColor(Color.red);
						g.fillRect(j, i, 1, 1);
						break;
					case centerPeg:
						g.setColor(Color.red);
						g.fillRect(j, i, 1, 1);
						break;
					case border:
						g.setColor(Color.gray);
						g.fillRect(j, i, 1, 1);
						break;
					case ballLanded:
						g.setColor(Color.green);
						g.fillRect(j, i, 1, 1);
				}
			}
		}
		try {
			Thread.sleep(200); //Pauses animation so it doesn't happen too quickly
		} catch (Exception e) {
		}
	}
	
	private void setup(Graphics g) { //set up the board by filling in the slots, pegs, and border
		for (int i = 0; i < grid.length; i++) { //adds border and slots
			for (int j = 0; j < grid[i].length; j++) {
				if (j == 0 || j == numCols - 1 || i == 0 || i == numRows - 1) { // if on border, put the border character in grid
					grid[i][j] = border;
				} else if (i == numRows - 2 && j % 2 == 0) { //put in slots. Border character is used to fill in slots
					grid[i][j] = border;
				}  
			}
		}
		//put in pegs. Find the center of each peg, then make + from center. Logic for this is a bit weird.
		//Start at same position where ball drops (1 below top border, centered vertically. The distance from here to the first center peg is 4.
		//Then continue this for every peg. The 2nd row of pegs (2 pegs) will be distance 8, the 3rd twelve, and so on.
		//Divide this distance by 4 to get the number of pegs per row. If the answer is a decimal (e.g. .75), there is no center peg located there.
		//Then, if number of pegs is odd, put center peg in center column at current row, then centers for other pegs at multiples of 4 from center peg.
		//If number of pegs is even, go two to the left and right and  put centers there. Then continue going out 4 steps each direction until number of pegs is filled.
		for (int i = 1; i < grid[0].length; i++) { //start at top and work down, i represents number of steps down from where we drop the ball (1 row below border, or grid[0].length)
			if ((grid[0].length - 1 - i) % 4 == 0) {
				int numOfPegs = (grid.length - 1 - i) / 4;
				if (numOfPegs % 2 != 0) { //if odd # of pegs
					int posOfCenterPeg = numCols / 2;
					grid[grid[0].length - i][posOfCenterPeg] = centerPeg;
					for (int j = 1; j <= (int) numOfPegs / 2; j++) {
						grid[grid[0].length - i][posOfCenterPeg + 4 * j] = centerPeg;
						grid[grid[0].length - i][posOfCenterPeg - 4 * j] = centerPeg;
					}
				} else { //even # of pegs
					for (int j = 1; j <= (int) numOfPegs / 2; j++) {
						int posOfCenter = numCols / 2;
						grid[grid[0].length - i][posOfCenter + 2 + (4 * (j - 1))] = centerPeg; //go out 2 left and right, then 6, then 10, etc.
						grid[grid[0].length - i][posOfCenter - 2 - (4 * (j - 1))] = centerPeg;
					}
				}
			}
		}
		for (int i = 0; i < grid.length; i++) { //Turns center pegs into + shapes. pegs above, below, right, and left.
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j] == centerPeg) {
					grid[i - 1][j] = peg;
					grid[i + 1][j] = peg;
					grid[i][j - 1] = peg;
					grid[i][j + 1] = peg;
				} 
			}
		}
		redraw(g);			
	} //end of setup
	
}
