#include <jni.h>
/* Header for class test_EthernetProbe */

#ifndef _Included_test_EthernetProbe
#define _Included_test_EthernetProbe
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jint JNICALL Java_test_EthernetProbe_init(JNIEnv *, jobject, jbyteArray, jint, jboolean);
JNIEXPORT jstring JNICALL Java_test_EthernetProbe_getError(JNIEnv *, jobject, jint);
JNIEXPORT jint JNICALL Java_test_EthernetProbe_setFilter(JNIEnv *, jobject, jbyteArray);
JNIEXPORT jbyteArray JNICALL Java_test_EthernetProbe_receiveNative(JNIEnv *, jobject);
JNIEXPORT jint JNICALL Java_test_EthernetProbe_sendNative(JNIEnv *, jobject, jbyteArray);
JNIEXPORT void JNICALL Java_test_EthernetProbe_close(JNIEnv *env, jobject obj);

JNIEXPORT void JNICALL Java_test_EthernetProbe_printPacket(JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
