package client;

import tank_lib.*;

/**
 * Client
 */
public class Client {
	public static void main(String[] args) {
		Map m = new Map(20, 20);
		Tank p1 = new Tank();
		Tank p2 = new Tank();
		BattleFrame f = new BattleFrame(m, p1, p2);
		f.paint(f.getGraphics());
	}
}
