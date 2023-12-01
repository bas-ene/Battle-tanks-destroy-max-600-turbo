package client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;

/**
 * Estende KeyAdapter e fornisce metodi per gestire la pressione dei tasti.
 */
public class BattleKey extends KeyAdapter {
    private HashSet<Integer> pressedKeys;

    /**
     * Costruttore di default.
     */
    public BattleKey() {
        pressedKeys = new HashSet<Integer>();
    }

    /**
     * Invocato quando un tasto viene premuto.
     * Aggiunge l'evento al set
     * 
     * @param e l'evento rappresentante la pressione del tasto.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    /**
     * Controlla se un tasto è premuto.
     * 
     * @param keyCode il codice del tasto da controllare.
     * @return true se il tasto è premuto, false altrimenti.
     */
    public boolean isPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }
}
