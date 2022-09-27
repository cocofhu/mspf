package com.cocofhu.mspf.protocol;

import lombok.Getter;

/**
 * MySQL client/server protocol packet, every packet will extend from this class.
 * it's consists of header(sequenceId + payloadLength) and payload.
 * <a href="https://dev.mysql.com/doc/internals/en/mysql-packet.html">...</a>
 */
public class MySQLProtocolPacket {

    /**
     *  The sequence-id is incremented with each packet and may wrap around.
     *  It starts at 0 and is reset to 0 when a new command begins in the Command Phase.
     */
    @Getter
    public final int sequenceId;

    @Getter
    protected final MySQLProtocolPacketPayload payload;



    /** initiate packet with an id of sequence and payload.*/
    public MySQLProtocolPacket(int sequenceId, MySQLProtocolPacketPayload payload) {
        this.sequenceId = sequenceId;
        this.payload = payload;
    }

    /** initiate packet with an id of sequence, it has an empty payload.*/
    public MySQLProtocolPacket(int sequenceId) {
        this.sequenceId = sequenceId;
        this.payload = new MySQLProtocolPacketPayload(0);
    }


    /** pack this packet as bytes array. */
    public byte[] toBytes(){
        int payloadLength = payload.getPayloadLength();
        byte[] headerBytes = new byte[]{
                (byte) ( payloadLength & 255),
                (byte) ((payloadLength / 255) & 255),
                (byte) ((payloadLength / 255 / 255) & 255),
                (byte) sequenceId
        };
        byte[] messageBytes = payload.underlyingBytes();
        byte[] newBytes = new byte[headerBytes.length + payloadLength];
        System.arraycopy(headerBytes, 0, newBytes, 0, headerBytes.length);
        System.arraycopy(messageBytes, 0, newBytes, headerBytes.length, payloadLength);
        return newBytes;
    }


}
