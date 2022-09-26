package com.cocofhu.mspf.protocol.packet;

import com.cocofhu.mspf.protocol.MySQLProtocolConstants;
import com.cocofhu.mspf.protocol.MySQLProtocolPacket;
import com.cocofhu.mspf.protocol.MySQLProtocolPacketPayload;
import lombok.Getter;


/**
 * MySQL error packet
 *
 */
public class MySQLProtocolErrorPacket extends MySQLProtocolPacket {

    private static final String DEFAULT_MARKER = "#";
    private static final String DEFAULT_SQL_STATE = "00000";

    public static final int MYSQL_ERR_MSG_SIZE = 512;

    @Getter
    private final int errorCode;
    @Getter
    private final String marker;
    @Getter
    private final String sqlState;
    @Getter
    private final String errorMessage;

//    private final Nati


    private MySQLProtocolErrorPacket(int sequenceId, int errorCode, String marker, String sqlState, String errorMessage) {
        super(sequenceId, new MySQLProtocolPacketPayload(32));
        this.errorCode = errorCode;
        this.marker = marker;
        this.sqlState = sqlState;
        this.errorMessage = errorMessage;
        payload.writeInteger(MySQLProtocolConstants.IntegerDataType.INT1, MySQLProtocolPacketPayload.TYPE_ID_ERROR);
        payload.writeInteger(MySQLProtocolConstants.IntegerDataType.INT2, this.errorCode);
        // CLIENT_PROTOCOL_41 标志位必须存在
        payload.writeBytes(MySQLProtocolConstants.StringLengthDataType.STRING_FIXED, marker.getBytes(), 0, 1);
        payload.writeBytes(MySQLProtocolConstants.StringLengthDataType.STRING_FIXED, sqlState.getBytes(), 0, 5);
        payload.writeBytes(MySQLProtocolConstants.StringSelfDataType.STRING_TERM, errorMessage.getBytes());
    }


    /**
     * 构建一个ErrorPacket
     * @param sequenceId    sequenceId
     * @param errorCode     错误码
     * @param errorMessage  错误信息
     */
    public MySQLProtocolErrorPacket(int sequenceId, int errorCode, String errorMessage) {
        this(sequenceId, errorCode, DEFAULT_MARKER, DEFAULT_SQL_STATE, errorMessage);
    }



}
