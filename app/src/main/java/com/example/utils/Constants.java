package com.example.utils;

import android.text.TextUtils;
import android.util.Patterns;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Constants {
    public static final String PROFILE_COLLECTION = "profile";
    public static final String GUNS_COLLECTION = "guns";
    public static final String BOOKING_COLLECTION = "booking";
    public static final String GUNS_IMAGE_PATH = "guns";
    public static final String PROFILE_IMAGE_PATH = "profile";
    public static final String AMMUNITION_IMAGE_PATH = "ammunition";
    public static final String MERCHANT_ID = "MERCHANT_ID";
    public static final String DATE_FORMAT = "dd MMM yyyy";
    public static boolean isMerchant = false;

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static StorageReference getProfileRef(String uid, FirebaseStorage storage) {
        return storage.getReference(PROFILE_IMAGE_PATH).child(uid + ".jpg");
    }
}
