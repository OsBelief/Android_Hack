LOCAL_PATH := $(call my-dir)

#完整的模块声明
include $(CLEAR_VARS)
LOCAL_MODULE := HelloJni	
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	E:\AndroidStudioProjects\Android_Hack\app\src\main\jni\HelloJni.c \
LOCAL_C_INCLUDES += E:\AndroidStudioProjects\Android_Hack\app\src\main\jni
LOCAL_C_INCLUDES += E:\AndroidStudioProjects\Android_Hack\app\src\release\jni

#开始构建
include $(BUILD_SHARED_LIBRARY)
