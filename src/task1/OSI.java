package task1;

import java.util.Scanner;

/*
 * This class implements the OSI model with the data flowing down
 */
public class OSI {

	public static String message;
	public static int output;

	public static void main(String[] args) {
		System.out.println("Please enter a message");
		Scanner in = new Scanner(System.in);
		message = in.nextLine();
		callAppLayer();
		in.close();
	}

	/*
	 * Method to implement OSI model's Application Layer
	 */
	public static void callAppLayer() {
		// HTTP header
		String header = "Layer7";
		message = header + "|" + message;
		System.out.println("Message at Layer 7: " + message);
		callPresentLayer();
	}

	/*
	 * Method to implement OSI model's Presentation Layer
	 */
	public static void callPresentLayer() {
		// Presentation layer header
		String header = "Layer6";
		message = header + "|" + message;
		System.out.println("Message at Layer 6: " + message);
		callSessionLayer();
	}

	public static void callSessionLayer() {
		// Session layer header
		String header = "Layer5";
		message = header + "|" + message;
		System.out.println("Message at Layer 5: " + message);
		callTransportLayer();
	}

	public static void callTransportLayer() {
		// Transport layer header
		String header = "Layer4";
		message = header + "|" + message;
		System.out.println("Message at Layer 4: " + message);
		callNetworkLayer();
	}

	public static void callNetworkLayer() {
		// Network layer header
		String header = "Layer3";
		message = header + "|" + message;
		System.out.println("Message at Layer 3: " + message);
		callDataLayer();
	}

	public static void callDataLayer() {
		// Data Link layer header
		String header = "Layer2";
		message = header + "|" + message;
		System.out.println("Message at Layer 2: " + message);
		callPhysicalLayer();
	}

	public static void callPhysicalLayer() {
		char[] ch = message.toCharArray();
		String bitstring;
		for (char c : ch) {
			if (c == '|') {
				System.out.print("| ");
			} else {
				// convert to binary
				String s = Integer.toBinaryString((int) c);
				// pad string with 0's until 8 bits
				bitstring = String.format("%08d", Integer.valueOf(s));
				System.out.print(bitstring + " ");
			}
		}
	}

}
