package com.cocofhu.mspf.protocol.origin;


import java.io.IOException;
import java.io.InputStream;

public class NativeProtocolPacketInputStream extends InputStream {



    final InputStream in;
    public NativeProtocolPacketInputStream(InputStream in) {
        this.in = in;
    }

    public NativeProtocolPacket readNativeProtocolPacket() throws IOException {
        //
        byte[] header = new byte[4];
        if(in.read(header) != 4){
            throw new IOException("read packet header failed, not has enough bytes to read.");
        }
        NativeProtocolPacketHeader hPacket = new NativeProtocolPacketHeader(header);
        byte[] payload = new byte[hPacket.getMessageSize()];
        if(in.read(payload) != hPacket.getMessageSize()){
            throw new IOException("read packet payload failed, not has enough bytes to read.");
        }
        NativeProtocolPacketPayload message = new NativeProtocolPacketPayload(payload);
        return new NativeProtocolPacket(hPacket.getMessageSequence(),message);
    }

    /** 不要使用该方法 */
    @Deprecated
    @Override
    public int read() throws IOException {
        return in.read();
    }
}
