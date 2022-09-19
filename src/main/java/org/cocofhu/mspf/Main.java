package org.cocofhu.mspf;

import org.cocofhu.mspf.protocol.HandshakeV10;
import org.cocofhu.mspf.protocol.Packet;
import org.cocofhu.mspf.protocol.origin.NativeProtocolConstants;
import org.cocofhu.mspf.protocol.origin.NativeProtocolPacketPayload;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        try(
                Socket socket = new Socket("192.168.31.96",3399);
                InputStream in = socket.getInputStream();
                ) {
            NativeProtocolPacketPayload packet = Packet.createPacket(in);
            System.out.println(packet.readInteger(NativeProtocolConstants.IntegerDataType.INT1));
            System.out.println(packet.readInteger(NativeProtocolConstants.IntegerDataType.INT3));
            System.out.println(packet.readString(NativeProtocolConstants.StringSelfDataType.STRING_TERM, "utf-8"));
            System.out.println(Arrays.toString(packet.readBytes(NativeProtocolConstants.StringLengthDataType.STRING_FIXED, 8)));
            packet.skipBytes(NativeProtocolConstants.StringSelfDataType.STRING_TERM);
            System.out.println(packet.readInteger(NativeProtocolConstants.IntegerDataType.INT2));
            System.out.println(packet.readInteger(NativeProtocolConstants.IntegerDataType.INT1));
            System.out.println(packet.readInteger(NativeProtocolConstants.IntegerDataType.INT2));
            System.out.println(packet.readInteger(NativeProtocolConstants.IntegerDataType.INT2));
            System.out.println(packet.readInteger(NativeProtocolConstants.IntegerDataType.INT1));
            packet.readInteger(NativeProtocolConstants.IntegerDataType.INT8);
            packet.readInteger(NativeProtocolConstants.IntegerDataType.INT2);
            System.out.println(packet.readString(NativeProtocolConstants.StringLengthDataType.STRING_VAR,"utf-8",13));
            System.out.println(packet.readString(NativeProtocolConstants.StringSelfDataType.STRING_TERM,"utf-8"));




            System.out.println(11);
//            System.out.println(new HandshakeV10(packet));

//            Handsh
        }





    }
}