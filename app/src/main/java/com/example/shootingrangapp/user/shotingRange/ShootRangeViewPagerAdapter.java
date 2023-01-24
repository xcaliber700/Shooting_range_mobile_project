package com.example.shootingrangapp.user.shotingRange;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.utils.Constants;

public class ShootRangeViewPagerAdapter extends FragmentStateAdapter {
    String merchantId;

    public ShootRangeViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String merchantId) {
        super(fragmentManager, lifecycle);
        this.merchantId = merchantId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        Bundle bundle = new Bundle();
        bundle.putString(Constants.MERCHANT_ID, merchantId);
        if (position == 0) {
            fragment = new GunsFragment();
        } else if (position == 1) {
            fragment = new AmmunitionFragment();
        } else
            fragment = new ReviewsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
