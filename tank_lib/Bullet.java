package tank_lib;

/**
 * Bullet
 */
public class Bullet {
	private int damage = 50;
	private Point position;
	private double directionRadian;

	public Bullet(Point position, double direction) {
		this.position = position;
		this.directionRadian = direction;
	}

}
