package org.cocofhu.mspf.protocol.origin;

public abstract class NativeProtocolBasicPacket {
    protected final NativeProtocolHeader header;
    protected final NativeProtocolPacketPayload payload;

    protected NativeProtocolBasicPacket(NativeProtocolHeader header, NativeProtocolPacketPayload payload) {
        this.header = header;
        this.payload = payload;
    }

}
