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
     * Aggiunge l'evento alla coda.
     * 
     * @param e l'evento rappresentante la pressione del tasto.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    // /**
    // * Invocato quando un tasti viene tenuto premuto.
    // * Aggiunge l'evento alla coda.
    // *
    // * @param e l'evento rappresentante la pressione del tasto.
    // */
    // @Override
    // public void keyTyped(KeyEvent e) {
    // keyEvents.add(e);
    // }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    public boolean isPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    // /**
    // * Ritorna e rimuove l'ultimo evento nella coda.
    // *
    // * @return l'ultimo evento, o null se la coda è vuota.
    // */
    // public KeyEvent getLastEvent() {
    // return keyEvents.poll();
    // }
}
