package tank_lib.map_lib;

import java.awt.Color;

/**
 * TileGrass
 */
public class TileGrass implements Tile {
	private TileTypes type = TileTypes.GRASS;
	private float speedMultiplier = 1.0f;

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