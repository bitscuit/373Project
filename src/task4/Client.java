package task4;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			//Get the IP address of the server
			InetAddress serverAddr = InetAddress.getByName("192.168.0.100");
			//Server Port Number to Send Connection Request to
			int serverTCPport = Integer.parseInt("10000"); //Receive Port Number from CmdLine
			//Open the socket
			//Socket to connect to the server at the IP address and port
			Socket clientSkt = new Socket(serverAddr, serverTCPport);
			//Client Waits for an incoming message from the server
			//Client is blocked until Server replies
			//			BufferedReader br = new BufferedReader(new InputStreamReader(clientSkt.getInputStream()));
			//			System.out.println(br.readLine());

			byte[] data = new byte[53];
			InputStream in = clientSkt.getInputStream();
			BufferedInputStream buffIn = null;
			buffIn = new BufferedInputStream(in);

			buffIn.read(data, 0, data.length);

			File dir = new File("ClientFiles");
			dir.mkdirs();
			BufferedOutputStream out = null;
			try {
				out = new BufferedOutputStream(new FileOutputStream("ClientFiles" + File.separator + "test.txt"));
				out.write(data);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Connection release
			clientSkt.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}