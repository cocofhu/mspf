package com.cocofhu.mspf.protocol.debug;

import com.cocofhu.mspf.protocol.MySQLProtocolPacket;
import com.cocofhu.mspf.protocol.MySQLProtocolPacketInputStream;
import com.cocofhu.mspf.protocol.MySQLProtocolPacketOutputStream;
import com.cocofhu.mspf.util.DebugUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class DebugProxy implements Runnable {
    private static final int LISTENED_PORT = 9988;
    private static final int MYSQL_PORT = 3399;
    private static final String MYSQL_IP = "127.0.0.1";
    private final MySQLProtocolPacketInputStream in;
    private final MySQLProtocolPacketOutputStream out;

    private final Socket socket;
    private final Socket proxy;

    private final String name;

    public DebugProxy(MySQLProtocolPacketInputStream in, MySQLProtocolPacketOutputStream out, Socket socket, Socket proxy, String name) {
        this.in = in;
        this.out = out;
        this.socket = socket;
        this.proxy = proxy;
        this.name = name;
    }


    public static void main(String[] args) {
        log.info("mysql packet proxy started. mysql ip = {}, mysql port = {}, listened port = {}", MYSQL_IP, MYSQL_PORT, LISTENED_PORT);
        log.info("using mysql -h 127.0.0.1 -P{} -u root -proot --ssl-mode=DISABLE to debugging", LISTENED_PORT);
        int maxClient = 99999;
        try (ServerSocket serverSocket = new ServerSocket(LISTENED_PORT)) {
            // 我只是想去掉警告
            while (maxClient >= 0) {
                Socket socket = serverSocket.accept();
                Socket proxy = new Socket(MYSQL_IP, MYSQL_PORT);
                Thread t1 = new Thread(new DebugProxy(new MySQLProtocolPacketInputStream(socket.getInputStream()), new MySQLProtocolPacketOutputStream(proxy.getOutputStream()), socket, proxy, "Server"));
                Thread t2 = new Thread(new DebugProxy(new MySQLProtocolPacketInputStream(proxy.getInputStream()), new MySQLProtocolPacketOutputStream(socket.getOutputStream()), proxy, socket, "Client"));
                t1.start();
                t2.start();
                log.info("someone connected, 2 thread started for debugging.");
                --maxClient;
            }
        } catch (Exception e) {
            log.error("fatal error, system exited, message:{}", e.getMessage());
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                MySQLProtocolPacket packet = in.readPacket();
                out.writePacket(packet);
                log.info("packet read from {} \n\n{}\n\n", name, DebugUtils.dumpAsHex(packet.toBytes()));
            } catch (IOException e) {
                log.info("an error occurred, stop 2 connections, message: {}.", e.getMessage());
                try {
                    in.close();
                    out.close();
                    socket.close();
                    proxy.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            }
        }
    }
}
