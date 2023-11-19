package client;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import tank_lib.Bullet;
import tank_lib.Tank;
import tank_lib.settings;
import tank_lib.map_lib.Map;
import tank_lib.network.BattlePacket;

public class Game extends Thread {
    BattleFrame battleFrame;
    Map map;
    Tank p1;
    Tank p2;
    ArrayList<Bullet> bullets;
    ThreadNetwork threadNetwork;

    public Game(BattleFrame battleFrame, Map map, Tank p1, Tank p2, ThreadNetwork threadNetwork) {
        this.battleFrame = battleFrame;
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
            // based on packets received, update the game
            ArrayList<BattlePacket> packet = this.threadNetwork.getPacketsReceived();
            // foreach packet, process it
            for (BattlePacket battlePacket : packet) {
                handlePacket(battlePacket);
            }

            // move the tank if necessary
            KeyEvent k = this.battleFrame.getLastEvent();
            if (k == null) {
                continue;
            }
            System.out.println(k.getKeyChar());
            handleMovement(k);
            sendMovement(k);
            // handle shooting

            // sends the packet to the server

        }

        // Clean up and exit the game
        // cleanup();
    }

    private void sendMovement(KeyEvent k) {
        // send the movement to the server
        if (k != null)
            this.threadNetwork.addPacketToSend(new BattlePacket(new byte[] { (byte) k.getKeyChar(), (byte) '\n' }));
    }

    private void handleMovement(KeyEvent k) {
        // wasd movement
        float speedMultiplier = map.getTile(p1.getPosition()).getSpeedMultiplier();
        float mov = (settings.TANK_SPEED_TILES_S * settings.TILE_SIZE_PX * speedMultiplier) * 1 / 30;
        // rotation per tick
        double rotation = 2 * Math.PI * settings.TANK_ROTATION_SPEED_RPM * 1 / 30;
        System.out.println(mov);
        System.out.println(rotation);
        if (k != null) {
            switch (k.getKeyChar()) {
                case 'w':
                    p1.moveBy(mov);
                    break;
                case 'a':
                    rotation *= -1;
                    p1.rotateBy(rotation);
                    break;
                case 's':
                    mov *= -1;
                    p1.moveBy(mov);
                    break;
                case 'd':
                    p1.rotateBy(rotation);
                    break;
                default:
                    break;
            }
        }
    }

    private void handlePacket(BattlePacket battlePacket) {
        switch (battlePacket.getPacketType()) {
            case CONN:
                // set the player id to the one received
                break;
            case SMAP:
                // set the map to the one received
                break;
            case STRT:
                // startGame();
                break;
            case MOVM:
                // set the location of the tank to the one received
                break;
            case SHOT:
                // add a bullet to the list of bullets
                break;
            case BDST:
                // remove the bullet from the list of bullets and destroy the building
                break;
            case HLTH:
                // update the tank health
                break;
            case GEND:
                // showEndGame();
                break;
            default:
                System.out.println("Packet type not recognized");
                break;

        }
    }

    private void update() {
        // Update game logic
    }

    // private void render() {
    // // Render game graphics
    // }

    // private void cleanup() {
    // // Clean up and exit the game
    // }
}