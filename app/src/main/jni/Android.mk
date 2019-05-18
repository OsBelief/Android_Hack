LOCAL_PATH := $(call my-dir)

#完整的模块声明
include $(CLEAR_VARS)
LOCAL_LDLIBS :=-llog
LOCAL_MODULE    := HelloJni
LOCAL_SRC_FILES := HelloJni.c

#开始构建
include $(BUILD_SHARED_LIBRARY)
