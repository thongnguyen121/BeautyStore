package com.example.beautystore.adapter;

import android.content.Context;
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
import com.example.beautystore.activity.Activity_Wish_List;
import com.example.beautystore.model.CartDetail;
import com.example.beautystore.model.WishList;

import java.util.ArrayList;

public class RecyclerView_WishList extends RecyclerView.Adapter<RecyclerView_WishList.WishListViewHolder> {

    private ArrayList<WishList> data;
    private Context context;

    public RecyclerView_WishList(ArrayList<WishList> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public WishListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View wishListView = inflater.inflate(R.layout.layout_item_wish_list, parent, false);
        return new WishListViewHolder(wishListView);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListViewHolder holder, int position) {
        WishList wishList = data.get(position);
        Glide.with(context)
                .load(R.drawable.abc)
                .into(holder.ivProductImage); //Fix replace image with image in firebase

        holder.tvProductName.setText(wishList.getProduct_id()); //Fix replace product name with firebase
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class WishListViewHolder extends RecyclerView.ViewHolder{

        ImageView ivProductImage;
        TextView tvProductName;
        ImageView ivAddWishList;
        public WishListViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivWishListProductImage);
            tvProductName = itemView.findViewById(R.id.tvWishListProductName);
            ivAddWishList = itemView.findViewById(R.id.ivWishListHeartButton);
        }
    }
}
