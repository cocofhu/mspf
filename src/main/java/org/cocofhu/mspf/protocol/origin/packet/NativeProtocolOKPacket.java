package org.cocofhu.mspf.protocol.origin.packet;

import org.cocofhu.mspf.protocol.origin.NativeProtocolConstants;
import org.cocofhu.mspf.protocol.origin.NativeProtocolPacket;
import org.cocofhu.mspf.protocol.origin.NativeProtocolPacketPayload;

import static org.cocofhu.mspf.protocol.origin.NativeProtocolConstants.TYPE_ID_OK;

public class NativeProtocolOKPacket extends NativeProtocolPacket {



    private final int affectedRows;
    private final int lastInsertId;

    // 协议必须支持 Protocol41
    private final int statusFlag;
    private final int warnings;

    // CLIENT_SESSION_TRACK
//    private boolean sessionTrack;
//    private


    public NativeProtocolOKPacket(int sequenceId) {
        super(sequenceId,new NativeProtocolPacketPayload(16));
        this.affectedRows = 0;
        this.lastInsertId = 0;
        this.statusFlag = 0 ;
        this.warnings = 0;
        NativeProtocolPacketPayload message = this.getMessage();
        message.writeInteger(NativeProtocolConstants.IntegerDataType.INT1, TYPE_ID_OK);
        message.writeInteger(NativeProtocolConstants.IntegerDataType.INT_LENENC, affectedRows);
        message.writeInteger(NativeProtocolConstants.IntegerDataType.INT_LENENC, lastInsertId);
        message.writeInteger(NativeProtocolConstants.IntegerDataType.INT2, statusFlag);
        message.writeInteger(NativeProtocolConstants.IntegerDataType.INT2, warnings);
    }

}
