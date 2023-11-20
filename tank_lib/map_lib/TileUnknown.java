package tank_lib.map_lib;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Rappresenta una tile sconosciuta, o meglio il cui tipo non e` ancora stato
 * determinato.
 */
public class TileUnknown implements Tile {
	private TileTypes type = TileTypes.UNKNOWN;
	private float speedMultiplier = 0.0f;
	private Map map;

	/**
	 * Constructor for TileUnknown.
	 *
	 * @param map the map object associated with the tile
	 */
	public TileUnknown(Map map) {
		this.map = map;
	}

	/**
	 * Determina i tipi di tile che possono essere piazzati nella posizione
	 * specificata.
	 * 
	 * @param xPos
	 * @param yPos
	 * @return la lista dei tipi di tile che possono essere piazzati
	 * @throws Exception
	 */
	public ArrayList<TileTypes> canBePlaced(int xPos, int yPos) throws Exception {
		var tps = map.getSquare(xPos, yPos);
		var possibleTypes = new ArrayList<TileTypes>();
		possibleTypes.add(TileTypes.GRASS);
		boolean couldBeBuilding = true;
		boolean couldBeSand = true;
		boolean couldBeWater = true;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (tps[i][j] == TileTypes.SAND || tps[i][j] == TileTypes.WATER) {
					couldBeBuilding = false;
				}
				if (tps[i][j] == TileTypes.BUILDING) {
					couldBeSand = false;
					couldBeWater = false;
				}
			}
		}
		if (couldBeBuilding)
			possibleTypes.add(TileTypes.BUILDING);
		if (couldBeSand)
			possibleTypes.add(TileTypes.SAND);
		if (couldBeWater)
			possibleTypes.add(TileTypes.WATER);
		return possibleTypes;
	}

	@Override
	public TileTypes getTileType() {
		return type;
	}

	@Override
	public float getSpeedMultiplier() {
		return speedMultiplier;
	}

	@Override
	public Color getColor() {
		return Color.BLACK;
	}
}
