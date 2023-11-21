package server;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import tank_lib.Tank;
import tank_lib.settings;
import tank_lib.map_lib.Map;
import tank_lib.network.BattlePacket;
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
			ArrayList<TcpClientThread> clients = new ArrayList<>();
			ArrayList<Tank> tanks = new ArrayList<>();

			while (true) {
				Map m = new Map(settings.DEFAULT_MAP_SIZE, settings.DEFAULT_MAP_SIZE);

				for (int i = 0; i < settings.NUMBER_OF_CLIENTS; i++) {
					clients.add(new TcpClientThread(serverSocket.accept()));
					clients.get(i).start();
					tanks.add(clients.get(i).getTank());
					clients.get(i).sendIDs(new int[] { 1, 2 });
				}
				// dopo che si sono connessi tutti, invio il pacchetto che identifica l'inizio
				// della partita
				for (int i = 0; i < settings.NUMBER_OF_CLIENTS; i++) {
					clients.get(i).sendStartPacket();
				}
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

					// DEMO
					// send the same position, but with a different angle,
					// to test if the client is receiving the packet correctly
					byte bytes[] = new byte[4 + 4 + 8 * 3];
					// lunghezza del messaggio - questa lunghezza
					ByteBuffer bb = ByteBuffer.wrap(bytes);
					bb.putInt(8 * 3);
					// inserisco il tipo di pacchetto
					bb.put(PacketTypes.MOVM.toString().getBytes());
					// dati
					bb.putDouble(150);
					bb.putDouble(100);
					bb.putDouble(Math.random() * 2 * Math.PI);
					out.write(bytes);
					out.flush();

				}
			}
			// serverSocket.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
