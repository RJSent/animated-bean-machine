public class AnimatedBall {

	protected String path;
	protected int[] position = {0, 0}; //x & y coordinates of ball. FIXME Must be initialized to 0, 0 b/c otherwise flashColor has nullPointer, even when adding a getWasRemoved == true to if statement.
	protected int timesWentLOrR = 0;
	protected boolean wasAdded = false;
	protected boolean wasRemoved = false; //Started to use a wasRemoved instead of {0, 0} to determine if the ball was still "in play" or not. Any instances of {0, 0} to check if it's removed should still work if replaced with wasRemoved.
	
	public AnimatedBall(String path) {
		this.path = path;
	}
	
	public int[] getPosition() {
		return position;
	}
	
	public void addBall () { //no need to pass numCols, b/c path.length() can tell us # of slots, which can be used to find numCols
		position = new int[] {1, (int) ((4 * (path.length() + 1) - 1) / 2)}; //put a ball in the top center of the machine, right underneath the border
		wasAdded = true;
	}
	
	public void dropBall() {
		if (!wasRemoved) {
			position[0] += 1; //lower vertical height (row) by 1.
		}
	}
	
	public void leftOrRight() {
		if (path.charAt((int) (timesWentLOrR / 2)) == 'L') { //divide by two because if path is e.g. LLR, ball will move diagonal left twice at first peg, diagonal left twice at second peg, diagonal right twice at 3rd peg.
			position = new int[] {position[0] + 1, position[1] - 1};
		} else if (path.charAt((int) (timesWentLOrR / 2)) == 'R') {
			position = new int[] {position[0] + 1, position[1] + 1};
		}
		timesWentLOrR += 1;
		//Removal of a ball could possibly be done with use of a try catch block. StringIndexOutOfBounds means it is at the bottom of the grid. When I did this I got weird results, which I was I went with a cleanup() method instead
	}
	
	public void removeBall() {
		position = new int[] {0, 0};
		wasRemoved = true;
	}
	
	public boolean getWasAdded() {
		return wasAdded;
	}
	
	public boolean getWasRemoved() {
		return wasRemoved;
	}
	
}