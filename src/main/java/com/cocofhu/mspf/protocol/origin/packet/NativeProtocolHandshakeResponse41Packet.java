package com.cocofhu.mspf.protocol.origin.packet;

import com.cocofhu.mspf.exception.CharsetNotSupportedException;
import com.cocofhu.mspf.exception.MinimumCapabilityException;
import com.cocofhu.mspf.protocol.origin.NativeProtocolConstants;
import com.cocofhu.mspf.protocol.origin.NativeProtocolPacket;
import com.cocofhu.mspf.protocol.origin.NativeProtocolPacketHeader;
import com.cocofhu.mspf.protocol.origin.NativeProtocolPacketPayload;
import com.cocofhu.mspf.util.DebugUtils;
import lombok.Getter;

import java.util.Arrays;

import static com.cocofhu.mspf.protocol.origin.NativeProtocolConstants.CapabilityFlags.*;

public class NativeProtocolHandshakeResponse41Packet extends NativeProtocolPacket {

    // 注意：CLIENT_PLUGIN_AUTH,CLIENT_PROTOCOL_41,CLIENT_PLUGIN_AUTH,CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA 这四个必须要有，否则将影响数据包的解析
    // https://dev.mysql.com/doc/dev/mysql-server/latest/page_protocol_connection_phase_packets_protocol_handshake_response.html
    private static final int MIN_CLIENT_FLAGS = CLIENT_LONG_PASSWORD | CLIENT_LONG_FLAG | CLIENT_PROTOCOL_41 | CLIENT_INTERACTIVE | CLIENT_TRANSACTIONS | CLIENT_SECURE_CONNECTION | CLIENT_PLUGIN_AUTH | CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA;

    // 目前仅支持utf-8
    private static final int ONLY_SUPPORTED_CHARSET = NativeProtocolConstants.Charset.UTF_8;
    private static final String ONLY_SUPPORTED_CHARSET_NAME = "utf-8";

    // the Authentication Method used by the client to generate auth-response value in this packet. This is an UTF-8 string.
    // https://dev.mysql.com/doc/dev/mysql-server/latest/page_protocol_connection_phase_packets_protocol_handshake_response.html
    // 鉴权插件名称需要用指定的字符编码解析
    private static final String CLIENT_PLUGIN_AUTH_CHARSET = "utf-8";


    @Getter
    private final int clientFlags;
    @Getter
    private final long maxPacketSize;
    @Getter
    private final int charset;
    @Getter
    private final String username;
    @Getter
    private final byte[] authResponse;
    @Getter
    private final String database;
    @Getter
    private final String pluginName;

    //
    private final NativeProtocolPacketHeader header;


    /**
     * 通过 sequenceId 和消息载体创建一个MySQL包
     */
    public NativeProtocolHandshakeResponse41Packet(NativeProtocolPacket packet) {
        super(packet.getHeader().getMessageSequence(), packet.getMessage());
        this.header = packet.getHeader();
        // 客户端兼容性标志
        this.clientFlags = (int) message.readInteger(NativeProtocolConstants.IntegerDataType.INT4);

        // 检查最低兼容标准
        if ((MIN_CLIENT_FLAGS & clientFlags) != MIN_CLIENT_FLAGS) {
            throw new MinimumCapabilityException(MIN_CLIENT_FLAGS, this.clientFlags);
        }

        // 最大支持的数据包大小
        this.maxPacketSize = message.readInteger(NativeProtocolConstants.IntegerDataType.INT4);
        // 字符集
        this.charset = (int) message.readInteger(NativeProtocolConstants.IntegerDataType.INT1);

        // 这里后期支持多字符集 目前仅支持utf-8
        if (this.charset != ONLY_SUPPORTED_CHARSET) {
            throw new CharsetNotSupportedException(this.charset);
        }

        String charsetName = ONLY_SUPPORTED_CHARSET_NAME;

        // 跳过23个字节的填充
        message.readBytes(NativeProtocolConstants.StringLengthDataType.STRING_FIXED, 23);

        // 读取用户名
        this.username = message.readString(NativeProtocolConstants.StringSelfDataType.STRING_TERM, charsetName);

        // CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA
        this.authResponse = message.readBytes(NativeProtocolConstants.StringSelfDataType.STRING_LENENC);

        if ((clientFlags & CLIENT_CONNECT_WITH_DB) != 0) {
            this.database = message.readString(NativeProtocolConstants.StringSelfDataType.STRING_TERM, charsetName);
        } else {
            this.database = null;
        }

        // CLIENT_PLUGIN_AUTH
        this.pluginName = message.readString(NativeProtocolConstants.StringSelfDataType.STRING_TERM, CLIENT_PLUGIN_AUTH_CHARSET);
    }



    @Override
    public NativeProtocolPacketHeader getHeader() {
        return header;
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
