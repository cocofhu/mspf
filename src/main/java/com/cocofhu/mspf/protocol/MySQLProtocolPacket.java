package com.cocofhu.mspf.protocol;


// https://dev.mysql.com/doc/internals/en/mysql-packet.html

import lombok.Getter;

/**
 * MySQL client/server协议网络包，由消息头和数据部分构成
 */
public class MySQLProtocolPacket {

    @Getter
    public final int sequenceId;

    @Getter
    protected final MySQLProtocolPacketPayload payload;



    /**
     * 通过 sequenceId 和消息载体创建一个MySQL包
     * @param sequenceId    0-255，一个字节的序列ID，多余的部分将会被忽略
     * @param payload       消息载体
     */
    public MySQLProtocolPacket(int sequenceId, MySQLProtocolPacketPayload payload) {
        this.sequenceId = sequenceId;
        this.payload = payload;
    }


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
