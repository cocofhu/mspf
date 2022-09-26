package com.cocofhu.mspf.protocol.packet;

import com.cocofhu.mspf.protocol.MySQLProtocolConstants;
import com.cocofhu.mspf.protocol.MySQLProtocolPacket;
import com.cocofhu.mspf.protocol.MySQLProtocolPacketPayload;

public class NativeProtocolOKPacket extends MySQLProtocolPacket {



    private final int affectedRows;
    private final int lastInsertId;

    // 协议必须支持 Protocol41
    private final int statusFlag;
    private final int warnings;

    // CLIENT_SESSION_TRACK
//    private boolean sessionTrack;
//    private


    public NativeProtocolOKPacket(int sequenceId) {
        super(sequenceId,new MySQLProtocolPacketPayload(16));
        this.affectedRows = 0;
        this.lastInsertId = 0;
        this.statusFlag = 0 ;
        this.warnings = 0;
        MySQLProtocolPacketPayload message = this.getPayload();
        message.writeInteger(MySQLProtocolConstants.IntegerDataType.INT1, MySQLProtocolConstants.TYPE_ID_OK);
        message.writeInteger(MySQLProtocolConstants.IntegerDataType.INT_LENENC, affectedRows);
        message.writeInteger(MySQLProtocolConstants.IntegerDataType.INT_LENENC, lastInsertId);
        message.writeInteger(MySQLProtocolConstants.IntegerDataType.INT2, statusFlag);
        message.writeInteger(MySQLProtocolConstants.IntegerDataType.INT2, warnings);
    }

}
