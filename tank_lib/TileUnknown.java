package tank_lib;

import java.util.ArrayList;

/**
 * TileUnknown
 */
public class TileUnknown implements Tile {
	private TileTypes type = TileTypes.UNKNOWN;
	private float speedMultiplier = 0.0f;
	private ArrayList<TileTypes> possibleTileType;
	private Tile[][] map;

	public TileUnknown(Tile[][] map) {
		this.map = map;

		possibleTileType = new ArrayList<>();

		possibleTileType.add(TileTypes.GRASS);
		possibleTileType.add(TileTypes.BUILDING);
		possibleTileType.add(TileTypes.SAND);
	}

	public TileTypes[] canBePlaced(int xPos, int yPos) {
		TileTypes[] tps = new TileTypes[] { TileTypes.GRASS, TileTypes.BUILDING, TileTypes.SAND };
		return tps;
	}

	public TileTypes getTileType() {
		return this.type;
	}
}
