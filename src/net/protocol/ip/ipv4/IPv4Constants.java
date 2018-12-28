package net.protocol.ip.ipv4;

/**
 * The IPv4Constants contains the constant values used from the ip version 4 protocol.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	
 */
public class IPv4Constants {
	/* Presedence */
	public static final int ROUTINE = 0x0000;
	public static final int PRIORITY = 0x0001;
	public static final int IMMEDIATE = 0x0002;
	public static final int FLASH = 0x0003;
	public static final int FLASH_OVERRIDE = 0x0004;
	public static final int CRITICAL_ECP = 0x0005;
	public static final int INTERNETWORK_CONTROL = 0x0006;
	public static final int NETWORK_CONTROL = 0x0007;
	
	/* Protocol */
	public static final int HOPOPT = 0x0000;
	public static final int ICMP = 0x0001;
	public static final int IGMP = 0x0002;
	public static final int RGMP = 0x0002;
	public static final int IP_IN_IP = 0x0004;
	public static final int ST = 0x0005;
	public static final int TCP = 0x0006;
	public static final int IGRP = 0x0009;
	public static final int UDP = 0x0011;
	public static final int RDP = 0x0027;
	public static final int RSVP = 0x0046;
	public static final int GRE = 0x0047;
	public static final int ESP = 0x0050;
	public static final int AH = 0x0051;
}
