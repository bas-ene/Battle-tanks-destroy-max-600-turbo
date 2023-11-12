package tank_lib;

import java.math.*;

/**
 * Point
 */
public class Point {
	private double x, y;

	public Point() {
		x = 0;
		y = 0;
	}

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getDistance(Point p) {
		double a = this.x - p.x;
		a *= a;
		double b = this.y - p.y;
		b *= b;
		return Math.sqrt(a + b);
	}

}
