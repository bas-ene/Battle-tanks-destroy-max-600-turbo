package server;

import java.io.*;
import java.net.*;

/**
 * Server
 */
public class Server {
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(23456);
			System.out.println("Server started and listening on port 23456");

			while (true) {
				Socket clientSocket = serverSocket.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

				String request;
				while (true) {
					request = in.readLine();
					if (request == null) {
						continue;
					}
					System.out.println("Client says: " + request);
					// To send a message
					out.println("Hello, client!");
				}
			}
			// serverSocket.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
