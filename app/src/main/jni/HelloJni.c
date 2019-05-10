//
// Created by yicha on 2015/9/24.
//

#include "com_android_hack_jni_NdkJniUtils.h"

/*
 * Class:     com_android_hack_jni_NdkJniUtils
 * Method:    getContent
 * Signature: ()Ljava/lang/String;
 */
const char *content;

JNIEXPORT jstring JNICALL Java_com_android_hack_jni_NdkJniUtils_getContent
        (JNIEnv *env, jobject obj) {
    if (content == NULL)
        return (*env)->NewStringUTF(env, "This just a test for Android Studio NDK JNI developer!");
    return (*env)->NewStringUTF(env, content);
}

JNIEXPORT void JNICALL Java_com_android_hack_jni_NdkJniUtils_setContent
        (JNIEnv *env, jobject obj, jstring txt) {
    content = (*env)->GetStringUTFChars(env, txt, NULL);
}