package com.example.nailscheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nailscheduler.enums.AppointmentStatus;
import com.example.nailscheduler.models.Appointment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BoManageAppointments extends AppCompatActivity {

    public FirebaseAuth fAuth;
    public DatabaseReference mRef;
    public ArrayList<Appointment> appointmentsL;
    public ListView listView;
    public AppointmentAdapter appointmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bo_manage_appointments);
        listView = (ListView) findViewById(R.id.appointment_list_view);
        appointmentsL = new ArrayList<>();
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser BoCurrentUser = fAuth.getCurrentUser();

            if (BoCurrentUser != null) { // User logged in
                String BoCurrentID = BoCurrentUser.getUid();
                mRef = FirebaseDatabase.getInstance().getReference().getRoot().child("Appointments");
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        appointmentsL.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Appointment appointment = data.getValue(Appointment.class);
                            if (appointment.getBoID().equals(BoCurrentID)) {
                                appointmentsL.add(appointment);
                            }
                        }
                        appointmentAdapter = new AppointmentAdapter(BoManageAppointments.this, 0, 0, appointmentsL);
                        listView.setAdapter(appointmentAdapter);
                        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                                String aptKey = appointmentsL.get(position).getKey();
                                Intent i = new Intent(BoManageAppointments.this, BoAppointmentDetails.class);
                                i.putExtra("appointmentKey", aptKey);
                                startActivity(i);
                                return true;

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

            }
            //else {
//                // No user is signed in
//                finish();
//            }

        }
    }

