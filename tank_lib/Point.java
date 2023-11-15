package tank_lib;

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

}
