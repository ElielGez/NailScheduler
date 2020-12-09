package com.example.nailscheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nailscheduler.models.BoAddress;
import com.example.nailscheduler.models.BusinessOwner;
import com.example.nailscheduler.models.CityJSON;
import com.example.nailscheduler.services.CitiesAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.transform.Source;

public class RegisterBusiness extends AppCompatActivity {
    private Button sign_up_btn;
    private EditText full_name;
    private EditText business_name;
    private Spinner city;
    private EditText street;
    private EditText number;
    private EditText email;
    private EditText password;
    private EditText phone;

    private FirebaseAuth fAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_business);
        ArrayList<CityJSON> cities = CitiesAdapter.readJsonCities(this);
        sign_up_btn = findViewById(R.id.btnRegister);
        full_name = findViewById(R.id.etFullName);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        phone = findViewById(R.id.etBPhone);
        business_name = findViewById(R.id.etBname);
        city = findViewById(R.id.addrCity);
        city.setAdapter(new ArrayAdapter<CityJSON>(this, android.R.layout.simple_spinner_dropdown_item,cities));
        street = findViewById(R.id.addrStreet);
        number = findViewById(R.id.addrNumber);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), ProfileBusiness.class));
            finish();
        }
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String b_user_fname = full_name.getText().toString().trim();
                String b_user_email = email.getText().toString().trim();
                String b_user_pwd = password.getText().toString().trim();
                String b_name = business_name.getText().toString().trim();
                String b_phone = phone.getText().toString().trim();
                String b_addrcity = "" + ((CityJSON)city.getSelectedItem()).getKey();
                String b_addrcityName = ((CityJSON)city.getSelectedItem()).getValue();
                String b_addrstreet = street.getText().toString().trim();
                String b_addrnumber = number.getText().toString().trim();

// this condition checks to make sure the user inputs all fields

                if (b_user_email.isEmpty()) {
                    email.setError("יש להזין כתובת אימייל");
                    email.requestFocus();
                } else if (b_user_pwd.isEmpty()) {
                    password.setError("יש להזין סיסמא");
                    password.requestFocus();
                } else if (b_user_fname.isEmpty()) {
                    full_name.setError("יש להזין שם מלא");
                    full_name.requestFocus();
                } else if (b_name.isEmpty()) {
                    business_name.setError("יש להזין שם בית העסק");
                    business_name.requestFocus();
                } else if (b_addrcity.isEmpty()) {
                    city.requestFocus();
                } else if (b_addrstreet.isEmpty()) {
                    street.setError("יש להזין רחוב");
                    street.requestFocus();
                } else if (b_addrnumber.isEmpty()) {
                    number.setError("יש להזין מספר");
                    number.requestFocus();
                } else if (b_phone.isEmpty()) {
                    phone.setError("יש להזין מספר טלפון");
                    phone.requestFocus();
                } else {
                    fAuth.createUserWithEmailAndPassword(b_user_email, b_user_pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid = fAuth.getCurrentUser().getUid();
                                BusinessOwner bo = new BusinessOwner(b_user_email, b_user_fname, b_phone);
                                bo.setBusinessName(b_name);
                                BoAddress boa = new BoAddress(b_addrcity, b_addrcityName, b_addrstreet, b_addrnumber);
                                bo.setBoAddress(boa);
                                mDatabase.child("BusinessOwners").child(uid).setValue(bo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            mDatabase.child("BoAddresses").child(boa.getCity()).child(uid).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast t = Toast.makeText(RegisterBusiness.this, "ההרשמה התבצעה בהצלחה! ", Toast.LENGTH_SHORT);
                                                        t.setGravity(Gravity.CENTER_VERTICAL, 0, 700);
                                                        t.show();
                                                        startActivity(new Intent(getApplicationContext(), ProfileBusiness.class));
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(RegisterBusiness.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(RegisterBusiness.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
