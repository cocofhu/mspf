package com.cocofhu.mspf;

import com.cocofhu.mspf.protocol.cs.NativeProtocolPacket;
import com.cocofhu.mspf.protocol.cs.NativeProtocolPacketInputStream;
import com.cocofhu.mspf.protocol.cs.NativeProtocolPacketOutputStream;
import com.cocofhu.mspf.protocol.cs.packet.NativeProtocolHandshakeResponse41Packet;
import com.cocofhu.mspf.protocol.cs.packet.NativeProtocolHandshakeV10Packet;
import com.cocofhu.mspf.protocol.cs.packet.NativeProtocolOKPacket;
import com.cocofhu.mspf.util.DebugUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws IOException {

        try(
                ServerSocket socket = new ServerSocket(9988);
                Socket accept = socket.accept();
                NativeProtocolPacketInputStream in = new NativeProtocolPacketInputStream(accept.getInputStream());
                NativeProtocolPacketOutputStream out = new NativeProtocolPacketOutputStream(accept.getOutputStream());
        ) {
            // 握手
            NativeProtocolHandshakeV10Packet first = new NativeProtocolHandshakeV10Packet(1);
            out.writeNativeProtocolPacket(first);
            NativeProtocolPacket nativeProtocolPacket = in.readNativeProtocolPacket();
            System.out.println(new NativeProtocolHandshakeResponse41Packet(nativeProtocolPacket));
            System.out.println(DebugUtils.dumpAsHex(nativeProtocolPacket.toBytes()));

            byte[] seed = new byte[20];
            System.arraycopy(first.getScrambleData(), 0, seed, 0, seed.length);
            System.out.println(DebugUtils.dumpAsHex(scramble411("1111".getBytes(), seed)));


            out.writeNativeProtocolPacket(new NativeProtocolOKPacket(2));





        }





    }

    public static byte[] scramble411(byte[] password, byte[] seed) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
//            throw new AssertionFailedException(ex);
        }

        byte[] passwordHashStage1 = md.digest(password);
        md.reset();

        byte[] passwordHashStage2 = md.digest(passwordHashStage1);
        md.reset();

        md.update(seed);
        md.update(passwordHashStage2);

        byte[] toBeXord = md.digest();

        int numToXor = toBeXord.length;

        for (int i = 0; i < numToXor; i++) {
            toBeXord[i] = (byte) (toBeXord[i] ^ passwordHashStage1[i]);
        }

        return toBeXord;
    }
}