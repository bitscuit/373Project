package task4;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private static ServerSocket conn = null;
	private static Socket dataSkt = null;

	private static File file1 = new File("ServerFiles", "File1.txt");
	private static File dir = new File("ServerFiles");
	
	private static int portNum = 10000;

	public static void main(String[] args) {

		setupFiles();
		setupConnection();
		listFiles();
		String filename = getFileName();
		byte[] file = readFile(filename);
		sendFile(file);

	}
	
	private static void listFiles() {
		System.out.println("Sending client list of available files...");
		OutputStream out = null;
		try {
			out = dataSkt.getOutputStream();
		} catch (IOException e) {
			System.err.print("Could not get socket output stream in listFiles()");
		}
		
		String list = "";
		for (File f : dir.listFiles()) {
			if (f.isFile()) {
				list += f.getName() + " ";
			}
		}
		
		byte[] b = list.getBytes();
		try {
			out.write(b, 0, b.length);
		} catch (IOException e) {
			System.err.println("Could not write filename in listFiles()");
		}
		
		try {
			out.flush();
			System.out.println("File list sent");
		} catch (IOException e) {
			System.err.println("Could not flush output stream in listFiles()");
		}
	}

	// Functional
	private static String getFileName() {
		System.out.println("Waiting for client request...");
		InputStream in = null;
		BufferedInputStream buffIn = null;
		try {
			in = dataSkt.getInputStream();
			buffIn = new BufferedInputStream(in);
		} catch (IOException e) {
			System.err.println("Inputstream failed for filename");
		}
		byte[] b = new byte[10];
		try {
			buffIn.read(b, 0, b.length);
			System.out.println("Request accepted");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Could not read file");
		}
		String s = new String(b);
		s = s.trim();
		return s;
	}

	/**
	 * Method to generate server files if they do not exist yet
	 */
	private static void setupFiles() {
		if (!dir.exists()) {
			dir.mkdirs();
		}
		BufferedWriter out = null;
		String s1 = "This is for file1.";

		if (!(file1.exists())) {
			System.out.println("File 1 doesn't exist");
			try {
				out = new BufferedWriter(new FileWriter("ServerFiles" + File.separator + file1.getName()));
				out.write(s1);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			if (out != null) {
				out.close();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Functional
	private static void setupConnection() {
		//Define Server Port Number to listen on
		int servPort = portNum;
		//Server Socket for connection
		try {
			conn = new ServerSocket(servPort);
			System.out.println("Server up");
		} catch (IOException e) {
			System.err.println("Couldn't set up server");
		}

		//Tells us if server is up and running
		System.out.println("Waiting for Connection...");
		//Socket for accepting message
		try {
			dataSkt = conn.accept();
			System.out.println("Established Connection");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("No connection");
		}
	}

	private static byte[] readFile(String s) {
		System.out.println("Copying contents of file...");
		byte[] data = new byte[8192];
		InputStream in = null;
		BufferedInputStream buffIn = null;
		try {
			in = new FileInputStream("ServerFiles" + File.separator + s);
			buffIn = new BufferedInputStream(in);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			buffIn.read(data, 0, data.length);
			System.out.println("File contents copied");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;

	}

	private static void sendFile(byte[] b) {
		System.out.println("Sending file to client...");
		//Output stream of data to be sent to client
		PrintStream sktOutput = null;
		try {
			sktOutput = new PrintStream(dataSkt.getOutputStream());
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		sktOutput.write(b, 0, b.length);
		sktOutput.flush();
		System.out.println("File sent");
		//Close the Socket
		try {
			dataSkt.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//Close or release connection
		try {
			conn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
