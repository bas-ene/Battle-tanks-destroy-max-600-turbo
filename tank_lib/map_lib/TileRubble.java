package tank_lib.map_lib;

import java.awt.Color;

/**
 * Rappresenta una tile di macerie nella mappa.
 */
public class TileRubble implements Tile {
	private TileTypes type = TileTypes.RUBBLE;
	private float speedMultiplier = 0.75f;

	/**
	 * Costruttore di default.
	 */
	public TileRubble() {

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
		return Color.ORANGE;
	}
}
