package server;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import tank_lib.Bullet;
import tank_lib.Point;
import tank_lib.Tank;
import tank_lib.settings;
import tank_lib.map_lib.Map;
import tank_lib.map_lib.TileTypes;
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
			CopyOnWriteArrayList<Bullet> bullets = new CopyOnWriteArrayList<>();
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
			int tanks_left = settings.NUMBER_OF_CLIENTS;
			// scambio di messaggi per il gioco
			while (tanks_left > 1) {

				// per ogni client ricevo il pacchetto e lo mando a tutti gli altri
				// aggiornando la posizione del tank e la loro vita
				for (int i = 0; i < clients.size(); i++) {
					// muovi proiettili
					CopyOnWriteArrayList<Bullet> remainingBullets = new CopyOnWriteArrayList<>();

					for (Bullet bullet : bullets) {
						bullet.move();
						// checkHit();
						// check if bullet is out of the map

						if (bullet.getPosition().getX() > 0 && bullet.getPosition().getY() > 0 &&
								bullet.getPosition().getX() < map.getWidth() * settings.TILE_SIZE_PX
								&& bullet.getPosition().getY() < map.getHeight() * settings.TILE_SIZE_PX) {
							remainingBullets.add(bullet);
							// System.out.println("bullet added" + map.getWidth() * settings.TILE_SIZE_PX
							// + map.getHeight() * settings.TILE_SIZE_PX);
						} else {
							remainingBullets.remove(bullet);
							System.out.println("Bullet removed");
						}
					}
					bullets = remainingBullets;

					// Check if the tank is hit by a bullet

					boolean isHit = false;
					for (Bullet bullet : bullets) {

						// prints bullet
						// System.out.println("Bullet position: " + bullet.toString());
						Point p_ = new Point(tanks.get(i).getPosition().getX(), tanks.get(i).getPosition().getY());
						if (bullet.getPosition().getX() > tanks.get(i).getPosition().getX()
								&& bullet.getPosition().getX() < tanks.get(i).getPosition().getX()
										+ tanks.get(i).getWidth()
								&& bullet.getPosition().getY() > tanks.get(i).getPosition().getY()
								&& bullet.getPosition().getY() < tanks.get(i).getPosition().getY()
										+ tanks.get(i).getHeight()) {
							tanks.get(i).decreaseHealth(bullet.getDamage());
							isHit = true;
							System.out.println(
									"Tank " + i + " is hit by a bullet. New health: " + tanks.get(i).getHealth());
						}
						// check if the bullet has hit a building
						if (map.getTile_1(new Point(bullet.getPosition().getX(),
								bullet.getPosition().getY() - settings.TITLE_BAR_HEIGHT))
								.getTileType() == TileTypes.BUILDING) {
							Entry<Integer, Integer> tile = map.getTileCoordinates(new Point(bullet.getPosition().getX(),
									bullet.getPosition().getY() - settings.TITLE_BAR_HEIGHT));
							System.out.println("Bullet hit a building");
							map.buildTile(tile.getKey(), tile.getValue(), TileTypes.RUBBLE);
							BattlePacket rubblePacket = buildBDSTPacket(tile.getKey(), tile.getValue());
							// send packet that signals that a building is destroyed
							for (int j = 0; j < clients.size(); j++) {
								if (clients.get(j).isRunning())
									clients.get(j).addPacketToSend(rubblePacket);
							}
							bullets.remove(bullet); // Remove the bullet after hitting a building
						}
					}

					// prints all bullet positions

					// If the tank is hit, send a HLTH packet to all players
					if (isHit) {
						System.out.println(12345);
						System.out.println("HIT!!!!");

						ByteBuffer byteBufHLTH = ByteBuffer.allocate(12);
						byteBufHLTH.putInt(i);
						byteBufHLTH.putDouble(tanks.get(i).getHealth());
						BattlePacket healthPacket = new BattlePacket(PacketTypes.HLTH, byteBufHLTH.array());
						System.out.println(123456);

						for (int j = 0; j < clients.size(); j++) {
							// if (i == j)
							// continue;
							if (clients.get(j).isRunning()) {
								clients.get(j).addPacketToSend(healthPacket);
								System.out.println("Tank " + i + " health sent to tank " + j);
							}
						}
						if (tanks.get(i).getHealth() <= 0) {
							if (!clients.get(i).isRunning())
								continue;
							System.out.println("Tank " + i + " is dead");
							// send him a gend packet, if it is the last one send a gend with its code
							// and close the connection with him
							ByteBuffer byteBufGEND = ByteBuffer.allocate(4);
							if (tanks_left == 1)
								byteBufGEND.putInt(i);
							else {
								byteBufGEND.putInt(-1);
							}
							BattlePacket gendPacket = new BattlePacket(PacketTypes.GEND, byteBufGEND.array());
							clients.get(i).addPacketToSend(gendPacket);
							// close connection with the dead tank
							clients.get(i).closeConnection();
							tanks_left--;
						}
					}

					BattlePacket p = clients.get(i).getPacketReceived();

					if (p == null)
						continue;
					System.out.println("Ricevuto pacchetto al secondo: " + System.currentTimeMillis() / 1000);

					// aggiorno la posizione del tank
					if (p.getPacketType() == PacketTypes.MOVM) {
						setTankPosition(tanks.get(i), p);
						System.out.println("Tank " + i + " si è mosso in posizione: " + tanks.get(i).getPosition());
						for (int j = 0; j < clients.size(); j++) {
							if (i == j || !clients.get(j).isRunning())
								continue;
							clients.get(j).addPacketToSend(p);
						}
					}
					if (p.getPacketType() == PacketTypes.SHOT) {
						System.out.println("Tank " + i + " ha sparato");
						// manda che il tank i ha sparato
						for (int j = 0; j < clients.size(); j++) {
							if (i == j || !clients.get(j).isRunning())
								continue;
							clients.get(j).addPacketToSend(p);
						}
						// aggiungi il proiettile alla lista dei proiettili
						bullets.add(getBulletFromPacket(p));
					}

				}

			}

			// trova il vincitore
			for (int i = 0; i < clients.size(); i++) {
				if (clients.get(i).isRunning()) {
					ByteBuffer byteBufGEND = ByteBuffer.allocate(4);
					byteBufGEND.putInt(i);
					BattlePacket gendPacket = new BattlePacket(PacketTypes.GEND, byteBufGEND.array());
					clients.get(i).addPacketToSend(gendPacket);
					// close connection with the dead tank
					clients.get(i).closeConnection();
				}
			}
			serverSocket.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

	private static BattlePacket buildBDSTPacket(Integer key, Integer value) {
		ByteBuffer byteBufSHOT = ByteBuffer.allocate(2 * Integer.BYTES);
		// id, inutilizzato in questo caso
		byteBufSHOT.putInt(key);
		byteBufSHOT.putInt(value);
		BattlePacket bulletPacket = new BattlePacket(PacketTypes.BDST, byteBufSHOT.array());
		return bulletPacket;
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
