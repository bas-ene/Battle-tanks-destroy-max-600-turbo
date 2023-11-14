package tank_lib;

/**
 * Tile
 */
public interface Tile {
	TileTypes type = TileTypes.UNKNOWN;
	float speedMultiplier = 0.0f;

	TileTypes getTileType();

	float getSpeedMultiplier();
}
