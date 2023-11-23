package client;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Client
 */
public class Client {

	public static void main(String[] args) {

		Game game = null;
		String ipString = "localhost";
		String portString = "23456";
		String username = "you";
		try {
			Lobby lobby = new Lobby(ipString, portString, username);
			// aspetta che la lobby si chiuda
			while (lobby.isVisible()) {
				Thread.sleep(100);
			}
			// controlli per vedere se i dati sono stati inseriti correttamente

			ipString = lobby.getIpString();
			portString = lobby.getPortString();
			username = lobby.getUsername();
			System.out.println(ipString + " " + portString + " " + username);

			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(InetAddress.getByName(ipString), Integer.parseInt(portString)), 0);

			game = new Game(socket, username);

			game.start();
			
			game.join();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
