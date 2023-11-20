package tank_lib;

/**
 * Bullet
 */
public class Bullet {
	private int damage = 50;
	private Point position;
	private double directionRadian;
	private String ID;
	private int speed;
	
	public Bullet(String ID, Point position, double direction) {
		this.position = position;
		this.directionRadian = direction;
		this.ID = ID;

		switch (ID) {
			case "01":
				speed = 10;
				damage = 50;
				break;
			case "02":
				speed = 20;
				damage = 100;
				break;
			case "03":
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
