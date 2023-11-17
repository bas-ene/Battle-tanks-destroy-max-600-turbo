package client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BattleKey extends KeyAdapter {
    private ConcurrentLinkedQueue<KeyEvent> keyEvents = new ConcurrentLinkedQueue<>();

    public BattleKey() {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyChar());
        keyEvents.add(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println(e.getKeyChar());
        keyEvents.add(e);
    }

    public KeyEvent getLastEvent() {
        return keyEvents.poll();
    }
}
