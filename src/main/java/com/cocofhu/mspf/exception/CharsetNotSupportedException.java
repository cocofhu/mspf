package com.cocofhu.mspf.exception;


import com.cocofhu.mspf.util.DebugUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


/**
 * MySQL 协议不支持指定的字符集
 */
@Slf4j
public class CharsetNotSupportedException extends ProtocolException{
    @Getter
    private final int charset;
    // see NativeProtocolConstants.Charset
    public CharsetNotSupportedException(int charset){
        super(String.format("charset is not supported, code of charset : %d." ,charset));
        this.charset = charset;
    }
}
