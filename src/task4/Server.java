package task4;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private static ServerSocket conn = null;
	private static Socket dataSkt = null;

	private static File file1 = new File("ServerFiles", "File1.txt");
	private static File file2 = new File("ServerFiles", "File2.txt");
	private static File file3 = new File("ServerFiles", "File3.txt");
	private static int portNum = 10000;

	public static void main(String[] args) {

		setupFiles();
		setupConnection();
		String filename = getFileName();
		byte[] file = readFile(filename);
		sendFile(file);

	}

	// Functional
	private static String getFileName() {
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Could not read filename");
		}
		String s = new String(b);
		return s;
	}

	/**
	 * Method to generate server files if they do not exist yet
	 */
	private static void setupFiles() {
		File dir = new File("ServerFiles");
		if (!dir.exists()) {
			dir.mkdirs();

		}
		BufferedWriter out = null;
		String s1 = "This is for file1.";
		String s2 = "This is for file2.";
		String s3 = "This is for file3.";

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
		if(!file2.exists()) {
			try {
				out = new BufferedWriter(new FileWriter("ServerFiles" + File.separator + file2.getName()));
				out.write(s2);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!file3.exists()) {
			try {
				out = new BufferedWriter(new FileWriter("ServerFiles" + File.separator + file3.getName()));
				out.write(s3);
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
		} catch (IOException e) {
			System.err.println("Couldn't set up connection");
		}

		//Tells us if server is up and running
		System.out.println("Up and Running...Waiting for Connection");
		//Socket for accepting message
		try {
			dataSkt = conn.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Cannot create socket");
		}
		System.out.println("Established Connection");
	}

	private static byte[] readFile(String s) {
		s = s.trim();
		System.out.println(s);
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;

	}

	private static void sendFile(byte[] b) {
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
