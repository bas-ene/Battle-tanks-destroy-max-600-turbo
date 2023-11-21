package tank_lib;

/**
 * Bullet
 */
public class Bullet {
	private int damage = 50;
	private Point position;
	private double directionRadian;
	private int ID;
	private int speed;
	private int width = 30;
	private int height=10;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getId() {
		return ID;
	}

	public Bullet(int ID, Point position, double direction) {
		this.position = position;
		this.directionRadian = direction;
		this.ID = ID;
	}

	public Bullet(int ID, double x, double y, double direction) {
		Point p = new Point(x, y);
		this.position = p;
		this.directionRadian = direction;
		this.ID = ID;
	}

	public Point getPosition() {
		return position;
	}

	public double getDirectionRadian() {
		return directionRadian;
	}

	public void setBulletType() {
		switch (ID) {
			case 01:
				speed = 10;
				damage = 50;
				break;
			case 02:
				speed = 20;
				damage = 100;
				break;
			case 03:
				speed = 30;
				damage = 150;
				break;
			default:
				speed = 40;
				damage = 200;
				break;
		}
	}

}
