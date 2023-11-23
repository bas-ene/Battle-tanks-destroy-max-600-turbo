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
	private int width = 10;
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

	public void setId(int ID) {
		this.ID = ID;
	}

	public Bullet(int ID, Point position, double direction) {
		this.position = position;
		this.directionRadian = direction;
		this.ID = ID;
		setBulletType();
	}

	public Bullet(int ID, double x, double y, double direction) {
		Point p = new Point(x, y);
		this.position = p;
		this.directionRadian = direction;
		this.ID = ID;
		setBulletType();
	}

	public Point getPosition() {
		Point p = new Point(position.getX(), position.getY());
		return p;
	}

	public void setPosition(double x, double y) {
		Point p = new Point(x, y);
		this.position = p;
	}

	public double getDirectionRadian() {
		return directionRadian;
	}

	public void setDirectionRadian(double directionRadian) {
		this.directionRadian = directionRadian;
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

	public void move() {
		double x = position.getX() + speed * Math.cos(directionRadian);
		double y = position.getY() + speed * Math.sin(directionRadian);
		position.setX(x);
		position.setY(y);
		System.out.println("speed: " + speed);
	}

}
