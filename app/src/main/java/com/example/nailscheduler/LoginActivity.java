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
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.example.nailscheduler.enums.UserType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private Button sign_up_btn, sign_in_btn;
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
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("יש להזין כתובת אימייל");
                    mEmail.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("יש להזין סיסמא");
                    mPassword.requestFocus();
                    return;
                }       
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            DatabaseReference userRef;
                            FirebaseUser user = fAuth.getCurrentUser();
                            String Cid = user.getUid();
                            userRef = FirebaseDatabase.getInstance().getReference("BusinessOwners").child(Cid);
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()) {
                                        startActivity(new Intent(getApplicationContext(), ProfileBusiness.class));
                                    } else  {
                                        startActivity(new Intent(getApplicationContext(), ProfileClient.class));
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mType == null) Toast.makeText(LoginActivity.this, "יש לבחור סוג משתמש", Toast.LENGTH_LONG).show();
                else if (mType == UserType.CLIENT) {
                    Intent i = new Intent(LoginActivity.this, RegisterClient.class);
                    startActivity(i);
                } else if (mType == UserType.BUSINESS_OWNER) {
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
                    break;
                case R.id.type_owner:
                    mType = UserType.BUSINESS_OWNER;
                    break;
            }
        }
    }
}

