/*
 *  test_EthernetProbe.c
 *  jnetprobe
 *
 *  Created by Pavlos Georgiadis on 10/16/05.
 *  Copyright 2005 Pavlos Georgiadis. All rights reserved.
 *
 */

#include <test_EthernetProbe.h>

#include <netprobe_capture.h>
#include <netprobe_inject.h>

JNIEXPORT jint JNICALL Java_test_EthernetProbe_init(JNIEnv *env, jobject obj, jbyteArray jdev, jint jsnaplen, jboolean jprom) {
	jchar *dev = (*env)->GetCharArrayElements(env, jdev, 0);
	int result = 0;
	
	result = capture_probe((char*)dev, jsnaplen, (int)jprom, 1000);
	(*env)->ReleaseCharArrayElements(env, jdev, dev, 0);
	if (result == -1) return -1;
	
	result = inject_probe((char*)dev);
	if (result == -1) return -2;
	
	return (jint)0;
}

JNIEXPORT jstring JNICALL Java_test_EthernetProbe_getError(JNIEnv *env, jobject obj, jint source) {
	if(source == 0)
		return (*env)->NewStringUTF(env, (char*)capture_get_error());
	if(source == 1)
		return (*env)->NewStringUTF(env, (char*)inject_get_error());
}

JNIEXPORT jint JNICALL Java_test_EthernetProbe_setFilter(JNIEnv *env, jobject obj, jbyteArray jfilter) {
	jchar *filter = (*env)->GetCharArrayElements(env, jfilter, 0);
	
	int result = capture_set_filter((char*)filter);
	(*env)->ReleaseCharArrayElements(env, jfilter, filter, 0);
	return result;
}

JNIEXPORT jbyteArray JNICALL Java_test_EthernetProbe_receiveNative(JNIEnv *env, jobject obj) {
	capture_receive_packet();
	
	int length = capture_get_packet_length();
	u_char *packet = (u_char*)capture_get_packet();
	jshortArray arr = (*env)->NewByteArray(env, length);
	(*env)->SetByteArrayRegion(env, arr, 0, length, (jbyte*)packet);
	return arr;
}

JNIEXPORT jint JNICALL Java_test_EthernetProbe_sendNative(JNIEnv *env, jobject obj, jbyteArray data) {
	int len = (*env)->GetArrayLength(env, data) - 14;
	
	jbyte src[6], dst[6], ether[2], payload[len-14];
	(*env)->GetByteArrayRegion(env, data, 0, 6, dst);
	(*env)->GetByteArrayRegion(env, data, 6, 6, src);
	(*env)->GetByteArrayRegion(env, data, 12, 2, ether);
	(*env)->GetByteArrayRegion(env, data, 14, len, payload);
	int ethertype = 256*ether[0] + ether[1];
	
	//printf("src: %d:%d:%d:%d:%d:%d, ", src[0], src[1], src[2], src[3], src[4], src[5]);
	//printf("dst: %d:%d:%d:%d:%d:%d, ", dst[0], dst[1], dst[2], dst[3], dst[4], dst[5]);
	//printf("ethertype: (%d-%d)%d, ", ether[0], ether[1], ethertype);
	//printf("length: %d\n", len);
	//int i;
	//for(i=0; i<len; i++){
	//	printf("%d ", payload[i]);
	//}
	//printf("\n");
	
	inject_create_packet((u_char*)src, (u_char*)dst, ethertype, (u_char*)payload, len);
	
	int result = inject_inject_packet();
	//printf("%d bytes writed to the medium\n", result);
	return result;
}

JNIEXPORT void JNICALL Java_test_EthernetProbe_close(JNIEnv *env, jobject obj) {
	capture_close();
	inject_close();
}

JNIEXPORT void JNICALL Java_test_EthernetProbe_printPacket(JNIEnv *env, jobject obj) {
	capture_print_packet_bytes();
}
