package com.example.shootingrangapp.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shootingrangapp.R;
import com.example.shootingrangapp.authentication.LoginActivity;
import com.example.shootingrangapp.model.BookingModel;
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

public class BookingsFragment extends Fragment implements BookingAdapter.OnItemClick {

    private static final String TAG = "BookingsFragment";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseStorage firebaseStorage;
    ProgressDialog pd;
    RecyclerView rvBooking;
    BookingAdapter adapter;
    boolean isMerchant;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookings, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isMerchant = BookingsFragmentArgs.fromBundle(getArguments()).getIsMerchant();
        firebaseStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait...");
        adapter = new BookingAdapter(firebaseStorage);
        adapter.setOnItemClick(this);


        rvBooking = view.findViewById(R.id.rvBookings);
        rvBooking.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBooking.setAdapter(adapter);
        loadBookings();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuProfile) {
            NavDirections directions = UserHomeFragmentDirections.actionUserHomeFragmentToProfileFragment();
            NavHostFragment.findNavController(this).navigate(directions);
            return true;
        } else if (item.getItemId() == R.id.menuLogout) {
            mAuth.signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    void loadBookings() {
        String checkKey;
        if(isMerchant)
            checkKey = "merchantId";
        else
            checkKey = "userId";
        pd.show();
        db.collection(Constants.BOOKING_COLLECTION)
                .whereEqualTo(checkKey, mAuth.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getException() == null) {
                    List<DocumentSnapshot> docs = task.getResult().getDocuments();
                    List<BookingModel> list = new ArrayList<>();
                    for (int i = 0; i < docs.size(); i++) {
                        DocumentSnapshot doc = docs.get(i);
                        list.add(doc.toObject(BookingModel.class));
                    }
                    adapter.addData(list);
                } else
                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                pd.hide();
            }
        });
    }

    @Override
    public void onAddReviewClick(BookingModel model) {

    }
}
