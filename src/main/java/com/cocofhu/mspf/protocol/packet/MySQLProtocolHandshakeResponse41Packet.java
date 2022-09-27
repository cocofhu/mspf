package com.cocofhu.mspf.protocol.packet;

import com.cocofhu.mspf.exception.CharsetNotSupportedException;
import com.cocofhu.mspf.exception.MinimumCapabilityException;
import com.cocofhu.mspf.protocol.MySQLProtocolPacket;
import com.cocofhu.mspf.protocol.MySQLProtocolConstants;
import com.cocofhu.mspf.util.DebugUtils;
import lombok.Getter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

import static com.cocofhu.mspf.protocol.MySQLProtocolConstants.CapabilityFlags.*;

public class MySQLProtocolHandshakeResponse41Packet extends MySQLProtocolPacket {

    // Required flags:
    // CLIENT_PLUGIN_AUTH,
    // CLIENT_PROTOCOL_41,
    // CLIENT_PLUGIN_AUTH,
    // CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA
    // https://dev.mysql.com/doc/dev/mysql-server/latest/page_protocol_connection_phase_packets_protocol_handshake_response.html
    // CLIENT_INTERACTIVE | CLIENT_TRANSACTIONS JDBC is not in mysql jdbc driver
    private static final int MIN_CLIENT_FLAGS = CLIENT_LONG_PASSWORD | CLIENT_LONG_FLAG | CLIENT_PROTOCOL_41  | CLIENT_SECURE_CONNECTION | CLIENT_PLUGIN_AUTH | CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA;

    // 目前仅支持utf-8
    private static final HashMap<Integer,Charset> ONLY_SUPPORTED_CHARSET;
    static {
        ONLY_SUPPORTED_CHARSET = new HashMap<>();
        // UTF-8
        MySQLProtocolConstants.UTF8_COLLATION_NAME.forEach((i,s)->ONLY_SUPPORTED_CHARSET.put(i,StandardCharsets.UTF_8));
    }

    // the Authentication Method used by the client to generate auth-response value in this packet. This is an UTF-8 string.
    // https://dev.mysql.com/doc/dev/mysql-server/latest/page_protocol_connection_phase_packets_protocol_handshake_response.html
    // 鉴权插件名称需要用指定的字符编码解析
    private static final String CLIENT_PLUGIN_AUTH_CHARSET = "utf-8";


    @Getter
    private final int clientFlags;
    @Getter
    private final long maxPacketSize;
    @Getter
    private final Charset charset;
    @Getter
    private final String username;
    @Getter
    private final byte[] authResponse;
    @Getter
    private final String database;
    @Getter
    private final String pluginName;

    @Getter
    private final int charsetCode;




    public MySQLProtocolHandshakeResponse41Packet(MySQLProtocolPacket packet) {
        super(packet.getSequenceId(), packet.getPayload());
        // 客户端兼容性标志
        this.clientFlags = (int) payload.readInteger(MySQLProtocolConstants.IntegerDataType.INT4);

        // 检查最低兼容标准
        if ((MIN_CLIENT_FLAGS & clientFlags) != MIN_CLIENT_FLAGS) {
            throw new MinimumCapabilityException(MIN_CLIENT_FLAGS, this.clientFlags);
        }

        // 最大支持的数据包大小
        this.maxPacketSize = payload.readInteger(MySQLProtocolConstants.IntegerDataType.INT4);
        // 字符集
        this.charsetCode = (int) payload.readInteger(MySQLProtocolConstants.IntegerDataType.INT1);


        if (!ONLY_SUPPORTED_CHARSET.containsKey(this.charsetCode)) {
            throw new CharsetNotSupportedException(this.charsetCode);
        }

        this.charset = ONLY_SUPPORTED_CHARSET.get(this.charsetCode);
        String charsetName = this.charset.name();

        // 跳过23个字节的填充
        payload.readBytes(MySQLProtocolConstants.StringLengthDataType.STRING_FIXED, 23);

        // 读取用户名
        this.username = payload.readString(MySQLProtocolConstants.StringSelfDataType.STRING_TERM, charsetName);

        // CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA
        this.authResponse = payload.readBytes(MySQLProtocolConstants.StringSelfDataType.STRING_LENENC);

        if ((clientFlags & CLIENT_CONNECT_WITH_DB) != 0) {
            this.database = payload.readString(MySQLProtocolConstants.StringSelfDataType.STRING_TERM, charsetName);
        } else {
            this.database = null;
        }

        this.pluginName = payload.readString(MySQLProtocolConstants.StringSelfDataType.STRING_TERM, CLIENT_PLUGIN_AUTH_CHARSET);
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
