package com.example.nailscheduler;

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

public class ProfileBusiness extends AppCompatActivity {

    private CircleImageView ProfileImage;
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    FirebaseAuth fAuth;
    private TextView nameTxtView, emailTxtView, phoneTxtView;
    private DatabaseReference userRef;
    private String email,fname,phone;
    private Button manage_apt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_business);

        nameTxtView = findViewById(R.id.tv_name);
        phoneTxtView = findViewById(R.id.tv_phone);
        emailTxtView = findViewById(R.id.tv_email);
        manage_apt =findViewById(R.id.manage_appointments);
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser CurrentUser = fAuth.getCurrentUser();
        String CUid = CurrentUser.getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("BusinessOwners").child(CUid);

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
                Toast.makeText(ProfileBusiness.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        manage_apt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileBusiness.this, BoManageAppointments.class);
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
}