package org.cocofhu.mspf.protocol;

import org.cocofhu.mspf.protocol.origin.NativeProtocolConstants;
import org.cocofhu.mspf.protocol.origin.NativeProtocolPacketPayload;

import java.util.Random;

import static org.cocofhu.mspf.protocol.origin.NativeProtocolConstants.CapabilityFlags.*;

// https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#packet-Protocol::HandshakeV10

/**
 * MySQL 握手协议V10
 */
public class HandshakeV10 {
    // 协议版本号，数据包第一个字节，总是10
    public static final int PROTOCOL_VERSION = 10;
    // 服务名称
    public static final String SERVER_VERSION = "MySQL Proxy Framework";
    // 加密算法
    public static final String DEFAULT_AUTH_PLUGIN_NAME = "caching_sha2_password";
    // 默认标志位
    public static final int DEFAULT_CAPABILITY_FLAGS = CLIENT_LONG_PASSWORD | CLIENT_FOUND_ROWS | CLIENT_LONG_FLAG | CLIENT_NO_SCHEMA |
            CLIENT_IGNORE_SPACE | CLIENT_PROTOCOL_41 | CLIENT_INTERACTIVE | CLIENT_PLUGIN_AUTH | CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA ;

    // 随机密码长度
    public static final int DEFAULT_SCRAMBLE_LENGTH = 20;

    // 用于加密的随机密码
    private final byte[] scrambleData;

    //

    // 连接ID threadId
    private final int connectionId;
    private final int capabilityFlags;
    private final byte charset = (byte) 255;
    private final int statusFlags = 2;


    public HandshakeV10(int connectionId) {
        scrambleData = new byte[DEFAULT_SCRAMBLE_LENGTH + 1];
        Random random = new Random();
        for(int i = 0 ; i < DEFAULT_SCRAMBLE_LENGTH ; ++i){
            scrambleData[i] = (byte) random.nextInt();
        }
        this.capabilityFlags = DEFAULT_CAPABILITY_FLAGS;
        this.connectionId = connectionId;
    }

    public byte[] toBytes(){
        NativeProtocolPacketPayload payload = new NativeProtocolPacketPayload(128);
        // 第一个字节为协议版本号
        payload.writeInteger(NativeProtocolConstants.IntegerDataType.INT1, PROTOCOL_VERSION);
        // 服务名称
        payload.writeBytes(NativeProtocolConstants.StringSelfDataType.STRING_TERM,SERVER_VERSION.getBytes());
        // ConnectionId
        payload.writeInteger(NativeProtocolConstants.IntegerDataType.INT4,connectionId);
        //
        payload.writeBytes(NativeProtocolConstants.StringLengthDataType.STRING_FIXED, scrambleData, 0, 8);
        // filler
        payload.writeInteger(NativeProtocolConstants.IntegerDataType.INT1,0);

        payload.writeInteger(NativeProtocolConstants.IntegerDataType.INT2,capabilityFlags);

        // charset
        payload.writeInteger(NativeProtocolConstants.IntegerDataType.INT1,charset);

        payload.writeInteger(NativeProtocolConstants.IntegerDataType.INT2,statusFlags);

        payload.writeInteger(NativeProtocolConstants.IntegerDataType.INT2,capabilityFlags>>>16);

        payload.writeInteger(NativeProtocolConstants.IntegerDataType.INT1,scrambleData.length);

        payload.writeInteger(NativeProtocolConstants.IntegerDataType.INT8,0);
        payload.writeInteger(NativeProtocolConstants.IntegerDataType.INT2,0);

        payload.writeBytes(NativeProtocolConstants.StringLengthDataType.STRING_FIXED, scrambleData, 8, scrambleData.length - 8);


        payload.writeBytes(NativeProtocolConstants.StringSelfDataType.STRING_TERM,DEFAULT_AUTH_PLUGIN_NAME.getBytes());

        return payload.getByteBuffer();

    }
}
