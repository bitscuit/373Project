package task1;

import java.util.Scanner;

/*
 * This class implements the OSI model with the data flowing down
 */
public class OSI {
	
	public static String inputMessage;
	public static int output;
	
	public static void main(String[] args) {
		callAppLayer();
	}
	
	/*
	 * Method to implement OSI model's Application Layer
	 */
	public static void callAppLayer() {
		System.out.println("Please enter a message");
		// get user input and convert to binary number
		Scanner in = new Scanner(System.in);
		String message = in.nextLine();
		inputMessage = message;
		callPresentLayer(message);
	}
	
	/*
	 * Method to implement OSI model's Presentation Layer
	 */
	public static void callPresentLayer(String message) {
		// Not needed for task 1. Dummy 64 bit header is added
		
	}
	
	public static void callSessionLayer() {
		
	}
	
	public static void callTransportLayer() {
		
	}
	
	public static void callNetworkLayer() {
		
	}
	
	public static void callDataLayer() {
		
	}
	
	public static void callPhysicalLayer() {
		
	}
	
}
