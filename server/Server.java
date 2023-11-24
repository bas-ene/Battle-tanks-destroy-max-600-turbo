package server;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import tank_lib.Bullet;
import tank_lib.Point;
import tank_lib.Tank;
import tank_lib.settings;
import tank_lib.map_lib.Map;
import tank_lib.network.BattlePacket;
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
			ArrayList<Bullet> bullets = new ArrayList<>();
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
			while (true) {
				// per ogni client ricevo il pacchetto e lo mando a tutti gli altri
				// aggiornando la posizione del tank e la loro vita
				for (int i = 0; i < clients.size(); i++) {
					BattlePacket p = clients.get(i).getPacketReceived();

					if (p == null)
						continue;
					System.out.println("Ricevuto pacchetto al secondo: " + System.currentTimeMillis() / 1000);

					// aggiorno la posizione del tank
					if (p.getPacketType() == PacketTypes.MOVM) {
						setTankPosition(tanks.get(i), p);
						System.out.println("Tank " + i + " si è mosso in posizione: " + tanks.get(i).getPosition());
						for (int j = 0; j < clients.size(); j++) {
							if (i == j)
								continue;
							clients.get(j).addPacketToSend(p);
						}
					}
					if (p.getPacketType() == PacketTypes.SHOT) {
						System.out.println("Tank " + i + " ha sparato");
						// manda che il tank i ha sparato
						for (int j = 0; j < clients.size(); j++) {
							if (i == j)
								continue;
							clients.get(j).addPacketToSend(p);
						}
						// aggiungi il proiettile alla lista dei proiettili
						bullets.add(getBulletFromPacket(p));
					}
					
 
						// Check if the tank is hit by a bullet
						System.out.println("Tank " + i + " has health: " + tanks.get(i).getHealth());

						boolean isHit = false;
						for (Bullet bullet : bullets) {
							if (bullet.isInside(tanks.get(i).getPosition())) {
								tanks.get(i).decreaseHealth(bullet.getDamage());
								isHit = true;
								System.out.println("Tank " + i + " is hit by a bullet. New health: " + tanks.get(i).getHealth());
							}
						}
						// If the tank is hit, send a HLTH packet to all players
						if (isHit) {
							System.out.println(12345);
							ByteBuffer byteBufHLTH = ByteBuffer.allocate(12);
							byteBufHLTH.putInt(i);
							byteBufHLTH.putDouble(tanks.get(i).getHealth());
							BattlePacket healthPacket = new BattlePacket(PacketTypes.HLTH, byteBufHLTH.array());
														System.out.println(123456);

							for (int j = 0; j < clients.size(); j++) {
							//	if (i == j)
							//		continue;
								clients.get(j).addPacketToSend(healthPacket);
							}
						}

				}

			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static Bullet getBulletFromPacket(BattlePacket p) {
		ByteBuffer byteBufSHOT = ByteBuffer.wrap(p.getPacketBytes());
		// id, inutilizzato in questo caso
		int id = byteBufSHOT.getInt();
		double x = byteBufSHOT.getDouble();
		double y = byteBufSHOT.getDouble();
		double angle = byteBufSHOT.getDouble();
		Bullet bullet = new Bullet(id, new Point(x, y), angle);
		return bullet;
	}
public static void setTankHealth(Tank tank, BattlePacket hlthPacket) {
						ByteBuffer byteBufHLTH = ByteBuffer.wrap(hlthPacket.getPacketBytes());
						// id, inutilizzato in questo caso
						byteBufHLTH.getInt();
						int health = byteBufHLTH.getInt();
						tank.setHealth(health);
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
