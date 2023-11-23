package server;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import tank_lib.Point;
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
			// inizializzazione serversocket
			ServerSocket serverSocket = new ServerSocket(23456);
			System.out.println("Server started and listening on port 23456");
			// lista dei client
			ArrayList<TcpClientThread> clients = new ArrayList<>();
			// lista dei tank
			ArrayList<Tank> tanks = new ArrayList<>();
			// init clients and map
			Map map = new Map(settings.DEFAULT_MAP_SIZE, settings.DEFAULT_MAP_SIZE);
			// inizializza i client
			System.out.println("In attesa che: " + settings.NUMBER_OF_CLIENTS + " clients si connettano...");
			for (int i = 0; i < settings.NUMBER_OF_CLIENTS; i++) {
				// aspetta che un client si connetta e poi mandagli/ricevi le informazioni
				// necessarie
				clients.add(new TcpClientThread(serverSocket.accept()));
				System.out.println("Client " + i + " connesso");
				clients.get(i).start();
				tanks.add(new Tank(map.getSpawnPoint(i), clients.get(i).getTankUsername()));
				System.out.println("Client " + i + " ha username: " + tanks.get(i).getUsername());
				tanks.get(i).setID(i);
				clients.get(i).sendIDs(i, settings.NUMBER_OF_CLIENTS);
				clients.get(i).sendMap(map);
				System.out.println("Mandato la mappa al client  " + i);
			}
			// dopo che si sono connessi tutti, invio il pacchetto che identifica l'inizio
			// della partita
			for (int i = 0; i < settings.NUMBER_OF_CLIENTS; i++) {
				clients.get(i).sendStartPacket();
			}
			System.out.println("Inviato pacchetto di inizio partita");
			// scambio di messaggi per il gioco
			// DEMO => il primo clietn mandato si muove al secondo
			while (true) {
				// per ogni client ricevo il pacchetto e lo mando a tutti gli altri
				// aggiornando la posizione del tank e la loro vita
				for (int i = 0; i < clients.size(); i++) {
					BattlePacket p = clients.get(i).getPacketReceived();
					System.out.println("Ricevuto pacchetto al secondo: " + System.currentTimeMillis() / 1000);

					if (p == null)
						continue;
					// aggiorno la posizione del tank
					if (p.getPacketType() == PacketTypes.MOVM) {
						setTankPosition(tanks.get(i), p);
						System.out.println("Tank " + i + " si è mosso in posizione: " + tanks.get(i).getPosition());
					}
					for (int j = 0; j < clients.size(); j++) {
						if (i == j)
							continue;
						clients.get(j).addPacketToSend(p);
					}
				}

			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void setTankPosition(Tank tank, BattlePacket movmPacket) {
		ByteBuffer byteBufMOVM = ByteBuffer.wrap(movmPacket.getPacketBytes());
		// id, inutilizzato in questo caso
		byteBufMOVM.getInt();
		double x = byteBufMOVM.getDouble();
		double y = byteBufMOVM.getDouble();
		double angle = byteBufMOVM.getDouble();
		tank.setPosition(new Point(x, y));
		tank.setRotation(angle);
	}
}
