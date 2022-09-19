package org.cocofhu.mspf.protocol.datatype;

import org.cocofhu.mspf.exception.ParseDataTypeException;
import org.cocofhu.mspf.util.Pair;

public class NulTerminatedString extends BasicDataType<String>{


    public NulTerminatedString(byte[] bytes, int offset, int size) {
        super(bytes, offset, size);
    }

    @Override
    public Pair<String,Integer> buildFromBytes(byte[] bytes, int offset, int size) {
        if(bytes == null){
            throw new ParseDataTypeException("parsing string<-1> error, empty bytes array.", null, offset, size);
        }
        int pos = offset;
        while(true){
            if(pos >= bytes.length){
                throw new ParseDataTypeException("parsing string<-1> error, unexpected eof, can not find a terminated byte[0x00].", bytes, offset, size);
            }
            if(bytes[pos] == 0) {
                break;
            }
            ++pos;
        }
        int len = pos - offset + 1;
        return new Pair<>(new String(bytes,offset,len - 1), len);
    }
}
