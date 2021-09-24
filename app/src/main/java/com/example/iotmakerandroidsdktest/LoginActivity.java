package com.example.iotmakerandroidsdktest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kt.gigaiot_sdk.Callback.APICallback;
import com.kt.gigaiot_sdk.GigaIotOAuthNew;
import com.kt.gigaiot_sdk.data.GiGaIotOAuthResponse;
import com.kt.gigaiot_sdk.network.ApiConstants;

public class LoginActivity extends AppCompatActivity {
    private EditText mEtId, mEtPw, mEtAppId, mEtSec;

    String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApplicationPreference.init(this);

        String token = ApplicationPreference.getInstance().getPrefAccessToken();
        if (token != null && token.equals("") == false) {
            Intent intent = new Intent(LoginActivity.this, DeviceListActivity.class);
            startActivity(intent);
            finish();
        }

        mEtId = findViewById(R.id.et_login_id);
        mEtPw = findViewById(R.id.et_login_pw);
        mEtAppId = findViewById(R.id.et_app_id);
        mEtSec = findViewById(R.id.et_app_secret);

        Button ivLogin =  findViewById(R.id.iv_login_bt);
        ivLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){

                    case R.id.iv_login_bt:
                        String id = mEtId.getText().toString();
                        String pw = mEtPw.getText().toString();
                        String app_id = mEtAppId.getText().toString();
                        String secret = mEtSec.getText().toString();

                        if(TextUtils.isEmpty(id)){
                            Toast.makeText(LoginActivity.this, R.string.login_id_empty, Toast.LENGTH_SHORT).show();
                            return;
                        } else if(TextUtils.isEmpty(pw)){
                            Toast.makeText(LoginActivity.this, R.string.login_pw_empty, Toast.LENGTH_SHORT).show();
                            return;
                        } else if(TextUtils.isEmpty(app_id)){
                            Toast.makeText(LoginActivity.this, R.string.login_app_id_empty, Toast.LENGTH_SHORT).show();
                            return;
                        } else if(TextUtils.isEmpty(secret)){
                            Toast.makeText(LoginActivity.this, R.string.login_secret_empty, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // LoginTask 클래스의 doInBackground() 메소드 호출
                        new LoginTask().execute(id,pw,app_id,secret);

                        break;

                }
            }
        });
    }
    public class LoginTask {
        ProgressDialog progressDialog;
        String id;

        protected void execute(String id, String pw, String app_id, String secret) {

            // API Callback 객체 생성
            APICallback callback = new APICallback<GiGaIotOAuthResponse>() {
                public void onStart() {
                    progressDialog = ProgressDialog.show(LoginActivity.this, "", getResources().getString(R.string.common_wait), true, false);
                }

                public void onDoing(GiGaIotOAuthResponse response) {
                    progressDialog.dismiss();
                    progressDialog = null;

                    if (response != null) {
                        if (response.getResponseCode().equals(ApiConstants.CODE_OK)) {
                            ApplicationPreference.getInstance().setPrefAccountId(id);
                            ApplicationPreference.getInstance().setPrefAccessToken(response.getAccessToken());
                            Intent intent = new Intent(LoginActivity.this, DeviceListActivity.class);
                            startActivity(intent);
                            // Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_fail) + response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                public void onFail() {
                    progressDialog.dismiss();
                    progressDialog = null;

                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
                }
            };

            // 로그인 시도
            new GigaIotOAuthNew(app_id, secret, callback).loginWithPassword(id, pw);
        }
    }
}