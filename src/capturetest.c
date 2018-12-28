/*
 *  capturetest.c
 *  jnetprobe
 *
 *  Created by Pavlos Georgiadis on 10/14/05.
 *  Copyright 2005 Pavlos Georgiadis. All rights reserved.
 *
 */

#include <netprobe_capture.h>

int main() {
	capture_probe("en0", 2000, 1, 1000);
	capture_set_filter("");
	
	int count = 0;
	while(1) {
		capture_receive_packet();
		printf("Packet: %d\n", count++);
		capture_print_packet_bytes();
		printf("\n");
	}
}	
