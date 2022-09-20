package org.cocofhu.mspf.util;

public class DebugUtils {
    public static String dumpAsHex(byte[] byteBuffer) {
        int length = byteBuffer.length;
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
}
