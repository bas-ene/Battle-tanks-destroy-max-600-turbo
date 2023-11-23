package tank_lib;

/**
 * Rappresenta il tank.
 */
public class Tank {
	private Point position;
	private float health = 100.0f;
	private boolean canShoot = true;
	private String username;
	private int width = 25;
	private int height = 20;
	private double angleRotationRadian = 0;
	private Bullet bullet;
	private int id;

	/**
	 * Costruttore parametrico.
	 * 
	 * @param spawnPoint La posizione di spawn del tank.
	 * @param username   Username.
	 */
	public Tank(Point spawnPoint, String username) {
		this.username = username;
		this.position = spawnPoint;
		bullet= new Bullet(01, position, angleRotationRadian);
	}

	public Tank(String username) {
		this.username = username;
		this.position = new Point(0, 0);
	}

	public Tank(String username, int id) {
		this.username = username;
		this.position = new Point(0, 0);
		this.id = id;
	}

	/**
	 * Ritorna la posizione corrente del tank.
	 * 
	 * @return La posizione corrente del tank.
	 */
	public Point getPosition() {
		Point p= new Point(position.getX(), position.getY());
		return p;
	}

	public Bullet shoot() {
		Point position = new Point(this.position.getX(), this.position.getY());
		double mirroredAngle = (2*Math.PI) - angleRotationRadian;
		Bullet bullet_ = new Bullet(01, position, mirroredAngle);
		this.bullet = bullet_;
		return bullet_;
	}

	public void setPosition(Point p) {
		this.position = p;
	}

	/**
	 * Ritorna la vita corrente del tank.
	 * 
	 * @return Vita del tank.
	 */
	public float getHealth() {
		return health;
	}

	/**
	 * Ritorna lo username.
	 * 
	 * @return Lo username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Ritorna la larghezza del tank.
	 * 
	 * @return La larghezza.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Ritorna l'altezza del tank.
	 * 
	 * @return L'altezza.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Imposta l'angolo di rotazione del tank.
	 * 
	 * @param angleRadian L'angolo di rotazione in radianti.
	 */
	public void setRotation(double angleRadian) {
		angleRotationRadian = angleRadian;
	}

	/**
	 * Ruota il tank di un certo angolo.
	 * 
	 * @param angleRadian L'angolo di rotazione in radianti.
	 */
	public void rotateBy(double angleRadian) {
		angleRotationRadian += angleRadian;
	}

	/**
	 * Ritorna la rotazione corrente del tank.
	 * 
	 * @return L'angolo di rotazione in radianti.
	 */
	public double getAngleRotationRadian() {
		return angleRotationRadian;
	}

	/**
	 * Muove il tank di un certo numero di pixel nella direzione corrente.
	 * 
	 * @param pixels Numero di pixel per il quale il tank si deve muovere.
	 */
	public void moveBy(double pixels) {
		this.position.moveX(Math.cos(angleRotationRadian) * pixels);
		this.position.moveY(-Math.sin(angleRotationRadian) * pixels);
	}

	/**
	 * Imposta la coordinata X della posizione del tank.
	 * 
	 * @param x La coordinata X.
	 */
	public void setX(double x) {
		this.position.setX(x);
	}

	/**
	 * Imposta la coordinata Y della posizione del tank.
	 * 
	 * @param y La coordinata Y.
	 */
	public void setY(double y) {
		this.position.setY(y);
	}

	/**
	 * Ritorna la posizione del tank tenendo conto nella mappa, non tenendo quindi
	 * conto della titlebar.
	 * 
	 * @return La posizione del tank nella mappa.
	 */
	public Point getPositionInMap() {
		return new Point(this.position.getX(), this.position.getY() - settings.TITLE_BAR_HEIGHT);
	}

	public void translatePositionInWindow() {
		this.position.setY(this.position.getY() + settings.TITLE_BAR_HEIGHT);
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPositionInWindow(Point point) {
		this.position = new Point(point.getX() * settings.TILE_SIZE + settings.TILE_SIZE / 2,
				point.getY() * settings.TILE_SIZE + settings.TITLE_BAR_HEIGHT + settings.TILE_SIZE / 2);

	}

	public Bullet getBullet() {
		return bullet;
	}

	public void setBullet(Bullet bullet) {
		this.bullet = bullet;
	}

	public void setHealth(float health) {
		this.health = health;
	}

}
