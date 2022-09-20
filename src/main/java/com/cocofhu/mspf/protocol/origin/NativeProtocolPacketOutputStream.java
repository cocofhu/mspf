package com.cocofhu.mspf.protocol.origin;


import java.io.IOException;
import java.io.OutputStream;

public class NativeProtocolPacketOutputStream extends OutputStream {

    final OutputStream out;
    public NativeProtocolPacketOutputStream(OutputStream out) {
        this.out = out;
    }

    public void writeNativeProtocolPacket(NativeProtocolPacket packet) throws IOException {
        byte[] bytes = packet.toBytes();
        for (byte b : bytes) {
            write(b);
        }
    }
    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }
}
