package tank_lib;

import java.nio.ByteBuffer;

/**
 * Point
 */
public class Point {
	/**
	 * Coordinate
	 */
	private double x, y;

	/**
	 * Costruttore di default
	 */
	public Point() {
		x = 0;
		y = 0;
	}

	/**
	 * Costruttore parametrico
	 * 
	 * @param x
	 * @param y
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Costruttore parametrico
	 * 
	 * @param x
	 * @param y
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point(byte[] byteArray) {
		ByteBuffer bb = ByteBuffer.wrap(byteArray);
		x = bb.getDouble();
		y = bb.getDouble();
	}

	/**
	 * @return x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sposta x di delta.
	 * 
	 * @param delta
	 */
	public void moveX(double delta) {
		x += delta;
	}

	/**
	 * Sposta y di delta.
	 * 
	 * @param delta
	 */
	public void moveY(double delta) {
		y += delta;
	}

	/**
	 * Calcola la distanza da un altro punto, non puo` assumere valori negativi.
	 * 
	 * @param p Punto da cui calcolare la distanza
	 * @return distanza
	 */
	public double getDistance(Point p) {
		double a = this.x - p.x;
		a *= a;
		double b = this.y - p.y;
		b *= b;
		return Math.sqrt(a + b);
	}

	/**
	 * Imposta x
	 * 
	 * @param x
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Imposta y
	 * 
	 * @param y
	 */
	public void setY(double y) {
		this.y = y;
	}

	public byte[] bitify() {
		ByteBuffer b = ByteBuffer.allocate(16);
		b.putDouble(x);
		b.putDouble(y);
		return b.array();
	}
}
