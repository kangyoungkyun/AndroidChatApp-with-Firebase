package com.example.macbookpro.chatapp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.macbookpro.chatapp.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

public class SignupActivity extends AppCompatActivity {
    private static final int PICK_FROM_ALBUM = 10;
    private FirebaseAuth mAuth;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    //private StorageReference mStorageRef;
    private EditText email;
    private EditText name;
    private EditText password;
    private Button signup;
    private ImageView profile;
    private Uri imageUri;



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
        profile = (ImageView)findViewById(R.id.signupActivity_imageview_profile);
        email = (EditText)findViewById(R.id.signupActivity_edittext_email);
        name = (EditText)findViewById(R.id.signupActivity_edittext_name);
        password = (EditText)findViewById(R.id.signupActivity_edittext_password);
        signup = (Button)findViewById(R.id.signupActivity_button_signup);
        signup.setBackgroundColor(Color.parseColor(splash_background));

        //회원 가입 클릭했을 때
        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(SignupActivity.this,"회원 가입 클릭", Toast.LENGTH_SHORT).show();

                //입력 값중에 null이 있으면
                if(email.getText().toString() == null || name.getText().toString() == null || password.getText().toString() == null){

                    Toast.makeText(SignupActivity.this,"빈칸을 입력해주세요", Toast.LENGTH_LONG).show();
                    return;
                }

                //사진이 비었다면
                if(profile.getDrawable() == null){
                    Toast.makeText(SignupActivity.this, "사진을 선택해주세요", Toast.LENGTH_LONG).show();
                    return;
                }

                //파이어 베이스 가입 함수
                mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //가입 성공하면 task 로 user 정보가 넘어 온다.
                        //가입한 유저의 uid를 가져 온다.
                        final String uid = task.getResult().getUser().getUid();
                        //파이어 베이스 저장소에 사진 저장
                        Toast.makeText(SignupActivity.this,"파베 가입 함수", Toast.LENGTH_SHORT).show();

                        FirebaseStorage.getInstance().getReference().child("userImages").child(uid).putFile(imageUri).addOnCompleteListener(
                                new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                Toast.makeText(SignupActivity.this,"파베 사진저장", Toast.LENGTH_SHORT).show();
                                @SuppressWarnings("VisibleForTests")
                                String imageUrl = task.getResult().getDownloadUrl().toString();
                                //데이터를 담을 모델을 만든다
                                UserModel userModel = new UserModel();
                                //이름을 가져 온다
                                userModel.userName = name.getText().toString();
                                userModel.profileImage = imageUrl;
                                //파이어 베이스에 저장한다.
                                FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);
                            }
                        });

                    }
                });
            }
        });

        //사진을 눌렀을 때 사진 라이브러리 가져오기
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,PICK_FROM_ALBUM);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK){

            profile.setImageURI(data.getData()); // 가운데 뷰를 변경
            imageUri = data.getData(); //이미지 경로 원본

        }
    }
}
