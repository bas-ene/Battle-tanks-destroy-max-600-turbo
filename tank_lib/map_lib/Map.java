package tank_lib.map_lib;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import tank_lib.Point;
import tank_lib.settings;

public class Map {
	// ! Matrice che contiene le celle che compongono la mappa
	/**
	 * Questa matrice vera riempita dal costrutttore con i parametri passati al
	 * costruttore (o con quelli di default) da tutte celle UKNOWN, o da decidere,
	 * il cui tipo, tramite un algoritmo di Wave Function Collapse (WFC), verra`
	 * determinato in modo procedurale.
	 *
	 */
	private Tile[][] map;

	private Point[] spawnPoints;

	// ! Costruttore di default
	/**
	 * Costruttore di default, la dimensione della mappa verra` quindi determinata
	 * da le impostazioni in {@link settings}
	 */
	public Map() {
		try {
			map = new Tile[settings.DEFAULT_MAP_SIZE][settings.DEFAULT_MAP_SIZE];
			spawnPoints = new Point[settings.NUMBER_OF_CLIENTS];
			generateMap(10, 10);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			map = null;
		}
	}

	// ! Costruttore parametrico
	/**
	 * Se uno dei parametri e` negativo map sara` null.
	 * 
	 * @param nRows Numero di righe che avra` la mappa
	 * @param nCols Numero di colonne che avra` la mappa
	 */
	public Map(int nRows, int nCols) {
		try {
			map = new Tile[nRows][nCols];
			spawnPoints = new Point[settings.NUMBER_OF_CLIENTS];
			generateMap(nRows, nCols);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			map = null;
		}
	}

	// ! Costruttore parametrico
	/**
	 * Costruttore orientato alla comunicazione tcp, in modo da poter convertire
	 * direttamente un vettore di byte in una mappa.
	 * Se uno dei parametri e` negativo, o se la lunghezza dell'array non e`
	 * corretta, mappa sara` null.
	 * 
	 * @param byteArray Vettore di byte, un byte rappresenta il tipo di una cella
	 * @param nRows     Numero di righe che avra` la mappa
	 * @param nCols     Numero di colonne che avra` la mappa
	 */
	public Map(byte[] byteArray, int nRows, int nCols) {
		if (nRows <= 0 || nCols <= 0 || byteArray.length != nRows * nCols) {
			map = null;
			return;
		}
		map = new Tile[nRows][nCols];
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				switch (byteArray[i * nRows + j]) {
					case 1:
						buildTile(i, j, TileTypes.GRASS);
						break;
					case 2:
						buildTile(i, j, TileTypes.BUILDING);
						break;
					case 3:
						buildTile(i, j, TileTypes.SAND);
						break;
					case 4:
						buildTile(i, j, TileTypes.WATER);
						break;
					default:
						break;
				}
			}
		}

	}

	// ! Genera mappa tramite WFC
	/**
	 * Genera una mappa di dimensioni nRows * nCols tramite WFC, i vincoli sono
	 * definiti in {@link TileUnknown}
	 * 
	 * @param nRows Numero di righe della mappa
	 * @param nCols Numero di colonne della mappa
	 */
	public void generateMap(int nRows, int nCols) throws Exception {
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				map[i][j] = new TileUnknown(this);
			}
		}

		for (int i = 0; i < spawnPoints.length; i++) {
			Point ithSpawnPoint = new Point((int) (Math.random() * this.getHeight()) % this.getHeight(),
					(int) (Math.random() * this.getWidth()) % this.getWidth());
			spawnPoints[i] = ithSpawnPoint;
			map[(int) ithSpawnPoint.getY()][(int) ithSpawnPoint.getX()] = new TileGrass();
		}

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

	// ! Costruisce un tipo di tile in un punto della mappa.
	/**
	 * @param i    Posizione della riga della cella
	 * @param j    Posizione della colonna della cella
	 * @param type Tipo della cella da costruire.
	 */
	private void buildTile(int i, int j, TileTypes type) {
		switch (type) {
			case GRASS:
				this.map[i][j] = new TileGrass();
				break;
			case BUILDING:
				this.map[i][j] = new TileBuilding();
				break;
			case SAND:
				this.map[i][j] = new TileSand();
				break;
			case RUBBLE:
				this.map[i][j] = new TileRubble();
			case WATER:
				this.map[i][j] = new TileWater();
			default:
				break;
		}

	}

	// ! Ritorna una matrice 3*3 che contiene i tipi delle celle attorno a quella
	// posizionata in i,j (compresa).
	/**
	 * @param i Riga della cella.
	 * @param j Colonna della cella.
	 * @throws Exception quando o i o j non sono valide (negative o oltre le
	 *                   dimensioni della mappa)
	 * @return {@link TileTypes}[3][3] Tipo delle cellule vicine,
	 *         {@link TileUnknown} se ai bordi
	 */
	TileTypes[][] getSquare(int i, int j) throws Exception {
		if (i < 0 || j < 0 || i >= map.length || j >= map[0].length)
			throw new Exception("Cell not valid");
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

	/**
	 * @return numero di righe
	 */
	public int getHeight() {
		return map.length;
	}

	/**
	 * 
	 * @return numero di colonne
	 */
	public int getWidth() {
		return map[0].length;
	}

	/**
	 * Ritorna l'oggetto con interfaccia Tile in posizione i,j
	 * 
	 * @param i
	 * @param j
	 * @return Tile in posizione i,j
	 */
	public Tile getTile(int i, int j) {
		return map[i][j];
	}

	public Tile getTile(Point p) {
		if (p.getX() < 0 || p.getY() < 0 || p.getX() > map[0].length * settings.TILE_SIZE
				|| p.getY() > map.length * settings.TILE_SIZE)
			return null;
		return map[(int) ((int) p.getY() / settings.TILE_SIZE)][(int) ((int) p.getX() / settings.TILE_SIZE)];
	}

	public Tile getTile(double x, double y) {
		if (x < 0 || y < 0 || x > map[0].length * settings.TILE_SIZE
				|| y > map.length * settings.TILE_SIZE)
			return null;
		return map[(int) ((int) y / settings.TILE_SIZE)][(int) ((int) x / settings.TILE_SIZE)];
	}

	@Override
	public String toString() {
		String s = "";
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				s += map[i][j].getTileType().toString().charAt(0) + " ";
			}
			s += "\r\n";
		}
		return s;
	}

	// ! Trasforma map in un array di byte per la connessione tcp
	/**
	 * @return byte[]
	 */
	public byte[] bitify() {
		ByteBuffer bb = ByteBuffer.allocate(2 * Integer.BYTES + 2 * Double.BYTES * spawnPoints.length +
				this.getHeight() * this.getWidth());
		bb.putInt(this.getHeight());
		bb.putInt(this.getWidth());
		byte[] bytes = new byte[map.length * map[0].length];
		for (int i = 0; i < this.getHeight(); i++) {
			for (int j = 0; j < this.getWidth(); j++) {
				byte type;
				switch (map[i][j].getTileType()) {
					case GRASS:
						type = (byte) 1;
						break;
					case BUILDING:
						type = (byte) 2;
						break;
					case SAND:
						type = (byte) 3;
						break;
					case WATER:
						type = (byte) 4;
						break;
					default:
						type = (byte) 0;
						break;
				}
				bytes[i * map.length + j] = type;
			}
		}
		bb.put(bytes);
		for (Point p : spawnPoints) {
			bb.put(p.bitify());
		}
		return bb.array();
	}

	public Point getSpawnPoint(int i) {
		if (i < 0 || i > spawnPoints.length)
			return null;
		return spawnPoints[i];
	}
}
