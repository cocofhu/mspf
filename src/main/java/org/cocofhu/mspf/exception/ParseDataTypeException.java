package org.cocofhu.mspf.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParseDataTypeException extends ProtocolException{
    @Getter
    private final byte[] payload;
    @Getter
    private final int offset;
    @Getter
    private final int size;


    public ParseDataTypeException(String msg, byte[] payload, int offset, int size) {
        super(msg);
        this.payload = payload;
        this.offset = offset;
        this.size = size;
    }
}
