package com.example.nailscheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nailscheduler.services.FirebaseStorageManage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BoAppointmentDetails extends AppCompatActivity {

    String currentCL;
    String aptKey;
    public DatabaseReference mRefApt, mRefBo ;
    String aptDate, aptStart, aptEnd, clName, clNumber, nailImage;
    private TextView dateTxtView, timeTxtView, clNameTxtView, clPhoneTxtView;
    private ImageView nailExImgView;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bo_appointment_details);
        dateTxtView = findViewById(R.id.date);
        timeTxtView = findViewById(R.id.time);
        clNameTxtView = findViewById(R.id.clientName);
        clPhoneTxtView = findViewById(R.id.clientPhoneNumber);
        nailExImgView = findViewById(R.id.nailExmpl);

        Intent intent = getIntent();
        aptKey = intent.getExtras().getString("appointmentKey");

        mRefApt = FirebaseDatabase.getInstance().getReference().getRoot().child("Appointments").child(aptKey);
        mRefApt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentCL = dataSnapshot.child("clientID").getValue().toString();
                aptDate = dataSnapshot.child("date").getValue().toString();
                aptStart = dataSnapshot.child("startTime").getValue().toString();
                aptEnd = dataSnapshot.child("endTime").getValue().toString();
                Task<Uri> downloadUri = FirebaseStorageManage.getAppointmentImage(aptKey, FirebaseStorageManage.APT_IMAGE_EXAMPLE);
                if (downloadUri.isComplete()) {
                    nailImage = downloadUri.getResult().toString();
                    Picasso.get().load(nailImage).into(nailExImgView);
                }
                dateTxtView.setText(aptDate);
                timeTxtView.setText(aptStart + ":00" + " - " + aptEnd + ":00");
                second();
            }

            private void second() {
                mRefBo = FirebaseDatabase.getInstance().getReference().getRoot().child("Clients").child(currentCL);
                mRefBo.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        clName = snapshot.child("fullName").getValue().toString();
                        clNumber = snapshot.child("phoneNumber").getValue().toString();

                        clNameTxtView.setText(clName);
                        clPhoneTxtView.setText(clNumber);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
    }

}