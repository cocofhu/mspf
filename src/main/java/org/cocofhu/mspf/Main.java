package org.cocofhu.mspf;

import org.cocofhu.mspf.protocol.origin.NativeProtocolPacket;
import org.cocofhu.mspf.protocol.origin.NativeProtocolPacketInputStream;
import org.cocofhu.mspf.protocol.origin.NativeProtocolPacketOutputStream;
import org.cocofhu.mspf.protocol.origin.packet.NativeProtocolHandshakeV10Packet;
import org.cocofhu.mspf.protocol.origin.NativeProtocolPacketPayload;
import org.cocofhu.mspf.protocol.origin.packet.NativeProtocolOKPacket;
import org.cocofhu.mspf.util.DebugUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException {
        try(
                ServerSocket socket = new ServerSocket(9988);
                Socket accept = socket.accept();
                NativeProtocolPacketInputStream in = new NativeProtocolPacketInputStream(accept.getInputStream());
                NativeProtocolPacketOutputStream out = new NativeProtocolPacketOutputStream(accept.getOutputStream());
        ) {
            out.writeNativeProtocolPacket(new NativeProtocolHandshakeV10Packet(1));
            NativeProtocolPacket nativeProtocolPacket = in.readNativeProtocolPacket();

            System.out.println(DebugUtils.dumpAsHex(nativeProtocolPacket.toBytes()));

            out.writeNativeProtocolPacket(new NativeProtocolOKPacket(2));





        }





    }
}