package tank_lib;

/**
 * TileBuilding
 */
public class TileBuilding implements Tile {
	private TileTypes type = TileTypes.BUILDING;
	private float speedMultiplier = 0.0f;

	public TileBuilding() {

	}

	public TileTypes getTileType() {
		return type;
	}
}
