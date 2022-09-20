package com.cocofhu.mspf.protocol;

public interface MessageHeader {

    byte[] getHeaderBytes();

    int getMessageSize();

    byte getMessageSequence();
}
