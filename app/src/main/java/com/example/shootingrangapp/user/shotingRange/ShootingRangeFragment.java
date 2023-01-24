package com.example.shootingrangapp.user.shotingRange;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.shootingrangapp.R;
import com.example.shootingrangapp.authentication.LoginActivity;
import com.example.shootingrangapp.model.ShootingRangeModel;
import com.example.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class ShootingRangeFragment extends Fragment {

    private static final String TAG = "UserHomeFragment";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseStorage firebaseStorage;
    ProgressDialog pd;
    ViewPager2 viewPager;
    TabLayout tabLayout;
    ShootRangeViewPagerAdapter shootRangeViewPagerAdapter;
    String[] title = {"Guns", "Ammunition", "Reviews"};
    ShootingRangeModel rangeModel;
    ImageView imgLogo, imgCall;
    TextView txtTitle, txtStoreAddress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shooting_range, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rangeModel = ShootingRangeFragmentArgs.fromBundle(getArguments()).getShootinRangeDetails();
        firebaseStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait...");

        imgLogo = view.findViewById(R.id.imgStore);
        imgCall = view.findViewById(R.id.imgCall);
        txtTitle = view.findViewById(R.id.txtTitle);
        txtStoreAddress = view.findViewById(R.id.txtAddress);


        Glide.with(getContext())
                .load(Constants.getProfileRef(rangeModel.getStoreId(),firebaseStorage))
                .into(imgLogo);
        txtTitle.setText(rangeModel.getName());
        txtStoreAddress.setText(rangeModel.getAddress());


        shootRangeViewPagerAdapter = new ShootRangeViewPagerAdapter(getChildFragmentManager(), getViewLifecycleOwner().getLifecycle(), rangeModel.getStoreId());
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager.setAdapter(shootRangeViewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(title[position]);
            }
        }).attach();



    }

}
