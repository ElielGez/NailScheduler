package com.example.nailscheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nailscheduler.models.Client;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterClient extends AppCompatActivity {

    private Button sign_up_btn;
    private EditText full_name;
    private EditText email;
    private EditText password;
    private EditText phone;

    private FirebaseAuth fAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);
        sign_up_btn = (Button) findViewById(R.id.btnRegister);
        full_name = (EditText) findViewById(R.id.etFullName);
        email = (EditText) findViewById(R.id.etEmail);
        password = (EditText) findViewById(R.id.etPassword);
        phone = (EditText) findViewById(R.id.etPhone);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_fname = full_name.getText().toString().trim();
                String user_email = email.getText().toString().trim();
                String user_pwd = password.getText().toString().trim();
                String user_phone = phone.getText().toString().trim();

// this condition checks to make sure the user inputs all fields

                if (user_email.isEmpty()) {
                    email.setError("יש להזין כתובת אימייל");
                    email.requestFocus();
                } else if (user_pwd.isEmpty()) {
                    password.setError("יש להזין סיסמא");
                    password.requestFocus();
                } else if (user_fname.isEmpty()) {
                    full_name.setError("יש להזין שם מלא");
                    full_name.requestFocus();
                } else if (user_phone.isEmpty()) {
                    phone.setError("יש להזין מספר טלפון");
                    phone.requestFocus();
                } else {
                    fAuth.createUserWithEmailAndPassword(user_email, user_pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid = fAuth.getCurrentUser().getUid();
                                Client client = new Client(user_email, user_fname, user_phone);
                                mDatabase.child("Clients").child(uid).setValue(client).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Toast t = Toast.makeText(RegisterClient.this, "ההרשמה התבצעה בהצלחה! ", Toast.LENGTH_SHORT);
                                            t.setGravity(Gravity.CENTER_VERTICAL, 0, 500);
                                            t.show();
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        } else {
                                            Toast.makeText(RegisterClient.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(RegisterClient.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }
}
