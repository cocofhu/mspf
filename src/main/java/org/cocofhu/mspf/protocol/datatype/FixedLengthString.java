package org.cocofhu.mspf.protocol.datatype;

import org.cocofhu.mspf.exception.ParseDataTypeException;
import org.cocofhu.mspf.util.Pair;

public class FixedLengthString extends BasicDataType<String>{


    public FixedLengthString(byte[] bytes, int offset, int size) {
        super(bytes, offset, size);
    }

    public FixedLengthString(String val, int size) {
        super(val, size);
    }

    @Override
    public Pair<String, Integer> buildFromBytes(byte[] bytes, int offset, int size) {
        if(bytes == null){
            throw new ParseDataTypeException(String.format("parsing string<%d> error, empty bytes array.", size), null, offset, size);
        }
        if(bytes.length < size + offset){
            throw new ParseDataTypeException(String.format("parsing string<%d> error, bytes array length error, offset : %d, size : %d, bytes array length : %d.",
                    size, offset, size, bytes.length), bytes, offset, size);
        }
        return new Pair<>(new String(bytes,offset,size), size);
    }


}
