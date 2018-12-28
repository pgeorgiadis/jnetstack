/*
 *  injecttest.c
 *  jnetprobe
 *
 *  Created by Pavlos Georgiadis on 10/14/05.
 *  Copyright 2005 Pavlos Georgiadis. All rights reserved.
 *
 */

#include <netprobe_inject.h>

int main() {
	if (inject_probe("en0") == -1)
		printf("%s\n", inject_get_error());
	
	u_char src[6] = {0x00, 0x0d, 0x93, 0x72, 0x1b, 0x1a};
	u_char dst[6] = {0x00, 0x30, 0x4f, 0x18, 0xbc, 0x29};
	int ethertype = 256*8 + 6;
	u_char payload[50] = {0, 1, 8, 0, 6, 4, 0, 2, 0, 13, -109, 114, 27, 26, 10, -125, 0, 6, 0, 48, 79, 24, 
		-68, 41, 10, -125, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	
	printf("src: %d:%d:%d:%d:%d:%d, ", src[0], src[1], src[2], src[3], src[4], src[5]);
	printf("dst: %d:%d:%d:%d:%d:%d, ", dst[0], dst[1], dst[2], dst[3], dst[4], dst[5]);
	printf("ethertype: %d, ", ethertype);
	int i;
	for(i=0; i<50; i++){
		printf("%d ", payload[i]);
	}
	printf("\n");
	
	inject_create_packet((u_char*)src, (u_char*)dst, ethertype, (u_char*)payload, 50);
	
	int result = inject_inject_packet();
	if (result == -1)
		printf("%s\n", inject_get_error());
	else
		printf("%d bytes writed to the medium\n", result);
}
