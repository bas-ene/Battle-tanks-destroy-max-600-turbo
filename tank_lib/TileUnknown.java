package tank_lib;

import java.util.ArrayList;

/**
 * TileUnknown
 */
public class TileUnknown implements Tile {
	private TileTypes type = TileTypes.UNKNOWN;
	private float speedMultiplier = 0.0f;
	private ArrayList<TileTypes> possibleTileType;
	private Map map;

	public TileUnknown(Map map) {
		this.map = map;

		possibleTileType = new ArrayList<>();

		possibleTileType.add(TileTypes.GRASS);
		possibleTileType.add(TileTypes.BUILDING);
		possibleTileType.add(TileTypes.SAND);
	}

	public ArrayList<TileTypes> canBePlaced(int xPos, int yPos) throws Exception {
		var tps = map.getSquare(xPos, yPos);
		var possibleTypes = new ArrayList<TileTypes>();
		possibleTypes.add(TileTypes.GRASS);
		boolean couldBeBuilding = true;
		boolean couldBeSand = true;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (tps[i][j] == TileTypes.SAND) {
					couldBeBuilding = false;
				}
				if (tps[i][j] == TileTypes.BUILDING) {
					couldBeSand = false;
				}
			}
		}
		if (couldBeBuilding)
			possibleTypes.add(TileTypes.BUILDING);
		if (couldBeSand)
			possibleTypes.add(TileTypes.SAND);
		return possibleTypes;

	}

	public TileTypes getTileType() {
		return this.type;
	}
}
