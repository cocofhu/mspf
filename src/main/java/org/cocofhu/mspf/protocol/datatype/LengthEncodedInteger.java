package org.cocofhu.mspf.protocol.datatype;

import org.cocofhu.mspf.exception.DataTypeNotSupportedException;
import org.cocofhu.mspf.exception.ParseDataTypeException;
import org.cocofhu.mspf.util.Pair;

public class LengthEncodedInteger extends BasicDataType<Long>{

    public LengthEncodedInteger(byte[] bytes, int offset, int size) {
        super(bytes, offset, size);
    }

    @Override
    public Pair<Long, Integer> buildFromBytes(byte[] bytes, int offset, int size) {
        if(size != -1){
            throw new DataTypeNotSupportedException(String.format("the size of LengthEncodedInteger must be -1 but %d.", size));
        }
        if(bytes == null){
            throw new ParseDataTypeException(String.format("parsing int<%d> error, empty bytes array.", size), null, offset, size);
        }
        if(offset >= bytes.length){
            throw new ParseDataTypeException(String.format("reading first byte of int<%d> error, insufficient byte array.", size), bytes, offset, size);
        }
        long num = ((long)bytes[offset]) & 255;
        size = Math.max((int)num - 0xf9, 1);
        if(size != 1){
            if(bytes.length < size + offset){
                throw new ParseDataTypeException(String.format("parsing int<-1> error, bytes array length error, offset : %d, size : %d, bytes array length : %d.",
                         offset, size, bytes.length), bytes, offset, size);
            }
            num = 0;
            for(int i = 1 ; i < size; ++i){
                num += (((long) bytes[i + offset]) & 255) << (i << 3);
            }
        }
        return new Pair<>(num, size);
    }


}
