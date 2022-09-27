package com.cocofhu.mspf.protocol;

import com.cocofhu.mspf.protocol.plugin.auth.MySQLProtocolAuth;
import lombok.Getter;
import lombok.Setter;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;

public class MySQLProtocolSession implements Closeable {

    @Getter
    @Setter
    private MySQLProtocolConstants.SessionPhase phase;
    @Getter
    private final Socket socket;
    @Getter
    private final MySQLProtocolPacketInputStream packetInputStream;
    @Getter
    private final MySQLProtocolPacketOutputStream packetOutputStream;
    @Getter
    private final int clientFlags;
    @Getter
    private final Charset defaultCharset;
    @Getter
    private final MySQLProtocolAuth protocolAuth;


    public MySQLProtocolSession(Socket socket, MySQLProtocolPacketInputStream packetInputStream, MySQLProtocolPacketOutputStream packetOutputStream, int clientFlags, Charset defaultCharset, MySQLProtocolAuth protocolAuth){
        this.socket = socket;
        this.packetInputStream = packetInputStream;
        this.packetOutputStream = packetOutputStream;
        this.clientFlags = clientFlags;
        this.defaultCharset = defaultCharset;
        this.protocolAuth = protocolAuth;
        phase = MySQLProtocolConstants.SessionPhase.CONNECTION;
    }

    @Override
    public void close() throws IOException {
        phase = MySQLProtocolConstants.SessionPhase.DISCONNECTED;
        if(socket!= null){
            socket.close();
        }
        if(packetOutputStream != null){
            packetOutputStream.close();
        }
        if(packetInputStream != null){
            packetInputStream.close();
        }

    }
}
