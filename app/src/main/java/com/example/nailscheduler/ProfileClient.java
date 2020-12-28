package com.example.nailscheduler;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nailscheduler.services.FirebaseStorageManage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import android.net.Uri;

public class ProfileClient extends AppCompatActivity {

    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int CAMERA_PERM_CODE = 101;
    private CircleImageView profileImage;
    private Uri imageUri;
    FirebaseAuth fAuth;
    private TextView nameTxtView, emailTxtView, phoneTxtView;
    private DatabaseReference userRef;
    private String email,fname,phone, cUid;
    private Button manageAppointment, scheduleAppointment;
    private String currentPhotoPath, downloadedUriUrl;

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
        profileImage =  (CircleImageView) findViewById(R.id.profile_image);

        fAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = fAuth.getCurrentUser();
        cUid = currentUser.getUid();
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

        FirebaseStorageManage.getUserImage(cUid, FirebaseStorageManage.USER_IMAGE_PROFILE).
                addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri uri = task.getResult();
                            Picasso.get().load(uri).into(profileImage);
                        }
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

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermission();
            }
        });
        createNotificationChannel();
    }

    private void askCameraPermission() {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }
        else{
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }else{
                Toast.makeText(this, "Camera Permission is Required to use camera", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                File f = (new File(currentPhotoPath));
                imageUri= Uri.fromFile(f);
                profileImage.setImageURI(imageUri);
                if (imageUri != null) {
                    FirebaseStorageManage.uploadUserImage(cUid, FirebaseStorageManage.USER_IMAGE_PROFILE, imageUri);
                }
            }
            else{
                Toast.makeText(this,"לא צולם תמונה", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = FirebaseStorageManage.USER_IMAGE_PROFILE;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
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