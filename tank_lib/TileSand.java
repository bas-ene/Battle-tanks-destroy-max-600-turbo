package tank_lib;

/**
 * TileSand
 */
public class TileSand implements Tile {
	private TileTypes type = TileTypes.SAND;
	private float speedMultiplier = 0.5f;

	public TileSand() {

	}

	@Override
	public TileTypes getTileType() {
		return type;
	}

	@Override
	public float getSpeedMultiplier() {
		return speedMultiplier;
	}

}
