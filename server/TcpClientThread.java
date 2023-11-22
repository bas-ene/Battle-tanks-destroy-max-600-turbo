package server;

import java.util.concurrent.ConcurrentLinkedQueue;

import tank_lib.map_lib.Map;
import tank_lib.network.BattlePacket;
import tank_lib.network.PacketTypes;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * La classe rappresenta un thread di rete che gestisce l'invio e la ricezione
 * di {@link BattlePacket} su una socket.
 */
/**
 * This class represents a thread that handles TCP client communication in a
 * Battle Tanks game server.
 * It is responsible for sending and receiving Battle Packets over a socket
 * connection.
 */
public class TcpClientThread extends Thread {
    private ConcurrentLinkedQueue<BattlePacket> packetsToSend = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<BattlePacket> packetsReceived = new ConcurrentLinkedQueue<>();
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    /**
     * Constructs a TcpClientThread object with the specified socket.
     *
     * @param socket The socket to communicate over.
     */
    public TcpClientThread(Socket socket) {
        this.socket = socket;
        try {
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The logic of the thread.
     * Continuously sends packets from the packetsToSend queue and receives packets
     * into the packetsReceived queue.
     */
    @Override
    public void run() {
        // init

        // Create a thread for sending packets
        Thread sendThread = new Thread(() -> {
            while (true) {
                BattlePacket battlePacket = packetsToSend.poll();
                if (battlePacket != null) {
                    byte[] packetBytes = battlePacket.bitify();

                    try {
                        outputStream.write(packetBytes);
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Create a thread for receiving packets
        Thread receiveThread = new Thread(() -> {
            while (true) {
                try {
                    // parsing
                    byte[] pLengthBytes = new byte[4];
                    inputStream.read(pLengthBytes);
                    int packetLength = ByteBuffer.wrap(pLengthBytes).getInt();
                    System.out.println("Received packet length: " + packetLength);

                    byte[] strBytes = new byte[4];
                    inputStream.read(strBytes);
                    String typeBytes = new String(strBytes);
                    System.out.println("Received packet type: " + typeBytes);
                    PacketTypes type = PacketTypes.valueOf(typeBytes);

                    byte[] dataBytes = new byte[packetLength];
                    inputStream.read(dataBytes);
                    BattlePacket p = new BattlePacket(type, dataBytes);
                    addPacketReceived(p);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Start both threads
        sendThread.start();
        receiveThread.start();

        // Wait for both threads to finish
        try {
            sendThread.join();
            receiveThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a BattlePacket to the packetsToSend queue.
     *
     * @param packet The BattlePacket to send.
     */
    public void addPacketToSend(BattlePacket packet) {
        packetsToSend.add(packet);
    }

    /**
     * Adds a BattlePacket to the packetsReceived queue.
     *
     * @param packet The received BattlePacket.
     */
    public void addPacketReceived(BattlePacket packet) {
        packetsReceived.add(packet);
    }

    /**
     * Retrieves and removes the next BattlePacket from the packetsReceived queue.
     *
     * @return The next received BattlePacket, or null if the queue is empty.
     */
    public BattlePacket getPacketReceived() {
        return packetsReceived.poll();
    }

    /**
     * Returns a list of received BattlePackets.
     *
     * @return List of received BattlePackets.
     */
    public ArrayList<BattlePacket> getPacketsReceived() {
        ArrayList<BattlePacket> packets = new ArrayList<>();
        while (packetsReceived.peek() != null) {
            packets.add(packetsReceived.poll());
        }
        return packets;
    }

    /**
     * Gets the username of the tank.
     *
     * @return The username of the tank.
     */
    public String getTankUsername() {

        while (true) {
            BattlePacket packet = getPacketReceived();
            if (packet != null && packet.getPacketType() == PacketTypes.CONN) {
                return new String(packet.getPacketBytes());
            }

        }
    }

    /**
     * Sends a start packet.
     */
    public void sendStartPacket() {
        BattlePacket packet = new BattlePacket(PacketTypes.STRT, null);
        addPacketToSend(packet);
    }

    /**
     * Sends IDs to the clients.
     *
     * @param i                 The ID of the tank.
     * @param NUMBER_OF_CLIENTS The total number of clients.
     */
    public void sendIDs(int i, int NUMBER_OF_CLIENTS) {
        // packet of type CONN, first puts the ID of the tank of interest, and then the
        // others in ascending order
        ByteBuffer byteBuf = ByteBuffer.allocate(NUMBER_OF_CLIENTS * Integer.BYTES);
        byteBuf.putInt(i);
        for (int j = 0; j < NUMBER_OF_CLIENTS; j++) {
            if (j != i) {
                byteBuf.putInt(j);
            }
        }
        BattlePacket packet = new BattlePacket(PacketTypes.CONN, byteBuf.array());
        addPacketToSend(packet);
    }

    /**
     * Sends the map to the clients.
     *
     * @param map The map to send.
     */
    public void sendMap(Map map) {
        BattlePacket packet = new BattlePacket(PacketTypes.SMAP, map.bitify());
        addPacketToSend(packet);
    }
}
