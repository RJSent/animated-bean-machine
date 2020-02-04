import java.util.Scanner;
import java.util.Arrays;

public class BeanMachine {

	protected int[] slots; //1D array that contains the number of balls that landed in different slots
	protected int numBalls;
	protected String[] paths; //1D array that contains the paths each ball takes
	
	public BeanMachine() { //default constructor, prompts user
		Scanner uInput = new Scanner(System.in);
		
		System.out.println("Please enter the number of slots at the bottom of the machine");
		slots = new int[uInput.nextInt()];
		
		System.out.println("Please enter the number of balls you want to drop");
		numBalls = uInput.nextInt();
		paths = new String[numBalls]; //numBalls is paths.length, so we can eliminate numBalls. Left in for clarity.
		Arrays.fill(paths, ""); //Needs to be filled with empty characters, not null, otherwise we will be concatenating L and R with null string, resulting in nullLRL... as the final string. (Odd behavior given that null != "null")
		dropBalls();
	} //end of BeanMachine
	
	public void dropBalls() { //simulates dropping the balls down the machine, then fills the slots array with the number of balls
		for (int i = 0; i < paths.length; i++) {
			for (int j = 0; j < slots.length - 1; j++) { //if we have 5 slots, we have a path of 4. (LLLL through RRRR, if we have RRRRR it goes to slot 6, which doesn't exist)
				if (Math.round(Math.random()) == 0) {
					paths[i] = paths[i] + "L";
				} else {
					paths[i] = paths[i] + "R";
				}
			}
		}
		this.fillSlots();
	} //end of dropBalls
	
	protected void fillSlots() { //fills the slots array with the number of balls in each slot
		for (int i = 0; i < paths.length; i++) {
			slots[numOfRs(paths[i])] += 1;
		}
	}
	
	protected int numOfRs(String onePath) { //calculates the number of Rs in a given path
		int numRs = 0;
		for (int i = 0; i < onePath.length(); i++) {
			if (onePath.charAt(i) == 'R') {
				numRs += 1;
			}
		}
		//System.out.println(numRs); //For debugging
		return numRs;
	}
	
	public void printPaths() { //prints the paths of every ball that was dropped
		for (int i = 0; i < paths.length; i++) {
			System.out.println(paths[i]);
		}
	}
	
	public void printSlots() { //prints the number of balls in each slot as a histogram.
		for (int i = 0; i < slots.length; i++) {
			System.out.print((i + 1) + "  "); //i + 1 because slot[0] is the first slot, not the zeroth.
			for (int j = 0; j < slots[i]; j++) {
				System.out.print("*");
			}
			System.out.println("");
		}
	}
	
} //end of BeanMachine class