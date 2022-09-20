package com.cocofhu.mspf.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NetworkException extends RuntimeException{
    @Getter
    private final int actualLength;
    @Getter
    private final int requiredLength;

    public NetworkException(int actualLength, int requiredLength) {
        super(String.format("Network Exception:: unexpected eof, require byte:%d, actually read:%d.", requiredLength, actualLength));
        this.actualLength = actualLength;
        this.requiredLength = requiredLength;
    }
}