package org.cocofhu.mspf.protocol.datatype;

import org.cocofhu.mspf.exception.ParseDataTypeException;
import org.cocofhu.mspf.exception.DataTypeNotSupportedException;
import org.cocofhu.mspf.util.Pair;


/**
 * MySQL定长整数类型，由于MySQL协议文档中只有1,2,3,4,6,8Bytes的整数，所以这里直接使用Long来存储
 */
public class FixedLengthInteger extends BasicDataType<Long>{


    public FixedLengthInteger(byte[] bytes, int offset, int size) {
        super(bytes, offset, size);
    }

    public FixedLengthInteger(Long val, int size) {
        super(val, size);
    }

    @Override
    public Pair<Long,Integer> buildFromBytes(byte[] bytes, int offset, int size) {
        if(size > 8 || size <= 0){
            throw new DataTypeNotSupportedException(String.format("the size of LengthEncodedInteger FixedLengthInteger in [1,8] but %d.", size) );
        }
        if(bytes == null){
            throw new ParseDataTypeException(String.format("parsing int<%d> error, empty bytes array.", size), null, offset, size);
        }
        if(bytes.length < size + offset){
            throw new ParseDataTypeException(String.format("parsing int<%d> error, bytes array length error, offset : %d, size : %d, bytes array length : %d.",
                    size, offset, size, bytes.length), bytes, offset, size);
        }
        long num = 0;
        for(int i = 0 ; i < size ; ++i){
            num += (((long) bytes[i + offset]) & 255) << (i << 3);
        }
        return new Pair<>(num, size);
    }


}
