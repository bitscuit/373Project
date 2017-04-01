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
		String header = "Layer7";
		message = header + "|" + message;
		System.out.println("Message at Layer 7: " + message);
		callPresentLayer();
	}

	/**
	 * Adds the presentation layer's header
	 */
	public static void callPresentLayer() {
		// Presentation layer header
		String header = "Layer6";
		message = header + "|" + message;
		System.out.println("Message at Layer 6: " + message);
		callSessionLayer();
	}

	/**
	 * Adds the session layer's header
	 */
	public static void callSessionLayer() {
		// Session layer header
		String header = "Layer5";
		message = header + "|" + message;
		System.out.println("Message at Layer 5: " + message);
		callTransportLayer();
	}

	/**
	 * Adds the transport layer's header
	 * The port number is assumed
	 */
	public static void callTransportLayer() {
		// Transport layer header
		String header = "Layer4 Port: 80";
		message = header + "|" + message;
		System.out.println("Message at Layer 4: " + message);
		callNetworkLayer();
	}

	/**
	 * Adds the network layer's header
	 * The IP address is assumed
	 */
	public static void callNetworkLayer() {
		// Network layer header
		String header = "Layer3 IP Address: 192.168.0.1";
		message = header + "|" + message;
		System.out.println("Message at Layer 3: " + message);
		callDataLayer();
	}

	/**
	 * Adds the data layer's header and trailer
	 */
	public static void callDataLayer() {
		// Data Link layer header
		String header = "Layer2";
		String trailer = "Layer2 Trailer";
		message = header + "|" + message + "|" + trailer;
		System.out.println("Message at Layer 2: " + message);
		callPhysicalLayer();
	}

	/**
	 * Converts the message with all the headers from previous layers into
	 * a bit stream. The "|" that were added from previous layers were only for
	 * convenience and are not converted to a bit stream. Instead they are used to 
	 * segment the bit stream in blocks of 8 (byte size) for readability purposes.
	 */
	public static void callPhysicalLayer() {
		char[] ch = message.toCharArray();
		String bitstring;
		System.out.println("Bit stream is: ");
		String size = "";
		// for each character convert it to the binary ascii representation unless
		// it is "|"
		for (char c : ch) {
			if (c == '|') {
				System.out.print("| ");
			} else {
				// convert to string to binary representation
				String s = Integer.toBinaryString((int) c);
				// pad string with 0's until 8 bits to ensure size is uniform 
				bitstring = String.format("%08d", Integer.valueOf(s));
				size += bitstring;
				System.out.print(bitstring + " ");
			}
		}
		// size of entire message in bits
		System.out.println(System.lineSeparator() + "Message size is: " + size.length() + " bits");
	}

}
