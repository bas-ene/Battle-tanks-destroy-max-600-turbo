package client;

import tank_lib.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.Font;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

/**
 * BattleFrame
 */
public class BattleFrame extends JFrame {
    Map map;
    Tank p1, p2;

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

    }

    /**
     * Funzione helper che setta vari paramentri del Frame
     */
    private void initFrame() {
        this.setPreferredSize(new Dimension(map.getWidth() * settings.TILE_SIZE, map.getHeight() * settings.TILE_SIZE));
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setFocusable(true);
        requestFocus();
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
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                // draw map
                g.setColor(map.getTile(i, j).getColor());
                g.fillRect(j * settings.TILE_SIZE, i * settings.TILE_SIZE + settings.TITLE_BAR_HEIGHT,
                        settings.TILE_SIZE, settings.TILE_SIZE);
            }
        }
        // draw tanks
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform tx = new AffineTransform();
        Rectangle tank = new Rectangle((int) (p1.getPosition().getX() - p1.getWidth() / 2),
                (int) (p1.getPosition().getY() - p1.getHeight() / 2), p1.getWidth(), p1.getHeight());
        tx.rotate(p1.getAngleRotationRadian());
        Shape transTank = tx.createTransformedShape(tank);
        g2d.setColor(Color.MAGENTA);
        g2d.fill(transTank);
        moveTank(p1);

        tank = new Rectangle((int) (p2.getPosition().getX() - p2.getWidth() / 2),
                (int) (p2.getPosition().getY() - p2.getHeight() / 2), p2.getWidth(),
                p2.getHeight());
        tx = new AffineTransform();
        tx.rotate(p2.getAngleRotationRadian());

        transTank = tx.createTransformedShape(tank);
        g2d.setColor(Color.WHITE);
        g2d.fill(transTank);
        moveTank(p2);
    }

    private void moveTank(Tank pX) {
        System.out.println(pX.getAngleRotationRadian());
        System.out.println(pX.getPosition().getX());
        System.out.println(pX.getPosition().getY());
        pX.rotateBy(Math.PI/2);
        pX.moveBy(1);
    }

}
