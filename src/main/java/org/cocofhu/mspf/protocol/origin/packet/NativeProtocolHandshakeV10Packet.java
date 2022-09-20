package org.cocofhu.mspf.protocol.origin.packet;

import org.cocofhu.mspf.protocol.origin.NativeProtocolConstants;
import org.cocofhu.mspf.protocol.origin.NativeProtocolPacket;
import org.cocofhu.mspf.protocol.origin.NativeProtocolPacketPayload;

import java.util.Random;

import static org.cocofhu.mspf.protocol.origin.NativeProtocolConstants.CapabilityFlags.*;
import static org.cocofhu.mspf.protocol.origin.NativeProtocolConstants.Charset.UTF_8;

// https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#packet-Protocol::HandshakeV10

/**
 * MySQL 握手协议V10
 */
public class NativeProtocolHandshakeV10Packet extends NativeProtocolPacket {
    // 协议版本号，数据包第一个字节，总是10
    public static final int PROTOCOL_VERSION = 10;
    // 服务名称
    public static final String SERVER_VERSION = "MySQL Proxy Framework";
    // 加密算法
    public static final String DEFAULT_AUTH_PLUGIN_NAME = "caching_sha2_password";
    // 默认标志位
    public static final int DEFAULT_CAPABILITY_FLAGS = CLIENT_LONG_PASSWORD | CLIENT_FOUND_ROWS | CLIENT_LONG_FLAG | CLIENT_NO_SCHEMA |
            CLIENT_IGNORE_SPACE | CLIENT_PROTOCOL_41 | CLIENT_INTERACTIVE | CLIENT_PLUGIN_AUTH | CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA ;

    // 随机密码长度，注意：最小值为20，建议使用20
    public static final int DEFAULT_SCRAMBLE_LENGTH = 20;

    // 用于加密的随机密码
    private final byte[] scrambleData;

    // 连接ID threadId
    private final int connectionId;
    private final int capabilityFlags;
    private final byte charset ;
    private final int statusFlags;


    public NativeProtocolHandshakeV10Packet(int connectionId) {
        // 初始化握手序列号必须为0
        super(0, new NativeProtocolPacketPayload(128));
        scrambleData = new byte[DEFAULT_SCRAMBLE_LENGTH + 1];
        Random random = new Random();
        for(int i = 0 ; i < DEFAULT_SCRAMBLE_LENGTH ; ++i){
            scrambleData[i] = (byte) random.nextInt();
        }
        this.capabilityFlags = DEFAULT_CAPABILITY_FLAGS;
        this.connectionId = connectionId;
        this.charset = (byte) UTF_8;
        this.statusFlags = 0;

        initPayload();
    }

    private void initPayload(){
        NativeProtocolPacketPayload payload = message;
        // 第一个字节为协议版本号
        payload.writeInteger(NativeProtocolConstants.IntegerDataType.INT1, PROTOCOL_VERSION);
        // 服务名称
        payload.writeBytes(NativeProtocolConstants.StringSelfDataType.STRING_TERM,SERVER_VERSION.getBytes());
        // 连接ID
        payload.writeInteger(NativeProtocolConstants.IntegerDataType.INT4,connectionId);
        // 随机挑战前8个字节
        payload.writeBytes(NativeProtocolConstants.StringLengthDataType.STRING_FIXED, scrambleData, 0, 8);
        // 没用的一个字节对填充
        payload.writeInteger(NativeProtocolConstants.IntegerDataType.INT1,0);
        // 服务器兼容性标志位低两位
        payload.writeInteger(NativeProtocolConstants.IntegerDataType.INT2,capabilityFlags);
        // 字符集
        payload.writeInteger(NativeProtocolConstants.IntegerDataType.INT1,charset);
        // 服务器状态
        payload.writeInteger(NativeProtocolConstants.IntegerDataType.INT2,statusFlags);
        // 服务器兼容性标志位高两位
        payload.writeInteger(NativeProtocolConstants.IntegerDataType.INT2,capabilityFlags>>>16);
        // 随机挑战长度，加上最后的0x00和前面的8个字节 如果使用默认值的话这里应该是21(0x15)
        payload.writeInteger(NativeProtocolConstants.IntegerDataType.INT1,scrambleData.length);
        // 10个字节的0填充
        payload.writeInteger(NativeProtocolConstants.IntegerDataType.INT8,0);
        payload.writeInteger(NativeProtocolConstants.IntegerDataType.INT2,0);
        // 剩余的随机挑战
        payload.writeBytes(NativeProtocolConstants.StringLengthDataType.STRING_FIXED, scrambleData, 8, scrambleData.length - 8);
        // 加密算法(plugin)
        payload.writeBytes(NativeProtocolConstants.StringSelfDataType.STRING_TERM,DEFAULT_AUTH_PLUGIN_NAME.getBytes());

    }
}
