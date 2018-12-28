/*
 *  probe_inject.h
 *  jnetprobe
 *
 *  Created by Pavlos Georgiadis on 10/18/05.
 *  Copyright 2005 Pavlos Georgiadis. All rights reserved.
 *
 */

#include <libnet.h>

int inject_probe(char*);
void inject_create_packet(u_char*, u_char*, int, u_char*, int);
int inject_inject_packet();
void inject_close();

void inject_print_error();
char* inject_get_error();
