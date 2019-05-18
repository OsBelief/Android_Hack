package com.android.hack.jni;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hack.R;

/**
 * 学习Android JNI编程
 * https://www.jianshu.com/p/433b2c93c6a7
 */
public class JniActivity extends AppCompatActivity {
    private TextView mHelloTv;
    private TextView mUserInfoTv;

    private EditText mNameET;
    private EditText mAgeET;
    private EditText mWeightET;
    private RadioGroup mGenderRG;
    private Button mUpdateBtn;

    private NdkJniUtils jniUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni);

        mHelloTv = (TextView) findViewById(R.id.tv_jni_hello);
        mUserInfoTv = findViewById(R.id.tv_jni_person);
        mNameET = findViewById(R.id.et_jni_name);
        mAgeET = findViewById(R.id.et_jni_age);
        mWeightET = findViewById(R.id.et_jni_weight);
        mGenderRG = findViewById(R.id.et_jni_gender);
        mUpdateBtn = findViewById(R.id.btn_update);

        jniUtils = new NdkJniUtils();
        mHelloTv.setText(jniUtils.getContent());

        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jniUtils.setContent(mNameET.getText().toString());
                mHelloTv.setText(jniUtils.getContent());

                UserInfo userInfo = new UserInfo();
                userInfo.mName = mNameET.getText().toString();
                userInfo.mAge = Integer.valueOf(mAgeET.getText().toString());
                userInfo.mGender = mGenderRG.getCheckedRadioButtonId() == R.id.et_jni_male;
                userInfo.mWeight = Float.valueOf(mWeightET.getText().toString());

                jniUtils.setUserInfo(userInfo);

                UserInfo jniUserInfo = jniUtils.getUserInfo();
                if (jniUserInfo == null) {
                    Toast.makeText(JniActivity.this, "JNI getUserInfo is null!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mUserInfoTv.setText(jniUserInfo.toString());
            }
        });
    }
}
