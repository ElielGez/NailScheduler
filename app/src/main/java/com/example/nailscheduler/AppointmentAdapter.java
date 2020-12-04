package com.example.nailscheduler;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nailscheduler.enums.AppointmentStatus;
import com.example.nailscheduler.models.Appointment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.collection.LLRBNode;
import java.util.ArrayList;

public class AppointmentAdapter extends ArrayAdapter<Appointment> {
    private Context context;
    private ArrayList<Appointment> appointments ;

    public AppointmentAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull ArrayList<Appointment> appointments) {
        super(context, resource, textViewResourceId, appointments);
        this.context=context;
        this.appointments=appointments;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.appointment_item, parent, false);
        }
        Appointment currentAppointment = getItem(position);
        if(currentAppointment!=null) {
            TextView clientNameTextView = (TextView) listItemView.findViewById(R.id.client_name);
            clientNameTextView.setText(currentAppointment.getClientName());

            TextView appointmentDateTextView = (TextView) listItemView.findViewById(R.id.appointment_date);
            appointmentDateTextView.setText(currentAppointment.getDate());

            TextView appointmentStartTimeTextView = (TextView) listItemView.findViewById(R.id.appointment_start_time);
            appointmentStartTimeTextView.setText(String.valueOf(currentAppointment.getStartTime()));


            TextView appointmentEndTimeTextView = (TextView) listItemView.findViewById(R.id.appointment_end_time);
            appointmentEndTimeTextView.setText(String.valueOf(currentAppointment.getEndTime()));

            Button appointmentStatus = (Button) listItemView.findViewById(R.id.appointment_status);
            AppointmentStatus status = currentAppointment.getStatus();
            switch (status) {

                case NEW_REQUEST: //0
                    appointmentStatus.setText("בקשה לתור");
                    appointmentStatus.setBackgroundColor(Color.WHITE);
                    break;
                case APPROVED: //1
                    appointmentStatus.setText("התור אושר");
                    break;
                case CANCELED: //2
                    appointmentStatus.setText("התור בוטל");
                    break;

                default:
                    break;

            }
            appointmentStatus.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    if (appointmentStatus.getText().equals("בקשה לתור")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("האם לאשר את התור ?");
                        builder.setTitle("אישור בקשה לתור");
                        builder.setPositiveButton("אשר", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                appointmentStatus.setText("התור אושר");
                                appointmentStatus.setBackgroundColor(Color.TRANSPARENT);
                                Appointment approved_appointment = (Appointment) appointments.get(position);
                                approved_appointment.setStatus(AppointmentStatus.APPROVED);
                                ((BoManageAppointments) context).mRef.child(approved_appointment.getKey()).child("status").setValue(AppointmentStatus.APPROVED).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            String ap_date = approved_appointment.getDate();
                                            String[] s = ap_date.split("/");
                                            ((BoManageAppointments) context).mRef.getRoot().child("Approved_Appointments").child(s[0]).child(s[1]).child(s[2]).child(String.valueOf(approved_appointment.getStartTime())).child(String.valueOf(approved_appointment.getEndTime())).child(approved_appointment.getBoID()).child(approved_appointment.getClientID()).setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast t = Toast.makeText(context, "התור אושר בהצלחה! ", Toast.LENGTH_SHORT);
                                                        t.setGravity(Gravity.CENTER_VERTICAL, 0, 700);
                                                        t.show();

                                                    } else {
                                                        Toast.makeText(context, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(context, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }
                        });
                        builder.setNegativeButton("בטל", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.TRANSPARENT);
                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.TRANSPARENT);
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                            }
                        });
                        alertDialog.show();
                    }
                }

            });
        }
        return listItemView;
    }


}
