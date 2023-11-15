package tank_lib;

import java.awt.Color;

/**
 * TileRubble
 */
public class TileRubble implements Tile {
	private TileTypes type = TileTypes.RUBBLE;
	private float speedMultiplier = 0.75f;

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
