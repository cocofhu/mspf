package com.cocofhu.mspf.protocol.cs;

import java.util.HashMap;

/**
 * MySQL TCP连接常量定义
 * @author cocofhu
 */
public class NativeProtocolConstants {

    public static final HashMap<Integer,String> UTF8_COLLATION_NAME;
    static {
        UTF8_COLLATION_NAME = new HashMap<>();
        UTF8_COLLATION_NAME.put(33, "utf8mb3_general_ci");
        UTF8_COLLATION_NAME.put(45, "utf8mb4_general_ci");
        UTF8_COLLATION_NAME.put(46, "utf8mb4_bin");
        UTF8_COLLATION_NAME.put(76, "utf8mb3_tolower_ci");
        UTF8_COLLATION_NAME.put(83, "utf8mb3_bin");
        UTF8_COLLATION_NAME.put(192, "utf8mb3_unicode_ci");
        UTF8_COLLATION_NAME.put(193, "utf8mb3_icelandic_ci");
        UTF8_COLLATION_NAME.put(194, "utf8mb3_latvian_ci");
        UTF8_COLLATION_NAME.put(195, "utf8mb3_romanian_ci");
        UTF8_COLLATION_NAME.put(196, "utf8mb3_slovenian_ci");
        UTF8_COLLATION_NAME.put(197, "utf8mb3_polish_ci");
        UTF8_COLLATION_NAME.put(198, "utf8mb3_estonian_ci");
        UTF8_COLLATION_NAME.put(199, "utf8mb3_spanish_ci");
        UTF8_COLLATION_NAME.put(200, "utf8mb3_swedish_ci");
        UTF8_COLLATION_NAME.put(201, "utf8mb3_turkish_ci");
        UTF8_COLLATION_NAME.put(202, "utf8mb3_czech_ci");
        UTF8_COLLATION_NAME.put(203, "utf8mb3_danish_ci");
        UTF8_COLLATION_NAME.put(204, "utf8mb3_lithuanian_ci");
        UTF8_COLLATION_NAME.put(205, "utf8mb3_slovak_ci");
        UTF8_COLLATION_NAME.put(206, "utf8mb3_spanish2_ci");
        UTF8_COLLATION_NAME.put(207, "utf8mb3_roman_ci");
        UTF8_COLLATION_NAME.put(208, "utf8mb3_persian_ci");
        UTF8_COLLATION_NAME.put(209, "utf8mb3_esperanto_ci");
        UTF8_COLLATION_NAME.put(210, "utf8mb3_hungarian_ci");
        UTF8_COLLATION_NAME.put(211, "utf8mb3_sinhala_ci");
        UTF8_COLLATION_NAME.put(212, "utf8mb3_german2_ci");
        UTF8_COLLATION_NAME.put(213, "utf8mb3_croatian_ci");
        UTF8_COLLATION_NAME.put(214, "utf8mb3_unicode_520_ci");
        UTF8_COLLATION_NAME.put(215, "utf8mb3_vietnamese_ci");
        UTF8_COLLATION_NAME.put(223, "utf8mb3_general_mysql500_ci");
        UTF8_COLLATION_NAME.put(224, "utf8mb4_unicode_ci");
        UTF8_COLLATION_NAME.put(225, "utf8mb4_icelandic_ci");
        UTF8_COLLATION_NAME.put(226, "utf8mb4_latvian_ci");
        UTF8_COLLATION_NAME.put(227, "utf8mb4_romanian_ci");
        UTF8_COLLATION_NAME.put(228, "utf8mb4_slovenian_ci");
        UTF8_COLLATION_NAME.put(229, "utf8mb4_polish_ci");
        UTF8_COLLATION_NAME.put(230, "utf8mb4_estonian_ci");
        UTF8_COLLATION_NAME.put(231, "utf8mb4_spanish_ci");
        UTF8_COLLATION_NAME.put(232, "utf8mb4_swedish_ci");
        UTF8_COLLATION_NAME.put(233, "utf8mb4_turkish_ci");
        UTF8_COLLATION_NAME.put(234, "utf8mb4_czech_ci");
        UTF8_COLLATION_NAME.put(235, "utf8mb4_danish_ci");
        UTF8_COLLATION_NAME.put(236, "utf8mb4_lithuanian_ci");
        UTF8_COLLATION_NAME.put(237, "utf8mb4_slovak_ci");
        UTF8_COLLATION_NAME.put(238, "utf8mb4_spanish2_ci");
        UTF8_COLLATION_NAME.put(239, "utf8mb4_roman_ci");
        UTF8_COLLATION_NAME.put(240, "utf8mb4_persian_ci");
        UTF8_COLLATION_NAME.put(241, "utf8mb4_esperanto_ci");
        UTF8_COLLATION_NAME.put(242, "utf8mb4_hungarian_ci");
        UTF8_COLLATION_NAME.put(243, "utf8mb4_sinhala_ci");
        UTF8_COLLATION_NAME.put(244, "utf8mb4_german2_ci");
        UTF8_COLLATION_NAME.put(245, "utf8mb4_croatian_ci");
        UTF8_COLLATION_NAME.put(246, "utf8mb4_unicode_520_ci");
        UTF8_COLLATION_NAME.put(247, "utf8mb4_vietnamese_ci");
        UTF8_COLLATION_NAME.put(255, "utf8mb4_0900_ai_ci");
        UTF8_COLLATION_NAME.put(256, "utf8mb4_de_pb_0900_ai_ci");
        UTF8_COLLATION_NAME.put(257, "utf8mb4_is_0900_ai_ci");
        UTF8_COLLATION_NAME.put(258, "utf8mb4_lv_0900_ai_ci");
        UTF8_COLLATION_NAME.put(259, "utf8mb4_ro_0900_ai_ci");
        UTF8_COLLATION_NAME.put(260, "utf8mb4_sl_0900_ai_ci");
        UTF8_COLLATION_NAME.put(261, "utf8mb4_pl_0900_ai_ci");
        UTF8_COLLATION_NAME.put(262, "utf8mb4_et_0900_ai_ci");
        UTF8_COLLATION_NAME.put(263, "utf8mb4_es_0900_ai_ci");
        UTF8_COLLATION_NAME.put(264, "utf8mb4_sv_0900_ai_ci");
        UTF8_COLLATION_NAME.put(265, "utf8mb4_tr_0900_ai_ci");
        UTF8_COLLATION_NAME.put(266, "utf8mb4_cs_0900_ai_ci");
        UTF8_COLLATION_NAME.put(267, "utf8mb4_da_0900_ai_ci");
        UTF8_COLLATION_NAME.put(268, "utf8mb4_lt_0900_ai_ci");
        UTF8_COLLATION_NAME.put(269, "utf8mb4_sk_0900_ai_ci");
        UTF8_COLLATION_NAME.put(270, "utf8mb4_es_trad_0900_ai_ci");
        UTF8_COLLATION_NAME.put(271, "utf8mb4_la_0900_ai_ci");
        UTF8_COLLATION_NAME.put(273, "utf8mb4_eo_0900_ai_ci");
        UTF8_COLLATION_NAME.put(274, "utf8mb4_hu_0900_ai_ci");
        UTF8_COLLATION_NAME.put(275, "utf8mb4_hr_0900_ai_ci");
        UTF8_COLLATION_NAME.put(277, "utf8mb4_vi_0900_ai_ci");
        UTF8_COLLATION_NAME.put(278, "utf8mb4_0900_as_cs");
        UTF8_COLLATION_NAME.put(279, "utf8mb4_de_pb_0900_as_cs");
        UTF8_COLLATION_NAME.put(280, "utf8mb4_is_0900_as_cs");
        UTF8_COLLATION_NAME.put(281, "utf8mb4_lv_0900_as_cs");
        UTF8_COLLATION_NAME.put(282, "utf8mb4_ro_0900_as_cs");
        UTF8_COLLATION_NAME.put(283, "utf8mb4_sl_0900_as_cs");
        UTF8_COLLATION_NAME.put(284, "utf8mb4_pl_0900_as_cs");
        UTF8_COLLATION_NAME.put(285, "utf8mb4_et_0900_as_cs");
        UTF8_COLLATION_NAME.put(286, "utf8mb4_es_0900_as_cs");
        UTF8_COLLATION_NAME.put(287, "utf8mb4_sv_0900_as_cs");
        UTF8_COLLATION_NAME.put(288, "utf8mb4_tr_0900_as_cs");
        UTF8_COLLATION_NAME.put(289, "utf8mb4_cs_0900_as_cs");
        UTF8_COLLATION_NAME.put(290, "utf8mb4_da_0900_as_cs");
        UTF8_COLLATION_NAME.put(291, "utf8mb4_lt_0900_as_cs");
        UTF8_COLLATION_NAME.put(292, "utf8mb4_sk_0900_as_cs");
        UTF8_COLLATION_NAME.put(293, "utf8mb4_es_trad_0900_as_cs");
        UTF8_COLLATION_NAME.put(294, "utf8mb4_la_0900_as_cs");
        UTF8_COLLATION_NAME.put(296, "utf8mb4_eo_0900_as_cs");
        UTF8_COLLATION_NAME.put(297, "utf8mb4_hu_0900_as_cs");
        UTF8_COLLATION_NAME.put(298, "utf8mb4_hr_0900_as_cs");
        UTF8_COLLATION_NAME.put(300, "utf8mb4_vi_0900_as_cs");
        UTF8_COLLATION_NAME.put(303, "utf8mb4_ja_0900_as_cs");
        UTF8_COLLATION_NAME.put(304, "utf8mb4_ja_0900_as_cs_ks");
        UTF8_COLLATION_NAME.put(305, "utf8mb4_0900_as_ci");
        UTF8_COLLATION_NAME.put(306, "utf8mb4_ru_0900_ai_ci");
        UTF8_COLLATION_NAME.put(307, "utf8mb4_ru_0900_as_cs");
        UTF8_COLLATION_NAME.put(308, "utf8mb4_zh_0900_as_cs");
        UTF8_COLLATION_NAME.put(309, "utf8mb4_0900_bin");
        UTF8_COLLATION_NAME.put(310, "utf8mb4_nb_0900_ai_ci");
        UTF8_COLLATION_NAME.put(311, "utf8mb4_nb_0900_as_cs");
        UTF8_COLLATION_NAME.put(312, "utf8mb4_nn_0900_ai_ci");
        UTF8_COLLATION_NAME.put(313, "utf8mb4_nn_0900_as_cs");
        UTF8_COLLATION_NAME.put(314, "utf8mb4_sr_latn_0900_ai_ci");
        UTF8_COLLATION_NAME.put(315, "utf8mb4_sr_latn_0900_as_cs");
        UTF8_COLLATION_NAME.put(316, "utf8mb4_bs_0900_ai_ci");
        UTF8_COLLATION_NAME.put(317, "utf8mb4_bs_0900_as_cs");
        UTF8_COLLATION_NAME.put(318, "utf8mb4_bg_0900_ai_ci");
        UTF8_COLLATION_NAME.put(319, "utf8mb4_bg_0900_as_cs");
        UTF8_COLLATION_NAME.put(320, "utf8mb4_gl_0900_ai_ci");
        UTF8_COLLATION_NAME.put(321, "utf8mb4_gl_0900_as_cs");
        UTF8_COLLATION_NAME.put(322, "utf8mb4_mn_cyrl_0900_ai_ci");
        UTF8_COLLATION_NAME.put(323, "utf8mb4_mn_cyrl_0900_as_cs");
    }

