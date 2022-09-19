package org.cocofhu.mspf.protocol.datatype;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.cocofhu.mspf.exception.DataTypeNotSupportedException;
import org.cocofhu.mspf.util.Pair;

/**
 * MySQL 协议基本数据类型，由数据的值和数据的大小(byte)构成
 * @param <T>   数据的值类型
 */
public abstract class BasicDataType<T> {
    @Getter
    private final T val;
    @Getter
    private final int size;

    public BasicDataType(byte[] bytes,int offset, int size) {
        Pair<T, Integer> pair = buildFromBytes(bytes, offset, size);
        this.val = pair.getFirst();
        this.size = pair.getSecond();
    }

    public BasicDataType(T val, int size) {
        this.val = val;
        this.size = size;
    }


    public abstract Pair<T,Integer> buildFromBytes(byte[] bytes, int offset, int size);

    @Override
    public String toString() {
        return "BasicDataType{" +
                "val=" + val +
                ", size=" + size +
                '}';
    }
}
