package com.example.shootingrangapp.merchant;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.shootingrangapp.R;

public class MerchantHomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    NavController navController;
    NavHostFragment navHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_home);

        toolbar = findViewById(R.id.toolbar);
        navHostFragment = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment));
        navController = navHostFragment.getNavController();
        setSupportActionBar(toolbar);
        NavigationUI.setupActionBarWithNavController(this, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (navHostFragment.getChildFragmentManager().getFragments() != null && navHostFragment.getChildFragmentManager().getFragments().size() > 0)
            for (int i = 0; i < navHostFragment.getChildFragmentManager().getFragments().size(); i++) {
                Fragment fragment = navHostFragment.getChildFragmentManager().getFragments().get(i);
                fragment.onActivityResult(requestCode, resultCode, data);
            }
    }
}