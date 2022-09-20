package com.cocofhu.mspf.util;

import com.cocofhu.mspf.protocol.origin.NativeProtocolConstants;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class DebugUtils {

    public static String dumpAsHex(byte[] byteBuffer,int length) {
        StringBuilder fullOutBuilder = new StringBuilder(length * 4);
        StringBuilder asciiOutBuilder = new StringBuilder(16);

        for (int p = 0, l = 0; p < length; l = 0) { // p: position in buffer (1..length); l: position in line (1..8)
            for (; l < 8 && p < length; p++, l++) {
                int asInt = byteBuffer[p] & 0xff;
                if (asInt < 0x10) {
                    fullOutBuilder.append("0");
                }
                fullOutBuilder.append(Integer.toHexString(asInt)).append(" ");
                asciiOutBuilder.append(" ").append(asInt >= 0x20 && asInt < 0x7f ? (char) asInt : ".");
            }
            for (; l < 8; l++) { // if needed, fill remaining of last line with spaces
                fullOutBuilder.append("   ");
            }
            fullOutBuilder.append("   ").append(asciiOutBuilder).append(System.lineSeparator());
            asciiOutBuilder.setLength(0);
        }
        return fullOutBuilder.toString();
    }
    public static String dumpAsHex(byte[] byteBuffer) {
        return dumpAsHex(byteBuffer,byteBuffer.length);
    }

    public static List<String> listAllCapabilityFlags(int capabilityFlags){
        List<String> flags = new ArrayList<>();
        Field[] allFieldOfFlags = NativeProtocolConstants.CapabilityFlags.class.getDeclaredFields();
        for (Field fieldOfFlag : allFieldOfFlags) {
            try {
                int flag = (int) fieldOfFlag.get(null);
                if ((flag & capabilityFlags) != 0) {
                    flags.add(fieldOfFlag.getName());
                }
            } catch (Exception e) {
                // 忽略错误的情况
                log.debug(e.getMessage());
            }
        }
        return flags;
    }

}
