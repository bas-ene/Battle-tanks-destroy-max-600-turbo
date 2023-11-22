package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import tank_lib.Tank;
import tank_lib.settings;
import tank_lib.map_lib.Map;
import tank_lib.network.BattlePacket;

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
				BattlePacket p = clients.get(0).getPacketReceived();
				if (p == null)
					continue;
				System.out.println("Ricevuto pacchetto al secondo: " + System.currentTimeMillis() / 1000);
				clients.get(1).addPacketToSend(p);

			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
