package tank_lib;

import java.nio.ByteBuffer;

import tank_lib.network.BattlePacket;
import tank_lib.network.PacketTypes;

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
		Point p = new Point(position.getX(), position.getY());
		this.position = p;
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

	//get the position a little bit forward in the direction of angle
	public Point getForwardPosition() {
		double x = position.getX() + 20 * Math.cos(directionRadian);
		double y = position.getY() + 20 * Math.sin(directionRadian);
		Point p = new Point(x, y);
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
				damage = 10;
				break;
			case 02:
				speed = 20;
				damage = 20;
				break;
			case 03:
				speed = 30;
				damage = 30;
				break;
			default:
				speed = 40;
				damage = 40;
				break;
		}
	}

	public void move() {
		double x = position.getX() + speed * Math.cos(directionRadian);
		double y = position.getY() + speed * Math.sin(directionRadian);
		position.setX(x);
		position.setY(y);
	}

	

	public int getDamage() {
		return damage;
	}

	public BattlePacket getPacket(){
			System.out.println("SPARO");
			// send the bullet to the server
			// tipo SHOT
			// 1 int per id, 2 double per posizione, 1 per angolo
			byte[] bytes = new byte[Integer.BYTES + Double.BYTES * 3];
			ByteBuffer byteBuf = ByteBuffer.wrap(bytes);
			byteBuf.putInt(01);
			byteBuf.putDouble(position.getX());
			byteBuf.putDouble(position.getY());
			byteBuf.putDouble(directionRadian);
			BattlePacket battlePacket = new BattlePacket(PacketTypes.SHOT, bytes);
			return battlePacket;
			//this.threadNetwork.addPacketToSend(battlePacket);
		
	}
	
}
