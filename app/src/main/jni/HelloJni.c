//
// Created by yicha on 2015/9/24.
//

// <>标准库头文件, ""自定义头文件
#include<android/log.h>
#include <string.h>
#include "com_android_hack_jni_NdkJniUtils.h"

#ifndef LOG_TAG
#define LOG_TAG "HELLO_JNI"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,LOG_TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,LOG_TAG ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,LOG_TAG ,__VA_ARGS__) // 定义LOGF类型
#endif

/*
 * Class:     com_android_hack_jni_NdkJniUtils
 * Method:    getContent
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_android_hack_jni_NdkJniUtils_getContent
        (JNIEnv *env, jobject obj) {
    if (content == NULL)
        return (*env)->NewStringUTF(env, "Hello JNI!");
    return (*env)->NewStringUTF(env, content);
}

JNIEXPORT void JNICALL Java_com_android_hack_jni_NdkJniUtils_setContent
        (JNIEnv *env, jobject obj, jstring txt) {
    content = (*env)->GetStringUTFChars(env, txt, NULL);
    // strcmp比较字符串, 区分大小写, 返回0表示相等
    if (strcmp(content, "hello") == 0) {
        LOGI("input hello!");
    } else {
        LOGI("input %s!", content);
    }
}

// C结构体向Java类转化
JNIEXPORT jobject JNICALL Java_com_android_hack_jni_NdkJniUtils_getUserInfo
        (JNIEnv *env, jobject obj) {
    LOGD("Java_com_android_hack_jni_NdkJniUtils_getUserInfo---");

    jclass userInfoClass = (*env)->FindClass(env, "com/android/hack/jni/UserInfo");
    jobject userInfo = (*env)->AllocObject(env, userInfoClass);
    if (userInfo == NULL) {
        LOGE("AllocObject Failed!");
    }
    LOGI("AllocObject Success!");

    LOGI("jUserInfo.age=%d", jUserInfo.age);
    jfieldID jAge = (*env)->GetFieldID(env, userInfoClass, "mAge", "I");
    (*env)->SetIntField(env, userInfo, jAge, jUserInfo.age);

    LOGI("jUserInfo.gender=%s", (jUserInfo.gender ? "true" : "false"));
    jfieldID jGender = (*env)->GetFieldID(env, userInfoClass, "mGender", "Z");
    (*env)->SetBooleanField(env, userInfo, jGender, jUserInfo.gender);

    LOGI("jUserInfo.name=%s", jUserInfo.name);
    jfieldID jName = (*env)->GetFieldID(env, userInfoClass, "mName", "Ljava/lang/String;");
    (*env)->SetObjectField(env, userInfo, jName, (*env)->NewStringUTF(env, jUserInfo.name));

    LOGI("jUserInfo.weight=%f", jUserInfo.weight);
    jfieldID jWeight = (*env)->GetFieldID(env, userInfoClass, "mWeight", "F");
    (*env)->SetFloatField(env, userInfo, jWeight, jUserInfo.weight);

    return userInfo;
}

// Java类向C结构体转化
JNIEXPORT void JNICALL Java_com_android_hack_jni_NdkJniUtils_setUserInfo
        (JNIEnv *env, jobject obj, jobject userInfoObj) {
    LOGD("Java_com_android_hack_jni_NdkJniUtils_setUserInfo---");

    jclass userInfoClass = (*env)->FindClass(env,
                                             "com/android/hack/jni/UserInfo"); // 获取Java中的实例类UserInfo, 类似Java反射
    jfieldID jAge = (*env)->GetFieldID(env, userInfoClass, "mAge", "I"); // 获取整形变量mAge的定义
    jUserInfo.age = (*env)->GetIntField(env, userInfoObj, jAge);

    jfieldID jGender = (*env)->GetFieldID(env, userInfoClass, "mGender", "Z");
    jUserInfo.gender = (*env)->GetBooleanField(env, userInfoObj, jGender);

    jfieldID jName = (*env)->GetFieldID(env, userInfoClass, "mName", "Ljava/lang/String;");
    jstring name = (*env)->GetObjectField(env, userInfoObj, jName);
    jUserInfo.name = (*env)->GetStringUTFChars(env, name, NULL);;

    jfieldID jWeight = (*env)->GetFieldID(env, userInfoClass, "mWeight", "F");
    jUserInfo.weight = (*env)->GetFloatField(env, userInfoObj, jWeight);
}