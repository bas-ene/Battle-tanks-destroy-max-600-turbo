package client;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

import tank_lib.network.BattlePacket;
import tank_lib.network.PacketTypes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * La classe rappresenta un thread di rete che gestisce l'invio e la ricezione
 * di {@link BattlePacket} su una socket.
 */
public class ThreadNetwork extends Thread {
    private ConcurrentLinkedQueue<BattlePacket> packetsToSend = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<BattlePacket> packetsReceived = new ConcurrentLinkedQueue<>();
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private Game game;

    /**
     * Costruttore parametrico
     * 
     * @param socket The socket to communicate over.
     */
    public ThreadNetwork(Socket socket, Game game) {
        this.socket = socket;
        this.game = game;
        try {
            outputStream = this.socket.getOutputStream();
            inputStream = this.socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * La logica del thread.
     * Invia continuamente pacchetti dalla coda packetsToSend e riceve pacchetti
     * nella coda packetsReceived.
     */
    @Override
    public void run() {
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
                    if (p.getPacketType() == PacketTypes.MOVM)
                        System.out.println("Received packet at second: " + System.currentTimeMillis() / 1000);
                    game.handlePacket(p);
                    if (p.getPacketType() == PacketTypes.GEND)
                        break;
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
     * Aggiungi un BattlePacket alla coda dei pacchetti da inviare.
     * 
     * @param packet Il BattlePacket da inviare.
     */
    public void addPacketToSend(BattlePacket packet) {
        packetsToSend.add(packet);
    }

    /**
     * Aggiunge un BattlePacket alla coda dei pacchetti ricevuti.
     * 
     * @param packet Il BattlePacket ricevuto.
     */
    public void addPacketReceived(BattlePacket packet) {
        packetsReceived.add(packet);
    }

    /**
     * Restituisce e rimuove il prossimo BattlePacket dalla coda packetsReceived.
     * 
     * @retun Il prossimo BattlePacket ricevuto, o null se la coda è vuota.
     */

    public BattlePacket getPacketReceived() {
        return packetsReceived.poll();
    }

    /**
     * Restituisce una lista dei pacchetti ricevuti.
     * 
     * @return Lista di BattlePacket ricevuti
     */
    public BattlePacket[] getPacketsReceived() {
        BattlePacket[] packets = new BattlePacket[packetsReceived.size()];
        for (int i = 0; i < packets.length; i++) {
            while (packetsReceived.peek() != null) {
                packets[i] = packetsReceived.poll();
            }
        }
        return packets;
    }
}
