package com.android.hack.jni;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.hack.R;

/**
 * 学习Android JNI编程
 */
public class JniActivity extends AppCompatActivity {
    private TextView helloTv;
    private EditText mEditText;
    private Button mButton;
    private NdkJniUtils jniUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni);

        mEditText = findViewById(R.id.et_jni_hello);
        helloTv = (TextView) findViewById(R.id.tv_jni_hello);
        mButton = findViewById(R.id.btn_update);

        jniUtils = new NdkJniUtils();
        helloTv.setText(jniUtils.getContent());

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jniUtils.setContent(mEditText.getText().toString());

                helloTv.setText(jniUtils.getContent());
            }
        });
    }
}
