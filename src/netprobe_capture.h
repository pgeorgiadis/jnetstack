/*
 *  probe_receiver.h
 *  jnetprobe
 *
 *  Created by Pavlos Georgiadis on 10/14/05.
 *  Copyright 2005 Pavlos Georgiadis. All rights reserved.
 *
 */

#include <pcap.h>

int capture_probe(char*, int, int, int);
int capture_set_filter(char*);
void capture_receive_packet();
int capture_get_packet_length();
const u_char* capture_get_packet();
void capture_close();

void capture_print_error();
char* capture_get_error();
void capture_print_packet_bytes();
