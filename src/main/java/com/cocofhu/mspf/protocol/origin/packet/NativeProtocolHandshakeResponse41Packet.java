package com.cocofhu.mspf.protocol.origin.packet;

import com.cocofhu.mspf.protocol.origin.NativeProtocolConstants;
import com.cocofhu.mspf.protocol.origin.NativeProtocolPacket;
import com.cocofhu.mspf.protocol.origin.NativeProtocolPacketHeader;
import com.cocofhu.mspf.protocol.origin.NativeProtocolPacketPayload;
import com.cocofhu.mspf.util.DebugUtils;

import java.util.Arrays;

import static com.cocofhu.mspf.protocol.origin.NativeProtocolConstants.CapabilityFlags.CLIENT_CONNECT_WITH_DB;

public class NativeProtocolHandshakeResponse41Packet extends NativeProtocolPacket {


    private int clientFlags;
    private long maxPacketSize;
    private int charset;
    private String username;
    private byte[] authResponse;
    private String database;
    private String pluginName;



    /**
     * 通过 sequenceId 和消息载体创建一个MySQL包
     */
    public NativeProtocolHandshakeResponse41Packet(NativeProtocolPacket packet) {
        super(packet.getHeader().getMessageSequence(), packet.getMessage());
        message.setByteArrayChangedListener(this::reloadFields);
        reloadFields();
    }

    protected void reloadFields() {
        // 客户端兼容性标志
        this.clientFlags = (int) message.readInteger(NativeProtocolConstants.IntegerDataType.INT4);
        // 最大支持的数据包大小
        this.maxPacketSize = message.readInteger(NativeProtocolConstants.IntegerDataType.INT4);
        // 字符集
        this.charset = (int) message.readInteger(NativeProtocolConstants.IntegerDataType.INT1);

        // 跳过23个字节的填充
        message.readBytes(NativeProtocolConstants.StringLengthDataType.STRING_FIXED, 23);

        // 用户名 这里实际上需要使用前面给定的字符集进行读取
        this.username = message.readString(NativeProtocolConstants.StringSelfDataType.STRING_TERM,"utf-8");

        // CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA 必须
        this.authResponse = message.readBytes(NativeProtocolConstants.StringSelfDataType.STRING_LENENC);

        if((clientFlags & CLIENT_CONNECT_WITH_DB)!=0){
            this.database = message.readString(NativeProtocolConstants.StringSelfDataType.STRING_TERM, "utf-8");
        }

        // CLIENT_PLUGIN_AUTH required
        this.pluginName = message.readString(NativeProtocolConstants.StringSelfDataType.STRING_TERM, "utf-8");



    }

    @Override
    public String toString() {
        return "NativeProtocolHandshakeResponse41Packet{" +
                "clientFlags=" + DebugUtils.listAllCapabilityFlags(clientFlags) +
                ", maxPacketSize=" + maxPacketSize +
                ", charset=" + charset +
                ", username='" + username + '\'' +
                ", authResponse=" + Arrays.toString(authResponse) +
                ", database='" + database + '\'' +
                ", pluginName='" + pluginName + '\'' +
                '}';
    }
}
