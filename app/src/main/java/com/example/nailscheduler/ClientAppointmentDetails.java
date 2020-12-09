package com.example.nailscheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nailscheduler.models.Appointment;
import com.example.nailscheduler.models.CityJSON;
import com.example.nailscheduler.services.CitiesAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClientAppointmentDetails extends AppCompatActivity {

    String currentBO;
    String aptKey;
    public DatabaseReference mRefApt, mRefBo ;
    String aptDate, aptStart, aptEnd, boName, boNumber, boCity, boStreet, boNumAd;
    private TextView dateTxtView, timeTxtView, boNameTxtView, boPhoneTxtView, boAddressTxtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_appointment_details);
        ArrayList<CityJSON> cities = CitiesAdapter.readJsonCities(this);
        dateTxtView = findViewById(R.id.date);
        timeTxtView = findViewById(R.id.time);
        boNameTxtView = findViewById(R.id.businessName);
        boPhoneTxtView = findViewById(R.id.businessPhoneNumber);
        boAddressTxtView = findViewById(R.id.businessAddress);

        Intent intent = getIntent();
        currentBO = intent.getExtras().getString("businessOwner");
        aptKey = intent.getExtras().getString("appointmentKey");
        mRefApt = FirebaseDatabase.getInstance().getReference().getRoot().child("Appointments").child(aptKey);
        mRefApt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                aptDate = dataSnapshot.child("date").getValue().toString();
                aptStart = dataSnapshot.child("startTime").getValue().toString();
                aptEnd = dataSnapshot.child("endTime").getValue().toString();

                dateTxtView.setText(aptDate);
                timeTxtView.setText(aptStart + ":00" + " - " + aptEnd + ":00");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

            mRefBo = FirebaseDatabase.getInstance().getReference().getRoot().child("BusinessOwners").child(currentBO);
            mRefBo.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boName = snapshot.child("businessName").getValue().toString();
                    boNumber = snapshot.child("phoneNumber").getValue().toString();
                    boCity = snapshot.child("boAddress").child("cityName").getValue().toString();
                    boStreet = snapshot.child("boAddress").child("street").getValue().toString();
                    boNumAd = snapshot.child("boAddress").child("number").getValue().toString();

                    boNameTxtView.setText(boName);
                    boPhoneTxtView.setText(boNumber);
                    boAddressTxtView.setText(boStreet+ " " + boNumAd + ", " +boCity);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
}
