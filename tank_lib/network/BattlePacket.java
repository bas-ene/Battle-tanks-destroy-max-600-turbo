package tank_lib.network;

import java.nio.ByteBuffer;

/**
 * Represents a packet used in the BattleTanks game network communication.
 */
public class BattlePacket {
    PacketTypes packetType;
    byte[] packetBytes;

    /**
     * Constructs a BattlePacket object with the specified packet type.
     * 
     * @param packetType The type of the packet.
     */
    public BattlePacket(PacketTypes packetType) {
        this.packetType = packetType;
    }

    /**
     * Constructs a BattlePacket object with the specified packet type and packet bytes.
     * 
     * @param packetType   The type of the packet.
     * @param packetBytes  The bytes of the packet.
     */
    public BattlePacket(PacketTypes packetType, byte[] packetBytes) {
        this.packetType = packetType;
        this.packetBytes = packetBytes;
    }

    /**
     * Constructs a BattlePacket object with the specified packet bytes.
     * 
     * @param packetBytes  The bytes of the packet.
     */
    public BattlePacket(byte[] packetBytes) {
        this.packetBytes = packetBytes;
    }

    /**
     * Returns the type of the packet.
     * 
     * @return The type of the packet.
     */
    public PacketTypes getPacketType() {
        return packetType;
    }

    /**
     * Converts the BattlePacket object to a byte array representation.
     * 
     * @return The byte array representation of the BattlePacket object.
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
     * Returns the bytes of the packet.
     * 
     * @return The bytes of the packet.
     */
    public byte[] getPacketBytes() {
        return packetBytes;
    }
}
