package com.cocofhu.mspf;

import com.cocofhu.mspf.protocol.MySQLServer;
import com.cocofhu.mspf.protocol.plugin.auth.MySQLNativePasswordAuth;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        MySQLServer server = new MySQLServer(new MySQLNativePasswordAuth(), 8888);
        server.listen();


    }
}