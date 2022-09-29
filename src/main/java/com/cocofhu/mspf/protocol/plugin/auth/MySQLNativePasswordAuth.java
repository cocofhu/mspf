package com.cocofhu.mspf.protocol.plugin.auth;

import com.cocofhu.mspf.exception.MinimumCapabilityException;
import com.cocofhu.mspf.protocol.MySQLProtocolSession;
import com.cocofhu.mspf.protocol.packet.MySQLProtocolHandshakeResponse41Packet;
import com.cocofhu.mspf.protocol.packet.MySQLProtocolHandshakeV10Packet;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.cocofhu.mspf.protocol.MySQLProtocolConstants.CapabilityFlags.*;
import static com.cocofhu.mspf.protocol.MySQLProtocolConstants.CapabilityFlags.CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA;

@Slf4j
public class MySQLNativePasswordAuth implements MySQLProtocolAuth{

    private static final int MIN_CLIENT_CAPABILITIES = CLIENT_LONG_PASSWORD | CLIENT_LONG_FLAG | CLIENT_PROTOCOL_41  | CLIENT_SECURE_CONNECTION | CLIENT_PLUGIN_AUTH | CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA;

    private static Map<String,String> users ;
    static {
        // you can load some users from file
        // but for testing, we'll put some users
        users = new HashMap<>();
        users.put("root", "123456");
        users.put("cocofhu", "334455");
    }

    @Override
    public String getAuthPluginName() {
        return "mysql_native_password";
    }

    @Override
    public boolean doAuthentication(MySQLProtocolSession session, MySQLProtocolHandshakeV10Packet handshakePacket, MySQLProtocolHandshakeResponse41Packet responsePacket) {
        int clientFlags = responsePacket.getClientFlags();

        if((clientFlags & MIN_CLIENT_CAPABILITIES) != MIN_CLIENT_CAPABILITIES){
            throw new MinimumCapabilityException(MIN_CLIENT_CAPABILITIES, clientFlags);
        }

        if(!getAuthPluginName().equals(responsePacket.getPluginName())){
            log.debug("client auth plugin not match, required: {}, actual: {}. ", getAuthPluginName(), responsePacket.getPluginName());
            return false;
        }

        String username = responsePacket.getUsername();
        String rawPass = users.get(username);
        if(username == null || rawPass == null){
            log.debug("invalid username: {}, not exist. ", username);
            return false;
        }

        // check password
        byte[] seed = handshakePacket.getScrambleData();
        byte[] realPass = scramble411(rawPass.getBytes(), seed);

        return Arrays.equals(realPass, responsePacket.getAuthResponse());
    }

    public byte[] scramble411(byte[] password, byte[] seed) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            throw new UnsupportedOperationException("");
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
