package com.example.nailscheduler;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nailscheduler.enums.AppointmentStatus;
import com.example.nailscheduler.models.Appointment;

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
            appointmentStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        return listItemView;
        }
}
