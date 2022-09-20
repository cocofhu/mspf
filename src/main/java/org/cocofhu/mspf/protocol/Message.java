package org.cocofhu.mspf.protocol;


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
        System.arraycopy(getUnderlyingBytes(),0,bytes,0, size);
        return bytes;
    }

    int getPayloadLength();

    byte[] getUnderlyingBytes();

}
