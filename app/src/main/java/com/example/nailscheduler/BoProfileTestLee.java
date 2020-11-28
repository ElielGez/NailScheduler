package com.example.nailscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BoProfileTestLee extends AppCompatActivity {

    private Button manage_appointments;
    private String boID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bo_profile_test_lee);
        manage_appointments=findViewById(R.id.manage_appointments);


        manage_appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BoProfileTestLee.this, BoManageAppointments.class);
                startActivity(i);
            }
        });
    }


}