package client;

import java.io.*;
import java.net.*;

import tank_lib.Point;
import tank_lib.Tank;
import tank_lib.map_lib.Map;

import javax.swing.JFrame;
import java.awt.event.*;
import javax.swing.*;
import tank_lib.*;

/**
 * Client
 */
public class Client {

	public static void main(String[] args) {

		Map m = new Map(20, 20);
		Tank p1 = new Tank(new Point(250, 250), "you");
		Tank p2 = new Tank(new Point(100, 100), "enemy");
		BattleFrame f = new BattleFrame(m, p1, p2);
			f.paint(f.getGraphics());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Client client = new Client();
		// client.startConnection("localhost", 12345);
	}

	public void startConnection(String ip, int port, BattleFrame f) {
		try {
			Socket socket = new Socket(ip, port);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

			f.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyChar() == 'w') {
						f.moveTank(f.p1);
						out.println("START_MOVING");
						System.out.println("START_MOVING");
					} else if (e.getKeyChar() == 's') {
						f.moveTankBack(f.p1);
						out.println("START_BACK");
						System.out.println("START_BACK");
					} else if (e.getKeyChar() == 'a') {
						f.rotateTankLeft(f.p1);
						out.println("START_LEFT");
						System.out.println("START_LEFT");
					} else if (e.getKeyChar() == 'd') {
						f.rotateTankRight(f.p1);
						out.println("START_RIGHT");
						System.out.println("START_RIGHT");
					}
				}

				public void keyReleased(KeyEvent e) {
					if (e.getKeyChar() == 'w') {
						out.println("STOP_MOVING");
						System.out.println("STOP_MOVING");
					} else if (e.getKeyChar() == 's') {
						out.println("STOP_BACK");
						System.out.println("STOP_BACK");
					} else if (e.getKeyChar() == 'a') {
						out.println("STOP_LEFT");
						System.out.println("STOP_LEFT");
					} else if (e.getKeyChar() == 'd') {
						out.println("STOP_RIGHT");
						System.out.println("STOP_RIGHT");
					}
				}
			});

			f.setFocusable(true);
			f.requestFocusInWindow();
			// To receive a message
			String response = in.readLine();
			System.out.println("Server says: " + response);
			while (true) {

				f.paint(f.getGraphics());
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
