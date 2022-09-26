package com.cocofhu.mspf.protocol.packet;

import com.cocofhu.mspf.protocol.MySQLProtocolConstants;
import com.cocofhu.mspf.protocol.MySQLProtocolPacket;
import com.cocofhu.mspf.protocol.MySQLProtocolPacketPayload;
import com.cocofhu.mspf.util.DebugUtils;

import java.util.Arrays;
import java.util.Random;


// https://dev.mysql.com/doc/internals/en/connection-phase-packets.html#packet-Protocol::HandshakeV10

/**
 * MySQL 握手协议V10
 */
public class NativeProtocolHandshakeV10Packet extends MySQLProtocolPacket {
    // 协议版本号，数据包第一个字节，总是10
    public static final int PROTOCOL_VERSION = 10;
    // 服务名称
    public static final String SERVER_VERSION = "Coco's SQL Proxy Server";
    // 加密算法
    public static final String DEFAULT_AUTH_PLUGIN_NAME = "mysql_native_password";
    // 默认标志位
    public static final int DEFAULT_CAPABILITY_FLAGS = MySQLProtocolConstants.CapabilityFlags.CLIENT_LONG_PASSWORD | MySQLProtocolConstants.CapabilityFlags.CLIENT_FOUND_ROWS | MySQLProtocolConstants.CapabilityFlags.CLIENT_LONG_FLAG | MySQLProtocolConstants.CapabilityFlags.CLIENT_NO_SCHEMA |
            MySQLProtocolConstants.CapabilityFlags.CLIENT_IGNORE_SPACE | MySQLProtocolConstants.CapabilityFlags.CLIENT_PROTOCOL_41 | MySQLProtocolConstants.CapabilityFlags.CLIENT_INTERACTIVE | MySQLProtocolConstants.CapabilityFlags.CLIENT_PLUGIN_AUTH | MySQLProtocolConstants.CapabilityFlags.CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA | MySQLProtocolConstants.CapabilityFlags.CLIENT_SECURE_CONNECTION;

    // 随机密码长度，注意：最小值为20，建议使用20
    public static final int DEFAULT_SCRAMBLE_LENGTH = 20;

    // 用于加密的随机密码
    private final byte[] scrambleData;

    private final String serverVersion;
    private final String authPluginName;

    // 连接ID threadId
    private final int connectionId;
    // 服务器兼容性标志位
    private final int capabilityFlags;
    // 服务器使用的字符集
    private final byte charset ;
    // 服务器的状态标志
    private final int statusFlags;


    public NativeProtocolHandshakeV10Packet(int connectionId) {
        // 初始化握手序列号必须为0
        super(0, new MySQLProtocolPacketPayload(128));
        this.scrambleData = new byte[DEFAULT_SCRAMBLE_LENGTH + 1];
        Random random = new Random();
        for(int i = 0 ; i < DEFAULT_SCRAMBLE_LENGTH ; ++i){
            this.scrambleData[i] = (byte) random.nextInt();
        }
        this.authPluginName = DEFAULT_AUTH_PLUGIN_NAME;
        this.serverVersion = SERVER_VERSION;
        this.capabilityFlags = DEFAULT_CAPABILITY_FLAGS;
        this.connectionId = connectionId;
        this.charset = (byte) MySQLProtocolConstants.Charset.UTF_8;
        this.statusFlags = 0;
        initPayload();
    }

    private void initPayload(){
        // 第一个字节为协议版本号
        payload.writeInteger(MySQLProtocolConstants.IntegerDataType.INT1, PROTOCOL_VERSION);
        // 服务名称
        payload.writeBytes(MySQLProtocolConstants.StringSelfDataType.STRING_TERM,serverVersion.getBytes());
        // 连接ID
        payload.writeInteger(MySQLProtocolConstants.IntegerDataType.INT4,connectionId);
        // 随机挑战前8个字节
        payload.writeBytes(MySQLProtocolConstants.StringLengthDataType.STRING_FIXED, scrambleData, 0, 8);
        // 没用的一个字节对填充
        payload.writeInteger(MySQLProtocolConstants.IntegerDataType.INT1,0);
        // 服务器兼容性标志位低两位
        payload.writeInteger(MySQLProtocolConstants.IntegerDataType.INT2,capabilityFlags);
        // 字符集
        payload.writeInteger(MySQLProtocolConstants.IntegerDataType.INT1,charset);
        // 服务器状态
        payload.writeInteger(MySQLProtocolConstants.IntegerDataType.INT2,statusFlags);
        // 服务器兼容性标志位高两位
        payload.writeInteger(MySQLProtocolConstants.IntegerDataType.INT2,capabilityFlags>>>16);
        // 随机挑战长度，加上最后的0x00和前面的8个字节 如果使用默认值的话这里应该是21(0x15)
        payload.writeInteger(MySQLProtocolConstants.IntegerDataType.INT1,scrambleData.length);
        // 10个字节的0填充
        payload.writeInteger(MySQLProtocolConstants.IntegerDataType.INT8,0);
        payload.writeInteger(MySQLProtocolConstants.IntegerDataType.INT2,0);
        // 剩余的随机挑战
        payload.writeBytes(MySQLProtocolConstants.StringLengthDataType.STRING_FIXED, scrambleData, 8, scrambleData.length - 8);
        // 加密算法(plugin)
        payload.writeBytes(MySQLProtocolConstants.StringSelfDataType.STRING_TERM,authPluginName.getBytes());
    }

    /**
     * 获得随机挑战（加密种子）的一份拷贝
     */
    public byte[] getScrambleData() {
        // 应该去掉最后的[0x00]
        int scrambleDataValidLength = scrambleData.length - 1;
        byte[] seed = new byte[scrambleDataValidLength];
        System.arraycopy(scrambleData, 0, seed, 0, scrambleDataValidLength);
        return seed;
    }

    @Override
    public String toString() {
        return "NativeProtocolHandshakeV10Packet{" +
                "scrambleData=" + Arrays.toString(scrambleData) +
                ", serverVersion='" + serverVersion + '\'' +
                ", authPluginName='" + authPluginName + '\'' +
                ", connectionId=" + connectionId +
                ", capabilityFlags=" + DebugUtils.listAllCapabilityFlags(capabilityFlags) +
                ", charset=" + charset +
                ", statusFlags=" + statusFlags +
                ", sequenceId=" + sequenceId +
                ", message=" + payload +
                '}';
    }
}
