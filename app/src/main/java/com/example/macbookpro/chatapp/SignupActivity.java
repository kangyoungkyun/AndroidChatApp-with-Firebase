package com.example.macbookpro.chatapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.macbookpro.chatapp.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private EditText email;
    private EditText name;
    private EditText password;
    private Button signup;
    //private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //리모트 콘피그 객체 생성
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        //파이어 베이스 리모트 콘피그 색깔 값 가져오기
        String splash_background = mFirebaseRemoteConfig.getString(getString(R.string.rc_color));


        //파이어 베이스 인증 객체
        mAuth = FirebaseAuth.getInstance();
        //엘리먼트 객체
        email = (EditText)findViewById(R.id.signupActivity_edittext_email);
        name = (EditText)findViewById(R.id.signupActivity_edittext_name);
        password = (EditText)findViewById(R.id.signupActivity_edittext_password);
        signup = (Button)findViewById(R.id.signupActivity_button_signup);
        signup.setBackgroundColor(Color.parseColor(splash_background));
        //회원 가입 클릭했을 때
        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //입력 값중에 null이 있으면
                if(email.getText().toString() == null || name.getText().toString() == null || password.getText().toString() == null){
                    return;

                }
                //파이어 베이스 가입 함수
                mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //가입 성공하면 task 로 user 정보가 넘어 온다.
                        //데이터를 담을 모델을 만든다
                        UserModel userModel = new UserModel();
                        //이름을 가져 온다
                        userModel.userName = name.getText().toString();
                        //가입한 유저의 uid를 가져 온다.
                        String uid = task.getResult().getUser().getUid();
                        //파이어 베이스에 저장한다.
                        FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);
                    }
                });
            }
        });
    }
}
