package com.cocofhu.mspf.protocol;


/**
 * MySQL 网络包
 * @param <H>   消息头
 * @param <M>   消息数据
 */
public interface Packet<H extends MessageHeader,M extends Message> {


    public H getHeader();

    public M getMessage();

    /**
     * 合并消息头和消息数据，返回字节数据
     * @return byte array
     */
    default byte[] toBytes(){
        H header = getHeader();
        M message = getMessage();
        byte[] headerBytes = header.getHeaderBytes();
        byte[] messageBytes = message.underlyingBytes();
        byte[] newBytes = new byte[headerBytes.length + message.getPayloadLength()];
        System.arraycopy(headerBytes, 0, newBytes, 0, headerBytes.length);
        // 获取底层数据，减少一次拷贝
        System.arraycopy(messageBytes, 0, newBytes, headerBytes.length, message.getPayloadLength());
        return newBytes;
    }
}
