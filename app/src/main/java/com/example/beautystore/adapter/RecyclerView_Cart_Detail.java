package com.example.beautystore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beautystore.R;
import com.example.beautystore.activity.Activity_Cart_Detail;
import com.example.beautystore.model.CartDetail;
import com.example.beautystore.model.Rating;

import java.util.ArrayList;

public class RecyclerView_Cart_Detail extends RecyclerView.Adapter<RecyclerView_Cart_Detail.CartDetailViewHolder> {
    private ArrayList<CartDetail> data;
    private Activity_Cart_Detail context;

    public RecyclerView_Cart_Detail(ArrayList<CartDetail> data, Activity_Cart_Detail context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public CartDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View cartDetailView = inflater.inflate(R.layout.layout_item_cart_detail, parent, false);
        return new CartDetailViewHolder(cartDetailView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartDetailViewHolder holder, int position) {
        CartDetail cartDetail = data.get(position);
        Glide.with(context)
                .load(R.drawable.abc)
                .into(holder.ivProductImage); //Fix replace image with image in firebase
        holder.tvProductName.setText(cartDetail.getProduct_id()); //Fix replace product name with firebase
        holder.tvProductPrice.setText(cartDetail.getProduct_id()); //Fix replace product price with Firebase
        holder.tvProductQty.setText(cartDetail.getQty());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class CartDetailViewHolder extends RecyclerView.ViewHolder{

ImageView ivProductImage;
TextView tvProductName;
TextView tvProductPrice;
ImageView ivClearBtn;
ImageView ivDeceaseQty;
TextView tvProductQty;
ImageView ivIncreaseQty;

        public CartDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivCartDetailProductImage);
            tvProductName = itemView.findViewById(R.id.tvCartDetailProductName);
            tvProductPrice = itemView.findViewById(R.id.tvCartDetailProductPrice);
            ivClearBtn = itemView.findViewById(R.id.ivCartDetailClearBtn);
            ivDeceaseQty = itemView.findViewById(R.id.ivCartDetailDecreaseQty);
            ivIncreaseQty = itemView.findViewById(R.id.ivCartDetailIncreaseQty);
            tvProductQty = itemView.findViewById(R.id.tvCartDetailProductQty);
        }
    }
}
