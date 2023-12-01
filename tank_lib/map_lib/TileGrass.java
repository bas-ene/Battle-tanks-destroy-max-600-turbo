package tank_lib.map_lib;

import java.awt.Color;

/**
 * Rappresenta una tile di erba nella mappa.
 */
public class TileGrass implements Tile {
	private TileTypes type = TileTypes.GRASS;
	private float speedMultiplier = 1.0f;

	/**
	 * Costruttore di default.
	 */
	public TileGrass() {

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
		return Color.GREEN;
	}
}
