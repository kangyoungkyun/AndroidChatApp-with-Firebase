package com.example.macbookpro.chatapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class SplashActivity extends AppCompatActivity {

    //리니어 레이아웃 전역변수 생성
    private LinearLayout linearLayout;
    //파이어 베이스 모듈 추가 후 전역변수 설정
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //상태바 없애기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //객체생성
        linearLayout = (LinearLayout)findViewById(R.id.splashactivity_linearlayout);

        //firebase remote config 객체
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);

        //디폴트 값으로 res -> xml에 만들어 놓은 데이터 사용
        mFirebaseRemoteConfig.setDefaults(R.xml.default_config);

        //언제든 remote config 새로 고침 가능
        mFirebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            mFirebaseRemoteConfig.activateFetched();

                        } else {

                        }
                        //remote config 적용할 함수 호출
                        displayMessage();
                    }
                });
    }
    public void displayMessage(){

        //firebase에 저장돠어 있는 remote config 설정 데이터들 가져온다
        String splash_background = mFirebaseRemoteConfig.getString("splash_phrase");
        Boolean caps = mFirebaseRemoteConfig.getBoolean("splash_message_caps");
        String splash_message = mFirebaseRemoteConfig.getString("splash_message");

        //적용
        linearLayout.setBackgroundColor(Color.parseColor(splash_background));

        //alert창 띄우기
        if(caps){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(splash_message).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
                builder.create().show();
        }else {
            //remote_config 적용이 안되어 있으면 바로 로그인 창열기
            startActivity(new Intent(this,LoginActivity.class));
        }

    }
}
