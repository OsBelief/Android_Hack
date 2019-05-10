package com.android.hack.jni;

/**
 * Created by chengzhenhua on 2015/9/24.
 */
public class NdkJniUtils {
    static {
        /**
         * 加载动态库libHelloJni.so
         * 加载so, 不要带上前缀lib和后缀.so
         */
        System.loadLibrary("HelloJni");    //defaultConfig.ndk.moduleName
    }

    public native String getContent();

    public native void setContent(String content);
}
