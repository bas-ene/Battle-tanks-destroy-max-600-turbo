package server;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

import tank_lib.settings;
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
			OutputStream out = clientSocket.getOutputStream();
			while (true) {
				// manda la mappa ai client che si connettono DEMO
				Map m = new Map(settings.DEFAULT_MAP_SIZE, settings.DEFAULT_MAP_SIZE);
				ByteBuffer mp = ByteBuffer
						.allocate(4 + 4 + 4 * 2 + 1 * settings.DEFAULT_MAP_SIZE * settings.DEFAULT_MAP_SIZE);
				mp.putInt(4 * 2 + 1 * settings.DEFAULT_MAP_SIZE * settings.DEFAULT_MAP_SIZE);
				mp.put(PacketTypes.SMAP.toString().getBytes());
				mp.putInt(settings.DEFAULT_MAP_SIZE);
				mp.putInt(settings.DEFAULT_MAP_SIZE);
				mp.put(m.bitify());
				System.out.println(m.toString());
				out.write(mp.array());
				out.flush();
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
					// DEMO
					// send the same position, but with a different angle,
					// to test if the client is receiving the packet correctly
					// lunghezza del messaggio - questa lunghezza
					byte bytes[];
					ByteBuffer bb;
					in.read(dataBytes);
					switch (type) {
						case MOVM:
							bytes = new byte[4 + 4 + 8 * 3];
							bb = ByteBuffer.wrap(bytes);
							bb.putInt(8 * 3);
							System.out.println("Received MOVM packet");
							double x = byteBuf.getDouble();
							double y = byteBuf.getDouble();
							double angle = byteBuf.getDouble();
							System.out.println("x: " + x + " y: " + y + " angle: " + angle);
							bb.put(PacketTypes.MOVM.toString().getBytes());
							out.write(bytes);
							out.flush();

							break;
						case SHOT:
							bytes = new byte[4 + 8 * 3];
							bb = ByteBuffer.wrap(bytes);
							//bb.putInt(8 * 3);
							System.out.println("Received SHOT packet");
							double x1 = byteBuf.getDouble();
							double y1 = byteBuf.getDouble();
							double angle1 = byteBuf.getDouble();
							System.out.println("x: " + x1 + " y: " + y1 + " angle: " + angle1);
							bb.put(PacketTypes.SHOT.toString().getBytes());
							out.write(bytes);
							out.flush();
							break;
						default:
							break;
					}

					
					

					// dati
					//bb.putDouble(150);
					//bb.putDouble(100);
					//bb.putDouble(Math.random() * 2 * Math.PI);
					//out.write(bytes);
					//out.flush();
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
