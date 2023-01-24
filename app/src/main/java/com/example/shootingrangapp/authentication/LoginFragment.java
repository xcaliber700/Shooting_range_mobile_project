package com.example.shootingrangapp.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.shootingrangapp.model.ProfileModel;
import com.example.shootingrangapp.R;
import com.example.shootingrangapp.merchant.MerchantHomeActivity;
import com.example.shootingrangapp.user.UserHomeActivity;
import com.example.utils.Constants;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class    LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    ProgressDialog pd;
    TextInputEditText etPassword, etEmail;
    Button btnLogin;
    TextView txtSignup;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait...");
        etPassword = view.findViewById(R.id.etPassword);
        etEmail = view.findViewById(R.id.etEmail);
        btnLogin = view.findViewById(R.id.btnLogin);
        txtSignup = view.findViewById(R.id.txtSignup);

        if (mAuth.getCurrentUser() != null)
            checkUserType(mAuth.getCurrentUser().getUid());

        txtSignup.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.signupFragment);
        });

        btnLogin.setOnClickListener(v -> {
            if (validate() == null) {
                login();

            } else {
                Toast.makeText(getContext(), validate(), Toast.LENGTH_LONG).show();
            }
        });

    }

    String validate() {
        if (!Constants.isValidEmail(etEmail.getText())) {
            return getString(R.string.please_enter_valid_email);
        }

        if (etPassword.getText() == null || etPassword.getText().toString().length() < 6) {
            return getString(R.string.password_should_be_6_digit);
        }
        return null;
    }

    void login() {
        pd.show();
        mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "sign in:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        checkUserType(user.getUid());
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        pd.hide();
                    }
                });
    }

    void checkUserType(String userId) {
        db.collection(Constants.PROFILE_COLLECTION).document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ProfileModel profileModel = task.getResult().toObject(ProfileModel.class);
                        redirectUser(profileModel.isMerchant());
                    } else {
                        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    }
                    pd.hide();
                });
    }

    void redirectUser(boolean merchant) {
        Constants.isMerchant = merchant;
        if (merchant) {
            Intent intent = new Intent(getActivity(), MerchantHomeActivity.class);
            getActivity().startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), UserHomeActivity.class);
            getActivity().startActivity(intent);
        }
        getActivity().finish();
    }

}
