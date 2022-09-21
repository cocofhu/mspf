package com.cocofhu.mspf.exception;


import com.cocofhu.mspf.util.DebugUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


/**
 * MySQL 协议最低兼容标志未得到满足
 */
@Slf4j
public class MinimumCapabilityException extends ProtocolException{
    @Getter
    private final int minimumCapability;
    @Getter
    private final int actualCapability;
    public MinimumCapabilityException(int minimumCapability, int actualCapability){
        super(String.format("minimum flag of capability are not satisfied, %s are required.",
                DebugUtils.listAllCapabilityFlags((actualCapability & minimumCapability) ^ minimumCapability)));
        this.minimumCapability = minimumCapability;
        this.actualCapability = actualCapability;
    }
}
