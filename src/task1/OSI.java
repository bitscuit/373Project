package task1;

import java.util.Scanner;

/**
 * @author Henry Li, Michael Tanel, Ross Vrana-Godwin
 * Task 1
 * This class implements the OSI model with the data flowing down
 * A message is accepted from the user and headers are added to the message
 * At the physical layer method, the entire message is converted the a bit stream
 */
public class OSI {

	public static String message;

	public static void main(String[] args) {
		System.out.println("Please enter a message");
		// get user input. message must be less than 80 characters
		Scanner user = new Scanner(System.in);
		do {
			System.out.println("Message should be less than 80 characters");
			message = user.nextLine();
		} while (message.length() > 80);
		// begin the process of adding headers
		callAppLayer();
		user.close();
	}

	/**
	 * Adds the application layer's header
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
		String header = "Layer4 Port: 80";
		message = header + "|" + message;
		System.out.println("Message at Layer 4: " + message);
		callNetworkLayer();
	}

	public static void callNetworkLayer() {
		// Network layer header
		String header = "Layer3 IP Address: 192.168.0.1";
		message = header + "|" + message;
		System.out.println("Message at Layer 3: " + message);
		callDataLayer();
	}

	public static void callDataLayer() {
		// Data Link layer header
		String header = "Layer2";
		String trailer = "Layer2 Trailer";
		message = header + "|" + message + "|" + trailer;
		System.out.println("Message at Layer 2: " + message);
		callPhysicalLayer();
	}

	public static void callPhysicalLayer() {
		char[] ch = message.toCharArray();
		String bitstring;
		System.out.println("Bit stream is: ");
		String size = "";
		for (char c : ch) {
			if (c == '|') {
				System.out.print("| ");
			} else {
				// convert to binary
				String s = Integer.toBinaryString((int) c);
				// pad string with 0's until 8 bits
				bitstring = String.format("%08d", Integer.valueOf(s));
				size += bitstring;
				System.out.print(bitstring + " ");
			}
		}
		System.out.println(System.lineSeparator() + "Message size is: " + size.length() + " bits");
	}

}
