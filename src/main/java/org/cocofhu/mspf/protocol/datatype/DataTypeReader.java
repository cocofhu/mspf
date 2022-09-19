package org.cocofhu.mspf.protocol.datatype;

import org.cocofhu.mspf.protocol.Packet;

public class DataTypeReader {
    private final Packet packet;
    protected int offset;

    public DataTypeReader(Packet packet) {
        this.packet = packet;
        this.offset = 0;
    }

    public FixedLengthInteger nextFixedLengthInteger(int size){
        FixedLengthInteger data = new FixedLengthInteger(packet.getPayload(), offset, size);
        offset += data.getSize();
        return data;
    }

    public FixedLengthString nextFixedLengthString(int size){
        FixedLengthString data = new FixedLengthString(packet.getPayload(), offset, size);
        offset += data.getSize();
        return data;
    }

    public LengthEncodedInteger nextLengthEncodedInteger(){
        LengthEncodedInteger data = new LengthEncodedInteger(packet.getPayload(), offset, -1);
        offset += data.getSize();
        return data;
    }

    public NulTerminatedString nextNulTerminatedString(){
        NulTerminatedString data = new NulTerminatedString(packet.getPayload(), offset, -1);
        offset += data.getSize();
        return data;
    }

    public void skip(int size){
        this.offset += size;
    }
    public void reset(){
        this.offset = 0;
    }
}
