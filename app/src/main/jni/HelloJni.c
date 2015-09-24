//
// Created by yicha on 2015/9/24.
//

#include "com_android_hack_jni_NdkJniUtils.h"
/*
 * Class:     com_android_hack_jni_NdkJniUtils
 * Method:    getUserName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_android_hack_jni_NdkJniUtils_getUserName
        (JNIEnv *env, jobject obj){
    return (*env)->NewStringUTF(env,"This just a test for Android Studio NDK JNI developer!");
}