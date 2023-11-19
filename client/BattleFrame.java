package client;

import tank_lib.*;
import tank_lib.map_lib.Map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.Image;
import javax.swing.JFrame;

/**
 * BattleFrame
 */
public class BattleFrame extends JFrame {
    Map map;
    Tank p1, p2;
    BattleKey keyHandler = new BattleKey();

    /**
     * Costrutture parametrico
     * 
     * @param m  mappa da renderizzare
     * @param p1 giocatore 1, (client che crea questa classe)
     * @param p2 giocatore 2, (altro client)
     */
    public BattleFrame(Map m, Tank p1, Tank p2) {
        this.map = m;
        this.p1 = p1;
        this.p2 = p2;
        this.initFrame();
        this.addKeyListener(keyHandler);
    }

    /**
     * Funzione helper che setta vari paramentri del Frame
     */
    private void initFrame() {
        this.setPreferredSize(
                new Dimension(map.getWidth() * settings.TILE_SIZE_PX, map.getHeight() * settings.TILE_SIZE_PX));
        pack();
        setVisible(true);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true);

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
        g.clearRect(0, 0, this.getWidth(), this.getHeight()); // super.paint(g);
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
        tank1Transform.translate(p1.getPosition().getX(), p1.getPosition().getY());
        tank1Transform.rotate(p1.getAngleRotationRadian());
        tank1Transform.translate(-p1.getWidth() / 2, -p1.getHeight() / 2);
        g2d.setTransform(tank1Transform);
        g2d.fillRect(0, 0, p1.getWidth(), p1.getHeight());

        // Draw tank 2
        g2d.setColor(Color.BLACK);
        AffineTransform tank2Transform = new AffineTransform();
        tank2Transform.translate(p2.getPosition().getX(), p2.getPosition().getY());
        tank2Transform.rotate(p2.getAngleRotationRadian());
        tank2Transform.translate(-p2.getWidth() / 2, -p2.getHeight() / 2);
        g2d.setTransform(tank2Transform);
        g2d.fillRect(0, 0, p2.getWidth(), p2.getHeight());

        g2d.setTransform(tx);
    }

    public KeyEvent getLastEvent() {
        return this.keyHandler.getLastEvent();
    }

}
