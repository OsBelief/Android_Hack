package com.android.hack.jni;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 学习Android JNI编程
 */
public class JniActivity extends AppCompatActivity {
    private TextView helloTv;
    private NdkJniUtils jniUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni);

        helloTv = (TextView) findViewById(R.id.tv_jni_hello);
        jniUtils = new NdkJniUtils();
        helloTv.setText(jniUtils.getUserName());
    }
}
