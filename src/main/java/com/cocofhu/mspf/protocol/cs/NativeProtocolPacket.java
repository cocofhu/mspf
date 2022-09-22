package com.cocofhu.mspf.protocol.cs;

import com.cocofhu.mspf.protocol.Packet;

// https://dev.mysql.com/doc/internals/en/mysql-packet.html
/**
 * MySQL client/server协议网络包，由消息头和数据部分构成
 */
public class NativeProtocolPacket {

    protected final int sequenceId;
    protected final NativeProtocolPacketPayload message;


    /**
     * 通过 sequenceId 和消息载体创建一个MySQL包
     * @param sequenceId    0-255，一个字节的序列ID，多余的部分将会被忽略
     * @param message       消息载体
     */
    public NativeProtocolPacket(int sequenceId, NativeProtocolPacketPayload message) {
        this.sequenceId = sequenceId;
        this.message = message;
    }

    public NativeProtocolPacketHeader getHeader() {
        return new NativeProtocolPacketHeader(sequenceId, message.getPayloadLength());
    }

    public NativeProtocolPacketPayload getMessage() {
        return message;
    }

    public byte[] toBytes(){
        // 获取底层数据，减少一次拷贝
        NativeProtocolPacketHeader header = getHeader();
        NativeProtocolPacketPayload message = getMessage();
        byte[] headerBytes = header.getHeaderBytes();
        byte[] messageBytes = message.underlyingBytes();
        byte[] newBytes = new byte[headerBytes.length + message.getPayloadLength()];
        System.arraycopy(headerBytes, 0, newBytes, 0, headerBytes.length);
        System.arraycopy(messageBytes, 0, newBytes, headerBytes.length, message.getPayloadLength());
        return newBytes;
    }
}
