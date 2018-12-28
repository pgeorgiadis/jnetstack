/*
 *  probe_inject.c
 *  jnetprobe
 *
 *  Created by Pavlos Georgiadis on 10/18/05.
 *  Copyright 2005 Pavlos Georgiadis. All rights reserved.
 *
 */

#include <netprobe_inject.h>

libnet_t *l = NULL;
char err_buf[LIBNET_ERRBUF_SIZE];

libnet_ptag_t eth = 0;

int inject_probe(char* device) {
	l = libnet_init(LIBNET_LINK, device, err_buf);
	if (l == NULL)
		return -1;
	return 0;
}

void inject_create_packet(u_char* src, u_char* dst, int ethertype, u_char* payload, int payload_len) {
	eth = libnet_build_ethernet(
		dst,
		src,
		ethertype,
		(u_char*)payload,
		payload_len,
		l,
		eth
	);
}

int inject_inject_packet() {
	return libnet_write(l);
}

void inject_close() {
	libnet_destroy(l);
}

void inject_print_error() {
	printf("inject_probe error: %s", err_buf);
}

char* inject_get_error() {
	return err_buf;
}
