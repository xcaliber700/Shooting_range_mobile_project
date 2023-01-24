package com.example.shootingrangapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shootingrangapp.authentication.LoginActivity;
import com.example.shootingrangapp.merchant.MerchantHomeActivity;
import com.example.shootingrangapp.model.ProfileModel;
import com.example.shootingrangapp.user.UserHomeActivity;
import com.example.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        if (mAuth.getCurrentUser() != null)
            checkUserType(mAuth.getCurrentUser().getUid());
        else
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    void checkUserType(String userId) {
        db.collection(Constants.PROFILE_COLLECTION).document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ProfileModel profileModel = task.getResult().toObject(ProfileModel.class);
                        redirectUser(profileModel.isMerchant());
                    } else {
                        Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void redirectUser(boolean merchant) {
        Constants.isMerchant = merchant;
        if (merchant) {
            Intent intent = new Intent(this, MerchantHomeActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, UserHomeActivity.class);
            startActivity(intent);
        }
        finish();
    }

}