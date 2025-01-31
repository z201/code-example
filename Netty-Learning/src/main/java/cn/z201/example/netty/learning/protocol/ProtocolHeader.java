package cn.z201.example.netty.learning.protocol;

/**
 * Jelly传输层协议头.
 *
 * Protocol __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __ __
 * __ __ __ __ | | | | | | | 2 1 1 1 4 Uncertainty |__ __ __ __|__ __ __ __|__ __ __ __|__
 * __ __ __|__ __ __ __ __|__ __ __ __ __ __ __ __ __| | | | | | | | Magic Sign Type
 * Status Body Length Body Content |__ __ __ __|__ __ __ __|__ __ __ __|__ __ __ __|__ __
 * __ __ __|__ __ __ __ __ __ __ __ __|
 *
 * 协议头9个字节定长 Magic // 数据包的验证位，short类型 Sign // 消息标志，请求／响应／通知，byte类型 Type //
 * 消息类型，登录／发送消息等，byte类型 Status // 响应状态，成功／失败，byte类型 BodyLength // 协议体长度，int类型
 *
 * @author z201.coding@gmail.com
 */
public class ProtocolHeader {

    /** 协议头长度 */
    public static final int HEADER_LENGTH = 9;

    /** Magic */
    public static final short MAGIC = (short) 0xabcd;

    /** 消息标志 */
    private byte sign;

    /**
     * sign: 0x01 ~ 0x03
     * ===========================================================================================
     */
    public static final byte REQUEST = 0x01; // 请求 Client --> Server

    public static final byte RESPONSE = 0x02; // 响应 Server --> Client

    public static final byte NOTICE = 0x03; // 通知 Server --> Client e.g.消息转发

    /** 消息类型 */
    private byte type;

    /**
     * type: 0x11 ~ 0x23
     * ===========================================================================================
     */
    public static final byte LOGIN = 0x11; // 登录

    public static final byte REGISTER = 0x12; // 注册

    public static final byte LOGOUT = 0x13; // 登出

    public static final byte ALL_MESSAGE = 0x14; // 消息

    public static final byte ONLINE_USER_LIST = 0x21; // 获取用户列表

    public static final byte RECONNCET = 0x22; // 重连

    public static final byte HEARTBEAT = 0x23; // 心跳

    /** 响应状态 */
    private byte status;

    /**
     * status: 0x31 ~ 0x34
     * =========================================================================================
     */
    public static final byte SUCCESS = 0x31; // 49 请求成功

    public static final byte REQUEST_ERROR = 0x32; // 50 请求错误

    public static final byte SERVER_BUSY = 0x33; // 51 服务器繁忙

    public static final byte SERVER_ERROR = 0x34; // 52 服务器错误

    /** 消息体长度 */
    private int bodyLength;

}
