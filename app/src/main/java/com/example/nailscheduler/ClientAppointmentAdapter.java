package com.example.nailscheduler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nailscheduler.enums.AppointmentStatus;
import com.example.nailscheduler.models.Appointment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class ClientAppointmentAdapter extends ArrayAdapter<Appointment> {

    private Context context;
    private ArrayList<Appointment> appointments ;

    public ClientAppointmentAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull ArrayList<Appointment> appointments) {
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
                    R.layout.client_apt_item, parent, false);
        }

        Appointment currentAppointment = getItem(position);
        if(currentAppointment!=null) {
            TextView boNameTextView = (TextView) listItemView.findViewById(R.id.bo_name);
            boNameTextView.setText(currentAppointment.getBoName());

            TextView appointmentDateTextView = (TextView) listItemView.findViewById(R.id.apt_date);
            appointmentDateTextView.setText(currentAppointment.getDate());

            TextView appointmentStartTimeTextView = (TextView) listItemView.findViewById(R.id.apt_start_time);
            appointmentStartTimeTextView.setText(String.valueOf(currentAppointment.getStartTime()));


            TextView appointmentEndTimeTextView = (TextView) listItemView.findViewById(R.id.apt_end_time);
            appointmentEndTimeTextView.setText(String.valueOf(currentAppointment.getEndTime()));

            Button appointmentStatus = (Button) listItemView.findViewById(R.id.apt_status);
            AppointmentStatus status = currentAppointment.getStatus();
            switch (status){

                case NEW_REQUEST: //0
                    appointmentStatus.setText("מחכה לאישור");
                    appointmentStatus.setBackgroundColor(Color.WHITE);
                    break;
                case APPROVED: //1
                    appointmentStatus.setText("התור מאושר");
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
                    if (appointmentStatus.getText().equals("התור מאושר")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("האם ברצונך לבטל את התור?");
                        builder.setTitle("ביטול תור");
                        builder.setPositiveButton("כן", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                appointmentStatus.setText("התור בוטל");
                                Appointment cancelled_appointment = (Appointment) appointments.get(position);
                                cancelled_appointment.setStatus(AppointmentStatus.CANCELED);
                                ((ClientManageApts) context).mRef.child(cancelled_appointment.getKey()).child("status").setValue(AppointmentStatus.CANCELED).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            String ap_date = cancelled_appointment.getDate();
                                            String[] s = ap_date.split("/");
                                            ((ClientManageApts) context).mRef.getRoot().child("Approved_Appointments").child(s[0]).child(s[1]).child(s[2]).child(String.valueOf(cancelled_appointment.getStartTime())).child(String.valueOf(cancelled_appointment.getEndTime())).child(cancelled_appointment.getBoID()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast t = Toast.makeText(context, "התור בוטל בהצלחה! ", Toast.LENGTH_SHORT);
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
                        builder.setNegativeButton("לא", new DialogInterface.OnClickListener() {
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
