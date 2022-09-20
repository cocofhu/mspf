package org.cocofhu.mspf.protocol;



/**
 * MySQL 网络包
 * @param <H>   消息头
 * @param <M>   消息数据
 */
public abstract class Packet<H extends MessageHeader,M extends Message> {
    protected final H header;
    protected final M message;

    public Packet(H header, M message) {
        this.header = header;
        this.message = message;
    }

    /**
     * 合并消息头和消息数据，返回字节数据
     * @return byte array
     */
    public byte[] toBytes(){
        byte[] headerBytes = header.getHeaderBytes();
        byte[] messageBytes = message.getByteBuffer();
        byte[] newBytes = new byte[headerBytes.length + messageBytes.length];
        System.arraycopy(headerBytes, 0, newBytes, 0, headerBytes.length);
        System.arraycopy(messageBytes, 0, newBytes, headerBytes.length, messageBytes.length);
        return newBytes;
    }
}
