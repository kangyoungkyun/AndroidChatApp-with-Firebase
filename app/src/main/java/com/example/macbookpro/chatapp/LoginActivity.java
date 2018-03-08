package com.example.macbookpro.chatapp;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private Button signin;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        String splash_background = mFirebaseRemoteConfig.getString("splash_phrase");

        //롤리팝 이후 부터 remote_config로 상태바 색깔 지정 사용 가능
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor(splash_background));
        }

        login = (Button)findViewById(R.id.loginActivity_button_login);
        signin = (Button)findViewById(R.id.loginActivity_button_signin);

        login.setBackgroundColor(Color.parseColor(splash_background));
        signin.setBackgroundColor(Color.parseColor(splash_background));
    }
}
