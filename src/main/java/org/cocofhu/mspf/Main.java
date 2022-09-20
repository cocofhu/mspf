package org.cocofhu.mspf;

import org.cocofhu.mspf.protocol.HandshakeV10;
import org.cocofhu.mspf.protocol.origin.NativeProtocolPacketInputStream;
import org.cocofhu.mspf.protocol.origin.NativeProtocolPacketPayload;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        try(
//                Socket socket = new Socket("192.168.31.96",3399);
//
                ServerSocket socket = new ServerSocket(9988);
                Socket accept = socket.accept()

        ) {



            InputStream in = accept.getInputStream();
            byte[] bytes = new HandshakeV10(10).toBytes();
            accept.getOutputStream().write(bytes.length & 255);
            accept.getOutputStream().write((bytes.length >> 8 )& 255);
            accept.getOutputStream().write((bytes.length >> 16 )& 255);
            accept.getOutputStream().write(0);
            accept.getOutputStream().write(bytes);

//            System.out.println("===");
            byte[] nextBytes = new NativeProtocolPacketInputStream(accept.getInputStream()).readNativeProtocolPacket().toBytes();
            System.out.println(NativeProtocolPacketPayload.dumpAsHex(nextBytes));
//            https://dev.mysql.com/doc/dev/mysql-server/latest/page_protocol_basic_ok_packet.html#sect_protocol_basic_ok_packet_sessinfo

//            NativeProtocolPacketPayload packet = new NativeProtocolPacketPayload();
//            System.out.println("===");
//            System.out.println(packet.readInteger(NativeProtocolConstants.IntegerDataType.INT1));
//
//            System.out.println(packet.readString(NativeProtocolConstants.StringSelfDataType.STRING_TERM, "utf-8"));
//            System.out.println(Arrays.toString(packet.readBytes(NativeProtocolConstants.StringLengthDataType.STRING_FIXED, 8)));
//            packet.skipBytes(NativeProtocolConstants.StringSelfDataType.STRING_TERM);
//            System.out.println(packet.readInteger(NativeProtocolConstants.IntegerDataType.INT2));
//            System.out.println(packet.readInteger(NativeProtocolConstants.IntegerDataType.INT1));
//            System.out.println(packet.readInteger(NativeProtocolConstants.IntegerDataType.INT2));
//            System.out.println(packet.readInteger(NativeProtocolConstants.IntegerDataType.INT2));
//            System.out.println(packet.readInteger(NativeProtocolConstants.IntegerDataType.INT1));
//            packet.readInteger(NativeProtocolConstants.IntegerDataType.INT8);
//            packet.readInteger(NativeProtocolConstants.IntegerDataType.INT2);
//            System.out.println(packet.readString(NativeProtocolConstants.StringLengthDataType.STRING_VAR,"utf-8",13));
//            System.out.println(packet.readString(NativeProtocolConstants.StringSelfDataType.STRING_TERM,"utf-8"));




        }





    }
}