package com.cocofhu.mspf.protocol;


import java.util.function.Consumer;

public interface Message {

    /**
     *  获取消息载体的字节数组，实际大小与getActuallySize相等
     *  改方法返回的数组不保证修改与底层数组同步，默认实现为原数
     *  组的部分拷贝。
     */
    default byte[] getBytes(){
        int size = getPayloadLength();
        if(size < 0) {
            return null;
        }
        byte[] bytes = new byte[size];
        underlyingBytes(underlyingBytes->System.arraycopy(underlyingBytes,0,bytes,0, size), false);
        return bytes;
    }

    int getPayloadLength();

    void underlyingBytes(Consumer<byte[]> consumer, boolean changed);

    default void underlyingBytes(Consumer<byte[]> consumer){
        underlyingBytes(consumer, true);
    }


}
