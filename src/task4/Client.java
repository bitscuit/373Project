package task4;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	private static InetAddress serverAddr;
	private static int port;
	private static Socket clientSkt = null;

	private static Scanner userInput = new Scanner(System.in);
	private static File dir = new File("ClientFiles");

	public static void main(String[] args) {
		setupDir();
		if (setupConnection()) {
			getFileList();
			sendRequests();
			exit();
		}
	}

	private static void setupDir() {
		// make new directory for server files if it does not exist
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	private static boolean setupConnection() {
		System.out.println("Enter IP address");
		String s = userInput.nextLine();

		// server IP address
		try {
			serverAddr = InetAddress.getByName(s);
		} catch (UnknownHostException e) {
			System.err.println("Could not establish host");
		}
		// server port 
		System.out.println("Enter port number");
		s = userInput.nextLine();
		port = Integer.parseInt(s);

		System.out.println("Setting up connection with server...");
		//Open the socket
		//Socket to connect to the server at the IP address and port
		try {
			clientSkt = new Socket(serverAddr, port);
			String hello = "HELLO";
			System.out.println("Sending HELLO to server...");
			if (!sendOutput(hello.getBytes())) {
				System.err.println("Could not send HELLO. Shutting down");
				return false;
			}
			System.out.println("HELLO sent to server");
			String ack = getInput();
			if (!ack.trim().equals("ACK")) {
				System.err.println("Did not receive ACK frame from server");
				return false;
			}
			System.out.println("Received ACK from server. Connection established");
		} catch (IOException e) {
			System.err.println("Failed to connect to server");
			return false;		
		}
		return true;
	}

	private static boolean getFileList() {
		System.out.println("Getting list of available files from server...");
		String fileList = getInput();
		if (fileList.trim().equals("-1")) {
			return false;
		}
		System.out.println("List of Files: ");
		System.out.println(fileList);
		return true;
	}

	private static void sendRequests() {
		System.out.println("Download file with \"download <file_name>\". List all server files with"
				+ "\"list <file_name>\". Exit with \"exit\"");
		// get command from user input
		String command;
		String[] args;
		String action;

		do {
			System.out.println("Input command");
			command = userInput.nextLine();
			args = command.split("\\s+");
			action = args[0].toLowerCase().trim();
			if (action.equals("exit")) {
				break;
			} else if (action.equals("download")) {
				sendOutput(command.getBytes());
				String fileContent = getInput();
				// invalid file
				if (fileContent.equals("-1")) {
					System.err.println("Unexpected error has occurred. Shutting down");
					break;
				}
				if (fileContent.equals("-2")) {
					System.out.println("File entered was invalid.");
					continue;
				}
				// file has been received, now send ACK to server
				String ack = "ACK";
				sendOutput(ack.getBytes());
				String filename = args[1];
				writeFile(fileContent, filename);

			} else if (action.equals("list")) {
				sendOutput(command.getBytes());
				if (!getFileList()) {
					break;
				}
			} else {
				System.out.println("Invalid command");
			}
			
		} while (true);
	}

	private static void writeFile(String s, String name) {
		try (BufferedWriter out = 
				new BufferedWriter(new FileWriter("ClientFiles" + File.separator + name))) {
			out.write(s);
			out.flush();
		} catch (IOException e) {
			System.err.println("Failed to write to file");
		}
	}

	private static void exit() {
		try {
			clientSkt.close();
		} catch (IOException e) {
			System.err.println("Failed to close socket");
		}
	}

	private static String getInput() {
		InputStream in = null;
		BufferedInputStream buffIn = null;
		try {
			in = clientSkt.getInputStream();
			buffIn = new BufferedInputStream(in);
		} catch (IOException e) {
			System.err.println("Failed to get input stream");
		}
		byte[] b = new byte[1024];
		System.out.println("Waiting for server response...");
		try {
			buffIn.read(b, 0, b.length);
			System.out.println("Server response accepted");
		} catch (IOException e) {
			System.err.println("Could not read server response. Terminating session.");
			return "-1";
		}
		String s = new String(b);
		s = s.trim();
		return s;
	}

	private static boolean sendOutput(byte[] b) {
		OutputStream out = null;
		BufferedOutputStream buffOut= null;
		try {
			out = clientSkt.getOutputStream();
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
}
