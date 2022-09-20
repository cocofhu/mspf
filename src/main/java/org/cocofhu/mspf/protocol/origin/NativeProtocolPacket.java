package org.cocofhu.mspf.protocol.origin;

import org.cocofhu.mspf.protocol.Packet;

// https://dev.mysql.com/doc/internals/en/mysql-packet.html
/**
 * MySQL tcp协议网络包，由消息头和数据部分构成
 */
public class NativeProtocolPacket extends Packet<NativeProtocolPacketHeader, NativeProtocolPacketPayload> {
    public NativeProtocolPacket(NativeProtocolPacketHeader header, NativeProtocolPacketPayload message) {
        super(header, message);
    }
    /**
     * 通过 sequenceId 和消息载体创建一个MySQL包
     * @param sequenceId    0-255，一个字节的序列ID，多余的部分将会被忽略
     * @param message       消息载体
     */
    public NativeProtocolPacket(int sequenceId, NativeProtocolPacketPayload message) {
        this(new NativeProtocolPacketHeader(new byte[]{
                (byte) (message.getPayloadLength() & 255),
                (byte) ((message.getPayloadLength() / 255) & 255),
                (byte) ((message.getPayloadLength() / 255 / 255) & 255),
                (byte) sequenceId
        }), message);
    }
}
