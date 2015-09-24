package com.android.hack.jni;

/**
 * Created by chengzhenhua on 2015/9/24.
 */
public class NdkJniUtils {
    static {
        System.loadLibrary("HelloJni");    //defaultConfig.ndk.moduleName
    }

    public native String getUserName();
}
