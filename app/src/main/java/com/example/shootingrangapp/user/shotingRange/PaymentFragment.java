package com.example.shootingrangapp.user.shotingRange;

import static com.example.utils.Constants.DATE_FORMAT;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.shootingrangapp.R;
import com.example.shootingrangapp.model.GunModel;
import com.example.shootingrangapp.user.UserHomeFragmentDirections;
import com.example.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;


public class PaymentFragment extends Fragment {

    EditText etExpiry, etCardHolderName, etCardNumber, etCVV;
    TextView txtBookingDate,txtTotalAmount;
    Button payment;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    ProgressDialog pd;
    GunModel gunModel;
    Calendar calendar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gunModel = PaymentFragmentArgs.fromBundle(getArguments()).getGun();

        calendar = Calendar.getInstance();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait...");

        txtBookingDate = view.findViewById(R.id.txtBookingDate);
        txtTotalAmount = view.findViewById(R.id.txtTotalAmount);
        etExpiry = view.findViewById(R.id.etExpiry);
        etCardHolderName = view.findViewById(R.id.etCardHolderName);
        etCardNumber = view.findViewById(R.id.etCardNumber);
        etCVV = view.findViewById(R.id.etCVV);
        payment = view.findViewById(R.id.payment);

        setClicks();
        etExpiry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = s.toString().length();
                if (before == 0 && len == 2)
                    etExpiry.append("/");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        updateDate();
    }

    private void setClicks() {
        DatePickerDialog.OnDateSetListener dialogListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateDate();
            }
        };
        txtBookingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), R.style.datepicker,dialogListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String error = validatePaymentDetails();
                if (error == null) {
                    createBooking();
                } else {
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void updateDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        txtBookingDate.setText(simpleDateFormat.format(calendar.getTime()));
    }


    String validatePaymentDetails() {
        if (etCardHolderName.getText() == null || etCardHolderName.getText().length() == 0)
            return getString(R.string.card_holder_name_can_not_be_blank);


        if (etCardNumber.getText() == null || etCardNumber.getText().length() < 14)
            return getString(R.string.please_enter_valid_card_no);

        if (etExpiry.getText() == null || etCardNumber.getText().length() < 14)
            return getString(R.string.please_enter_valid_card_no);

        if (etExpiry.getText() == null || etExpiry.getText().length() < 5)
            return getString(R.string.please_enter_valid_expiry);

        if (etCVV.getText() == null || etCVV.getText().length() < 3)
            return getString(R.string.please_enter_valid_cvv);

        return null;
    }

    void createBooking() {
        String paymentMethod = "Card";
        DocumentReference docRef = db.collection(Constants.BOOKING_COLLECTION).document();
        String bookingId = docRef.getId();
        HashMap<String, Object> data = new HashMap<>();
        data.put("userId", mAuth.getCurrentUser().getUid());
        data.put("merchantId", gunModel.getMerchantId());
        data.put("gun", gunModel);
        data.put("paymentMethod", paymentMethod);
        data.put("bookingAmount", txtTotalAmount.getText().toString());
        data.put("bookingId", bookingId);
        data.put("bookingDate", calendar.getTime());
        docRef.set(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                NavHostFragment.findNavController(PaymentFragment.this).popBackStack(R.id.userHomeFragment, false);
                NavHostFragment.findNavController(PaymentFragment.this).navigate(
                        UserHomeFragmentDirections
                                .actionUserHomeFragmentToPaymentConfirmationFragment(bookingId));
            } else
                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
            pd.dismiss();
        });
    }

}