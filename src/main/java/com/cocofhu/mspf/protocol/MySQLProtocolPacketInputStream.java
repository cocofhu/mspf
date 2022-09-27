package com.cocofhu.mspf.protocol;


import java.io.IOException;
import java.io.InputStream;

/**
 * input stream for package of mysql protocol
 * @author cocofhu
 */
public class MySQLProtocolPacketInputStream extends InputStream {


    private final InputStream in;

    public MySQLProtocolPacketInputStream(InputStream in) {
        this.in = in;
    }

    public MySQLProtocolPacket readPacket() throws IOException {
        //
        byte[] header = new byte[4];
        if (in.read(header) != 4) {
            throw new IOException("read packet header failed, not has enough bytes to read.");
        }
        int payloadSize = (header[0] & 255) + (header[1] & 255) * 255 + (header[2] & 255) * 255 * 255;
        byte[] payload = new byte[payloadSize];
        if (in.read(payload) != payloadSize) {
            throw new IOException("read packet payload failed, not has enough bytes to read.");
        }
        MySQLProtocolPacketPayload message = new MySQLProtocolPacketPayload(payload);
        return new MySQLProtocolPacket(header[3] & 255, message);
    }

    /**
     * 不要使用该方法
     */
    @Deprecated
    @Override
    public int read() throws IOException {
        return in.read();
    }
}
