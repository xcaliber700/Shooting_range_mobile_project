package com.example.shootingrangapp.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shootingrangapp.R;
import com.example.shootingrangapp.model.BookingModel;
import com.example.shootingrangapp.model.ShootingRangeModel;
import com.example.utils.Constants;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {

    private ArrayList<BookingModel> dataList = new ArrayList<>();
    private OnItemClick onItemClick;
    FirebaseStorage storage;

    public BookingAdapter(FirebaseStorage storage) {
        this.storage = storage;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.onBind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    void addData(List<BookingModel> list) {
        dataList.clear();
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgGun;
        TextView txtBookingId,txtBookingDate,txtPaymentMethod,txtBookingAmount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGun = itemView.findViewById(R.id.imgGun);
            txtBookingId = itemView.findViewById(R.id.txtBookingId);
            txtBookingAmount = itemView.findViewById(R.id.txtBookingAmount);
            txtBookingDate = itemView.findViewById(R.id.txtBookingDate);
            txtPaymentMethod= itemView.findViewById(R.id.txtPaymentMethod);
        }


        void onBind(BookingModel model) {
            Glide.with(itemView.getContext())
                    .load(storage.getReference(model.getGun().getGunImage()))
                    .into(imgGun);
            Context context = itemView.getContext();
            txtBookingId.setText(context.getString(R.string.booking_id, model.getBookingId()));
            txtBookingAmount.setText(context.getString(R.string.booking_amount, model.getBookingAmount()));
            txtBookingDate.setText(context.getString(R.string.booking_date_, model.getBookingDate()));
            txtPaymentMethod.setText(context.getString(R.string.payment_method, model.getPaymentMethod()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClick!=null)
                    onItemClick.onAddReviewClick(model);
                }
            });
        }

    }


    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    interface OnItemClick {
        void onAddReviewClick(BookingModel model);
    }
}
