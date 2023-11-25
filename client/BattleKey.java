package client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Estende KeyAdapter e fornisce metodi per gestire la pressione dei tasti.
 */
public class BattleKey extends KeyAdapter {
    private ConcurrentLinkedQueue<KeyEvent> keyEvents;

    /**
     * Costruttore di default.
     */
    public BattleKey() {
        keyEvents = new ConcurrentLinkedQueue<>();
    }

    /**
     * Invocato quando un tasto viene premuto.
     * Aggiunge l'evento alla coda.
     * 
     * @param e l'evento rappresentante la pressione del tasto.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() != KeyEvent.VK_Z)
            keyEvents.add(e);
    }

    /**
     * Invocato quando un tasti viene tenuto premuto.
     * Aggiunge l'evento alla coda.
     * 
     * @param e l'evento rappresentante la pressione del tasto.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        keyEvents.add(e);
    }

    /**
     * Ritorna e rimuove il primo evento nella coda.
     * 
     * @return l'ultimo evento, o null se la coda è vuota.
     */
    public KeyEvent getEvent() {
        return keyEvents.poll();
    }
}
