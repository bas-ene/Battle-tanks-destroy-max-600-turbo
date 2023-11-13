package tank_lib;

/**
 * TileRubble
 */
public class TileRubble implements Tile {
	private TileTypes type = TileTypes.RUBBLE;
	private float speedMultiplier = 0.75f;

	public TileRubble() {

	}

	public TileTypes getTileType() {
		return type;
	}
}
