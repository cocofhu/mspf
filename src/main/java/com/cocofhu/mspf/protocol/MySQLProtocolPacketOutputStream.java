package com.cocofhu.mspf.protocol;


import java.io.IOException;
import java.io.OutputStream;

public class MySQLProtocolPacketOutputStream extends OutputStream {

    private final OutputStream out;
    public MySQLProtocolPacketOutputStream(OutputStream out) {
        this.out = out;
    }

    public void writePacket(MySQLProtocolPacket packet) throws IOException {
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
