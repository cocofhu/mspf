package com.cocofhu.mspf.protocol.origin;



import com.cocofhu.mspf.protocol.Message;

import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;

import static com.cocofhu.mspf.protocol.origin.NativeProtocolConstants.IntegerDataType.*;
import static com.cocofhu.mspf.protocol.origin.NativeProtocolConstants.StringLengthDataType.*;



/**
 * MySQL TCP 协议网络包数据部分实现类
 */
public class NativeProtocolPacketPayload implements Message {

    @FunctionalInterface
    public interface ByteArrayChangedListener{
        void changed();
    }

    // 如果可变长度整数的第一个字节是251(0xfb),这将是一个空的ProtocolText::ResultsetRow.
    public static final long NULL_LENGTH = -1;

    /* 通过数据的第一个字节可以判断数据包的基本类型. */
    public static final short TYPE_ID_ERROR = 0xFF;
    public static final short TYPE_ID_EOF = 0xFE;
    /** 和EOF一样，不过只出现在鉴权阶段 **/
    public static final short TYPE_ID_AUTH_SWITCH = 0xFE;
    public static final short TYPE_ID_OK = 0;
    public static final short TYPE_ID_AUTH_MORE_DATA = 0x01;
    public static final short TYPE_ID_AUTH_NEXT_FACTOR = 0x02;

    /** 当内部byteBuffer不够时的扩容因子 */
    public static final float DEFAULT_RESIZE_FACTOR = 1.25f;

    /** 数据实际大小 */
    private int payloadLength;
    private byte[] byteBuffer;
    private int position = 0;

    private ByteArrayChangedListener byteArrayChangedListener;

    protected void setByteArrayChangedListener(ByteArrayChangedListener byteArrayChangedListener) {
        this.byteArrayChangedListener = byteArrayChangedListener;
    }

    public NativeProtocolPacketPayload(byte[] buf) {
        this.byteBuffer = buf;
        this.payloadLength = buf.length;
    }

    public NativeProtocolPacketPayload(int size) {
        this(size, false);
    }

    public NativeProtocolPacketPayload(int size, boolean emptyData) {
        this.byteBuffer = new byte[size];
        if(emptyData){
            this.payloadLength = size;
        }else{
            this.payloadLength = 0;
        }

    }

    /**
     * 确保内部字节数组中存在指定的存储空间，如果不够则扩容
     * @param additionalData    指定存储空间大小
     */
    public final void ensureCapacity(int additionalData) {
        if ((this.position + additionalData) > this.byteBuffer.length) {
            int newLength = (int) (this.byteBuffer.length * DEFAULT_RESIZE_FACTOR);
            if (newLength < (this.byteBuffer.length + additionalData)) {
                // 扩容一次大小还不够则安装additionalData扩容
                newLength = this.byteBuffer.length + (int) (additionalData * DEFAULT_RESIZE_FACTOR);
            }
            if (newLength < this.byteBuffer.length) {
                newLength = this.byteBuffer.length + additionalData;
            }
            byte[] newBytes = new byte[newLength];
            System.arraycopy(this.byteBuffer, 0, newBytes, 0, this.byteBuffer.length);
            this.byteBuffer = newBytes;
        }
    }

    // Package detect methods
    public boolean isErrorPacket() {
        return (this.byteBuffer[0] & 0xff) == TYPE_ID_ERROR;
    }
    public final boolean isEOFPacket() {
        return (this.byteBuffer[0] & 0xff) == TYPE_ID_EOF && (this.payloadLength <= 5);
    }
    public final boolean isAuthMethodSwitchRequestPacket() {
        return (this.byteBuffer[0] & 0xff) == TYPE_ID_AUTH_SWITCH;
    }
    public final boolean isOKPacket() {
        return (this.byteBuffer[0] & 0xff) == TYPE_ID_OK;
    }
    public final boolean isResultSetOKPacket() {
        return (this.byteBuffer[0] & 0xff) == TYPE_ID_EOF && (this.payloadLength > 5) && (this.payloadLength < 16777215);
    }
    public final boolean isAuthMoreDataPacket() {
        return (this.byteBuffer[0] & 0xff) == TYPE_ID_AUTH_MORE_DATA;
    }
    public final boolean isAuthNextFactorPacket() {
        return (this.byteBuffer[0] & 0xff) == TYPE_ID_AUTH_NEXT_FACTOR;
    }

