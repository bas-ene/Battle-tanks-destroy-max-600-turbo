package client;

import tank_lib.*;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;

/**
 * BattleFrame
 */
public class BattleFrame extends JFrame {
    Map map;
    Tank p1, p2;

    /**
     * Costrutture parametrico
     * @param m mappa da renderizzare
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
        this.setSize(new Dimension(map.getWidth() * settings.TILE_SIZE, map.getHeight() * settings.TILE_SIZE));
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setFocusable(true);
        requestFocus();
    }

    /**
     * Metodo che renderizza il frame
     */
    @Override
    public void paint(Graphics g) {
        for (int i = 0; i < map.getHeight(); i++) {
            for (int j = 0; j < map.getWidth(); j++) {
                g.setColor(map.getTile(i, j).getColor());
                g.fillRect(j * settings.TILE_SIZE, i * settings.TILE_SIZE + settings.TITLE_BAR_HEIGHT,
                        settings.TILE_SIZE, settings.TILE_SIZE);
            }
        }
    }

}
