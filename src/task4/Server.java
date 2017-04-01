package task4;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Henry Li, Michael Tanel, Ross Vrana-Godwin
 * Task 4 Server
 * Server sends files from the ServerFiles to client upon request
 * Server must be running before client
 */
public class Server {

	private static ServerSocket conn = null;
	private static Socket dataSkt = null;

	// directory and file are generated if they do not exist
	private static File file1 = new File("ServerFiles", "File1.txt");
	private static File dir = new File("ServerFiles");

	public static void main(String[] args) {
		setupFiles();
		if (setupConnection()) {
			listFiles();
			getClientRequest();
		}
		exit();
	}

	/**
	 * Generates server directory file1 if they do not exist
	 */
	private static void setupFiles() {
		// make new directory for server files if it does not exist
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String s = "Dummy data for file1.";
		if (!(file1.exists())) {
			System.out.println("File 1 doesn't exist");
			// put dummy data in file
			try (BufferedWriter out = 
					new BufferedWriter(new FileWriter("ServerFiles" + File.separator + file1.getName()))) {
				out.write(s);
				out.flush();
			} catch (IOException e) {
				System.err.println("Failed to write to file");
			}
		}
	}

	/**
	 * Sets up server and wait for client to connect
	 * @return true if connection was fully established. False otherwise
	 */
	private static boolean setupConnection() {
		// Define Server Port Number to listen on
		System.out.println("Enter port number");
		Scanner user = new Scanner(System.in);
		String portNum = user.nextLine();
		user.close();
		int servPort = Integer.valueOf(portNum);
		// Server Socket for connection
		try {
			conn = new ServerSocket(servPort);
			System.out.println("Server up");
		} catch (IOException e) {
			System.err.println("Couldn't set up server");
			return false;
		}

		// waits for connection from a client
		System.out.println("Waiting for Connection...");
		// create socket to connect to client
		try {
			dataSkt = conn.accept();
			String hello = getInput();
			if (!hello.trim().equals("HELLO")) {
				return false;
			}
			System.out.println("Received " + hello + " from client");
			String ack = "ACK";
			System.out.println("Sending ACK to client...");
			if (sendOutput(ack.getBytes())) {
				System.out.println("ACK sent");
			}
			System.out.println("Established Connection");
		} catch (IOException e) {
			System.err.println("No connection");
			return false;
		}
		return true;
	}

	/**
	 * List the files that are in the ServerFiles directory, i.e. files available for download
	 */
	private static void listFiles() {
		System.out.println("Sending client list of available files...");
		// get list of available files
		String list = "";
		for (File f : dir.listFiles()) {
			if (f.isFile()) {
				list += f.getName() + " ";
			}
		}
		// send list to client
		byte[] fileList = list.getBytes();
		if (sendOutput(fileList)) {
			System.out.println("File list sent to client");
		} else {
			System.out.println("Failed to send file list to client");
		}
	}

	/**
	 * Gets and parses client request to determine action. Either download files, list files or exit
	 */
	private static void getClientRequest() {
		String request;
		String[] args;
		do {
			// wait for client request
			request = getInput();
			args = request.split("\\s+"); // split string by spaces/tabs, i.e. separate the arguments
			if (args[0].toLowerCase().equals("download")) {
				String filename = args[1].trim();
				// get file contents
				System.out.println("Copying contents of " + filename);
				String file = readFile(filename);
				// checks if file requested is valid
				if (file == null) {
					System.out.println("Client requested invalid file");
					String msg = "-2";
					sendOutput(msg.getBytes());
					continue;
				}
				
				System.out.println("Sending " + filename + " to client...");
				sendFile(file);
				// ensure client received file
				String ack = getInput();
				System.out.println(ack + " received from client");
			} else if (args[0].toLowerCase().equals("list")) { // list available files to download
				listFiles();
			} else {
				break;
			}
		} while (!args[0].toLowerCase().equals("exit")); // exit when client exits
		System.out.println("Exiting");
		return;
	}

	/**
	 * Retrieves whatever is in input stream for the server, i.e. get client output
	 * @return String representation of input stream
	 */
	private static String getInput() {
		// get the input stream
		InputStream in = null;
		BufferedInputStream buffIn = null;
		try {
			in = dataSkt.getInputStream();
			buffIn = new BufferedInputStream(in);
		} catch (IOException e) {
			System.err.println("Failed to get input stream");
		}
		
		// read the input stream
		byte[] b = new byte[1024];
		System.out.println("Waiting for client request...");
		try {
			buffIn.read(b, 0, b.length);
			System.out.println("Client request accepted");
		} catch (IOException e) {
			System.err.println("Could not read client request");
		}
		// convert byte[] to string
		String s = new String(b);
		s = s.trim();
		return s;
	}

	/**
	 * Reads file specified and copies contents into a string
	 * @param s The file name
	 * @return string representation of the file contents
	 */
	private static String readFile(String s) {
		String contents = "";
		File f = new File("ServerFiles", s);
		if (!f.exists()) {
			return null;
		}
		try (BufferedReader reader =
				new BufferedReader(new FileReader("ServerFiles" + File.separator + s))) {
			String line = "";
			while ((line = reader.readLine()) != null) {
				contents += line + System.lineSeparator();
			}
			System.out.println("File contents copied");
		} catch (IOException e) {
			System.err.println("Failed to copy file contents");
		}
		return contents;
	}
	
	/**
	 * Sends the requested file to the client
	 * @param s String representation of file contents
	 */
	private static void sendFile(String s) {
		byte[] fileContent = s.getBytes();
		if (sendOutput(fileContent)) {
			System.out.println("File sent");
		} else {
			System.out.println("Failed to send file");
		}
	}

	/**
	 * Sends message to the server output stream, i.e, send to client
	 * @param b Byte array representation of message
	 * @return True if and only if message was successfully put on server output stream
	 */
	private static boolean sendOutput(byte[] b) {
		OutputStream out = null;
		BufferedOutputStream buffOut= null;
		// get output stream
		try {
			out = dataSkt.getOutputStream();
			buffOut = new BufferedOutputStream(out);
		} catch (IOException e) {
			System.err.print("Could not create output stream");
		}

		// write to output stream
		try {
			buffOut.write(b, 0, b.length);
			buffOut.flush();
			return true;
		} catch (IOException e) {
			System.err.println("Could not send contents to output stream");
		}
		return false;
	}

	/**
	 * Shuts down the server by closing the socket and server socket
	 */
	private static void exit() {
		//Close the Socket
		try {
			dataSkt.close();
		} catch (IOException e1) {
			System.err.println("Failed to close socket");
		}
		//Close connection
		try {
			conn.close();
		} catch (IOException e) {
			System.err.println("Failed to close connection");
		}
	}
}
