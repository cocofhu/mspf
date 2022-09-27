package com.cocofhu.mspf.protocol.packet;

import com.cocofhu.mspf.exception.MinimumCapabilityException;
import com.cocofhu.mspf.protocol.MySQLProtocolConstants;
import com.cocofhu.mspf.protocol.MySQLProtocolPacket;
import com.cocofhu.mspf.protocol.MySQLProtocolPacketPayload;
import com.cocofhu.mspf.protocol.MySQLProtocolSession;
import lombok.Getter;

import static com.cocofhu.mspf.protocol.MySQLProtocolConstants.CapabilityFlags.CLIENT_PROTOCOL_41;

public class MySQLProtocolOKPacket extends MySQLProtocolPacket {

    private static final int MINIMAL_CLIENT_CAPABILITIES =  CLIENT_PROTOCOL_41;

    @Getter
    private final int affectedRows;
    @Getter
    private final int lastInsertId;
    @Getter
    private final int statusFlag;
    @Getter
    private final int warnings;

    public MySQLProtocolOKPacket(int sequenceId, int affectedRows, int lastInsertId, int statusFlag, int warnings, MySQLProtocolSession session) {
        super(sequenceId);

        if((MINIMAL_CLIENT_CAPABILITIES & session.getClientFlags()) != MINIMAL_CLIENT_CAPABILITIES){
            throw new MinimumCapabilityException(MINIMAL_CLIENT_CAPABILITIES, session.getClientFlags());
        }

        this.affectedRows = affectedRows;
        this.lastInsertId = lastInsertId;
        this.statusFlag = statusFlag;
        this.warnings = warnings;

        MySQLProtocolPacketPayload message = this.getPayload();
        message.writeInteger(MySQLProtocolConstants.IntegerDataType.INT1, MySQLProtocolConstants.TYPE_ID_OK);
        message.writeInteger(MySQLProtocolConstants.IntegerDataType.INT_LENENC, affectedRows);
        message.writeInteger(MySQLProtocolConstants.IntegerDataType.INT_LENENC, lastInsertId);
        message.writeInteger(MySQLProtocolConstants.IntegerDataType.INT2, statusFlag);
        message.writeInteger(MySQLProtocolConstants.IntegerDataType.INT2, warnings);
    }



}
