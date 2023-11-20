package server;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import client.BattleFrame;
import client.ThreadPaint;
import tank_lib.Point;
import tank_lib.Tank;
import tank_lib.map_lib.Map;
import tank_lib.network.PacketTypes;

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
			Socket clientSocket = serverSocket.accept();
			InputStream in = clientSocket.getInputStream();
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			while (true) {
				while (true) {

					byte[] pLengthBytes = new byte[4];
					in.read(pLengthBytes);
					int packetLength = ByteBuffer.wrap(pLengthBytes).getInt();
					System.out.println("Received packet length: " + packetLength);
					// Read 4 bytes and convert to a string
					byte[] strBytes = new byte[4];
					in.read(strBytes);
					String typeBytes = new String(strBytes);
					System.out.println("Received packet type: " + typeBytes);
					PacketTypes type = PacketTypes.valueOf(typeBytes);
					byte[] dataBytes = new byte[packetLength];
					ByteBuffer byteBuf = ByteBuffer.wrap(dataBytes);
					in.read(dataBytes);
					switch (type) {
						case MOVM:
							System.out.println("Received MOVM packet");
							double x = byteBuf.getDouble();
							double y = byteBuf.getDouble();
							double angle = byteBuf.getDouble();
							System.out.println("x: " + x + " y: " + y + " angle: " + angle);
							break;

						default:
							break;
					}
					// System.out.println();
					// if (requestLength == null) {
					// continue;
					// }
					// System.out.println("Client says: " + requestLength);

					// switch (requestLength) {
					// case "w":
					// out.println("You pressed w");
					// // f.moveTankForward(p1);

					// break;
					// case "a":
					// out.println("You pressed a");
					// // f.rotateTankLeft(p1);
					// break;
					// case "s":
					// out.println("You pressed s");
					// // f.moveTankBack(p1);
					// break;
					// case "d":
					// out.println("You pressed d");
					// // f.rotateTankRight(p1);
					// break;
					// case "z":
					// // shoot
					// break;
					// case "esc":
					// out.println("You pressed esc");
					// break;
					// default:

					// break;
					// }
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
