package tank_lib;

/**
 * TileSand
 */
public class TileSand implements Tile {
	private TileTypes type = TileTypes.SAND;
	private float speedMultiplier = 0.0f;

	public TileSand() {

	}

	public TileTypes getTileType() {
		return type;
	}
}