    /** 最大负载大小 */
    public static final int MAX_PACKET_SIZE = 256 * 256 * 256 - 1;
    /** 网络包头大小定义，由3个字节的负载大小定义和一个字节的序列ID构成 */
    public static final int HEADER_LENGTH = 4;
    /** 服务器提供的用于鉴权的数据大小 */
    public static final int SEED_LENGTH = 20;

    /* Type ids of response packets. */
    public static final short TYPE_ID_ERROR = 0xFF;
    public static final short TYPE_ID_EOF = 0xFE;
    public static final short TYPE_ID_LOCAL_INFILE = 0xFB;
    public static final short TYPE_ID_OK = 0;

    /* MySQL binary protocol value lengths. */
    public static final int BIN_LEN_INT1 = 1;
    public static final int BIN_LEN_INT2 = 2;
    public static final int BIN_LEN_INT4 = 4;
    public static final int BIN_LEN_INT8 = 8;
    public static final int BIN_LEN_FLOAT = 4;
    public static final int BIN_LEN_DOUBLE = 8;
    public static final int BIN_LEN_DATE = 4;
    public static final int BIN_LEN_TIMESTAMP_NO_FRAC = 7;
    public static final int BIN_LEN_TIMESTAMP_WITH_MICROS = 11;
    public static final int BIN_LEN_TIMESTAMP_WITH_TZ = 13;
    public static final int BIN_LEN_TIME_NO_FRAC = 8;
    public static final int BIN_LEN_TIME_WITH_MICROS = 12;


