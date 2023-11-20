package tank_lib.map_lib;

import java.awt.Color;

/**
 * Rappresenta dell'acqua nella mappa.
 */
public class TileWater implements Tile {
	private TileTypes type = TileTypes.WATER;
	private float speedMultiplier = 0.3f;

	/**
	 * Costruttore di defualt.
	 */
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

	@Override
	public Color getColor() {
		return Color.CYAN;
	}
}
