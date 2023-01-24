package com.example.shootingrangapp.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.shootingrangapp.model.ProfileModel;
import com.example.shootingrangapp.R;
import com.example.utils.Constants;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    EditText etName, etEmail;
    TextInputLayout layoutName, layoutEmail;
    EditText etCompanyName, etContactEmail, etContactNo, etAddress;
    TextInputLayout layoutCompanyName, layoutContactEmail, layoutContactNo, layoutAddress;
    RadioGroup rgGender;
    RadioButton rbMale, rbFemale;
    ImageView img, imgEdit;
    Button btnSave;
    TextView txtGender;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProfileModel profileModel;
    ProgressDialog pd;
    FirebaseStorage firebaseStorage;
    Uri uriProfile;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseStorage = FirebaseStorage.getInstance();
        pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait...");
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        etCompanyName = view.findViewById(R.id.etCompanyName);
        etContactEmail = view.findViewById(R.id.etCompanyEmail);
        etContactNo = view.findViewById(R.id.etTelephone);
        etAddress = view.findViewById(R.id.etAddress);
        layoutName = view.findViewById(R.id.layoutName);
        layoutEmail = view.findViewById(R.id.layoutEmail);
        layoutCompanyName = view.findViewById(R.id.layoutCompanyName);
        layoutContactEmail = view.findViewById(R.id.layoutCompanyEmail);
        layoutContactNo = view.findViewById(R.id.layoutTelephone);
        layoutAddress = view.findViewById(R.id.layoutAddress);
        rgGender = view.findViewById(R.id.rgGender);
        img = view.findViewById(R.id.imgProfile);
        imgEdit = view.findViewById(R.id.imgEdit);
        txtGender = view.findViewById(R.id.txtGender);
        btnSave = view.findViewById(R.id.btnUpdate);
        rbMale = view.findViewById(R.id.rbMale);
        rbFemale = view.findViewById(R.id.rbFemale);

        setViews();
        loadUserProfile(mAuth.getUid());
        btnSave.setOnClickListener(v -> {
            updateProfile();
        });

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(getActivity())
                        .crop(1, 1)
                        .compress(512)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });

        Glide.with(this)
                .load(Constants.getProfileRef(mAuth.getCurrentUser().getUid(), firebaseStorage))
                .circleCrop()
                .into(img);
    }

    private void setViews() {
        if (Constants.isMerchant) {
            layoutName.setVisibility(View.GONE);
            layoutEmail.setVisibility(View.GONE);
            rgGender.setVisibility(View.GONE);
            txtGender.setVisibility(View.GONE);
            layoutCompanyName.setVisibility(View.VISIBLE);
            layoutContactEmail.setVisibility(View.VISIBLE);
            layoutContactNo.setVisibility(View.VISIBLE);
            layoutAddress.setVisibility(View.VISIBLE);
        } else {
            layoutName.setVisibility(View.VISIBLE);
            layoutEmail.setVisibility(View.VISIBLE);
            txtGender.setVisibility(View.VISIBLE);
            rgGender.setVisibility(View.VISIBLE);
            layoutCompanyName.setVisibility(View.GONE);
            layoutContactEmail.setVisibility(View.GONE);
            layoutContactNo.setVisibility(View.GONE);
            layoutAddress.setVisibility(View.GONE);
        }
    }

    void updateProfile() {
        if (validate() == null) {
            pd.show();
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", profileModel.getName());
            map.put("email", profileModel.getEmail());
            map.put("telephoneNo", profileModel.getTelephoneNo());
            map.put("gender", profileModel.getGender());
            map.put("address", profileModel.getAddress());
            db.collection(Constants.PROFILE_COLLECTION).document(mAuth.getCurrentUser().getUid())
                    .update(map)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), R.string.profile_updated, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                        }
                        pd.hide();
                    });
        } else {
            Toast.makeText(getContext(), validate(), Toast.LENGTH_SHORT).show();
        }
    }

    void setProfile(ProfileModel profileModel) {
        this.profileModel = profileModel;
        setViews();
        if (Constants.isMerchant) {
            etCompanyName.setText(profileModel.getName());
            etContactEmail.setText(profileModel.getEmail());
            etContactNo.setText(profileModel.getTelephoneNo());
            etAddress.setText(profileModel.getAddress());
        } else {
            etName.setText(profileModel.getName());
            etEmail.setText(profileModel.getEmail());
            if (profileModel.getGender() != null && profileModel.getGender().equalsIgnoreCase("male"))
                rbMale.setSelected(true);
            else
                rbFemale.setSelected(true);
        }
    }

    void loadUserProfile(String userId) {
        pd.show();
        db.collection(Constants.PROFILE_COLLECTION).document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ProfileModel profileModel = task.getResult().toObject(ProfileModel.class);
                        Constants.isMerchant = profileModel.isMerchant();
                        setProfile(profileModel);
                    } else {
                        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    }
                    pd.hide();
                });
    }

    String validate() {
        if (Constants.isMerchant) {
            if (etCompanyName.getText() == null || etCompanyName.getText().toString().isEmpty()) {
                return getString(R.string.please_enter_valid_name);
            }

            if (!Constants.isValidEmail(etContactEmail.getText())) {
                return getString(R.string.please_enter_valid_email);
            }

            if (etContactNo.getText() == null || etContactNo.getText().toString().length() < 10) {
                return getString(R.string.please_enter_valid_contact_no);
            }

            if (etAddress.getText() == null || etAddress.getText().toString().isEmpty()) {
                return getString(R.string.please_enter_valid_address);
            }
            profileModel.setName(etCompanyName.getText().toString());
            profileModel.setEmail(etContactEmail.getText().toString());
            profileModel.setTelephoneNo(etContactNo.getText().toString());
            profileModel.setAddress(etAddress.getText().toString());
        } else {
            if (etName.getText() == null || etName.getText().toString().isEmpty()) {
                return getString(R.string.please_enter_valid_name);
            }

            if (!Constants.isValidEmail(etEmail.getText())) {
                return getString(R.string.please_enter_valid_email);
            }
            String gender;
            if (rbFemale.isChecked())
                gender = "male";
            else
                gender = "female";
            profileModel.setName(etName.getText().toString());
            profileModel.setEmail(etEmail.getText().toString());
            profileModel.setGender(gender);
        }
        return null;
    }


    void uploadImage(String uid) {
        pd.show();
        StorageReference profileRef = firebaseStorage.getReference(Constants.PROFILE_IMAGE_PATH).child(uid + ".jpg");
        profileRef.putFile(uriProfile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful() && task.getResult().getError() == null) {
                    Toast.makeText(getContext(), R.string.picture_changed_successfully, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                }
                pd.hide();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            uriProfile = data.getData();
            Glide.with(this).load(uriProfile).circleCrop().into(img);
            uploadImage(mAuth.getCurrentUser().getUid());
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}
