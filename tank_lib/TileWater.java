package tank_lib;

/**
 * TileWater
 */
public class TileWater implements Tile {
	private TileTypes type = TileTypes.WATER;
	private float speedMultiplier = 0.3f;

	public TileWater() {

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
