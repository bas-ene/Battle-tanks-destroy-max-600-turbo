package tank_lib.network;

import java.nio.ByteBuffer;

/**
 * Rappresenta un pacchetto utilizzato nella comunicazione di rete.
 */
public class BattlePacket {
    /**
     * Tipo di pacchetto.
     */
    PacketTypes packetType;
    /**
     * Dati sotto forma di byte[].
     */
    byte[] packetBytes;

    /**
     * Costruisce un oggetto BattlePacket con il tipo di pacchetto specificato.
     * 
     * @param packetType Il tipo di pacchetto.
     */
    public BattlePacket(PacketTypes packetType) {
        this.packetType = packetType;
        this.packetBytes = new byte[0];
    }

    /**
     * Costruisce un oggetto BattlePacket con il tipo di pacchetto e i byte del
     * pacchetto specificati.
     * 
     * @param packetType  Il tipo di pacchetto.
     * @param packetBytes I dati del pacchetto.
     */
    public BattlePacket(PacketTypes packetType, byte[] packetBytes) {
        this.packetType = packetType;
        this.packetBytes = packetBytes;
    }

    /**
     * Ritorna il tipo di pacchetto.
     * 
     * @return Tipo del pacchetto.
     */
    public PacketTypes getPacketType() {
        return packetType;
    }

    /**
     * Converte l'oggetto BattlePacket in una rappresentazione in un array di byte.
     * Il formato e` costante: 4 byte per il numero di byte dei dati, 4 byte per il
     * tipo di pacchetto, il resto per i dati.
     * 
     * @return Il pacchetto convertito in un array di byte.
     */
    public byte[] bitify() {
        // 4 for the message length, 4 bytes for the packet type, the rest for the data
        int packetBytesLength = packetBytes == null ? 0 : packetBytes.length;
        byte bytes[] = new byte[4 + 4 + packetBytesLength];
        // message length - this length
        ByteBuffer b = ByteBuffer.wrap(bytes);
        b.putInt(packetBytesLength);
        // insert the packet type
        b.put(packetType.toString().getBytes());
        // data
        if (packetBytes != null)
            b.put(packetBytes);
        return bytes;
    }

    /**
     * Ritorna i byte del pacchetto.
     * 
     * @return I byte del pacchetto.
     */
    public byte[] getPacketBytes() {
        return packetBytes;
    }
}
