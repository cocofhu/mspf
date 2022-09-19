package org.cocofhu.mspf.protocol;

public @interface ProtocolData {
    int size();

    int order();

    Class<?> parser();
}
