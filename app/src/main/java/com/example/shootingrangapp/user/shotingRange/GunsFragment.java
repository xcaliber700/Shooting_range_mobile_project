package com.example.shootingrangapp.user.shotingRange;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shootingrangapp.R;
import com.example.shootingrangapp.model.GunModel;
import com.example.shootingrangapp.user.ShootingRangeAdapter;
import com.example.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class GunsFragment extends Fragment implements GunsAdapter.OnItemClick {

    private static final String TAG = "GunsFragment";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseStorage firebaseStorage;
    ProgressDialog pd;
    String merchantId;
    RecyclerView rvGuns;
    GunsAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guns_ammunition, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        merchantId = getArguments().getString(Constants.MERCHANT_ID);
        firebaseStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait...");


        adapter = new GunsAdapter(firebaseStorage, true);
        adapter.setOnItemClick(this);
        rvGuns = view.findViewById(R.id.rvGuns);
        rvGuns.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGuns.setAdapter(adapter);
        loadGuns();
    }


    private void loadGuns() {
        pd.show();
        db.collection(Constants.GUNS_COLLECTION)
                .whereEqualTo("merchantId", merchantId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getException() == null) {
                    List<DocumentSnapshot> docs = task.getResult().getDocuments();
                    List<GunModel> list = new ArrayList<>();
                    for (int i = 0; i < docs.size(); i++) {
                        list.add(docs.get(i).toObject(GunModel.class));
                    }
                    adapter.addData(list);
                } else
                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                pd.hide();
            }
        });
    }

    @Override
    public void onBookNowClick(GunModel gunModel) {
        NavHostFragment.findNavController(this).navigate(ShootingRangeFragmentDirections.actionShootingFragmentToPaymentFragment(gunModel));
    }
}
