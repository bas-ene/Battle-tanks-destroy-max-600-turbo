package client;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Vector;

import tank_lib.Bullet;
import tank_lib.Tank;
import tank_lib.map_lib.Map;
import tank_lib.network.BattlePacket;

public class Game extends Thread {
    BattleFrame battleFrame;
    BattleKey keyHandler;
    Map map;
    Tank p1;
    Tank p2;
    ArrayList<Bullet> bullets;
    ThreadNetwork threadNetwork;

    public Game(BattleFrame battleFrame, Map map, Tank p1, Tank p2, ThreadNetwork threadNetwork) {
        this.battleFrame = battleFrame;
        this.keyHandler = new BattleKey();
        this.battleFrame.addKeyListener(keyHandler);
        this.battleFrame.setFocusable(true);
        this.battleFrame.requestFocusInWindow();
        this.map = map;
        this.p1 = p1;
        this.p2 = p2;
        this.bullets = new ArrayList<>();

        this.threadNetwork = threadNetwork;
    }

    public void run() {
        // Initialize game variables and objects

        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        this.threadNetwork.start();
        // Start the game loop
        while (true) {
            KeyEvent k = this.keyHandler.getLastEvent();
            if (k == null) {
                continue;
            }
            System.out.println(k.getKeyChar());
            String keyChar = String.valueOf(k.getKeyChar());
            System.out.println(keyChar);

            byte[] keyBytes = keyChar.getBytes();
            this.threadNetwork.addPacketToSend(new BattlePacket(keyBytes));

            // long now = System.nanoTime();
            // delta += (now - lastTime) / ns;
            // lastTime = now;

            // Update the game state
            // while (delta >= 1) {
            // // Update game logic
            // update();
            // updates++;
            // delta--;
            // }

            // Render the game
            // Render game graphics
            // render();
            // frames++;

            // Handle user input
            // Handle user input events

            // Check for game over conditions
            // if (gameOver()) {
            // break;
            // }

            // Repeat steps 3-6 until the game is over
        }

        // Clean up and exit the game
        // cleanup();
    }

    private void update() {
        // Update game logic
    }

    private void render() {
        // Render game graphics
    }

    private boolean gameOver() {
        // Check for game over conditions
        return false;
    }

    private void cleanup() {
        // Clean up and exit the game
    }
}
