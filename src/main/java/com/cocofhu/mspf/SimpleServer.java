package com.cocofhu.mspf;

import com.cocofhu.mspf.protocol.origin.NativeProtocolPacket;
import com.cocofhu.mspf.protocol.origin.NativeProtocolPacketInputStream;
import com.cocofhu.mspf.protocol.origin.NativeProtocolPacketOutputStream;
import com.cocofhu.mspf.protocol.origin.packet.NativeProtocolHandshakeResponse41Packet;
import com.cocofhu.mspf.protocol.origin.packet.NativeProtocolHandshakeV10Packet;
import com.cocofhu.mspf.protocol.origin.packet.NativeProtocolOKPacket;
import com.cocofhu.mspf.util.DebugUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class SimpleServer {

    static class DefaultHandler implements Runnable{
        // client Socket
        private final Socket socket;
        private final int connectionId;
        private NativeProtocolPacketInputStream in ;
        private NativeProtocolPacketOutputStream out;


        public DefaultHandler(Socket socket, int connectionId) {
            this.socket = socket;
            this.connectionId = connectionId;
            System.out.println(connectionId);
        }

        public static final String PASSWORD = "123456";

        @Override
        public void run() {
            try {
                this.out = new NativeProtocolPacketOutputStream(socket.getOutputStream());
                this.in = new NativeProtocolPacketInputStream(socket.getInputStream());
                // 握手
                NativeProtocolHandshakeV10Packet initialPacket = new NativeProtocolHandshakeV10Packet(this.connectionId);
                this.out.writeNativeProtocolPacket(initialPacket);
                log.debug("send handshake packet, connectionId = {} : {}.", this.connectionId, initialPacket);
                // 鉴权
                NativeProtocolPacket nativeProtocolPacket = in.readNativeProtocolPacket();


                NativeProtocolHandshakeResponse41Packet authResponse = new NativeProtocolHandshakeResponse41Packet(nativeProtocolPacket);
                log.debug("read response of handshake packet from server, connectionId = {} : {}.", this.connectionId, authResponse);

                byte[] authData = scramble411(PASSWORD.getBytes(), initialPacket.getScrambleData());

                log.debug("client auth data, connectionId = {}: {}.", this.connectionId, Arrays.toString(authResponse.getAuthResponse()));
                log.debug("database auth data, connectionId = {} : {}.", this.connectionId, Arrays.toString(authData));

                out.writeNativeProtocolPacket(new NativeProtocolOKPacket(2));

                log.info("send auth ok, connectionId = {}.", this.connectionId);


            } catch (Exception e) {
                log.debug("exception occurred connectionId = {}", this.connectionId);
            } finally {
                if(this.out != null) {
                    try {
                        this.out.close();
                    } catch (IOException ignored) {

                    }
                }
                if(this.in != null) {
                    try {
                        this.in.close();
                    } catch (IOException ignored) {

                    }
                }
                if(this.socket != null) {
                    try {
                        this.socket.close();
                    } catch (IOException ignored) {

                    }
                }
                log.info("client was disconnected, connectionId = {}", this.connectionId);
            }

        }
    }

    static AtomicInteger connectionNextId = new AtomicInteger();
    public static void main(String[] args) throws IOException {

        try(
                ServerSocket socket = new ServerSocket(9988);
        ) {
            while (true){
                Socket accept = socket.accept();
                Thread thread = new Thread(new DefaultHandler(accept, connectionNextId.getAndIncrement()));
                thread.start();
            }





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
