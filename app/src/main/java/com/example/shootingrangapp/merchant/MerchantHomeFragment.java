package com.example.shootingrangapp.merchant;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;

import com.example.shootingrangapp.R;
import com.example.shootingrangapp.authentication.LoginActivity;
import com.example.shootingrangapp.user.UserHomeFragmentDirections;
import com.google.firebase.auth.FirebaseAuth;

public class MerchantHomeFragment extends Fragment {

    private static final String TAG = "MerchantHomeFragment";

    Button btnGoToShop, btnAddGuns, btnBookings, btnLogout;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_merchant_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        btnGoToShop = view.findViewById(R.id.btnGoToShop);
        btnAddGuns = view.findViewById(R.id.btnAddGuns);
        btnBookings = view.findViewById(R.id.btnBookings);
        btnLogout = view.findViewById(R.id.btnLogout);

        btnGoToShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MerchantHomeFragment.this).navigate(R.id.profileFragment);
            }
        });
        btnAddGuns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MerchantHomeFragment.this).navigate(R.id.addGunsFragment);
            }
        });
        btnBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MerchantHomeFragment.this).navigate(
                        MerchantHomeFragmentDirections.actionMerchantHomeFragmentToBookingsFragment2(true)
                );
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });
    }
}
