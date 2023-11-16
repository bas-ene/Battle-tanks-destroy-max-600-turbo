package server;
import java.io.*;
import java.net.*;
/**
 * Server
 */
public class Server {
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(12345);
			Socket clientSocket = serverSocket.accept();
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

			// To receive a message
			String request = in.readLine();
			System.out.println("Client says: " + request);

			// To send a message
			out.println("Hello, client!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
