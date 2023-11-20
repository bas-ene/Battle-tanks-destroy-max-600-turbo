package tank_lib.map_lib;

import java.awt.Color;

/**
 * Rappresenta un edificio sulla mappa.
 * Potra` essere distrutto e diventare {@link TileRubble}.
 */
public class TileBuilding implements Tile {
	private TileTypes type = TileTypes.BUILDING;
	private float speedMultiplier = 0.05f;

	/**
	 * Costruttore di default.
	 */
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
