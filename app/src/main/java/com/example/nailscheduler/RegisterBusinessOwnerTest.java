package com.example.nailscheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nailscheduler.models.BusinessOwner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterBusinessOwnerTest extends AppCompatActivity {
    String mType;
    EditText mEmail, mPassword, mFullName,mBusinessName,mPhoneNumber;
    Button mRegisterButton;
    FirebaseAuth fAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_business_owner_test);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mFullName = findViewById(R.id.fullName);
        mBusinessName = findViewById(R.id.businessName);
        mPhoneNumber = findViewById(R.id.phoneNumber);
        mRegisterButton = findViewById(R.id.registerBtn);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String fullName = mFullName.getText().toString();
                String businessName = mBusinessName.getText().toString();
                String phoneNumber = mPhoneNumber.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Required");
                    return;
                }
                if (TextUtils.isEmpty(fullName)) {
                    mPassword.setError("Required");
                    return;
                }
                if (TextUtils.isEmpty(businessName)) {
                    mBusinessName.setError("Required");
                    return;
                }
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = fAuth.getCurrentUser().getUid();
                            BusinessOwner bo = new BusinessOwner(email, fullName);
                            bo.setBusinessName(businessName);
                            bo.setPhoneNumber(phoneNumber);
                            mDatabase.child("BusinessOwners").child(uid).setValue(bo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    } else {
                                        Toast.makeText(RegisterBusinessOwnerTest.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterBusinessOwnerTest.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
}