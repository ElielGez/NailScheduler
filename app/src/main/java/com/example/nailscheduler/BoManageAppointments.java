package com.example.nailscheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.example.nailscheduler.models.Appointment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BoManageAppointments extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private DatabaseReference mRef;
    private ArrayList<Appointment> appointmentsL;
    ListView listView;
    AppointmentAdapter appointmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bo_manage_appointments);
        listView= (ListView) findViewById(R.id.appointment_list_view);

        fAuth = FirebaseAuth.getInstance();
        FirebaseUser BoCurrentUser = fAuth.getCurrentUser();
        if (fAuth.getCurrentUser() != null) {
            FirebaseUser BoUser = fAuth.getCurrentUser();

            if (BoCurrentUser != null) { // User logged in
                String BoCurrentID = BoCurrentUser.getUid();
                mRef = FirebaseDatabase.getInstance().getReference("Appointments");
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Appointment appointment = data.getValue(Appointment.class);
                            if (appointment.getBoID().equals(BoCurrentID)) {
                                appointmentsL.add(appointment);
                            }
                        }
                        appointmentAdapter = new AppointmentAdapter(BoManageAppointments.this, 0, 0, appointmentsL);
                        listView.setAdapter(appointmentAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
        // listView.setAdapter(adapter);
        else {
            // No user is signed in
            finish();
        }

    }
}