    /*
     * Command signatures
     */
    public static final int COM_SLEEP = 0;
    public static final int COM_QUIT = 1;
    public static final int COM_INIT_DB = 2;
    public static final int COM_QUERY = 3;
    public static final int COM_FIELD_LIST = 4; // Not used; deprecated in MySQL 5.7.11 and MySQL 8.0.0.
    public static final int COM_CREATE_DB = 5; // Not used; deprecated?
    public static final int COM_DROP_DB = 6; // Not used; deprecated?
    public static final int COM_REFRESH = 7; // Not used; deprecated in MySQL 5.7.11 and MySQL 8.0.0.
    public static final int COM_SHUTDOWN = 8; // Deprecated in MySQL 5.7.9 and MySQL 8.0.0.
    public static final int COM_STATISTICS = 9;
    public static final int COM_PROCESS_INFO = 10; // Not used; deprecated in MySQL 5.7.11 and MySQL 8.0.0.
    public static final int COM_CONNECT = 11;
    public static final int COM_PROCESS_KILL = 12; // Not used; deprecated in MySQL 5.7.11 and MySQL 8.0.0.
    public static final int COM_DEBUG = 13;
    public static final int COM_PING = 14;
    public static final int COM_TIME = 15;
    public static final int COM_DELAYED_INSERT = 16;
    public static final int COM_CHANGE_USER = 17;
    public static final int COM_BINLOG_DUMP = 18;
    public static final int COM_TABLE_DUMP = 19;
    public static final int COM_CONNECT_OUT = 20;
    @Deprecated
    public static final int COM_REGISTER_SLAVE = 21;
    public static final int COM_STMT_PREPARE = 22;
    public static final int COM_STMT_EXECUTE = 23;
    public static final int COM_STMT_SEND_LONG_DATA = 24;
    public static final int COM_STMT_CLOSE = 25;
    public static final int COM_STMT_RESET = 26;
    public static final int COM_SET_OPTION = 27;
    public static final int COM_STMT_FETCH = 28;
    public static final int COM_DAEMON = 29;
    public static final int COM_BINLOG_DUMP_GTID = 30;
    public static final int COM_RESET_CONNECTION = 31;


