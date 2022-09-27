package com.cocofhu.mspf.protocol;

import com.cocofhu.mspf.protocol.packet.MySQLProtocolErrorPacket;
import com.cocofhu.mspf.protocol.packet.MySQLProtocolHandshakeResponse41Packet;
import com.cocofhu.mspf.protocol.packet.MySQLProtocolHandshakeV10Packet;
import com.cocofhu.mspf.protocol.packet.MySQLProtocolOKPacket;
import com.cocofhu.mspf.protocol.plugin.auth.MySQLProtocolAuth;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MySQLServer {
    private final AtomicInteger connectionIdPool = new AtomicInteger(0);
    @Setter
    private volatile boolean running;
    private final MySQLProtocolAuth defaultAuth;
    private final ServerSocket socket;

    public MySQLServer(MySQLProtocolAuth defaultAuth, int port) throws IOException {
        this.defaultAuth = defaultAuth;
        this.socket = new ServerSocket(port);
        this.running = false;
    }

    public void listen() throws IOException {
        this.running = true;
        while (running) {
            int connectionId = this.connectionIdPool.get();
            Socket client;
            try {
                client = socket.accept();
            } catch (IOException e) {
                log.error("fatal error, server stopped: {}", e.getMessage());
                running = false;
                throw e;
            }
            try {
                log.debug("a client connected from {} with connection id : {}.", client.getInetAddress().toString(), connectionId);
                MySQLProtocolPacketInputStream in = new MySQLProtocolPacketInputStream(client.getInputStream());
                MySQLProtocolPacketOutputStream out = new MySQLProtocolPacketOutputStream(client.getOutputStream());
                MySQLProtocolHandshakeV10Packet handShakePacket = new MySQLProtocolHandshakeV10Packet(connectionId, defaultAuth);
                log.debug("send handshake packet, connectionId = {} : {}.", connectionId, handShakePacket);
                out.writePacket(handShakePacket);
                MySQLProtocolPacket rawResponse = in.readPacket();
                MySQLProtocolHandshakeResponse41Packet responsePacket = new MySQLProtocolHandshakeResponse41Packet(rawResponse);
                log.debug("read response of handshake packet from client, connectionId = {} : {}.", connectionId, responsePacket);
                MySQLProtocolSession session = new MySQLProtocolSession(client, in, out, responsePacket.getClientFlags(), responsePacket.getCharset(), defaultAuth);
                boolean auth = defaultAuth.doAuthentication(session, handShakePacket, responsePacket);
                if (!auth) {
                    log.debug("authentication failed connectionId = {}. disconnecting socket.", connectionId);
                    MySQLProtocolErrorPacket errorPacket = new MySQLProtocolErrorPacket(2, MySQLProtocolConstants.ErrorCode.INVALID_AUTH_DATA, "incorrect username or password.", session);
                    out.writePacket(errorPacket);
                    try {
                        session.close();
                    } catch (IOException ignore) {
                    }
                } else {
                    log.debug("authentication passed connectionId = {}.", connectionId);
                    MySQLProtocolOKPacket okPacket = new MySQLProtocolOKPacket(2, 0, 0, 0, 0, session);
                    out.writePacket(okPacket);
                    session.setPhase(MySQLProtocolConstants.SessionPhase.COMMAND);
                    connectionIdPool.incrementAndGet();
                }
            } catch (IOException e) {
                log.warn("client connected failed, connectionId = {}, error message = {}. ", connectionId, e.getMessage());
            }
        }
    }


}
