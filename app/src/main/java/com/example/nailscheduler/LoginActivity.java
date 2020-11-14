package com.example.nailscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class LoginActivity extends AppCompatActivity {

    private RadioGroup userTypeGroup;
    private RadioButton userType;
    private Button sign_up_btn;
    private Button sign_in_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sign_up_btn = (Button) findViewById(R.id.btnSignUp);
        sign_in_btn = (Button) findViewById(R.id.btnSignIn);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        if (checked) {
            switch (view.getId()) {

                case R.id.type_client:
                    sign_up_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(LoginActivity.this, RegisterClient.class);
                            startActivity(i);
                        }
                    });
                    sign_in_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    break;
                case R.id.type_owner:
                    sign_up_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(LoginActivity.this, RegisterBusiness.class);
                            startActivity(i);
                        }
                    });
                    sign_in_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    break;
            }
        }
        else
        {
            //view - didnt choose a type
        }
    }
}