    public interface Charset{
        int UTF_8 = 255;
    }

    public interface ServerStatus{
        int SERVER_STATUS_IN_TRANS = 1;
        int SERVER_STATUS_AUTOCOMMIT = 2; // Server in auto_commit mode
        int SERVER_MORE_RESULTS_EXISTS = 8; // Multi query - next query exists
        int SERVER_QUERY_NO_GOOD_INDEX_USED = 16;
        int SERVER_QUERY_NO_INDEX_USED = 32;
        int SERVER_STATUS_CURSOR_EXISTS = 64;
        int SERVER_STATUS_LAST_ROW_SENT = 128; // The server status for 'last-row-sent'
        int SERVER_QUERY_WAS_SLOW = 2048;
        int SERVER_SESSION_STATE_CHANGED = 1 << 14; // 16384
    }


    public interface CapabilityFlags{
        /** 使用增强版本的密码鉴权 4.11版本之后该标志位恒为1 */
        int CLIENT_LONG_PASSWORD = 0x00000001;
        /**  发送找到的行数而不是影响的行数，可以忽略 */
        int CLIENT_FOUND_ROWS = 0x00000002;
        /** 支持更长的flags(4bytes) */
        int CLIENT_LONG_FLAG = 0x00000004;
        /** 连接时可以指定DB名字 */
        int CLIENT_CONNECT_WITH_DB = 0x00000008;

        /** 不允许database.table.column，可以忽略 */
        int CLIENT_NO_SCHEMA = 0x00000010;
        /** 支持压缩 */
        int CLIENT_COMPRESS = 0x00000020;
        /** 特殊处理ODBC 3.22之后该标志位无特殊行为，可以忽略 */
        int CLIENT_ODBC = 0x00000040;
        /** 支持处理本地文件 */
        int CLIENT_LOCAL_FILES = 0x00000080;

