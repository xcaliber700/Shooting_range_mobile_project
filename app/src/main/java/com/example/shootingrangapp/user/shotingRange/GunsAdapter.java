package com.example.shootingrangapp.user.shotingRange;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shootingrangapp.R;
import com.example.shootingrangapp.model.GunModel;
import com.example.shootingrangapp.model.ShootingRangeModel;
import com.example.utils.Constants;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class GunsAdapter extends RecyclerView.Adapter<GunsAdapter.MyViewHolder> {

    private ArrayList<GunModel> dataList = new ArrayList<>();
    private OnItemClick onItemClick;
    FirebaseStorage storage;
    boolean guns;

    public GunsAdapter(FirebaseStorage storage, boolean guns) {
        this.storage = storage;
        this.guns = guns;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gun_ammunition_range, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.onBind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    void addData(List<GunModel> list) {
        dataList.clear();
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGun;
        TextView txtGunName, txtAmmunitionName, txtAmmunitionPrice, txtAmmunitionQty;
        Button btnBookNow;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGun = itemView.findViewById(R.id.imgGun);
            btnBookNow = itemView.findViewById(R.id.btnBookNow);
            txtGunName = itemView.findViewById(R.id.txtGunName);
            txtAmmunitionName = itemView.findViewById(R.id.txtAmmunitionName);
            txtAmmunitionPrice = itemView.findViewById(R.id.txtAmmunitionPrice);
            txtAmmunitionQty = itemView.findViewById(R.id.txtAmmunitionQty);
        }


        void onBind(GunModel gunModel) {
            txtGunName.setText(gunModel.getGun());
            txtAmmunitionName.setText("Ammunition : " + gunModel.getAmmunition());
            txtAmmunitionPrice.setText("Price : " + gunModel.getAmmunitionPrice());
            txtAmmunitionQty.setText("Unit : " + gunModel.getAmmunitionQty() + " Round");
            if (guns) {
                txtGunName.setVisibility(View.VISIBLE);
                btnBookNow.setVisibility(View.VISIBLE);
                Glide.with(itemView.getContext())
                        .load(storage.getReference(gunModel.getGunImage()))
                        .into(imgGun);
            } else {
                txtGunName.setVisibility(View.GONE);
                btnBookNow.setVisibility(View.GONE);
                Glide.with(itemView.getContext())
                        .load(storage.getReference(gunModel.getAmmunitionImage()))
                        .into(imgGun);
            }
            btnBookNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onBookNowClick(gunModel);
                }
            });
        }

    }


    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    interface OnItemClick {
        void onBookNowClick(GunModel gunModel);
    }
}
