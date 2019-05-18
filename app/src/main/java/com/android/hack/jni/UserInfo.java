package com.android.hack.jni;

public class UserInfo {
    public int mAge;
    public boolean mGender;
    public String mName;
    public float mWeight;

    @Override
    public String toString() {
        return "UserInfo{" +
                "mAge=" + mAge +
                ", mGender=" + mGender +
                ", mName='" + mName + '\'' +
                ", mWeight=" + mWeight +
                '}';
    }
}