    // Read-Write methods
    public void writeInteger(NativeProtocolConstants.IntegerDataType type, long l) {
        byte[] b;
        int size = getSize(type);
        if(size != -1){
            ensureCapacity(size);
            b = this.byteBuffer;
            for(int i = 0 ; i < size ;++i){
                b[this.position+ i ] = (byte) ((l >>> (i * 8)) & 0xff);
            }
            this.position += size;
        }else{
            if (l < 251) {
                ensureCapacity(1);
                writeInteger(INT1, l);

            } else if (l < 65536L) {
                ensureCapacity(3);
                writeInteger(INT1, 252);
                writeInteger(INT2, l);

            } else if (l < 16777216L) {
                ensureCapacity(4);
                writeInteger(INT1, 253);
                writeInteger(INT3, l);

            } else {
                ensureCapacity(9);
                writeInteger(INT1, 254);
                writeInteger(INT8, l);
            }
        }

        adjustPayloadLength();
    }
    public final long readInteger(NativeProtocolConstants.IntegerDataType type) {
        byte[] b = this.byteBuffer;
        int size = getSize(type);
        if(size != -1){
            long num = 0;
            for(int i = 0 ; i < size ; ++i){
                num += (((long) b[i + this.position]) & 255) << (i << 3);
            }
            this.position += size;
            return num;
        }

        int sw = b[this.position++] & 0xff;
        switch (sw) {
            case 251: return NULL_LENGTH; // represents a NULL in a ProtocolText::ResultsetRow
            case 252: return readInteger(INT2);
            case 253: return readInteger(INT3);
            case 254: return readInteger(INT8);
            default: return sw;
        }


    }
    public final void writeBytes(NativeProtocolConstants.StringSelfDataType type, byte[] b) {
        writeBytes(type, b, 0, b.length);
    }
    public final void writeBytes(NativeProtocolConstants.StringLengthDataType type, byte[] b) {
        writeBytes(type, b, 0, b.length);
    }
    public void writeBytes(NativeProtocolConstants.StringSelfDataType type, byte[] b, int offset, int len) {
        switch (type) {
            case STRING_EOF:
                writeBytes(STRING_FIXED, b, offset, len);
                break;

            case STRING_TERM:
                ensureCapacity(len + 1);
                writeBytes(STRING_FIXED, b, offset, len);
                this.byteBuffer[this.position++] = 0;
                break;

            case STRING_LENENC:
                ensureCapacity(len + 9);
                writeInteger(INT_LENENC, len);
                writeBytes(STRING_FIXED, b, offset, len);
                break;
        }

        adjustPayloadLength();
    }
    public void writeBytes(NativeProtocolConstants.StringLengthDataType type, byte[] b, int offset, int len) {
        switch (type) {
            case STRING_FIXED:
            case STRING_VAR:
                ensureCapacity(len);
                System.arraycopy(b, offset, this.byteBuffer, this.position, len);
                this.position += len;
                break;
        }

        adjustPayloadLength();
    }
    public byte[] readBytes(NativeProtocolConstants.StringSelfDataType type) {
        byte[] b;
        switch (type) {
            case STRING_TERM:
                int i = this.position;
                while ((i < this.payloadLength) && (this.byteBuffer[i] != 0)) {
                    i++;
                }
                b = readBytes(STRING_FIXED, i - this.position);
                this.position++; // skip terminating byte
                return b;

            case STRING_LENENC:
                long l = readInteger(INT_LENENC);
                return l == NULL_LENGTH ? null : (l == 0 ? new byte[0] : readBytes(STRING_FIXED, (int) l));

            case STRING_EOF:
                return readBytes(STRING_FIXED, this.payloadLength - this.position);
        }
        return null;
    }
    public byte[] readBytes(NativeProtocolConstants.StringLengthDataType type, int len) {
        byte[] b;
        switch (type) {
            case STRING_FIXED:
            case STRING_VAR:
                b = new byte[len];
                System.arraycopy(this.byteBuffer, this.position, b, 0, len);
                this.position += len;
                return b;
        }
        return null;
    }
    public String readString(NativeProtocolConstants.StringSelfDataType type, String encoding) {
        String res = null;
        switch (type) {
            case STRING_TERM:
                int i = this.position;
                while ((i < this.payloadLength) && (this.byteBuffer[i] != 0)) {
                    i++;
                }
                res = readString(STRING_FIXED, encoding, i - this.position);
                this.position++; // skip terminating byte
                break;

            case STRING_LENENC:
                long l = readInteger(INT_LENENC);
                return l == NULL_LENGTH ? null : (l == 0 ? "" : readString(STRING_FIXED, encoding, (int) l));

            case STRING_EOF:
                return readString(STRING_FIXED, encoding, this.payloadLength - this.position);

        }
        return res;
    }
    public String readString(NativeProtocolConstants.StringLengthDataType type, String encoding, int len) {
        String res = null;
        switch (type) {
            case STRING_FIXED:
            case STRING_VAR:
                if ((this.position + len) > this.payloadLength) {
//                    throw ExceptionFactory.createException(WrongArgumentException.class, Messages.getString("Buffer.1"));
                }
                try {
                    res = new String(this.byteBuffer, this.position, len, encoding);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                this.position += len;
                break;

        }
        return res;
    }

    /** 跳过一个指定的数据类型 */
    public void skipBytes(NativeProtocolConstants.StringSelfDataType type) {
        switch (type) {
            case STRING_TERM:
                while ((this.position < this.payloadLength) && (this.byteBuffer[this.position] != 0)) {
                    this.position++;
                }
                this.position++; // skip terminating byte
                break;

            case STRING_LENENC:
                long len = readInteger(INT_LENENC);
                if (len != NULL_LENGTH && len != 0) {
                    this.position += (int) len;
                }
                break;

            case STRING_EOF:
                this.position = this.payloadLength;
                break;
        }
    }


    // Override methods

    @Override
    public void underlyingBytes(Consumer<byte[]> consumer, boolean changed) {
        consumer.accept(this.byteBuffer);
        if(byteArrayChangedListener != null && changed){
            byteArrayChangedListener.changed();
        }
    }

    /** 这里的position就相当于实际的bufferSize */
    @Override
    public int getPayloadLength() {
        return this.payloadLength;
    }




    // Private Method
    private static int getSize(NativeProtocolConstants.IntegerDataType type) {
        int size = -1;
        switch (type) {
            case INT1: size = 1; break;
            case INT2: size = 2; break;
            case INT3: size = 3; break;
            case INT4: size = 4; break;
            case INT6: size = 6; break;
            case INT8: size = 8; break;
        }
        return size;
    }

    /**
     * 当写入底层数组时，需要重新调整length
     */
    protected void adjustPayloadLength() {
        if (this.position > this.payloadLength) {
            this.payloadLength = this.position;
        }
        // 当底层发生变更，需要通知上层字段做相应的变更
        if(byteArrayChangedListener != null){
            byteArrayChangedListener.changed();
        }
    }



    // JUST FOR DEBUGGING


}

