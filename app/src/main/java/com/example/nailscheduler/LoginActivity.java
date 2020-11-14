package com.example.nailscheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.nailscheduler.enums.UserType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button sign_up_btn,sign_in_btn;
    private EditText mEmail, mPassword;
    private FirebaseAuth fAuth;
    private UserType mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sign_up_btn = findViewById(R.id.btnSignUp);
        sign_in_btn = findViewById(R.id.btnSignIn);
        mEmail = findViewById(R.id.etEmail);
        mPassword = findViewById(R.id.etPassword);

        fAuth = FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Required");
                    return;
                }
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mType == null) return;
                else if(mType == UserType.CLIENT){
                    Intent i = new Intent(LoginActivity.this, RegisterClient.class);
                    startActivity(i);
                }
                else if(mType == UserType.BUSINESS_OWNER){
                    Intent i = new Intent(LoginActivity.this, RegisterBusiness.class);
                    startActivity(i);
                }
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        if (checked) {
            switch (view.getId()) {

                case R.id.type_client:
                    mType = UserType.CLIENT;
//                    sign_up_btn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent i = new Intent(LoginActivity.this, RegisterClient.class);
//                            startActivity(i);
//                        }
//                    });
                    break;
                case R.id.type_owner:
                    mType = UserType.BUSINESS_OWNER;
//                    sign_up_btn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent i = new Intent(LoginActivity.this, RegisterBusiness.class);
//                            startActivity(i);
//                        }
//                    });
                    break;
            }
        }
        else
        {
            //view - didnt choose a type
        }
    }
}

