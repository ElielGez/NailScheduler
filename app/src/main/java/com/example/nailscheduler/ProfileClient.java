package com.example.nailscheduler;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import android.provider.MediaStore;
import android.widget.Toast;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import android.net.Uri;

public class ProfileClient extends AppCompatActivity {

    private CircleImageView ProfileImage;
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    FirebaseAuth fAuth;
    private TextView nameTxtView, emailTxtView, phoneTxtView;
    private DatabaseReference userRef;
    private String email,fname,phone;
    private Button manageAppointment;
    private Button scheduleAppointment;
    public static final String NOTIFICATION_CHANNEL_ID = "CHANNEL" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_client);

        nameTxtView = findViewById(R.id.tv_name);
        phoneTxtView = findViewById(R.id.tv_phone);
        emailTxtView = findViewById(R.id.tv_email);
        manageAppointment = findViewById(R.id.btn_apt_hist);
        scheduleAppointment = findViewById(R.id.btn_sched_apt);
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = fAuth.getCurrentUser();
        String cUid = currentUser.getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Clients").child(cUid);
        // Read from the database
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                fname = dataSnapshot.child("fullName").getValue().toString();
                email = dataSnapshot.child("email").getValue().toString();
                phone = dataSnapshot.child("phoneNumber").getValue().toString();

                nameTxtView.setText(fname);
                emailTxtView.setText(email);
                phoneTxtView.setText(phone);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(ProfileClient.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        manageAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileClient.this, ClientManageApts.class);
                startActivity(i);
            }
        });
        scheduleAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileClient.this, NewAppointment.class);
                startActivity(i);
            }
        });
        ProfileImage = (CircleImageView) findViewById(R.id.profile_image);
        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
            }
        });
        createNotificationChannel();
    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ProfileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void logout(View view) {
        fAuth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

        private void createNotificationChannel()
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "NailScheduler - Channel";
            String description = "Notification channel for NailScheduler";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager nm= getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
        }
    }
}