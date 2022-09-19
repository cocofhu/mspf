package org.cocofhu.mspf.exception;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtocolException extends RuntimeException{
    public ProtocolException(String msg){
        super(msg);
        log.error("Protocol Exception:: " + msg);
    }
}