        /** 处理SQL时忽略左括号前面的空格 */
        int CLIENT_IGNORE_SPACE = 0x00000100;
        /** 支持4.1协议返回*/
        int CLIENT_PROTOCOL_41 = 0x00000200; // for > 4.1.1
        /** 交互模式*/
        int CLIENT_INTERACTIVE = 0x00000400;
        /** 支持SSL，握手之后建立SSL*/
        int CLIENT_SSL = 0x00000800;

        /** Not used. */
        int CLIENT_IGNORE_SIGPIPE = 0x00001000;
        /** 支持事务 */
        int CLIENT_TRANSACTIONS = 0x00002000; // Client knows about transactions
        /** 废弃 */
        @Deprecated
        int CLIENT_RESERVED = 0x00004000; // for 4.1.0 only
        /** 废弃 */
        @Deprecated
        int CLIENT_SECURE_CONNECTION = 0x00008000;

        /** 多语句支持 */
        int CLIENT_MULTI_STATEMENTS = 0x00010000; // Enable/disable multiquery support
        /** 多结果支持 */
        int CLIENT_MULTI_RESULTS = 0x00020000; // Enable/disable multi-results
        int CLIENT_PS_MULTI_RESULTS = 0x00040000; // Enable/disable multi-results for server prepared statements
        int CLIENT_PLUGIN_AUTH = 0x00080000;

        int CLIENT_CONNECT_ATTRS = 0x00100000;
        int CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA = 0x00200000;
        int CLIENT_CAN_HANDLE_EXPIRED_PASSWORD = 0x00400000;
        int CLIENT_SESSION_TRACK = 0x00800000;

        int CLIENT_DEPRECATE_EOF = 0x01000000;
        int CLIENT_OPTIONAL_RESULTSET_METADATA = 0x02000000;
        int CLIENT_ZSTD_COMPRESSION_ALGORITHM = 0x04000000;
        int CLIENT_QUERY_ATTRIBUTES = 0x08000000;

        int CLIENT_MULTI_FACTOR_AUTHENTICATION = 0x10000000;
        // there are 3 flags reserved.
        // CLIENT_CAPABILITY_EXTENSION
        // CLIENT_SSL_VERIFY_SERVER_CERT
        // CLIENT_REMEMBER_OPTIONS


    }

    /**
     * Basic protocol data types as they are defined in http://dev.mysql.com/doc/internals/en/integer.html
     *
     */
    public enum IntegerDataType {

        /**
         * 1 byte Protocol::FixedLengthInteger
         */
        INT1,

        /**
         * 2 byte Protocol::FixedLengthInteger
         */
        INT2,

        /**
         * 3 byte Protocol::FixedLengthInteger
         */
        INT3,

        /**
         * 4 byte Protocol::FixedLengthInteger
         */
        INT4,

        /**
         * 6 byte Protocol::FixedLengthInteger
         */
        INT6,

        /**
         * 8 byte Protocol::FixedLengthInteger
         */
        INT8,

        /**
         * Length-Encoded Integer Type
         */
        INT_LENENC;
    }

    /**
     * Basic protocol data types as they are defined in http://dev.mysql.com/doc/internals/en/string.html
     * which require explicit length specification.
     *
     */
    public static enum StringLengthDataType {

        /**
         * Protocol::FixedLengthString
         * Fixed-length strings have a known, hardcoded length.
         */
        STRING_FIXED,

        /**
         * Protocol::VariableLengthString
         * The length of the string is determined by another field or is calculated at runtime
         */
        STRING_VAR;
    }

    /**
     * Basic self-describing protocol data types as they are defined in http://dev.mysql.com/doc/internals/en/string.html
     *
     */
    public static enum StringSelfDataType {

        /**
         * Protocol::NulTerminatedString
         * Strings that are terminated by a [00] byte.
         */
        STRING_TERM,

        /**
         * Protocol::LengthEncodedString
         * A length encoded string is a string that is prefixed with length encoded integer describing the length of the string.
         * It is a special case of Protocol::VariableLengthString
         */
        STRING_LENENC,

        /**
         * Protocol::RestOfPacketString
         * If a string is the last component of a packet, its length can be calculated from the overall packet length minus the current position.
         */
        STRING_EOF;
    }

    public NativeProtocolConstants() {
        super();
    }
}
