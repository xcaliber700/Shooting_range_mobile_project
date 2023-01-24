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
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.shootingrangapp.merchant.MerchantHomeActivity;
import com.example.shootingrangapp.user.UserHomeActivity;
import com.example.utils.Constants;
import com.example.shootingrangapp.model.ProfileModel;
import com.example.shootingrangapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupFragment extends Fragment {

    private static final String TAG = "SignupFragment";
    ProgressDialog pd;
    TextInputEditText etName, etPassword, etConfirmPassword, etEmail;
    Button btnSignup;
    SwitchCompat switchMerchant;
    TextView txtLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait...");
        etName = view.findViewById(R.id.etName);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        etEmail = view.findViewById(R.id.etEmail);
        switchMerchant = view.findViewById(R.id.switchMerchant);
        btnSignup = view.findViewById(R.id.btnSignup);
        txtLogin = view.findViewById(R.id.txtLogin);

        txtLogin.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });
        btnSignup.setOnClickListener(v -> {
            signup();
        });

    }

    private void signup() {
        if (validate() == null) {
            pd.show();
            mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                    .addOnCompleteListener(getActivity(), (OnCompleteListener<AuthResult>) task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            createProfile(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            pd.hide();
                        }
                    });
        } else {
            Toast.makeText(getContext(), validate(), Toast.LENGTH_LONG).show();
        }
    }

    String validate() {
        if (etName.getText() == null || etName.getText().toString().isEmpty()) {
            return getString(R.string.please_enter_valid_name);
        }

        if (!Constants.isValidEmail(etEmail.getText())) {
            return getString(R.string.please_enter_valid_email);
        }


        if (etPassword.getText() == null || etPassword.getText().toString().length()<6) {
            return getString(R.string.password_should_be_6_digit);
        }

        if (etPassword.getText()==null || !etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            return getString(R.string.confirm_password_does_not_match);
        }

        return null;
    }

    void createProfile(String userId){
        ProfileModel profileModel = new ProfileModel(userId, etName.getText().toString(), etEmail.getText().toString(), switchMerchant.isChecked(),"","","");
        DocumentReference docRef = db.collection(Constants.PROFILE_COLLECTION).document(userId);
        docRef.set(profileModel).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                redirectUser();
            }
            else
                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
            pd.dismiss();
        });
    }

    void redirectUser(){
        Constants.isMerchant = switchMerchant.isChecked();
        if(switchMerchant.isChecked()) {
            Intent intent = new Intent(getActivity(), MerchantHomeActivity.class);
            getActivity().startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(getActivity(), UserHomeActivity.class);
            getActivity().startActivity(intent);
        }
        getActivity().finish();
    }
}
