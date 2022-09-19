package org.cocofhu.mspf.protocol;

// https://dev.mysql.com/doc/internals/en/mysql-packet.html



import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.cocofhu.mspf.exception.NetworkException;
import org.cocofhu.mspf.protocol.datatype.FixedLengthInteger;
import org.cocofhu.mspf.protocol.datatype.FixedLengthString;
import org.cocofhu.mspf.protocol.datatype.LengthEncodedInteger;
import org.cocofhu.mspf.protocol.datatype.NulTerminatedString;
import org.cocofhu.mspf.protocol.origin.NativeProtocolPacketPayload;
import org.cocofhu.mspf.util.PacketUtils;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class Packet {

    @Getter
    private final int payloadLength;
    @Getter
    private final int sequenceId;
    @Getter
    private final byte[] payload;

    private static byte[] readBytesOrThrow(InputStream in,int len){
        try {
            byte[] buf = new byte[len];
            int actualLength = in.read(buf);
            if(actualLength != len){
                throw new NetworkException(actualLength, len);
            }
            return buf;
        } catch (IOException e) {
            throw new NetworkException(0, len);
        }
    }

    // 同步从输入流中构建一个MySQL包
    public static NativeProtocolPacketPayload createPacket(InputStream in){
        log.debug("trying to create packet from input stream.");
        byte[] header = readBytesOrThrow(in,4);
        int len = Math.toIntExact(new FixedLengthInteger(header, 0, 3).getVal());
        int seqId = Math.toIntExact(new FixedLengthInteger(header, 3, 1).getVal());
        log.debug("packet header::payloadLength = {}" , len);
        log.debug("packet header::sequenceId = {}", seqId );
        byte[] payload = readBytesOrThrow(in,len);
        return new NativeProtocolPacketPayload(payload);
    }


    private Packet(int payloadLength, int sequenceId, byte[] payload){
        this.payloadLength = payloadLength;
        this.sequenceId = sequenceId;
        this.payload = payload;
    }


}
