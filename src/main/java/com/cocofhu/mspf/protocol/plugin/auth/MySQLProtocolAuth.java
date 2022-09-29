package com.cocofhu.mspf.protocol.plugin.auth;

import com.cocofhu.mspf.protocol.MySQLProtocolSession;
import com.cocofhu.mspf.protocol.packet.MySQLProtocolHandshakeResponse41Packet;
import com.cocofhu.mspf.protocol.packet.MySQLProtocolHandshakeV10Packet;

public interface MySQLProtocolAuth {
    String getAuthPluginName();
    boolean doAuthentication(MySQLProtocolSession session, MySQLProtocolHandshakeV10Packet handshakePacket, MySQLProtocolHandshakeResponse41Packet responsePacket);
}
