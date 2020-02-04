import java.util.Scanner;

public class Quincunx_Sent {

	public static void main(String[] args) {
		Scanner uInput = new Scanner(System.in);      
      
      
		System.out.println("Would you like an animation to play? Y or N");
		if (uInput.nextLine().toUpperCase().equals("Y")) {
			AnimatedBeanMachine beanMachine = new AnimatedBeanMachine();
			beanMachine.printPaths();
			beanMachine.printSlots();
			beanMachine.animate();
			System.out.println("Animation finished. Please close the window to end the program.");
		} else {
			BeanMachine beanMachine = new BeanMachine();
			beanMachine.printPaths();
			beanMachine.printSlots();
		}
	}
	
}