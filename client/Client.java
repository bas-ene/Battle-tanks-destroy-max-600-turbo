package client;
import java.io.*;
import java.net.*;
import tank_lib.*;

/**
 * Client
 */
public class Client {
	public static void main(String[] args) {
		/* 
		Map m = new Map(20, 20);
		Tank p1 = new Tank(new Point(250, 250), "you");
		Tank p2 = new Tank(new Point(100, 100), "enemy");
		BattleFrame f = new BattleFrame(m, p1, p2);
		while (true) {
			f.paint(f.getGraphics());
			try {
				Thread.sleep(66);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
		Client client = new Client();
		client.startConnection("localhost", 12345);
	}

	public void startConnection(String ip, int port) {
		try {
			Socket socket = new Socket(ip, port);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

			// To send a message
			out.println("Hello, server!");

			// To receive a message
			String response = in.readLine();
			System.out.println("Server says: " + response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
