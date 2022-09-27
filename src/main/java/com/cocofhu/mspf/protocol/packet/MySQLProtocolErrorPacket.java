package com.cocofhu.mspf.protocol.packet;

import com.cocofhu.mspf.exception.MinimumCapabilityException;
import com.cocofhu.mspf.protocol.MySQLProtocolConstants;
import com.cocofhu.mspf.protocol.MySQLProtocolPacket;
import com.cocofhu.mspf.protocol.MySQLProtocolPacketPayload;
import com.cocofhu.mspf.protocol.MySQLProtocolSession;
import lombok.Getter;

import static com.cocofhu.mspf.protocol.MySQLProtocolConstants.CapabilityFlags.CLIENT_PROTOCOL_41;


/**
 * MySQL Error Packet definition, This packet signals that an error occurred.
 * It contains a SQL state value if CLIENT_PROTOCOL_41 is enabled.
 * Error texts cannot exceed MYSQL_ERR_MSG_SIZE(512)
 * <a href="https://dev.mysql.com/doc/dev/mysql-server/latest/page_protocol_basic_err_packet.html">...</a>
 */
public class MySQLProtocolErrorPacket extends MySQLProtocolPacket {

    private static final int MINIMAL_CLIENT_CAPABILITIES =  CLIENT_PROTOCOL_41;

    /** # marker of the SQL state */
    private static final String DEFAULT_MARKER = "#";
    /** SQL state */
    private static final String DEFAULT_SQL_STATE = "00000";

    /** Max length of error texts */
    public static final int MYSQL_ERR_MSG_SIZE = 512;

    /** error-code */
    @Getter
    private final int errorCode;
    /**  marker of the SQL state */
    @Getter
    private final String marker;
    /** SQL state*/
    @Getter
    private final String sqlState;
    /** error message */
    @Getter
    private final String errorMessage;


    private MySQLProtocolErrorPacket(int sequenceId, int errorCode, String marker, String sqlState, String errorMessage, MySQLProtocolSession session) {
        super(sequenceId, new MySQLProtocolPacketPayload(32));

        this.errorCode = errorCode;
        this.marker = marker;
        this.sqlState = sqlState;
        this.errorMessage = errorMessage;

        // check minimal capabilities
        if((MINIMAL_CLIENT_CAPABILITIES & session.getClientFlags()) != MINIMAL_CLIENT_CAPABILITIES){
            throw new MinimumCapabilityException(MINIMAL_CLIENT_CAPABILITIES, session.getClientFlags());
        }
        // check the length of errorMessage, it's a null terminal string.
        if(errorMessage.length() > MYSQL_ERR_MSG_SIZE - 1){
            throw new IllegalArgumentException(String.format("error texts exceeds MYSQL_ERR_MSG_SIZE(512) : %s. ", errorMessage));
        }
        payload.writeInteger(MySQLProtocolConstants.IntegerDataType.INT1, MySQLProtocolPacketPayload.TYPE_ID_ERROR);
        payload.writeInteger(MySQLProtocolConstants.IntegerDataType.INT2, this.errorCode);
        payload.writeBytes(MySQLProtocolConstants.StringLengthDataType.STRING_FIXED, marker.getBytes(session.getDefaultCharset()), 0, 1);
        payload.writeBytes(MySQLProtocolConstants.StringLengthDataType.STRING_FIXED, sqlState.getBytes(session.getDefaultCharset()), 0, 5);
        payload.writeBytes(MySQLProtocolConstants.StringSelfDataType.STRING_TERM, errorMessage.getBytes(session.getDefaultCharset()));
    }


    /**
     *  create error packet
     */
    public MySQLProtocolErrorPacket(int sequenceId, int errorCode, String errorMessage, MySQLProtocolSession session) {
        this(sequenceId, errorCode, DEFAULT_MARKER, DEFAULT_SQL_STATE, errorMessage, session);
    }



}
