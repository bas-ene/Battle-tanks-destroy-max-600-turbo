package tank_lib;

/**
 * Tank
 */
public class Tank {
	private Point position;
	private float health = 100.0f;
	private boolean canShoot = true;
	private String username;
	private int width = 20;
	private int height = 25;
	private double angleRotationRadian = 0;

	public Tank(Point spawnPoint, String username) {
		this.username = username;
		this.position = spawnPoint;
	}

	public Point getPosition() {
		return position;
	}

	public float getHealth() {
		return health;
	}

	public String getUsername() {
		return username;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void rotateBy(double angleRadian) {
		angleRotationRadian += angleRadian;
	}

	public double getAngleRotationRadian() {
		return angleRotationRadian;
	}

	public void moveBy(double pixels) {
		this.position.moveX(Math.cos(angleRotationRadian) * pixels);
		this.position.moveY(Math.sin(angleRotationRadian) * pixels);

	}
}
