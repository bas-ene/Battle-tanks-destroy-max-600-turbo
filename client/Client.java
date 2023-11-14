package client;

import tank_lib.Map;

/**
 * Client
 */
public class Client {
	public static void main(String[] args) throws Exception {
		Map map = new Map(100, 100);
		map.generateMap(100, 100);
		map.renderMap();

	}
}
