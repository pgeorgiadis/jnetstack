/*
 *  netprobe_capture.c
 *  jnetprobe
 *
 *  Created by Pavlos Georgiadis on 10/14/05.
 *  Copyright 2005 Pavlos Georgiadis. All rights reserved.
 *
 */

#include <netprobe_capture.h>

pcap_t *p = NULL;
char err_buf[PCAP_ERRBUF_SIZE];

const u_char* packet;
struct pcap_pkthdr header;

int capture_probe(char* dev, int snaplen, int promisc, int timeout) {
	p = pcap_open_live(dev, snaplen, promisc, timeout, err_buf);
	if (p == NULL)
		return -1;
		
	return 0;
}

int capture_set_filter(char* filter_exp) {
	bpf_u_int32 net;
	struct bpf_program fp;
	
	if (pcap_compile(p, &fp, filter_exp, 0, net) == -1)
		return(-1);
	
	if (pcap_setfilter(p, &fp) == -1)
		return(-2);
		
	return 0;
}

void capture_receive_packet() {
	int r;
	do {
		packet = pcap_next(p, &header);
	} while(packet == NULL);
}

int capture_get_packet_length() {
	return header.caplen;
}

const u_char* capture_get_packet() {
	return packet;
}

void capture_close() {
	pcap_close(p);
}

void capture_print_error() {
	printf("capture_probe error: %s", err_buf);
}

char* capture_get_error() {
	return err_buf;
}

void capture_print_packet_bytes() {
	int i = header.caplen;
	printf("Internal dump. Length: %d\n", i);
	while(i != 0) {
		int p = header.caplen-i--;
		printf("[%d]%d ", p, packet[p]);
	}
	printf("\n");
}
