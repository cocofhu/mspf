package com.cocofhu.mspf.protocol.cs;


public class NativeProtocolPacketHeader {

    private final byte[] headerBytes;

    public NativeProtocolPacketHeader(byte[] headerBytes) {
        this.headerBytes = headerBytes;
    }

    public NativeProtocolPacketHeader(int sequenceId, int payloadLength) {
        this(new byte[]{
                (byte) (payloadLength & 255),
                (byte) ((payloadLength / 255) & 255),
                (byte) ((payloadLength / 255 / 255) & 255),
                (byte) sequenceId});
    }

    public byte[] getHeaderBytes() {
        return headerBytes;
    }

    public int getMessageSize() {
        return (this.headerBytes[0] & 0xff) + ((this.headerBytes[1] & 0xff) << 8) + ((this.headerBytes[2] & 0xff) << 16);
    }

    public byte getMessageSequence() {
        return this.headerBytes[3];
    }

}
