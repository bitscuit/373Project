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

public class Server {

	private static ServerSocket conn = null;
	private static Socket dataSkt = null;

	private static File file1 = new File("ServerFiles", "File1.txt");
	private static File dir = new File("ServerFiles");

	public static void main(String[] args) {
		setupFiles();
		setupConnection();
		listFiles();
		getClientRequest();
		exit();
	}

	/**
	 * Generates server files if they do not exist yet
	 */
	private static void setupFiles() {
		// make new directory for server files if it does not exist
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String s = "Dummy data for file1.";
		// put dummy data in file
		if (!(file1.exists())) {
			System.out.println("File 1 doesn't exist");
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
	 */
	private static void setupConnection() {
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
		}

		System.out.println("Waiting for Connection...");
		// create socket connect to client
		try {
			dataSkt = conn.accept();
			String hello = getInput();
			System.out.println("Received " + hello + " from client");
			String ack = "ACK";
			System.out.println("Sending ACK to client...");
			if (sendOutput(ack.getBytes())) {
				System.out.println("ACK sent");
			}
			System.out.println("Established Connection");
		} catch (IOException e) {
			System.err.println("No connection");
		}
	}

	/**
	 * List files that are in the ServerFiles directory, i.e. files available for download
	 */
	private static void listFiles() {
		System.out.println("Sending client list of available files...");
		// get list of files
		String list = "";
		for (File f : dir.listFiles()) {
			if (f.isFile()) {
				list += f.getName() + " ";
			}
		}
		// send list
		byte[] fileList = list.getBytes();
		if (sendOutput(fileList)) {
			System.out.println("File list sent to client");
		} else {
			System.out.println("Failed to send file list to client");
		}
	}

	/**
	 * Gets and parses client output to determine action. Either download files or exit
	 */
	private static void getClientRequest() {
		String request;
		String[] args;
		do {
			request = getInput();
			args = request.split("\\s+");
			if (args[0].toLowerCase().equals("download")) {
				String filename = args[1].trim();

				System.out.println("Copying contents of " + filename);
				String file = readFile(filename);
				if (file == null) {
					System.out.println("Client requested invalid file");
					String msg = "-2";
					sendOutput(msg.getBytes());
					continue;
				}

				System.out.println("Sending " + filename + " to client...");
				sendFile(file);
				
				String ack = getInput();
				System.out.println(ack + " received from client");
			} else if (args[0].toLowerCase().equals("list")) {
				listFiles();
				
			} else {
				break;
			}
		} while (!args[0].toLowerCase().equals("exit"));
		System.out.println("Exiting");
		return;
	}

	/**
	 * Retrieves whatever is in input stream for the server, i.e. get client output
	 * @return String representation of input stream
	 */
	private static String getInput() {
		InputStream in = null;
		BufferedInputStream buffIn = null;
		try {
			in = dataSkt.getInputStream();
			buffIn = new BufferedInputStream(in);
		} catch (IOException e) {
			System.err.println("Failed to get input stream");
		}
		byte[] b = new byte[1024];
		System.out.println("Waiting for client request...");
		try {
			buffIn.read(b, 0, b.length);
			System.out.println("Client request accepted");
		} catch (IOException e) {
			System.err.println("Could not read client request");
		}
		String s = new String(b);
		s = s.trim();
		return s;
	}

	/**
	 * Reads file specified by the file name, s, and copies contents into a string
	 * @param s The file name
	 * @return String representation of the file contents
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
	 * Sends message to the server output stream, i.e, send to client input stream
	 * @param b Byte array representation of message
	 * @return True if and only if message was successfully put on server output stream
	 */
	private static boolean sendOutput(byte[] b) {
		OutputStream out = null;
		BufferedOutputStream buffOut= null;
		try {
			out = dataSkt.getOutputStream();
			buffOut = new BufferedOutputStream(out);
		} catch (IOException e) {
			System.err.print("Could not create output stream");
		}

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
