package server;

import java.io.*;
import java.net.*;

import client.BattleFrame;
import client.ThreadPaint;
import tank_lib.Point;
import tank_lib.Tank;
import tank_lib.map_lib.Map;

/**
 * Server
 */
public class Server {
	public static void main(String[] args) {
		try {
			// create battleframe
			// Map m = new Map(100, 100);
			// Tank p1 = new Tank(new Point(250, 250), "you");
			// Tank p2 = new Tank(new Point(100, 100), "enemy");
			// BattleFrame f = new BattleFrame(m, p1, p2);
			ServerSocket serverSocket = new ServerSocket(23456);
			System.out.println("Server started and listening on port 23456");

			while (true) {
				Socket clientSocket = serverSocket.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				String request;
				while (true) {
					request = in.readLine();
					if (request == null) {
						continue;
					}
					System.out.println("Client says: " + request);

					switch (request) {
						case "w":
							out.println("You pressed w");
							// f.moveTankForward(p1);

							break;
						case "a":
							out.println("You pressed a");
							// f.rotateTankLeft(p1);
							break;
						case "s":
							out.println("You pressed s");
							// f.moveTankBack(p1);
							break;
						case "d":
							out.println("You pressed d");
							// f.rotateTankRight(p1);
							break;
						case "z":
							// shoot
							break;
						case "esc":
							out.println("You pressed esc");
							break;
						default:
							
							break;
					}
					// To send a message
					// out.println("Hello, client!");
				}
			}
			// serverSocket.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
