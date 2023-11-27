package client;

import tank_lib.*;
import tank_lib.map_lib.Map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.Image;
import javax.swing.JFrame;

/**
 * BattleFrame
 */
public class BattleFrame extends JFrame {
    /**
     * Mappa da renderizzare
     */
    Map map;
    /**
     * ID del giocatore per disegnarlo diverasamente e sapere la sua posizione
     * nell'array
     */
    int playerID;
    /**
     * Tutti i giocatori
     */
    Tank[] players;
    /**
     * Lista di proiettili da renderizzare
     */
    private CopyOnWriteArrayList<Bullet> bullets = new CopyOnWriteArrayList<>();
    /**
     * KeyHandler per gestire la pressione dei tasti
     */
    BattleKey keyHandler;

    /**
     * Costrutture parametrico
     * 
     * @param m        mappa da renderizzare
     * @param players  gli altri giocatori
     * @param playerID id del giocatore
     */
    public BattleFrame(Map m, Tank[] players, int playerID) {
        this.map = m;
        this.players = players;
        this.playerID = playerID;
        this.initFrame();
        this.keyHandler = new BattleKey();
        this.addKeyListener(keyHandler);
        this.bullets = new CopyOnWriteArrayList<>();
    }

    /**
     * Funzione helper che setta vari paramentri del Frame
     */
    private void initFrame() {
        this.setPreferredSize(
                new Dimension(map.getWidth() * settings.TILE_SIZE_PX, map.getHeight() * settings.TILE_SIZE_PX));
        pack();
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true);
        requestFocusInWindow();
        this.setBackground(Color.BLACK);
    }

    private Image offScreenImageDrawed = null;
    private Graphics offScreenGraphicsDrawed = null;

    /**
     * Metodo che renderizza il frame
     */
    @Override
    public void paint(Graphics g) {
        // ArrayList<Bullet> bullets_=new ArrayList<>();
        // super.paint(g);
        final Dimension d = getSize();
        if (offScreenImageDrawed == null) {
            // Double-buffer: clear the offscreen image.
            offScreenImageDrawed = createImage(d.width, d.height);
        }
        // g.clearRect(0, 0, this.getWidth(), this.getHeight());
        offScreenGraphicsDrawed = offScreenImageDrawed.getGraphics();
        offScreenGraphicsDrawed.setColor(Color.white);
        offScreenGraphicsDrawed.fillRect(0, 0, d.width, d.height);
        renderOffScreen(offScreenImageDrawed.getGraphics());
        g.drawImage(offScreenImageDrawed, 0, 0, null);
    }

    /**
     * @brief Metodo per double buffer
     * 
     */
    public void renderOffScreen(final Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        // drawHealthBar(g2d);

        CopyOnWriteArrayList<Bullet> bullets_ = moveBullets();
//CopyOnWriteArrayList<Bullet> bullets_=bullets;
        // renderizza la mappa
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                // draw map
                g2d.setColor(map.getTile(i, j).getColor());
                g2d.fillRect(j * settings.TILE_SIZE_PX, i * settings.TILE_SIZE_PX + settings.TITLE_BAR_HEIGHT,
                        settings.TILE_SIZE_PX, settings.TILE_SIZE_PX);
            }
        }

        // draw tanks
        AffineTransform tx = g2d.getTransform();

        // Draw tank 1
        g2d.setColor(Color.RED);
        AffineTransform tank1Transform = new AffineTransform();
        tank1Transform.translate(players[playerID].getPosition().getX(),
                players[playerID].getPosition().getY());
        tank1Transform.rotate(-players[playerID].getAngleRotationRadian());
        tank1Transform.translate(-players[playerID].getWidth() / 2, -players[playerID].getHeight() / 2);
        g2d.setTransform(tank1Transform);
        g2d.fillRect(0, 0, players[playerID].getWidth(), players[playerID].getHeight());
        g2d.drawRect(0, 0, players[playerID].getWidth(), players[playerID].getHeight());
        // Draw enemies
        for (int i = 0; i < players.length; i++) {
            if (i == playerID)
                continue;
            g2d.setColor(Color.BLACK);
            AffineTransform tank2Transform = new AffineTransform();
            tank2Transform.translate(players[i].getPosition().getX(), players[i].getPosition().getY());
            tank2Transform.rotate(-players[i].getAngleRotationRadian());
            tank2Transform.translate(-players[i].getWidth() / 2, -players[i].getHeight() / 2);
            g2d.setTransform(tank2Transform);
            g2d.fillRect(0, 0, players[i].getWidth(), players[i].getHeight());

            g2d.setTransform(tx);
        }

        // draw bullets
        // print all array bullets

        g2d.setColor(Color.BLUE);
        for (Bullet b : bullets_) {
            // System.out.println("Bullet");
            // System.out.println(players[playerID].getPosition().getX() +
            // players[playerID].getPosition().getY());
            // System.out.println(b.getPosition().getX() + b.getPosition().getY());
            AffineTransform originalTransform = g2d.getTransform(); // save the original transform
            AffineTransform bulletTransform = new AffineTransform();
            bulletTransform.translate(b.getForwardPosition().getX(), b.getForwardPosition().getY());
            bulletTransform.rotate(b.getDirectionRadian());
            bulletTransform.translate(-b.getWidth() / 2, -b.getHeight() / 2);
            g2d.setTransform(bulletTransform);
            g2d.fillRect(0, 0, b.getWidth(), b.getHeight());
            g2d.setTransform(originalTransform); // restore the original transform
        }
        // draw healthbar
        for (int i = 0; i < players.length; i++) {
            // if (i == playerID)
            // continue;
            g2d.setColor(Color.RED);
            g2d.fillRect((int) players[i].getPosition().getX(), (int) players[i].getPosition().getY() - 10,
                    (int) players[i].getHealth() / 5, 5); // Double the width by dividing by 5 instead of 10
        }

        g2d.setTransform(tx);

    }

    public boolean isKeyPressed(int keyCode) {
        return this.keyHandler.isPressed(keyCode);
    }

    /**
     * @brief Metodo per muovere i proiettili
     *        I movimenti sono calcolati in base alla direzione e alla velocità del
     *        proiettile, ma potrebbero non rispecchiare quelli reali salvati sul
     *        server
     * @return la lista dei proiettili rimasti, spostati
     */
    public CopyOnWriteArrayList<Bullet> moveBullets() {
        CopyOnWriteArrayList<Bullet> remainingBullets = new CopyOnWriteArrayList<>();
        for (Bullet bullet : bullets) {
            bullet.move();
            // checkHit();
            if (bullet.getPosition().getX() > 0 && bullet.getPosition().getY() > 0 &&
                    bullet.getPosition().getX() < map.getWidth() * settings.TILE_SIZE_PX
                    && bullet.getPosition().getY() < map.getHeight() * settings.TILE_SIZE_PX) {
                remainingBullets.add(bullet);
                // System.out.println("bullet added" + map.getWidth() * settings.TILE_SIZE_PX
                // + map.getHeight() * settings.TILE_SIZE_PX);
            } else {
                remainingBullets.remove(bullet);
                // System.out.println("Bullet removed");
            }
        }
        bullets = remainingBullets;
        return remainingBullets;
    }

    /**
     * @brief Metodo per aggiungere un proiettile alla lista
     * @param bullet
     */
    public void addBullet(Bullet bullet) {
        this.bullets.add(bullet);
    }

    /**
     * @brief Metodo per controllare se un proiettile ha colpito un tank
     */
    public void checkHit() {
        for (Bullet bullet : bullets) {
            for (int i = 0; i < players.length; i++) {
                if (i == playerID)
                    continue;
                if (bullet.getPosition().getX() > players[i].getPosition().getX()
                        && bullet.getPosition().getX() < players[i].getPosition().getX() + players[i].getWidth()
                        && bullet.getPosition().getY() > players[i].getPosition().getY()
                        && bullet.getPosition().getY() < players[i].getPosition().getY() + players[i].getHeight()) {
                    System.out.println("hit");
                    // remove 1/10 of health from tank
                    players[i].setHealth(players[i].getHealth() - 10);
                    // if players health is 0, remove player from game
                    System.out.println(players[i].getHealth());
                    if (players[i].getHealth() <= 0) {
                        removePlayer(i);
                    }
                }
            }
        }
    }

    /**
     * @brief Metodo per rimuovere un giocatore
     * @param id id del giocatore da rimuovere
     */
    public void removePlayer(int id) {
        players[id] = null;
    }

    /**
     * @brief Metodo per settare la vita di un giocatore, in modo da renderizzare
     *        correttamente la barra della vita
     * @param id
     * @param health
     */
    public void setHealth(int id, float health) {
        players[id].setHealth(health);
    }

}
