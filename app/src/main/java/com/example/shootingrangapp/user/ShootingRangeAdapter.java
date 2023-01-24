package com.example.shootingrangapp.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shootingrangapp.R;
import com.example.shootingrangapp.model.ShootingRangeModel;
import com.example.utils.Constants;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class ShootingRangeAdapter extends RecyclerView.Adapter<ShootingRangeAdapter.MyViewHolder> {

    private ArrayList<ShootingRangeModel> dataList = new ArrayList<>();
    private OnItemClick onItemClick;
    FirebaseStorage storage;

    public ShootingRangeAdapter(FirebaseStorage storage) {
        this.storage = storage;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shooting_range, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.onBind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    void addData(List<ShootingRangeModel> list) {
        dataList.clear();
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLogo;
        TextView txtTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLogo = itemView.findViewById(R.id.imgStore);
            txtTitle = itemView.findViewById(R.id.txtTitle);
        }


        void onBind(ShootingRangeModel shootingRangeModel) {
            Glide.with(itemView.getContext())
                    .load(Constants.getProfileRef(shootingRangeModel.getStoreId(), storage))
                    .into(imgLogo);
            txtTitle.setText(shootingRangeModel.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onShootingRangClick(shootingRangeModel);
                }
            });
        }

    }


    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    interface OnItemClick {
        void onShootingRangClick(ShootingRangeModel shootingRangeModel);
    }
}
