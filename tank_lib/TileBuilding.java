package tank_lib;

import java.awt.Color;

/**
 * TileBuilding
 */
public class TileBuilding implements Tile {
	private TileTypes type = TileTypes.BUILDING;
	private float speedMultiplier = 0.0f;

	public TileBuilding() {

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
		return Color.GRAY;
	}

}
