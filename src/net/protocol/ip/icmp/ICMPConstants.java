package net.protocol.ip.icmp;

/**
 * The ICMPConstants contains the constant values used from the icmp protocol.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	
 */
public class ICMPConstants {
	/* ICMP type*/
	public static final int ECHO_REPLY = 0x0000;
	public static final int DESTINATION_UNREACHABLE = 0x0003;
	public static final int SOURCE_QUENCH = 0x0004;
	public static final int REDIRECT = 0x0005;
	public static final int ALTERNATE_HOST = 0x0006;
	public static final int ECHO_REQUEST = 0x0008;
	public static final int ROUTER_ADVERTISMENT = 0x0009;
	public static final int ROUTER_SOLICITATION = 0x0010;
	public static final int TIME_EXEEDED = 0x0011;
	public static final int PARAMETER_PROBLEM = 0x0012;
	public static final int TIMESTAMP_REQUEST = 0x0013;
	public static final int TIMESTAMP_REPLY = 0x0014;
	public static final int INFORMATION_REQUEST = 0x0015;
	public static final int INFORMATION_REPLY = 0x0016;
	public static final int ADDRESS_MASK_REQUEST = 0x0017;
	public static final int ADDRESS_MASK_REPLY = 0x0018;
	public static final int TRACEROUTE = 0x0030;
	public static final int CONVERSION_ERROR = 0x0031;
}
