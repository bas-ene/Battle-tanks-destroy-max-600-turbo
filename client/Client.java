package client;

import java.net.InetSocketAddress;
import java.net.Socket;
import tank_lib.Point;
import tank_lib.Tank;
import tank_lib.map_lib.Map;

/**
 * Client
 */
public class Client {

	public static void main(String[] args) {

		Map m = new Map(20, 20);
		Tank p1 = new Tank(new Point(250, 250), "you");
		Tank p2 = new Tank(new Point(100, 100), "enemy");
		BattleFrame f = new BattleFrame(m, p1, p2);
		// f.paint(f.getGraphics());
		// try {
		// Thread.sleep(100);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		try {
			Socket socket = new Socket();
			socket.bind(new InetSocketAddress("localhost", 12345));
			socket.connect(new InetSocketAddress("localhost", 23456), 0);
			Game game = new Game(f, m, p1, p2, new ThreadNetwork(socket));
			ThreadPaint threadPaint = new ThreadPaint(f);
			threadPaint.start();
			game.start();
		} catch (Exception e) {
		}
	}

	// Client client = new Client();
	// client.startConnection("localhost", 12345);
}
