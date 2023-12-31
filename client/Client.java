package client;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Client
 */
public class Client {

	/**
	 * Il main si occupa di inizializzare la lobby, aspetta che questa si chiuda,
	 * riceve l'indirizzo IP, la porta e lo username dalla lobby,
	 * stabilisce una connessione tramite socket, crea e avvia il gioco,
	 * aspetta che il gioco finisca e crea la finestra di fine gioco.
	 */
	public static void main(String[] args) {

		Game game = null;
		String ipString = "localhost";
		String portString = "23456";
		String username = "you";
		try {
			LobbyFrame lobby = new LobbyFrame(ipString, portString, username);
			lobby.setVisible(true);

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
			lobby.dispose();

			game = new Game(socket, username);
			game.start();
			game.join();
			int winnerID = game.getWinnerID();
			int playerID = game.getPlayerID();
			EndGameFrame endGameFrame = new EndGameFrame(winnerID, playerID);
			endGameFrame.setVisible(true);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
