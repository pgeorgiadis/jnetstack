# Makefile for jnetprobe
# Copyright (c) 2005 Pavlos Georgiadis

JNI_INCLUDE = /Library/Java/Home/include
LIB_INCLUDE = /sw/include
COMPILE_OPTION = -shared
CC = gcc
ASTLINK=-Wl,-dynamic
SOLINK=-dynamic -bundle -undefined suppress -force_flat_namespace

all: jnetprobe capturetest injecttest

jnetprobe:	test_EthernetProbe.c netprobe_capture.c netprobe_inject.c
		$(CC) $(ASTLINK) $(SOLINK)\
		-I$(JNI_INCLUDE) -I$(LIB_INCLUDE) -I./\
		-lpcap -lnet\
		test_EthernetProbe.c netprobe_capture.c netprobe_inject.c\
		-o jnetprobe.so

capturetest:	netprobe_capture.c capturetest.c
		$(CC) -I$(LIB_INCLUDE) -I./\
		-lpcap\
		netprobe_capture.c capturetest.c\
		-o capturetest

injecttest:	netprobe_inject.c injecttest.c
		$(CC) $(COMPILE_OPTIONS)\
		-I$(LIB_INCLUDE) -I./\
		-lpcap -lnet\
		netprobe_inject.c injecttest.c\
		-o injecttest

install:	jnetprobe.so
		cp jnetprobe.so /usr/lib

uninstall:	/usr/lib/jnetprobe.so
		rm /usr/lib/jnetprobe.so

clean:		
		rm jnetprobe.so capturetest injecttest
