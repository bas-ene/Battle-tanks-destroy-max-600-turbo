package client;

import tank_lib.*;
import tank_lib.map_lib.Map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.Image;
import javax.swing.JFrame;

/**
 * BattleFrame
 */
public class BattleFrame extends JFrame {
    Map map;
    int playerID;
    Tank[] players;
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
        setVisible(true);
    }

    private Image offScreenImageDrawed = null;
    private Graphics offScreenGraphicsDrawed = null;

    /**
     * Metodo che renderizza il frame
     */
    @Override
    public void paint(Graphics g) {
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

    }

    public KeyEvent getLastEvent() {
        return this.keyHandler.getLastEvent();
    }

}
