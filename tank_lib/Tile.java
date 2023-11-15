package tank_lib;

import java.awt.Color;

/**
 * Tile
 */
public interface Tile {
	TileTypes getTileType();

	float getSpeedMultiplier();

	Color getColor();
}
