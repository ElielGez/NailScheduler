package com.example.nailscheduler;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nailscheduler.models.CityJSON;
import com.example.nailscheduler.services.CitiesAdapter;
import com.example.nailscheduler.services.FirebaseStorageManage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ClientAppointmentDetails extends AppCompatActivity {

    String currentBO;
    String aptKey;
    public DatabaseReference mRefApt, mRefBo ;
    String aptDate, aptStart, aptEnd, boName, boNumber, boCity, boStreet, boNumAd, nailImage;
    private TextView dateTxtView, timeTxtView, boNameTxtView, boPhoneTxtView, boAddressTxtView;
    private ImageView nailExImgView;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_appointment_details);
        dateTxtView = findViewById(R.id.date);
        timeTxtView = findViewById(R.id.time);
        boNameTxtView = findViewById(R.id.businessName);
        boPhoneTxtView = findViewById(R.id.businessPhoneNumber);
        boAddressTxtView = findViewById(R.id.businessAddress);
        nailExImgView = findViewById(R.id.nailExample);

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
                Task<Uri> downloadUri = FirebaseStorageManage.getAppointmentImage(aptKey, FirebaseStorageManage.APT_IMAGE_EXAMPLE);
                if(downloadUri.isComplete()){
                    nailImage = downloadUri.getResult().toString();
                    Picasso.get().load(nailImage).into(nailExImgView);
                }
                dateTxtView.setText(aptDate);
                timeTxtView.setText(aptStart + ":00" + " - " + aptEnd + ":00");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseStorageManage.getAppointmentImage(aptKey, FirebaseStorageManage.APT_IMAGE_EXAMPLE).
                addOnCompleteListener(new OnCompleteListener<Uri>(){
                    @Override
                    public void onComplete(Task<Uri> task) {
                        if(task.isSuccessful()) {
                            Uri uri = task.getResult();
                            Picasso.get().load(uri).into(nailExImgView);
                        }
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
