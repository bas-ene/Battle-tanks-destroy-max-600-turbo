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
			System.out.println("Server started and listening on port 12345");

			while (true) {
				Socket clientSocket = serverSocket.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

				String request;
				while ((request = in.readLine()) != null) {
					System.out.println("Client says: " + request);
					// To send a message
					out.println("Hello, client!");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
