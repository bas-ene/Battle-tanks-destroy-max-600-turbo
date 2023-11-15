package client;

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
		while (true) {
			f.paint(f.getGraphics());
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
