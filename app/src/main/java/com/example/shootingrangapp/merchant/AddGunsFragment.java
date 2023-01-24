package com.example.shootingrangapp.merchant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.shootingrangapp.R;
import com.example.shootingrangapp.model.GunModel;
import com.example.utils.Constants;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddGunsFragment extends Fragment {

    private static final String TAG = "AddGunsFragment";

    Button btnPickGunImage, btnAmmunitionImage, btnAddGun;
    ImageView imgGun, imgAmmunition;
    boolean isGunImage = false;
    Uri uriGun, uriAmmunition;
    FirebaseStorage firebaseStorage;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    TextInputEditText etGunName, etAmmunitionName, getEtAmmunitionPrice, getEtAmmunitionQty;
    ProgressDialog pd;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_guns, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(getContext());
        pd.setMessage("Please wait...");

        btnPickGunImage = view.findViewById(R.id.btnGunImage);
        btnAmmunitionImage = view.findViewById(R.id.btnAmmunitionImage);
        btnAddGun = view.findViewById(R.id.btnAddGun);
        imgGun = view.findViewById(R.id.imgGun);
        imgAmmunition = view.findViewById(R.id.imgAmmunition);

        etGunName = view.findViewById(R.id.etGunName);
        etAmmunitionName = view.findViewById(R.id.etAmmunition);
        getEtAmmunitionPrice = view.findViewById(R.id.etAmmunitionPrice);
        getEtAmmunitionQty = view.findViewById(R.id.etAmmunitionQty);


        btnPickGunImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGunImage = true;
                ImagePicker.with(getActivity())
                        .crop(1, 1)
                        .compress(512)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });
        btnAmmunitionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGunImage = false;
                ImagePicker.with(getActivity())
                        .crop(1, 1)
                        .compress(512)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        });


        btnAddGun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate() == null) {
                    addGun();
                } else {
                    Toast.makeText(getContext(), validate(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    void addGun() {
        String image = System.currentTimeMillis() + ".jpg";
        String gun = Constants.GUNS_IMAGE_PATH + "/" + image;
        String ammunition = Constants.AMMUNITION_IMAGE_PATH + "/" + image;

        GunModel gunModel = new GunModel(
                mAuth.getCurrentUser().getUid(),
                etAmmunitionName.getText().toString(),
                Integer.parseInt(getEtAmmunitionQty.getText().toString()),
                Float.parseFloat(getEtAmmunitionPrice.getText().toString()),
                gun,
                ammunition,
                etGunName.getText().toString());
        uploadImages(image, gunModel);
    }

    void uploadImages(String image, GunModel gunModel) {
        pd.show();
        StorageReference gunRef = firebaseStorage.getReference(Constants.GUNS_IMAGE_PATH).child(image);
        StorageReference ammunitionRef = firebaseStorage.getReference(Constants.AMMUNITION_IMAGE_PATH).child(image);
        gunRef.putFile(uriGun).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful() && task.getResult().getError() == null) {
                    ammunitionRef.putFile(uriAmmunition).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful() && task.getResult().getError() == null) {
                                addGunData(gunModel);
                            } else {
                                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                                pd.hide();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                    pd.hide();
                }
            }
        });
    }

    void addGunData(GunModel gunModel) {
        DocumentReference docRef = db.collection(Constants.GUNS_COLLECTION).document();
        docRef.set(gunModel).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), R.string.gun_added_successfully, Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).navigateUp();
            } else
                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
            pd.dismiss();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri uri = data.getData();
            if (isGunImage) {
                imgGun.setImageURI(uri);
                uriGun = uri;
            } else {
                imgAmmunition.setImageURI(uri);
                uriAmmunition = uri;
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getContext(), "Task Cancelled", Toast.LENGTH_SHORT).show();
    }


    String validate() {
        if (etGunName.getText() == null || etGunName.getText().toString().isEmpty()) {
            return getString(R.string.gun_name_can_not_blank);
        }
        if (etAmmunitionName.getText() == null || etAmmunitionName.getText().toString().isEmpty()) {
            return getString(R.string.ammunition_name_can_not_blank);
        }
        if (getEtAmmunitionPrice.getText() == null || getEtAmmunitionPrice.getText().toString().isEmpty() || Integer.parseInt(getEtAmmunitionPrice.getText().toString()) <= 0) {
            return getString(R.string.ammunition_invalid_price);
        }
        if (getEtAmmunitionQty.getText() == null || getEtAmmunitionQty.getText().toString().isEmpty() || Integer.parseInt(getEtAmmunitionQty.getText().toString()) <= 0) {
            return getString(R.string.ammunition_qty_can_not_blank);
        }
        if (uriGun == null) {
            return getString(R.string.please_select_gun_image);
        }
        if (uriAmmunition == null) {
            return getString(R.string.please_select_ammunition_image);
        }
        return null;
    }

}
