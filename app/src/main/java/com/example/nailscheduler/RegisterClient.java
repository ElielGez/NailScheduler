package com.example.nailscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterClient extends AppCompatActivity {

    private Button sign_up_btn;
    private EditText full_name;
    private EditText email;
    private EditText password;
    private EditText phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);
        sign_up_btn = (Button) findViewById(R.id.btnRegister);
        full_name= (EditText) findViewById(R.id.etFullName);
        email=(EditText)findViewById(R.id.etEmail);
        password=(EditText)findViewById(R.id.etPassword);
        phone=(EditText)findViewById(R.id.etPhone);

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

                }
                else {
                    Toast t = Toast.makeText(RegisterClient.this, "ההרשמה התבצעה בהצלחה! ", Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.CENTER_VERTICAL, 0, 500);
                    t.show();

                }
            }
        });
        }
    }
