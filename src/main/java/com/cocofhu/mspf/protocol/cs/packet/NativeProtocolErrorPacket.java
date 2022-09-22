package com.cocofhu.mspf.protocol.cs.packet;

import com.cocofhu.mspf.protocol.cs.NativeProtocolConstants;
import com.cocofhu.mspf.protocol.cs.NativeProtocolPacket;
import com.cocofhu.mspf.protocol.cs.NativeProtocolPacketPayload;
import lombok.Getter;

public class NativeProtocolErrorPacket extends NativeProtocolPacket {

    private static final String DEFAULT_MARKER = "#";
    private static final String DEFAULT_SQL_STATE = "00000";

    @Getter
    private final int errorCode;
    @Getter
    private final String marker;
    @Getter
    private final String sqlState;
    @Getter
    private final String errorMessage;


    public NativeProtocolErrorPacket(int sequenceId, int errorCode, String errorMessage) {
        super(sequenceId, new NativeProtocolPacketPayload(32));
        this.errorCode = errorCode;
        this.marker = DEFAULT_MARKER;
        this.sqlState = DEFAULT_SQL_STATE;
        this.errorMessage = errorMessage;
        message.writeInteger(NativeProtocolConstants.IntegerDataType.INT1, NativeProtocolPacketPayload.TYPE_ID_ERROR);
        message.writeInteger(NativeProtocolConstants.IntegerDataType.INT2, this.errorCode);
        // CLIENT_PROTOCOL_41 标志位必须存在
        message.writeBytes(NativeProtocolConstants.StringLengthDataType.STRING_FIXED, marker.getBytes(), 0, 1);
        message.writeBytes(NativeProtocolConstants.StringLengthDataType.STRING_FIXED, sqlState.getBytes(), 0, 5);
        message.writeBytes(NativeProtocolConstants.StringSelfDataType.STRING_TERM, errorMessage.getBytes());
    }



}
