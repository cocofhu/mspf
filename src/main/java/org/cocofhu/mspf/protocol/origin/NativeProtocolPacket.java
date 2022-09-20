package org.cocofhu.mspf.protocol.origin;

import org.cocofhu.mspf.protocol.Packet;

// https://dev.mysql.com/doc/internals/en/mysql-packet.html
/**
 * MySQL tcp协议网络包，由消息头和数据部分构成
 */
public class NativeProtocolPacket implements Packet<NativeProtocolPacketHeader, NativeProtocolPacketPayload> {

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


    @Override
    public NativeProtocolPacketHeader getHeader() {
        return new NativeProtocolPacketHeader(sequenceId, message.getPayloadLength());
    }

    @Override
    public NativeProtocolPacketPayload getMessage() {
        return message;
    }
}
