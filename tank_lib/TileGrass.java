package tank_lib;

/**
 * TileGrass
 */
public class TileGrass implements Tile {
	private TileTypes type = TileTypes.GRASS;
	private float speedMultiplier = 0.0f;

	public TileGrass() {

	}

	public TileTypes getTileType() {
		return type;
	}
}
