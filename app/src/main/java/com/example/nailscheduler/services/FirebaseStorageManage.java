package com.example.nailscheduler.services;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseStorageManage {
    private static StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private static String IMAGES_PATH = "images/";
    private static String APPOINTMENTS_PATH = "appointments/";
    private static String USERS_PATH = "users/";
    public static String APT_IMAGE_EXAMPLE = "exampleNailImage.png";
    public static String USER_IMAGE_PROFILE = "userProfileImage.png";

    private static UploadTask uploadFile(String path, Uri uri) {
        StorageReference ref = mStorageRef.child(path);
        return ref.putFile(uri);
    }
    private static Task<Uri> getFile(String path) {
        StorageReference ref = mStorageRef.child(path);
        return ref.getDownloadUrl();
    }

    public static UploadTask uploadAppointmentImage(String aptId, String fileName, Uri uri) {
        return uploadFile(APPOINTMENTS_PATH + aptId + "/" + IMAGES_PATH + fileName, uri);
    }

    public static UploadTask uploadUserImage(String userId, String fileName, Uri uri) {
        return uploadFile(USERS_PATH + userId + "/" + IMAGES_PATH + fileName, uri);
    }

    public static Task<Uri> getAppointmentImage(String aptId,String fileName){
        return getFile(APPOINTMENTS_PATH + aptId + "/" + IMAGES_PATH + fileName);
    }

    public static Task<Uri> getUserImage(String userId,String fileName){
        return getFile(USERS_PATH + userId + "/" + IMAGES_PATH + fileName);
    }

}
