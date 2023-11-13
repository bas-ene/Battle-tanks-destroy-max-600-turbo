package tank_lib;

import java.util.ArrayList;

/**
 * Map
 */
public class Map {
	private Tile[][] map;

	public Map() {
		this.map = generateMap(10, 10);
	}

	public Tile[][] generateMap(int nRows, int nCols) {
		Tile[][] m = new Tile[nRows][nCols];
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				m[i][j] = new TileUnknown(m);
			}
		}
		Point spawnPointP1 = new Point((int) (Math.random() * 10), (int) (Math.random() * 10));
		Point spawnPointP2 = new Point((int) (Math.random() * 10), (int) (Math.random() * 10));
		m[(int) spawnPointP1.getX()][(int) spawnPointP1.getY()] = new TileGrass();
		m[(int) spawnPointP2.getX()][(int) spawnPointP2.getY()] = new TileGrass();
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				if (m[i][j].getTileType() != TileTypes.UNKNOWN)
					continue;
				TileTypes[] possibleTipes = ((TileUnknown) m[i][j]).canBePlaced(i, j);
				TileTypes t = possibleTipes[(int) (Math.random() * possibleTipes.length)];
				switch (t) {
					case TileTypes.GRASS:
						m[i][j] = new TileGrass();
						break;
					case TileTypes.BUILDING:
						m[i][j] = new TileBuilding();
						break;
					case TileTypes.SAND:
						m[i][j] = new TileSand();
						break;
					default:
						break;
				}
			}
		}
		return m;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s = "";
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				s += map[i][j].getTileType() + " ";
			}
			s += "\r\n";
		}
		return s;
	}
}
