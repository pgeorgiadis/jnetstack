package net.protocol.arp;

/**
 * The ARPConstants contains the constant values used from the arp protocol.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	14.8.05
 */
public class ARPConstants {
	/* Hardware types */
	public static final int H_ETHERNET = 0x0001;		/* Ethernet */
	public static final int H_EXP_ETHERNET = 0x0002;	/* Experimental Ethernet */
	public static final int H_AX25 = 0x0003;			/* Amateur radio AX.25 */
	public static final int H_PRONET_TOKEN_RING = 0x0004;	/* Proteon ProNET Token Ring */
	public static final int H_CHAOS = 0x0005;		/* Chaos */
	public static final int H_IEEE_802 = 0x0006;		/* IEEE 802 */
	public static final int H_ARCNET = 0x0007;		/* ARCNET */
	public static final int H_HYPERCHANNEL = 0x0008;		/* Hyperchannel */
	public static final int H_LANSTAR = 0x0009;		/* Lanstar */
	public static final int H_AUTONET = 0x0010;		/* Autonet Short Address */
	public static final int H_LOCALTALK = 0x0011;	/* LocalTalk */
	public static final int H_LOCALNET = 0x0012;		/* IBM PCNet or SYTEK LocalNET */
	public static final int H_ULTRA_LINK = 0x0013;	/* Ultra link */
	public static final int H_SMDS = 0x0014;			/* SMDS */
	public static final int H_FRAME_RELAY = 0x0015;	/* Frame Relay */
	public static final int H_ATM_1 = 0x0016;		/* Asynchronous Transmission Mode */
	public static final int H_HDLC = 0x0017;			/* HDLC */
	public static final int H_FIBER_CHANNEL = 0x0018;	/* Fiber Channel */
	public static final int H_ATM_2 = 0x0019;		/* Asynchronous Transmission Mode */
	public static final int H_SERIAL_LINE = 0x0020;	/* Serial Line */
	public static final int H_ATM_3 = 0x0021;		/* Asynchronous Transmission Mode */
	public static final int H_MIL_STD_188_220 = 0x0022;	/* MIL-STD-188-220 */
	public static final int H_METRICOM = 0x0023;		/* Metricom */
	public static final int H_IEEE_1394 = 0x0024;	/* IEEE-1394 */
	public static final int H_MAPOS = 0x0025;		/* MAPOS */
	public static final int H_TWINAXIAL = 0x0026;	/* Twinaxial */
	public static final int H_EUI_64 = 0x0027;		/* EUI-64 */
	public static final int H_HIPARP = 0x0028;		/* HIPARP */
	public static final int H_ISO_7816_3 = 0x0029;	/* IP and ARP over ISO 7816-3 */
	public static final int H_ARP_SEC = 0x0030;		/* ARPsec */
	public static final int H_IPSEC_TUNNEL = 0x0031;	/* IPsec tunnel */
	public static final int H_INFINIBAND = 0x0032;	/* Infiniband */
	public static final int H_TIA_102 = 0x0033;		/* CAI, TIA-102 Project 25 Common Air Interface */
	
	/* Protocol types */
	public static final int P_IPv4 = 0x0800;			/* IPv4 */
	
	/* Opcodes */
	public static final int OP_REQUEST = 0x0001;			/* ARP request */
	public static final int OP_REPLY = 0x0002;			/* ARP reply */
	public static final int OP_REVERSE_REQUEST = 0x0003;	/* RARP request */
	public static final int OP_REVERSE_REPLY = 0x0004;	/* RARP reply */
	public static final int OP_DRARP_REQUEST = 0x0005;	/* DRARP request */
	public static final int OP_DRARP_REPLY = 0x0006;		/* DRARP reply */
	public static final int OP_DRARP_ERROR = 0x0007;		/* DRARP error */
	public static final int OP_INARP_REQUEST = 0x0008;	/* InARP request */
	public static final int OP_INARP_REPLY = 0x0009;		/* InARP reply */
	public static final int OP_ARP_NAK = 0x0010;			/* ARP NAK */
	public static final int OP_MARS_REQUEST = 0x0011;		/* MARS */
	public static final int OP_MARS_MULTI = 0x0012;		/* MARS */
	public static final int OP_MARS_MSERV = 0x0013;		/* MARS */
	public static final int OP_MARS_JOIN = 0x0014;		/* MARS */
	public static final int OP_MARS_LEAVE = 0x0015;		/* MARS */
	public static final int OP_MARS_NAK = 0x0016;			/* MARS */
	public static final int OP_MARS_UNSERV = 0x0017;		/* MARS */
	public static final int OP_MARS_SJOIN = 0x0018;		/* MARS */
	public static final int OP_MARS_SLEAVE = 0x0019;		/* MARS */
	public static final int OP_MARS_GLIST_REQUEST = 0x0020;	/* MARS */
	public static final int OP_MARS_GLIST_REPLY = 0x0021;		/* MARS */
	public static final int OP_MARS_REDIRECT_MAP = 0x0022;	/* MARS */
	public static final int OP_MARS_UNARP = 0x0023;		/* MARS */
}