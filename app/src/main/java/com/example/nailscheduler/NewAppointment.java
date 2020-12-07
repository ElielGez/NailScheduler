package com.example.nailscheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.nailscheduler.enums.AppointmentStatus;
import com.example.nailscheduler.models.Appointment;
import com.example.nailscheduler.models.BusinessOwner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nailscheduler.models.CityJSON;
import com.example.nailscheduler.services.CitiesAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class NewAppointment extends AppCompatActivity {
    final Calendar now = Calendar.getInstance();
    final int MAX_SELECTABLE_DATE_IN_FUTURE = 365; // 365 days into the future max
    final int MIN_START_TIME = 9;
    final int MAX_START_TIME = 17;
    private AutoCompleteTextView city;
    private int selectedCityKey;
    private EditText date;
    private EditText startTime;
    private EditText endTime;
    private Button searchBtn;
    private ProgressBar searchBtnPB;
    private DatabaseReference mDatabase;
    FirebaseAuth fAuth;
    private LinearLayout selectBoLayout;
    private Spinner selectBo;
    private LinearLayout noMatchFoundLayout;
    private Button createAppointment;
    private String clientName;
    private Appointment selectedAp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        ArrayList<CityJSON> cities = CitiesAdapter.readJsonCities(this);
        city = findViewById(R.id.city);
        date = findViewById(R.id.date);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        searchBtn = findViewById(R.id.searchAppointment);
        searchBtnPB = findViewById(R.id.searchBtnPB);
        selectBoLayout = findViewById(R.id.selectBoLayout);
        selectBo = findViewById(R.id.selectBo);
        noMatchFoundLayout = findViewById(R.id.noMatchFoundLayout);
        createAppointment = findViewById(R.id.createAppointment);

        mDatabase.child("Clients").child(fAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                clientName = dataSnapshot.child("fullName").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        startTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int st = Integer.parseInt(s.toString());
                    if (st < MIN_START_TIME || st > MAX_START_TIME) {
                        startTime.setError("עליך לבחור שעה בין " + MIN_START_TIME + " לבין" + MAX_START_TIME);
                        startTime.requestFocus();
                    } else {
                        endTime.setText("" + (st + 1));
                    }
                } catch (Exception ex) {

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });
        city.setAdapter(new ArrayAdapter<CityJSON>(this, android.R.layout.simple_list_item_1, cities));
        city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CityJSON selectedItem = (CityJSON) parent.getItemAtPosition(position);
                selectedCityKey = selectedItem.getKey();
            }
        });

        DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                now.set(Calendar.YEAR, year);
                now.set(Calendar.MONTH, monthOfYear);
                now.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                date.setText(sdf.format(now.getTime()));
            }

        };

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog dpd = DatePickerDialog.newInstance(datePicker, now
                                .get(Calendar.YEAR), now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                ArrayList<Calendar> weekdays = new ArrayList<Calendar>();
                Calendar day = Calendar.getInstance();
                for (int i = 0; i < MAX_SELECTABLE_DATE_IN_FUTURE; i++) {
                    if (day.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && day.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
                        Calendar d = (Calendar) day.clone();
                        weekdays.add(d);
                    }
                    day.add(Calendar.DATE, 1);
                }
                Calendar[] weekdayDays = weekdays.toArray(new Calendar[weekdays.size()]);
                dpd.setSelectableDays(weekdayDays);
                dpd.show(getSupportFragmentManager(), "DatePickerDialog");
            }
        });
        createAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBtnPB.setVisibility(View.VISIBLE);
                String key = mDatabase.push().getKey();
                BusinessOwner bo = (BusinessOwner)selectBo.getSelectedItem();
                selectedAp.setKey(key);
                selectedAp.setBoID(bo.getId());
                selectedAp.setBoName(bo.getBusinessName());
                //TODO: here before save appointments , validate again that the appointment is free to schedule
                mDatabase.child("Appointments").child(key).setValue(selectedAp).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast t = Toast.makeText(NewAppointment.this, "התור הוזמן בהצלחה! ", Toast.LENGTH_SHORT);
                            t.setGravity(Gravity.CENTER_VERTICAL, 0, 700);
                            t.show();
                            finish();
                        } else {
                            Toast.makeText(NewAppointment.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String b_city = "" + selectedCityKey;
                String b_date = date.getText().toString();
                String b_startTime = startTime.getText().toString();
                String b_endTime = endTime.getText().toString();
                if (b_city.isEmpty()) {
                    city.setError("יש להזין עיר");
                    city.requestFocus();
                } else if (b_date.isEmpty()) {
                    date.setError("יש להזין תאריך");
                    date.requestFocus();
                } else if (b_startTime.isEmpty()) {
                    startTime.setError("יש להזין שעת התחלה");
                    startTime.requestFocus();
                } else {
                    searchBtnPB.setVisibility(View.VISIBLE);
                    selectBoLayout.setVisibility(View.GONE);
                    noMatchFoundLayout.setVisibility(View.GONE);
                    String[] dateSplit = b_date.split("/");
                    selectedAp = new Appointment("",fAuth.getUid(),clientName,"","",b_date,Integer.parseInt(b_startTime),Integer.parseInt(b_endTime), AppointmentStatus.NEW_REQUEST);
                    mDatabase.child("BoAddresses").child(b_city).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            GenericTypeIndicator<HashMap<String, Boolean>> t = new GenericTypeIndicator<HashMap<String, Boolean>>() {
                            };
                            HashMap<String, Boolean> bosByAddress = snapshot.getValue(t);
                            mDatabase.child("Approved_Appointments").child(dateSplit[0]).child(dateSplit[1]).child(dateSplit[2]).child(b_startTime).child(b_endTime).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ArrayList<String> freeBos = new ArrayList<>();
                                    GenericTypeIndicator<HashMap<String, Object>> t = new GenericTypeIndicator<HashMap<String, Object>>() {
                                    };
                                    HashMap<String, Object> appBosHp = snapshot.getValue(t);
                                    if (bosByAddress != null) {
                                        for (String key : bosByAddress.keySet()) {
                                            if (appBosHp == null || !appBosHp.containsKey(key)) {
                                                freeBos.add(key);
                                            }
                                        }
                                    } else {
                                        noMatchFoundLayout.setVisibility(View.VISIBLE);
                                    }
                                    if (!freeBos.isEmpty()) {
                                        mDatabase.child("BusinessOwners").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                GenericTypeIndicator<HashMap<String, BusinessOwner>> t = new GenericTypeIndicator<HashMap<String, BusinessOwner>>() {
                                                };
                                                HashMap<String, BusinessOwner> businessOwners = snapshot.getValue(t);
                                                ArrayList<BusinessOwner> bosDetails = new ArrayList<>();
                                                for (String boId : freeBos) {
                                                    BusinessOwner bo = businessOwners.get(boId);
                                                    bo.setId(boId);
                                                    bosDetails.add(bo);
                                                }
                                                if (!bosDetails.isEmpty()) {
                                                    selectBoLayout.setVisibility(View.VISIBLE);
                                                    selectBo.setAdapter(new ArrayAdapter<BusinessOwner>(NewAppointment.this, android.R.layout.simple_spinner_dropdown_item, bosDetails));
                                                } else {
                                                    noMatchFoundLayout.setVisibility(View.VISIBLE);
                                                }
                                                searchBtnPB.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                searchBtnPB.setVisibility(View.GONE);
                                            }
                                        });
                                    } else {
                                        noMatchFoundLayout.setVisibility(View.VISIBLE);
                                    }
                                    searchBtnPB.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    searchBtnPB.setVisibility(View.GONE);
                                }
                            });
                            searchBtnPB.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            searchBtnPB.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }
}