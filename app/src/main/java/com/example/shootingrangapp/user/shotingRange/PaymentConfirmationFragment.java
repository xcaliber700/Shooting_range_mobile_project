package com.example.shootingrangapp.user.shotingRange;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.shootingrangapp.R;
import com.example.utils.Constants;

public class PaymentConfirmationFragment extends Fragment {

    private static final String TAG = "PaymentConfirmationFragment";

    TextView txtOrderId;
    ImageView btnClose;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_confirmed, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtOrderId = view.findViewById(R.id.txtTitle);
        btnClose = view.findViewById(R.id.imgClose);
        String orderId = PaymentConfirmationFragmentArgs.fromBundle(getArguments()).getOrderId();
        txtOrderId.setText(getString(R.string.order_confirmation,orderId));

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment
                        .findNavController(PaymentConfirmationFragment.this)
                        .popBackStack();
            }
        });
    }

}
