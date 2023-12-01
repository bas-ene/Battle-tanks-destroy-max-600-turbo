package tank_lib.map_lib;

import java.awt.Color;

/**
 * Rappresenta una tile di sabbia nella mappa.
 */
public class TileSand implements Tile {
	private TileTypes type = TileTypes.SAND;
	private float speedMultiplier = 0.5f;

	/**
	 * Costruttore di default.
	 */
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

	@Override
	public Color getColor() {
		return Color.YELLOW;
	}
}
