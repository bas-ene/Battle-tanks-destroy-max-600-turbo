package tank_lib;

import java.util.ArrayList;

/**
 * Map
 */
public class Map {
	private Tile[][] map;

	public Map() {
		try {
			map = new Tile[10][10];
			generateMap(10, 10);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			map = null;
		}
	}

	public void generateMap(int nRows, int nCols) throws Exception {
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				map[i][j] = new TileUnknown(this);
			}
		}
		Point spawnPointP1 = new Point((int) (Math.random() * 10), (int) (Math.random() * 10));
		Point spawnPointP2 = new Point((int) (Math.random() * 10), (int) (Math.random() * 10));
		map[(int) spawnPointP1.getX()][(int) spawnPointP1.getY()] = new TileGrass();
		map[(int) spawnPointP2.getX()][(int) spawnPointP2.getY()] = new TileGrass();
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				if (map[i][j].getTileType() != TileTypes.UNKNOWN)
					continue;
				ArrayList<TileTypes> possibleTipes = ((TileUnknown) map[i][j]).canBePlaced(i, j);
				TileTypes t = possibleTipes.get((int) (Math.random() * possibleTipes.size()));
				this.buildTile(i, j, t);
			}
		}
	}

	private void buildTile(int x, int y, TileTypes type) {
		switch (type) {
			case TileTypes.GRASS:
				this.map[x][y] = new TileGrass();
				break;
			case TileTypes.BUILDING:
				this.map[x][y] = new TileBuilding();
				break;
			case TileTypes.SAND:
				this.map[x][y] = new TileSand();
				break;
			case TileTypes.RUBBLE:
				this.map[x][y] = new TileRubble();
			default:
				break;
		}

	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s = "";
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				s += map[i][j].getTileType() + "   ";
			}
			s += "\r\n";
		}
		return s;
	}

	public TileTypes[][] getSquare(int i, int j) throws Exception {
		System.out.println("TEST\n");
		System.out.println(map.length);
		if (i < 0 || j < 0 || i >= map.length || j >= map[0].length)
			throw new Exception("Cell not valid");
		System.out.println("SIUM");
		var square = new TileTypes[][] {
				{ TileTypes.UNKNOWN, TileTypes.UNKNOWN, TileTypes.UNKNOWN },
				{ TileTypes.UNKNOWN, map[i][j].getTileType(), TileTypes.UNKNOWN },
				{ TileTypes.UNKNOWN, TileTypes.UNKNOWN, TileTypes.UNKNOWN }
		};
		// top
		if (i > 0) {
			square[0][1] = map[i - 1][j].getTileType();
		}
		// bottom
		if (i < map.length - 1) {
			square[2][1] = map[i + 1][j].getTileType();
		}
		// left
		if (j > 0) {
			square[1][0] = map[i][j - 1].getTileType();
		}
		// right
		if (j < map[0].length - 1) {
			square[1][2] = map[i][j + 1].getTileType();
		}
		// top left
		if (i > 0 && j > 0) {
			square[0][0] = map[i - 1][j - 1].getTileType();
		}
		// top right
		if (i > 0 && j < map[0].length - 1) {
			square[0][2] = map[i - 1][j + 1].getTileType();
		}
		// bottom left
		if (i < map.length - 1 && j > 0) {
			square[2][0] = map[i + 1][j - 1].getTileType();
		}
		// bottom right
		if (i < map.length - 1 && j < map[0].length - 1) {
			square[2][2] = map[i + 1][j + 1].getTileType();
		}

		return square;
	}
}
