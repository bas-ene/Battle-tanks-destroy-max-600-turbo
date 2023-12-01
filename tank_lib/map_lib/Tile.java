package tank_lib.map_lib;

import java.awt.Color;

/**
 * Tile
 */
/**
 * Represents a tile in the map.
 */
public interface Tile {
	/**
	 * Ritorna il {@link TileTypes} della tile.
	 * 
	 * @return il tipo della Tile.
	 */
	TileTypes getTileType();

	/**
	 * Ritorna il moltiplicatore di velocità della tile.
	 * 
	 * @return il moltiplicatore di velocità.
	 */
	float getSpeedMultiplier();

	/**
	 * Ritorna il colore della tile.
	 * 
	 * @return il colore della tile.
	 */
	Color getColor();
}
