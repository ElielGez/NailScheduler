package com.example.nailscheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

public class ClientManageApts extends AppCompatActivity {

    public FirebaseAuth fAuth;
    public DatabaseReference mRef;
    public ArrayList<Appointment> appointmentsL;
    public ListView listView;
    public ClientAppointmentAdapter clAppointmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_manage_apts);

        listView = (ListView) findViewById(R.id.lv_apt);
        appointmentsL = new ArrayList<>();
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser clCurrentUser = fAuth.getCurrentUser();

        if (clCurrentUser != null) { // User logged in
            //POPUP - Appointment Cancellation Explanation
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(" ");
            builder.setMessage("ניתן לבטל תור בעל סטטוס ״התור מאושר״ עד 24 שעות לפני מועד התור");
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setBackgroundColor(Color.TRANSPARENT);
                    alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.BLACK);
                }
            });
            alertDialog.show();

            String clCurrentID = clCurrentUser.getUid();
            mRef = FirebaseDatabase.getInstance().getReference().getRoot().child("Appointments");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    appointmentsL.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Appointment appointment = data.getValue(Appointment.class);
                        if (appointment.getClientID().equals(clCurrentID)) {
                            appointmentsL.add(appointment);
                        }
                    }
                    clAppointmentAdapter = new ClientAppointmentAdapter(ClientManageApts.this, 0, 0, appointmentsL);
                    listView.setAdapter(clAppointmentAdapter);
                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                           String currentBO = appointmentsL.get(position).getBoID();
                           String aptKey = appointmentsL.get(position).getKey();
                            Intent i = new Intent(ClientManageApts.this, ClientAppointmentDetails.class);
                            i.putExtra("businessOwner", currentBO);
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
    }
}