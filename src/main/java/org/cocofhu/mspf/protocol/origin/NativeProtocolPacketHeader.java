package org.cocofhu.mspf.protocol.origin;

import org.cocofhu.mspf.protocol.MessageHeader;

public class NativeProtocolPacketHeader implements MessageHeader {

    private final byte[] headerBytes;

    public NativeProtocolPacketHeader(byte[] headerBytes) {
        this.headerBytes = headerBytes;
    }

    @Override
    public byte[] getHeaderBytes() {
        return headerBytes;
    }

    @Override
    public int getMessageSize() {
        return (this.headerBytes[0] & 0xff) + ((this.headerBytes[1] & 0xff) << 8) + ((this.headerBytes[2] & 0xff) << 16);
    }

    @Override
    public byte getMessageSequence() {
        return this.headerBytes[3];
    }

}
