package client;

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import tank_lib.network.BattlePacket;
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

    /**
     * Costruttore parametrico
     * 
     * @param socket The socket to communicate over.
     */
    public ThreadNetwork(Socket socket) {
        this.socket = socket;
        try {
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
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
        byte[] buffer = new byte[2048];
        while (true) {
            // Send packets
            BattlePacket battlePacket = packetsToSend.poll();
            if (battlePacket != null) {
                // Get the byte array from the BattlePacket
                byte[] packetBytes = battlePacket.bitify();

                try {
                    // Send the byte array through the socket's output stream
                    outputStream.write(packetBytes);
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Receive packets
            // try {
            // int bytesRead = inputStream.read(buffer);
            // if (bytesRead != -1) {
            // byte[] packetBytes = Arrays.copyOf(buffer, bytesRead);
            // BattlePacket receivedPacket = new BattlePacket(packetBytes); // replace with
            // actual constructor
            // addPacketReceived(receivedPacket);
            // }
            // } catch (IOException e) {
            // e.printStackTrace();
            // }
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
    public ArrayList<BattlePacket> getPacketsReceived() {
        ArrayList<BattlePacket> packets = new ArrayList<>();
        while (packetsReceived.peek() != null) {
            packets.add(packetsReceived.poll());
        }
        return packets;
    }
}
