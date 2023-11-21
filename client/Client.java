package client;

import java.net.InetSocketAddress;
import java.net.Socket;
import tank_lib.Point;
import tank_lib.Tank;

/**
 * Client
 */
public class Client {

	public static void main(String[] args) {

		// Map m = new Map(20, 20);
		Tank p1 = new Tank(new Point(250, 250), "you");
		Tank p2 = new Tank(new Point(100, 100), "enemy");
		// BattleFrame f = new BattleFrame(m, p1, p2);

		Game game;
		try {
			Socket socket = new Socket();
			socket.bind(new InetSocketAddress("localhost", 12345));
			socket.connect(new InetSocketAddress("localhost", 23456), 0);

			game = new Game(p1, p2, new ThreadNetwork(socket));

			game.start();
			game.join();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// Client client = new Client();
	// client.startConnection("localhost", 12345);
}
